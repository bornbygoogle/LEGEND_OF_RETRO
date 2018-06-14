/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

/**
 *
 * @author Home
 */
public class PersonneForm extends Form
{
    private int idPersonne;
    
    private String nom;
    private String prenom;
    private String societe;
    private String adresse;
    private String ville;
    private String codePostal;
    private String pays;
    private String dateNaiss;
    private String mail;
    private String telephone;
    
    public PersonneForm()
    {
        this.idPersonne = -1;
        this.prenom = "";
        this.nom = "";
        this.adresse = "";
        this.ville = "";
        this.societe = "N/A";
        this.codePostal = "";
        this.pays = "";
        this.dateNaiss = "";
        this.mail = "";
        this.telephone = "";
    }
    
    public int getIdPersonne()                      {return this.idPersonne;}
    public String getPrenom()                       {return this.prenom;}
    public String getNom()                          {return this.nom;}
    public String getSociete()                      {return this.societe;}
    public String getAdresse()                      {return this.adresse;}
    public String getVille()                        {return this.ville;}
    public String getCodePostal()                   {return this.codePostal;}
    public String getPays()                         {return this.pays;}
    public String getDateNaissance()                {return this.dateNaiss;}
    public String getMail()                         {return this.mail;}
    public String getTelephone()                    {return this.telephone;}
    
    public void setPrenom(String prenom)            {this.prenom = prenom;}
    public void setNom(String nom)                  {this.nom = nom;}
    public void setSociete(String societe)          {this.societe = societe;}
    public void setAdresse(String adresse)          {this.adresse = adresse;}
    public void setVille(String ville)              {this.ville = ville;}
    public void setCodePostal(String cp)            {this.codePostal = cp;}
    public void setPays(String pays)                {this.pays = pays;}
    public void setDateNaissance(String date)       {this.dateNaiss = date;}
    public void setMail(String mail)                {this.mail = mail;}
    public void setTelephone(String tel)            {this.telephone = tel;}
    
}
