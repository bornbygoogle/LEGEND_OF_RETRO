/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

/**
 *
 * @author Adrien Marchand
 * on va intercepter les erreurs de initialisation
 * 
 */
public class InitException extends Exception
{
    public InitException(String erreur)
    {
        super(erreur);
    }
}
