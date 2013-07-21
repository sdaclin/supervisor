package fr.supervisor.analyzer;

import fr.supervisor.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 21/07/13
 * Time: 09:34
 */
public class Supervisor {
    Logger logger = LoggerFactory.getLogger(Supervisor.class);

    private final Project project;

    public Supervisor(Project project){
        this.project = project;
    }

    public void run(){
        // Search for version directories according projectConf version pattern and ignoring projectConf ignoreFile
        final List<Path> versionPaths = new ArrayList<>();
        try {
            Files.walkFileTree(project.getConf().getDocumentation(),new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (project.getConf().getIgnoreFile() != null && project.getConf().getIgnoreFile().matcher(dir.toFile().getName()).matches()){
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    if (project.getConf().getVersion().matcher(dir.toFile().getName()).matches()) {
                        versionPaths.add(dir);
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (versionPaths.size()==0){
            logger.info("No version found");
            return;
        }
        logger.info("{} version founded", versionPaths.size());

        // todo to be continued
    }
}
