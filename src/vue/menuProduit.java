/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.CodeBarreForm;
import bean.Form;
import bean.ProduitForm;
import controleur.Controleur;
import controleur.DonneeInvalideException;
import controleur.DonneesInsuffisantesException;
import controleur.ResultatInvalideException;
import java.awt.BorderLayout;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Home
 */
public class menuProduit extends JPanel implements Chercheur
{
    private Controleur controleur;

    private critResultat Criteres;
    private Resultat<ProduitForm> Resultats;

    /**
     * Creates new form menuProduit
     * le contructeur met en parametre un objet de type Controleur:
     * @param c qui est utilisé pour appeler dans la methode avec des objet de type ProduitForm et CodeBarreForm:
     * @see controleur.Controleur#chercher(CodeBarreForm form)
     * 
     */
    public menuProduit(Controleur c)
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

        this.Criteres = new critResultat(this.controleur, this);
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
        this.Criteres.setForm((ProduitForm) res);
    }
    
    /*
    *cette methode utilise des type generique pour manipuler des objets de type ProduitForm
    *pour efectuer l'operation de recherche d'un element existant dans la base de  donnnées
    *@param form un objet qui va contenir la valeur de la recherche introduit de l'utilisateur
    *l'objet utilis avec des methodes:
    *@see #chercher((CodeBarreForm form)
    *@see #chercher((ProduitForm form)
    */

    @Override
    public void lancerRecherche(Form form)
    {
        try {
            // Affectuer la recherche avec fonction RECHERCHE dans CONTROLEUR
            Vector<ProduitForm> resultatsRecherche = null;
            if (form instanceof CodeBarreForm)
                resultatsRecherche = this.controleur.chercher((CodeBarreForm) form);
            else if (form instanceof ProduitForm)
                resultatsRecherche = this.controleur.chercher((ProduitForm) form);
            // Afficher les résultats avec fonction AFFICHERES dans RESULTAT
            if (resultatsRecherche != null)
                this.Resultats.afficherRes(resultatsRecherche); }
        catch (DonneeInvalideException e) {
            afficherErreur(e);}
        catch (ResultatInvalideException e) {
            afficherErreur(e);} catch (DonneesInsuffisantesException ex) {
            Logger.getLogger(menuProduit.class.getName()).log(Level.SEVERE, null, ex);
        }
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
