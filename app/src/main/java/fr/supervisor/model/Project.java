package fr.supervisor.model;

import fr.supervisor.model.configuration.ProjectConf;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private transient ProjectConf conf;

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
    
    public String getName(){
        return name;
    }
    public ProjectConf getConf(){
        return conf;
    }
    
    public void setConf(ProjectConf projectConf) {
        conf = projectConf;
    }
    
    public void addVersion(Version version, Date dateVersion) throws ParseException{
        //retrieve the map Date->Version, index by the version name
        Map<Date,Version> map = versions.get(version.getName());
        
        if(map == null){
            //this version is not referenced yet, create the corresponding map
            Map<Date,Version> mapDateVersion = new HashMap<Date,Version>();
            mapDateVersion.put(dateVersion, version);
            versions.put(version.getName(), mapDateVersion);
        }else{
            //the version is already referenced
            //check if it has already been scanned today
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Date todayWithZeroTime =formatter.parse(formatter.format(today));
            
            if(hasVersion(version,todayWithZeroTime)){
                //this version has already been scanned today
                //dump the already existing version for this date
                map.remove(todayWithZeroTime);
            }
            //add the version at the specified date
            map.put(dateVersion, version);
        }
       
        //TODO : enregistrer les versions a une date ne comprenant pas d'heure ->plus besoin de la comparaison avec le simpledateformat
    }
    
    public boolean hasVersion(Version version, Date date) throws ParseException{
        
        Map<Date,Version> mapFound = versions.get(version.getName());
        if(mapFound == null)
            return false;
        //now check if this version was analyzed today 
        return mapFound.containsKey(date);    
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
