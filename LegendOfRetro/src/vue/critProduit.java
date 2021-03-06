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
import controleur.EnregistrementInexistantException;
import controleur.ResultatInvalideException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bornbygoogle
 */
public class critProduit extends javax.swing.JPanel
{
    private ProduitForm selectedForm;
    private Controleur controleur;
    private Chercheur parent;
    
    private int idVersionJeu;
    private int idVersionConsole;
    private String urlPhotoJeu;

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
        
        labelPhoto.setVisible(false);
        
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
        labelDescription = new javax.swing.JLabel();
        labelPhoto = new javax.swing.JLabel();
        labelCurrency = new javax.swing.JLabel();
        labelCote = new javax.swing.JLabel();
        fieldCote = new javax.swing.JLabel();
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
        fieldPrix = new javax.swing.JFormattedTextField();
        fieldStock = new javax.swing.JFormattedTextField();
        titre = new javax.swing.JLabel();

        setName("critResultat"); // NOI18N

        labelCodeBarre.setText("Code Barre : ");

        labelZone.setText("Zone : ");

        fieldNom.setToolTipText("Nom");

        fieldEditeur.setToolTipText("Developpeur / Fabricant");

        labelNom.setText("Nom : ");
        labelNom.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        fieldDevFab.setText("Fabricant : ");

        labelPlateforme.setText("Plateforme : ");
        labelPlateforme.setVisible(false);

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
        labelTag.setVisible(false);

        fieldEdition.setToolTipText("Edition");

        fieldTag.setToolTipText("Tag");
        fieldTag.setVisible(false);

        listePlateforme.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PC", "PS", " " }));
        listePlateforme.setVisible(false);
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

        labelDescription.setText("Description :");
        labelDescription.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelDescription.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        labelDescription.setVisible(false);

        labelPhoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPhoto.setText("Photo :");
        labelPhoto.setToolTipText("");
        labelPhoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelPhoto.setEnabled(false);
        labelPhoto.setFocusable(false);

        labelCurrency.setText("€");

        labelCote.setText("Cote :");

        fieldCote.setText("0");

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

        buttonChercher.setText("Chercher");
        buttonAjoutZone.setVisible(false);
        buttonChercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChercherActionPerformed(evt);
            }
        });

        buttonNouveau.setText("Nouveau");
        buttonNouveau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNouveauActionPerformed(evt);
            }
        });

        buttonModifier.setText("Modifier");
        buttonModifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModifierActionPerformed(evt);
            }
        });

        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setRows(5);
        jTextAreaDescription.setVisible(false);
        jScrollPane1.setViewportView(jTextAreaDescription);

        fieldPrix.setText("0");

        fieldStock.setText("0");

        titre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        titre.setText("Produit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelCategorie)
                                .addComponent(labelPlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(12, 12, 12)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(labelNom)
                                        .addComponent(fieldDevFab)
                                        .addComponent(labelZone)
                                        .addComponent(labelEdition)))
                                .addComponent(buttonChercher, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(labelCodeBarre))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(buttonNouveau)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonModifier))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(139, 139, 139)
                                        .addComponent(fieldTxtAjoutZone, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(listeCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldNom, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldEditeur, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(listeZone, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldEdition, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(listePlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonAjoutZone)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelTag)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldTag, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelCasse)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelPrix)
                                        .addComponent(labelStock)
                                        .addComponent(labelCote)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(fieldCasse)
                                        .addGap(43, 43, 43)
                                        .addComponent(fieldAjoutCasse, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(fieldPrix, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(fieldCote)
                                    .addComponent(fieldStock, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelDescription)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titre)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(listeCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fieldCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelCodeBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fieldNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelNom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fieldEditeur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldDevFab)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelCote)
                                    .addComponent(fieldCote))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelPrix)
                                    .addComponent(labelCurrency)
                                    .addComponent(fieldPrix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelStock)
                                    .addComponent(fieldStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelCasse)
                                    .addComponent(fieldCasse)
                                    .addComponent(fieldAjoutCasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listeZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldTxtAjoutZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonAjoutZone)
                            .addComponent(labelZone))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldEdition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelEdition)
                            .addComponent(labelTag)
                            .addComponent(fieldTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(listePlateforme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelPlateforme))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonNouveau)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonChercher)
                                .addComponent(buttonModifier))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelDescription)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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
            fieldTag.setVisible(true);
            labelPhoto.setVisible(true);
            labelDescription.setVisible(true);
            jTextAreaDescription.setVisible(true);
        }
        else 
        { 
            fieldDevFab.setText("Fabricant : ");
            labelPlateforme.setVisible(false);
            listePlateforme.setVisible(false);
            labelTag.setVisible(false);
            fieldTag.setVisible(false);
            labelPhoto.setVisible(false);
            labelDescription.setVisible(false);
            jTextAreaDescription.setVisible(false);
        }
        clean(); //réinitialisation des champs
    }//GEN-LAST:event_listeCategorieItemStateChanged

    private void listePlateformeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listePlateformeItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_listePlateformeItemStateChanged

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
            this.parent.lancerRecherche(toForm());
        }
        catch (DonneeInvalideException ex) {
            this.parent.afficherErreur(ex);
        }
    }//GEN-LAST:event_buttonChercherActionPerformed

    private void buttonNouveauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNouveauActionPerformed
        try {
            Form f = this.toForm();
            //controleur.creer(CodeBarreForm) lance une exception si le form n'est pas un ProduitForm.
            this.parent.afficherLog(this.controleur.creer((CodeBarreForm) f).toString());
            //exécuté seulement si le form est un ProduitForm
            this.selectedForm = (ProduitForm) f;
            
            /*
            *  Refresh la liste Platforme après Ajout
            */
            listePlateforme.removeAllItems();
            Vector<String> plateformes = controleur.listeConsoles();
            plateformes.add(0, "");
            listePlateforme.setModel(new javax.swing.DefaultComboBoxModel<>(plateformes));
        }
        catch (DonneeInvalideException | DonneesInsuffisantesException |
                EnregistrementExistantException | ResultatInvalideException ex) {
            this.parent.afficherErreur(ex);}
    }//GEN-LAST:event_buttonNouveauActionPerformed

    private void buttonModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifierActionPerformed
        try {
            Form f = toForm();
            if (!(f instanceof ProduitForm))
                throw new DonneesInsuffisantesException(
                        "Erreur : on ne peut pas effacer toutes ces données sur le produit");
            this.parent.afficherLog(
                    this.controleur.modifier((ProduitForm) f).toString());
            this.selectedForm = (ProduitForm) f;
            setForm(this.selectedForm); //update affichage dans critProduit (normalement inutile)
        }
        catch (DonneesInsuffisantesException ex) {this.parent.afficherErreur(ex);}
        catch (DonneeInvalideException ex) {this.parent.afficherErreur(ex);}
        catch (EnregistrementInexistantException ex) {this.parent.afficherErreur(ex);} catch (IOException ex) {
            Logger.getLogger(critProduit.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_buttonModifierActionPerformed

    private void listeZoneItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listeZoneItemStateChanged
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

    private void listePlateformeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listePlateformeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listePlateformeActionPerformed

    public void clean()
    {
        this.fieldAjoutCasse.setValue(0);
        this.fieldCodeBarre.setText("");
        this.fieldEditeur.setText("");
        this.fieldEdition.setText("");
        this.fieldNom.setText("");
        this.fieldPrix.setValue(String.format("%.2f", 0.0f));
        this.fieldStock.setText("0");
        this.fieldCote.setText(String.format("%.2f", 0.0f));
        this.fieldTag.setText("");
        this.fieldTxtAjoutZone.setText("");
        this.labelPhoto.setIcon(null);
        this.jTextAreaDescription.setText("");
        this.listePlateforme.setSelectedIndex(0);
        this.listeZone.setSelectedIndex(0);
        this.selectedForm = null;
        this.idVersionConsole = -1;
        this.idVersionJeu = -1;
        parent.lancerRecherche(null);
    }
    public void setCodeBarre(String cb)
    {
        this.fieldCodeBarre.setText(cb);
    }
    
    /********
    *
    *   Fonction utilisé pour remplir les champs dans le menu
    *   @param ProduitForm f
    *   @return void ( le remplissage se fait directement par le menu )
    * 
    *********/
    public void setForm(ProduitForm f) throws MalformedURLException, IOException
    {
        this.selectedForm = f;
        String errors = "";
        
        if ("Jeu".equals(f.getType()))
        {
            this.listeCategorie.setSelectedIndex(1); //type Jeu
            this.fieldTag.setText(f.getTags());
            this.jTextAreaDescription.setText(f.getDescription());
            //plateforme
            int i=0;
            boolean found = false;
            String dansListe = null;
            String plateforme = f.getPlateforme();
            while (!found && i < listePlateforme.getModel().getSize())
            {
                dansListe = listePlateforme.getModel().getElementAt(i);
                found = plateforme.equals(dansListe);
                i++;
            }
            if (found)
                listePlateforme.getModel().setSelectedItem(dansListe);
            else
                errors = errors.concat("Erreur lors de la sélection de la plateforme " + plateforme
                        + " : plateforme non trouvée dans la liste déroulante. \n");
            
            //Affichage photo dans le cadre          
            if (f.getPhoto()!="") { controleur.setPhotoProduct(f.getPhoto()); }
            
        }
        else if ("console".equals(f.getType()))
            this.listeCategorie.setSelectedIndex(0); //type Console
        
        this.fieldCodeBarre.setText(f.getCodeBarre());
        this.fieldNom.setText(f.getNom());
        this.fieldEditeur.setText(f.getEditeur());
        this.fieldEdition.setText(f.getEdition());
        this.fieldPrix.setText(String.valueOf(String.format("%.2f", f.getPrix())));
        this.fieldStock.setText(String.valueOf(f.getStock()));
        this.fieldCote.setText(String.valueOf(String.format("%.2f", f.getCote())));
    
        //zone
        int i = 0;
        boolean found = false;
        String dansListe = null;
        String zone = f.getZone();
        while (!found && i < listeZone.getModel().getSize())
        {
            dansListe = listeZone.getModel().getElementAt(i);
            found = zone.equals(dansListe);
            i++;
        }
        if (found)
            listeZone.getModel().setSelectedItem(dansListe);
        else
            errors = errors.concat("Erreur lors de la sélection de la zone " + zone
                    + " : zone non trouvée dans la liste déroulante. \n");
                
        
        this.idVersionJeu = f.getIdVersionJeu();
        this.idVersionConsole = f.getIdVersionConsole();

        if (!"".equals(errors))
            this.parent.afficherErreur(new Exception(errors));

    }
    
    /********
    *
    *   Fonction utilisé pour récupérer les champs dans le menu
    *   @param void
    *   @return Form ( récupération tous les champs remplis dans le menu et popularise le form avec.
    * 
    *********/
    private Form toForm() throws DonneeInvalideException
    {
        float prix = 0f;
        float cote = 0f;
        int stock = 0;
        try {
            prix = Float.valueOf(fieldPrix.getText());
            stock = Integer.valueOf(fieldStock.getText());}
        catch (NumberFormatException nfe) {
            if (!"".equals(fieldPrix.getText()))
                throw new DonneeInvalideException("Erreur : veuillez saisir le 'prix' en notation anglo-saxonne (par exemple : 2.5)");
            if (!"".equals(fieldStock.getText()))
                throw new DonneeInvalideException("Erreur : veuillez saisir un entier dans le champ 'stock'");
        }
            
        if("".equals(fieldNom.getText()) && "".equals(fieldEditeur.getText())
                && "".equals(listeZone.getSelectedItem())  && "".equals(listePlateforme.getSelectedItem())
                && "".equals(fieldEdition.getText()) && "".equals(fieldTag.getText()))
            return new CodeBarreForm(fieldCodeBarre.getText());
        else
        {
            ProduitForm retour = new ProduitForm();
            retour.setIdVersionConsole(this.idVersionConsole);
            retour.setIdVersionJeu(this.idVersionJeu);
            retour.setType((String) listeCategorie.getSelectedItem());
            retour.setCodeBarre(fieldCodeBarre.getText());
            retour.setNom(fieldNom.getText());
            retour.setEdition(fieldEdition.getText());
            retour.setZone((String) listeZone.getSelectedItem());
            retour.setEditeur(fieldEditeur.getText());
            retour.setPhoto("");
            retour.setDescription(jTextAreaDescription.getText());
            retour.setTags(fieldTag.getText());
            retour.setPlateforme((String) listePlateforme.getSelectedItem());
            retour.setPrix(prix);
            retour.setStock(stock);
            retour.setCote(cote);
            return retour;
        }
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton buttonAjoutZone;
    public static javax.swing.JButton buttonChercher;
    public static javax.swing.JButton buttonModifier;
    public static javax.swing.JButton buttonNouveau;
    public static javax.swing.JSpinner fieldAjoutCasse;
    public static javax.swing.JLabel fieldCasse;
    public static javax.swing.JFormattedTextField fieldCodeBarre;
    public static javax.swing.JLabel fieldCote;
    public static javax.swing.JLabel fieldDevFab;
    public static javax.swing.JTextField fieldEditeur;
    public static javax.swing.JTextField fieldEdition;
    public static javax.swing.JTextField fieldNom;
    public static javax.swing.JFormattedTextField fieldPrix;
    public static javax.swing.JFormattedTextField fieldStock;
    public static javax.swing.JTextField fieldTag;
    public static javax.swing.JTextField fieldTxtAjoutZone;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea jTextAreaDescription;
    public static javax.swing.JLabel labelCasse;
    public static javax.swing.JLabel labelCategorie;
    public static javax.swing.JLabel labelCodeBarre;
    public static javax.swing.JLabel labelCote;
    public static javax.swing.JLabel labelCurrency;
    public static javax.swing.JLabel labelDescription;
    public static javax.swing.JLabel labelEdition;
    public static javax.swing.JLabel labelNom;
    public static javax.swing.JLabel labelPhoto;
    public static javax.swing.JLabel labelPlateforme;
    public static javax.swing.JLabel labelPrix;
    public static javax.swing.JLabel labelStock;
    public static javax.swing.JLabel labelTag;
    public static javax.swing.JLabel labelZone;
    public static javax.swing.JComboBox<String> listeCategorie;
    public static javax.swing.JComboBox<String> listePlateforme;
    public static javax.swing.JComboBox<String> listeZone;
    public static javax.swing.JLabel titre;
    // End of variables declaration//GEN-END:variables

}

