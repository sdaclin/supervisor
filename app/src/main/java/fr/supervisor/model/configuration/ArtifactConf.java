package fr.supervisor.model.configuration;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:37
 *
 * A mandatory object for the project life or traceability
 * It can be a document file a scm commit
 */
public abstract class ArtifactConf {

    public String toStringTabbed(int level){
        return toString();
    }
}
