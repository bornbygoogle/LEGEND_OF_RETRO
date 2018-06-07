/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LOREntities;

/**
 *
 * @author Home
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
    
    public void setIdFacture(int idTag) {
        this.idFacture = idTag;
    }
    public int getIdVersionJeu() {
        return this.idVersionJeu;
    }
    
    public void setIdVersionJeu(int idJeu) {
        this.idVersionJeu = idJeu;
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
