package fr.supervisor.analyzer;

import fr.supervisor.model.Project;
import fr.supervisor.model.configuration.ArtifactConfFile;
import fr.supervisor.model.configuration.ArtifactConfSVN;
import fr.supervisor.model.configuration.PhaseConf;
import fr.supervisor.model.configuration.ProjectConf;
import org.junit.Test;
import serializer.ProjectSerializer;

import java.net.URL;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 21/07/13
 * Time: 09:49
 */
public class SiclopSupervisorTest {
    @Test
    public void testRun() throws Exception {
        ProjectConf projectConf = new ProjectConf.Builder()
                .path(Paths.get("C:\\bgex\\siclop"))
                .svn(new URL("http://svn.tpas.astek.fr/siclop/trunk"))
                .version(Pattern.compile("(G[\\d]+)(R[\\d]+)*(C[\\d]+)*.*"))
                .ignore(Pattern.compile("archive|validé|envoyé|old"))
                .phase(new PhaseConf.Builder()
                        .name("Fournitures client")
                        .artifact(new ArtifactConfFile.Builder()
                                .directoryPattern(Pattern.compile(".*Fournitures.*"))
                                .fileNamePattern(Pattern.compile(".*PS.*\\.doc[x]*"))
                                .requirementPattern(Pattern.compile("IDEA [\\d]+"))
                                .stylePattern(Pattern.compile("Titre[\\d]*"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()
                        .name("Chiffrage")
                        .artifact(new ArtifactConfFile.Builder()
                                .directoryPattern(Pattern.compile(".*Chiffrage.*"))
                                .fileNamePattern(Pattern.compile("CDS-[\\d]*-[\\d]*-.*\\.xls[x]*"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()
                        .name("Specifications")
                        .artifact(new ArtifactConfFile.Builder()
                                .directoryPattern(Pattern.compile(".*Conception.*"))
                                .fileNamePattern(Pattern.compile("CDS-[\\d]*-[\\d]*-.*-DFD\\.docx"))
                                .requirementPattern(Pattern.compile("ES-FT-SICLOP[-\\w]*[\\d]+"))
                                .stylePattern(Pattern.compile("[Tt].*[\\d]"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()  //phase conception
                        .name("Conception")
                        .artifact(new ArtifactConfFile.Builder()
                                .directoryPattern(Pattern.compile(".*Conception.*"))
                                .fileNamePattern(Pattern.compile("CDS-[\\d]*-[\\d]*-.*-DFD\\.docx"))
                                .requirementPattern(Pattern.compile("EC-FT-SICLOP[-\\w]*[\\d]+"))
                                .stylePattern(Pattern.compile("[Tt].*[\\d]"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()  //phase Dev
                        .name("Dev")
                        .artifact(new ArtifactConfSVN.Builder()
                                .url(new URL("http://svn.tpas.astek.fr/siclop/trunk"))
                                .user("sonar")
                                .password("sonar")
                                .requirementPattern(Pattern.compile("^EC-.*-(version)-\\d*"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()  //phase Test
                        .name("Tests")
                        .artifact(new ArtifactConfFile.Builder()
                                .directoryPattern(Pattern.compile(".*Tests.*"))
                                .fileNamePattern(Pattern.compile("CDS-[\\d]*-[\\d]*-.*-PDT\\.docx"))
                                .requirementPattern(Pattern.compile("PTV-FT-SICLOP-[GRC\\d]+-[\\d]+"))
                                .stylePattern(Pattern.compile("ASTEK[\\w]*-[\\w]*[Tt].*[\\d]*"))
                                .build())
                        .build())

                .build();


         //retrieve the former project object, if it exists
        Project project = ProjectSerializer.loadProject("web/src/main/webapp/data/data.json.js");
        //no project found
        if(project == null){
            project = new Project("Siclop",projectConf);
        }
        else{
            //project already exists -> set its configuration
            project.setConf(projectConf);
        }
        

        Supervisor supervisor = new Supervisor(project);
        supervisor.run();

        System.out.println(project.toString());

        //Save the project as a json file
        ProjectSerializer.saveProject("web/src/main/webapp/data/data.json.js", project);
    }
}
