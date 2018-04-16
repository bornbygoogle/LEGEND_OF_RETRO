/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import bean.Form;

/**
 *Décrit un menu qui contient une zone de recherche.
 * La zone de recherche est alors susceptible de lui demander de sélectionner un résultat de recherche en particulier.
 * @author Home
 */
interface Chercheur
{
    public void selectionnerResultat(Form res);
    public void lancerRecherche(Form form);
    public void afficherErreur(Exception e);
}
