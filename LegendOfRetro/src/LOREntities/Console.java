package LOREntities;
// Generated Jun 15, 2018 7:40:31 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Console generated by hbm2java
 */
public class Console  implements java.io.Serializable {


     private Integer idConsole;
     private Fabricant fabricant;
     private String nomConsole;
     private Set versionConsoles = new HashSet(0);
     private Set versionJeus = new HashSet(0);

    public Console() {
    }

	
    public Console(Fabricant fabricant, String nomConsole) {
        this.fabricant = fabricant;
        this.nomConsole = nomConsole;
    }
    public Console(Fabricant fabricant, String nomConsole, Set versionConsoles, Set versionJeus) {
       this.fabricant = fabricant;
       this.nomConsole = nomConsole;
       this.versionConsoles = versionConsoles;
       this.versionJeus = versionJeus;
    }
   
    public Integer getIdConsole() {
        return this.idConsole;
    }
    
    public void setIdConsole(Integer idConsole) {
        this.idConsole = idConsole;
    }
    public Fabricant getFabricant() {
        return this.fabricant;
    }
    
    public void setFabricant(Fabricant fabricant) {
        this.fabricant = fabricant;
    }
    public String getNomConsole() {
        return this.nomConsole;
    }
    
    public void setNomConsole(String nomConsole) {
        this.nomConsole = nomConsole;
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


