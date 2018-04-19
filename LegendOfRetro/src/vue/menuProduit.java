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
        this.add(new JButton("Ici"), BorderLayout.CENTER);
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
            Vector<ProduitForm> resultatsRecherche = this.controleur.chercher(form);
            
            System.out.println("Affiché à des fins de test dans menuProduit > lancerRecherche(Form)");
            resultatsRecherche.addElement(new ProduitForm(0,0,0,0,0,"jeu", "123456", "zelda", "edition", "FR", "Nintendo", "joli", "", "nintendo", 0.5f, 12));
            resultatsRecherche.addElement(new ProduitForm(0,0,0,0,0,"console", "43", "Un super nain tend dos", "gold", "FR", "Nintendo", "joli", "", "nintendo", 100000f, 1));
            resultatsRecherche.addElement(new ProduitForm(0,0,0,0,0,"jeu", "09876543", "Mario", "edition", "IT", "Sony", "moche", "", "PSX", 5000f, 1));
            
            this.Resultats.afficherRes(resultatsRecherche); }
        catch (DonneeInvalideException e) {
            afficherErreur(e);}
        catch (ResultatInvalideException e) {
            afficherErreur(e);}
        catch (DonneesInsuffisantesException e) {
            afficherErreur(e);}
    }

    @Override
    public void afficherErreur(Exception e)
    {
        this.Resultats.afficherErreur(e.getMessage());
    }


}
