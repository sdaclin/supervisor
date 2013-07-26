package fr.supervisor.tool.extractor;

import fr.supervisor.model.Requirement;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 22/07/13
 * Time: 12:40
 */
public class SVNExtractor {

    public static Collection<Requirement> extractRequirements(Pattern patternRequirement, URL svnUrl, String user, String pass, int revisionFrom, int revisionTo, Requirement rootRequirement) {
        List<Requirement> requirements = new ArrayList<>();
        SVNRepository repository = null;
        try {
            DAVRepositoryFactory.setup();
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl.toString()));

            // Auth
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(user, pass);
            repository.setAuthenticationManager(authManager);

            // Search
            Collection<SVNLogEntry> logEntries = repository.log(new String[]{""}, null, revisionFrom, revisionTo, true, false);

            // Creating artifacts for entries that match requirements
            for (SVNLogEntry log : logEntries){
                if (log.getMessage() == null)
                    continue;

                Matcher matcher = patternRequirement.matcher(log.getMessage());
                if (matcher.find()){

                    Requirement requirement = new Requirement(""+log.getRevision());
                    Set<String> tags  = TagExtractor.extractTags(log.getMessage()); 
                    
                    //discard the tags if any
                    StringBuilder comment = new StringBuilder(log.getMessage().replaceAll("\\[.*\\]", ""));

                    // List of changedPath for this commit
                    for (Object logEntry : log.getChangedPaths().values()){
                        comment.append("\n\t").append(logEntry);
                    }
                    requirement.setComment(comment.toString());

                    Requirement parent = rootRequirement.findById(matcher.group(0).trim());
                    if(parent != null){
                        parent.addChild(requirement);
                    }
                    else{
                        rootRequirement.addChild(requirement);
                    }

                    requirements.add(requirement);
                }
            }
            //System.out.println("logEntries = " + logEntries);

        } catch (SVNException svne) {
            svne.printStackTrace();
        }

        return requirements;
    }
}
