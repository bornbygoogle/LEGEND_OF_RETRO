package LOREntities;
// Generated May 23, 2018 12:38:35 AM by Hibernate Tools 4.3.1



/**
 * VersionJeu generated by hbm2java
 */
public class VersionJeu  implements java.io.Serializable {


     private Integer idVersionJeu;
     private Console console;
     private Jeu jeu;
     private Zone zone;
     private String codeBarre;
     private String edition;
     private float prix;
     private int stock;

    public VersionJeu() {
    }

    public VersionJeu(Console console, Jeu jeu, Zone zone, String codeBarre, String edition, float prix, int stock) {
       this.console = console;
       this.jeu = jeu;
       this.zone = zone;
       this.codeBarre = codeBarre;
       this.edition = edition;
       this.prix = prix;
       this.stock = stock;
    }
   
    public Integer getIdVersionJeu() {
        return this.idVersionJeu;
    }
    
    public void setIdVersionJeu(Integer idVersionJeu) {
        this.idVersionJeu = idVersionJeu;
    }
    public Console getConsole() {
        return this.console;
    }
    
    public void setConsole(Console console) {
        this.console = console;
    }
    public Jeu getJeu() {
        return this.jeu;
    }
    
    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }
    public Zone getZone() {
        return this.zone;
    }
    
    public void setZone(Zone zone) {
        this.zone = zone;
    }
    public String getCodeBarre() {
        return this.codeBarre;
    }
    
    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }
    public String getEdition() {
        return this.edition;
    }
    
    public void setEdition(String edition) {
        this.edition = edition;
    }
    public float getPrix() {
        return this.prix;
    }
    
    public void setPrix(float prix) {
        this.prix = prix;
    }
    public int getStock() {
        return this.stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }




}


