package LOREntities;
// Generated Jun 7, 2018 5:40:08 PM by Hibernate Tools 4.3.1



/**
 * LigneFactureJeu generated by hbm2java
 */
public class LigneFactureJeu  implements java.io.Serializable {


     private LigneFactureJeuId id;
     private Facture facture;
     private VersionJeu versionJeu;
     private int quantite;

    public LigneFactureJeu() {
    }

    public LigneFactureJeu(LigneFactureJeuId id, Facture facture, VersionJeu versionJeu, int quantite) {
       this.id = id;
       this.facture = facture;
       this.versionJeu = versionJeu;
       this.quantite = quantite;
    }
   
    public LigneFactureJeuId getId() {
        return this.id;
    }
    
    public void setId(LigneFactureJeuId id) {
        this.id = id;
    }
    public Facture getFacture() {
        return this.facture;
    }
    
    public void setFacture(Facture facture) {
        this.facture = facture;
    }
    public VersionJeu getVersionJeu() {
        return this.versionJeu;
    }
    
    public void setVersionJeu(VersionJeu versionJeu) {
        this.versionJeu = versionJeu;
    }
    public int getQuantite() {
        return this.quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }




}


