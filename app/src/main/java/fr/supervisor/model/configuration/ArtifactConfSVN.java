package fr.supervisor.model.configuration;

import java.net.URL;
import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:38
 */
public class ArtifactConfSVN extends ArtifactConf {

    private Pattern requirementPattern;
    
    private String user;
    private String password;
    
    private URL url;

    private ArtifactConfSVN(){}

    public Pattern getRequirementPattern() {
        return requirementPattern;
    }

    public URL getUrl() {
        return url;
    }
    
    public String getUser() {
        return user;
    }
     
    public String getPassword() {
        return password;
    }
    
    public static class Builder{
        private ArtifactConfSVN conf = new ArtifactConfSVN();

        public Builder user(String user){
            conf.user = user;
            return this;
        }
        
        public Builder password(String password){
            conf.password = password;
            return this;
        }
        
        public Builder url(URL url){
            conf.url = url;
            return this;
        }
        
        public Builder requirementPattern(Pattern requirement){
            conf.requirementPattern = requirement;
            return this;
        }

        public ArtifactConfSVN build(){
            if (conf.requirementPattern == null){
                throw new IllegalStateException("Requirement pattern mandatory");
            }
            if (conf.url == null){
                throw new IllegalStateException("SVN url mandatory");
            }
            if (conf.user == null){
                throw new IllegalStateException("SVN user mandatory");
            }
            return conf;
        }
    }
   
    @Override
    public String toString() {
        return toStringTabbed(0);
    }

    public String toStringTabbed(int level){
        // 1337 M0D3
        String tabs = new String(new char[level]).replace("\0", "\t");

        StringBuilder sb =
            new StringBuilder(tabs)
                    .append("Requirement pattern is set to " + requirementPattern + "\n");

        return sb.toString();
    }
}
