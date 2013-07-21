package fr.supervisor.model.configuration;

import static com.google.common.base.Preconditions.*;
import com.sun.istack.internal.Nullable;

import java.util.regex.Pattern;

/**
 * Configuration for an artifact based on a file with a name matching a pattern.
 * The Artifact can contain some requirements
 */
public class ArtifactConfFile extends ArtifactConf {
    private Pattern fileNamePattern;
    @Nullable
    private Pattern requirementPattern;

    private ArtifactConfFile(){}

    public static class Builder{
        private ArtifactConfFile conf = new ArtifactConfFile();

        public Builder fileNamePattern(Pattern fileName){
            conf.fileNamePattern = fileName;
            return this;
        }

        public Builder requirementPattern(Pattern requirement){
            conf.requirementPattern = requirement;
            return this;
        }

        public ArtifactConfFile build(){
            if (conf.fileNamePattern == null){
                throw new IllegalStateException("File name mandatory");
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
                    .append("Document pattern is set to " + fileNamePattern + "\n");

        if (requirementPattern != null){
            sb.append(tabs).append("No requirements will be searched\n");
        }else{
            sb.append(tabs).append("Requirement pattern is set to "+requirementPattern+"\n");
        }

        return sb.toString();
    }
}