package LOREntities;
// Generated Jun 15, 2018 7:40:31 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Zone generated by hbm2java
 */
public class Zone  implements java.io.Serializable {


     private Integer idZone;
     private String nomZone;
     private Set versionConsoles = new HashSet(0);
     private Set versionJeus = new HashSet(0);

    public Zone() {
    }

	
    public Zone(String nomZone) {
        this.nomZone = nomZone;
    }
    public Zone(String nomZone, Set versionConsoles, Set versionJeus) {
       this.nomZone = nomZone;
       this.versionConsoles = versionConsoles;
       this.versionJeus = versionJeus;
    }
   
    public Integer getIdZone() {
        return this.idZone;
    }
    
    public void setIdZone(Integer idZone) {
        this.idZone = idZone;
    }
    public String getNomZone() {
        return this.nomZone;
    }
    
    public void setNomZone(String nomZone) {
        this.nomZone = nomZone;
    }
    public Set getVersionConsoles() {
        return this.versionConsoles;
    }
    
    public void setVersionConsoles(Set versionConsoles) {
        this.versionConsoles = versionConsoles;
    }
    public Set getVersionJeus() {
        return this.versionJeus;
    }
    
    public void setVersionJeus(Set versionJeus) {
        this.versionJeus = versionJeus;
    }




}


