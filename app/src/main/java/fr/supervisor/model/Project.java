package fr.supervisor.model;

import fr.supervisor.model.configuration.ProjectConf;

import java.util.Set;
import java.util.TreeSet;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:32
 */
public class Project {
    private String name;
    private ProjectConf conf;
    private Set<Version> versions = new TreeSet<>();

    public Project(String name, ProjectConf conf){
        this.name = name;
        this.conf = conf;
    }

    public ProjectConf getConf(){
        return conf;
    }

    public String toString(){
        StringBuilder result = new StringBuilder(this.name);
        for (Version version : versions){
            result.append(version);
        }
        return result.toString();
    }
}
