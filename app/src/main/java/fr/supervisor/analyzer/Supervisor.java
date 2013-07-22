package fr.supervisor.analyzer;

import fr.supervisor.model.Artifact;
import fr.supervisor.model.Phase;
import fr.supervisor.model.Project;
import fr.supervisor.model.Version;
import fr.supervisor.model.configuration.ArtifactConf;
import fr.supervisor.model.configuration.ArtifactConfFile;
import fr.supervisor.model.configuration.PhaseConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
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
    public void findVersions() throws IOException{

           List<Path> versionPaths =  findElements(project.getConf().getDocumentation(),project.getConf().getVersion(),project.getConf().getIgnoreFile());
           for(Path path : versionPaths){     
               Version version = new Version(path);
               project.addVersion(version,new Date());
               //find phases for the current version
               findPhasesByVersion(version);
           }  
    }
    
    
    /**
     * Search for phase directories inside the already found version directories
     */
    public void findPhasesByVersion(Version version) throws IOException{
        
            for(PhaseConf phaseConf : project.getConf().getPhaseConfs()){
               
                List<Path> phasePaths = findElements(version.getPath(),phaseConf.getName(),null);
                for(Path path : phasePaths){
                    Phase phase = new Phase(path,phaseConf);
                    version.addPhase(phase);
                    findArtifactsByPhase(phase);
                }
            }
    }

    /**
     * Search for artificats in each phase directory
     */
    public void findArtifactsByPhase(Phase phase) throws IOException{
        
        //for each artifact configuration of this phase
        for(ArtifactConf artConf : phase.getConf().getArtifactConfs()){
            List<Path> artifactsList = findFile(phase.getPath(),((ArtifactConfFile)artConf).getName(),null);
            for(Path path : artifactsList){
                phase.addArtifact(new Artifact(path));
            }
        }
    }
    
    public void run(){
        // Search for version directories according projectConf version pattern and ignoring projectConf ignoreFile
        try {
            findVersions();
            if (project.getVersions().size()==0){
                logger.info("No version found");
                return;
            }
            else{
                logger.info("{} version found", project.getVersions().size());           
            }
    
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
