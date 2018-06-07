package LOREntities;
// Generated Jun 7, 2018 5:40:08 PM by Hibernate Tools 4.3.1



/**
 * LigneFactureJeuId generated by hbm2java
 */
public class LigneFactureJeuId  implements java.io.Serializable {


     private int idFacture;
     private int idVersionJeu;

    public LigneFactureJeuId() {
    }

    public LigneFactureJeuId(int idFacture, int idVersionJeu) {
       this.idFacture = idFacture;
       this.idVersionJeu = idVersionJeu;
    }
   
    public int getIdFacture() {
        return this.idFacture;
    }
    
    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }
    public int getIdVersionJeu() {
        return this.idVersionJeu;
    }
    
    public void setIdVersionJeu(int idVersionJeu) {
        this.idVersionJeu = idVersionJeu;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof LigneFactureJeuId) ) return false;
		 LigneFactureJeuId castOther = ( LigneFactureJeuId ) other; 
         
		 return (this.getIdFacture()==castOther.getIdFacture())
 && (this.getIdVersionJeu()==castOther.getIdVersionJeu());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdFacture();
         result = 37 * result + this.getIdVersionJeu();
         return result;
   }   


}

