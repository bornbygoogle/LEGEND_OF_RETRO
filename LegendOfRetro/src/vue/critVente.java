/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.CodeBarreForm;
import bean.FactureLigneForm;
//import bean.FactureForm;
//import bean.FactureLigneForm;
import bean.Form;
import bean.ProduitForm;
import bean.PersonneForm;
import controleur.Controleur;
import controleur.DonneeInvalideException;
import controleur.DonneesInsuffisantesException;
import java.awt.Color;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Home
 */
public class critVente extends javax.swing.JPanel {

    private Controleur controleur;
    private menuVente parent;
    
//    private FactureLigneForm ligneFacture; //si une ligne est en train d'être modifiée
    private ProduitForm produitExamine;
    
    /**
     * Creates new form critPersonne
     */
    public critVente(Controleur controleur, menuVente parent)
    {
        this.controleur = controleur;
        this.parent = parent;
        initComponents();
        clean();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelCodeBarre = new javax.swing.JLabel();
        labelQuantite = new javax.swing.JLabel();
        fieldQuantite = new javax.swing.JSpinner();
        labelNom = new javax.swing.JLabel();
        labelDevFab = new javax.swing.JLabel();
        labelZone = new javax.swing.JLabel();
        labelEdition = new javax.swing.JLabel();
        labelPlateforme = new javax.swing.JLabel();
        labelPrix = new javax.swing.JLabel();
        labelStock = new javax.swing.JLabel();
        buttonChercher = new javax.swing.JButton();
        buttonAjouter = new javax.swing.JButton();
        buttonSupprimer = new javax.swing.JButton();
        buttonTerminer = new javax.swing.JButton();
        fieldCodeBarre = new javax.swing.JFormattedTextField();

        labelCodeBarre.setText("Code Barre : ");

        labelQuantite.setText("Quantité :");
        labelQuantite.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        fieldQuantite.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        labelNom.setText("Nom : ");
        labelNom.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        labelDevFab.setText("Fabricant : ");

        labelZone.setText("Zone : ");

        labelEdition.setText("Edition : ");

        labelPlateforme.setText("Plateforme : ");

        labelPrix.setText("Prix :");

        labelStock.setText("Stock :");

        buttonChercher.setText("Chercher");
        buttonChercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChercherActionPerformed(evt);
            }
        });

        buttonAjouter.setText("Ajouter");
        buttonAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAjouterActionPerformed(evt);
            }
        });

        buttonSupprimer.setText("Supprimer");
        buttonSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSupprimerActionPerformed(evt);
            }
        });

        buttonTerminer.setText("Terminer");
        buttonTerminer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTerminerActionPerformed(evt);
            }
        });

        fieldCodeBarre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldCodeBarreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(labelEdition, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelZone, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelDevFab, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(labelPlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(819, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(labelPrix)
                            .addContainerGap(880, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonAjouter)
                                .addGap(18, 18, 18)
                                .addComponent(buttonSupprimer)
                                .addGap(41, 41, 41)
                                .addComponent(buttonTerminer))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelCodeBarre)
                                    .addComponent(labelNom))
                                .addGap(35, 35, 35)
                                .addComponent(fieldCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(buttonChercher))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(labelQuantite)
                                    .addGap(18, 18, 18)
                                    .addComponent(fieldQuantite, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(labelStock)))
                        .addContainerGap(551, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonChercher)
                    .addComponent(fieldCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(labelDevFab)
                .addGap(12, 12, 12)
                .addComponent(labelZone)
                .addGap(12, 12, 12)
                .addComponent(labelEdition)
                .addGap(12, 12, 12)
                .addComponent(labelPlateforme)
                .addGap(14, 14, 14)
                .addComponent(labelPrix)
                .addGap(12, 12, 12)
                .addComponent(labelStock)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelQuantite, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldQuantite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAjouter)
                    .addComponent(buttonSupprimer)
                    .addComponent(buttonTerminer))
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonChercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChercherActionPerformed
        this.parent.lancerRecherche(new CodeBarreForm(this.fieldCodeBarre.getText()));
        buttonChercher.setBackground(Color.GREEN);
    }//GEN-LAST:event_buttonChercherActionPerformed

    private void buttonAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAjouterActionPerformed
        this.parent.ajouterLigne(toForm());
        clean();
        buttonAjouter.setBackground(Color.GREEN);
    }//GEN-LAST:event_buttonAjouterActionPerformed

    private void buttonSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSupprimerActionPerformed
        this.parent.supprimerLigne(toForm());
         buttonSupprimer.setBackground(Color.GREEN);
    }//GEN-LAST:event_buttonSupprimerActionPerformed

    private void buttonTerminerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTerminerActionPerformed
        try {
            this.parent.afficherLog(
                    this.controleur.creer(this.parent.getFacture())
                            .toString());
        }
        catch (DonneesInsuffisantesException ex) {
            this.parent.afficherErreur(ex);}
         buttonTerminer.setBackground(Color.GREEN);
         
    }//GEN-LAST:event_buttonTerminerActionPerformed

    private void fieldCodeBarreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldCodeBarreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldCodeBarreActionPerformed

    
    public void clean()
    {
        this.fieldCodeBarre.setText("");
        this.fieldQuantite.getModel().setValue(new Integer(0));
        this.produitExamine = null;
        this.labelDevFab.setVisible(false);
        this.labelNom.setVisible(false);
        this.labelPlateforme.setVisible(false);
        this.labelEdition.setVisible(false);
        this.labelZone.setVisible(false);
        this.labelPrix.setVisible(false);
        this.labelStock.setVisible(false);
    }
    public void setForm(Form f)
    {
        boolean typeJeu;
        String typeDev;
        
        if (f instanceof ProduitForm) {
            //this.ligneFacture = null;
            this.produitExamine = (ProduitForm) f;
            
        }
        else
            throw new IllegalArgumentException("Erreur dans menuProduit: le formulaire à sélectionner n'est pas un ProduitForm ou un FactureLigneForm.");
        
        typeJeu = ("Jeu".equals(this.produitExamine.getType()));
        if (typeJeu) {
            typeDev = "Développeur : ";
            this.labelPlateforme.setText("Plateforme : " + this.produitExamine.getPlateforme());
        }
        else
            typeDev = "Fabricant : ";
        this.labelDevFab.setText(typeDev + this.produitExamine.getEditeur());
        this.labelNom.setText("Nom : " + this.produitExamine.getNom());
        this.labelEdition.setText("Edition : " + this.produitExamine.getEdition());
        this.labelZone.setText("Zone : " + this.produitExamine.getZone());
        this.labelPrix.setText("Prix : " + this.produitExamine.getPrix() + " €");
        this.labelStock.setText("Stock : " + this.produitExamine.getStock());

        this.labelDevFab.setVisible(true);
        this.labelNom.setVisible(true);
        this.labelPlateforme.setVisible(typeJeu);
        this.labelEdition.setVisible(true);
        this.labelZone.setVisible(true);
        this.labelPrix.setVisible(true);
        this.labelStock.setVisible(true);
    }
    private FactureLigneForm toForm()
    {
        FactureLigneForm retour = new FactureLigneForm();
        retour.setProduit(this.produitExamine);
        int q = (((SpinnerNumberModel) this.fieldQuantite.getModel()).getNumber().intValue());
        retour.setQuantite(q);
        retour.setPrixLigne(q * this.produitExamine.getPrix());
        
        return retour;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAjouter;
    private javax.swing.JButton buttonChercher;
    private javax.swing.JButton buttonSupprimer;
    private javax.swing.JButton buttonTerminer;
    private javax.swing.JFormattedTextField fieldCodeBarre;
    private javax.swing.JSpinner fieldQuantite;
    private javax.swing.JLabel labelCodeBarre;
    private javax.swing.JLabel labelDevFab;
    private javax.swing.JLabel labelEdition;
    private javax.swing.JLabel labelNom;
    private javax.swing.JLabel labelPlateforme;
    private javax.swing.JLabel labelPrix;
    private javax.swing.JLabel labelQuantite;
    private javax.swing.JLabel labelStock;
    private javax.swing.JLabel labelZone;
    // End of variables declaration//GEN-END:variables
}
