package LOREntities;
// Generated May 23, 2018 12:38:35 AM by Hibernate Tools 4.3.1



/**
 * DecrireId generated by hbm2java
 */
public class DecrireId  implements java.io.Serializable {


     private int idTag;
     private int idJeu;

    public DecrireId() {
    }

    public DecrireId(int idTag, int idJeu) {
       this.idTag = idTag;
       this.idJeu = idJeu;
    }
   
    public int getIdTag() {
        return this.idTag;
    }
    
    public void setIdTag(int idTag) {
        this.idTag = idTag;
    }
    public int getIdJeu() {
        return this.idJeu;
    }
    
    public void setIdJeu(int idJeu) {
        this.idJeu = idJeu;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof DecrireId) ) return false;
		 DecrireId castOther = ( DecrireId ) other; 
         
		 return (this.getIdTag()==castOther.getIdTag())
 && (this.getIdJeu()==castOther.getIdJeu());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdTag();
         result = 37 * result + this.getIdJeu();
         return result;
   }   


}


