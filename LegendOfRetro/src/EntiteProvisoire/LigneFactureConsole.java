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
public class LigneFactureConsole  implements java.io.Serializable {
    
     private LigneFactureConsoleId id;
     private int quantite;
     private Facture facture;
     private VersionConsole versionConsole;
     
     public LigneFactureConsole()       {}
     public LigneFactureConsole(LigneFactureConsoleId id, int quantite,
             Facture facture, VersionConsole versionConsole)
     {
         this.id = id;
         this.quantite = quantite;
         this.facture = facture;
         this.versionConsole = versionConsole;
     }
   
    public LigneFactureConsoleId getId() {
        return this.id;
    }
    public void setId(LigneFactureConsoleId id) {
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
    
    public VersionConsole getVersionConsole()
    {
        return this.versionConsole;
    }
    public void VersionConsole(VersionConsole versionConsole)
    {
        this.versionConsole = versionConsole;
    }
    
}
