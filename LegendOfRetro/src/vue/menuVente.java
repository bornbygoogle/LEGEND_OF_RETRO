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
import java.awt.Color;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Home
 */
public class menuVente extends JPanel implements Chercheur
{
    private Controleur controleur;

    private critVente Criteres;
    private Resultat<ProduitForm> Resultats;

    /**
     * Creates new form menuProduit
     */
    public menuVente(Controleur c)
    {
        super();
        this.controleur = c;
        initComponents();
        this.facture.setNature(false);
    }

    /**
     * Cette méthode est appelée depuis le constructeur pour initialiser le JPanel.
     * ATTENTION : cette fonction reprend du code généré par un JForm.
     */                       
    private void initComponents()
    {
        this.setSize(500, 560);

        this.Criteres = new critVente(this.controleur, this);
        this.Resultats = new Resultat<ProduitForm>(this);
        
        
        this.setLayout(new BorderLayout());
        this.add(this.Criteres, BorderLayout.CENTER);
        this.add(this.Resultats, BorderLayout.SOUTH);
    }
    
    @Override
    public void selectionnerResultat(Form res)
    {
        if (res instanceof ProduitForm) 
            this.Criteres.setForm((ProduitForm) res);
/*        else if (res instanceof FactureLigneForm)
            this.Criteres.setForm(res);*/
        else
            throw new IllegalArgumentException("Erreur dans menuProduit: le formulaire à sélectionner n'est pas un ProduitForm ou un FactureLigneForm.");
    }

    @Override
    public void lancerRecherche(Form form)
    {
        if (!(form instanceof CodeBarreForm))
            throw new IllegalArgumentException("Erreur dans menuVente: le formulaire à rechercher n'est pas un CodeBarreForm.");
        try {
            // Affectuer la recherche avec fonction RECHERCHE dans CONTROLEUR
            Vector<ProduitForm> resultatsRecherche = null;
            resultatsRecherche = this.controleur.chercher(form); //! TODO: attention, dans le contrôleur, à ce que renvoie chercherform()
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

    public void ajouterLigne(FactureLigneForm ligne)
    {
//TODO: rechercher dans la facture pour voir si la ligne existe, et si elle existe, mofifier (attention ajoutLigneLegal devrait prendre en compte le DEUX linges -> polymorphisme ?)
        try {
            if (!ajoutLigneLegal(ligne))
                throw new Exception("La quantité excède les stocks disponibles.");
            this.facture.getLignes().add(ligne);
            this.affichageFacture.afficherRes(this.facture.getLignes());
        }
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
        
        this.affichageFacture.afficherRes(this.facture.getLignes());
    }
    
    public boolean ajoutLigneLegal(FactureLigneForm ligne) {
        return ligne.getProduit().getStock() > ligne.getQuantite()
                && ligne.getQuantite() > 0;
    }
    protected void traiterEchecRecherche(String codeBarre) {
        afficherErreur(new Exception("Aucun produit trouvé."));
    }
    
    public FactureForm getFacture()     {return this.facture;}

}
