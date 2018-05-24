/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import LOREntities.*;

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
    public Rapport creer(ProduitForm f)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
        {
        Rapport rapport = new Rapport();
        
        String type = f.getType();

        if ("Console".equals(type))
        {
            f.setIdVersionJeu(-1);
            f.setIdVersionConsole(
                    creerVersionConsole(rapport,
                    f.getCodeBarre(), 
                    f.getEdition(), 
                    f.getZone(),
                    f.getPrix(), 
                    f.getStock(),
                    f.getNom(),
                    f.getEditeur()));
        }
        else if ("Jeu".equals(type))
        {
            f.setIdVersionConsole(-1);
            f.setIdVersionJeu(creerVersionJeu(rapport,
                    f.getCodeBarre(),
                    f.getEdition(), 
                    f.getZone(),
                    f.getPrix(), 
                    f.getStock(),
                    f.getNom(),       //getNom() renvoie le nom du JEU, pas de la Version du jeu
                    f.getDescription(), stringToVector(f.getTags() ,','),
                    f.getPlateforme(),
                    f.getEditeur()));
        }
        
        return rapport;
    }
    public Rapport creer(CodeBarreForm form)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
            throw new DonneesInsuffisantesException("Impossible de créer le produit : le code barre n'est pas une donnée suffisante.");
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
        Fabricant existant = chercherFabricant(nomFabr);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer le fabricant : ce fabricant existe déjà.");
            
        //création du fabricant
        Fabricant fabr = new Fabricant();
        fabr.setNomFabricant(nomFabr);
        
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
        Editeur existant = chercherEditeur(nomEditeur);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer l'éditeur : cet éditeur existe déjà.");
            
        //création de l'éditeur
        Editeur ed = new Editeur();
        ed.setNomEditeur(nomEditeur);
        
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
        Tag existant = chercherTag(tag);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer le tag : ce tag existe déjà."); //vérifier le code appelant.
        
        //création du tag
        Tag t = new Tag();
        t.setLabelTag(tag);
        
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
            String nomEditeur) throws DonneesInsuffisantesException, EnregistrementExistantException
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
            Editeur editeur = chercherEditeur(nomEditeur);
            if (editeur == null)
                editeur = creerEditeur(rapport, nomEditeur); //s'il n'existe pas, on le crée à la volée.

            //on vérifie que le jeu n'existe pas déjà !
            Jeu jeuExistant = chercherJeu(nomJeu, tags, "");
            if (jeuExistant != null)
                throw new EnregistrementExistantException("Impossible de créer le jeu : ce jeu existe déjà.");

            //création de la console
            Jeu jeu = new Jeu();
            jeu.setNomJeu(nomJeu);
            jeu.setEditeur(editeur);
            jeu.setDescriptionJeu(description);

            //traitement des tags
            /*for (String tag : tags)
            {
                //on vérifie l'existence du tag et, au besoin, on le crée.
                Tag t = chercherTag(tag);
                if (t == null)
                    t = creerTag(rapport, tag);

                //jeu.getTags().add(t);
                
                rapport.addOperation(t.getIdTag(), Rapport.Table.DESCRIPTION, Rapport.Operation.CREER);
            }*/

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
    protected Console creerConsole(Rapport rapport, String nomConsole, String nomFabr)
            throws DonneesInsuffisantesException, EnregistrementExistantException
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
            Fabricant fabricant = chercherFabricant(nomFabr);
            if (fabricant == null)
                fabricant = creerFabricant(rapport, nomFabr); //s'il n'existe pas, on le crée à la volée.

            //on vérifie que la console n'existe pas déjà !
            Console existante = chercherConsole(nomConsole, nomFabr);
            if (existante != null)
                throw new EnregistrementExistantException("Impossible de créer la console : cette console existe déjà.");

            //création de la console
            Console cons = new Console();
            cons.setNomConsole(nomConsole);
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
    protected int creerVersionConsole(Rapport rapport, String cb, String edition, String nomZone,
            float prix, int stock, String nomConsole, String nomFabr)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
        if ("".equals(edition) && "".equals(nomZone)) //si on ne crée pas une version de console.
        {
            try {
                creerConsole(rapport, nomConsole, nomFabr);}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "\nImpossible de créer la version de console : une information de zone ou d'édition est requise.");}
        }
        else //si on crée une version de console
        {
            cb = codeBarreValide(cb); //on vérifie le code barre
            
            //on détermine l'identifiant de la console
            Console console = chercherConsole(nomConsole, nomFabr);
            if (console == null)
                console = creerConsole(rapport, nomConsole, nomFabr); //si elle n'existe pas, on la crée à la volée.
            
            //on détermine l'identifiant de la zone
            Zone zone = chercherZone(nomZone);
            if (zone == null)
                throw new DonneeInvalideException("Impossible de créer la version de console : la zone renseignée n'existe pas.");
            
            //on vérifie que la version de console n'existe pas déjà !
            Vector<VersionConsole> existe = chercherVersionsConsole(cb, edition,
                    zone.getNomZone(), console.getNomConsole(), console.getFabricant().getNomFabricant());
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
            
            return vc.getIdVersionConsole();
        }
        
        //si aucun produit n'a été créé
        return -1;
    }
    /**
     * Crée une version d'un jeu et/ou le jeu lui-même et/ou son éditeur et/ou ses tags.
     * Si un produit est créé, assure la validité du code barre ainsi que l'unicité de l'enregistrement.
     * Si le jeu est inexistant dans la base de données, un nouveau jeu est ajouté à la volée. Par transitivité, cela s'applique à l'éditeur et aux tags de ce nouveau jeu. Ne met pas à jour les tags et l'éditeur d'un jeu existant.
     * La zone renseignée doit déjà exister dans la base de données.
     * La console renseignée doit déjà exister dans la base de données.
    */
    protected int creerVersionJeu(Rapport rapport, String cb, String edition, String nomZone,
            float prix, int stock, String nomJeu, String description, Vector<String> tags,
            String nomConsole, String nomEditeur)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
        if ("".equals(edition) && "".equals(nomZone) && "".equals(nomConsole)) //si on ne crée pas une version de jeu.
        {
            try {
                creerJeu(rapport, nomJeu, description, tags, nomEditeur);}
            catch (EnregistrementExistantException eee) {
                throw new DonneesInsuffisantesException(eee.getMessage()
                        + "\nImpossible de créer la version de jeu : une information de plateforme, de zone ou d'édition est requise.");}
        }
        else //si on crée une version de jeu
        {
            cb = codeBarreValide(cb); //on vérifie le code barre
            
            //on détermine l'identifiant du jeu
            Jeu existant = chercherJeu(nomJeu, tags, nomEditeur);
            if (existant == null)
                creerJeu(rapport, nomJeu, description, tags, nomEditeur); //s'il n'existe pas, on le crée à la volée.
            
            //on détermine l'identifiant de la zone
            Zone zone = chercherZone(nomZone);
            if (zone == null)
                throw new DonneeInvalideException("Impossible de créer la version de console : la zone renseignée n'existe pas.");
            
            //on détermine l'identifiant de la plateforme
            Console console = chercherConsole(nomConsole, "");
            if (console == null)
                throw new DonneeInvalideException("Impossible de créer la version de jeu : la plateforme renseignée n'existe pas.");
            
            //on détermine l'identifiant du jeu
            Jeu jeu = chercherJeu(nomJeu, tags, nomEditeur);
            if (jeu == null)
                throw new DonneeInvalideException("Impossible de créer la version de jeu : le jeu renseigné n'existe pas.");
            
            //on vérifie que la version de jeu n'existe pas déjà !
            Vector<VersionJeu> existante = chercherVersionsJeu(cb, edition, zone.getNomZone(), console.getNomConsole(), nomJeu, nomEditeur, tags);
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
            
            rapport.addOperation(vj.getIdVersionJeu(), Rapport.Table.VERSIONJEU, Rapport.Operation.CREER);
            
            return vj.getIdVersionJeu();
        }
        
        //si aucun produit n'a été créé
        return -1;
    }
    
    /**
     * Détermine, à partir d'un bean, quelle(s) requête(s) de recherche générer et exécuter. Transforme les résultats en formulaires.
     * Les formulaires renvoyés correspondront à des produits : nommément, soit à une version de jeu, soit à une version de console.
    */
    public Vector<ProduitForm> chercher(Form form) throws DonneeInvalideException, ResultatInvalideException, DonneesInsuffisantesException
    {
        if (form instanceof CodeBarreForm)
            return chercher((CodeBarreForm) form);
        else //if (form instanceof ProduitForm)
            return chercher((ProduitForm) form);
    }
    public Vector<ProduitForm> chercher(CodeBarreForm form) throws DonneeInvalideException, ResultatInvalideException, DonneesInsuffisantesException
    {
        Vector<ProduitForm> ret = new Vector<ProduitForm>();
        
        String cb = codeBarreValide(form.getCodeBarre());
        for (VersionConsole enr : chercherVersionsConsole(cb, "", "", "", ""))
            ret.add(new ProduitForm(enr.getIdVersionConsole(), -1, "Console",
                    enr.getCodeBarre(), enr.getConsole().getNomConsole(),
                    enr.getEdition(), enr.getZone().getNomZone(),
                    enr.getConsole().getFabricant().getNomFabricant(), "", "", "",
                    enr.getPrix(), enr.getStock()));
        for (VersionJeu enr : chercherVersionsJeu(cb, "", "", "", "","", new Vector<String>()))
            ret.add(new ProduitForm(-1, enr.getIdVersionJeu(), "Jeu",
                    enr.getCodeBarre(), enr.getJeu().getNomJeu(), enr.getEdition(), enr.getZone().getNomZone(),
                    enr.getJeu().getNomJeu(), enr.getJeu().getDescriptionJeu(),
                    decriresToString(enr.getJeu().getDecrires(), ','), enr.getConsole().getNomConsole(),
                    enr.getPrix(), enr.getStock()));
        if (ret.size() > 1)
            throw new ResultatInvalideException("Erreur : la recherche du code barre " + cb
                    + " renvoie plus d'un résultat", ret);
        
        return ret;
    }
    public Vector<ProduitForm> chercher(ProduitForm form) throws DonneeInvalideException, ResultatInvalideException, DonneesInsuffisantesException
    {
        Vector<ProduitForm> ret = new Vector<ProduitForm>();

        //On récupère les variables du bean pour améliorer la lisibilité
        String type = form.getType();
        String cb = form.getCodeBarre();
        if (!"".equals(cb))
            cb = codeBarreValide(cb);
        String nom = form.getNom();
        String edition = form.getEdition();
        String zone = form.getZone();
        String editeur = form.getEditeur();
        String description = form.getDescription();      //La description n'est pas un critère de recherche viable.
        Vector<String> tags = stringToVector(form.getTags().replace(" ", ""), ',');
        String plateforme = form.getPlateforme();
        //Pas besoin de récupérer les identifiants, la description de jeu, le prix et le stock.

System.out.println(tags);
        
        if ("Console".equals(type))
        {
            if (!"".equals(cb) || !"".equals(edition) || !"".equals(zone) || !"".equals(nom) || !"".equals(editeur))
            for (VersionConsole enr : chercherVersionsConsole(cb, edition, zone, nom, editeur))
                ret.add(new ProduitForm(enr.getIdVersionConsole(), -1, "Console",
                        enr.getCodeBarre(), enr.getConsole().getNomConsole(), enr.getEdition(), enr.getZone().getNomZone(),
                        enr.getConsole().getFabricant().getNomFabricant(), "", "", "",
                        enr.getPrix(), enr.getStock()));
            else
                throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");
        }
        else if ("Jeu".equals(type))
        {
            if (!"".equals(cb) || !"".equals(nom) || !"".equals(editeur) /*|| !tags.isEmpty()*/)
                for (VersionJeu enr : chercherVersionsJeu(cb, edition, zone, plateforme, nom, editeur, tags))
                    ret.add(new ProduitForm(-1, enr.getIdVersionJeu(), "Jeu",
                            enr.getCodeBarre(), enr.getJeu().getNomJeu(), enr.getEdition(), enr.getZone().getNomZone(),
                            enr.getJeu().getNomJeu(), enr.getJeu().getDescriptionJeu(),
                            decriresToString(enr.getJeu().getDecrires(), ','), enr.getConsole().getNomConsole(),
                            enr.getPrix(), enr.getStock()));
            else
                throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");
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
        
        HQLRecherche q = new HQLRecherche("LOREntities.VersionConsole vc"); //TODO: requête imbriquée imbrCons
        //rédaction de la requête imbriquée pour console
        if (!"".equals(nom) || !"".equals(fabricant)) //si la console est renseignée (et/ou son fabricant)
        {
            HQLRecherche imbrCons = new HQLRecherche("LOREntities.Console c");
            imbrCons.setImbriquee(true);
            imbrCons.setSelect("c.idConsole");
            imbrCons.addCondition("c.nomConsole", nom, HQLRecherche.Operateur.LIKE);
            //rédaction de la requête imbriquée pour fabricant
            if (!"".equals(fabricant)) //si le fabricant est renseigné
            {
                HQLRecherche imbrFabr = new HQLRecherche("LOREntities.Fabricant f");
                imbrFabr.setImbriquee(true);
                imbrFabr.addCondition("f.nomFabricant", fabricant, HQLRecherche.Operateur.LIKE);                
                imbrCons.addCondition("c.fabricant", imbrFabr.toString(), HQLRecherche.Operateur.IN);
            }
            q.addCondition("vc.console", imbrCons.toString(), HQLRecherche.Operateur.IN);
        }
        //rédaction des requêtes imbriquées pour zone
        if (!"".equals(zone)) //si la zone est renseignée
        {
            HQLRecherche imbrZone = new HQLRecherche("LOREntities.Zone z");
            imbrZone.setImbriquee(true);
            imbrZone.setSelect("z.idZone");
            imbrZone.addCondition("z.nomZone", zone, HQLRecherche.Operateur.LIKE);            
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
    private VersionConsole chercherVersionConsole(int id) throws DonneeInvalideException
    {
        if (id < 0)
            throw new DonneeInvalideException("Impossible de chercher un produit (console) : aucun identifiant n'a été renseigné.");
        
        HQLRecherche q = new HQLRecherche("VersionConsole vc");
        q.addCondition("vc.idVersionConsole", id, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (VersionConsole) resultats.get(0);
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
        if ("".equals(cb) && "".equals(plateforme) && "".equals(nom) && "".equals(editeur) /*&& tags.isEmpty()*/)
            throw new DonneesInsuffisantesException("Erreur lors de la recherche des produits de type jeu : il faut renseigner un code barre, une plateforme, un nom, un éditeur ou au moins un tag.");
        
        Vector<VersionJeu> ret = new Vector<VersionJeu>();
        
        HQLRecherche q = new HQLRecherche("LOREntities.VersionJeu vj"); //TODO: requête imbriquée imbrCons
        //rédaction de la requête imbriquée pour console
        if (!"".equals(plateforme)) //si la console est renseignée
        {
            HQLRecherche imbrCons = new HQLRecherche("LOREntities.Console c");
            imbrCons.setImbriquee(true);
            imbrCons.setSelect("c.idConsole");
            imbrCons.addCondition("c.nomConsole", plateforme, HQLRecherche.Operateur.EGAL);
            
            q.addCondition("vj.console.idConsole", imbrCons.toString(), HQLRecherche.Operateur.IN);
        }

        // rédaction de la requete imbriquée Jeu
        if (!"".equals(nom) || !"".equals(editeur) || !tags.isEmpty()) //si le nom du jeu est renseignée
        {
            HQLRecherche imbrJeu = new HQLRecherche("LOREntities.Jeu j");
            imbrJeu.setImbriquee(true);
            
            if (!"".equals(nom)) //condition sur le nom
                imbrJeu.addCondition("j.nomJeu", nom, HQLRecherche.Operateur.LIKE);
            if (!"".equals(editeur)) //condition sur le développeur
            {
                HQLRecherche imbrEditeur = new HQLRecherche("LOREntities.Jeu j");
                imbrEditeur.setImbriquee(true);
                imbrEditeur.setSelect("j.editeur.idEditeur");
                imbrEditeur.addCondition("j.nomJeu", nom, HQLRecherche.Operateur.LIKE);            
            }
            if (!tags.isEmpty())
            {
                for (String tag : tags)
                {
                    //on sélectionne le tag
                    HQLRecherche imbrTag = new HQLRecherche("LOREntities.Tag t");
                    imbrTag.setImbriquee(true);
                    imbrTag.setSelect("t.idTag");
                    imbrTag.addCondition("t.labelTag", tag, HQLRecherche.Operateur.EGAL);   
                    
                    //on liste les jeux des relations "décrire" correspondant à ce tag (identifiants seulement)
                    HQLRecherche imbrDecr = new HQLRecherche("LOREntities.Decrire d");
                    imbrDecr.setImbriquee(true);
                    imbrDecr.setSelect("d.jeu.idJeu");
                    imbrDecr.addCondition("d.tag.dTag", imbrTag.toString(), HQLRecherche.Operateur.IN);   
                    
                    //la requête qui recherche le jeu sélectionne parmi les jeux qui ont tous ces tags
                    imbrJeu.addCondition("j.idJeu", imbrDecr.toString(), HQLRecherche.Operateur.IN);
                }
            }
            
            q.addCondition("vj.jeu", imbrJeu.toString(), HQLRecherche.Operateur.IN);
        } 

        //rédaction des requêtes imbriquées pour zone
        if (!"".equals(zone)) //si la zone est renseignée
        {
            HQLRecherche imbrZone = new HQLRecherche("LOREntities.Zone z");
            imbrZone.setImbriquee(true);
            imbrZone.setSelect("z.idZone");
            imbrZone.addCondition("z.nomZone", zone, HQLRecherche.Operateur.LIKE);            
            q.addCondition("vj.zone", imbrZone.toString(), HQLRecherche.Operateur.IN);
        }
        //autres conditions
        if (!"".equals(cb)) //code barre
            q.addCondition("vj.codeBarre", cb, HQLRecherche.Operateur.EGAL);
        if (!"".equals(edition)) //edition
            q.addCondition("vj.edition", edition, HQLRecherche.Operateur.LIKE);
        
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        ret.addAll(resultats);
        
        return ret;
    }
    private VersionJeu chercherVersionJeu(int id) throws DonneeInvalideException
    {
        if (id < 0)
            throw new DonneeInvalideException("Impossible de chercher un produit (jeu) : aucun identifiant n'a été renseigné.");
        
        HQLRecherche q = new HQLRecherche("VersionJeu vj");
        q.addCondition("vj.idVersionJeu", id, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (VersionJeu) resultats.get(0);
    }
    /**
     * Recherche les consoles dont le nom correspond parfaitement à la chaîne renseignée et ayant le fabricant désigné.
     */
    private Console chercherConsole(String nomCons, String nomFabr) throws DonneesInsuffisantesException
    {
        if (nomCons == null || "".equals(nomCons))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la console : nom de la console non renseigné.");

        HQLRecherche q = new HQLRecherche("LOREntities.Console c");
        q.addCondition("c.nomConsole", nomCons, HQLRecherche.Operateur.EGAL);
        if (!"".equals(nomFabr))
            q.addCondition("c.fabricant.nomFabricant", nomFabr, HQLRecherche.Operateur.EGAL);
        System.out.println("Recherche Console"); //imprimé à des fins de test
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else if (resultats.size() != 1)
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la console : plusieurs résultats sont retournés.");
        else
            return (Console) resultats.get(0);
    }
    /**
     * Recherche le jeu dont le nom correspond parfaitement à la chaîne renseignée et ayant l'éditeur et les tags renseignés.
     */
    private Jeu chercherJeu(String nomJeu, Vector<String> tags, String nomEditeur) 
            throws DonneesInsuffisantesException
    {
        int idEditeur = 0;
        if (nomJeu == null || "".equals(nomJeu))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du jeu : nom du jeu non renseigné.");
        
        if (nomEditeur != null && !"".equals(nomEditeur))
        {
            Editeur edtr = chercherEditeur(nomEditeur);
            if (edtr == null)
                return null;
            //else
            idEditeur = edtr.getIdEditeur();
        }
        
        //!à implémenter; (voir chercher console, encore que, y'a les tags...');
        
        HQLRecherche q = new HQLRecherche("LOREntities.Jeu j");
        q.addCondition("j.nomJeu", nomJeu, HQLRecherche.Operateur.EGAL);
        if (!"".equals(nomEditeur))
            q.addCondition("j.editeur.idEditeur", idEditeur , HQLRecherche.Operateur.EGAL);
        System.out.println("Recherche Jeu"); //imprimé à des fins de test
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        /* Attention
        Il est possible qu'une recherche renvoie plusieurs résultats. Par exemple, si deux jeux produits par des éditeurs
        différents ont le même nom et que l'éditeur n'a pas été renseigné. Pour traiter ce type d'erreur,
        on commence par stocker les résultats de la requête dans un vecteur ; puis, si le vecteur n'a pas une taille de 1,
        on renvoie null ou on lance une exception.
        */
        //Vector<Jeu> resultat;
        
        //TODO: la recherche à proprement parler. On ne réutilise pas chercherVersionsJeu car la correspondance demandée par chercherJeu est parfaite.
        //Attention, pour l'éditeur, si le nom est renseigné, on demande une correspondance parfaite.
        //Penser à traiter les tags avec une requête imbriquée
        
        /*if (resultat.isEmpty())
            return null;
        else if (resultat.size() > 1)
            throw new  DonneesInsuffisantesException("Erreur lors de la recherche du jeu : plusieurs résultats obtenus. Veuillez renseigner l'éditeur du jeu.");
        
        return resultat.firstElement();*/
        
        if (resultats.isEmpty())
            return null;
        else if (resultats.size() != 1)
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la console : plusieurs résultats sont retournés.");
        else
            return (Jeu) resultats.get(0);
        //return null;
    }
    /**
     * Recherche les fabricants dont le nom contient la chaîne renseignée.
     */
    private Fabricant chercherFabricant(String nomFabr) throws DonneesInsuffisantesException
    {
        if (nomFabr == null || "".equals(nomFabr))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du fabricant : nom du fabricant non renseigné.");
        
        HQLRecherche q = new HQLRecherche("Fabricant f");
        q.addCondition("f.nomFabricant", nomFabr, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Fabricant) resultats.get(0);
    }
    /**
     * Recherche l'éditeur dont le nom correspond parfaitement à la chaîne renseignée.
     */
    private Editeur chercherEditeur(String nomEdit) throws DonneesInsuffisantesException
    {
        if (nomEdit == null || "".equals(nomEdit))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de l'éditeur : nom de l'éditeur non renseigné.");
        
        HQLRecherche q = new HQLRecherche("Editeur e");
        q.addCondition("e.nomEditeur", nomEdit, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Editeur) resultats.get(0);
    }
    /**
     * Recherche une zone dans la base de données. Si la zone renseignée est trouvé, un objet Zone est renvoyé. Sinon, la méthode renvoie null.
     */
    private Zone chercherZone(String zone) throws DonneesInsuffisantesException
    {
        if (zone == null || "".equals(zone))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la zone : nom de la zone non renseigné.");
        
        HQLRecherche q = new HQLRecherche("Zone z");
        q.addCondition("z.nomZone", zone, HQLRecherche.Operateur.EGAL);
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
    private Tag chercherTag(String tag) throws DonneesInsuffisantesException
    {
        if (tag == null || "".equals(tag))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du tag : nom du tag non renseigné.");
        
        HQLRecherche q = new HQLRecherche("Tag t");
        q.addCondition("t.labelTag", tag, HQLRecherche.Operateur.EGAL);
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
            throws EnregistrementExistantException, DonneesInsuffisantesException, DonneeInvalideException
    {
        Rapport rapport = new Rapport();
        
        if (zone == null || "".equals(zone))
            throw new DonneesInsuffisantesException("Erreur lors de la création de la zone : nom de la zone non renseigné.");
        else if ("Autre".equals(zone))
            throw new DonneesInsuffisantesException("Erreur lors de la création de la zone : on ne peut pas nommer une zone 'Autre'.");
            
        //on vérifie que la zone n'existe pas déjà !
        Zone existante = chercherZone(zone);
        if (existante != null)
            throw new EnregistrementExistantException("Impossible de créer la zone : cette zone existe déjà.");
        
        //création de la zone
        Zone z = new Zone();
        z.setNomZone(zone);
        
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
        
        List zones = modele.createQuery("from LOREntities.Zone").list();
        for (Object z : zones)
            ret.add(((Zone) z).getNomZone());       
        return ret;
    }
    /**
     * Renvoie la liste des consoles.
     */
    public Vector<String> listeConsoles()
    {
        Vector<String> ret = new Vector();
        
        List consoles = modele.createQuery("from LOREntities.Console").list();
        for (Object c : consoles)
            ret.add(((Console) c).getNomConsole());
        
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
     * @param tags
     * @param separator
     * @return 
     */
    protected static final String decriresToString(Set<Decrire> decrires, char separator)
    {
        Vector <String> vect = new Vector<String>();
        for (Decrire d : decrires)
            vect.add(d.getTag().getLabelTag());
        return vectorToString(vect, separator);
    }
    protected static final Vector<String> stringToVector(String s, char separator)
    {
        Vector<String> ret = new Vector();
        String[] tab = s.split(Character.toString(separator));
        for (String str : tab)
            if (!"".equals(str)) //Cela arrive si le paramètre s est une chaîne vide.
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
