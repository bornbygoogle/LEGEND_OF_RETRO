/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

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
public class menuPromo extends JPanel implements Chercheur
{
    private Controleur controleur;

    private critPromo Criteres;
    private ResultatPromo<ProduitForm> Resultats;

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
        this.Resultats = new ResultatPromo<ProduitForm>(this);
        
        this.setLayout(new BorderLayout());
        this.add(this.Criteres, BorderLayout.CENTER);
        this.add(this.Resultats, BorderLayout.SOUTH);
    }
    
    @Override
    public void selectionnerResultat(Form res)
    {
        if (!(res instanceof ProduitForm))
            throw new IllegalArgumentException("Erreur dans menuProduit: le formulaire à sélectionner n'est pas un ProduitForm.");
        this.Criteres.setForm((ProduitForm) res);
    }

    @Override
    public void lancerRecherche(Form form)
    {
        try {
            // Affectuer la recherche avec fonction RECHERCHE dans CONTROLEUR
            Vector<ProduitForm> resultatsRecherche = null;
            resultatsRecherche = this.controleur.chercher(form); //! TODO: attention, dans le contrôleur, à ce que renvoie chercherform() !
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
