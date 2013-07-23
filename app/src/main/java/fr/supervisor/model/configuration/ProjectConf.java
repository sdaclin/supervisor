package fr.supervisor.model.configuration;

import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:36
 */
public class ProjectConf {
    /**
     * Physical path where the documentation is stored
     */
    private Path documentation;

    /**
     * Pattern for dir or file to ignore
     */
    private Pattern ignoreFile;

    /**
     * SVN URL of the project tag dir
     */
    private URL svn;

    /**
     * Pattern to designate a version
     *  ex :    G00R00C00
     *          v1.3
     *          1.0
     */
    private Pattern version;

    /**
     * Project's phase configuration
     * Order is set by builder
     */
    private Set<PhaseConf> phaseConfs = new LinkedHashSet<>();

    private ProjectConf(){}

    public static class Builder {
        private ProjectConf conf = new ProjectConf();

        public Builder path(Path documentationPath){
            conf.documentation = documentationPath;
            return this;
        }

        public Builder ignore(Pattern ignoreFileOrDirPattern){
            conf.ignoreFile = ignoreFileOrDirPattern;
            return this;
        }

        public Builder svn(URL svnUrl){
            conf.svn = svnUrl;
            return this;
        }

        public Builder version(Pattern version){
            conf.version = version;
            return this;
        }

        public Builder phase(PhaseConf phaseConf){
            conf.phaseConfs.add(phaseConf);
            return this;
        }

        public ProjectConf build(){
            if (conf.svn == null){
                throw new IllegalStateException("SVN must be configured");
            }
            if (conf.documentation == null){
                throw new IllegalStateException("Documentation path must be configured");
            }
            if (conf.version == null){
                throw new IllegalStateException("Version glob must be configured");
            }
            if (conf.phaseConfs.size() == 0){
                throw new IllegalStateException("A phase must be defined");
            }
            return conf;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Documentation dir is set to " + documentation + "\n");
        sb.append("SVN path is set to " + svn + "\n");
        sb.append("Version pattern is set to " + version + "\n");
        sb.append("Project's phases :\n");
        for (PhaseConf phaseConf : phaseConfs){
            sb.append(phaseConf.toStringTabbed(1));
        }
        return sb.toString();
    }

    public Path getDocumentation() {
        return documentation;
    }

    @Nullable
    public Pattern getIgnoreFile() {
        return ignoreFile;
    }

    public URL getSvn() {
        return svn;
    }

    public Pattern getVersion() {
        return version;
    }

    public Set<PhaseConf> getPhaseConfs() {
        return new LinkedHashSet<>(phaseConfs);
    }
}
