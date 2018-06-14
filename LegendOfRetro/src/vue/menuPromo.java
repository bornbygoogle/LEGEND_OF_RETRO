/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.Form;
import bean.PromoForm;
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
public class menuPromo extends JPanel implements Chercheur
{
    private Controleur controleur;

    private critPromo Criteres;
    private Resultat<PromoForm> Resultats;

    /**
     * Creates new form menuProduit
     */
    public menuPromo(Controleur c)
    {
        super();
        this.controleur = c;
        initComponents();
    }

    /**
     * Cette méthode est appelée depuis le constructeur pour initialiser le JPanel.
     * ATTENTION : cette fonction reprend du code généré par un JForm.
     */                       
    private void initComponents()
    {
        this.setSize(500, 560);

        this.Criteres = new critPromo(this.controleur, this);
        this.Resultats = new Resultat<PromoForm>(this);
        
        this.setLayout(new BorderLayout());
        this.add(this.Criteres, BorderLayout.CENTER);
        this.add(this.Resultats, BorderLayout.SOUTH);
    }
    
    @Override
    public void selectionnerResultat(Form res)
    {
        if (!(res instanceof PromoForm))
            throw new IllegalArgumentException("Erreur dans menuProduit: le formulaire à sélectionner n'est pas un ProduitForm.");
        try { this.Criteres.setForm((PromoForm) res); 
        } catch (IOException ex) {
            Logger.getLogger(menuProduit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void lancerRecherche(Form form)
    {
        try {
            // Affectuer la recherche avec fonction RECHERCHE dans CONTROLEUR
            Vector<PromoForm> resultatsRecherche = null;
            resultatsRecherche = this.controleur.chercherPromo((PromoForm) form); //! TODO: attention, dans le contrôleur, à ce que renvoie chercherform() !
            // Afficher les résultats avec fonction AFFICHERES dans RESULTAT
            if (resultatsRecherche != null)
                this.Resultats.afficherRes(resultatsRecherche); }
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
