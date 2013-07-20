package fr.supervisor;

import fr.supervisor.model.Project;
import fr.supervisor.model.configuration.ArtifactConf;
import fr.supervisor.model.configuration.ArtifactWordConf;
import fr.supervisor.model.configuration.PhaseConf;
import fr.supervisor.model.configuration.ProjectConf;
import static org.junit.Assert.*;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:48
 */
public class Supervisor {
    @Test
    public void testSupervisor() throws MalformedURLException {
        ProjectConf projectConf = new ProjectConf.Builder()
            .path(Paths.get("c:/siclop"))
            .svn(new URL("http://svn.tpas.astek.fr/siclop/tags"))
            .versionPattern(Pattern.compile("(G[\\d]+)(R[\\d]+)*(C[\\d]+)*.*"))
            .phase(new PhaseConf.Builder()
                    .name(Pattern.compile("fournitures client"))
                    .artifact(new ArtifactWordConf(Pattern.compile("\\.docx"), null))
                    .build())
            .phase(new PhaseConf.Builder()
                    .name(Pattern.compile("conception"))
                    .artifact(new ArtifactWordConf(Pattern.compile("CDS.*DFD\\.docx"), null))
                    .build())
            .build();
        Project project = new Project("Siclop",projectConf);

        System.out.println(project.getConf());

        System.out.println(project);
    }
}
