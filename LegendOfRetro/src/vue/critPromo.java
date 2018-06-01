/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.Form;
import bean.PromoForm;
import controleur.Controleur;
import controleur.DonneeInvalideException;
import controleur.DonneesInsuffisantesException;
import controleur.EnregistrementExistantException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bornbygoogle
 */
public class critPromo extends javax.swing.JPanel
{
    private Form selectedForm;
    private Controleur controleur;
    private Chercheur parent;
    
    private int idVersionJeu;
    private int idVersionConsole;

    /**
     * Creates new form Resultat
     */
    public critPromo(Controleur controleur, Chercheur parent)
    {
        this.controleur = controleur;
        this.parent = parent;
        this.selectedForm = null;
        initComponents();
        
        
       // Initialisation la modele pour listeEdition
        Vector<String> editions;
        editions = controleur.listeEdition((String)listeCategorie.getSelectedItem());
        editions.add(0, "");
        listeEdition.setModel(new javax.swing.DefaultComboBoxModel<>(editions));

       // Initialisation la modele pour listeZone        
        Vector<String> zones = controleur.listeZones();
        zones.add(0, "");
        listeZone.setModel(new javax.swing.DefaultComboBoxModel<>(zones));

       // Initialisation la modele pour listeConsoles
        Vector<String> plateformes = controleur.listeConsoles();
        plateformes.add(0, "");
        listePlateforme.setModel(new javax.swing.DefaultComboBoxModel<>(plateformes));
        
       // Initialisation la modele pour listeFabricant
        Vector<String> fabricants = controleur.listeFabricant();
        fabricants.add(0, "");
        listeFabricant.setModel(new javax.swing.DefaultComboBoxModel<>(fabricants));
        
       // Initialisation la modele pour listeTags
        Vector<String> tags = controleur.listeTags();
        tags.add(0, "");
        listeTags.setModel(new javax.swing.DefaultComboBoxModel<>(tags)); 
        
        this.idVersionJeu = -1;
        this.idVersionConsole = -1;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelZone = new javax.swing.JLabel();
        fieldDevFab = new javax.swing.JLabel();
        labelPlateforme = new javax.swing.JLabel();
        labelPlateforme.setVisible(false);
        labelEdition = new javax.swing.JLabel();
        labelCategorie = new javax.swing.JLabel();
        listeCategorie = new javax.swing.JComboBox<>();
        labelTag = new javax.swing.JLabel();
        labelTag.setVisible(false);
        listePlateforme = new javax.swing.JComboBox<>();
        listePlateforme.setVisible(false);
        labelPrix = new javax.swing.JLabel();
        labelStock = new javax.swing.JLabel();
        labelCurrency = new javax.swing.JLabel();
        labelCote = new javax.swing.JLabel();
        fieldCote = new javax.swing.JLabel();
        listeZone = new javax.swing.JComboBox<>();
        buttonModifier = new javax.swing.JButton();
        fieldPrix = new javax.swing.JFormattedTextField();
        listeFabricant = new javax.swing.JComboBox<>();
        listeEdition = new javax.swing.JComboBox<>();
        listeTags = new javax.swing.JComboBox<>();
        listeTags.setVisible(false);
        fieldStock = new javax.swing.JLabel();

        setName("critResultat"); // NOI18N

        labelZone.setText("Zone : ");

        fieldDevFab.setText("Fabricant : ");

        labelPlateforme.setText("Plateforme : ");

        labelEdition.setText("Edition : ");

        labelCategorie.setForeground(new java.awt.Color(248, 7, 7));
        labelCategorie.setText("Categorie * :");

        listeCategorie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Console", "Jeu" }));
        listeCategorie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listeCategorieItemStateChanged(evt);
            }
        });

        labelTag.setText("Tags : ");

        listePlateforme.setModel(listePlateforme.getModel());
        listePlateforme.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listePlateformeItemStateChanged(evt);
            }
        });

        labelPrix.setText("Prix :");

        labelStock.setText("Stock :");

        labelCurrency.setText("€");

        labelCote.setText("Cote :");

        fieldCote.setText("0");

        listeZone.setModel(listeZone.getModel());
        listeZone.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listeZoneItemStateChanged(evt);
            }
        });

        buttonModifier.setText("Modifier");
        buttonModifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModifierActionPerformed(evt);
            }
        });

        listeFabricant.setModel(listeFabricant.getModel());
        listeFabricant.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listeFabricantItemStateChanged(evt);
            }
        });

        listeEdition.setModel(listeEdition.getModel());
        listeEdition.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listeEditionItemStateChanged(evt);
            }
        });

        listeTags.setModel(listeTags.getModel());
        listeTags.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listeTagsItemStateChanged(evt);
            }
        });

        fieldStock.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelCategorie)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listeCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPlateforme)
                            .addComponent(labelZone, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(listeZone, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(listePlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelTag, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelEdition, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(fieldDevFab)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listeFabricant, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listeEdition, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listeTags, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(labelPrix)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fieldPrix, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(labelCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(labelCote)
                            .addGap(10, 10, 10)
                            .addComponent(fieldCote)
                            .addGap(40, 40, 40)
                            .addComponent(labelStock)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(fieldStock)))
                    .addComponent(buttonModifier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(listeCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listeZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelZone))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelPlateforme)
                            .addComponent(listePlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelEdition)
                                    .addComponent(listeEdition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(listeFabricant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldDevFab)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelCote)
                                    .addComponent(fieldCote)
                                    .addComponent(fieldStock)
                                    .addComponent(labelStock))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelPrix)
                                    .addComponent(fieldPrix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelCurrency))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonModifier)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(listeTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelTag)))))
                .addGap(9, 9, 9))
        );

        getAccessibleContext().setAccessibleName("critResultat");
    }// </editor-fold>//GEN-END:initComponents

    private void listeCategorieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeCategorieItemStateChanged
        // TODO add your handling code here:
        if ("Jeu".equals((String)listeCategorie.getSelectedItem())) 
           {
               fieldDevFab.setText("Développeur :");
               labelPlateforme.setVisible(true);
               listePlateforme.setVisible(true);
               labelTag.setVisible(true);
               listeTags.setVisible(true);
           }
        else 
            { 
                fieldDevFab.setText("Fabricant : ");
                labelPlateforme.setVisible(true);
                listePlateforme.setVisible(false);
                labelTag.setVisible(true);
                listeTags.setVisible(false);
            }
        try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listeCategorieItemStateChanged

    private void listePlateformeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listePlateformeItemStateChanged
        // TODO add your handling code here:
        try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listePlateformeItemStateChanged

    private void buttonModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifierActionPerformed
        throw new UnsupportedOperationException("La modification de produit n'a pas encore été implémentée.");
    }//GEN-LAST:event_buttonModifierActionPerformed

    private void listeZoneItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeZoneItemStateChanged
        // TODO add your handling code here:
        try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listeZoneItemStateChanged

    private void listeFabricantItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeFabricantItemStateChanged
        // TODO add your handling code here:
        try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listeFabricantItemStateChanged

    private void listeEditionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeEditionItemStateChanged
        // TODO add your handling code here:
        try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listeEditionItemStateChanged

    private void listeTagsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeTagsItemStateChanged
        // TODO add your handling code here:
        try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listeTagsItemStateChanged

    public void setForm(PromoForm f)
    {
        this.selectedForm = f;
        
        /*
        *    Si le type de form est "jeu"
        */
        if ("jeu".equals(f.getType()))
        {
            
            this.listeCategorie.setSelectedIndex(1);                   //type Jeu
        }
        else if ("console".equals(f.getType()))
            this.listeCategorie.setSelectedIndex(0);                   //type Console
            
        this.listeEdition.setSelectedItem(f.getEdition());             // Edition
        this.listeFabricant.setSelectedItem(f.getEditeur());           // Editeur
        this.listePlateforme.setSelectedItem(f.getPlateforme());       // Platforme
        this.listeTags.setSelectedItem(f.getTags());                   // Tags
        this.listeZone.setSelectedItem(f.getZone());                   // Zone
        this.idVersionJeu = f.getIdVersionJeu();                       // Id Version Jeu
        this.idVersionConsole = f.getIdVersionConsole();               // Id Version Console
        this.fieldPrix.setText(String.valueOf(f.getPrix()));           // Prix
        this.fieldStock.setText(String.valueOf(f.getStock()));         // Stock
        this.fieldCote.setText("10");                                  // Cote
    }
    private Form toForm() throws DonneeInvalideException
    {
        float prix;
        int stock;
        try {
            prix = Float.valueOf(fieldPrix.getText());
            stock = Integer.valueOf(fieldStock.getText());}
        catch (NumberFormatException nfe) {
            prix = 0f;
            stock = 0;
        }
            
        return new PromoForm(this.idVersionConsole,
                                this.idVersionJeu,
                                (String) listeCategorie.getSelectedItem() /*type*/, 
                                 ""/*CodeBarre*/,
                                 ""/*Nom*/,
                                 (String) listeEdition.getSelectedItem() /*Edition*/,
                                 (String) listeZone.getSelectedItem()/*Zone*/,
                                 (String) listeFabricant.getSelectedItem()/*Fabricant*/,
                                 ""/* Description */,
                                 (String) listeTags.getSelectedItem()/*Tags*/,
                                 (String) listePlateforme.getSelectedItem() /*Platforme*/,
                                 prix,
                                 stock);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton buttonModifier;
    public static javax.swing.JLabel fieldCote;
    public static javax.swing.JLabel fieldDevFab;
    public static javax.swing.JFormattedTextField fieldPrix;
    public static javax.swing.JLabel fieldStock;
    public static javax.swing.JLabel labelCategorie;
    public static javax.swing.JLabel labelCote;
    public static javax.swing.JLabel labelCurrency;
    public static javax.swing.JLabel labelEdition;
    public static javax.swing.JLabel labelPlateforme;
    public static javax.swing.JLabel labelPrix;
    public static javax.swing.JLabel labelStock;
    public static javax.swing.JLabel labelTag;
    public static javax.swing.JLabel labelZone;
    public static javax.swing.JComboBox<String> listeCategorie;
    public static javax.swing.JComboBox<String> listeEdition;
    public static javax.swing.JComboBox<String> listeFabricant;
    public static javax.swing.JComboBox<String> listePlateforme;
    public static javax.swing.JComboBox<String> listeTags;
    public static javax.swing.JComboBox<String> listeZone;
    // End of variables declaration//GEN-END:variables

}
