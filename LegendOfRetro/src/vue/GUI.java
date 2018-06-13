/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.Controleur;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Home
 */
public class GUI extends JFrame implements ActionListener
{
    private Controleur controleur;
    
    public enum Menu {AUCUN, PRODUIT, ACHAT, VENTE, PERSONNE, PROMO};
    private JPanel menuOuvert;
    private JPanel menuPanel;
    private JButton buttonProduit;
    private JButton buttonAchat;
    private JButton buttonVente;
    private JButton buttonClient;
    private JButton buttonPromo;
    private int largueur;
    private int longueur;
    
    public GUI(Controleur controleur)
    {
        super();
        
        // définition des dimensions des menus
        largueur = 700;
        longueur = 1100;
        
        //initialisation des composants
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new java.awt.Dimension(150, largueur));
        
        buttonProduit = new JButton();
        buttonProduit.setText("Produit");
        buttonProduit.addActionListener(this);

        buttonAchat = new JButton();
        buttonAchat.setText("Achat");
        buttonAchat.addActionListener(this);
        
        buttonVente = new JButton();
        buttonVente.setText("Vente");
        buttonVente.addActionListener(this);
        
        buttonClient = new JButton();
        buttonClient.setText("Client / Fournisseur");
        buttonClient.addActionListener(this);
        
        buttonPromo = new JButton();
        buttonPromo.setText("Promo");
        buttonPromo.addActionListener(this);

        //les lignes suivantes servent à dessiner le menu. Elles ont été générées automatiquement par un form.
        GroupLayout menuPanelLayout = new GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(buttonProduit, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(buttonClient, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(buttonAchat, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonVente, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonPromo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonProduit, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonClient, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonAchat, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonVente, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonPromo, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //mise en forme de la fenêtres.
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(this.menuPanel, BorderLayout.WEST);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(longueur, largueur);
        this.setVisible(true);
        this.controleur = controleur;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == buttonAchat)
            ouvrirMenu(Menu.ACHAT);
        else if (e.getSource() == buttonVente)
            ouvrirMenu(Menu.VENTE);
        else if (e.getSource() == buttonClient)
            ouvrirMenu(Menu.PERSONNE);
        else if (e.getSource() == buttonProduit)
            ouvrirMenu(Menu.PRODUIT);
        else if (e.getSource() == buttonPromo)
            ouvrirMenu(Menu.PROMO);
        else //erreur
            throw new UnsupportedOperationException("Erreur GUI : origine de l'action inconnue.");
    }
    
    public void ouvrirMenu(Menu m, String parametreDInitialisation)
    {
        if (m == Menu.PRODUIT)
        {
            fermerMenu();
            this.menuOuvert = new menuProduit(this.controleur, parametreDInitialisation);
            Container c = this.getContentPane();
            c.add(this.menuOuvert, BorderLayout.CENTER);
            this.setContentPane(c);
            buttonVente.setBackground(null);
            buttonClient.setBackground(null);
            buttonPromo.setBackground(null);
            buttonAchat.setBackground(null);
            buttonProduit.setBackground(Color.GREEN);
        }
        else //ignorer le second paramètre
            ouvrirMenu(m);
    }
    public void ouvrirMenu(Menu m)
    {
        if (m == Menu.PRODUIT)
        {
            fermerMenu();
            this.menuOuvert = new menuProduit(this.controleur);
            Container c = this.getContentPane();
            c.add(this.menuOuvert, BorderLayout.CENTER);
            this.setContentPane(c);
            buttonVente.setBackground(null);
            buttonClient.setBackground(null);
            buttonPromo.setBackground(null);
            buttonAchat.setBackground(null);
            buttonProduit.setBackground(Color.blue);
        }
        else if (m == Menu.PERSONNE)
        {
            fermerMenu();
            this.menuOuvert = new menuPersonne(this.controleur);
            Container c = this.getContentPane();
            c.add(this.menuOuvert, BorderLayout.CENTER);
            this.setContentPane(c);
            buttonVente.setBackground(null);
            buttonPromo.setBackground(null);
            buttonProduit.setBackground(null);
            buttonAchat.setBackground(null);
            buttonClient.setBackground(Color.blue);
            
            
        }
        else if (m == Menu.ACHAT)
        {
            fermerMenu();
            this.menuOuvert = new menuAchat(this.controleur, this);
            Container c = this.getContentPane();
            c.add(this.menuOuvert, BorderLayout.CENTER);
            this.setContentPane(c);
            buttonVente.setBackground(null);
            buttonClient.setBackground(null);
            buttonPromo.setBackground(null);
            buttonProduit.setBackground(null);
            buttonAchat.setBackground(Color.blue);
        }
        else if (m == Menu.VENTE)
        {
            fermerMenu();
            this.menuOuvert = new menuVente(this.controleur);
            Container c = this.getContentPane();
            c.add(this.menuOuvert, BorderLayout.CENTER);
            this.setContentPane(c);
            buttonClient.setBackground(null);
            buttonPromo.setBackground(null);
            buttonProduit.setBackground(null);
            buttonAchat.setBackground(null);
            buttonPromo.setBackground(null);
            buttonVente.setBackground(Color.blue);
        }
        
        else if (m == Menu.PROMO)
        {
            fermerMenu();
            this.menuOuvert = new menuPromo(this.controleur);
            Container c = this.getContentPane();
            c.add(this.menuOuvert, BorderLayout.CENTER);
            this.setContentPane(c);
            buttonVente.setBackground(null);
            buttonClient.setBackground(null);
            buttonClient.setBackground(null);
            buttonPromo.setBackground(null);
            buttonAchat.setBackground(null);
            buttonProduit.setBackground(null);
            buttonPromo.setBackground(Color.blue);
        }
        else
            throw new UnsupportedOperationException("Le programme ne gère pas ce menu.");
    }
    public void fermerMenu()
    {
        Container c = this.getContentPane();
        c.removeAll();
        c.add(this.menuPanel, BorderLayout.WEST);
        this.setContentPane(c);
        
        this.menuOuvert = null;
    }
    
}
