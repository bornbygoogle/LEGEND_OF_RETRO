/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.controleur;

/**
 *
 * @author Home
 */
public class DonneesInsuffisantesException extends Exception
{
    public DonneesInsuffisantesException(String erreur)
    {
        super(erreur);
    }
}
