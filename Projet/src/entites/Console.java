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
public class Console
{
    private int id;
    private String nom;
    private Fabricant fabricant;
    
    public Console()
    {
        this.id = -1;
        this.nom = "";
        this.fabricant = null;
    }
    public Console(int id, String nom, Fabricant fabricant)
    {
        this.id = id;
        this.nom = nom;
        this.fabricant = fabricant;
    }
    public int getId()                      {return this.id;}
    public String getNom()                  {return this.nom;}
    public Fabricant getFabricant()         {return this.fabricant;}
    public void setId(int id)               {this.id = id;}
    public void setNom(String nom)          {this.nom = nom;}
    public void setFabricant(Fabricant f)   {this.fabricant = f;}
}
