/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.CodeBarreForm;
import bean.FactureForm;
import bean.FactureLigneForm;
//import bean.FactureLigneForm;
import bean.Form;
import bean.ProduitForm;
import controleur.Controleur;
import controleur.DonneeInvalideException;
import controleur.ResultatInvalideException;
import java.awt.BorderLayout;
import java.util.Iterator;
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
        if (res instanceof ProduitForm) 
            this.selectionProduit.setForm((ProduitForm) res);
/*        else if (res instanceof FactureLigneForm)
            this.Criteres.setForm(res);*/
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
            resultatsRecherche = this.controleur.chercher(form);
            // Afficher le produit dans CRITERE
            if (resultatsRecherche != null && !resultatsRecherche.isEmpty())
                this.selectionProduit.setForm(resultatsRecherche.elementAt(0)); //normalement, il n'y a qu'un produit
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

    public void ajouterLigne(FactureLigneForm ligne)
    {
        try {
            if (!ajoutLigneLegal(ligne))
                throw new Exception("La quantité excède les stocks disponibles.");
            this.facture.getLignes().add(ligne);
System.out.println("            this.affichageFacture.ajouter(ligne) //!TODO");}
        catch (Exception e)     {afficherErreur(e);}
    }
    void supprimerLigne(FactureLigneForm ligne)
    {
        //on commence par trouver, dans this.facture, une ligne qui a le même code barre que la ligne fournie en paramètre.
        Iterator<FactureLigneForm> it = this.facture.getLignes().iterator();
        String codeBarre = ligne.getProduit().getCodeBarre();
        boolean ligneNonTrouvee = it.hasNext();
        
        while(ligneNonTrouvee)
        {
            ligneNonTrouvee = it.next().getProduit().getCodeBarre().equals(
                    codeBarre);
            if (!ligneNonTrouvee) //si la ligne a été trouvée, on l'enlève de la facture
                it.remove();
            else if (!it.hasNext()) //si la ligne n'est pas trouée à la fin de la boucle, on affiche l'erreur et on quitte la boucle
            {
                afficherErreur(new Exception(
                        "Erreur: aucune ligne préalablement entrée ne correspond à ce code barre."));
                ligneNonTrouvee = false; //on quitte la boucle
            }
        }
    }
    
    public boolean ajoutLigneLegal(FactureLigneForm ligne) {
        return ligne.getProduit().getStock() < ligne.getQuantite();
    }
    protected void traiterEchecRecherche(String codeBarre) {
        afficherErreur(new Exception("Aucun produit trouvé."));
    }
    
    public FactureForm getFacture()     {return this.facture;}


}
