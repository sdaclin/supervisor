package fr.supervisor.analyzer;

import fr.supervisor.model.Project;
import fr.supervisor.model.configuration.ArtifactConfFile;
import fr.supervisor.model.configuration.ArtifactConfSVN;
import fr.supervisor.model.configuration.PhaseConf;
import fr.supervisor.model.configuration.ProjectConf;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import serializer.ProjectSerializer;

/**
 * User: sdaclin
 * Date: 21/07/13
 * Time: 09:49
 */
public class SupervisorTest {
    @Test
    public void testRun() throws Exception {
        ProjectConf projectConf = new ProjectConf.Builder()
                .path(Paths.get("c:/siclop"))
                .svn(new URL("http://svn.tpas.astek.fr/siclop/tags"))
                .version(Pattern.compile("(G[\\d]+)(R[\\d]+)*(C[\\d]+)*.*"))
                .phase(new PhaseConf.Builder()
                        .name("Fournitures")
                        .artifact(new ArtifactConfFile.Builder()
                                .directoryPattern(Pattern.compile(".*Fournitures.*"))
                                .fileNamePattern(Pattern.compile("CDS-LYO-FournituresClient-\\d*\\.docx"))
                                .requirementPattern(Pattern.compile("IDEA \\d{2}"))
                                .stylePattern(Pattern.compile("T.*1"))
                                .ignore(Pattern.compile("archive"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()  //Phase spec
                        .name("Specs")
                        .artifact(new ArtifactConfFile.Builder()
                                .directoryPattern(Pattern.compile(".*Conception.*"))
                                .fileNamePattern(Pattern.compile("CDS-LYO-Conception-\\d*-DFD\\.docx"))
                                .requirementPattern(Pattern.compile("ES-.*-\\d{5}"))
                                .stylePattern(Pattern.compile("T.*1"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()  //phase conception
                        .name("Conception")
                        .tag("valide")
                        .tag("ok")
                        .artifact(new ArtifactConfFile.Builder()
                                .directoryPattern((Pattern.compile(".*Conception.*")))
                                .fileNamePattern(Pattern.compile("CDS-LYO-Conception-\\d*-DFD\\.docx"))
                                .requirementPattern(Pattern.compile("EC-.*-\\d*"))
                                .stylePattern(Pattern.compile("T.*1"))
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
                                .fileNamePattern(Pattern.compile("CDS-LYO-Test-\\d*-TST.docx"))
                                .requirementPattern(Pattern.compile("TST-SUPERVISOR-.*-\\d{5}"))
                                .stylePattern(Pattern.compile("T.*1"))
                                .build())
                        .build())
                
                .build();
        
        
        //retrieve the former project object, if it exists
        Project project = ProjectSerializer.loadProject("../web/src/main/webapp/data/data.json.js");
        //no project found
        if(project == null){
            project = new Project("SUPERVISOR",projectConf);
        }
        else{
            //project already exists -> set its configuration
            project.setConf(projectConf);
        }
        
        System.out.println(project.getConf());

        Supervisor supervisor = new Supervisor(project);
        supervisor.run();
        
        System.out.println(project.toString());
        //Save the project as a json file 
        ProjectSerializer.saveProject("../web/src/main/webapp/data/data.json.js",project);
    }
}
