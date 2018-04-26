package LOREntities;
// Generated 19 avr. 2018 21:09:00 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Editeur generated by hbm2java
 */
public class Editeur  implements java.io.Serializable {

     private Integer idEditeur;
     private String nom;
     private Set jeus = new HashSet(0);

    public Editeur() {
    }

	
    public Editeur(String nom) {
        this.nom = nom;
    }
    public Editeur(String nom, Set jeus) {
       this.nom = nom;
       this.jeus = jeus;
    }
   
    public Integer getIdEditeur() {
        return this.idEditeur;
    }
    
    public void setIdEditeur(Integer idEditeur) {
        this.idEditeur = idEditeur;
    }
    public String getNom() {
        return this.nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Set getJeus() {
        return this.jeus;
    }
    
    public void setJeus(Set jeus) {
        this.jeus = jeus;
    }




}

