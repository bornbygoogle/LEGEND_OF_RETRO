/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.FactureLigneForm;
import controleur.Controleur;

/**
 *
 * @author Home
 */
public class menuAchat extends menuVente
{
    /**
     * Creates new form menuProduit
     */
    public menuAchat(Controleur c)
    {
        super(c);
        
        //TODO: vérifier sérialisation
    }
    
    @Override
    public boolean ajoutLigneLegal(FactureLigneForm ligne)
    {
        return true; //On peut toujours acheter, il n'y a pas de condition sur la quantité.
    }
    @Override
    protected void traiterEchecRecherche(String codeBarre) {
        ;//TODO !
    }
}
