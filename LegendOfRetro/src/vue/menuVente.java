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
import controleur.DonneesInsuffisantesException;
import controleur.ResultatInvalideException;
import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
/*/ à des fins de tests, création d'une facture :)
FactureForm ff = new FactureForm();
ff.setNature(false); //vente
ff.setReductions(12.5f);
try {
    FactureLigneForm flf1 = new FactureLigneForm();
    Vector<ProduitForm> Vpf1;
    Vpf1 = this.controleur.chercher(new CodeBarreForm("4343434343434"));
    if (Vpf1.isEmpty())
        System.out.println("Shit");
    flf1.setProduit(Vpf1.elementAt(0));
    flf1.setQuantite(3);
    flf1.setPrixLigne(flf1.getQuantite() * flf1.getProduit().getPrix());
    ff.getLignes().add(flf1);

    FactureLigneForm flf2 = new FactureLigneForm();
    Vector<ProduitForm> Vpf2;
    Vpf2 = this.controleur.chercher(new CodeBarreForm("1234567890128"));
    if (Vpf2.isEmpty())
        System.out.println("Shit2");
    flf2.setProduit(Vpf2.elementAt(0));
    flf2.setQuantite(2);
    flf2.setPrixLigne(flf2.getQuantite() * flf2.getProduit().getPrix());
    ff.getLignes().add(flf2);

    this.controleur.creer(ff);
} catch (DonneeInvalideException ex) {
    Logger.getLogger(menuVente.class.getName()).log(Level.SEVERE, null, ex);
} catch (ResultatInvalideException ex) {
    Logger.getLogger(menuVente.class.getName()).log(Level.SEVERE, null, ex);
} catch (DonneesInsuffisantesException ex) {
    Logger.getLogger(menuVente.class.getName()).log(Level.SEVERE, null, ex);
}
//fin des tests*/
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
        else if (res instanceof FactureLigneForm)
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
//test System.out.println("Code barre recherché " + codeBarre);
        boolean ligneTrouvee = !it.hasNext();
        
        if (ligneTrouvee)afficherErreur(new Exception(
                        "Erreur: aucune ligne ne peut être supprimée."));
        
        while(!ligneTrouvee)
        {
//test FactureLigneForm exam = it.next();
//test System.out.println("Code barre examiné " + exam.getProduit().getCodeBarre());
            ligneTrouvee = it.next().getProduit().getCodeBarre().equals(
                    codeBarre);
//test System.out.println(ligneNonTrouvee);
            if (!ligneTrouvee) //si la ligne a été trouvée, on l'enlève de la facture
                it.remove();
            else if (!it.hasNext()) //si la ligne n'est pas trouée à la fin de la boucle, on affiche l'erreur et on quitte la boucle
            {
                afficherErreur(new Exception(
                        "Erreur: aucune ligne préalablement entrée ne correspond à ce code barre."));
                ligneTrouvee = false; //on quitte la boucle
            }
        }
        
        this.affichageFacture.afficherRes(this.facture.getLignes());
    }
    
    public boolean ajoutLigneLegal(FactureLigneForm ligne) {
        return ligne.getProduit().getStock() >= ligne.getQuantite()
                && ligne.getQuantite() > 0;
    }
    protected void traiterEchecRecherche(String codeBarre) {
        afficherErreur(new Exception("Aucun produit trouvé."));
    }
    
    public FactureForm getFacture()     {return this.facture;}


}
