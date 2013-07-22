package fr.supervisor.tool.extractor;

import fr.supervisor.model.Requirement;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 22/07/13
 * Time: 13:00
 */
public class SVNExtractorTest {
    @Test
    public void extractRequirement(){
        try {
            for (Requirement requirement : SVNExtractor.extractRequirements(Pattern.compile("^DM\\s*[\\d]*|^G[\\d]R*[\\d]*C*[\\d]*|^IRMA\\s*[\\d]*"), new URL("http://svn.tpas.astek.fr/siclop/tags/G4R9C1"), "sonar", "sonar", 0, -1)) {
                System.out.println(requirement);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
