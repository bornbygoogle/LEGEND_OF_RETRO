/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.Form;
import controleur.Controleur;
import controleur.DonneeInvalideException;
import controleur.DonneesInsuffisantesException;
import controleur.ResultatInvalideException;
import java.awt.BorderLayout;
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
    private Resultat Resultats;

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
    private void initComponents() {
        this.setSize(500, 560);

        this.Criteres = new critResultat(this.controleur, this);
        this.Resultats = new Resultat(this);

        /*
        GroupLayout jPanel1Layout = new GroupLayout(Criteres);
        Criteres.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 182, Short.MAX_VALUE)
        );

        GroupLayout jPanel2Layout = new GroupLayout(Resultats);
        Resultats.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 112, Short.MAX_VALUE)
        );
        //*/
        
        this.setLayout(new BorderLayout());
        this.add(this.Criteres, BorderLayout.CENTER);
        this.add(this.Resultats, BorderLayout.SOUTH);
        /*GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(Criteres, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Resultats, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Criteres, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Resultats, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );//*/
    }                     

    //TODO: fonction de création dans la BDD
    
    @Override
    public void selectionnerResultat(Object res)
    {
        System.out.println("Un résultat a été sélectionné (à implémenter)");
    }

    @Override
    public void lancerRecherche(Form form)
    {
        System.out.println("Une recherche a été lancée (à implémenter)");
        try {
            this.controleur.chercher(form);}
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
        this.Resultats.displayError(e.getMessage());
    }


}
