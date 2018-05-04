package LOREntities;
// Generated May 4, 2018 8:34:43 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Fabricant generated by hbm2java
 */
public class Fabricant  implements java.io.Serializable {


     private Integer idFabricant;
     private String nom;
     private Set consoles = new HashSet(0);

    public Fabricant() {
    }

	
    public Fabricant(String nom) {
        this.nom = nom;
    }
    public Fabricant(String nom, Set consoles) {
       this.nom = nom;
       this.consoles = consoles;
    }
   
    public Integer getIdFabricant() {
        return this.idFabricant;
    }
    
    public void setIdFabricant(Integer idFabricant) {
        this.idFabricant = idFabricant;
    }
    public String getNom() {
        return this.nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Set getConsoles() {
        return this.consoles;
    }
    
    public void setConsoles(Set consoles) {
        this.consoles = consoles;
    }




}


