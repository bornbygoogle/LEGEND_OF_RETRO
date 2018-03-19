/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.controleur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.bean.CodeBarreForm;
import projet.bean.Form;
import projet.bean.ProduitForm;
import projet.vue.GUI;
import projet.vue.Vue;

/**
 *
 * @author Adrien Marchand
 */
public class Controleur
{
    private Vue vue; //utilisé pour communiquer avec l'affichage
    private Connection BDD; //utilisé pour créer des requêtes SQL
    
    public Controleur() throws InitException
    {
        init();
    }
    /**
     * Crée la vue et initialise la connection avec la base de données.
     */
    public void init() throws InitException
    {
        this.vue = new GUI(this);
        
        try {
            Class.forName(Modele.DRIVER);
        this.BDD = DriverManager.getConnection(
                Modele.getDerbyURL(),
                Modele.ID,
                Modele.MDP);
        }
        catch (ClassNotFoundException ex) {
            throw new InitException("Erreur à l'initialisation du driver de la base de données.");}
        catch (SQLException ex) {
            throw new InitException("Erreur lors de la connection avec la base de données.");}
    }
    
    /**
     * Détermine, à partir d'un bean, quelle(s) requête(s) d'INSERT générer et exécuter.
    */
    protected Rapport creer(Form form)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
        Rapport ret = null;
        
        if (form instanceof ProduitForm)
        {
            ProduitForm f = (ProduitForm) form;
            String type = f.getType();
            
            if ("Console".equals(type))
                ret = creerVersionConsole(f.getCodeBarre(), f.getEdition(), f.getZone(),
                        f.getPrix(), f.getStock(),
                        f.getIdConsole(), f.getNom(),       //getNom() renvoie le nom de la CONSOLE, pas de la Version de la console
                        f.getIdEditeur(), f.getEditeur());
            else if ("Jeu".equals(type))
                ret = creerVersionJeu(f.getCodeBarre(),
                        f.getEdition(), f.getZone(),
                        f.getPrix(), f.getStock(),
                        f.getIdJeu(), f.getNom(),       //getNom() renvoie le nom du JEU, pas de la Version du jeu
                        f.getDescription(), f.getTags(),
                        f.getIdEditeur(), f.getEditeur());
        }
        else if (form instanceof CodeBarreForm)
            throw new DonneesInsuffisantesException("Impossible de créer le produit : le code barre n'est pas une donnée suffisante.");
        
        return ret;
    }
    /**
     * Crée un fabricant. Assure l'unicité de l'enregistrement.
    */
    protected Rapport creerFabricant(String nomFabr)
            throws DonneesInsuffisantesException, EnregistrementExistantException
    {
        if ("".equals(nomFabr)) //si le nom du fabricant n'est pas saisi
            throw new DonneesInsuffisantesException("Impossible de créer le fabricant : un nom est requis.");
        
        //on vérifie que le fabricant n'existe pas déjà !
        Vector<Fabricant> existant = chercherFabricant(nomFabr);
        if (!existant.isEmpty())
            throw new EnregistrementExistantException("Impossible de créer le fabricant : ce fabricant existe déjà.");
            
        //TODO : on génère la requête pour créer le fabricant

        //problème : comment obtenir l'identifiant du fabricant pour le rapport ?
        return new Rapport(idFabricant, Rapport.Table.FABRICANT, Rapport.Operation.CREER);
    }
    /**
     * Crée un éditeur. Assure l'unicité de l'enregistrement.
    */
    protected Rapport creerEditeur(String nomEditeur)
            throws DonneesInsuffisantesException, EnregistrementExistantException
    {
        if ("".equals(nomEditeur)) //si le nom de l'éditeur n'est pas saisi
            throw new DonneesInsuffisantesException("Impossible de créer l'éditeur : un nom est requis.");
        
        //on vérifie que l'éditeur n'existe pas déjà !
        Vector<Editeur> existant = chercherEditeur(nomEditeur);
        if (!existant.isEmpty())
            throw new EnregistrementExistantException("Impossible de créer l'éditeur : cet éditeur existe déjà.");
            
        //TODO : on génère la requête pour créer l'éditeur

        //problème : comment obtenir l'identifiant de l'éditeur pour le rapport ?
        return new Rapport(idEditeur, Rapport.Table.EDITEUR, Rapport.Operation.CREER);
    }
    /**
     * Crée un tag. Assure l'unicité de l'enregistrement.
    */
    protected Rapport creerTag(String tag)
            throws DonneesInsuffisantesException, EnregistrementExistantException
            //Remarque : si une erreur est renvoyée, la faute en revient à un développeur !
    {
        if ("".equals(tag)) //si le nom de l'éditeur n'est pas saisi
            throw new DonneesInsuffisantesException("Impossible de créer le tag : un libellé est requis."); //vérifier le code appelant.
        
        //on vérifie que le tag n'existe pas déjà !
        Vector<Tag> existant = chercherTag(tag);
        if (!existant.isEmpty())
            throw new EnregistrementExistantException("Impossible de créer le tag : ce tag existe déjà."); //vérifier le code appelant.
            
        //TODO : on génère la requête pour créer l'éditeur

        //problème : comment obtenir l'identifiant du tag pour le rapport ?
        return new Rapport(idTag, Rapport.Table.TAG, Rapport.Operation.CREER);
    }
    /**
     * Lie un tag à un jeu.
     * L'identifiant du tag doit être valide. L'identifiant du jeu doit être valide. La relation ne doit pas déjà exister. Aucun contrôle d'erreur n'est réalisé.
     */
    protected Rapport lierTag(int idJeu, int idTag)
    {
        //TODO : on génère la requête pour créer l'enregistrement.
        return new Rapport(idTag, Rapport.Table.DESCRIPTION, Rapport.Operation.CREER);
    }
    /**
     * Crée un jeu et/ou son éditeur. Assure l'unicité de l'enregistrement.
     * Si l'éditeur est inexistant dans la base de données, un nouvel éditeur est ajouté à la volée. De même pour les tags.
     */
    protected Rapport creerJeu(String nomJeu, String description, Vector<String> tags,
            int idEditeur, String nomEditeur) throws DonneesInsuffisantesException, EnregistrementExistantException
    {
        Rapport rapport = new Rapport();
        
        if ("".equals(nomJeu)) //si on ne crée pas un jeu.
        {
            try {
                rapport.concatener(creerEditeur(nomEditeur));}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "Impossible de créer le jeu : un nom est requis.");}
            
        }
        else //si on crée un jeu
        {
            //on détermine l'identifiant de l'éditeur
            Vector<Editeur> existant = chercherEditeur(nomEditeur);
            if (existant.isEmpty())
            {
                rapport.concatener(creerEditeur(nomEditeur)); //s'il n'existe pas, on le crée à la volée.
                idEditeur = rapport.getidDerniereOperation();
            }
            else
            {
                idEditeur = existant.iterator().next().getId(); //s'il existe, il n'en existe qu'un.
            
                //on vérifie que le jeu n'existe pas déjà !
                Vector<Jeu> jeuExistant = chercherJeu(nom);
                if (!jeuExistant.isEmpty())
                    throw new EnregistrementExistantException("Impossible de créer le jeu : ce jeu existe déjà.");

                //TODO : on génère la requête pour créer la console.

                //problème : comment obtenir l'identifiant de la console pour le rapport ?
                rapport.addOperation(idJeu, Rapport.Table.CONSOLE, Rapport.Operation.CREER);

                //traitement des tags
                int idTag;
                for (String tag : tags)
                {
                    //on vérifie l'existence du tag et, au besoin, on le crée.
                    Vector<Tag> tagExistant = chercherTag(tag);
                    if (tagExistant.isEmpty())
                        idTag = tagExistant.next().getId(); //si le tag existe, il n'y en a qu'un
                    else
                    {
                        rapport.concatener(creerTag(tag));
                        idTag = rapport.getidDerniereOperation();
                    }

                    //on associe le tag au jeu.
                    rapport.concatener(lierTag(idJeu, idTag));
                }
            }
        }
        
        return rapport;
    }
    /**
     * Crée une console et/ou son fabricant.
     * Si le fabricant est renseigné par un label inexistant dans la base de données, un nouveau fabricant est ajouté à la volée.
    */
    protected Rapport creerConsole(String nomConsole,
            int idFabr, String nomFabr) throws DonneesInsuffisantesException, EnregistrementExistantException
    {
        Rapport rapport = new Rapport();
        
        if ("".equals(nomConsole)) //si on ne crée pas une console.
        {
            try {
                rapport.concatener(creerFabricant(nomFabr));}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "Impossible de créer la console : un nom est requis.");}
            
        }
        else //si on crée une console
        {
            //on détermine l'identifiant du fabricant
            Vector<Fabricant> existant = chercherFabricant(nomFabr);
            if (existant.isEmpty())
            {
                rapport.concatener(creerFabricant(nomFabr)); //s'il n'existe pas, on le crée à la volée.
                idFabr = rapport.getidDerniereOperation();
            }
            else
            {
                idFabr = existant.iterator().next().getId(); //s'il existe, il n'en existe qu'un.
            
                //on vérifie que la console n'existe pas déjà !
                Vector<Console> existante = chercherConsole(nomConsole, idFabr);
                if (!existante.isEmpty())
                    throw new EnregistrementExistantException("Impossible de créer la console : cette console existe déjà.");

                //TODO : on génère la requête pour créer la console.

                //problème : comment obtenir l'identifiant de la console pour le rapport ?
                rapport.addOperation(idConsole, Rapport.Table.CONSOLE, Rapport.Operation.CREER);
            }
        }
        
        return rapport;
    }
    /**
     * Crée une version d'une console et/ou la console elle-même et/ou son fabricant.
     * Si un produit est créé, assure la validité du code barre ainsi que l'unicité de l'enregistrement.
     * Si la console est inexistante dans la base de données, une nouvelle console est ajoutée à la volée. Par transitivité, cela s'applique au fabricant. Ne met pas à jour le fabricant d'une console existante.
     * La zone renseignée doit déjà exister dans la base de données.
    */
    protected Rapport creerVersionConsole(String cb, String edition, String zone,
            float prix, int stock,
            int idConsole, String nomConsole,
            int idFabr, String nomFabr)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
        Rapport rapport = new Rapport();
        
        if ("".equals(edition) && "".equals(zone)) //si on ne crée pas une version de console.
        {
            try {
                rapport.concatener(creerConsole(nomConsole,idFabr, nomFabr));}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "Impossible de créer la version de console : une information de zone ou d'édition est requise.");}
        }
        else //si on crée une version de console
        {
            cb = codeBarreValide(cb); //on vérifie le code barre
            
            //on détermine l'identifiant de la console
            Vector<Console> existante = chercherConsole(nomConsole, idFabr, nomFabr);
            if (existante.isEmpty())
            {
                rapport.concatener(creerConsole(nomConsole, idFabr, nomFabr)); //s'il n'existe pas, on le crée à la volée.
                idConsole = rapport.getidDerniereOperation();
            }
            else
                idConsole = existante.iterator().next().getId(); //s'il existe, il n'en existe qu'un.
            
            //on détermine l'identifiant de la zone
            int idZone;
            Vector<Zone> zones = chercherZone(zone);
            if (zones.isEmpty())
                throw new DonneeInvalideException("Impossible de créer la version de console : la zone renseignée n'existe pas.");
            else
                idZone = zones.iterator().next();
            
            //on vérifie que la console n'existe pas déjà !
            Vector<VersionConsole> existe = chercherVersionConsole(cb, edition, idZone, idConsole);
            if (!existante.isEmpty())
                throw new EnregistrementExistantException("Impossible de créer la version de console : cette dernière existe déjà.");
            
            //TODO : on génère la requête pour créer la version de console.
            
            //problème : comment obtenir l'identifiant de la version de console pour le rapport ?
            rapport.addOperation(idVersionConsole, Rapport.Table.VERSIONCONSOLE, Rapport.Operation.CREER);
        }
        
        return rapport;
    }
    /**
     * Crée une version d'un jeu et/ou le jeu lui-même et/ou son éditeur et/ou ses tags.
     * Si un produit est créé, assure la validité du code barre ainsi que l'unicité de l'enregistrement.
     * Si le jeu est inexistant dans la base de données, un nouveau jeu est ajouté à la volée. Par transitivité, cela s'applique à l'éditeur et aux tags de ce nouveau jeu. Ne met pas à jour les tags et l'éditeur d'un jeu existant.
     * La zone renseignée doit déjà exister dans la base de données.
     * La console renseignée doit déjà exister dans la base de données.
    */
    protected Rapport creerVersionJeu(String cb, String edition, String zone,
            float prix, int stock,
            int idJeu, String nomJeu, String description, Vector<String> tags,
            int idEditeur, String nomEditeur)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
        Rapport rapport = new Rapport();
        
        if ("".equals(edition) && "".equals(zone)) //si on ne crée pas une version de jeu.
        {
            try {
                rapport.concatener(creerJeu(nomJeu, description, tags, idEditeur, nomEditeur));}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "Impossible de créer la version de jeu : une information de zone ou d'édition est requise.");}
        }
        else //si on crée une version de jeu
        {
            cb = codeBarreValide(cb); //on vérifie le code barre
            
            //on détermine l'identifiant du jeu
            Vector<Jeu> existant = chercherJeu(nomJeu, description, tags, idEditeur, nomEditeur);
            if (existant.isEmpty())
            {
                rapport.concatener(creerJeu(nomJeu, description, tags, idEditeur, nomEditeur)); //s'il n'existe pas, on le crée à la volée.
                idJeu = rapport.getidDerniereOperation();
            }
            else
                idJeu = existant.iterator().next().getId(); //s'il existe, il n'en existe qu'un.
            
            //on détermine l'identifiant de la zone
            int idZone;
            Vector<Zone> zones = chercherZone(zone);
            if (zones.isEmpty())
                throw new DonneeInvalideException("Impossible de créer la version de jeu : la zone renseignée n'existe pas.");
            else
                idZone = zones.iterator().next();
            
            //on détermine l'identifiant de la console
            int idConsole;
            Vector<Console> consoles = chercherConsole(nom);
            if (consoles.isEmpty())
                throw new DonneeInvalideException("Impossible de créer la version de jeu : la console renseignée n'existe pas.");
            else
                idConsole = consoles.iterator().next();
            
            //on vérifie que la version de jeu n'existe pas déjà !
            Vector<VersionJeu> existante = chercherVersionJeu(cb, edition, idZone, idJeu);
            if (!existante.isEmpty())
                throw new EnregistrementExistantException("Impossible de créer la version de jeu : cette dernière existe déjà.");
            
            //TODO : on génère la requête pour créer la version de jeu.
            
            //problème : comment obtenir l'identifiant de la console pour le rapport ?
            rapport.addOperation(idVersionJeu, Rapport.Table.VERSIONJEU, Rapport.Operation.CREER);
        }
        
        return rapport;
    }
    
    /**
     * Détermine, à partir d'un bean, quelle(s) requête(s) de recherche générer et exécuter. Transforme les résultats en formulaires.
    */
    protected Vector<ProduitForm> chercher(Form form) throws DonneeInvalideException, ResultatInvalideException
    {
        Vector<ProduitForm> ret = new Vector<ProduitForm>();
        
        if (form instanceof CodeBarreForm)
        {
            String cb = codeBarreValide(((CodeBarreForm) form).getCodeBarre());
            for (Console c : chercherConsole(cb))
                ret.add(new ProduitForm(
                        c.getId(), -1, -1, c.getFabricant().getId(),
                        "Console", cb, c.getNom(), c.getEdition(), c.getZone(),
                        c.getFabricant().getNom(), "", new Vector<String>(),
                        c.getPrix(), c.getStock()));
            for (VersionJeu vj : chercherVersionJeu(cb))
                ret.add(new ProduitForm(
                        vj.getConsole.getId(), vj.getJeu().getId()),
                        vj.getId(), vj.getJeu().getEditeur().getId(),
                        "Jeu", cb, vj.getEdition(), vj.getZone(),
                        vj.getJeu().getNom(), vj.getJeu().getEditeur().getNom(),
                        vj.getJeu.getDescription(), new Vector<String>(),
                        c.getPrix(), c.getStock()));
            if (ret.size() > 1)
                throw new ResultatInvalideException("Erreur : la recherche du code barre " + cb
                        + " renvoie plus d'un résultat", ret);
        }
        else if (form instanceof ProduitForm)
        {
            ProduitForm f = (ProduitForm) form;
            String type = f.getType();
            
            if ("Console".equals(type))
            {
                if (f.getZone() != "" || f.getEdition() != "")
                    ret = chercherVersionConsole(XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX);
                else if (f.getNom() != "" || f.getCodeBarre() != "")
                    ret = chercherConsole(XXXXXXXXXXXXXXXXXXXXXXXXX);
            }
            else if ("Jeu".equals(type))
            {
                
            }
        }
        
        return ret;
    }

    protected final String codeBarreValide(String cb)
            throws DonneeInvalideException
    {
        String ret = "";
        if (!cb.matches("\\d{1,13}"))
            throw new DonneeInvalideException("Veuillez entrer un code barre composé de 13 chiffres.");

        int missingCharacters = 13 - cb.length();
        for (int i = 0 ; i < missingCharacters ; i++)
            ret = ret.concat("0");
        
        return ret.concat(cb);
    }
    
    /**
     * Démarre l'application
     */
    public static void main(String[] args)
    {
        try {
            new Controleur();}
        catch (InitException ex) {
            System.out.println(ex.getMessage());}
    }
}
