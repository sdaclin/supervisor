package fr.supervisor.analyzer;

import fr.supervisor.model.Project;
import fr.supervisor.model.configuration.ArtifactConfFile;
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
                        .name(Pattern.compile("fournitures client"))
                        .artifact(new ArtifactConfFile.Builder()
                                .fileNamePattern(Pattern.compile("\\.docx"))
                                .build())
                        .build())
                .phase(new PhaseConf.Builder()
                        .name(Pattern.compile("conception"))
                        .artifact(new ArtifactConfFile.Builder()
                                .fileNamePattern(Pattern.compile("CDS.*DFD\\.docx"))
                                .build())
                        .build())
                .build();
        Project project = new Project("Siclop",projectConf);

        System.out.println(project.getConf());

        Supervisor supervisor = new Supervisor(project);
        supervisor.run();
    }
}
