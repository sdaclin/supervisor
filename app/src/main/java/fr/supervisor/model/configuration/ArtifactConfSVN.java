package fr.supervisor.model.configuration;

import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:38
 */
public class ArtifactConfSVN extends ArtifactConf {

    private Pattern requirementPattern;
    

    private ArtifactConfSVN(){}

    public Pattern getRequirementPattern() {
        return requirementPattern;
    }

    public static class Builder{
        private ArtifactConfSVN conf = new ArtifactConfSVN();

        public Builder requirementPattern(Pattern requirement){
            conf.requirementPattern = requirement;
            return this;
        }

        public ArtifactConfSVN build(){
            if (conf.requirementPattern == null){
                throw new IllegalStateException("Requirement pattern mandatory");
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
