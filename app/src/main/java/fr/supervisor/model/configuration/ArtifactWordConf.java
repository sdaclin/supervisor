package fr.supervisor.model.configuration;

import com.sun.istack.internal.Nullable;

import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:38
 */
public class ArtifactWordConf extends ArtifactConf {
    private Pattern documentPattern;
    @Nullable
    private Pattern requirementPattern;

    public ArtifactWordConf(Pattern documentPattern, Pattern requirementPattern){
        if (documentPattern == null){
            throw new IllegalArgumentException("Document pattern is mandatory");
        }
        this.documentPattern = documentPattern;
        this.requirementPattern = requirementPattern;
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
                    .append("Document pattern is set to " + documentPattern + "\n");

        if (requirementPattern != null){
            sb.append(tabs).append("No requirements will be searched\n");
        }else{
            sb.append(tabs).append("Requirement pattern is set to "+requirementPattern+"\n");
        }

        return sb.toString();
    }
}