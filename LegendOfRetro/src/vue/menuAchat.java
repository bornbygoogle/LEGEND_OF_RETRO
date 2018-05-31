/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.CodeBarreForm;
//import bean.FactureLigneForm;
import bean.Form;
import bean.PersonneForm;
import bean.ProduitForm;
import controleur.Controleur;
import controleur.DonneeInvalideException;
import controleur.DonneesInsuffisantesException;
import controleur.ResultatInvalideException;
import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Home
 */
public class menuAchat extends menuVente implements Chercheur
{
    private Controleur controleur;

    private Resultat<ProduitForm> Resultats;

    /**
     * Creates new form menuProduit
     */
    public menuAchat(Controleur c)
    {
        super(c);
    }
}
