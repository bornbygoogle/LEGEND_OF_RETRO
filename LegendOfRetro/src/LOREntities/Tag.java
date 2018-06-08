package LOREntities;
// Generated Jun 9, 2018 12:02:55 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Tag generated by hbm2java
 */
public class Tag  implements java.io.Serializable {


     private Integer idTag;
     private String labelTag;
     private Set decrires = new HashSet(0);

    public Tag() {
    }

	
    public Tag(String labelTag) {
        this.labelTag = labelTag;
    }
    public Tag(String labelTag, Set decrires) {
       this.labelTag = labelTag;
       this.decrires = decrires;
    }
   
    public Integer getIdTag() {
        return this.idTag;
    }
    
    public void setIdTag(Integer idTag) {
        this.idTag = idTag;
    }
    public String getLabelTag() {
        return this.labelTag;
    }
    
    public void setLabelTag(String labelTag) {
        this.labelTag = labelTag;
    }
    public Set getDecrires() {
        return this.decrires;
    }
    
    public void setDecrires(Set decrires) {
        this.decrires = decrires;
    }




}


