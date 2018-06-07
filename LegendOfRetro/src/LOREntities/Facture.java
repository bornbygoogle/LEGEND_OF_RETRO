package LOREntities;
// Generated 7 juin 2018 17:20:44 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Facture generated by hbm2java
 */
public class Facture  implements java.io.Serializable {


     private Integer idFacture;
     private float prixTtc;
     private char typeFacture;
     private Float reduction;
     private Date dateFacture;
     private Set ligneFactureJeus = new HashSet(0);
     private Set ligneFactureConsoles = new HashSet(0);

    public Facture() {
    }

	
    public Facture(float prixTtc, char typeFacture, Date dateFacture) {
        this.prixTtc = prixTtc;
        this.typeFacture = typeFacture;
        this.dateFacture = dateFacture;
    }
    public Facture(float prixTtc, char typeFacture, Float reduction, Date dateFacture, Set ligneFactureJeus, Set ligneFactureConsoles) {
       this.prixTtc = prixTtc;
       this.typeFacture = typeFacture;
       this.reduction = reduction;
       this.dateFacture = dateFacture;
       this.ligneFactureJeus = ligneFactureJeus;
       this.ligneFactureConsoles = ligneFactureConsoles;
    }
   
    public Integer getIdFacture() {
        return this.idFacture;
    }
    
    public void setIdFacture(Integer idFacture) {
        this.idFacture = idFacture;
    }
    public float getPrixTtc() {
        return this.prixTtc;
    }
    
    public void setPrixTtc(float prixTtc) {
        this.prixTtc = prixTtc;
    }
    public char getTypeFacture() {
        return this.typeFacture;
    }
    
    public void setTypeFacture(char typeFacture) {
        this.typeFacture = typeFacture;
    }
    public Float getReduction() {
        return this.reduction;
    }
    
    public void setReduction(Float reduction) {
        this.reduction = reduction;
    }
    public Date getDateFacture() {
        return this.dateFacture;
    }
    
    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }
    public Set getLigneFactureJeus() {
        return this.ligneFactureJeus;
    }
    
    public void setLigneFactureJeus(Set ligneFactureJeus) {
        this.ligneFactureJeus = ligneFactureJeus;
    }
    public Set getLigneFactureConsoles() {
        return this.ligneFactureConsoles;
    }
    
    public void setLigneFactureConsoles(Set ligneFactureConsoles) {
        this.ligneFactureConsoles = ligneFactureConsoles;
    }




}


