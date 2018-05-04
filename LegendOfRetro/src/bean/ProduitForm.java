/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.Vector;

/**
 *
 * @author Adrien Marchand
 */
public class ProduitForm extends Form
{
    private int idConsole;
    private int idVersionConsole;
    private int idJeu;
    private int idVersionJeu;
    private int idEditeur;
    
    private String type;
    private String codeBarre;
    private String nom;
    private String edition;
    private String zone;
    private String editeur;
    private String description;
    //private String tags;
    private String plateforme;
    private float prix;
    private int stock;
    
    public ProduitForm()
    {
        this.idVersionConsole = -1;
        this.idConsole = -1;
        this.idJeu = -1;
        this.idVersionJeu = -1;
        this.idEditeur = -1;
        this.type = "";
        this.codeBarre = "";
        this.nom = "";
        this.edition = "";
        this.zone = "";
        this.editeur = "";
        this.description = "";
        //this.tags = "";
        this.prix = -1;
        this.stock = -1;
    }
    public ProduitForm(
            int idConsole, int idVersionConsole,
            int idJeu, int idVersionJeu, int idEditeur,
            String type, String cb, String nom, String edition, String zone,
            String editeur, String description, /*String tags,*/ String plateforme,
            float prix, int stock)
    {
        this.idConsole = idConsole;
        this.idVersionConsole = idVersionConsole;
        this.idJeu = idJeu;
        this.idVersionJeu = idVersionJeu;
        this.idEditeur = idEditeur;
        this.type = type;
        this.codeBarre = cb;
        this.nom = nom;
        this.edition = edition;
        this.zone = zone;
        this.editeur = editeur;
        this.description = description;
        this.plateforme = plateforme;
        //this.tags = tags;
        this.prix = prix;
        this.stock = stock;
    }
    
    public int getIdConsole()      {return this.idConsole;}
    public int getIdVersionConsole()      {return this.idVersionConsole;}
    public int getIdJeu()      {return this.idJeu;}
    public int getIdVersionJeu()      {return this.idVersionJeu;}
    public int getIdEditeur()      {return this.idEditeur;}
    public String getType()      {return this.type;}
    public String getCodeBarre()      {return this.codeBarre;}
    public String getNom()      {return this.nom;}
    public String getEdition()      {return this.edition;}
    public String getZone()      {return this.zone;}
    public String getEditeur()      {return this.editeur;}
    public String getDescription()      {return this.description;}
    //public String getTags()      {return this.tags;}
    public String getPlateforme()      {return this.plateforme;}
    public float getPrix()      {return this.prix;}
    public int getStock()      {return this.stock;}
    
    public void setIdConsole(int id)      {this.idConsole = id;}
    public void setIdVersionConsole(int id)      {this.idVersionConsole = id;}
    public void setIdJeu(int id)      {this.idJeu = id;}
    public void setIdVersionJeu(int id)      {this.idVersionJeu = id;}
    public void setIdEditeur(int id)      {this.idEditeur = id;}
    public void setType(String type)      {this.type = type;}
    public void setCodeBarre(String cb)      {this.codeBarre = cb;}
    public void setNom(String nom)      {this.nom = nom;}
    public void setEdition(String edition)      {this.edition = edition;}
    public void setZone(String zone)      {this.zone = zone;}
    public void setEditeur(String editeur)      {this.editeur = editeur;}
    public void setDescription(String descr)      {this.description = descr;}
   // public void setTags(String tags)      {this.tags = tags;}
    public void setPlateforme(String pf)      {this.plateforme = pf;}
    public void setPrix(float p)      {this.prix = p;}
    public void setStock(int n)      {this.stock = n;}
    
}
