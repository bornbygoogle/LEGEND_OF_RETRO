package LOREntities;
// Generated 7 juin 2018 17:49:03 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Pays generated by hbm2java
 */
public class Pays  implements java.io.Serializable {


     private Integer idPays;
     private String nomPays;
     private Set villes = new HashSet(0);

    public Pays() {
    }

	
    public Pays(String nomPays) {
        this.nomPays = nomPays;
    }
    public Pays(String nomPays, Set villes) {
       this.nomPays = nomPays;
       this.villes = villes;
    }
   
    public Integer getIdPays() {
        return this.idPays;
    }
    
    public void setIdPays(Integer idPays) {
        this.idPays = idPays;
    }
    public String getNomPays() {
        return this.nomPays;
    }
    
    public void setNomPays(String nomPays) {
        this.nomPays = nomPays;
    }
    public Set getVilles() {
        return this.villes;
    }
    
    public void setVilles(Set villes) {
        this.villes = villes;
    }




}


