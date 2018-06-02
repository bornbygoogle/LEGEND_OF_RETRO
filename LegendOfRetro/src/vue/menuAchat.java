/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.FactureLigneForm;
import controleur.Controleur;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        
        //TODO: vérifier sérialisation
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
