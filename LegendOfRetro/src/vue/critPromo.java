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
import controleur.EnregistrementInexistantException;
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
        editions = controleur.listeEdition("Console");
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
        
        this.idVersionJeu = 0;
        this.idVersionConsole = 0;
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
        buttonChercher = new javax.swing.JButton();

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

        buttonChercher.setText("Chercher");
        buttonChercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChercherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCategorie)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelPlateforme)
                        .addComponent(labelZone, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listeCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listeZone, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listePlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelTag, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelEdition, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(fieldDevFab))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listeFabricant, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listeEdition, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listeTags, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(buttonChercher, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonModifier, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(169, 169, 169))
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listeTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelTag))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonModifier)
                    .addComponent(buttonChercher))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                
                //Réinitialiser tous les champs
                listeEdition.removeAllItems();
                // Initialisation la modele pour listeEdition
                Vector<String> editions;
                editions = controleur.listeEdition("Jeu");
                editions.add(0, "");
                listeEdition.setModel(new javax.swing.DefaultComboBoxModel<>(editions));

                // Initialisation la modele pour listeZone        
                listeZone.removeAllItems();
                Vector<String> zones = controleur.listeZones();
                zones.add(0, "");
                listeZone.setModel(new javax.swing.DefaultComboBoxModel<>(zones));

                // Initialisation la modele pour listeConsoles
                listePlateforme.removeAllItems();
                Vector<String> plateformes = controleur.listeConsoles();
                plateformes.add(0, "");
                listePlateforme.setModel(new javax.swing.DefaultComboBoxModel<>(plateformes));
        
                // Initialisation la modele pour listeFabricant
                listeFabricant.removeAllItems();
                Vector<String> fabricants = controleur.listeFabricant();
                fabricants.add(0, "");
                listeFabricant.setModel(new javax.swing.DefaultComboBoxModel<>(fabricants));
        
                // Initialisation la modele pour listeTags
                listeTags.removeAllItems();
                Vector<String> tags = controleur.listeTags();
                tags.add(0, "");
                listeTags.setModel(new javax.swing.DefaultComboBoxModel<>(tags)); 
           }
        else 
            {
                fieldDevFab.setText("Fabricant : ");
                labelPlateforme.setVisible(false);
                listePlateforme.setVisible(false);
                labelTag.setVisible(false);
                listeTags.setVisible(false);
                //Réinitialiser tous les champs
                listeEdition.removeAllItems();
                // Initialisation la modele pour listeEdition
                Vector<String> editions;
                editions = controleur.listeEdition("Console");
                editions.add(0, "");
                listeEdition.setModel(new javax.swing.DefaultComboBoxModel<>(editions));

                // Initialisation la modele pour listeZone        
                listeZone.removeAllItems();
                Vector<String> zones = controleur.listeZones();
                zones.add(0, "");
                listeZone.setModel(new javax.swing.DefaultComboBoxModel<>(zones));
   
                // Initialisation la modele pour listeFabricant
                listeFabricant.removeAllItems();
                Vector<String> fabricants = controleur.listeFabricant();
                fabricants.add(0, "");
                listeFabricant.setModel(new javax.swing.DefaultComboBoxModel<>(fabricants));
            }
    }//GEN-LAST:event_listeCategorieItemStateChanged

    private void listePlateformeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listePlateformeItemStateChanged
     try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listePlateformeItemStateChanged

    private void buttonModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifierActionPerformed
        try {
            this.controleur.calculCote("Console", 2);
        } catch (EnregistrementInexistantException ex) {
            Logger.getLogger(critPromo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DonneeInvalideException ex) {
            Logger.getLogger(critPromo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonModifierActionPerformed

    private void listeZoneItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeZoneItemStateChanged
     try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listeZoneItemStateChanged

    private void listeFabricantItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeFabricantItemStateChanged
     try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listeFabricantItemStateChanged

    private void listeEditionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeEditionItemStateChanged
     try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listeEditionItemStateChanged

    private void listeTagsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeTagsItemStateChanged
     try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_listeTagsItemStateChanged

    private void buttonChercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChercherActionPerformed
        // TODO add your handling code here:
     try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_buttonChercherActionPerformed

    public void setForm(PromoForm f)
    {
        this.selectedForm = f;
        
        /*
        *    Si le type de form est "jeu"
        */
        if ("Jeu".equals(f.getType()))
        {           
            this.listeCategorie.setSelectedIndex(1);                   //type Jeu
        }
        else if ("Console".equals(f.getType()))
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
        this.fieldCote.setText(String.valueOf(f.getCote()));           // Cote
    }
    private Form toForm() throws DonneeInvalideException
    {
        String typeCat = "";
        String cb = ""/*CodeBarre*/;
        String nom = ""/*Nom*/;
        String edition = ""/*Edition*/;
        String zone = ""/*Zone*/;
        String fab = ""/*Fabricant*/;
        String photo = ""/*Photo*/;
        String desc = ""/* Description */;
        String platforme = ""/*Platforme*/;               
        String tag = ""/*Tags*/;
        float prix,cote=0f;
        int stock;
        try {        
                    //typeCat = listeCategorie.getSelectedItem().toString()/*type*/; 
                    cb = ""/*CodeBarre*/;
                    nom = ""/*Nom*/;
                    System.out.println(listeEdition.getSelectedItem());
                    edition = listeEdition.getSelectedItem().toString()/*Edition*/;
                    zone = listeZone.getSelectedItem().toString()/*Zone*/;
                    fab = listeFabricant.getSelectedItem().toString()/*Fabricant*/;
                    photo = ""/*Photo*/;
                    desc = ""/* Description */;
                    platforme = listePlateforme.getSelectedItem().toString()/*Platforme*/;               
                    tag = listeTags.getSelectedItem().toString()/*Tags*/;
                    prix = Float.valueOf(fieldPrix.getText());
                    stock = Integer.valueOf(fieldStock.getText());
                    cote = Float.valueOf(fieldCote.getText());
        }
        catch (NumberFormatException nfe) {
            prix = 0f;
            stock = 0;
            cote = 0f;
        }
                System.out.println(this.idVersionConsole);    
        return new PromoForm(this.idVersionConsole,
                                this.idVersionJeu,
                                typeCat /*type*/, 
                                cb/*CodeBarre*/,
                                nom/*Nom*/,
                                edition/*Edition*/,
                                zone/*Zone*/,
                                fab/*Fabricant*/,
                                photo/*Photo*/,
                                desc/* Description */,
                                platforme/*Platforme*/,                
                                tag/*Tags*/,
                                prix, stock, cote);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton buttonChercher;
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
