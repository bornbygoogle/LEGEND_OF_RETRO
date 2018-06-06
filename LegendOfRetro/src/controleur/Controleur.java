/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import EntiteProvisoire.Facture;
import EntiteProvisoire.FactureLigneJeu;
import EntiteProvisoire.FactureLigneConsole;
import LOREntities.*;

import bean.CodeBarreForm;
import bean.FactureForm;
import bean.FactureLigneForm;
import bean.Form;
import bean.ProduitForm;
import bean.PromoForm;
import hibernateConfig.HibernateUtil;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import org.hibernate.*;
import vue.GUI;

/**
 * @author Adrien Marchand
 * La classe controleur contien:  1.Controleur() throws InitException-un contructeur qui initialise la Vue et le Modele et 2.Des methode qui vont faire l'aiguillage entre Vue-Modele 26:05:2018
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
     * la vue sera utilisé pour communiquer avec l'affichage
     * le modele va preparer la sesion hibernate
     * @throws InitException va etre lancér si un fichier de confiquration requis n'est pas trouvé
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
     * Détermine, à partir d'un bean, quelle(s) requête(s) d'INSERT va générer et va exécuter.
     * @param f est un objet de type ProduitForm-(voir See Also), ce objet est utilisé pour de efectuer l'operation de cast  et a l'intérieur de cette méthode il y a appelé une autre méthode (voir See Also)
     * @see bean.ProduitForm
     * @see #creerVersionConsole(controleur.Rapport, java.lang.String, java.lang.String, java.lang.String, float, int, java.lang.String, java.lang.String)
     * @throws DonneesInsuffisantesException si l'utilisateur rentre des données insufisantes
     * @throws DonneeInvalideException si l'utilisateur n'utilise le type de variable requise
     * @throws EnregistrementExistantException si la valeur entrée existe déja dans la base de données
     * @return Rapport donc va etre utiliseé pour HQLRecherche,voir le constructeur Rapport
    */
    public Rapport creer(CodeBarreForm form)
            throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementExistantException
    {
        if (!(form instanceof ProduitForm))
            throw new DonneesInsuffisantesException("Impossible de créer le produit : le code barre n'est pas une donnée suffisante.");
        
        ProduitForm f = (ProduitForm) form;
        Rapport rapport = new Rapport();
        String type = f.getType();

        if ("Console".equals(type))
        {
            f.setIdVersionJeu(-1);
            f.setIdVersionConsole(
                    creerVersionConsole(rapport,
                    f.getCodeBarre(),
                    normalize(f.getEdition()),
                    f.getZone(),
                    f.getPrix(),
                    f.getStock(),
                    normalize(f.getNom()),
                    normalize(f.getEditeur())));
        }
        else if ("Jeu".equals(type))
        {
            f.setIdVersionConsole(-1);
            f.setIdVersionJeu(creerVersionJeu(rapport,
                    f.getCodeBarre(),
                    normalize(f.getEdition()),
                    f.getZone(),
                    f.getPrix(),
                    f.getStock(),
                    normalize(f.getNom()),       //getNom() renvoie le nom du JEU, pas de la Version du jeu
                    f.getDescription(), stringToVector(normalize(f.getTags()) ,','),
                    f.getPlateforme(),
                    normalize(f.getEditeur())));
        }

        return rapport;
    }
    /**
     * Crée une facture décrite par le bean.
     * @param form formulaire décrivant la facture.
     * @throws DonneesInsuffisantesException si la facture ne contient aucune ligne.
     * @return un objet rapport qui permet d'afficher les différentes opérations réalisées.
    */
    public Rapport creer(FactureForm form) throws DonneesInsuffisantesException
    {
        if (form.getLignes().isEmpty())
            throw new DonneesInsuffisantesException("Erreur : la facture est vide.");
        
        Rapport rapport = new Rapport();
        
        //création de la facture
        Facture facture = new Facture();
        //type
        if (form.getNature()) //achat
            facture.setTypeFacture('a');
        else //vente
            facture.setTypeFacture('v');
        //réductions éventuelles
        facture.setReduction(form.getReductions());
        
        //date
        facture.setDate(Date.from(Instant.now()));
        
        /*/Client ou fournisseur lié
        TODO: à implémenter
        if (form.getActeurId() != -1) {
            //chercher Personne et l'affecter
        }
        else
            facture.setActeur(null);//*/

        //sauvegarde de la facture dans la base de données
        this.modele.beginTransaction();
        this.modele.save(facture);
        this.modele.getTransaction().commit();
        this.modele.flush();
        
        for(FactureLigneForm ligne : form.getLignes())
            creerFactureLigne(rapport, ligne, facture);
        
        return rapport;
    }
    /**
     * Crée une ligne et l'ajoute à la facture. Ceci fonctionne que le produit soit une console ou un jeu.
     * @param rapport un objet rapport qui permet d'afficher les différentes opérations réalisées.
     * @param ligne formulaire décrivant le contenu de la ligne
     * @param facture objet POJO auquel est liée la ligne à créer.
     * @throws DonneesInsuffisantesException si la ligne ne réfère à aucun produit
    */
    private void creerFactureLigne(Rapport rapport,
            FactureLigneForm ligne, Facture facture) throws DonneesInsuffisantesException
    {
        if (rapport == null || facture == null || ligne == null)
            throw new DonneesInsuffisantesException("Erreur : creerFactureLigne requiert des arguments non nuls.");
        else if (ligne.getProduit() == null)
            throw new DonneesInsuffisantesException("Erreur : la facture est vide.");
        
        if ("Jeu".equals(ligne.getProduit().getType()))
            creerFactureLigneJeu(rapport, ligne, facture);
        else if ("Console".equals(ligne.getProduit().getType()))
            creerFactureLigneConsole(rapport, ligne, facture);
        else
            throw new IllegalArgumentException("Erreur : une ligne correspond à un type de produit inconnu.");
    }
    /**
     * Crée une ligne et l'ajoute à la facture. Ceci ne fonctionne que si le produit est une console.
     * @param rapport un objet rapport qui permet d'afficher les différentes opérations réalisées.
     * @param ligne formulaire décrivant le contenu de la ligne
     * @param facture objet POJO auquel est liée la ligne à créer.
     * @throws DonneesInsuffisantesException si la ligne ne réfère à aucun produit
    */
    private void creerFactureLigneConsole(Rapport rapport,
            FactureLigneForm ligne, Facture facture) throws DonneesInsuffisantesException
    {
        FactureLigneConsole ligneConsole = new FactureLigneConsole();
        ligneConsole.setProduit((VersionConsole) this.modele.load(
                "VersionConsole", ligne.getProduit().getIdVersionConsole()));
        ligneConsole.setQuantite(ligne.getQuantite());
        
        //sauvegarde de la ligne dans la base de données
        this.modele.beginTransaction();
        this.modele.save(ligneConsole);
        this.modele.getTransaction().commit();
        this.modele.flush();
        
        //mise à jour de la facture dans la base de données
        facture.getFactureLigneConsoles().add(ligneConsole);
        this.modele.beginTransaction();
        this.modele.save(facture);
        this.modele.getTransaction().commit();
        this.modele.flush();
    }
    /**
     * Crée une ligne et l'ajoute à la facture. Ceci ne fonctionne que si le produit est un jeu.
     * @param rapport un objet rapport qui permet d'afficher les différentes opérations réalisées.
     * @param ligne formulaire décrivant le contenu de la ligne
     * @param facture objet POJO auquel est liée la ligne à créer.
     * @throws DonneesInsuffisantesException si la ligne ne réfère à aucun produit
    */
    private void creerFactureLigneJeu(Rapport rapport,
            FactureLigneForm ligne, Facture facture) throws DonneesInsuffisantesException
    {
        FactureLigneJeu ligneJeu = new FactureLigneJeu();
        ligneJeu.setProduit((VersionJeu) this.modele.load(
                "VersionJeu", ligne.getProduit().getIdVersionJeu()));
        ligneJeu.setQuantite(ligne.getQuantite());
        
        //sauvegarde de la ligne dans la base de données
        this.modele.beginTransaction();
        this.modele.save(ligneJeu);
        this.modele.getTransaction().commit();
        this.modele.flush();
        
        //mise à jour de la facture dans la base de données
        facture.getFactureLigneJeus().add(ligneJeu);
        this.modele.beginTransaction();
        this.modele.save(facture);
        this.modele.getTransaction().commit();
        this.modele.flush();
    }
     /**
     * Crée un fabricant. Assure l'unicité de l'enregistrement dans l'intérieur de cette méthode sont appelées les méthodes-voir See Also.
     * @see #chercherFabricant(String nomFabr) dans cette methode s'effectue un traitement qui appelle:
     * @see HQLRecherche#addCondition(String membreGauche, String membreDroite, Operateur operateur)
     * @see Fabricant#getNomFabricant()
     * @see Rapport#idDerniereOperation
     * @param rapport est une variable objet de type Rapport pour afficher un retour dans l'interface graphique voir {@link vue.Resultat}
     * @param nomFabr met en parametre une variable de type String
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de données insufisantes
     * @throws EnregistrementExistantException si la valeur entrée existe deja dans la base de données
     * @return Fabricant va retourner un objet de type Fabricant (voir See Also)
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
        this.modele.flush();

        rapport.addOperation(fabr.getIdFabricant(), Rapport.Table.FABRICANT, Rapport.Operation.CREER);
        return fabr;
    }

    /**
     * Crée un éditeur. Assure l'unicité de l'enregistrement.
     * @param rapport-une variable de type Rapport(voir See Also) qui est utilisé pour retourner une reponse dans l'interface graphique-(voir See Also).
     * @see Rapport
     * @param nomEditeur-est utilisé pour appeller la méthode setNomEditeur(nomEditeur)-voir See Also
     * @see LOREntities.Editeur#setNomEditeur(String nomEditeur)
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de données insufisantes
     * @throws EnregistrementExistantException si la valeur entrée existe deja dans la base de données
     * @return Editeur va retourner un objet de type Editeur (voir See Also)
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
        this.modele.flush();

        rapport.addOperation(ed.getIdEditeur(), Rapport.Table.EDITEUR, Rapport.Operation.CREER);
        return ed;
    }
    /**
     * Crée un tag. Assure l'unicité de l'enregistrement.
     * @param rapport met en parametre une variable de type Rapport qui est utilisé pour retourner une réponse dans l'interface graphique
     * @param tag-une variable de type String qui est utilise pour appeller la methode setLabelTag - (pour détail voir-See Also)  et après de sauvegarde dans la base de données
     * @see  LOREntities.Tag#setLabelTag(java.lang.String)
     * @throws DonneesInsuffisantesException si l'utilisateur rentre des données insufisantes
     * @throws EnregistrementExistantException si la valeur entrée existe deja dans la base de données
     * @return Tag va retourner un objet de type Tag,voir la classe Tag situé dans le package LOREntities-(voir  See Also)
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
        this.modele.flush();

        rapport.addOperation(t.getIdTag(), Rapport.Table.TAG, Rapport.Operation.CREER);
        return t;
    }
    /**
     * Crée un jeu et/ou son éditeur. Assure l'unicité de l'enregistrement.Si l'éditeur est inexistant dans la base de données, un nouvel éditeur est ajouté à la volée. De même pour les tags.
     * @param  rapport-une variable de type Rapport qui est utilisé pour retourner une réponse dans l'interface graphique
     * @param  nomJeu est utilisé pour etre mis en parametre dans la methode creerEditeur (voir See Also)pour créer un Editeur de jeu,soit pour un Jeu
     * @see  #creerEditeur(controleur.Rapport, java.lang.String)
     * @param  description est utilisé pour etre mis en parametre dans la methode creerEditeur (voir See Also)pour créer un Editeur de jeu,soit pour un Jeu
     * @param  tags est utilisé dans la methode creerEditeur (voir See Also)pour créer un Editeur de jeu,soit pour un Jeu
     * @param  nomEditeur est dans la methode creerEditeur (voir See Also)pour créer un Editeur de jeu,soit pour un Jeu
     * @throws DonneesInsuffisantesException si l'utilisateur rentre des données insufisantes
     * @throws EnregistrementExistantException si la valeur entrée existe deja dans la base de données
     * @return un objet de type jeu qui contien des attributs qui appartient à la classe Jeu (voir See Also)
     * @see LOREntities.Jeu
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

            //création du jeu
            Jeu jeu = new Jeu();
            jeu.setNomJeu(nomJeu);
            jeu.setEditeur(editeur);
            jeu.setDescriptionJeu(description);

            //création de l'enregistrement dans la table Jeu
            this.modele.beginTransaction();
            this.modele.save(jeu);
            this.modele.getTransaction().commit();
            this.modele.flush();
            
            rapport.addOperation(jeu.getIdJeu(), Rapport.Table.JEU, Rapport.Operation.CREER);
            
            //traitement des tags (requiert enregistrement Jeu préalablement créé)
            for (String tag : tags)
            {
                //on vérifie l'existence du tag et, au besoin, on le crée.
                Tag t = chercherTag(tag);
                if (t == null)
                    t = creerTag(rapport, tag);
                
                //on lie le jeu au tag dans la table DECRIRE
                Decrire d = new Decrire();
                d.setId(new DecrireId());
                d.getId().setIdTag(t.getIdTag());
                d.getId().setIdJeu(jeu.getIdJeu());
                
                //mise à jour jeu
                this.modele.beginTransaction();
                this.modele.saveOrUpdate(jeu);
                this.modele.getTransaction().commit();
                this.modele.flush();
                //mise à jour tag
                this.modele.beginTransaction();
                this.modele.saveOrUpdate(t);
                this.modele.getTransaction().commit();
                this.modele.flush();
                //création de l'enregistrement "décrire"
                this.modele.beginTransaction();
                this.modele.save(d);
                this.modele.getTransaction().commit();
                this.modele.flush();

                rapport.addOperation(t.getIdTag(), Rapport.Table.DESCRIPTION, Rapport.Operation.CREER);
                
            }
            
            return jeu;
        }
    }
    /**
     * Crée une console et/ou son fabricant.
     * Si le fabricant est renseigné par un label inexistant dans la base de données, un nouveau fabricant est ajouté à la volée voir les methodées qui existe dans l'interioeur de cette methode -voir SeeAlso.
     * @see #creerFabricant(controleur.Rapport, java.lang.String)
     * @see #chercherFabricant(java.lang.String)
     * @see #chercherConsole(java.lang.String, java.lang.String)
     * @see LOREntities.Console#setNomConsole(java.lang.String)
     * @see LOREntities.Fabricant#setNomFabricant(java.lang.String)
     * @param  rapport-met en parametre une variable de type Rapport qui est utilisé
     * pour retourner une réponse dans l'interface graphique
     * @param   nomConsole une variable qui represente le nom de la console
     * @param   nomFabr une variable qui represente le nom de fabricant pour ces deux variable on effectue des traitement si le nom de fabricant existe:
     * @see #chercherConsole(java.lang.String, java.lang.String)
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de données insufisantes
     * @throws EnregistrementExistantException si la valeur entre existe deja dans la base de données
     * @return  Console va retourner un objet de type Console
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
            this.modele.flush();

            rapport.addOperation(cons.getIdConsole(), Rapport.Table.CONSOLE, Rapport.Operation.CREER);
            return cons;
        }
    }
   /**
     * Crée une version d'une console et/ou la console elle-même et/ou son fabricant.
     * Si un produit est créé, il assure la validité du code barre ainsi que l'unicité de l'enregistrement.
     * Si la console est inexistante dans la base de données, une nouvelle console est ajoutée à la volée. Par transitivité, cela s'applique au fabricant. Ne met pas à jour le fabricant d'une console existante.
     * La zone renseignée doit déjà exister dans la base de données.
     * Dans l'intérieur de cette méthode sont appelées les méthodes-voir See Also.
     * @see #codeBarreValide(java.lang.String)
     * @see #creerConsole(controleur.Rapport, java.lang.String, java.lang.String)
     * @see #chercherConsole(java.lang.String, java.lang.String)
     * @see #chercherZone(java.lang.String)
     * @see #chercherVersionsConsole(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     * @param rapport utilisé pour afficher le resultat
     * @param cb met une variable de type String représente le code de barre"
     * @param edition met en parametre variable de type String représente l'editeur
     * @param nomZone met en parametre variable de type String représente la nom de Zone
     * @param prix met en parametre variable de type float représente le prix d'une version de console
     * @param stock met en parametre variable de type integer représente le stock d'un certaine version console
     * @param nomConsole met en parametre variable de type String représente le nome de la console
     * @param nomFabr met en parametre variable de type String représente le nom de fabricant
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de données insufisantes
     * @throws EnregistrementExistantException si la valeur entre existe dejq dans la base de données
     * @throws DonneeInvalideException si l'utilisateur va rentrer des variables non conformes comme type
     * @return Rapport une variable objet de type Rapport
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
            this.modele.flush();

            rapport.addOperation(vc.getIdVersionConsole(), Rapport.Table.VERSIONCONSOLE, Rapport.Operation.CREER);

            return vc.getIdVersionConsole();
        }

        //si aucun produit n'a été créé
        return -1;
    }
     /**
     * Crée une version d'un jeu et/ou le jeu lui-même et/ou son éditeur et/ou ses tags.
     * Si un produit est créé, il assure la validité du code barre ainsi que l'unicité de l'enregistrement.
     * Si le jeu est inexistant dans la base de données, un nouveau jeu est ajouté à la volée. Par transitivité, cela s'applique à l'éditeur et aux tags de ce nouveau jeu. Ne met pas à jour les tags et l'éditeur d'un jeu existant.
     * La zone renseignée doit déjà exister dans la base de données {@link #chercherZone(java.lang.String) }
     * La console renseignée doit déjà exister dans la base de données.
     * Dans l'intérieur de cette méthode sont appelées les méthodes-voir See Also.
     * @see #creerJeu(controleur.Rapport, java.lang.String, java.lang.String, java.util.Vector, java.lang.String)
     * @see #codeBarreValide(java.lang.String)
     * @see #chercherJeu(java.lang.String, java.util.Vector, java.lang.String)
     * @see #chercherVersionsJeu(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector)
     * @see #chercherConsole(java.lang.String, java.lang.String)
     * @param rapport utilisé pour afficher le resultat
     * @param cb variable de type String qui represente le code barre
     * @param edition met en parametre une variable de type String qui represente l'editeur
     * @param nomZone met en parametre une variable de type String qui represente le nome de la zone
     * @param prix met en parametre une variable de type float qui reprente le prix d'un versione jeu
     * @param stock met en parametre une variable de type integer qui represente le stock d'un cetain jeu
     * @param nomJeu met en parametre une variable de type String qui represente le nome de jeu
     * @param description met en parametre une variable de type String qui represent unde description d'un jeu
     * @param tags met en parametre un vecteur de type String qui represent des tags
     * @param nomConsole met en parametre une variable de type String qui represente le nome de la console
     * @param nomEditeur met en parametre une variable de type String qui represente le nome d'editeur
     * @throws DonneesInsuffisantesException si l'utilisateur rentre des données insufisantes
     * @throws EnregistrementExistantException si la valeur entre existe deja dans la base de données
     * @throws DonneeInvalideException si l'utilisateur va entrer des variables non conformes comme type
     * @return Rapport un objet de type Rapport pour etre afficher dand l'interface graphque
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
            Jeu jeu = chercherJeu(nomJeu, tags, nomEditeur);
            if (jeu == null)
                jeu = creerJeu(rapport, nomJeu, description, tags, nomEditeur); //s'il n'existe pas, on le crée à la volée.

            //on détermine l'identifiant de la zone
            Zone zone = chercherZone(nomZone);
            if (zone == null)
                throw new DonneeInvalideException("Impossible de créer la version de console : la zone renseignée n'existe pas.");

            //on détermine l'identifiant de la plateforme
            Console console = chercherConsole(nomConsole, "");
            if (console == null)
                throw new DonneeInvalideException("Impossible de créer la version de jeu : la plateforme renseignée n'existe pas.");

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
            this.modele.flush();

            rapport.addOperation(vj.getIdVersionJeu(), Rapport.Table.VERSIONJEU, Rapport.Operation.CREER);

            return vj.getIdVersionJeu();
        }

        //si aucun produit n'a été créé
        return -1;
    }

    /**
     * Détermine, à partir d'un bean, quelle(s) requête(s) de recherche générer et exécuter. Transforme les résultats en formulaires.
     * Les formulaires renvoyés correspondront à des produits : nommément, soit à une version de jeu, soit à une version de console.
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de donnes insufisantes
     * @throws ResultatInvalideException si le resultat affiché n'est pas conforme
     * @throws DonneeInvalideException si l'utilisateur va entrer des variables non conformes comme type
     * @param  form met en parametre une variable objet de type Form ce objet contien des attributs qui vont etre decharger dans un vecteur de type generique
     * le but est de chercher une version de console ou une verion de jeu dans la base de données
     * @return Vecteur( un vecteur de type  générique)retourne un vecteur des objets de type ProduitForm le but est
    */
    public Vector<ProduitForm> chercher(Form form) throws DonneeInvalideException, ResultatInvalideException, DonneesInsuffisantesException
    {
        if (form instanceof ProduitForm)
            return chercher((ProduitForm) form);
        else if (form instanceof CodeBarreForm)
            return chercher((CodeBarreForm) form);
        else
            throw new UnsupportedOperationException("On ne sait pas rechercher les Form de type " + form.getClass());
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
                    enr.getJeu().getEditeur().getNomEditeur(), enr.getJeu().getDescriptionJeu(),
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
        String nom = normalize(form.getNom());
        String edition = normalize(form.getEdition());
        String zone = form.getZone();
        String editeur = normalize(form.getEditeur());
        String description = form.getDescription();      //La description n'est pas un critère de recherche viable.
        Vector<String> tags = stringToVector(normalize(form.getTags()).replace(" ", ""), ',');
        String plateforme = form.getPlateforme();
        //Pas besoin de récupérer les identifiants, la description de jeu, le prix et le stock.

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
            if (!"".equals(cb) || !"".equals(nom) || !"".equals(editeur) || !tags.isEmpty())
                for (VersionJeu enr : chercherVersionsJeu(cb, edition, zone, plateforme, nom, editeur, tags))
                    ret.add(new ProduitForm(-1, enr.getIdVersionJeu(), "Jeu",
                            enr.getCodeBarre(), enr.getJeu().getNomJeu(), enr.getEdition(), enr.getZone().getNomZone(),
                            enr.getJeu().getEditeur().getNomEditeur(), enr.getJeu().getDescriptionJeu(),
                            decriresToString(enr.getJeu().getDecrires(), ','), enr.getConsole().getNomConsole(),
                            enr.getPrix(), enr.getStock()));
            else
                throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");
        }
        return ret;
    }
    public Vector<PromoForm> chercherPromo(PromoForm form) throws ResultatInvalideException, DonneeInvalideException, DonneesInsuffisantesException
    {
        Vector<PromoForm> ret = new Vector<PromoForm>();

        //On récupère les variables du bean pour améliorer la lisibilité
        String type = form.getType();
        String cb = form.getCodeBarre();
        if (!"".equals(cb))
            cb = codeBarreValide(cb);
        String nom = normalize(form.getNom());
        String edition = normalize(form.getEdition());
        String zone = form.getZone();
        String editeur = normalize(form.getEditeur());
        String description = form.getDescription();      //La description n'est pas un critère de recherche viable.
        Vector<String> tags = stringToVector(normalize(form.getTags()).replace(" ", ""), ',');
        String plateforme = form.getPlateforme();
        //Pas besoin de récupérer les identifiants, la description de jeu, le prix et le stock.

        if ("Console".equals(type))
        {
            if (!"".equals(edition) || !"".equals(zone) || !"".equals(editeur))
            for (VersionConsole enr : chercherVersionsConsolePromo(edition, zone, editeur))
                ret.add(new PromoForm(enr.getIdVersionConsole(), -1, "Console",
                        enr.getCodeBarre(), enr.getConsole().getNomConsole(), enr.getEdition(), enr.getZone().getNomZone(),
                        enr.getConsole().getFabricant().getNomFabricant(), "", "", "",
                        enr.getPrix(), enr.getStock()));
            else
                throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");
        }
        else if ("Jeu".equals(type))
        {
            if (!"".equals(cb) || !"".equals(nom) || !"".equals(editeur) || !tags.isEmpty())
                for (VersionJeu enr : chercherVersionsJeu(cb, edition, zone, plateforme, nom, editeur, tags))
                    ret.add(new PromoForm(-1, enr.getIdVersionJeu(), "Jeu",
                            enr.getCodeBarre(), enr.getJeu().getNomJeu(), enr.getEdition(), enr.getZone().getNomZone(),
                            enr.getJeu().getEditeur().getNomEditeur(), enr.getJeu().getDescriptionJeu(),
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
     * @param cb met en parametre une variable String qui represente le code barre
     * @param edition met en parametre une variable String qui represente l'editeur
     * @param zone met en parametre une variable String qui represente la zone
     * @param nome en parametre une variable String qui represente de la console
     * @param fabricant met en parametre une variable String qui represente le nome de fabricante de la console
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de données insufisantes
     * @throws EnregistrementExistantException si la valeur entre existe deja dans la base de données
     * @throws DonneeInvalideException si l'utilisateur va rentrer des variable non conformes comme type
     * @return Vector retourne un vecteur generique des objets de type VersionConsole pour afficher dans l'interface
     */
    private Vector<VersionConsole> chercherVersionsConsole(String cb, String edition, String zone, String nom, String fabricant)
            throws DonneesInsuffisantesException, DonneeInvalideException
    {
        if ("".equals(cb) && "".equals(nom) && "".equals(fabricant))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche des produits de type console : il faut renseigner un code barre, un nom, ou un fabricant.");

        Vector<VersionConsole> ret = new Vector<VersionConsole>();

        HQLRecherche q = new HQLRecherche("LOREntities.VersionConsole vc");
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
        this.modele.flush();
        ret.addAll(resultats);

        return ret;
    }
    private Vector<VersionConsole> chercherVersionsConsolePromo(String edition, String zone, String fabricant)
            throws DonneesInsuffisantesException, DonneeInvalideException
    {
        Vector<VersionConsole> ret = new Vector<VersionConsole>();

        HQLRecherche q = new HQLRecherche("LOREntities.VersionConsole vc");
        //rédaction de la requête imbriquée pour console
        if (!"".equals(fabricant)) //si la console est renseignée (et/ou son fabricant)
        {
            HQLRecherche imbrCons = new HQLRecherche("LOREntities.Console c");
            imbrCons.setImbriquee(true);
            imbrCons.setSelect("c.idConsole");
            //imbrCons.addCondition("c.nomConsole", nom, HQLRecherche.Operateur.LIKE);
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
        if (!"".equals(edition))
            q.addCondition("vc.edition", edition, HQLRecherche.Operateur.LIKE);

        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        this.modele.flush();
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
        this.modele.flush();

        if (resultats.isEmpty())
            
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (VersionConsole) resultats.get(0);
    }
    /**
     * Recherche les versions de jeux dont le code barre, l'édition, la zone et le fabricant correspondent parfaitement aux données renseignées,et dont le nom contient la chaîne renseignée.
     * La zone et l'édition ne sont pas suffisantes pour lancer une recherche.
     * A l'intérieur de cette méthode il y a appelé des méthodes de la classe -voir See Also
     * @see HQLRecherche
     * @param cb met en parametre une variable String qui represente le code barre
     * @param edition met en parametre une variable String qui represente l'editeur
     * @param zone met en parametre une variable String qui represente la zone
     * @param nome en parametre une variable String qui represente de la console
     * @param fabricant met en parametre une variable String qui represente le nome de fabricante de la console
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de données insufisantes
     * @throws EnregistrementExistantException si la valeur entre existe deja dans la base de données
     * @throws DonneeInvalideException si l'utilisateur va rentrer des variable non conformes comme type
     * @return Vector retourne un vecteur generique des objets de type VersionConsole pour afficher dans l'interface
     */
    private Vector<VersionJeu> chercherVersionsJeu(String cb, String edition, String zone,
            String plateforme, String nom, String editeur, Vector<String> tags)
            throws DonneesInsuffisantesException
    {
        if ("".equals(cb) && "".equals(plateforme) && "".equals(nom) && "".equals(editeur) && tags.isEmpty())
            throw new DonneesInsuffisantesException("Erreur lors de la recherche des produits de type jeu : il faut renseigner un code barre, une plateforme, un nom, un éditeur ou au moins un tag.");

        Vector<VersionJeu> ret = new Vector<VersionJeu>();

        HQLRecherche q = new HQLRecherche("LOREntities.VersionJeu vj");
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
                HQLRecherche imbrEditeur = new HQLRecherche("LOREntities.Editeur e");
                imbrEditeur.setImbriquee(true);
                imbrEditeur.setSelect("e.idEditeur");
                imbrEditeur.addCondition("e.nomEditeur", editeur, HQLRecherche.Operateur.LIKE);

                imbrJeu.addCondition("j.editeur.idEditeur", imbrEditeur.toString(), HQLRecherche.Operateur.IN);
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
                    imbrDecr.addCondition("d.tag.idTag", imbrTag.toString(), HQLRecherche.Operateur.IN);

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
        this.modele.flush();
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
     *@param nomCons une variable de type String utilisé dans la methode addCondition("c.nomConsole", nomCons, HQLRecherche.Operateur.EGAL) pour la recherche d'une console ou le nom correspond
     *@param nomFabr une variable de type String utilisé dans la methode addCondition("c.fabricant.nomFabricant", nomFabr, HQLRecherche.Operateur.EGAL) pour la recherche d'une console ou le nome est désigné
     *@return un objet de type Console,(voir See Also) qui est une classe.
     * @see  HQLRecherche#addCondition(java.lang.String, java.lang.String, controleur.HQLRecherche.Operateur)
     * @see  LOREntities.Console
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
        this.modele.flush();

        if (resultats.isEmpty())
            return null;
        else if (resultats.size() != 1)
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la console : plusieurs résultats sont retournés.");
        else
            return (Console) resultats.get(0);
    }
    /**
     * Recherche le jeu dont le nom correspond parfaitement à la chaîne renseignée et ayant l'éditeur et les tags renseignés.
     * @param nomJeu est ultilisé pour la methode faire declanché la methode DonneesInsuffisantesException
     * @param tags-n'est pas utilisé
     * @param  nomEditeur-une variable de type String utilisé dans la methode appelé chercherEditeur(nomEditeur)-voir SeeAlso
     * @return un objet de type Jeu(voir See Also) qui est une classe
     * @see LOREntities.Jeu
     * @see #chercherEditeur(java.lang.String)
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

        HQLRecherche q = new HQLRecherche("LOREntities.Jeu j");
        q.addCondition("j.nomJeu", nomJeu, HQLRecherche.Operateur.EGAL);
        if (!"".equals(nomEditeur))
            q.addCondition("j.editeur.idEditeur", idEditeur , HQLRecherche.Operateur.EGAL);
        System.out.println("Recherche Jeu"); //imprimé à des fins de test
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        this.modele.flush();

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
     * @param nomFabr une variable utilisé pour declancher eventuelment DonneesInsuffisantesException Exception et dans la methode addCondition-voir See Also
     * @return Fabricant un objet de type Fabricant qui est une classe voir See Also
     * @see HQLRecherche#addCondition(java.lang.String, java.lang.String, controleur.HQLRecherche.Operateur)
     * @see LOREntities.Fabricant
     */
    private Fabricant chercherFabricant(String nomFabr) throws DonneesInsuffisantesException
    {
        if (nomFabr == null || "".equals(nomFabr))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du fabricant : nom du fabricant non renseigné.");

        HQLRecherche q = new HQLRecherche("Fabricant f");
        q.addCondition("f.nomFabricant", nomFabr, HQLRecherche.Operateur.EGAL);
        System.out.println(q.toString()); //imprimé à des fins de test
        List resultats = modele.createQuery(q.toString()).list();
        this.modele.flush();

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
        //this.modele.flush();

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
        this.modele.flush();

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
        this.modele.flush();

        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Tag) resultats.get(0);
    }

     /**
     * Crée une zone dans la table des zones. Si la zone renseignée est trouvé, un objet Zone est renvoyé. Sinon, la méthode renvoie null.
     * On trouve des methodés a l'interioeur de cette methode voir See Also.
     * @see #chercherZone(java.lang.String)
     * @see LOREntities.Zone#setNomZone(java.lang.String)
     * @param zone mettre un parametre un String qui est utilisé d'abord pour verifier si on vérifie si la zone n'existe pas déjà aprés pour sauvegarde dans la base de données
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de données insufisantes
     * @throws EnregistrementExistantException si la valeur entre existe deja dans la base de données
     * @throws DonneeInvalideException si l'utilisateur va rentrer des variable non conformes comme type
     * @return  Zone après le traitement returne un objet de type Zone
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
        this.modele.flush();

        rapport.addOperation(z.getIdZone(), Rapport.Table.ZONE, Rapport.Operation.CREER);

        return rapport;
    }
//!TODO : créer ville / créer pays / créer personne (==> fonctions de recherche)
    
    /**
     * Renvoie la liste des Editions.
     * @param : type Console / Jeu
     * @return : un vecteur/liste de resultat de type Edition
     */
    public Vector<String> listeEdition(String type)
    {
        Vector<String> ret = new Vector();
        List editions;
        if (type=="Console")
            { editions = modele.createQuery("select vc.edition from LOREntities.VersionConsole vc").list(); }
        else
            { editions = modele.createQuery("select vj.edition from LOREntities.VersionJeu vj").list(); }

        for (Object e : editions)
            ret.add((String) e);
        this.modele.flush();
            
        return ret;
    }

    /**
     * Renvoie la liste des Editions.
     * @param : void
     * @return : un vecteur/liste de resultat de type Edition
     */
    public Vector<String> listeFabricant()
    {
        Vector<String> ret = new Vector();

        List fabricants = modele.createQuery("from LOREntities.Fabricant").list();
        for (Object f : fabricants)
            ret.add(((Fabricant) f).getNomFabricant());
        this.modele.flush();

        return ret;
    }    
    
    /**
     * Renvoie la liste des zones.
     * @param : void
     * @return : un vecteur/liste de resultat de type Zone
     */
    public Vector<String> listeZones()
    {
        Vector<String> ret = new Vector();

        List zones = modele.createQuery("from LOREntities.Zone z order by z.nomZone").list();
        for (Object z : zones)
            ret.add(((Zone) z).getNomZone());
        this.modele.flush();

        return ret;
    }
    /**
     * Renvoie la liste des consoles.
     * @param : void
     * @return : un vecteur/liste de resultat de type Console
     */
    public Vector<String> listeConsoles()
    {
        Vector<String> ret = new Vector();

        List consoles = modele.createQuery("from LOREntities.Console c order by c.nomConsole").list();
        for (Object c : consoles)
            ret.add(((Console) c).getNomConsole());
        this.modele.flush();

        return ret;
    }

    /**
     * Renvoie la liste des tags.
     * @param : void
     * @return : un vecteur/liste de resultat de type Tags
     */
    public Vector<String> listeTags()
    {
        Vector<String> ret = new Vector();
        
        List tags = modele.createQuery("from LOREntities.Tag t order by t.labelTag").list();
        for (Object t : tags)
            ret.add(((Tag) t).getLabelTag());
        this.modele.flush();
        
        return ret;
    }
    
    /**
     * Renvoie la liste des pays.
     * @param : void
     * @return : un vecteur/liste de resultat qui contient les noms des pays
     */
    public Vector<String> listePays()
    {
        Vector<String> ret = new Vector();
        
        List pays = modele.createQuery("from LOREntities.Pays p order by p.nomPays").list();
        for (Object p : pays)
            ret.add(((Pays) p).getNomPays());
        this.modele.flush();
        
        return ret;
    }
    /**
     * Renvoie la liste des villes d'un pays.
     * @param : void
     * @return : un vecteur/liste de resultat qui contient les noms des villes
     */
    public Vector<String> listeVilles(String pays)
    {
        Vector<String> ret = new Vector();
        
        List villes = modele.createQuery(
                "from LOREntities.Ville v where v.pays.nomPays = '" + pays + "' order by v.nomVille"
                ).list();
        for (Object v : villes)
            ret.add(((Ville) v).getNomVille() + " (" + ((Ville) v).getCp() + " )");
        this.modele.flush();
        
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
     * @param decrires un objet de type Decrire dans une colection,il est utilisé pour l'intération
     * @param separator est utilisé pour l'iterateur
     * @return return un String re resultat d'itération voir la classe Decrire (See Also)
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
     * args x
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
