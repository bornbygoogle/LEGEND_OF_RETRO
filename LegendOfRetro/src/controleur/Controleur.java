/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

//Imports pour version temporaire ? Voir fonctionalités de Hibernate.
    import LOREntities.VersionJeu;
    import LOREntities.VersionConsole;
    import LOREntities.Jeu;
    import LOREntities.Console;
    import LOREntities.Editeur;
    import LOREntities.Fabricant;
    import LOREntities.Tag;
    import LOREntities.Zone;
//Fin imports pour version temporaire
import bean.CodeBarreForm;
import bean.Form;
import bean.ProduitForm;
import hibernateConfig.HibernateUtil;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import org.hibernate.*;
import vue.GUI;
//import vue.Vue;

/**
 *
 * @author Adrien Marchand
 */
public class Controleur
{
    private GUI vue; //utilisé pour communiquer avec l'affichage
    private Session modele; //session hibernate
    
    public Controleur() throws InitException
    {
        init();
    }
    /**
     * Crée la vue et initialise la connection avec la base de données.
     */
    public void init() throws InitException
    {
        try {
            this.modele = (HibernateUtil.getSessionFactory()).openSession();
            this.vue = new GUI(this);
        }
        catch (ExceptionInInitializerError eiie)    {System.out.println("Erreur lors de l'initialisation du modèle.\n"
                + eiie.getMessage());}
        
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
                ret = creerVersionConsole(
                        f.getCodeBarre(), 
                        f.getEdition(), 
                        f.getZone(),
                        f.getPrix(), 
                        f.getStock(),
                        f.getIdConsole(), 
                        f.getNom(),       //getNom() renvoie le nom de la CONSOLE, pas de la Version de la console
                        f.getIdEditeur(), 
                        f.getEditeur());
            else if ("Jeu".equals(type))
                ret = creerVersionJeu(
                        f.getCodeBarre(),
                        f.getEdition(), 
                        f.getZone(),
                        f.getPrix(), 
                        f.getStock(),
                        f.getIdJeu(), 
                        f.getNom(),       //getNom() renvoie le nom du JEU, pas de la Version du jeu
                        f.getDescription(), stringToVector(/*f.getTags(),*/ "some string" ,','),
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
    protected Fabricant creerFabricant(Rapport rapport, String nomFabr)
            throws DonneesInsuffisantesException, EnregistrementExistantException
    {
        if ("".equals(nomFabr)) //si le nom du fabricant n'est pas saisi
            throw new DonneesInsuffisantesException("Impossible de créer le fabricant : un nom est requis.");
        
        //on vérifie que le fabricant n'existe pas déjà !
        Fabricant existant = chercherFabricant(-1, nomFabr);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer le fabricant : ce fabricant existe déjà.");
            
        //création du fabricant
        Fabricant fabr = new Fabricant();
        fabr.setNom(nomFabr);
        
        //sauvegarde dans la base de données
        this.modele.beginTransaction();
        this.modele.save(fabr);
        this.modele.getTransaction().commit();
        
        rapport.addOperation(fabr.getIdFabricant(), Rapport.Table.FABRICANT, Rapport.Operation.CREER);
        return fabr;
    }
    /**
     * Crée un éditeur. Assure l'unicité de l'enregistrement.
    */
    protected Editeur creerEditeur(Rapport rapport, String nomEditeur)
            throws DonneesInsuffisantesException, EnregistrementExistantException
    {
        if ("".equals(nomEditeur)) //si le nom de l'éditeur n'est pas saisi
            throw new DonneesInsuffisantesException("Impossible de créer l'éditeur : un nom est requis.");
        
        //on vérifie que l'éditeur n'existe pas déjà !
        Editeur existant = chercherEditeur(-1, nomEditeur);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer l'éditeur : cet éditeur existe déjà.");
            
        //création de l'éditeur
        Editeur ed = new Editeur();
        ed.setNom(nomEditeur);
        
        //sauvegarde dans la base de données
        this.modele.beginTransaction();
        this.modele.save(ed);
        this.modele.getTransaction().commit();
        
        rapport.addOperation(ed.getIdEditeur(), Rapport.Table.EDITEUR, Rapport.Operation.CREER);
        return ed;
    }
    /**
     * Crée un tag. Assure l'unicité de l'enregistrement.
    */
    protected Tag creerTag(Rapport rapport, String tag)
            throws DonneesInsuffisantesException, EnregistrementExistantException
            //Remarque : si une erreur est renvoyée, la faute en revient à un développeur !
    {
        if ("".equals(tag)) //si le nom de l'éditeur n'est pas saisi
            throw new DonneesInsuffisantesException("Impossible de créer le tag : un libellé est requis."); //vérifier le code appelant.
        
        //on vérifie que le tag n'existe pas déjà !
        Tag existant = chercherTag(-1, tag);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer le tag : ce tag existe déjà."); //vérifier le code appelant.
        
        //création du tag
        Tag t = new Tag();
        t.setLibelle(tag);
        
        //sauvegarde dans la base de données
        this.modele.beginTransaction();
        this.modele.save(t);
        this.modele.getTransaction().commit();
        
        rapport.addOperation(t.getIdTag(), Rapport.Table.TAG, Rapport.Operation.CREER);
        return t;
    }
//    /**
//     * Lie un tag à un jeu.
//     * L'identifiant du tag doit être valide. L'identifiant du jeu doit être valide. La relation ne doit pas déjà exister. Aucun contrôle d'erreur n'est réalisé.
//     */
//    protected void lierTag(Rapport rapport, Jeu jeu, Tag tag)
//    {
//        //TODO : on génère la requête pour créer l'enregistrement.
//        rapport.addOperation(desc., Rapport.Table.DESCRIPTION, Rapport.Operation.CREER);
//    }
    /**
     * Crée un jeu et/ou son éditeur. Assure l'unicité de l'enregistrement.
     * Si l'éditeur est inexistant dans la base de données, un nouvel éditeur est ajouté à la volée. De même pour les tags.
     */
    protected Jeu creerJeu(Rapport rapport, String nomJeu, String description, Vector<String> tags,
            int idEditeur, String nomEditeur) throws DonneesInsuffisantesException, EnregistrementExistantException
    {
        if ("".equals(nomJeu)) //si on ne crée pas un jeu.
        {
            try {
                creerEditeur(rapport, nomEditeur);}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "\nImpossible de créer le jeu : un nom est requis.");}
            return null;
        }
        else //si on crée un jeu
        {
            //on détermine l'identifiant de l'éditeur
            Editeur editeur = chercherEditeur(-1, nomEditeur);
            if (editeur == null)
                editeur = creerEditeur(rapport, nomEditeur); //s'il n'existe pas, on le crée à la volée.

            //on vérifie que le jeu n'existe pas déjà !
            Jeu jeuExistant = chercherJeu(-1, idEditeur, nomJeu, tags, "");
            if (jeuExistant != null)
                throw new EnregistrementExistantException("Impossible de créer le jeu : ce jeu existe déjà.");

            //création de la console
            Jeu jeu = new Jeu();
            jeu.setNom(nomJeu);
            jeu.setEditeur(editeur);
            jeu.setDescription(description);

            //traitement des tags
            for (String tag : tags)
            {
                //on vérifie l'existence du tag et, au besoin, on le crée.
                Tag t = chercherTag(-1, tag);
                if (t == null)
                    t = creerTag(rapport, tag);

                //jeu.getTags().add(t);
                
                rapport.addOperation(t.getIdTag(), Rapport.Table.DESCRIPTION, Rapport.Operation.CREER);
            }

            //sauvegarde dans la base de données
            this.modele.beginTransaction();
            this.modele.save(jeu);
            this.modele.getTransaction().commit();

            rapport.addOperation(jeu.getIdJeu(), Rapport.Table.JEU, Rapport.Operation.CREER);
            return jeu;
        }
    }
    /**
     * Crée une console et/ou son fabricant.
     * Si le fabricant est renseigné par un label inexistant dans la base de données, un nouveau fabricant est ajouté à la volée.
    */
    protected Console creerConsole(Rapport rapport, String nomConsole,
            int idFabr, String nomFabr) throws DonneesInsuffisantesException, EnregistrementExistantException
    {
        if ("".equals(nomConsole)) //si on ne crée pas une console.
        {
            try {
                creerFabricant(rapport, nomFabr);}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "\nImpossible de créer la console : un nom est requis.");}
            return null;
        }
        else //si on crée une console
        {
            //on détermine l'identifiant du fabricant
            Fabricant fabricant = chercherFabricant(-1, nomFabr);
            if (fabricant == null)
                fabricant = creerFabricant(rapport, nomFabr); //s'il n'existe pas, on le crée à la volée.

            //on vérifie que la console n'existe pas déjà !
            Console existante = chercherConsole(-1, idFabr, nomConsole, nomFabr);
            if (existante != null)
                throw new EnregistrementExistantException("Impossible de créer la console : cette console existe déjà.");

            //création de la console
            Console cons = new Console();
            cons.setNom(nomConsole);
            cons.setFabricant(fabricant);

            //sauvegarde dans la base de données
            this.modele.beginTransaction();
            this.modele.save(cons);
            this.modele.getTransaction().commit();

            rapport.addOperation(cons.getIdConsole(), Rapport.Table.CONSOLE, Rapport.Operation.CREER);
            return cons;
        }
    }
    /**
     * Crée une version d'une console et/ou la console elle-même et/ou son fabricant.
     * Si un produit est créé, assure la validité du code barre ainsi que l'unicité de l'enregistrement.
     * Si la console est inexistante dans la base de données, une nouvelle console est ajoutée à la volée. Par transitivité, cela s'applique au fabricant. Ne met pas à jour le fabricant d'une console existante.
     * La zone renseignée doit déjà exister dans la base de données.
    */
    protected Rapport creerVersionConsole(String cb, String edition, String nomZone,
            float prix, int stock,
            int idConsole, String nomConsole,
            int idFabr, String nomFabr)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
        Rapport rapport = new Rapport();
        
        if ("".equals(edition) && "".equals(nomZone)) //si on ne crée pas une version de console.
        {
            try {
                creerConsole(rapport, nomConsole,idFabr, nomFabr);}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "\nImpossible de créer la version de console : une information de zone ou d'édition est requise.");}
        }
        else //si on crée une version de console
        {
            cb = codeBarreValide(cb); //on vérifie le code barre
            
            //on détermine l'identifiant de la console
            Console console = chercherConsole(idConsole, idFabr, nomConsole, nomFabr);
            if (console == null)
                console = creerConsole(rapport, nomConsole, idFabr, nomFabr); //si elle n'existe pas, on la crée à la volée.
            
            //on détermine l'identifiant de la zone
            Zone zone = chercherZone(-1, nomZone);
            if (zone == null)
                throw new DonneeInvalideException("Impossible de créer la version de console : la zone renseignée n'existe pas.");
            
            //on vérifie que la version de console n'existe pas déjà !
            Vector<VersionConsole> existe = chercherVersionsConsole(cb, edition,
                    zone.getNom(), console.getNom(), console.getFabricant().getNom());
            if (!(existe == null) && !existe.isEmpty())
                throw new EnregistrementExistantException("Impossible de créer la version de console : cette dernière existe déjà.");
            
            //création de la version de console
            VersionConsole vc = new VersionConsole();
            vc.setCodeBarre(cb);
            vc.setEdition(edition);
            vc.setZone(zone);
            vc.setConsole(console);
            vc.setPrix(prix);
            vc.setStock(stock);

            //sauvegarde dans la base de données
            this.modele.beginTransaction();
            this.modele.save(vc);
            this.modele.getTransaction().commit();
            
            rapport.addOperation(vc.getIdVersionConsole(), Rapport.Table.VERSIONCONSOLE, Rapport.Operation.CREER);
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
    protected Rapport creerVersionJeu(String cb, String edition, String nomZone,
            float prix, int stock,
            int idJeu, String nomJeu, String description, Vector<String> tags,
            int idCons, String nomConsole,
            int idEditeur, String nomEditeur)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
        Rapport rapport = new Rapport();
        
        if ("".equals(edition) && "".equals(nomZone) && "".equals(nomConsole)) //si on ne crée pas une version de jeu.
        {
            try {
                creerJeu(rapport, nomJeu, description, tags, idEditeur, nomEditeur);}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "\nImpossible de créer la version de jeu : une information de plateforme, de zone ou d'édition est requise.");}
        }
        else //si on crée une version de jeu
        {
            cb = codeBarreValide(cb); //on vérifie le code barre
            
            //on détermine l'identifiant du jeu
            Jeu existant = chercherJeu(-1, idEditeur, nomJeu, tags, nomEditeur);
            if (existant == null)
                creerJeu(rapport, nomJeu, description, tags, idEditeur, nomEditeur); //s'il n'existe pas, on le crée à la volée.
            
            //on détermine l'identifiant de la zone
            Zone zone = chercherZone(-1, nomZone);
            if (zone == null)
                throw new DonneeInvalideException("Impossible de créer la version de console : la zone renseignée n'existe pas.");
            
            //on détermine l'identifiant de la plateforme
            Console console = chercherConsole(idCons, -1, nomConsole, "");
            if (console == null)
                throw new DonneeInvalideException("Impossible de créer la version de jeu : la plateforme renseignée n'existe pas.");
            
            //on détermine l'identifiant du jeu
            Jeu jeu = chercherJeu(idJeu, idEditeur, nomJeu, tags, nomEditeur);
            if (jeu == null)
                throw new DonneeInvalideException("Impossible de créer la version de jeu : le jeu renseigné n'existe pas.");
            
            //on vérifie que la version de jeu n'existe pas déjà !
            Vector<VersionJeu> existante = chercherVersionsJeu(cb, edition, zone.getNom(), console.getNom(), nomJeu, nomEditeur, tags);
            if (!(existante == null) && !existante.isEmpty())
                throw new EnregistrementExistantException("Impossible de créer la version de jeu : cette dernière existe déjà.");
            
            //création de la version de console
            VersionJeu vj = new VersionJeu();
            vj.setCodeBarre(cb);
            vj.setEdition(edition);
            vj.setZone(zone);
            vj.setConsole(console);
            vj.setJeu(jeu);
            vj.setPrix(prix);
            vj.setStock(stock);

            //sauvegarde dans la base de données
            this.modele.beginTransaction();
            this.modele.save(vj);
            this.modele.getTransaction().commit();
            
            rapport.addOperation(vj.getIdVersionJeu(), Rapport.Table.VERSIONCONSOLE, Rapport.Operation.CREER);
        }
        
        return rapport;
    }
    
    /**
     * Détermine, à partir d'un bean, quelle(s) requête(s) de recherche générer et exécuter. Transforme les résultats en formulaires.
     * Les formulaires renvoyés correspondront à des produits : nommément, soit à une version de jeu, soit à une version de console.
    */
    public Vector<ProduitForm> chercher(Form form) throws DonneeInvalideException, ResultatInvalideException/*, DonneesInsuffisantesException*/
    {
        Vector<ProduitForm> ret = new Vector<ProduitForm>();
        
        if (form instanceof CodeBarreForm) //recherche rapide par code barre
        {
            String cb = codeBarreValide(((CodeBarreForm) form).getCodeBarre());
System.out.println("Codebarreform CB : " + cb);
            /*for (VersionConsole enr : chercherVersionsConsole(cb, "", "", "", ""))
                ret.add(new ProduitForm(
                        enr.getConsole().getIdConsole(), enr.getIdVersionConsole(), -1, -1,
                        enr.getConsole().getFabricant().getIdFabricant(), "Console",
                        enr.getCodeBarre(), enr.getConsole().getNom(),
                        enr.getEdition(), enr.getZone().getNom(),
                        enr.getConsole().getFabricant().getNom(), "", "", "",
                        enr.getPrix(), enr.getStock()));//*/
            /*for (VersionJeu enr : chercherVersionsJeu(cb, "", "", "", "", "", new Vector<String>()))
                ret.add(new ProduitForm(
                        enr.getConsole().getIdConsole(), -1,
                        enr.getJeu().getIdJeu(), enr.getIdVersionJeu(),
                        enr.getJeu().getEditeur().getIdEditeur(), "Jeu",
                        enr.getCodeBarre(), enr.getJeu().getNom(), enr.getEdition(), enr.getZone().getNom(),
                        enr.getJeu().getEditeur().getNom(), enr.getJeu().getDescription(),
                        tagsToString(enr.getJeu().getTags(), ','), enr.getConsole().getNom(),
                        enr.getPrix(), enr.getStock()));*/
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
            //Vector<String> tags = stringToVector(f.getTags(), ',');
            String plateforme = f.getPlateforme();
            //Pas besoin de récupérer les identifiants, la description de jeu, le prix et le stock.
System.out.println("ProduitForm TYPE : " + type + " CB : " + cb + " NOM : " + nom + " EDITION : " + edition + " EDITEUR : " + editeur + " ZONE : " + zone + " PF : " + plateforme);
            
            /*if ("Console".equals(type))
            {
                if (!"".equals(cb) || !"".equals(edition) || !"".equals(zone) || !"".equals(nom) || !"".equals(editeur))
                for (VersionConsole enr : chercherVersionsConsole(cb, edition, zone, nom, editeur))
                    ret.add(new ProduitForm(
                            enr.getConsole().getIdConsole(), enr.getIdVersionConsole(), -1, -1,
                            enr.getConsole().getFabricant().getIdFabricant(), "Console",
                            enr.getCodeBarre(), enr.getConsole().getNom(), enr.getEdition(), enr.getZone().getNom(),
                            enr.getConsole().getFabricant().getNom(), "", "", "",
                            enr.getPrix(), enr.getStock()));
                else
                    throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");
            }
            else if ("Jeu".equals(type))
            {
                if (!"".equals(cb) || !"".equals(nom) || !"".equals(editeur) || !tags.isEmpty())
                    for (VersionJeu enr : chercherVersionsJeu(cb, edition, zone, plateforme, nom, editeur, tags))
                        ret.add(new ProduitForm(
                                enr.getConsole().getIdConsole(), enr.getConsole().getIdConsole(),
                                enr.getJeu().getIdJeu(), enr.getIdVersionJeu(),
                                enr.getJeu().getEditeur().getIdEditeur(), "Jeu",
                                enr.getCodeBarre(), enr.getJeu().getNom(), enr.getEdition(), enr.getZone().getNom(),
                                enr.getJeu().getEditeur().getNom(), enr.getJeu().getDescription(),
                                //tagsToString(enr.getJeu().getTags(), ','), enr.getConsole().getNom(),
                                enr.getPrix(), enr.getStock()));
                else
                    throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");
            }*/
        }
        
        return ret;
    }
    /**
     * Recherche les versions de consoles dont le code barre, l'édition, la zone et le fabricant correspondent parfaitement aux données renseignées,
     * et dont le nom contient la chaîne renseignée.
     * La zone et l'édition ne sont pas suffisantes pour lancer une recherche.
     */
    private Vector<VersionConsole> chercherVersionsConsole(String cb, String edition, String zone, String nom, String fabricant)
            throws DonneesInsuffisantesException, DonneeInvalideException
    {
        
        if ("".equals(cb) && "".equals(nom) && "".equals(fabricant))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche des produits de type console : il faut renseigner un code barre, un nom, ou un fabricant.");
        
        Vector<VersionConsole> ret = new Vector<VersionConsole>();
        
        HQLRecherche q = new HQLRecherche("VersionConsole vc"); //TODO: requête imbriquée imbrCons
        //rédaction de la requête imbriquée pour console
        if (!"".equals(nom) || !"".equals(fabricant)) //si la console est renseignée (et/ou son fabricant)
        {
            HQLRecherche imbrCons = new HQLRecherche("console c");
            imbrCons.setImbriquee(true);
            imbrCons.addCondition("c.nom", nom, HQLRecherche.Operateur.LIKE);
            //rédaction de la requête imbriquée pour fabricant
            if (!"".equals(fabricant)) //si le fabricant est renseigné
            {
                HQLRecherche imbrFabr = new HQLRecherche("fabricant f");
                imbrFabr.setImbriquee(true);
                imbrFabr.addCondition("f.nom", fabricant, HQLRecherche.Operateur.LIKE);
                
                imbrCons.addCondition("c.fabricant", imbrFabr.toString(), HQLRecherche.Operateur.IN);
            }
            
            q.addCondition("vc.console", imbrCons.toString(), HQLRecherche.Operateur.IN);
        }
        //rédaction des requêtes imbriquées pour zone
        if (!"".equals(zone)) //si la zone est renseignée
        {
            HQLRecherche imbrZone = new HQLRecherche("zone z");
            imbrZone.setImbriquee(true);
            imbrZone.addCondition("z.nom", zone, HQLRecherche.Operateur.EGAL);
            
            q.addCondition("vc.zone", imbrZone.toString(), HQLRecherche.Operateur.IN);
        }
        //autres conditions
        if (!"".equals(cb))
            q.addCondition("vc.codeBarre", cb, HQLRecherche.Operateur.EGAL);
        if (!"".equals(edition))
            q.addCondition("vc.edition", edition, HQLRecherche.Operateur.LIKE);
        
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        ret.addAll(resultats);
        
        return ret;
    }
    /**
     * Recherche les versions de consoles dont le code barre, l'édition, la zone et le fabricant correspondent parfaitement aux données renseignées,
     * et dont le nom contient la chaîne renseignée.
     * La zone et l'édition ne sont pas suffisantes pour lancer une recherche.
     */
    private Vector<VersionJeu> chercherVersionsJeu(String cb, String edition, String zone,
            String plateforme, String nom, String editeur, Vector<String> tags)
            throws DonneesInsuffisantesException
    {
        if ("".equals(cb) && "".equals(plateforme) && "".equals(nom) && "".equals(editeur) && tags.isEmpty())
            throw new DonneesInsuffisantesException("Erreur lors de la recherche des produits de type jeu : il faut renseigner un code barre, une plateforme, un nom, un éditeur ou au moins un tag.");
        
        Vector<VersionJeu> ret = new Vector<VersionJeu>();
        
        //TODO: la recherche à proprement parler.
        
        return ret;
    }
    /**
     * Recherche les consoles dont le nom correspond parfaitement à la chaîne renseignée et ayant le fabricant désigné.
     */
    private Console chercherConsole(int idConsole, int idFabr, String nomCons, String nomFabr) throws DonneesInsuffisantesException
    {
        if (idConsole < 0 && (nomCons == null || "".equals(nomCons)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la console : nom de la console non renseigné.");

        HQLRecherche q = new HQLRecherche("Console c");
        q.addCondition("c.nom", nomCons, HQLRecherche.Operateur.EGAL);
        if (!"".equals(nomFabr))
            q.addCondition("c.fabricant.nom", nomFabr, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else if (resultats.size() != 1)
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la console : plusieurs résultats sont retournés.");
        else
            return (Console) resultats.get(0);
//        
//        if (nomFabr != null && !"".equals(nomFabr))
//        {
//            Fabricant fabr = chercherFabricant(-1, nomFabr);
//            if (fabr == null)
//                return null;
//            //else
//            idFabr = fabr.getIdFabricant();
//        }
//        
//        /* Attention
//        Il est possible qu'une recherche renvoie plusieurs résultats. Par exemple, si deux consoles produites par des fabricants
//        différents ont le même nom et que le fabricant n'a pas été renseigné. Pour traiter ce type d'erreur,
//        on commence par stocker les résultats de la requête dans un vecteur ; puis, si le vecteur n'a pas une taille de 1,
//        on renvoie null (pour 0) ou on lance une exception.
//        */
//        Vector<Console> resultat;
//        
//        //TODO: la recherche à proprement parler. On ne réutilise pas chercherVersionsConsole car la correspondance demandée par chercherConsole est parfaite.
//        //Attention, pour le fabricant, si le nom est renseigné, on demande une correspondance parfaite.
//        
//        /*if (resultat.isEmpty())
//            return null;
//        else if (resultat.size() > 1)
//            throw new  DonneesInsuffisantesException("Erreur lors de la recherche de la console : plusieurs résultats obtenus. Veuillez renseigner le fabricant de la console.");
//        
//        return resultat.firstElement();*/
//        return null;
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
        {
            Editeur edtr = chercherEditeur(-1, nomEditeur);
            if (edtr == null)
                return null;
            //else
            idEditeur = edtr.getIdEditeur();
        }
        
        //!à implémenter; (voir chercher console, encore que, y'a les tags...');
        
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
        
        HQLRecherche q = new HQLRecherche("Fabricant f");
        q.addCondition("f.nom", nomFabr, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Fabricant) resultats.get(0);
    }
    /**
     * Recherche les fabricants dont le nom contient la chaîne renseignée.
     */
    private Vector<Fabricant> chercherFabricants(String nomFabr) throws DonneesInsuffisantesException
    {
        if (nomFabr == null || "".equals(nomFabr))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de fabricants : nom du fabricant non renseigné.");
        
        Vector<Fabricant> ret = new Vector<Fabricant>();
        
        HQLRecherche q = new HQLRecherche("Fabricant f");
        q.addCondition("f.nom", nomFabr, HQLRecherche.Operateur.LIKE);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        ret.addAll(resultats);
        
        return ret;
    }
    /**
     * Recherche l'éditeur dont le nom correspond parfaitement à la chaîne renseignée.
     */
    private Editeur chercherEditeur(int id, String nomEdit) throws DonneesInsuffisantesException
    {
        if (id < 0 && (nomEdit == null || "".equals(nomEdit)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de l'éditeur : nom de l'éditeur non renseigné.");
        
        HQLRecherche q = new HQLRecherche("Editeur e");
        q.addCondition("e.nom", nomEdit, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Editeur) resultats.get(0);
    }
    /**
     * Recherche les éditeurs dont le nom contient la chaîne renseignée.
     */
    private Vector<Editeur> chercherEditeurs(String nomEdit) throws DonneesInsuffisantesException
    {
        if (nomEdit == null || "".equals(nomEdit))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche des éditeurs : nom de l'éditeur non renseigné.");
        
        Vector<Editeur> ret = new Vector<Editeur>();
        
        HQLRecherche q = new HQLRecherche("Editeur e");
        q.addCondition("e.nom", nomEdit, HQLRecherche.Operateur.LIKE);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        ret.addAll(resultats);
        
        return ret;
    }
    /**
     * Recherche une zone dans la base de données. Si la zone renseignée est trouvé, un objet Zone est renvoyé. Sinon, la méthode renvoie null.
     */
    private Zone chercherZone(int id, String zone) throws DonneesInsuffisantesException
    {
        if (id < 0 && (zone == null || "".equals(zone)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la zone : nom de la zone non renseigné.");
        
        HQLRecherche q = new HQLRecherche("Zone z");
        q.addCondition("z.nom", zone, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Zone) resultats.get(0);
    }
    /**
     * Recherche un tag dans la base de données. Si le tag renseigné est trouvé, un objet Tag est renvoyé. Sinon, la méthode renvoie null.
     */
    private Tag chercherTag(int id, String tag) throws DonneesInsuffisantesException
    {
        if (id < 0 && (tag == null || "".equals(tag)))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du tag : nom du tag non renseigné.");
        
        HQLRecherche q = new HQLRecherche("Tag t");
        q.addCondition("t.libelle", tag, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Tag) resultats.get(0);
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
        
        //création de la zone
        Zone z = new Zone();
        z.setNom(zone);
        
        //sauvegarde dans la base de données
        this.modele.beginTransaction();
        this.modele.save(z);
        this.modele.getTransaction().commit();
        
        rapport.addOperation(z.getIdZone(), Rapport.Table.ZONE, Rapport.Operation.CREER);
        
        return rapport;
    }
    /**
     * Renvoie la liste des zones.
     */
    public Vector<String> listeZones()
    {
        Vector<String> ret = new Vector();
        
        List zones = modele.createQuery("from Zone").list();
        for (Object z : zones)
            ret.add(((Zone) z).getNom());
        
        return ret;
    }
    /**
     * Renvoie la liste des consoles.
     */
    public Vector<String> listeConsoles()
    {
        Vector<String> ret = new Vector();
        
        List consoles = modele.createQuery("from Zone").list();
        for (Object c : consoles)
            ret.add(((Console) c).getNom());
        
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
        //si le code barre n'est pas renseigné, il n'y a rien à faire
        if (cb == null || "".equals(cb))
            return cb;
        
        //on vérifie que l'entrée est légale
        String ret = "";
        if (cb.length() > 13)
            throw new DonneeInvalideException("Veuillez entrer un code barre composé de 13 chiffres");
        else
            try { long test = new Long(cb); }
            catch (NumberFormatException nfe) {
                throw new DonneeInvalideException("Veuillez entrer un code barre composé de 13 chiffres.");
            }

        //si le code barre est trop court, on complète à gauche par des 0.
        int missingCharacters = 13 - cb.length();
        for (int i = 0 ; i < missingCharacters ; i++)
            ret = ret.concat("0");
        return ret.concat(cb);
    }
    /**
     * Transforme un vecteur de tags en un vecteur de strings pour l'affichage.
     */
    protected static final String tagsToString(Set<Tag> tags, char separator)
    {
        Vector <String> vect = new Vector<String>();
        for (Tag t : tags)
            vect.add(t.getLibelle());
        return vectorToString(vect, separator);
    }
    protected static final Vector<String> stringToVector(String s, char separator)
    {
        Vector<String> ret = new Vector();
        String[] tab = s.split(Character.toString(separator));
        for (String str : tab)
            ret.add(str);
        return ret;
    }
    protected static final String vectorToString(Vector<String> v, char separator)
    {
        String ret = "";
        for (String s : v)//on ajoute la chaîne suivie d'un séparateur.
            ret = ret.concat(s + separator);
        //Par exemple, le vecteur {"mario", "plateforme", "3D"} donne ici ret = "mario,plateforme,3D,"
        if (!"".equals(ret)) //si le vecteur n'était pas vide,
            //on enlève le dernier séparateur (dans notre exemple, la dernière virgule)
            ret = ret.substring(0, ret.length() - 1);
        return ret;
    }
    protected static final String normalize(String s)
    {
        s = s.replaceAll(" ", "");
        s = s.replaceAll("é", "e");
        s = s.replaceAll("è", "e");
        s = s.replaceAll("ê", "e");
        s = s.replaceAll("à", "a");
        s = s.replaceAll("î", "i");
        s = s.replaceAll("ù", "u");
        s = s.toUpperCase(new Locale("FRENCH", "FRANCE"));
        return s;
    }
    
    /**
     * Démarre l'application
     */
    public static void main(String[] args)
    {
        try {
            //magical - do not touch
              /*@SuppressWarnings("unused")
                org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
                java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF); //or whatever level you need*/

            new Controleur();}
        catch (InitException ex) {
            System.out.println(ex.getMessage());}
    }
}
