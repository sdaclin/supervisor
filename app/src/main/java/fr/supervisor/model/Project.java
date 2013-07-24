package fr.supervisor.model;

import fr.supervisor.model.configuration.ProjectConf;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.TreeMap;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:32
 */
public class Project {
    private String name;
    private ProjectConf conf;

    /**
     * versions =
     *      "G4R3" ->
     *          2005/02/25 ->
     *              Version G4R3 (analysée le 25)
     *          2005/02/26 ->
     *              Version G4R3 (analysée le 26)
     *      "G4R4"
     *          2005/02/25 ->
     *              Version G4R4 (analysée le 25)
     */
    private Map<String,Map<Date,Version>> versions = new TreeMap<String,Map<Date,Version>>();

    public Project(String name, ProjectConf conf){
        this.name = name;
        this.conf = conf;
    }

    public ProjectConf getConf(){
        return conf;
    }

    public void addVersion(Version version, Date dateVersion){
        Map<Date,Version> map = new HashMap<Date,Version>();
        map.put(dateVersion, version);
        versions.put(version.getPath().getFileName().toString(),map);
    }

    /*
     * Return the list of versions
     */
    public List<Version> getVersions(){
        List list = new ArrayList<Version>();
        for(Map<Date,Version> map : versions.values()){
            list.addAll(map.values());
        }
        return list;
    }

    @Override
    public String toString(){

        StringBuilder result = new StringBuilder(this.name);
        for(String nomVersion : versions.keySet()){
            result.append("\n\t" + nomVersion);

            Map<Date,Version> currentMap = versions.get(nomVersion);
            for(Date dateVersion : currentMap.keySet()){
                result.append("\n\t\t"+dateVersion);
                //print the version
                result.append(currentMap.get(dateVersion).toStringTabbed(2));

            }
        }
        return result.toString();
    }



}
