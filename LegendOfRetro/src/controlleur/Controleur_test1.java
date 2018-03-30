/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

//Imports pour version temporaire ? Voir fonctionalités de Hibernate.
    import entites.VersionJeu;
    import entites.Jeu;
    //import entites.VersionConsole;
    import entites.Console;
    import entites.Editeur;
    import entites.Fabricant;
    import entites.Tag;
    import entites.Zone;
//Fin imports pour version temporaire
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import bean.CodeBarreForm;
import bean.Form;
import bean.ProduitForm;
import vue.GUI;
//import vue.Vue;

/**
 *
 * @author Adrien Marchand
 */
public class Controleur_test1
{
    //private Vue vue; //utilisé pour communiquer avec l'affichage
    private Connection BDD; //utilisé pour créer des requêtes SQL
    
    public Controleur_test1() throws InitException
    {
        init();
    }
    /**
     * Crée la vue et initialise la connection avec la base de données.
     */
    public void init() throws InitException
    {
        //this.vue = new GUI(this);
        
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
    public Rapport creer(Form form)
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
                        f.getIdConsole(), f.getPlateforme(),
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
        Fabricant existant = chercherFabricant(-1, nomFabr);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer le fabricant : ce fabricant existe déjà.");
            
        //TODO : on génère la requête pour créer le fabricant

        //problème : comment obtenir l'identifiant du fabricant pour le rapport ?
        //return new Rapport(idFabricant, Rapport.Table.FABRICANT, Rapport.Operation.CREER);
        return null;
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
        Editeur existant = chercherEditeur(-1, nomEditeur);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer l'éditeur : cet éditeur existe déjà.");
            
        //TODO : on génère la requête pour créer l'éditeur

        //problème : comment obtenir l'identifiant de l'éditeur pour le rapport ?
        //return new Rapport(idEditeur, Rapport.Table.EDITEUR, Rapport.Operation.CREER);
        return null;
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
        Tag existant = chercherTag(-1, tag);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer le tag : ce tag existe déjà."); //vérifier le code appelant.
            
        //TODO : on génère la requête pour créer l'éditeur

        //problème : comment obtenir l'identifiant du tag pour le rapport ?
        //return new Rapport(idTag, Rapport.Table.TAG, Rapport.Operation.CREER);
        return null;
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
            Editeur existant = chercherEditeur(-1, nomEditeur);
            if (existant == null)
            {
                rapport.concatener(creerEditeur(nomEditeur)); //s'il n'existe pas, on le crée à la volée.
                idEditeur = rapport.getidDerniereOperation();
            }
            else
                idEditeur = existant.getId(); //s'il existe, il n'en existe qu'un.

            //on vérifie que le jeu n'existe pas déjà !
            Jeu jeuExistant = chercherJeu(-1, idEditeur, nomJeu, tags, "");
            if (jeuExistant != null)
                throw new EnregistrementExistantException("Impossible de créer le jeu : ce jeu existe déjà.");

            //TODO : on génère la requête pour créer la console.

            //problème : comment obtenir l'identifiant de la console pour le rapport ?
            //rapport.addOperation(idJeu, Rapport.Table.CONSOLE, Rapport.Operation.CREER);

            //traitement des tags
            int idTag;
            for (String tag : tags)
            {
                //on vérifie l'existence du tag et, au besoin, on le crée.
                Tag t = chercherTag(-1, tag);
                if (t == null)
                {
                    rapport.concatener(creerTag(tag));
                    idTag = rapport.getidDerniereOperation();
                }
                else
                    idTag = t.getId();

                //on associe le tag au jeu.
                //rapport.concatener(lierTag(idJeu, idTag));
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
            Fabricant fabricant = chercherFabricant(-1, nomFabr);
            if (fabricant == null)
            {
                rapport.concatener(creerFabricant(nomFabr)); //s'il n'existe pas, on le crée à la volée.
                idFabr = rapport.getidDerniereOperation();
            }
            else
            {
                idFabr = fabricant.getId(); //s'il existe, il n'en existe qu'un.
            
                //on vérifie que la console n'existe pas déjà !
                Console existante = chercherConsole(-1, idFabr, nomConsole, "");
                if (existante != null)
                    throw new EnregistrementExistantException("Impossible de créer la console : cette console existe déjà.");

                //TODO : on génère la requête pour créer la console.

                //problème : comment obtenir l'identifiant de la console pour le rapport ?
                //rapport.addOperation(idConsole, Rapport.Table.CONSOLE, Rapport.Operation.CREER);
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
            Console existante = chercherConsole(idConsole, idFabr, nomConsole, nomFabr);
            if (existante == null)
            {
                rapport.concatener(creerConsole(nomConsole, idFabr, nomFabr)); //si elle n'existe pas, on la crée à la volée.
                idConsole = rapport.getidDerniereOperation();
            }
            else
                idConsole = existante.getId();
            
            //on détermine l'identifiant de la zone
            Zone zoneExistante = chercherZone(-1, zone);
            if (zoneExistante == null)
                throw new DonneeInvalideException("Impossible de créer la version de console : la zone renseignée n'existe pas.");
            
            //on vérifie que la version de console n'existe pas déjà !
            //Vector<VersionConsole> existe = chercherVersionConsole(cb, edition, zoneExistante.getId(), idConsole);
            //if (!existante.isEmpty())
                //throw new EnregistrementExistantException("Impossible de créer la version de console : cette dernière existe déjà.");
            
            //TODO : on génère la requête pour créer la version de console.
            
            //problème : comment obtenir l'identifiant de la version de console pour le rapport ?
            //rapport.addOperation(idVersionConsole, Rapport.Table.VERSIONCONSOLE, Rapport.Operation.CREER);
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
            int idCons, String nomConsole,
            int idEditeur, String nomEditeur)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
        Rapport rapport = new Rapport();
        
        if ("".equals(edition) && "".equals(zone) && "".equals(nomConsole)) //si on ne crée pas une version de jeu.
        {
            try {
                rapport.concatener(creerJeu(nomJeu, description, tags, idEditeur, nomEditeur));}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "Impossible de créer la version de jeu : une information de plateforme, de zone ou d'édition est requise.");}
        }
        else //si on crée une version de jeu
        {
            cb = codeBarreValide(cb); //on vérifie le code barre
            
            //on détermine l'identifiant du jeu
            Jeu existant = chercherJeu(-1, idEditeur, nomJeu, tags, nomEditeur);
            if (existant == null)
            {
                rapport.concatener(creerJeu(nomJeu, description, tags, idEditeur, nomEditeur)); //s'il n'existe pas, on le crée à la volée.
                idJeu = rapport.getidDerniereOperation();
            }
            else
                idJeu = existant.getId(); //s'il existe, il n'en existe qu'un.
            
            //on détermine l'identifiant de la zone
            Zone zoneExistante = chercherZone(-1, zone);
            if (zoneExistante == null)
                throw new DonneeInvalideException("Impossible de créer la version de console : la zone renseignée n'existe pas.");
            
            //on détermine l'identifiant de la console
            Console console = chercherConsole(idCons, -1, nomConsole, "");
            if (console == null)
                throw new DonneeInvalideException("Impossible de créer la version de jeu : la console renseignée n'existe pas.");
            
            //on vérifie que la version de jeu n'existe pas déjà !
            //Vector<VersionJeu> existante = chercherVersionJeu(cb, edition, zoneExistante.getId(), console.getId(), idJeu);
            //if (!existante.isEmpty())
                //throw new EnregistrementExistantException("Impossible de créer la version de jeu : cette dernière existe déjà.");
            
            //TODO : on génère la requête pour créer la version de jeu.
            
            //problème : comment obtenir l'identifiant de la console pour le rapport ?
            //rapport.addOperation(idVersionJeu, Rapport.Table.VERSIONJEU, Rapport.Operation.CREER);
        }
        
        return rapport;
    }
    
    /**
     * Détermine, à partir d'un bean, quelle(s) requête(s) de recherche générer et exécuter. Transforme les résultats en formulaires.
     * Les formulaires renvoyés correspondront à des produits : nommément, soit à une version de jeu, soit à une version de console.
    */
    public Vector<ProduitForm> chercher(Form form) throws DonneeInvalideException, ResultatInvalideException, DonneesInsuffisantesException
    {
        Vector<ProduitForm> ret = new Vector<ProduitForm>();
        
        if (form instanceof CodeBarreForm) //recherche rapide par code barre
        {
            String cb = codeBarreValide(((CodeBarreForm) form).getCodeBarre());
            /*for (VersionConsole enr : chercherVersionsConsole(cb, "", "", "", ""))
                ret.add(new ProduitForm(
                        enr.getConsole().getId(), enr.getIdVersionConsole(), -1, -1,
                        enr.getFabricant().getId(), "Console",
                        enr.getCodeBarre(), enr.getConsole().getNom(), enr.getEdition(), enr.getZone().getNom(),
                        enr.getConsole().getFabricant().getNom(), "", new Vector<String>(), "",
                        enr.getPrix(), enr.getStock()));*/
            for (VersionJeu enr : chercherVersionJeu(cb, "", "", "", "", "", null))
                ret.add(new ProduitForm(
                        enr.getPlateforme().getId(), enr.getPlateforme().getId(),
                        enr.getJeu().getId(), enr.getId(),
                        enr.getJeu().getEditeur().getId(), "Jeu",
                        enr.getCodeBarre(), enr.getJeu().getNom(), enr.getEdition(), enr.getZone().getNom(),
                        enr.getJeu().getEditeur().getNom(), enr.getJeu().getDescription(),
                        tagsToStrings(enr.getJeu().getTags()), enr.getPlateforme().getNom(),
                        enr.getPrix(), enr.getStock()));
            if (ret.size() > 1)
                throw new ResultatInvalideException("Erreur : la recherche du code barre " + cb
                        + " renvoie plus d'un résultat", ret);
        }
        else if (form instanceof ProduitForm)
        {
            ProduitForm f = (ProduitForm) form;
            //On récupère les variables du bean pour améliorer la lisibilité
            String type = f.getType();
            String cb = f.getCodeBarre();
            if (!"".equals(cb))
                cb = codeBarreValide(cb);
            String nom = f.getNom();
            String edition = f.getEdition();
            String zone = f.getZone();
            String editeur = f.getEditeur();
            //String description = f.getDescription();      La description n'est pas un critère de recherche viable.
            Vector<String> tags = f.getTags();
            String plateforme = f.getPlateforme();
            //Pas besoin de récupérer les identifiants, la description de jeu, le prix et le stock.
            
            if ("Console".equals(type))
            {
                /*if (!"".equals(cb) || !"".equals(edition) || !"".equals(zone) || !"".equals(nom) || !"".equals(editeur))
                for (VersionConsole enr : chercherVersionsConsole(cb, edition, zone, nom, editeur))
                    ret.add(new ProduitForm(
                            enr.getConsole().getId(), enr.getId(), -1, -1,
                            enr.getFabricant().getId(), "Console",
                            enr.getCodeBarre(), enr.getConsole().getNom(), enr.getEdition(), enr.getZone().getNom(),
                            enr.getConsole().getFabricant().getNom(), "", new Vector<String>(), "",
                            enr.getPrix(), enr.getStock()));
                else
                    throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");*/
            }
            else if ("Jeu".equals(type))
            {
                if (!"".equals(cb) || !"".equals(nom) || !"".equals(editeur) || !tags.isEmpty())
                    for (VersionJeu enr : chercherVersionJeu(cb, edition, zone, plateforme, nom, editeur, tags))
                        ret.add(new ProduitForm(
                                enr.getPlateforme().getId(), enr.getPlateforme().getId(),
                                enr.getJeu().getId(), enr.getId(),
                                enr.getJeu().getEditeur().getId(), "Jeu",
                                enr.getCodeBarre(), enr.getJeu().getNom(), enr.getEdition(), enr.getZone().getNom(),
                                enr.getJeu().getEditeur().getNom(), enr.getJeu().getDescription(),
                                tagsToStrings(enr.getJeu().getTags()), enr.getPlateforme().getNom(),
                                enr.getPrix(), enr.getStock()));
                else
                    throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");
            }
        }
        
        return ret;
    }
    /**
     * Recherche les versions de consoles dont le code barre, l'édition, la zone et le fabricant correspondent parfaitement aux données renseignées,
     * et dont le nom contient la chaîne renseignée.
     * La zone et l'édition ne sont pas suffisantes pour lancer une recherche.
     */
    /*private Vector<VersionConsole> chercherVersionsConsole(String cb, String edition, String zone, String nom, String fabricant)
            throws DonneesInsuffisantesException
    {
        if ("".equals(cb) && "".equals(nom) && "".equals(fabricant))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche des produits de type console : il faut renseigner un code barre, un nom, ou un fabricant.");
        
        //TODO: la recherche à proprement parler.
        
        return ret;
    }
    /**
     * Recherche les versions de consoles dont le code barre, l'édition, la zone et le fabricant correspondent parfaitement aux données renseignées,
     * et dont le nom contient la chaîne renseignée.
     * La zone et l'édition ne sont pas suffisantes pour lancer une recherche.
     */
    private Vector<VersionJeu> chercherVersionJeu(String cb, String edition, String zone,
            String plateforme, String nom, String editeur, Vector<String> tags)
            throws DonneesInsuffisantesException
    {
        if ("".equals(cb) && "".equals(plateforme) && "".equals(nom) && "".equals(editeur) && tags.isEmpty())
            throw new DonneesInsuffisantesException("Erreur lors de la recherche des produits de type jeu : il faut renseigner un code barre, une plateforme, un nom, un éditeur ou au moins un tag.");
        
        //TODO: la recherche à proprement parler.
        
        //return ret;
        return null;
    }
    /**
     * Recherche les consoles dont le nom correspond parfaitement à la chaîne renseignée et ayant le fabricant désigné.
     */
    private Console chercherConsole(int idConsole, int idFabr, String nomCons, String nomFabr) throws DonneesInsuffisantesException
    {
        if (idConsole < 0 && (nomCons == null || "".equals(nomCons)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la console : nom de la console non renseigné.");
        
        if (nomFabr != null && !"".equals(nomFabr))
            idFabr = chercherFabricant(-1, nomFabr).getId();
        
        /* Attention
        Il est possible qu'une recherche renvoie plusieurs résultats. Par exemple, si deux consoles produites par des fabricants
        différents ont le même nom et que le fabricant n'a pas été renseigné. Pour traiter ce type d'erreur,
        on commence par stocker les résultats de la requête dans un vecteur ; puis, si le vecteur n'a pas une taille de 1,
        on renvoie null ou on lance une exception.
        */
        Vector<Console> resultat;
        
        //TODO: la recherche à proprement parler. On ne réutilise pas chercherVersionsConsole car la correspondance demandée par chercherConsole est parfaite.
        //Attention, pour le fabricant, si le nom est renseigné, on demande une correspondance parfaite.
        
        /*if (resultat.isEmpty())
            return null;
        else if (resultat.size() > 1)
            throw new  DonneesInsuffisantesException("Erreur lors de la recherche de la console : plusieurs résultats obtenus. Veuillez renseigner le fabricant de la console.");
        
        return resultat.firstElement();*/
        return null;
    }
    /**
     * Recherche les consoles dont le nom contient la chaîne renseignée et/ou ayant le fabricant désigné.
     */
    private Vector<Console> chercherConsoles(int idFabr, String nomCons, String nomFabr) throws DonneesInsuffisantesException
    {
        if ((nomCons == null || "".equals(nomCons)) && idFabr < 0 && (nomFabr == null || "".equals(nomFabr)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de consoles : aucun champ renseigné.");
        
        //TODO: la recherche à proprement parler. Attention, pour le fabricant, si le nom est renseigné, on demande une correspondance parfaite.
        //Attention, pour le fabricant, si le nom est renseigné, on demande une correspondance parfaite.
        
        //return ret;
        return null;
    }
    /**
     * Recherche le jeu dont le nom correspond parfaitement à la chaîne renseignée et ayant l'éditeur et les tags renseignés.
     */
    private Jeu chercherJeu(int idJeu, int idEditeur, String nomJeu,
            Vector<String> tags, String nomEditeur) throws DonneesInsuffisantesException
    {
        if (idJeu < 0 && (nomJeu == null || "".equals(nomJeu)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du jeu : nom du jeu non renseigné.");
        
        if (nomEditeur != null && !"".equals(nomEditeur))
            idEditeur = chercherEditeur(-1, nomEditeur).getId();
        
        /* Attention
        Il est possible qu'une recherche renvoie plusieurs résultats. Par exemple, si deux jeux produits par des éditeurs
        différents ont le même nom et que l'éditeur n'a pas été renseigné. Pour traiter ce type d'erreur,
        on commence par stocker les résultats de la requête dans un vecteur ; puis, si le vecteur n'a pas une taille de 1,
        on renvoie null ou on lance une exception.
        */
        Vector<Jeu> resultat;
        
        //TODO: la recherche à proprement parler. On ne réutilise pas chercherVersionsJeu car la correspondance demandée par chercherJeu est parfaite.
        //Attention, pour l'éditeur, si le nom est renseigné, on demande une correspondance parfaite.
        //Penser à traiter les tags avec une requête imbriquée
        
        /*if (resultat.isEmpty())
            return null;
        else if (resultat.size() > 1)
            throw new  DonneesInsuffisantesException("Erreur lors de la recherche du jeu : plusieurs résultats obtenus. Veuillez renseigner l'éditeur du jeu.");
        
        return resultat.firstElement();*/
        return null;
    }
    /**
     * Recherche les jeux dont le nom contient la chaîne renseignée et/ou ayant l'éditeur désigné et/ou les tags renseignés.
     */
    private Vector<Console> chercherJeux(int idEditeur, String nomCons, String nomEditeur) throws DonneesInsuffisantesException
    {
        if ((nomCons == null || "".equals(nomCons)) && idEditeur < 0 && (nomEditeur == null || "".equals(nomEditeur)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de consoles : aucun champ renseigné.");
        
        //TODO: la recherche à proprement parler.
        //Attention, pour l'éditeur, si le nom est renseigné, on demande une correspondance parfaite.
        //Penser à traiter les tags avec une requête imbriquée
        
        //return ret;
        return null;
    }
    /**
     * Recherche les fabricants dont le nom contient la chaîne renseignée.
     */
    private Fabricant chercherFabricant(int id, String nomFabr) throws DonneesInsuffisantesException
    {
        if (id < 0 && (nomFabr == null || "".equals(nomFabr)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du fabricant : nom du fabricant non renseigné.");
        
        //TODO: la recherche à proprement parler.
        
        //return ret;
        return null;
    }
    /**
     * Recherche les fabricants dont le nom contient la chaîne renseignée.
     */
    private Vector<Fabricant> chercherFabricants(String nomFabr) throws DonneesInsuffisantesException
    {
        if (nomFabr == null || "".equals(nomFabr))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de fabricants : nom du fabricant non renseigné.");
        
        Vector<Fabricant> ret = new Vector<Fabricant>();
        
        //TODO: la recherche à proprement parler.
        
        return ret;
    }
    /**
     * Recherche l'éditeur dont le nom correspond parfaitement à la chaîne renseignée.
     */
    private Editeur chercherEditeur(int id, String nomEdit) throws DonneesInsuffisantesException
    {
        if (id < 0 && (nomEdit == null || "".equals(nomEdit)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de l'éditeur : nom de l'éditeur non renseigné.");
        
        //TODO: la recherche à proprement parler.
        
        //return ret;
        return null;
    }
    /**
     * Recherche les éditeurs dont le nom contient la chaîne renseignée.
     */
    private Vector<Editeur> chercherEditeurs(String nomEdit) throws DonneesInsuffisantesException
    {
        if (nomEdit == null || "".equals(nomEdit))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche des éditeurs : nom de l'éditeur non renseigné.");
        
        Vector<Editeur> ret = new Vector<Editeur>();
        
        //TODO: la recherche à proprement parler.
        
        return ret;
    }
    /**
     * Recherche une zone dans la base de données. Si la zone renseignée est trouvé, un objet Zone est renvoyé. Sinon, la méthode renvoie null.
     */
    private Zone chercherZone(int id, String zone) throws DonneesInsuffisantesException
    {
        if (id < 0 && (zone == null || "".equals(zone)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la zone : nom de la zone non renseigné.");
        
        //TODO: la recherche à proprement parler.
        
        //return ret;
        return null;
    }
    /**
     * Recherche un tag dans la base de données. Si le tag renseigné est trouvé, un objet Tag est renvoyé. Sinon, la méthode renvoie null.
     */
    private Tag chercherTag(int id, String tag) throws DonneesInsuffisantesException
    {
        if (id < 0 && (tag == null || "".equals(tag)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du tag : nom du tag non renseigné.");
        
        //TODO: la recherche à proprement parler.
        
        //return ret;
        return null;
    }
    
    /**
     * Crée une zone dans la table des zones.
    */
    public Rapport creerZone(String zone)
            throws EnregistrementExistantException, DonneesInsuffisantesException
    {
        Rapport rapport = new Rapport();
        
        if (zone == null || "".equals(zone))
            throw new DonneesInsuffisantesException("Erreur lors de la création de la zone : nom de la zone non renseigné.");
            
        //on vérifie que la zone n'existe pas déjà !
        Zone existante = chercherZone(-1, zone);
        if (existante != null)
            throw new EnregistrementExistantException("Impossible de créer la zone : cette zone existe déjà.");
        
        //TODO : on génère la zone pour créer la version de jeu.
        
        return rapport;
    }
    /**
     * Renvoie la liste des zones.
     */
    public Vector<String> listeZones()
    {
        Vector<String> ret = new Vector();
        
        Vector<Zone> zones = null;
        //TODO : un SELECT * FROM ZONE avec Hibernate
        
        for (Zone z : zones)
            ret.add(z.getNom());
        
        return ret;
    }
    /**
     * Renvoie la liste des consoles.
     */
    public Vector<String> listeConsoles()
    {
        Vector<String> ret = new Vector();
        
        Vector<Console> consoles = null;
        //TODO : un SELECT * FROM CONSOLE avec Hibernate
        
        for (Console c : consoles)
            ret.add(c.getNom());
        
        return ret;
    }
    
    /**
     * Convertit une chaîne de 1 à 13 chiffres en code barre valides. Si le code barre fait moins de 13 chiffres, il est complété par des 0 à gauche.
     * @param cb le code barre à vérifier.
     * @return un code barre valide.
     * @throws DonneeInvalideException le code barre renseigné n'est pas composé de 1 à 13 chiffres.
     */
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
     * Transforme un vecteur de tags en un vecteur de strings pour l'affichage.
     */
    protected final Vector<String> tagsToStrings(Vector<Tag> tags)
    {
        Vector <String> ret = new Vector<String>();
        for (Tag t : tags)
            ret.add(t.getNom());
        return ret;
    }
    
    /**
     * Démarre l'application
     */
    public static void main(String[] args)
    {
        try {
            new Controleur_test1();}
        catch (InitException ex) {
            System.out.println(ex.getMessage());}
    }
}
