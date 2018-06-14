/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.Form;
import bean.PersonneForm;
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
public class menuPersonne extends JPanel implements Chercheur
{
    private Controleur controleur;
    private menuVente menuAppelant;

    private critPersonne Criteres;

    private Resultat<PersonneForm> Resultats;

    /**
     * Creates new form menuProduit
     * @param c x
     */
    public menuPersonne(Controleur c)
    {
        super();
        this.controleur = c;
        this.menuAppelant = null;
        initComponents();
        
		System.out.println("test liste pays " + this.controleur.listePays());
		System.out.println("test liste villes de France " + this.controleur.listeVilles("France"));
    }
	
    public menuPersonne(Controleur c, menuVente menuVente)
    {
        this(c);
        this.menuAppelant = menuVente;
        this.Criteres.selectionnerVisible(false);
    }

    /**
     * Cette méthode est appelée depuis le constructeur pour initialiser le JPanel.
     * ATTENTION : cette fonction reprend du code généré par un JForm.
     */                       
    private void initComponents()
    {
        this.setSize(500, 560);

        this.Criteres = new critPersonne(this.controleur, this);
        this.Resultats = new Resultat<PersonneForm>(this);
        
        this.setLayout(new BorderLayout());
        this.add(this.Criteres, BorderLayout.CENTER);
        this.add(this.Resultats, BorderLayout.SOUTH);
    }
    
    @Override
    public void selectionnerResultat(Form res)
    {
        if (!(res instanceof PersonneForm))
            throw new IllegalArgumentException("Erreur dans menuProduit: le formulaire à sélectionner n'est pas un ProduitForm.");
        //this.Criteres.setForm((PersonneForm) res);
    }
    @Override
    public void lancerRecherche(Form form)
    {
//        try {
//            // Affectuer la recherche avec fonction RECHERCHE dans CONTROLEUR
//            Vector<PersonneForm> resultatsRecherche = null;
//     !       resultatsRecherche = this.controleur.chercher(form); //! TODO: attention, dans le contrôleur, à ce que renvoie chercherform() !
//            // Afficher les résultats avec fonction AFFICHERES dans RESULTAT
//            if (resultatsRecherche != null)
//                this.Resultats.afficherRes(resultatsRecherche); }
//        catch (DonneeInvalideException e) {
//            afficherErreur(e);}
//        catch (controleur.DonneesInsuffisantesException e) {
//            afficherErreur(e);}
//        catch (ResultatInvalideException e) {
//            afficherErreur(e);}
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
    
    public void selectionner(Form f)
    {
        //cette méthode ne peut être appelée que si ce menu est appelé par un autre menu
        if (this.menuAppelant == null)
            throw new java.lang.Error("Erreur : on tente de sélectionner une personne alors que "
                    + "le menuPersonne n'a pas de menuAppelant.");
        if (!(f instanceof PersonneForm))
            throw new IllegalArgumentException("Erreur : on essaye de sélectionner autre chose qu'une personne.");
        
        this.menuAppelant.selectionnerPersonne((PersonneForm) f);
    }
    public void creer(PersonneForm pf)
    {
        //TODO : this.controleur.creer(pf)
System.out.println("TODO : la méthode creer(PersonneForm) dans le contrôleur./nCe message est contenu à la fin de menuPersonne./n            this.afficherLog((this.controleur.creer(pf)).toString());");
        
         /*si nous sommes dans un sous-menu (de menuVente), il faut informer
        le menu appelant de notre création/sélection*/
        if (this.menuAppelant != null)
            this.menuAppelant.selectionnerPersonne(pf);
    }

}
