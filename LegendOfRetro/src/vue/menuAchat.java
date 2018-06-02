/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.FactureForm;
import bean.FactureLigneForm;
import controleur.Controleur;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Home
 */
public class menuAchat extends menuVente
{
    private GUI parent;
    
    /**
     * Creates new form menuProduit
     */
    public menuAchat(Controleur c, GUI parent)
    {
        super(c);
        this.parent = parent;
        
        //chargement d'un éventuel fichier sérialisé (reprise d'une facture après création d'un produit)
        File serializedFile = new File("facture_achat_en_cours.ser");
        if (serializedFile.exists())
        {
            //si un fichier .ser existe, on informe l'utilisateur
            JOptionPane.showMessageDialog(this,
                    "Une transaction en cours a été sauvegardée. Elle sera chargée automatiquement.",
                    "Reprise d'une transaction.", JOptionPane.INFORMATION_MESSAGE);
            //puis on le charge par défaut et on le supprime.
            try {
                FileInputStream fis = new FileInputStream(serializedFile);
                ObjectInputStream ois = new ObjectInputStream(fis);

                this.facture = (FactureForm) ois.readObject();
                ois.close();
                fis.close();
            }
            catch (FileNotFoundException ex)    {System.out.println(
                    "Erreur lors de la désérialization : fichier non trouvé.");}
            catch (IOException ex)              {System.out.println(
                    "Erreur lors de la désérialization : entrée/sortie.");}
            catch (ClassNotFoundException ex)   {System.out.println(
                    "Erreur lors de la désérialization : classe FactureForm non trouvée.");}
            finally {serializedFile.delete();}
            
            this.affichageFacture.afficherRes(this.facture.getLignes());
        }
    }
    
    @Override
    public boolean ajoutLigneLegal(FactureLigneForm ligne)
    {
        return true; //On peut toujours acheter, il n'y a pas de condition sur la quantité.
    }
    @Override
    protected void traiterEchecRecherche(String codeBarre) {
        //sérialisation de la facture en cours
        File file = new File("facture_achat_en_cours.ser");
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(this.facture);
            oos.close();
            fos.close();
        }
        catch (FileNotFoundException ex) {System.out.println("Problème lors de la sérialisation : fichier non trouvé");}
        catch (IOException ex) {System.out.println("Problème lors de la sérialisation : entrée/sortie");}
        
        JOptionPane.showMessageDialog(this,
                "Produit non trouvé.\nVous serez redirigé vers le menu Produit.\nLa facture en cours sera sauvegardée.",
                "Produit non trouvé.", JOptionPane.INFORMATION_MESSAGE);
        this.parent.ouvrirMenu(GUI.Menu.PRODUIT, codeBarre);
    }
}
