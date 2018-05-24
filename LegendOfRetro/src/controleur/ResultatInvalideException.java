/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.util.Vector;
import bean.ProduitForm;

/**
 *
 * @author Home
 */
public class ResultatInvalideException extends Exception
{
    private Vector<ProduitForm> resultat;
    public ResultatInvalideException(String message, Vector<ProduitForm> res)
    {
        super(message);
        this.resultat = res;
    }
    public Vector<ProduitForm> getResultat()            {return this.resultat;}
}
