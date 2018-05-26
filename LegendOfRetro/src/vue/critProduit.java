/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.CodeBarreForm;
import bean.Form;
import bean.ProduitForm;
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
public class critProduit extends javax.swing.JPanel
{
    private Form selectedForm;
    private Controleur controleur;
    private Chercheur parent;
    
    private int idVersionJeu;
    private int idVersionConsole;

    /**
     * Creates new form Resultat
     * @param controleur x
     * @param parent x
     */
    public critProduit(Controleur controleur, Chercheur parent)
    {
        this.controleur = controleur;
        this.parent = parent;
        this.selectedForm = null;
        initComponents();
        
        Vector<String> zones = controleur.listeZones();
        zones.add(0, "");
        zones.add("Autre");
        listeZone.setModel(new javax.swing.DefaultComboBoxModel<>(zones));

        Vector<String> plateformes = controleur.listeConsoles();
        plateformes.add(0, "");
        listePlateforme.setModel(new javax.swing.DefaultComboBoxModel<>(plateformes));
        
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

        labelCodeBarre = new javax.swing.JLabel();
        labelZone = new javax.swing.JLabel();
        fieldNom = new javax.swing.JTextField();
        fieldEditeur = new javax.swing.JTextField();
        labelNom = new javax.swing.JLabel();
        fieldDevFab = new javax.swing.JLabel();
        labelPlateforme = new javax.swing.JLabel();
        labelEdition = new javax.swing.JLabel();
        labelCategorie = new javax.swing.JLabel();
        listeCategorie = new javax.swing.JComboBox<>();
        labelTag = new javax.swing.JLabel();
        fieldEdition = new javax.swing.JTextField();
        fieldTag = new javax.swing.JTextField();
        listePlateforme = new javax.swing.JComboBox<>();
        labelPrix = new javax.swing.JLabel();
        labelStock = new javax.swing.JLabel();
        labelCasse = new javax.swing.JLabel();
        fieldCasse1 = new javax.swing.JLabel();
        fieldCasse2 = new javax.swing.JLabel();
        fieldPrix = new javax.swing.JLabel();
        labelCurrency = new javax.swing.JLabel();
        labelCote = new javax.swing.JLabel();
        fieldCote = new javax.swing.JLabel();
        fieldStock = new javax.swing.JLabel();
        fieldCasse = new javax.swing.JLabel();
        listeZone = new javax.swing.JComboBox<>();
        fieldAjoutCasse = new javax.swing.JSpinner();
        fieldTxtAjoutZone = new javax.swing.JTextField();
        buttonAjoutZone = new javax.swing.JButton();
        fieldCodeBarre = new javax.swing.JFormattedTextField();
        buttonChercher = new javax.swing.JButton();
        buttonNouveau = new javax.swing.JButton();
        buttonModifier = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescription = new javax.swing.JTextArea();

        setName("critResultat"); // NOI18N

        labelCodeBarre.setText("Code Barre : ");

        labelZone.setText("Zone : ");

        fieldNom.setToolTipText("Nom");
        fieldNom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldNomKeyPressed(evt);
            }
        });

        fieldEditeur.setToolTipText("Developpeur / Fabricant");
        fieldEditeur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldEditeurActionPerformed(evt);
            }
        });

        labelNom.setText("Nom : ");
        labelNom.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

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

        labelTag.setText("Tag : ");

        fieldEdition.setToolTipText("Edition");

        fieldTag.setToolTipText("Tag");

        listePlateforme.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PC", "PS", " " }));
        listePlateforme.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listePlateformeItemStateChanged(evt);
            }
        });
        listePlateforme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listePlateformeActionPerformed(evt);
            }
        });

        labelPrix.setText("Prix :");

        labelStock.setText("Stock :");

        labelCasse.setText("Casse :");

        fieldCasse1.setText("Description :");
        fieldCasse1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fieldCasse1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        fieldCasse2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fieldCasse2.setText("Photo :");
        fieldCasse2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fieldCasse2.setEnabled(false);
        fieldCasse2.setFocusable(false);

        fieldPrix.setText("0");

        labelCurrency.setText("€");

        labelCote.setText("Cote :");

        fieldCote.setText("0");

        fieldStock.setText("0");

        fieldCasse.setText("1000");

        listeZone.setModel(listeZone.getModel());
        listeZone.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listeZoneItemStateChanged(evt);
            }
        });

        fieldAjoutCasse.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        fieldTxtAjoutZone.setToolTipText("Developpeur / Fabricant");
        fieldTxtAjoutZone.setVisible(false);

        buttonAjoutZone.setText("Ajouter");
        buttonAjoutZone.setVisible(false);
        buttonAjoutZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAjoutZoneActionPerformed(evt);
            }
        });

        fieldCodeBarre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldCodeBarreActionPerformed(evt);
            }
        });

        buttonChercher.setText("Chercher");
        buttonAjoutZone.setVisible(false);
        buttonChercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChercherActionPerformed(evt);
            }
        });

        buttonNouveau.setText("Nouveau");
        buttonAjoutZone.setVisible(false);
        buttonNouveau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNouveauActionPerformed(evt);
            }
        });

        buttonModifier.setText("Modifier");
        buttonAjoutZone.setVisible(false);
        buttonModifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModifierActionPerformed(evt);
            }
        });

        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescription);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelCategorie)
                                    .addComponent(labelCodeBarre)
                                    .addComponent(fieldDevFab)
                                    .addComponent(labelNom)
                                    .addComponent(labelEdition)
                                    .addComponent(labelZone))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(fieldNom, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fieldEditeur, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fieldCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(listeCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(listeZone, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(fieldEdition, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(labelPlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(listePlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelCasse)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelPrix)
                                        .addComponent(labelStock)
                                        .addComponent(labelCote)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldStock)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(fieldCasse)
                                        .addGap(43, 43, 43)
                                        .addComponent(fieldAjoutCasse, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(fieldPrix, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(labelCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(fieldCote)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelTag)
                                        .addGap(18, 18, 18)
                                        .addComponent(fieldTag, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(fieldTxtAjoutZone, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonAjoutZone)))
                                .addGap(0, 64, Short.MAX_VALUE)))
                        .addGap(56, 56, 56))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonChercher)
                        .addGap(18, 18, 18)
                        .addComponent(buttonNouveau)
                        .addGap(18, 18, 18)
                        .addComponent(buttonModifier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldCasse2, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldCasse1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fieldCasse2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelCote)
                                .addComponent(fieldCote)
                                .addComponent(listeCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelPrix)
                                .addComponent(fieldPrix)
                                .addComponent(labelCurrency))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelStock)
                                .addComponent(fieldStock))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelCasse)
                                .addComponent(fieldCasse)
                                .addComponent(fieldAjoutCasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fieldCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fieldNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelNom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fieldEditeur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fieldDevFab)))))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listeZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelZone)
                            .addComponent(fieldTxtAjoutZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonAjoutZone))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fieldEdition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelEdition))
                            .addComponent(labelTag)
                            .addComponent(fieldTag))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listePlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelPlateforme))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonChercher)
                            .addComponent(buttonNouveau)
                            .addComponent(buttonModifier)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fieldCasse1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("critResultat");
    }// </editor-fold>//GEN-END:initComponents

    private void listeCategorieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeCategorieItemStateChanged
        // TODO add your handling code here:
        if ("Jeu".equals((String)listeCategorie.getSelectedItem())) { fieldDevFab.setText("Développeur :"); }
            else { fieldDevFab.setText("Fabricant : "); }
    }//GEN-LAST:event_listeCategorieItemStateChanged

    private void listePlateformeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listePlateformeItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_listePlateformeItemStateChanged

    private void listePlateformeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listePlateformeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listePlateformeActionPerformed

    private void fieldEditeurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEditeurActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldEditeurActionPerformed

    private void buttonAjoutZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAjoutZoneActionPerformed
        try {
            // TODO add your handling code here:
            controleur.creerZone(fieldTxtAjoutZone.getText());
            
            /*
            *  Refresh la liste des Zones après Ajout
            */
            listeZone.removeAllItems();
            Vector<String> zones = controleur.listeZones();
            zones.add(0, "");
            zones.add("Autre");
            listeZone.setModel(new javax.swing.DefaultComboBoxModel<>(zones));
        } catch (EnregistrementExistantException ex) {
            this.parent.afficherErreur(ex);
        } catch (DonneesInsuffisantesException ex) {
            this.parent.afficherErreur(ex);
        } catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_buttonAjoutZoneActionPerformed

    private void buttonChercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChercherActionPerformed
        try {
            this.parent.lancerRecherche(toForm());}
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_buttonChercherActionPerformed

    private void buttonNouveauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNouveauActionPerformed
        try {
            Form f = this.toForm();
            if (f instanceof CodeBarreForm)
                this.parent.afficherLog(this.controleur.creer((CodeBarreForm) f).toString());
            else if (f instanceof ProduitForm)
                this.parent.afficherLog(this.controleur.creer((ProduitForm) f).toString());
            
            /*
            *  Refresh la liste Platforme après Ajout
            */
            listePlateforme.removeAllItems();
            Vector<String> plateformes = controleur.listeConsoles();
            plateformes.add(0, "");
            listePlateforme.setModel(new javax.swing.DefaultComboBoxModel<>(plateformes));
        }
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);}
        catch (DonneesInsuffisantesException ex) {
            this.parent.afficherErreur(ex);}
        catch (EnregistrementExistantException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_buttonNouveauActionPerformed

    private void buttonModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifierActionPerformed
        throw new UnsupportedOperationException("La modification de produit n'a pas encore été implémentée.");
    }//GEN-LAST:event_buttonModifierActionPerformed

    private void fieldCodeBarreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldCodeBarreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldCodeBarreActionPerformed

    private void listeZoneItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeZoneItemStateChanged
        // TODO add your handling code here:
        if ("Autre".equals((String)listeZone.getSelectedItem())) 
        {
            fieldTxtAjoutZone.setVisible(true); 
            buttonAjoutZone.setVisible(true);
        }
        else 
        {
            fieldTxtAjoutZone.setVisible(false); 
            buttonAjoutZone.setVisible(false);
        }
    }//GEN-LAST:event_listeZoneItemStateChanged

    private void fieldNomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldNomKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNomKeyPressed

    public void setForm(ProduitForm f)
    {
        this.selectedForm = f;
        
        if ("jeu".equals(f.getType()))
        {
            this.listeCategorie.setSelectedIndex(1); //type Jeu
            //this.fieldTag.setText(f.getTags());
            this.jTextAreaDescription.setText(f.getDescription());
        }
        else if ("console".equals(f.getType()))
            this.listeCategorie.setSelectedIndex(0); //type Console
        
        this.fieldCodeBarre.setText(f.getCodeBarre());
        this.fieldNom.setText(f.getNom());
        this.fieldEditeur.setText(f.getEditeur());
        this.fieldEdition.setText(f.getEdition());
        
        this.idVersionJeu = f.getIdVersionJeu();
        this.idVersionConsole = f.getIdVersionConsole();
        
        //TODO: à terminer (zones, plateformes... D'ailleurs, celles-ci doivent être initialisées proprement !)
    }
    private Form toForm() throws DonneeInvalideException
    {
        float prix;
        int stock;
        try {
            prix = Float.valueOf(fieldPrix.getText());
            stock = Integer.valueOf(fieldStock.getText());}
        catch (NumberFormatException nfe) {
            if (!"".equals(fieldPrix.getText()))
                throw new DonneeInvalideException("Erreur : veuillez saisir le 'prix' en notation anglo-saxonne (par exemple : 2.5");
            if (!"".equals(Integer.valueOf(fieldStock.getText())))
                throw new DonneeInvalideException("Erreur : veuillez saisir un entier dans le champ 'stock'");
            prix = 0f;
            stock = 0;
        }
            
        if("".equals(fieldNom.getText()) && "".equals(fieldEditeur.getText())
                && "".equals(listeZone.getSelectedItem())  && "".equals(listePlateforme.getSelectedItem())
                && "".equals(fieldEdition.getText()) && "".equals(fieldTag.getText()))
            return new CodeBarreForm(fieldCodeBarre.getText());
        else
            return new ProduitForm(this.idVersionConsole, this.idVersionJeu,
                    (String) listeCategorie.getSelectedItem(), fieldCodeBarre.getText(),
                    fieldNom.getText(), fieldEdition.getText(),
                    (String) listeZone.getSelectedItem(),
                    fieldEditeur.getText(), jTextAreaDescription.getText(),
                    fieldTag.getText(), (String) listePlateforme.getSelectedItem(),
                    prix, stock);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton buttonAjoutZone;
    public static javax.swing.JButton buttonChercher;
    public static javax.swing.JButton buttonModifier;
    public static javax.swing.JButton buttonNouveau;
    public static javax.swing.JSpinner fieldAjoutCasse;
    public static javax.swing.JLabel fieldCasse;
    public static javax.swing.JLabel fieldCasse1;
    public static javax.swing.JLabel fieldCasse2;
    public static javax.swing.JFormattedTextField fieldCodeBarre;
    public static javax.swing.JLabel fieldCote;
    public static javax.swing.JLabel fieldDevFab;
    public static javax.swing.JTextField fieldEditeur;
    public static javax.swing.JTextField fieldEdition;
    public static javax.swing.JTextField fieldNom;
    public static javax.swing.JLabel fieldPrix;
    public static javax.swing.JLabel fieldStock;
    public static javax.swing.JTextField fieldTag;
    public static javax.swing.JTextField fieldTxtAjoutZone;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea jTextAreaDescription;
    public static javax.swing.JLabel labelCasse;
    public static javax.swing.JLabel labelCategorie;
    public static javax.swing.JLabel labelCodeBarre;
    public static javax.swing.JLabel labelCote;
    public static javax.swing.JLabel labelCurrency;
    public static javax.swing.JLabel labelEdition;
    public static javax.swing.JLabel labelNom;
    public static javax.swing.JLabel labelPlateforme;
    public static javax.swing.JLabel labelPrix;
    public static javax.swing.JLabel labelStock;
    public static javax.swing.JLabel labelTag;
    public static javax.swing.JLabel labelZone;
    public static javax.swing.JComboBox<String> listeCategorie;
    public static javax.swing.JComboBox<String> listePlateforme;
    public static javax.swing.JComboBox<String> listeZone;
    // End of variables declaration//GEN-END:variables

}

