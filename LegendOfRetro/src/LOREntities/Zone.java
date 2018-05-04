package LOREntities;
// Generated May 4, 2018 8:34:43 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Zone generated by hbm2java
 */
public class Zone  implements java.io.Serializable {


     private Integer idZone;
     private String nom;
     private Set versionConsoles = new HashSet(0);
     private Set versionJeus = new HashSet(0);

    public Zone() {
    }

	
    public Zone(String nom) {
        this.nom = nom;
    }
    public Zone(String nom, Set versionConsoles, Set versionJeus) {
       this.nom = nom;
       this.versionConsoles = versionConsoles;
       this.versionJeus = versionJeus;
    }
   
    public Integer getIdZone() {
        return this.idZone;
    }
    
    public void setIdZone(Integer idZone) {
        this.idZone = idZone;
    }
    public String getNom() {
        return this.nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
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


