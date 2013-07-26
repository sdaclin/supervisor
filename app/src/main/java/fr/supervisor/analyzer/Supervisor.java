package fr.supervisor.analyzer;

import fr.supervisor.model.Artifact;
import fr.supervisor.model.Phase;
import fr.supervisor.model.Project;
import fr.supervisor.model.Requirement;
import fr.supervisor.model.Version;
import fr.supervisor.model.configuration.ArtifactConf;
import fr.supervisor.model.configuration.ArtifactConfFile;
import fr.supervisor.model.configuration.ArtifactConfSVN;
import fr.supervisor.model.configuration.PhaseConf;
import fr.supervisor.tool.extractor.SVNExtractor;
import fr.supervisor.tool.extractor.WordExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 21/07/13
 * Time: 09:34
 */
public class Supervisor {
    Logger logger = LoggerFactory.getLogger(Supervisor.class);

    private final Project project;

    public Supervisor(Project project){
        this.project = project;
    }


    /**
     * Retrieve elements (version, phase...)in the directory (at any depth) indicated by srcPath, with name matching the pattern toMatch
     * And excluding files that match toIgnore
     * @param srcPath starting directory for this search
     * @param toMatch pattern to select elements
     * @param toIgnore pattern to exclude elements
     * @return a Path list
     * @throws IOException
     */
    public List<Path> findElements(Path srcPath, final Pattern toMatch, final Pattern toIgnore) throws IOException {

        final List<Path> pathList = new ArrayList<Path>();
        Files.walkFileTree(srcPath,new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (toIgnore != null && toIgnore.matcher(dir.toFile().getName()).matches()){
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (toMatch.matcher(dir.toFile().getName()).matches()) {

                    pathList.add(dir);
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return pathList;
    }

    /**
     * Search files
     * @param srcPath starting directory
     * @param toMatch pattern to select elements
     * @param toIgnore pattern to reject elements
     * @return a Path list
     * @throws IOException
     */
    public List<Path> findFile(Path srcPath,final Pattern toMatch, final Pattern toIgnore) throws IOException {

        final List<Path> pathList = new ArrayList<Path>();
        Files.walkFileTree(srcPath,new SimpleFileVisitor<Path>(){

            //Skip a directory if its name matches toIgnore pattern
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (toIgnore != null && toIgnore.matcher(dir.toFile().getName()).matches()){
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file,final BasicFileAttributes attrs) throws IOException {

                final String nom = file.getFileName().toString();
                //ignore hidden files
                if( ! file.toFile().isHidden() && toMatch.matcher(nom).matches())
                    pathList.add(file);
                return FileVisitResult.CONTINUE;
            }
        });
        return pathList;
    }
    /*
     * Search for version directories
     */
    public void findVersions() throws IOException, ParseException{

           List<Path> versionPaths =  findElements(project.getConf().getDocumentation(),project.getConf().getVersion(),project.getConf().getIgnoreFile());
           for(Path path : versionPaths){             
               Version version = new Version(path);
               project.addVersion(version,new Date());
               //find phases for the current version
               findPhasesByVersion(version);
           }
    }



    public void findPhasesByVersion(Version version) throws IOException{

            for(PhaseConf phaseConf : project.getConf().getPhaseConfs()){

                Phase phase = new Phase(phaseConf.getName(),phaseConf);
                version.addPhase(phase);
                findArtifactsByPhase(phase,version);
            }
    }

    /**
     * Search for artificats in each version directory if the artifact is a File Artifact
     * Otherwise, start the svn extractor for this phase
     */

    public void findArtifactsByPhase(Phase phase,Version version) throws IOException{

        //for each artifact configuration of this phase
        for(ArtifactConf artConf : phase.getConf().getArtifactConfs()){

            if(artConf instanceof ArtifactConfFile){

                ArtifactConfFile currentConf = (ArtifactConfFile)artConf;

                //find directories that match the directory pattern for the artifacts
                List<Path> directoryPaths = findElements(version.getPath(),currentConf.getDirectoryPattern(),project.getConf().getIgnoreFile());

                for(Path directoryPath : directoryPaths){

                    //find artifacts in the current directory
                    List<Path> artifactsList = findFile(directoryPath,currentConf.getName(),project.getConf().getIgnoreFile());

                    //for each artifact found, create an artifact and add it to the current phase
                    for(Path artifactPath : artifactsList){
                        Artifact newArtifact = new Artifact(artifactPath,artConf);
                        
                        //artifacts get the same tag list as their phase
                        newArtifact.setTags(phase.getConf().getTags());
                        
                        phase.addArtifact(newArtifact);
                        //extract the requirements from this artifact if the pattern is set
                        if (currentConf.getRequirementPattern() != null){
                            if (! artifactPath.toFile().getName().endsWith("docx")){
                                logger.warn("Un pattern est défini pour trouver des Requirements dans l'artifact {} mais celui-ci n'est pas au format docx", artifactPath);
                                continue;
                            }
                            List<Requirement> requirements = WordExtractor.extractRequirements(currentConf.getRequirementPattern(),currentConf.getStylePattern(), artifactPath.toFile(),version.getRootRequirement());
                             //requirements get the same tag list as their phase
                            for(Requirement requirement : requirements){
                               requirement.addAllTag(phase.getConf().getTags());
                            }
                            newArtifact.setRequirements(requirements);
                        }
                    }
                }
            }
            else if(artConf instanceof ArtifactConfSVN){

                //the configuration indicates a SVN artifact
                ArtifactConfSVN currentConf = (ArtifactConfSVN)artConf;
                Artifact svnArtifact = new Artifact(Paths.get(currentConf.getUrl().getPath()),currentConf);
                //artifacts get the same tag list as their phase
                svnArtifact.setTags(phase.getConf().getTags());

                //compute the correct pattern, including the current version
                Pattern svnReqPattern = Pattern.compile(currentConf.getRequirementPattern().pattern().replace("(version)",version.getVersion()));

                //extract requirements from the svn commit log
                List<Requirement> requirements =(List<Requirement>)SVNExtractor.extractRequirements(svnReqPattern, currentConf.getUrl(), currentConf.getUser(), currentConf.getPassword(), 0, -1, version.getRootRequirement());
                 //requirements get the same tag list as their phase
                for(Requirement requirement : requirements){
                   requirement.addAllTag(phase.getConf().getTags());
                }
                svnArtifact.setRequirements(requirements);
                phase.addArtifact(svnArtifact);
            }
            else{
                throw new IllegalStateException("L'artifact ne correspond a aucun type connu");
            }
        }
    }

    public void run(){
        // Search for version directories according projectConf version pattern and ignoring projectConf ignoreFile
        try {
            findVersions();
            if (project.getVersions().isEmpty()){
                logger.info("No version found");
                return;
            }
            else{
                logger.info("{} version found", project.getVersions().size());
            }

            for(Version version : project.getVersions()){
                logger.info("Version {} : {}",version.getName(),version.getRootRequirement().toString());
            }
               
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
    }
}
