/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

/**
 *
 * @author Adrien Marchand
 */
public abstract class Modele
{
    public static String DRIVER = "";
    public static String SERVER = "";
    public static String BDD_NOM = "";
    public static int PORT = 0;
    public static String ID = "";
    public static String MDP = "";
    
    public static String getDerbyURL()
    {
        return "jdbc:derby:" + SERVER + ":" + PORT + "/" + BDD_NOM;
    }
}
