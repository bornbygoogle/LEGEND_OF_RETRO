/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

/**
 *
 * @author Home
 */
public class VersionJeu
{
    private int id;
    private String codeBarre;
    private String edition;
    private Zone zone;
    private Console plateforme;
    private Jeu jeu;
    private float prix;
    private int stock;
    
    public VersionJeu()
    {
        this.id = -1;
        this.codeBarre = "";
        this.edition = "";
        this.zone = null;
        this.plateforme = null;
        this.jeu = null;
        this.stock = -1;
        this.prix = -1f;
    }
    public VersionJeu(int id, String cb, String edition,
            Zone zone, Console plateforme, Jeu jeu,
            int stock, float prix)
    {
        this.id = id;
        this.codeBarre = cb;
        this.edition = edition;
        this.zone = zone;
        this.plateforme = plateforme;
        this.jeu = jeu;
        this.stock = stock;
        this.prix = prix;
    }
    public int getId()  {return this.id;}
    public String getCodeBarre()  {return this.codeBarre;}
    public String getEdition()  {return this.edition;}
    public Zone getZone()  {return this.zone;}
    public Console getPlateforme()  {return this.plateforme;}
    public Jeu getJeu()  {return this.jeu;}
    public int getStock()  {return this.stock;}
    public float getPrix()  {return this.prix;}
    
    public void setId(int id)  {this.id = id;}
    public void setCodeBarre(String cb)  {this.codeBarre = cb;}
    public void setEdition(String edition)  {this.edition = edition;}
    public void setZone(Zone zone)  {this.zone = zone;}
    public void setPlateforme(Console plateforme)  {this.plateforme = plateforme;}
    public void setJeu(Jeu jeu)  {this.jeu = jeu;}
    public void setStock(int stock)  {this.stock = stock;}
    public void setPrix(float prix)  {this.prix = prix;}
}
