/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Home
 */
public class HQLRecherche
{
    public enum Operateur
    {
        EGAL, INF, INFOUEGAL, SUP, SUPOUEGAL, DIFF, LIKE, IN, NOTIN, ISNULL, ISNOTNULL;
        public String toString()
        {
            String ret;
            if (this.equals(EGAL))
                ret = "=";
            else if (this.equals(INF))
                ret = "<";
            else if (this.equals(INFOUEGAL))
                ret = "<=";
            else if (this.equals(SUP))
                ret = ">";
            else if (this.equals(SUPOUEGAL))
                ret = ">=";
            else if (this.equals(DIFF))
                ret = "!=";
            else if (this.equals(LIKE))
                ret = "LIKE";
            else if (this.equals(IN))
                ret = "in";
            else if (this.equals(NOTIN))
                ret = "not in";
            else if (this.equals(ISNULL))
                ret = "IS NULL";
            else //if (this.equals(ISNOTNULL))
                ret = "IS NOT NULL";
            return ret;
        }
    };
    
    protected boolean imbriquee; //par défaut, false
    protected String select;
    protected String classe;
    /*taille 3 : membre de gauche, memebre de droite, opérateur*/
    protected Vector<String[]> conditions; 
    
    public HQLRecherche(String classe)
    {
        this.imbriquee = false;
        this.select = "";
        this.classe = classe;
        this.conditions = new Vector<String[]>();
    }
    
    //les conditions sont cumulatives (ET)
    public void addCondition(String membreGauche, String membreDroite, Operateur operateur)
    {
        if (operateur.equals(Operateur.LIKE))
            membreDroite = "'%".concat(membreDroite).concat("%'");
        String[] c = {membreGauche, "'" + membreDroite + "'", operateur.toString()};
        this.conditions.add(c);
    }
    public void addCondition(String membreGauche, int membreDroite, Operateur operateur)
    {
        String[] c = {membreGauche, Integer.toString(membreDroite), operateur.toString()};
        this.conditions.add(c);
    }
    
    public void setSelect(String select)        {this.select = select;}
    public void setImbriquee(boolean i)     {this.imbriquee = i;}
    
    //Attention, ne garantit pas que la requête fonctionne !
    public String toString()
    {
        String ret = "";
        
        if (this.imbriquee)
            ret = ret.concat("(");
        
        if (!"".equals(this.select))
            ret = ret.concat("select " + this.select);
        
        ret = ret.concat(" from " + this.classe);

        if (!this.conditions.isEmpty())
        {
            Iterator<String[]> itCond = this.conditions.iterator();
            String[] cond;
            if (itCond.hasNext()) //normalement, toujours vrai, puisque conditions n'est pas vide.
            {
                cond = itCond.next();
                ret = ret.concat(" where " + cond[0] + " " + cond[2] + " " + cond[1]);
            }
            while (itCond.hasNext())
            {
                cond = itCond.next();
                ret = ret.concat(" and " + cond[0] + " " + cond[2] + " " + cond[1]);
            }
        }
        
        if (this.imbriquee)
            ret = ret.concat(" ) ");
        
        return ret;
    }
}
