package fr.supervisor.model.configuration;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:37
 */
public class PhaseConf {
    /**
     * Pattern representing name of the phase to discover in document repository
     * ex : .*customer artifact.* matches "01 - customer artifact" folder
     */
    private Pattern name;

    /**
     * List of configuration matching this phase conf
     */
    private Set<ArtifactConf> artifactConfs = new HashSet<>();

    private PhaseConf(){}

    public static class Builder {
        private PhaseConf conf = new PhaseConf();

        public Builder name(Pattern name){
            conf.name = name;
            return this;
        }

        public Builder artifact(ArtifactConf artifactConf){
            conf.artifactConfs.add(artifactConf);
            return this;
        }

        public PhaseConf build(){
            if (conf.name == null){
                throw new IllegalStateException("Name pattern must be set");
            }
            return conf;
        }
    }

    public Pattern getName(){
        return name;
    }
    
    public Set<ArtifactConf> getArtifactConfs(){
        return artifactConfs;
    }
    
    @Override
    public String toString() {
        return toStringTabbed(0);
    }

    public String toStringTabbed(int level){
        // 1337 M0D3
        String tabs = new String(new char[level]).replace("\0", "\t");

        StringBuilder sb = new StringBuilder(tabs).append("Phase name pattern is set to " + name + "\n");
        sb.append(tabs).append("Artifact conf phases :\n");
        for (ArtifactConf artifactConf : artifactConfs){
            sb.append(artifactConf.toStringTabbed(level+1));
        }
        return sb.toString();
    }
}