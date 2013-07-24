package fr.supervisor.model.configuration;


import java.util.regex.Pattern;
import javax.annotation.Nullable;

/**
 * Configuration for an artifact based on a file with a name matching a pattern.
 * The Artifact can contain some requirements
 */
public class ArtifactConfFile extends ArtifactConf {
    
    private Pattern directoryPattern;
    
    private Pattern fileNamePattern;
    @Nullable
    private Pattern requirementPattern;
    
    private Pattern ignoreFile;
    
    private Pattern stylePattern;

    private ArtifactConfFile(){}

    public Pattern getName() {
        return fileNamePattern;
    }

    public static class Builder{
        private ArtifactConfFile conf = new ArtifactConfFile();
        
        public Builder directoryPattern(Pattern directoryPattern){
            conf.directoryPattern = directoryPattern;
            return this;
        }

        public Builder fileNamePattern(Pattern fileName){
            conf.fileNamePattern = fileName;
            return this;
        }
        
        public Builder ignore(Pattern ignoreFileOrDirPattern){
            conf.ignoreFile = ignoreFileOrDirPattern;
            return this;
        }
        
         public Builder stylePattern(Pattern stylePattern){
            conf.stylePattern = stylePattern;
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
    
    public Pattern getDirectoryPattern(){
        return directoryPattern;
    }
    
    public Pattern getRequirementPattern(){
        return requirementPattern;
    }
    
    @Nullable
    public Pattern getIgnoreFile() {
        return ignoreFile;
    }
    
    public Pattern getStylePattern() {
        return stylePattern;
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
                    .append("Directory pattern is set to : "+directoryPattern+"\nDocument pattern is set to " + fileNamePattern + "\n");

        if (requirementPattern != null){
            sb.append(tabs).append("No requirements will be searched\n");
        }else{
            sb.append(tabs).append("Requirement pattern is set to "+requirementPattern+"\n");
        }
        
        if (stylePattern != null){
            sb.append(tabs).append("No style pattern indicated\n");
        }else{
            sb.append(tabs).append("MS Word style pattern is set to "+stylePattern+"\n");
        }

        return sb.toString();
    }
}