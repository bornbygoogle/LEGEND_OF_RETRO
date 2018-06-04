/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.Vector;

/**
 *
 * @author Home
 */
public class FactureForm extends Form
{
    /**
     * Indique la nature de la transaction. Vrai si la transaction est un achat, faux si la transaction est une vente.
     */
    private boolean nature;
    /**
     * Identifiant de la personne (fournisseur ou client) qui constitue l'autre partie de la transaction. Voir table Personne dans la base de données.
     */
    private int acteurId;
    private Vector<FactureLigneForm> lignes;
    private float reductions;
    
    public FactureForm()
    {
        this.nature = false;
        this.acteurId = -1;
        this.lignes = new Vector<FactureLigneForm>();
        this.reductions = 0f;
    }
    
    public boolean getNature()              {return this.nature;}
    public void setNature(boolean achat)    {this.nature = achat;}
    public int getActeurId()                {return this.acteurId;}
    public void setActeurId(int acteurId)   {this.acteurId = acteurId;}
    public Vector<FactureLigneForm> getLignes()                 {return this.lignes;}
    public void setLignes(Vector<FactureLigneForm> lignes)      {this.lignes = lignes;}
    public float getReductions()          {return this.reductions;}
    public void setReductions(float reductions)    {this.reductions = reductions;}
}
