/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

/**
 *
 * @author Home
 */
public class FactureLigneForm extends Form
{
    private ProduitForm produit;
    private int quantite;
    private float prixLigne;
    
    public FactureLigneForm() {}
    public ProduitForm getProduit()             {return this.produit;}
    public void setProduit(ProduitForm produit) {this.produit = produit;}
    public int getQuantite()                    {return this.quantite;}
    public void setQuantite(int quantite)       {this.quantite = quantite;}
    public float getPrixLigne()                 {return this.prixLigne;}
    public void setPrixLigne(float prixLigne)   {this.prixLigne = prixLigne;}
}
