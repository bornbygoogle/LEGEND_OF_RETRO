/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.Form;
import bean.ProduitForm;
import controleur.Controleur;
import controleur.DonneeInvalideException;
import controleur.ResultatInvalideException;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Home
 */
public class menuProduit extends JPanel implements Chercheur
{

    private int largueur;
    private int longueur;    

    private Controleur controleur;

    private critProduit Criteres;
    private Resultat<ProduitForm> Resultats;

    /**
     * Creates new form menuProduit
     * @param c x
     */
    public menuProduit(Controleur c)
    {
        super();
        this.controleur = c;
        initComponents();
    }
    public menuProduit(Controleur c, String cbInitial)
    {
        super();
        this.controleur = c;
        initComponents();
        this.Criteres.setCodeBarre(cbInitial);
    }

    /**
     * Cette méthode est appelée depuis le constructeur pour initialiser le JPanel.
     * ATTENTION : cette fonction reprend du code généré par un JForm.
     */                       
    private void initComponents()
    { 
        // définition des dimensions des menus
        largueur = 660;
        longueur = 1100;
        
        this.setSize(longueur, largueur-150);

        this.Criteres = new critProduit(this.controleur, this);
        this.Resultats = new Resultat<ProduitForm>(this);
        
        this.setLayout(new BorderLayout());
        this.add(this.Criteres, BorderLayout.CENTER);
        this.add(this.Resultats, BorderLayout.SOUTH);
    }
    
    @Override
    public void selectionnerResultat(Form res)
    {
        if (!(res instanceof ProduitForm))
            throw new IllegalArgumentException("Erreur dans menuProduit: le formulaire à sélectionner n'est pas un ProduitForm.");
        try {
            this.Criteres.setForm((ProduitForm) res);
        } catch (IOException ex) {
            Logger.getLogger(menuProduit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void lancerRecherche(Form form)
    {
        if (form == null)
        {
            this.Resultats.afficherRes(new Vector<ProduitForm>()); //affichage d'un vecteur nul (0 résultats);
            afficherLog("");
            return;
        }
        try {
            // Affectuer la recherche avec fonction RECHERCHE dans CONTROLEUR
            Vector<ProduitForm> resultatsRecherche = null;
            resultatsRecherche = this.controleur.chercher(form);
            // Afficher les résultats avec fonction AFFICHERES dans RESULTAT
            if (resultatsRecherche != null)
                this.Resultats.afficherRes(resultatsRecherche); 
        }

        catch (DonneeInvalideException e) {
            afficherErreur(e);}
        catch (controleur.DonneesInsuffisantesException e) {
            afficherErreur(e);}
        catch (ResultatInvalideException e) {
            afficherErreur(e);}
    }

    @Override
    public void afficherErreur(Exception e)
    {
        this.Resultats.afficherErreur(e.getMessage());
    }

    @Override
    public void afficherLog(String log)
    {
        this.Resultats.afficherMessage(log);
    }


}
