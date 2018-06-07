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
public class LigneFactureConsoleId  implements java.io.Serializable {


     private int idFacture;
     private int idVersionConsole;

    public LigneFactureConsoleId() {
    }

    public LigneFactureConsoleId(int idFacture, int idVersionConsole) {
       this.idFacture = idFacture;
       this.idVersionConsole = idVersionConsole;
    }
   
    public int getIdFacture() {
        return this.idFacture;
    }
    
    public void setIdFacture(int idTag) {
        this.idFacture = idTag;
    }
    public int getIdVersionConsole() {
        return this.idVersionConsole;
    }
    
    public void setIdVersionConsole(int idJeu) {
        this.idVersionConsole = idJeu;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof LigneFactureConsoleId) ) return false;
		 LigneFactureConsoleId castOther = ( LigneFactureConsoleId ) other; 
         
		 return (this.getIdFacture()==castOther.getIdFacture())
 && (this.getIdVersionConsole()==castOther.getIdVersionConsole());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdFacture();
         result = 37 * result + this.getIdVersionConsole();
         return result;
   }   


}
