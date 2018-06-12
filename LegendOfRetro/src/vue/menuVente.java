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
import bean.PersonneForm;
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
    protected menuPersonne selectionPersonne;
    FactureForm facture;
    int   count=0;
    Vector vectorcontrolCodeBarre;
    boolean detectredondCodeBarre;
    int     detectQuelLigne;

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
        vectorcontrolCodeBarre=new Vector();
        detectredondCodeBarre=false;
        detectQuelLigne=1;
        
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
            { 
                
                this.count=this.count+1;
                vectorcontrolCodeBarre.add(resultatsRecherche.elementAt(0).getCodeBarre());
                
                if(this.count>1)
                {    
                    for(int i=0; i<vectorcontrolCodeBarre.size()-1;i++)
                    {
                       if(vectorcontrolCodeBarre.elementAt(i)==vectorcontrolCodeBarre.lastElement())
                       {
                        System.out.println( "--Codebarre-table--"+vectorcontrolCodeBarre.elementAt(i)+"----");
                        this.detectredondCodeBarre=true;
                        this.detectQuelLigne=i;
                       } 
                        System.out.println( "--Codebarre-tableinafara--"+vectorcontrolCodeBarre.elementAt(i)+"----");
                    
                     }  
                }
                
                
               
                this.selectionProduit.setForm(resultatsRecherche.elementAt(0)); //normalement, il n'y a qu'un produit
                System.out.println( "--count1 --"+this.count+"----");
                System.out.println( "--code barre --"+resultatsRecherche.elementAt(0).getCodeBarre()+"-----");
            
            
            }
            
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
        boolean ligneclean=false;
          
                   for(int i=0; i<vectorcontrolCodeBarre.size()-1;i++)
                    {
                      ligneNonTrouvee = it.next().getProduit().getCodeBarre().equals(
                        codeBarre);
                       if(vectorcontrolCodeBarre.elementAt(i)==vectorcontrolCodeBarre.lastElement())
                       {
                        System.out.println( "--Codebarre-table--"+vectorcontrolCodeBarre.elementAt(i)+"----");
                        this.detectredondCodeBarre=false;
                        this.detectQuelLigne=i;
                        it.remove();
                        vectorcontrolCodeBarre.remove(i);
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
}
