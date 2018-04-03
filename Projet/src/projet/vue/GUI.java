/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import projet.controleur.Controleur;

/**
 *
 * @author Adrien Marchand
 */
public class GUI extends JFrame implements ActionListener, Vue
{
    private enum MenuActif {PRODUIT};
    private Controleur controleur;
    
    
    public GUI(Controleur c)
    {
        this.controleur = c;
    }
    @Override
    public void setContoleur(Controleur c)
    {
        this.controleur = c;
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //si 
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
