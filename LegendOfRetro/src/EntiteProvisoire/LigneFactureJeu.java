/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LOREntities;

/**
 *
 * @author Home
 */
public class LigneFactureJeu  implements java.io.Serializable {
    
     private LigneFactureJeuId id;
     private int quantite;
     private Facture facture;
     private VersionJeu versionJeu;
     
     public LigneFactureJeu()       {}
     public LigneFactureJeu(LigneFactureJeuId id, int quantite,
             Facture facture, VersionJeu versionJeu)
     {
         this.id = id;
         this.quantite = quantite;
         this.facture = facture;
         this.versionJeu = versionJeu;
     }
   
    public LigneFactureJeuId getId() {
        return this.id;
    }
    public void setId(LigneFactureJeuId id) {
        this.id = id;
    }
    
    public int getQuantite()
    {
        return this.quantite;
    }
    public void setQuantite(int quantite)
    {
        this.quantite = quantite;
    }
    
    public Facture getFacture()
    {
        return this.facture;
    }
    public void setFacture(Facture facture)
    {
        this.facture = facture;
    }
    
    public VersionJeu getVersionJeu()
    {
        return this.versionJeu;
    }
    public void VersionJeu(VersionJeu versionJeu)
    {
        this.versionJeu = versionJeu;
    }
}
