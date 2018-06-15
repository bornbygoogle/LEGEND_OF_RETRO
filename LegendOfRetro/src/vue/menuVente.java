/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.CodeBarreForm;
import bean.FactureForm;
import bean.FactureLigneForm;
import bean.Form;
import bean.PersonneForm;
import bean.ProduitForm;
import controleur.Controleur;
import controleur.DonneeInvalideException;
import controleur.DonneesInsuffisantesException;
import controleur.ResultatInvalideException;
import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.JPanel;

/**
 *
 * @author Home
 */
public class menuVente extends JPanel implements Chercheur
{
    protected Controleur controleur;

    protected critVente selectionProduit;
    protected Resultat<FactureLigneForm> affichageFacture;
    protected menuPersonne selectionPersonne;
    FactureForm facture;

    /**
     * Creates new form menuProduit
     */
    public menuVente(Controleur c)
    {
        super();
        this.controleur = c;
        initComponents();
        this.facture = new FactureForm();
        this.facture.setNature(false);
    }

    /**
     * Cette méthode est appelée depuis le constructeur pour initialiser le JPanel.
     * ATTENTION : cette fonction reprend du code généré par un JForm.
     */                       
    private void initComponents()
    {
        this.setSize(500, 560);

        this.selectionProduit = new critVente(this.controleur, this);
        this.affichageFacture = new Resultat<FactureLigneForm>(this);
        
        
        this.setLayout(new BorderLayout());
        this.add(this.selectionProduit, BorderLayout.CENTER);
        this.add(this.affichageFacture, BorderLayout.SOUTH);
    }
    
    @Override
    public void selectionnerResultat(Form res)
    {
        if (res instanceof FactureLigneForm)
            this.selectionProduit.setForm(res);
        else
            throw new IllegalArgumentException("Erreur dans menuProduit: le formulaire à sélectionner n'est pas un ProduitForm ou un FactureLigneForm.");
    }

    @Override
    public void lancerRecherche(Form form)
    {
        if (!(form instanceof CodeBarreForm) || form instanceof ProduitForm)
            throw new IllegalArgumentException("Erreur dans menuVente: le formulaire à rechercher n'est pas un CodeBarreForm.");
        try {
            // Effectuer la recherche avec fonction RECHERCHE dans CONTROLEUR
            Vector<ProduitForm> resultatsRecherche = null;
            resultatsRecherche = this.controleur.chercherProduits(form);
            // Afficher le produit dans CRITERE
            if (resultatsRecherche != null && !resultatsRecherche.isEmpty())
                this.selectionProduit.setForm(resultatsRecherche.elementAt(0)); //normalement, il n'y a qu'un produit
//            }
                else //ou, si le produit n'a pas été trouvé :
                traiterEchecRecherche(((CodeBarreForm) form).getCodeBarre());
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
        this.affichageFacture.afficherErreur(e.getMessage());
    }

    @Override
    public void afficherLog(String log)
    {
        this.affichageFacture.afficherMessage(log);
    }

    public void selectionnerPersonne() //appelé par un composant qui ne peut pas sélectionner de personne
    {
        if (this.facture.getLignes().isEmpty())
        {
            this.afficherErreur(new IllegalArgumentException("Erreur : la facture est vide."));
            return;
        }
        
        this.selectionPersonne = new menuPersonne(this.controleur, this);
        
        this.removeAll();
        this.add(this.selectionPersonne, BorderLayout.CENTER);
        //refresh
        this.setVisible(false);
        this.setVisible(true);
    }
    public void selectionnerPersonne(PersonneForm pf) //appelé par un composant qui A sélectionné une personne
    {
        this.facture.setActeur(pf);
        //création de la facture !
        try {
            this.afficherLog(
                    this.controleur.creer(this.facture).toString());
        }
        catch (DonneesInsuffisantesException ex) {
            this.afficherErreur(ex);}
        //retour à l'affichage initial
        this.removeAll();
        this.add(this.selectionProduit, BorderLayout.CENTER);
        this.add(this.affichageFacture, BorderLayout.SOUTH);
        //refresh
        this.setVisible(false);
        this.setVisible(true);
    }
    
    /**
     * Recherche dans la facture en cours de construction une ligne ayant le code barre mentionné.
     * @param codeBarre le code barre de la ligne à rechercher
     * @return l'indice de la ligne trouvée. Si aucune ligne n'est trouvée, renvoie -1.
     */
    public int chercherLigne(String codeBarre)
    {
            boolean ligneExistante = false;
            int indiceLigne = 0;
            while (!ligneExistante && indiceLigne < facture.getLignes().size())
            {
                ligneExistante = facture.getLignes().elementAt(indiceLigne)
                        .getProduit().getCodeBarre().equals(codeBarre);
                indiceLigne++;
            }
            //Si le code barre est déjà dans une ligne, on renvoie l'indice ; sinon, on renvoie -1
            return ligneExistante ? indiceLigne - 1 : -1;
    }
    public void ajouterLigne(FactureLigneForm ligne)
    {
        try {
            if (!ajoutLigneLegal(ligne))
                throw new Exception("La quantité excède les stocks disponibles.");
            //Détecte une redondance du code barre
            int indiceLigneExistante = chercherLigne(ligne.getProduit().getCodeBarre());
            if (indiceLigneExistante >= 0) //Si le code barre est déjà dans une ligne, on modifie ladite ligne.
                modifierLigne(ligne, indiceLigneExistante);
            else //sinon, on ajoute la ligne à la facture
                this.facture.getLignes().add(ligne);
            
            //affichage de la nouvelle facture.
            this.affichageFacture.afficherRes(this.facture.getLignes());
        }
        catch (Exception e)     {afficherErreur(e);}
    }
   public void modifierLigne(FactureLigneForm nouvelleLigne, int indiceLigneAModifier)
    {
        FactureLigneForm ligneAModifier = facture.getLignes().elementAt(indiceLigneAModifier);
        try {
            if (!modifierLigneLegal(nouvelleLigne,
                    ligneAModifier))
                throw new Exception("La quantité excède les stocks disponibles.");
            ligneAModifier.setQuantite(ligneAModifier.getQuantite() + nouvelleLigne.getQuantite());
            ligneAModifier.setPrixLigne(ligneAModifier.getPrixLigne() + nouvelleLigne.getPrixLigne());
        }
        catch (Exception e)     {afficherErreur(e);}
    }
  void supprimerLigne(String codeBarre)
    {
        try {
            int indiceLigneASupprimer = chercherLigne(codeBarre);
            if (indiceLigneASupprimer >= 0) //Si le code barre est déjà dans une ligne, on modifie ladite ligne.
                this.facture.getLignes().removeElementAt(indiceLigneASupprimer);
            else
                throw new Exception("Ligne non trouvée.");

            this.affichageFacture.afficherRes(this.facture.getLignes());
            afficherLog(""); //efface le message (0 résultats)
        }
        catch (Exception e)     {afficherErreur(e);}
    }        
    
    public boolean ajoutLigneLegal(FactureLigneForm ligne) {
        return ligne.getProduit().getStock() >= ligne.getQuantite()
                && ligne.getQuantite() > 0;
    }
    public boolean modifierLigneLegal(FactureLigneForm ligne1, FactureLigneForm ligne2) {
        int quantiteTotale = ligne1.getQuantite() + ligne2.getQuantite();
        return ligne1.getProduit().getStock() >= quantiteTotale
                && quantiteTotale > 0;
    }
    protected void traiterEchecRecherche(String codeBarre) {
        afficherErreur(new Exception("Aucun produit trouvé."));
    }
}
