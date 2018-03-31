/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

import java.util.Vector;

/**
 *
 * @author Home
 */
public class Jeu
{
    private int id;
    private String nom;
    private String description;
    private Editeur editeur;
    private Vector<Tag> tags;
    
    public Jeu()
    {
        this.id = -1;
        this.nom = "";
        this.description = "";
        this.editeur = null;
        this.tags = new Vector<Tag>();
    }
    public Jeu(int id, String nom, String descr, Editeur editeur, Vector<Tag> tags)
    {
        this.id = id;
        this.nom = nom;
        this.description = descr;
        this.editeur = editeur;
        this.tags = tags;
    }
    public int getId()                      {return this.id;}
    public String getNom()                  {return this.nom;}
    public String getDescription()          {return this.description;}
    public Editeur getEditeur()             {return this.editeur;}
    public Vector<Tag> getTags()            {return this.tags;}
    public void setId(int id)               {this.id = id;}
    public void setNom(String nom)          {this.nom = nom;}
    public void setDescription(String descr){this.description = descr;}
    public void setEditeur(Editeur e)       {this.editeur = e;}
    public void setTags(Vector<Tag> tags)   {this.tags = tags;}
}
