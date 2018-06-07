/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.util.Vector;

/**
 *
 * @author Home
 */
public class Rapport
{
    public enum Table {FABRICANT, CONSOLE, ZONE,
            VERSIONCONSOLE, VERSIONJEU, JEU, EDITEUR, TAG, DESCRIPTION,
            FACTURE, LIGNEFACTUREJEU, LIGNEFACTURECONSOLE};
    public enum Operation {CREER, MODIFIER};
    
    private int idDerniereOperation;
    private Vector<Integer> ids;
    private Vector<Table> tables;
    private Vector<Operation> operations;
    
    public Rapport()
    {
        idDerniereOperation = -1;
        this.ids = new Vector<Integer>();
        this.tables = new Vector<Table>();
        this.operations = new Vector<Operation>();
    }
    public Rapport(int id, Table table, Operation operation)
    {
        this();
        addOperation(id, table, operation);
    }
    public void concatener(Rapport r)
    {
        this.idDerniereOperation = r.getidDerniereOperation();
        this.ids.addAll(r.getIds());
        this.tables.addAll(r.getTables());
        this.operations.addAll(r.getOperations());
    }
    
    public final void addOperation(int id, Table table, Operation operation)
    {
        this.idDerniereOperation = id;
        
        this.ids.add(id);
        this.tables.add(table);
        this.operations.add(operation);
    }
    
    public int getidDerniereOperation()         { return this.idDerniereOperation;} //permet de connaître l'identifiant du dernier enregistrement créé ou modifié.
    public Vector<Integer> getIds()             {return this.ids;}
    public Vector<Table> getTables()            {return this.tables;}
    public Vector<Operation> getOperations()    {return this.operations;}
    
    public String toString()
    {
        String ret = "";
        
        if (this.tables.size() != this.operations.size())
            return ret;
        
        for (int i = 0 ; i < tables.size() ; i++)
        {
            if (this.operations.elementAt(i) == Operation.CREER)
                ret = ret.concat("Ajout");
            else if (this.operations.elementAt(i) == Operation.MODIFIER)
                ret = ret.concat("Modification");
            System.out.println(this.tables.elementAt(i));
            if (this.tables.elementAt(i) == Table.DESCRIPTION)
                ret = ret.concat(" d'un lien entre un jeu et le tag ");
            else
            {
                if (this.tables.elementAt(i) == Table.CONSOLE)
                    ret = ret.concat(" de la console ");
                else if (this.tables.elementAt(i) == Table.VERSIONCONSOLE)
                    ret = ret.concat(" de la version de console ");
                else if (this.tables.elementAt(i) == Table.VERSIONJEU)
                    ret = ret.concat(" de la version de jeu ");
                else if (this.tables.elementAt(i) == Table.EDITEUR)
                    ret = ret.concat(" de l'éditeur ");
                else if (this.tables.elementAt(i) == Table.FABRICANT)
                    ret = ret.concat(" du fabricant ");
                else if (this.tables.elementAt(i) == Table.JEU)
                    ret = ret.concat(" du jeu ");
                else if (this.tables.elementAt(i) == Table.TAG)
                    ret = ret.concat(" du tag ");
                else if (this.tables.elementAt(i) == Table.ZONE)
                    ret = ret.concat(" de la zone ");
                else if (this.tables.elementAt(i) == Table.FACTURE)
                    ret = ret.concat(" de la facture ");
                    ret = ret.concat(" de la ligne de facture ");
            }
            ret = ret.concat("id "+this.ids.elementAt(i) + ".\n");
        }
        
        return ret;
    }
}
