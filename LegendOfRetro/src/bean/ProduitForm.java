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
public class ProduitForm extends CodeBarreForm
{
    private int idVersionConsole;
    private int idVersionJeu;
    
    private String type;
    private String nom;
    private String edition;
    private String zone;
    private String editeur;
    private String photo;
    private String description;
    private String tags;
    private String plateforme;
    private float prix;
    private int stock;
    private float cote;
    
    public ProduitForm()
    {
        this.idVersionConsole = -1;
        this.idVersionJeu = -1;
        this.type = "";
        this.nom = "";
        this.edition = "";
        this.zone = "";
        this.editeur = "";
        this.photo = "";
        this.description = "";
        this.tags = "";
        this.plateforme = "";
        this.prix = -1;
        this.stock = -1;
        this.cote = 1.0f;
    }
    public ProduitForm(
            int idVersionConsole, int idVersionJeu,
            String type, String cb, String nom, String edition, String zone,
            String editeur, String photo, String description, String tags, String plateforme,
            float prix, int stock, float cote)
    {
        super(cb);
        this.idVersionConsole = idVersionConsole;
        this.idVersionJeu = idVersionJeu;
        this.type = type;
        this.nom = nom;
        this.edition = edition;
        this.zone = zone;
        this.editeur = editeur;
        this.photo = photo;
        this.description = description;
        this.plateforme = plateforme;
        this.tags = tags;
        this.prix = prix;
        this.stock = stock;
        this.cote = cote;
    }
    
    public int getIdVersionConsole()      {return this.idVersionConsole;}
    public int getIdVersionJeu()      {return this.idVersionJeu;}
    public String getType()      {return this.type;}
    public String getNom()      {return this.nom;}
    public String getEdition()      {return this.edition;}
    public String getZone()      {return this.zone;}
    public String getEditeur()      {return this.editeur;}
    public String getPhoto()  { return this.photo; }
    public String getDescription()      {return this.description;}
    public String getTags()      {return this.tags;}
    public String getPlateforme()      {return this.plateforme;}
    public float getPrix()      {return this.prix;}
    public int getStock()      {return this.stock;}
    public float getCote()     {return this.cote;}
    
    public void setIdVersionConsole(int id)      {this.idVersionConsole = id;}
    public void setIdVersionJeu(int id)      {this.idVersionJeu = id;}
    public void setType(String type)      {this.type = type;}
    public void setNom(String nom)      {this.nom = nom;}
    public void setEdition(String edition)      {this.edition = edition;}
    public void setZone(String zone)      {this.zone = zone;}
    public void setEditeur(String editeur)      {this.editeur = editeur;}
    public void setDescription(String descr)      {this.description = descr;}
    public void setPhoto(String photo)      {this.photo = photo;}
    public void setTags(String tags)      {this.tags = tags;}
    public void setPlateforme(String pf)      {this.plateforme = pf;}
    public void setPrix(float p)      {this.prix = p;}
    public void setStock(int n)      {this.stock = n;}
    public void setCote(float c)      {this.cote = c;}    
}
