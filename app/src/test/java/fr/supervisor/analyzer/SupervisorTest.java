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
                        .name(Pattern.compile(".*Fournitures.*"))
                        .artifact(new ArtifactConfFile.Builder()
                                .fileNamePattern(Pattern.compile("CDS-LYO-FournituresClient-\\d*\\.docx"))
                                .requirementPattern(Pattern.compile("IDEA \\d{2}"))
                                .stylePattern(Pattern.compile("T.*1"))
                                .ignore(Pattern.compile("archive"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()  //Phase spec
                        .name(Pattern.compile(".*Conception.*"))
                        .artifact(new ArtifactConfFile.Builder()
                                .fileNamePattern(Pattern.compile("CDS-LYO-Conception-\\d*-DFD\\.docx"))
                                .requirementPattern(Pattern.compile("ES-.*-\\d{5}"))
                                .stylePattern(Pattern.compile("T.*1"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()  //phase conception
                        .name(Pattern.compile(".*Conception.*"))
                        .artifact(new ArtifactConfFile.Builder()
                                .fileNamePattern(Pattern.compile("CDS-LYO-Conception-\\d*-DFD\\.docx"))
                                .requirementPattern(Pattern.compile("EC-.*-\\d*"))
                                .stylePattern(Pattern.compile("T.*1"))
                                .build())
                        .build())
                
                 .phase(new PhaseConf.Builder()  //phase Dev
                        .name(Pattern.compile(".*Dev.*"))
                        .artifact(new ArtifactConfSVN.Builder()
                                .requirementPattern(Pattern.compile("EC-.*-\\d*"))
                                .build())
                        .build())
                
                 .phase(new PhaseConf.Builder()  //phase Test
                        .name(Pattern.compile(".*Dev.*"))
                        .artifact(new ArtifactConfSVN.Builder()
                                .requirementPattern(Pattern.compile("EC-.*-\\d*"))
                                .build())
                        .build())
                
                .build();
        
        
        Project project = new Project("SUPERVISOR",projectConf);

        System.out.println(project.getConf());

        Supervisor supervisor = new Supervisor(project);
        supervisor.run();
        
        System.out.println(project.toString());
    }
}
