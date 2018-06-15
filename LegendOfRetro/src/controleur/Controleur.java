/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import LOREntities.*;

import bean.CodeBarreForm;
import bean.FactureForm;
import bean.FactureLigneForm;
import bean.Form;
import bean.PersonneForm;
import bean.ProduitForm;
import bean.PromoForm;
import hibernateConfig.HibernateUtil;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.hibernate.*;
import vue.GUI;
import static vue.critProduit.labelPhoto;


/**
 * @author Adrien Marchand
 * La classe controleur contien:  1.Controleur() throws InitException-un contructeur qui initialise la Vue et le Modele et 2.Des methode qui vont faire l'aiguillage entre Vue-Modele 26:05:2018
 */
public class Controleur
{
    private static final float TVA = 1.2f;
    private static final int largeurFacture = 43;
    private static final String nomEntreprise = "Legend Of Retro";
    private GUI vue; //utilisé pour communiquer avec l'affichage
    private static Session modele; //session hibernate

    public Controleur()
    {
        init();
    }
    /**
     * la vue sera utilisé pour communiquer avec l'affichage
     * le modele va preparer la sesion hibernate
     * @throws InitException va etre lancér si un fichier de confiquration requis n'est pas trouvé
     */
    public void init()
    {
        this.vue = new GUI(this);
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
        
        //prixTTC
        facture.setPrixTtc(calculTotalTTC(form));
        
        //date
        facture.setDateFacture(Date.from(Instant.now()));
        
        //Client ou fournisseur lié
System.out.println("        TODO: à implémenter, Personne dans Facture (méthode creer(FactureForm))");
        /*
        if (form.getActeur() != null) {
            //chercher Personne et l'affecter
        }
        else
            facture.setActeur(null);//*/

        //sauvegarde de la facture dans la base de données
        modele.beginTransaction();
        modele.save(facture);
        modele.getTransaction().commit();
        modele.flush();
        
        for(FactureLigneForm ligne : form.getLignes())
            creerLigneFacture(rapport, ligne, facture);
        
        rapport.addOperation(facture.getIdFacture(), Rapport.Table.FACTURE, Rapport.Operation.CREER);
        
        try {
            exporter(facture);}
        catch (IOException ex) {System.out.println(ex.getMessage());}
        
        return rapport;
    }
    /**
     * Crée une ligne et l'ajoute à la facture. Ceci fonctionne que le produit soit une console ou un jeu.
     * @param rapport un objet rapport qui permet d'afficher les différentes opérations réalisées.
     * @param ligne formulaire décrivant le contenu de la ligne
     * @param facture objet POJO auquel est liée la ligne à créer.
     * @throws DonneesInsuffisantesException si la ligne ne réfère à aucun produit
    */
    private void creerLigneFacture(Rapport rapport,
            FactureLigneForm ligne, Facture facture) throws DonneesInsuffisantesException
    {
        if (rapport == null || facture == null || ligne == null)
            throw new DonneesInsuffisantesException("Erreur : creerLigneFacture requiert des arguments non nuls.");
        else if (ligne.getProduit() == null)
            throw new DonneesInsuffisantesException("Erreur : la facture est vide.");
        
        if ("Jeu".equals(ligne.getProduit().getType()))
            creerLigneFactureJeu(rapport, ligne, facture);
        else if ("Console".equals(ligne.getProduit().getType()))
            creerLigneFactureConsole(rapport, ligne, facture);
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
    private void creerLigneFactureConsole(Rapport rapport,
            FactureLigneForm ligne, Facture facture) throws DonneesInsuffisantesException
    {
        LigneFactureConsole ligneConsole = new LigneFactureConsole();
        ligneConsole.setId(new LigneFactureConsoleId(
                ligne.getProduit().getIdVersionConsole(), facture.getIdFacture()));
        ligneConsole.setQuantite(ligne.getQuantite());
        
        try {
            //mise à jour de la version de console dans la base de données
            VersionConsole vc = chercherVersionConsole(ligneConsole.getId().getIdVersionConsole());
            vc.getLigneFactureConsoles().add(ligneConsole);
            int nouveauStock = vc.getStock() - ligneConsole.getQuantite();
            if (nouveauStock < 0)
                throw new DonneeInvalideException("Impossible de créer la ligne : le stock obtenu est négatif.");
            vc.setStock(nouveauStock);
            modele.beginTransaction();
            modele.save(facture);
            modele.getTransaction().commit();
            modele.flush();
            //sauvegarde de la ligne dans la base de données
            modele.beginTransaction();
            modele.save(ligneConsole);
            modele.getTransaction().commit();
            modele.flush();

            //mise à jour de la facture dans la base de données
            facture.getLigneFactureConsoles().add(ligneConsole);
            modele.beginTransaction();
            modele.save(facture);
            modele.getTransaction().commit();
            modele.flush();

            rapport.addOperation(facture.getIdFacture(), Rapport.Table.LIGNEFACTURECONSOLE, Rapport.Operation.CREER);
        }
        catch (DonneeInvalideException die) {System.out.println(die.getMessage());}
    }
    /**
     * Crée une ligne et l'ajoute à la facture. Ceci ne fonctionne que si le produit est un jeu.
     * @param rapport un objet rapport qui permet d'afficher les différentes opérations réalisées.
     * @param ligne formulaire décrivant le contenu de la ligne
     * @param facture objet POJO auquel est liée la ligne à créer.
     * @throws DonneesInsuffisantesException si la ligne ne réfère à aucun produit
    */
    private void creerLigneFactureJeu(Rapport rapport,
            FactureLigneForm ligne, Facture facture) throws DonneesInsuffisantesException
    {
        LigneFactureJeu ligneJeu = new LigneFactureJeu();
        ligneJeu.setId(new LigneFactureJeuId(
                facture.getIdFacture(), ligne.getProduit().getIdVersionJeu()));
        ligneJeu.setQuantite(ligne.getQuantite());
        
        try {
            //mise à jour de la version de jeu dans la base de données
            VersionJeu vj = chercherVersionJeu(ligneJeu.getId().getIdVersionJeu());
            vj.getLigneFactureJeus().add(ligneJeu);
            int nouveauStock = vj.getStock() - ligneJeu.getQuantite();
            if (nouveauStock < 0)
                throw new DonneeInvalideException("Impossible de créer la ligne : le stock obtenu est négatif.");
            vj.setStock(nouveauStock);
            modele.beginTransaction();
            modele.save(facture);
            modele.getTransaction().commit();
            modele.flush();
            //mise à jour de la facture dans la base de données
            facture.getLigneFactureJeus().add(ligneJeu);
            modele.beginTransaction();
            modele.save(facture);
            modele.getTransaction().commit();
            modele.flush();

            //sauvegarde de la ligne dans la base de données
            modele.beginTransaction();
            modele.save(ligneJeu);
            modele.getTransaction().commit();
            modele.flush();

            rapport.addOperation(facture.getIdFacture(), Rapport.Table.LIGNEFACTUREJEU, Rapport.Operation.CREER);
        }
        catch (DonneeInvalideException die) {System.out.println(die.getMessage());}
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
        modele.beginTransaction();
        modele.save(fabr);
        modele.getTransaction().commit();
        modele.flush();

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
        modele.beginTransaction();
        modele.save(ed);
        modele.getTransaction().commit();
        modele.flush();

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
        modele.beginTransaction();
        modele.save(t);
        modele.getTransaction().commit();
        modele.flush();

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
            //remarque : les tags ne sont pas un critère de recherche
            Jeu jeuExistant = chercherJeu(nomJeu, new Vector<String>(), "");
            if (jeuExistant != null)
                throw new EnregistrementExistantException("Impossible de créer le jeu : ce jeu existe déjà.");

            //création du jeu
            Jeu jeu = new Jeu();
            jeu.setNomJeu(nomJeu);
            jeu.setEditeur(editeur);
            jeu.setPhotoJeu("");
            jeu.setDescriptionJeu(description);

            //création de l'enregistrement dans la table Jeu
            modele.beginTransaction();
            modele.save(jeu);
            modele.getTransaction().commit();
            modele.flush();
            
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
                modele.beginTransaction();
                modele.saveOrUpdate(jeu);
                modele.getTransaction().commit();
                modele.flush();
                //mise à jour tag
                modele.beginTransaction();
                modele.saveOrUpdate(t);
                modele.getTransaction().commit();
                modele.flush();
                //création de l'enregistrement "décrire"
                modele.beginTransaction();
                modele.save(d);
                modele.getTransaction().commit();
                modele.flush();

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
            modele.beginTransaction();
            modele.save(cons);
            modele.getTransaction().commit();
            modele.flush();

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
            modele.beginTransaction();
            modele.save(vc);
            modele.getTransaction().commit();
            modele.flush();

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
            modele.beginTransaction();
            modele.save(vj);
            modele.getTransaction().commit();
            modele.flush();

            rapport.addOperation(vj.getIdVersionJeu(), Rapport.Table.VERSIONJEU, Rapport.Operation.CREER);

            return vj.getIdVersionJeu();
        }

        //si aucun produit n'a été créé
        return -1;
    }
    
    /**
     * Crée un pays. Assure l'unicité de l'enregistrement.
     * @param rapport le rapport dans lequel l'opération sera enregistrée
     * @param nomPays le nom exact du pays à créer
     * @throws DonneesInsuffisantesException si l'utilisateur rentre des données insufisantes
     * @throws EnregistrementExistantException si la valeur entrée existe deja dans la base de données
     * @return un rapport dans lequel l'opération est enregistrée
    */
    public Rapport creerPays(String nomPays)
            throws DonneesInsuffisantesException, EnregistrementExistantException
    {
        if ("".equals(nomPays)) //si le nom de du pays n'est pas saisi
            throw new DonneesInsuffisantesException("Impossible de créer le pays : un nom est requis."); //vérifier le code appelant.

        //on vérifie que le pays n'existe pas déjà !
        Pays existant = chercherPays(nomPays);
        if (existant != null)
            throw new EnregistrementExistantException("Impossible de créer le pays : ce pays existe déjà."); //vérifier le code appelant.

        //création du pays
        Pays p = new Pays();
        p.setNomPays(nomPays);

        //sauvegarde dans la base de données
        modele.beginTransaction();
        modele.save(p);
        modele.getTransaction().commit();
        modele.flush();

        Rapport rapport = new Rapport();
        rapport.addOperation(p.getIdPays(), Rapport.Table.PAYS, Rapport.Operation.CREER);
        return rapport;
    }
    /**
     * Crée une ville.
     * @param nomVille le nom exact de la ville à créer
     * @param CP le code postal de la ville à créer
     * @param nomPays le nom exact du pays auquel appartient la ville
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de données insufisantes
     * @throws EnregistrementExistantException si la valeur entre existe deja dans la base de données
     * @throws DonneeInvalideException - si le pays est renseigné n'existe pas dans la base de données
     * - si le code postal est déjà attribué
     * @return un rapport dans lequel l'opération est enregistrée
    */
    public Rapport creerVille(String nomVille, String cp, String nomPays)
            throws DonneesInsuffisantesException, EnregistrementExistantException, DonneeInvalideException
    {
        if ("".equals(nomVille) || "".equals(cp) || "".equals(nomPays))
            throw new DonneesInsuffisantesException("Impossible de créer la ville : un nom, un code postal et un pays sont requis.");

        //on détermine l'identifiant du pays
        Pays pays = chercherPays(nomPays);
        if (pays == null)
            throw new DonneeInvalideException("Impossible de créer la ville : le pays renseigné n'existe pas dans la base de données.");

        //on vérifie que la ville n'existe pas déjà !
        Ville existante = chercherVille(nomVille, cp, nomPays);
        if (existante != null)
            throw new EnregistrementExistantException("Impossible de créer la ville : cette ville existe déjà.");

        //création de la ville
        Ville ville = new Ville();
        ville.setNomVille(nomVille);
        ville.setCp(cp);
        ville.setPays(pays);

        //sauvegarde dans la base de données
        modele.beginTransaction();
        modele.save(ville);
        modele.getTransaction().commit();
        modele.flush();

        Rapport rapport = new Rapport();
        rapport.addOperation(ville.getIdVille(), Rapport.Table.VILLE, Rapport.Operation.CREER);
        return rapport;
    }

    /**
     * Détermine, à partir d'un bean, quelle(s) requête(s) de recherche générer et exécuter. Transforme les résultats en formulaires.
     * Les formulaires renvoyés correspondront à des produits : nommément, soit à une version de jeu, soit à une version de console.
     * @throws DonneesInsuffisantesException si l'utilisateur rentre de donnes insufisantes
     * @throws ResultatInvalideException si le resultat affiché n'est pas conforme
     * @throws DonneeInvalideException si l'utilisateur va entrer des variables non conformes comme type
     * @param  form met en parametre une variable objet de type Form ce objet contien des attributs qui vont etre decharger dans un vecteur de type generique
 le but est de chercherProduit une version de console ou une verion de jeu dans la base de données
     * @return Vecteur( un vecteur de type  générique)retourne un vecteur des objets de type ProduitForm le but est
    */
    public Vector<ProduitForm> chercherProduits(Form form) throws DonneeInvalideException, ResultatInvalideException, DonneesInsuffisantesException
    {
        if (form instanceof ProduitForm)
                return chercherProduit((ProduitForm) form);
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
                    enr.getConsole().getFabricant().getNomFabricant(), "", "", "", "",
                    enr.getPrix(), enr.getStock(), getCoteProduct("Console",enr.getIdVersionConsole()) ));
        for (VersionJeu enr : chercherVersionsJeu(cb, "", "", "", "","", new Vector<String>()))
            ret.add(new ProduitForm(-1, enr.getIdVersionJeu(), "Jeu",
                    enr.getCodeBarre(), enr.getJeu().getNomJeu(), enr.getEdition(), enr.getZone().getNomZone(),
                    enr.getJeu().getEditeur().getNomEditeur(), enr.getJeu().getPhotoJeu(), enr.getJeu().getDescriptionJeu(),
                    decriresToString(enr.getJeu().getDecrires(), ','), enr.getConsole().getNomConsole(),
                    enr.getPrix(), enr.getStock(), getCoteProduct("Jeu",enr.getIdVersionJeu())));
        if (ret.size() > 1)
            throw new ResultatInvalideException("Erreur : la recherche du code barre " + cb
                    + " renvoie plus d'un résultat", ret);

        return ret;
    }
    public Vector<ProduitForm> chercherProduit(ProduitForm form) throws DonneeInvalideException, ResultatInvalideException, DonneesInsuffisantesException
    {
        Vector<ProduitForm> ret = new Vector<ProduitForm>();

        //On récupère les variables du bean pour améliorer la lisibilité
        int idVersion = form.getIdVersionConsole();
                System.out.println(idVersion);
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
            {
                ProduitForm pf = new ProduitForm();
                pf.setIdVersionConsole(enr.getIdVersionConsole());
                pf.setIdVersionJeu(-1);
                pf.setType("Console");
                pf.setCodeBarre(enr.getCodeBarre());
                pf.setNom(enr.getConsole().getNomConsole());
                pf.setEdition(enr.getEdition());
                pf.setZone(enr.getZone().getNomZone());
                pf.setEditeur(enr.getConsole().getFabricant().getNomFabricant());
                pf.setPrix(enr.getPrix());
                pf.setStock(enr.getStock());
                pf.setCote(getCoteProduct("Console",enr.getIdVersionConsole()));
                ret.add(pf);
            }
            else
                throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");
        }
        else if ("Jeu".equals(type))
        {
            if (!"".equals(cb) || !"".equals(nom) || !"".equals(editeur) || !tags.isEmpty())
                for (VersionJeu enr : chercherVersionsJeu(cb, edition, zone, plateforme, nom, editeur, tags))
            {
                ProduitForm pf = new ProduitForm();
                pf.setIdVersionConsole(-1);
                pf.setIdVersionJeu(enr.getIdVersionJeu());
                pf.setType("Jeu");
                pf.setCodeBarre(enr.getCodeBarre());
                pf.setNom(enr.getJeu().getNomJeu());
                pf.setEdition(enr.getEdition());
                pf.setZone(enr.getZone().getNomZone());
                pf.setEditeur(enr.getJeu().getEditeur().getNomEditeur());
                pf.setPhoto(enr.getJeu().getPhotoJeu());
                pf.setDescription(enr.getJeu().getDescriptionJeu());
                pf.setTags(decriresToString(enr.getJeu().getDecrires(), ','));
                pf.setPlateforme(enr.getConsole().getNomConsole());
                pf.setPrix(enr.getPrix());
                pf.setStock(enr.getStock());
                pf.setCote(getCoteProduct("Jeu",enr.getIdVersionJeu()));
                ret.add(pf);
            }
            else
                throw new DonneesInsuffisantesException("Données insuffisantes pour lancer une recherche.");
        }
        return ret;
    }
    public Vector<PromoForm> chercherPromo(PromoForm form) throws ResultatInvalideException, DonneeInvalideException, DonneesInsuffisantesException
    {
        Vector<PromoForm> ret = new Vector<PromoForm>();

        //On récupère les variables du bean pour améliorer la lisibilité
        int idPromo = 0;
        String type = form.getType();
        String cb = form.getCodeBarre();
        if (!"".equals(cb))
            cb = codeBarreValide(cb);
        String nom = normalize(form.getNom());
        String edition = normalize(form.getEdition());
        String zone = form.getZone();
        String editeur = normalize(form.getEditeur());
        String description = form.getDescription();
        Vector<String> tags = stringToVector(normalize(form.getTags()).replace(" ", ""), ',');
        String plateforme = form.getPlateforme();
        Float cote = 0.0f;
        //Pas besoin de récupérer les identifiants, la description de jeu, le prix et le stock.

        if ("Console".equals(type))
        {
            for (VersionConsole enr : chercherVersionsConsolePromo(edition, zone, editeur))
            {
                /*if (enr.getIdVersionConsole()<1) cote = 0.0f;
                else 
                {   System.out.println(enr.getIdVersionConsole());
                    cote = getCoteProduct("Console", enr.getIdVersionConsole());}*/
                // Vérifier s'il y a pas deja une promo sur ce VersionConsole
                float prix = enr.getPrix();
                if (chercherPromoExiste(type, enr.getIdVersionConsole()))
                {
                    PromoConsole pctmp = chercherPromoConsole(chercherIdPromo(type,enr.getIdVersionConsole()));
                    idPromo = pctmp.getIdPromoConsole();
                    prix = pctmp.getPrixPromoConsole();
                }
                else { idPromo = 0; }
                ret.add(new PromoForm(idPromo,enr.getIdVersionConsole(), -1, "Console",
                        enr.getCodeBarre(), enr.getConsole().getNomConsole(), enr.getEdition(), enr.getZone().getNomZone(),
                        enr.getConsole().getFabricant().getNomFabricant(), "", "", "", "",
                        prix, enr.getStock(), getCoteProduct(type, enr.getIdVersionConsole())));
            }
        }
        else if ("Jeu".equals(type))
        {
            for (VersionJeu enr : chercherVersionsJeuPromo(edition, zone, plateforme,editeur, tags))
            {
                // Vérifier s'il y a pas deja une promo sur ce VersionConsole
                float prix = enr.getPrix();
                if (chercherPromoExiste(type, enr.getIdVersionJeu()))
                {
                    PromoJeu pctmp = chercherPromoJeu(chercherIdPromo(type,enr.getIdVersionJeu())); 
                    idPromo = pctmp.getIdPromoJeu();
                    prix = pctmp.getPrixPromoJeu();
                }
                else { idPromo = 0; }
                ret.add(new PromoForm(idPromo,-1, enr.getIdVersionJeu(), "Jeu",
                    enr.getCodeBarre(), enr.getJeu().getNomJeu(), enr.getEdition(), enr.getZone().getNomZone(),
                    enr.getJeu().getEditeur().getNomEditeur(), ""/*Photo*/, enr.getJeu().getDescriptionJeu(),
                    decriresToString(enr.getJeu().getDecrires(), ','), enr.getConsole().getNomConsole(),
                    prix, enr.getStock(), getCoteProduct(type, enr.getIdVersionJeu())));
            }
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

        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();
        ret.addAll(resultats);

        return ret;
    }
   /**
     * Recherche les promotions des versions de consoles 
     * @param id id du PromoConsole
     * @return Vector retourne un vecteur generique des objets de type PromoConsole 
     */
    private PromoConsole chercherPromoConsole(int id) throws DonneeInvalideException
    {
        if (id < 0)
            throw new DonneeInvalideException("Impossible de chercher un promotion produit (console) : aucun identifiant n'a été renseigné.");

        HQLRecherche q = new HQLRecherche("PromoConsole pc");
        q.addCondition("pc.idPromoConsole", id, HQLRecherche.Operateur.EGAL);
        List resultats = modele.createQuery(q.toString()).list();
        
        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (PromoConsole) resultats.get(resultats.size()-1);
    }
   /**
     * Recherche les promotions des versions de produit
     * @param string type du produit
     * @param id id du VersionConsole
     * @return id du PromoConsole correspond
     */
    private int chercherIdPromo(String type, int id) throws DonneeInvalideException
    {
        int idPromo = 0;
        List resultats = null;
        if (id < 0)
            throw new DonneeInvalideException("Impossible de chercher un promotion produit (console) : aucun identifiant n'a été renseigné.");
  
        if ("Console".equals(type))
        {
            HQLRecherche q = new HQLRecherche("PromoConsole pc");
            HQLRecherche imbrVersionConsole = new HQLRecherche("LOREntities.VersionConsole vc");
            imbrVersionConsole.setImbriquee(true);
            imbrVersionConsole.setSelect("vc.idVersionConsole");
            imbrVersionConsole.addCondition("vc.idVersionConsole", id, HQLRecherche.Operateur.EGAL);
            q.addCondition("pc.versionConsole", imbrVersionConsole.toString(), HQLRecherche.Operateur.IN);
            resultats = modele.createQuery(q.toString()).list();
        }
        else if ("Jeu".equals(type))
        {
            HQLRecherche q = new HQLRecherche("PromoJeu pj");
            HQLRecherche imbrVersionConsole = new HQLRecherche("LOREntities.VersionJeu vj");
            imbrVersionConsole.setImbriquee(true);
            imbrVersionConsole.setSelect("vj.idVersionJeu");
            imbrVersionConsole.addCondition("vj.idVersionJeu", id, HQLRecherche.Operateur.EGAL);
            q.addCondition("pj.versionJeu", imbrVersionConsole.toString(), HQLRecherche.Operateur.IN);
            resultats = modele.createQuery(q.toString()).list();
        }        
                
        if (resultats.isEmpty())
            return 0;
        else //on suppose qu'il n'y a qu'un seul résultat !
        {
            if ("Console".equals(type))
            {
                idPromo  = ((PromoConsole) resultats.get(resultats.size()-1)).getIdPromoConsole();
            }
            else if ("Jeu".equals(type))
            {
                idPromo  = ((PromoJeu) resultats.get(resultats.size()-1)).getIdPromoJeu();
            } 
        }
            return idPromo;
    }
   /**
     * Recherche les promotions des versions de produits
     * @param String type du produit
     * @param id id du PromoConsole
     * @return Vector retourne un vecteur generique des objets de type PromoConsole 
     */
    private boolean chercherPromoExiste(String type, int id) throws DonneeInvalideException
    {
        List resultats = null;
        if (id < 0)
            throw new DonneeInvalideException("Impossible de chercher un promotion produit (console) : aucun identifiant n'a été renseigné.");

        if ("Console".equals(type))
        {
            HQLRecherche q = new HQLRecherche("PromoConsole pc");
            HQLRecherche imbrVersionConsole = new HQLRecherche("LOREntities.VersionConsole vc");
            imbrVersionConsole.setImbriquee(true);
            imbrVersionConsole.setSelect("vc.idVersionConsole");
            imbrVersionConsole.addCondition("vc.idVersionConsole", id, HQLRecherche.Operateur.EGAL);
            q.addCondition("pc.versionConsole", imbrVersionConsole.toString(), HQLRecherche.Operateur.IN);
            resultats = modele.createQuery(q.toString()).list();
        }
        else if ("Jeu".equals(type))
        {
            HQLRecherche q = new HQLRecherche("PromoJeu pj");
            HQLRecherche imbrVersionConsole = new HQLRecherche("LOREntities.VersionJeu vj");
            imbrVersionConsole.setImbriquee(true);
            imbrVersionConsole.setSelect("vj.idVersionJeu");
            imbrVersionConsole.addCondition("vj.idVersionJeu", id, HQLRecherche.Operateur.EGAL);
            q.addCondition("pj.versionJeu", imbrVersionConsole.toString(), HQLRecherche.Operateur.IN);
            resultats = modele.createQuery(q.toString()).list();
        } 
        
        if (resultats.isEmpty())
            return false;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return true;
    }
   /**
     * Recherche les promotions des versions de jeux 
     * @param id id du PromoJeu
     * @return Vector retourne un vecteur generique des objets de type PromoJeux 
     */
    private PromoJeu chercherPromoJeu(int id) throws DonneeInvalideException
    {
        if (id < 0)
            throw new DonneeInvalideException("Impossible de chercher un produit (jeu) : aucun identifiant n'a été renseigné.");

        HQLRecherche q = new HQLRecherche("PromoJeu pj");
        q.addCondition("pj.idPromoJeu", id, HQLRecherche.Operateur.EGAL);
        List resultats = modele.createQuery(q.toString()).list();

        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (PromoJeu) resultats.get(resultats.size() - 1);
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

        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();
        ret.addAll(resultats);

        return ret;
    }
    private VersionConsole chercherVersionConsole(int id) throws DonneeInvalideException
    {
        if (id < 0)
            throw new DonneeInvalideException("Impossible de chercher un produit (console) : aucun identifiant n'a été renseigné.");

        HQLRecherche q = new HQLRecherche("VersionConsole vc");
        q.addCondition("vc.idVersionConsole", id, HQLRecherche.Operateur.EGAL);
        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();

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

        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();
        ret.addAll(resultats);

        return ret;
    }
    private Vector<VersionJeu> chercherVersionsJeuPromo(String edition, String zone,
            String plateforme, String editeur, Vector<String> tags)
            throws DonneesInsuffisantesException
    {
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
        if (!"".equals(editeur) || !tags.isEmpty()) //si le nom du jeu est renseignée
        {
            HQLRecherche imbrJeu = new HQLRecherche("LOREntities.Jeu j");
            imbrJeu.setImbriquee(true);

            /*if (!"".equals(nom)) //condition sur le nom
                imbrJeu.addCondition("j.nomJeu", nom, HQLRecherche.Operateur.LIKE);*/
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
        /*if (!"".equals(cb)) //code barre
            q.addCondition("vj.codeBarre", cb, HQLRecherche.Operateur.EGAL);*/
        if (!"".equals(edition)) //edition
            q.addCondition("vj.edition", edition, HQLRecherche.Operateur.LIKE);

        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();
        ret.addAll(resultats);

        return ret;
    }
    private VersionJeu chercherVersionJeu(int id) throws DonneeInvalideException
    {
        if (id < 0)
            throw new DonneeInvalideException("Impossible de chercher un produit (jeu) : aucun identifiant n'a été renseigné.");

        HQLRecherche q = new HQLRecherche("VersionJeu vj");
        q.addCondition("vj.idVersionJeu", id, HQLRecherche.Operateur.EGAL);
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
        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();

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
        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();

        if (resultats.isEmpty())
            return null;
        else if (resultats.size() != 1)
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du jeu : plusieurs résultats sont retournés.");
        else
            return (Jeu) resultats.get(0);
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
        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();

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
        List resultats = modele.createQuery(q.toString()).list();
        //modele.flush();

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
        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();

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
        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();

        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Tag) resultats.get(0);
    }
    
   /**
     * Recherche le client/fournisseur dont le nom correspond parfaitement à la chaîne renseignée et/ou ayant le prenom renseigné.
     *@param nomPers une variable de type String utilisé dans la methode .addCondition("pers.nom", nomPers, HQLRecherche.Operateur.LIKE) pour la recherche d'un client via son nom
     *@param prenomPers une variable de type String utilisé dans la methode query.addCondition("pers.prenom", prenomPers, HQLRecherche.Operateur.LIKE) pour la recherche d'un client via son prenom
     *@return un objet de type Personne,(voir See Also) qui est une classe.
     * @see  HQLRecherche#addCondition(java.lang.String, java.lang.String, controleur.HQLRecherche.Operateur)
     * @see  LOREntities.Personne
     */
    
   private Personne chercherPersonne(String nomPers, String prenomPers) throws DonneesInsuffisantesException{
       
        if ("".equals(nomPers) || "".equals(prenomPers)){
            throw new DonneesInsuffisantesException(
                    "Erreur lors de la recherche du client/fournisseur : le nom ET le prenom doivent être renseignés.");
        }
        
        HQLRecherche query = new HQLRecherche("LOREntities.Personne pers");
               
        if (!"".equals(nomPers))
            query.addCondition("pers.nom", nomPers, HQLRecherche.Operateur.LIKE);
        if (!"".equals(prenomPers))
            query.addCondition("pers.prenom", prenomPers, HQLRecherche.Operateur.LIKE);
     
        List resultats = modele.createQuery(query.toString()).list();
        modele.flush();

        if (resultats.isEmpty())
            return null;
        else if (resultats.size() != 1)
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la console : plusieurs résultats sont retournés.");
        else
            return (Personne) resultats.get(0);
    }
   private Vector<PersonneForm> chercherPersonnes(PersonneForm form) throws DonneesInsuffisantesException{
        
       Vector<PersonneForm> retour = new Vector<PersonneForm>();
        
        String nom = normalize(form.getNom());
        String prenom = normalize(form.getPrenom());
        String ville = normalize(form.getVille());
        String pays = normalize(form.getPays());
        
        if ("".equals(nom) || "".equals(prenom)){
            throw new DonneesInsuffisantesException(
                    "Erreur lors de la recherche du client/fournisseur : le nom ET le prenom doivent être renseignés.");
        }
        
        HQLRecherche query = new HQLRecherche("LOREntities.Personne pers");
               
        if (!"".equals(nom))
            query.addCondition("pers.nom", nom, HQLRecherche.Operateur.LIKE);
        if (!"".equals(prenom))
            query.addCondition("pers.prenom", prenom, HQLRecherche.Operateur.LIKE);
        if (!"".equals(pays))
        {
            query.addCondition("pers.ville.pays.nomPays", pays, HQLRecherche.Operateur.EGAL);
            if (!"".equals(ville))
                query.addCondition("pers.ville.nomVille", ville, HQLRecherche.Operateur.EGAL);
        }
     
        List resultats = modele.createQuery(query.toString()).list();
        modele.flush();

        //traduction des entités hibernate (Personne) en beans (PersonneForm)
        for (Object resultBDD : resultats)
        {
            Personne personneBDD = (Personne) resultBDD;
            PersonneForm pf = new PersonneForm();
            
            pf.setPrenom(personneBDD.getPrenom());
            pf.setNom(personneBDD.getNom());
            pf.setSociete(""); //TODO : champ Société dans la BDD
            pf.setAdresse(personneBDD.getAdresse());
            pf.setMail(personneBDD.getMail());
            pf.setTelephone(personneBDD.getTelephone());
            pf.setVille(personneBDD.getVille().getNomVille());
            pf.setCodePostal(personneBDD.getVille().getCp());
            pf.setPays(personneBDD.getVille().getPays().getNomPays());
            
            //factures
            Vector<FactureForm> factures = new Vector<FactureForm>();
            for(Object elementBDD : personneBDD.getFactures())
            {
                Facture factureBDD = (Facture) elementBDD;
                FactureForm ff = new FactureForm();
                
                ff.setActeur(pf);
                //...
                
                factures.add(ff);
            }
            pf.setFactures(factures);
            
            retour.add(pf);
        }
        return retour;
    }
    
    /**
     * Recherche un pays dans la base de données. Si le pays renseigné est trouvé, un objet Pays est renvoyé. Sinon, la méthode renvoie null.
     */
    private Pays chercherPays(String pays) throws DonneesInsuffisantesException
    {
        if (pays == null || "".equals(pays))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche du pays : nom du pays non renseigné.");

        HQLRecherche q = new HQLRecherche("Pays p");
        q.addCondition("p.nomPays", pays, HQLRecherche.Operateur.EGAL);
        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();

        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Pays) resultats.get(0);
    }
    /**
     * Recherche une ville dans la base de données
     * @param nomVille le nom de la ville à rechercher (obligatoire)
     * @param cp le code postal de la ville à rechercher (obligatoire)
     * @param nomPays le nom du pays dans lequel se situe la ville
     * @return L'entité hibernate associée à l'enregistrement correspondant
     */
    private Ville chercherVille(String nomVille, String cp, String nomPays) throws DonneesInsuffisantesException
    {
        if (nomVille == null || "".equals(nomVille)
                || cp == null || "".equals(cp)
                || nomPays == null || "".equals(nomPays))
            throw new DonneesInsuffisantesException("Erreur lors de la recherche de la ville : nom de la ville, code postal ou nom du pays non renseigné.");

        HQLRecherche q = new HQLRecherche("Ville v");
        q.addCondition("v.nomVille", nomVille, HQLRecherche.Operateur.EGAL);
        q.addCondition("v.cp", cp, HQLRecherche.Operateur.EGAL);
        q.addCondition("v.pays.nomPays", nomPays, HQLRecherche.Operateur.EGAL);
        List resultats = modele.createQuery(q.toString()).list();
        modele.flush();

        if (resultats.isEmpty())
            return null;
        else //on suppose qu'il n'y a qu'un seul résultat !
            return (Ville) resultats.get(0);
    }
    
    public Rapport modifier(ProduitForm form) throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementInexistantException
    {
        Rapport rapport = new Rapport();
        
        int id;
        String type = form.getType();
        String codeBarre = codeBarreValide(form.getCodeBarre());
        String edition = normalize(form.getEdition());
        String nom = normalize(form.getNom());
        String editeur = normalize(form.getEditeur());
        float prix = form.getPrix();
        int stock = form.getStock();
        if ("".equals(nom))
            throw new DonneeInvalideException("Erreur : le nom ne peut pas être vide.");
        if (prix <= 0f)
            throw new DonneeInvalideException("Erreur : le prix ne peut pas être négatif ou nul.");
        if (stock < 0)
            throw new DonneeInvalideException("Erreur : le stock ne peut pas être négatif.");
        
        if ("Console".equals(type))
        {
            id = form.getIdVersionConsole();
            if (id <= 0)
                throw new DonneesInsuffisantesException(
                        "Erreur : impossible de modifier console (version) d'identifiant " + id);
            VersionConsole vc = chercherVersionConsole(id);
            if (vc == null)
                throw new EnregistrementInexistantException("Erreur : version de console " + id
                        + " non trouvée");
            //attributs directs : code barre, édition, prix, stock
//            if (vc.getCodeBarre() != codeBarre)
            vc.setCodeBarre(codeBarre);
//            if (!(edition.equals(vc.getEdition())))
            vc.setEdition(edition);
//            if (vc.getPrix() != prix)
            vc.setPrix(prix);
//            if (vc.getStock() != stock)
            vc.setStock(stock);
            //zone
            if (!(form.getZone().equals(vc.getZone().getNomZone())))
            {
                Zone nouvelleZone = chercherZone(form.getZone());
                if (nouvelleZone == null) //si la zone n'existe pas, on ne la crée pas.
                    throw new EnregistrementInexistantException("Erreur : la zone "
                            + form.getZone() + " n'existe pas.");
                //affectation de la zone à la version de console
                vc.setZone(nouvelleZone);
            }
            //console
            if (!(nom.equals(vc.getConsole().getNomConsole())
                    && editeur.equals(vc.getConsole().getFabricant().getNomFabricant())))
            {
                Console nouvelleConsole = chercherConsole(nom, editeur);
                if (nouvelleConsole != null) //si la console existe, on remplace
                    vc.setConsole(nouvelleConsole);
                else //sinon, on change le nom/l'éditeur de l'ancienne console
                {
                    if (!(nom.equals(vc.getConsole().getNomConsole())))
                        vc.getConsole().setNomConsole(nom);
                    //fabricant
                    if (!(editeur.equals(vc.getConsole().getFabricant().getNomFabricant())))
                    {
                        Fabricant nouveauFabricant = chercherFabricant(editeur);
                        if (nouveauFabricant != null) //si le fabricant existe, on remplace
                            vc.getConsole().setFabricant(nouveauFabricant);
                        else //sinon, on change le nom du fabricant
                            vc.getConsole().getFabricant().setNomFabricant(editeur);
                    }
                }
            }
            
            //sauvegarde de la version de console
            modele.beginTransaction();
            modele.save(vc);
            modele.getTransaction().commit();
            modele.flush();
            
            rapport.addOperation(vc.getIdVersionConsole(), Rapport.Table.VERSIONCONSOLE, Rapport.Operation.MODIFIER);
        }
        else if ("Jeu".equals(type))
        {
            id = form.getIdVersionJeu();
            if (id <= 0)
                throw new DonneesInsuffisantesException(
                        "Erreur : impossible de modifier jeu (version) d'identifiant " + id);
            Vector<String> tags = stringToVector(normalize(form.getTags()) ,',');
            String descr = form.getDescription();
            
            VersionJeu vj = chercherVersionJeu(id);
            if (vj == null)
                throw new EnregistrementInexistantException("Erreur : version de jeu " + id
                        + " non trouvée");
            
            //attributs directs : code barre, édition, prix, stock
//            if (vj.getCodeBarre() != codeBarre)
            vj.setCodeBarre(codeBarre);
//            if (!(edition.equals(vj.getEdition())))
            vj.setEdition(edition);
//            if (vj.getPrix() != prix)
            vj.setPrix(prix);
//            if (vj.getStock() != stock)
            vj.setStock(stock);
            //zone
            if (!(form.getZone().equals(vj.getZone().getNomZone())))
            {
                Zone nouvelleZone = chercherZone(form.getZone());
                if (nouvelleZone == null) //si la zone n'existe pas, on ne la crée pas.
                    throw new EnregistrementInexistantException("Erreur : la zone "
                            + form.getZone() + " n'existe pas.");
                //affectation de la zone à la version de console
                vj.setZone(nouvelleZone);
            }
            //plateforme
            if (!(form.getPlateforme().equals(vj.getConsole().getNomConsole())))
            {
                Console nouvellePlateforme = chercherConsole(form.getPlateforme(), "");
                if (nouvellePlateforme == null) //si la console n'existe pas, on ne la crée pas.
                    throw new EnregistrementInexistantException("Erreur : la console "
                            + form.getPlateforme() + " n'existe pas.");
                //affectation de la plateforme à la version de jeu
                vj.setConsole(chercherConsole(form.getPlateforme(), ""));
            }
            //jeu
            Jeu jeu = vj.getJeu();
            if (!(nom.equals(jeu.getNomJeu())
                    && editeur.equals(jeu.getEditeur().getNomEditeur())))
            {
                //remarque : les tags ne sont pas un critère de recherche
                Jeu nouveauJeu = chercherJeu(nom, new Vector<String>(), editeur);
                if (nouveauJeu != null) //si le jeu existe, on remplace
                    jeu = nouveauJeu;
                else //sinon, on change le nom/l'éditeur de l'ancien jeu
                {
                    if (!(nom.equals(jeu.getNomJeu())))
                        jeu.setNomJeu(nom);
                    //éditeur
                    if (!(editeur.equals(jeu.getEditeur().getNomEditeur())))
                    {
                        Editeur nouvelEditeur = chercherEditeur(editeur);
                        if (nouvelEditeur != null) //si l'éditeur existe, on remplace
                            jeu.setEditeur(nouvelEditeur);
                        else //sinon, on change le nom de l'éditeur
                            jeu.getEditeur().setNomEditeur(editeur);
                    }
                }
            }
            //description du jeu
//            if (!(jeu.getDescriptionJeu().equals(descr)))
            jeu.setDescriptionJeu(descr);
            
            //tags
            //1° supprimer les decrire qui sont dans la BDD mais pas dans le form (variable tags);
            //pour chaque tag décrivant le jeu dans la base de données...
            Vector<Decrire> liensASupprimer = new Vector<Decrire>();
            for (Object dec : jeu.getDecrires())
            {
                Tag tagBDD = (Tag) modele.load(Tag.class, ((Decrire) dec).getId().getIdTag());
                //...on vérifie si il est dans les tags décrits par le formulaire
                boolean present = false;
                Iterator<String> iteratorTagsForm = tags.iterator();
                while (!present && iteratorTagsForm.hasNext())
                    present = iteratorTagsForm.next().equals(tagBDD.getLabelTag());
                
                //et si le tag n'est pas présent dans le formulaire, on supprime la relation entre le tag et le jeu.
                if (!present)
                    liensASupprimer.add((Decrire) dec);
            }
            for (Object dec : liensASupprimer)
            {
                Tag tagBDD = (Tag) modele.load(Tag.class, ((Decrire) dec).getId().getIdTag());
                //mise à jour du tag
                tagBDD.getDecrires().remove(dec);
                modele.beginTransaction();
                modele.saveOrUpdate(tagBDD);
                modele.getTransaction().commit();
                modele.flush();
                //mise à jour du jeu
                jeu.getDecrires().remove(dec);
                modele.beginTransaction();
                modele.saveOrUpdate(jeu);
                modele.getTransaction().commit();
                modele.flush();
                //destruction de l'enregistrement Decrire
                modele.beginTransaction();
                modele.delete(dec);
                modele.getTransaction().commit();
                modele.flush();
                //si le tag ne décrit plus rien, on le supprime
                if (tagBDD.getDecrires().isEmpty())
                {
                    modele.beginTransaction();
                    modele.delete(tagBDD);
                    modele.getTransaction().commit();
                    modele.flush();
                }
            }
            //2° créer/chercher et lier les tags qui sont dans le formulaire mais pas dans la table Decrire;
            //pour chaque tag dans le formulaire...
            for (String tagForm : tags)
            {
                //on vérifie s'il n'est pas lié au jeu dans la base de données
                Iterator<Object> iterateurDecriresBDD = jeu.getDecrires().iterator();
                boolean present = false;
                while (!present && iterateurDecriresBDD.hasNext())
                    present = tagForm.equals(((Tag) modele.load(
                            Tag.class, ((Decrire) iterateurDecriresBDD.next()).getId().getIdTag())).getLabelTag());
                //si la relation entre le tag et le jeu n'existe pas dans la base de données
                if (!present)
                {
                    //on recherche le tag ou on le crée
                    Tag tagBDD;
                    try {
                        tagBDD = creerTag(rapport, tagForm);}
                    catch (EnregistrementExistantException ex) {
                        tagBDD = chercherTag(tagForm);}
                
                //on lie le jeu au tag dans la table DECRIRE
                Decrire d = new Decrire();
                d.setId(new DecrireId());
                d.getId().setIdTag(tagBDD.getIdTag());
                d.getId().setIdJeu(jeu.getIdJeu());
                
                //mise à jour jeu
                modele.beginTransaction();
                modele.saveOrUpdate(jeu);
                modele.getTransaction().commit();
                modele.flush();
                //mise à jour tag
                modele.beginTransaction();
                modele.saveOrUpdate(tagBDD);
                modele.getTransaction().commit();
                modele.flush();
                //création de l'enregistrement "décrire"
                modele.beginTransaction();
                modele.save(d);
                modele.getTransaction().commit();
                modele.flush();

                rapport.addOperation(tagBDD.getIdTag(), Rapport.Table.DESCRIPTION, Rapport.Operation.CREER);
                }
            }
            
            //sauvegarde des modifications sur le jeu
            modele.beginTransaction();
            modele.saveOrUpdate(jeu);
            modele.getTransaction().commit();
            modele.flush();
            
            //sauvegarde de la version de jeu
            vj.setJeu(jeu);
            modele.beginTransaction();
            modele.save(vj);
            modele.getTransaction().commit();
            modele.flush();
            
            rapport.addOperation(vj.getIdVersionJeu(), Rapport.Table.VERSIONJEU, Rapport.Operation.MODIFIER);
        }
        return rapport;
    }

    public Rapport modifierPromo(PromoForm form) throws DonneesInsuffisantesException, DonneeInvalideException, EnregistrementInexistantException
    {
        Rapport rapport = new Rapport();
        
        int id;
        int idPromo;
        String type = form.getType();

        float prix = form.getPrix();

        if (prix <= 0f)
            throw new DonneeInvalideException("Erreur : le prix ne peut pas être négatif ou nul.");
        
        if ("Console".equals(type))
        {
            idPromo = form.getIdPromo();
            id = form.getIdVersionConsole();
            if (id <= 0)
                throw new DonneesInsuffisantesException(
                        "Erreur : impossible de modifier console (version) d'identifiant " + id);
            // Chercher s'il existe un promo sur Console
            PromoConsole pc = chercherPromoConsole(idPromo);
            // Si non
            if (pc==null)
            {
                VersionConsole vc = chercherVersionConsole(id);
                if (vc == null)
                    throw new EnregistrementInexistantException("Erreur : version de console " + id
                                                                + " non trouvée");
                PromoConsole newpc = new PromoConsole();
                newpc.setVersionConsole(vc);
                newpc.setPrixPromoConsole(prix);
                newpc.setCoteConsole(calculCoteAPartirPrix(type,id,prix));
                //sauvegarde de la version de console
                modele.beginTransaction();
                modele.saveOrUpdate(newpc);
                modele.getTransaction().commit();
                modele.flush();
            }
            else // Si promo existe
            {
                if (pc.getPrixPromoConsole() != prix)
                    pc.setPrixPromoConsole(prix);
                pc.setIdPromoConsole(idPromo);
                pc.setCoteConsole(calculCoteAPartirPrix(type,id,prix));
                //sauvegarde de la version de console
                modele.beginTransaction();
                modele.saveOrUpdate(pc);
                modele.getTransaction().commit();
                modele.flush(); 
            }
            rapport.addOperation(pc.getIdPromoConsole(), Rapport.Table.PROMOCONSOLE, Rapport.Operation.MODIFIER);
        }
        else if ("Jeu".equals(type))
        {
            idPromo = form.getIdPromo();
            id = form.getIdVersionJeu();
            if (id <= 0)
                throw new DonneesInsuffisantesException(
                        "Erreur : impossible de modifier jeu (version) d'identifiant " + id);
            
            PromoJeu pj = chercherPromoJeu(idPromo);
            if (pj == null)
            {
                VersionJeu vj = chercherVersionJeu(id);
                if (vj == null)
                    throw new EnregistrementInexistantException("Erreur : version de jeu " + id
                                                                + " non trouvée");
                PromoJeu newpj = new PromoJeu();
                newpj.setVersionJeu(vj);
                newpj.setPrixPromoJeu(prix);
                newpj.setCoteJeu(calculCoteAPartirPrix(type,id,prix));
                //sauvegarde de la version de console
                modele.beginTransaction();
                modele.saveOrUpdate(newpj);
                modele.getTransaction().commit();
                modele.flush();
            }
            else
            {
                // Chercher a changer le prix
                if (pj.getPrixPromoJeu() != prix)
                    pj.setPrixPromoJeu(prix);
                pj.setIdPromoJeu(idPromo);
                pj.setCoteJeu(calculCoteAPartirPrix(type,id,prix));
                //sauvegarde de la version de jeu
                modele.beginTransaction();
                modele.saveOrUpdate(pj);
                modele.getTransaction().commit();
                modele.flush();
            }
            rapport.addOperation(pj.getIdPromoJeu(), Rapport.Table.PROMOJEU, Rapport.Operation.MODIFIER);
        }
        return rapport;       
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
        modele.beginTransaction();
        modele.save(z);
        modele.getTransaction().commit();
        modele.flush();

        rapport.addOperation(z.getIdZone(), Rapport.Table.ZONE, Rapport.Operation.CREER);

        return rapport;
    }
    
    /**
     * Renvoie la liste des Editions.
     * @param : type Console / Jeu
     * @return : un vecteur/liste de resultat de type Edition
     */
    public Vector<String> listeEdition(String type)
    {
        Vector<String> ret = new Vector();
        List editions = new ArrayList();
        if (type.equals("Console"))
            { editions = modele.createQuery("select vc.edition from LOREntities.VersionConsole vc").list(); }
        else if (type.equals("Jeu"))
            { editions = modele.createQuery("select vj.edition from LOREntities.VersionJeu vj").list(); }
        for (Object e : editions)
            if (e!=null) ret.add((String) e);
        modele.flush();
            
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
            if (f!=null) ret.add(((Fabricant) f).getNomFabricant());
        modele.flush();

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
            if (z!=null) ret.add(((Zone) z).getNomZone());
        modele.flush();

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
            if (c!=null) ret.add(((Console) c).getNomConsole());
        modele.flush();

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
        modele.flush();
        
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
        modele.flush();
        
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
        modele.flush();
        
        return ret;
    }

    /**
     * Enregistre la facture fournie sous forme de fichier texte.
     * @param facture
     * @throws IOException 
     */
    private void exporter(Facture facture) throws IOException
    {
        String adresseFichier = "facture_numero_" + facture.getIdFacture() + ".fac";
        File fichier = new File(adresseFichier);
        if (fichier.exists())
            throw new IOException("Erreur lors de l'export de la facture : le fichier "
                    + adresseFichier + " existe déjà.");
        FileWriter fw = new FileWriter(fichier);
        
        String ligneCourante;
        
        //ligne du haut
        for (int i = 0 ; i < largeurFacture ; i++)
            fw.write('-');
        fw.write("\n");
        //En-ête : informations facture
        ligneCourante = "Facture ";
        if (facture.getTypeFacture() == 'a')
            ligneCourante = ligneCourante.concat("d'achat ");
        else if (facture.getTypeFacture() == 'v')
            ligneCourante = ligneCourante.concat("de vente ");
        ligneCourante = ligneCourante.concat("numéro " + facture.getIdFacture());
        //écriture
        for (String sl : formater(ligneCourante, "", largeurFacture, 0, ' '))
            fw.write(sl);
        //information entreprise
        for (String sl : formater("Entreprise : " + nomEntreprise, "", largeurFacture, 0, ' '))
            fw.write(sl);
        //TODO: informations client/fourn
        if (facture.getTypeFacture() == 'a')
            ligneCourante = "Fournisseur : ";
        else if (facture.getTypeFacture() == 'v')
            ligneCourante = "Client : ";
        ligneCourante  =ligneCourante.concat("#TODO");
        
        
        //ligne du milieu haut
        fw.write('|');
        for (int i = 1 ; i < largeurFacture - 1 ; i++)
            fw.write('-');
        fw.write("|\n");
        //lignes de la facture : d'abord consoles, puis jeux
        for (Object o : facture.getLigneFactureConsoles())
        {
            int quantite = ((LigneFactureConsole) o).getQuantite();
            VersionConsole vc = (VersionConsole) modele.load(VersionConsole.class,
                    ((LigneFactureConsole) o).getId().getIdVersionConsole());
            for (String sl : formater(
                    vc.getConsole().getNomConsole() + " x" + quantite,
                    vc.getPrix() * quantite + "€",
                    largeurFacture, 6, '.'))
                fw.write(sl);
        }
        for (Object o : facture.getLigneFactureJeus())
        {
            int quantite = ((LigneFactureJeu) o).getQuantite();
            VersionJeu vj = (VersionJeu) modele.load(VersionJeu.class,
                    ((LigneFactureJeu) o).getId().getIdVersionJeu());
            for (String sl : formater(vj.getJeu().getNomJeu() + " x" + quantite,
                    vj.getPrix() * quantite + "€",
                    largeurFacture, 6, '.'))
                fw.write(sl);
        }
        
        //ligne du milieu bas
        fw.write('|');
        for (int i = 1 ; i < largeurFacture - 1 ; i++)
            fw.write('-');
        fw.write("|\n");
        //réduction
        for (String sl : formater("REDUCTIONS", "-" + ((Float) facture.getReduction()).toString() + "€",
                largeurFacture, 0, '.'))
            fw.write(sl);
        //ligne du total
        for (String sl : formater("TOTAL TTC", ((Float) facture.getPrixTtc()).toString() + "€",
                largeurFacture, 0, '.'))
            fw.write(sl);
        
        //ligne du bas (fin)
        for (int i = 0 ; i < largeurFacture ; i++)
            fw.write('-');
        fw.write("\n");
        
        fw.close();
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
     * Renvoie la quantite de vente d'un produit
     * @param : type de produit, ID du produit
     * @return : la quantité du produit demandé en Integer
     */
    private int getSellQuantityProduct(String typeProduit, Integer idProduit)
    {
        int nombreAchat = 0;
        //int resul;
        Query resul = null;
        if ("CONSOLE".equals(normalize(typeProduit))) 
        {
            resul = modele.createQuery("select sum(quantite) from LOREntities.LigneFactureConsole lfc "
                    + "where "
                    + "(lfc.versionConsole IN (select vc.idVersionConsole from LOREntities.VersionConsole vc  where vc.idVersionConsole="+idProduit+")"
                            + " AND lfc.facture IN ( select f.idFacture from LOREntities.Facture f where f.typeFacture='v')"
                            + " )");
        }
        else if ("JEU".equals(normalize(typeProduit))) 
        {
            resul = modele.createQuery("select sum(quantite) from LOREntities.LigneFactureJeu lfj "
                    + "where "
                    + "(lfj.versionJeu IN (select vj.idVersionJeu from LOREntities.VersionJeu vj  where vj.idVersionJeu="+idProduit+")"
                            + " AND lfj.facture IN ( select f.idFacture from LOREntities.Facture f where f.typeFacture='v')"
                            + " )");
        } 
        modele.flush();
        nombreAchat = Integer.valueOf(resul.uniqueResult().toString());
        return nombreAchat;
    }
    /**
     * Renvoie la quantite de stock d'un produit
     * @param : type de produit, ID du produit
     * @return : la quantité du produit demandé en Integer
     */
    private int getStockProduct(String typeProduit, Integer idProduit) throws DonneeInvalideException
    {
        int stock = 0;
        if ("CONSOLE".equals(normalize(typeProduit))) 
        {
            VersionConsole vc = new VersionConsole();
            vc = chercherVersionConsole(idProduit);
            stock = vc.getStock();
        }
        else if ("JEU".equals(normalize(typeProduit))) 
        {
            VersionJeu vj = new VersionJeu();
            vj = chercherVersionJeu(idProduit);
            stock = vj.getStock();
        }  
        modele.flush();
        return stock;
    }
    /**
     * Renvoie le prix de vente d'un produit
     * @param : type de produit, ID du produit
     * @return : le prix du produit demandé en réel
     */
    private float getSellPrixProduct(String typeProduit, Integer idProduit)
    {
        float prixProduit = 0.0f;
        //int resul;
        List resul = null;
        if ("CONSOLE".equals(normalize(typeProduit))) 
        {
            HQLRecherche q = new HQLRecherche("VersionConsole vc");
            q.setSelect("vc.prix");
            q.addCondition("vc.idVersionConsole", idProduit, HQLRecherche.Operateur.EGAL);
            List resultats = modele.createQuery(q.toString()).list();
        
            if (resultats.isEmpty())
                prixProduit = 0;
            else //on suppose qu'il n'y a qu'un seul résultat !
                prixProduit = Float.valueOf(resultats.get(0).toString());          
        }
        else if ("JEU".equals(normalize(typeProduit))) 
        {
            HQLRecherche q = new HQLRecherche("VersionJeu vj");
            q.setSelect("vj.prix");
            q.addCondition("vj.idVersionJeu", idProduit, HQLRecherche.Operateur.EGAL);
            List resultats = modele.createQuery(q.toString()).list();
        
            if (resultats.isEmpty())
                prixProduit = 0;
            else //on suppose qu'il n'y a qu'un seul résultat !
                prixProduit = Float.valueOf(resultats.get(0).toString());  
        } 
        modele.flush();
        return prixProduit;
    }
    /**
     * Renvoie la frequence de vendre d'un produit
     * 
     * Par défault, j'ai pris le choix de prendre en compte que les ventes sur un an
     * 
     * @param : type de produit, ID du produit
     * @return : la fréquence de ventre du produit demandé en Integer
     */
    private float getFrequentSellProduct(String typeProduit, int idProduit)
    {
        
        float frequenceDeVente = 0.0f;
        Integer yearDebutPeriod = 0;

        Integer yearActuel = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
         
        String HQL_QUERY = "select sum(lfc.quantite) from LigneFactureConsole lfc  "
                         + "where (lfc.versionConsole IN (select vc.idVersionConsole from VersionConsole vc  where vc.idVersionConsole=2) "
                           + "AND lfc.facture IN ( select f.idFacture from Facture f where f.typeFacture LIKE '%v%' "
                                                + "AND year(f.dateFacture) BETWEEN :yearDebutPeriod AND :yearActuel ))";                       

        Query query = modele.createQuery(HQL_QUERY)
                      .setParameter("yearDebutPeriod", yearDebutPeriod)
                      .setParameter("yearActuel",yearActuel);
        int nombreVente = Integer.valueOf(query.uniqueResult().toString());
        modele.flush();
        
        return frequenceDeVente = nombreVente/12;
    }
    /**
     * Renvoie la cote d'un produit
     * 
     * @param : type de produit, ID du produit
     * @return : le cote du produit demandé en Float
     */
    public float getCoteProduct(String typeProduit, int idProduit)
    {
        float coteProduit = 0.0f;
        if ("CONSOLE".equals(normalize(typeProduit))) 
        {
            String HQL_QUERY = "select pc.coteConsole from LOREntities.PromoConsole pc where pc.versionConsole IN (select vc.idVersionConsole from VersionConsole vc where vc.idVersionConsole= :idProduit)";
            Query query = modele.createQuery(HQL_QUERY).setParameter("idProduit", idProduit);
            if (query.list().isEmpty()) { coteProduit = 0f; } else { coteProduit = Float.valueOf(query.list().get(query.list().size()-1).toString()); }
            modele.flush();
        }
        else if ("JEU".equals(normalize(typeProduit))) 
        {
        modele.flush();
            String HQL_QUERY = "select pj.coteJeu from LOREntities.PromoJeu pj where pj.versionJeu IN (select vj.idVersionJeu from VersionJeu vj where vj.idVersionJeu= :idProduit)";
            Query query = modele.createQuery(HQL_QUERY).setParameter("idProduit", idProduit);
            if (query.list().isEmpty()) { coteProduit = 0f; } else { coteProduit = Float.valueOf(query.list().get(query.list().size()-1).toString()); }
            modele.flush();
         }
        return coteProduit;
    }    
    /**
     * Brancher la photo des jeux
     * @param : String - url de la photo du jeu
     * @return : void
     */
    public void setPhotoProduct(String urlPhotoJeu) throws IOException
    {
        try{
            URL url = new URL(urlPhotoJeu);
            BufferedImage img = ImageIO.read(url);
            Image newimg = img.getScaledInstance(242, 128,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            ImageIcon icon = new ImageIcon(newimg);
            labelPhoto.setIcon(icon);
        }catch(MalformedURLException ex){
            labelPhoto.setText("Cant get photo !!!");
        }
    }
    
    /**
     * Divise une ligne en un tableau de sous-lignes de longueur adéquate pour mise en forme.
     */
    protected Vector<String> formater(String ligne, String finLigne, int longueurMax,
            int tailleBuffer, char caractereEntreLigneEtFinLigne)
    {
        if (tailleBuffer >= longueurMax - 2 - 5) //je pose ici que la ligne doit faire au moins 5 caractères de long !
            throw new IllegalArgumentException("Erreur : le buffer de la facture est trop grand.");
        if (longueurMax <= 7) //je pose ici que la ligne doit faire au moins 5 caractères de long !
            throw new IllegalArgumentException("Erreur : la longueur de la ligne de facture est trop petite.");
        
        Vector<String> retour = new Vector<String>();
        
        longueurMax -= 2; //prend en compte le '|' au début et à la fin de la ligne
        String caracEntreLigneEtFinLigne = ((Character) caractereEntreLigneEtFinLigne).toString();
        int indexCoupure; //dernier espace de la sous-ligne, pour coupure (retour à la ligne)
        
        //Si la ligne est trop longue, on la découpe en sous-lignes, au niveau des espaces (si possible).
        while (ligne.length() + finLigne.length() > longueurMax)
        {
            //recherche du point de coupure
            //par défaut, on prend la sous-chaîne la plus longue possible. On cherche la position du dernier espace.
            indexCoupure = ligne.substring(0, longueurMax - tailleBuffer).lastIndexOf(" ");
            if (indexCoupure == -1) //si la ligne ne contient aucun espace avant la position maximale
                indexCoupure = longueurMax - tailleBuffer; //on repositionne le curseur à la fin
            
            //génération du buffer pour la sous-ligne
            String buffer = "";
            for (int i = indexCoupure ; i < longueurMax ; i++)
                buffer = buffer.concat(" ");
            
            //ajout de la sous-ligne
            retour.add("|"
                    .concat(ligne.substring(0, indexCoupure))
                    .concat(buffer)
                    .concat("|\n"));

            //on enlève la sous-ligne de la ligne
            ligne = ligne.substring(indexCoupure);
        }

        //Ensuite, on s'occupe de la dernière ligne.
        String buffer = "";
        for (int i = ligne.length() + finLigne.length() ; i < longueurMax ; i++)
            buffer = buffer.concat(caracEntreLigneEtFinLigne);
        retour.add("|"
                .concat(ligne)
                .concat(buffer)
                .concat(finLigne)
                .concat("|\n"));
        
        return retour;
    }

    /**
     * Calcule le total d'une facture (sous forme de formulaire) et applique la TVA
     * @param form la facture dont il faut calculer le total
     * @return le total TTC de la facture
     */
    public float calculTotalTTC(FactureForm form)
    {
        float retour = 0f; //calcul du total des lignes
        for (FactureLigneForm flf : form.getLignes())
            retour += flf.getPrixLigne();
        retour -= form.getReductions(); //application de la réduction globale
        retour*= Controleur.TVA; //application de la TVA
        return retour;
    }
    /**
     * Calcule et mis a jour dans la BDD le cote d'un produit pour Promotion
     * @param type de produit, ID du produit
     */
    public void calculCote(String typeProduit, Integer idProduit) throws EnregistrementInexistantException, DonneeInvalideException
    {
        float cote, prixPromo; 
        

        //on vérifie que le jeu existe déjà !
        if ((chercherVersionJeu(idProduit) == null) && (chercherVersionConsole(idProduit) == null))
            throw new EnregistrementInexistantException("Le produit recherché n'existe pas !!!");
        else
        {
            // Recuperer la fréquence de vente d'un produit sur une période donnée
            Float frequentDeVente = getFrequentSellProduct(typeProduit, idProduit);
            // Recuperer le nombre de vente
            int nbreVente = getSellQuantityProduct(typeProduit, idProduit);
            // Recuperer le stock actuel
            int stockActuel = getStockProduct(typeProduit, idProduit);
            //Calculer cote
            cote = (float) Math.round(((frequentDeVente/stockActuel)*100) + Float.valueOf(nbreVente/10)) / 100;
            prixPromo = (float) Math.round(( getSellPrixProduct(typeProduit, idProduit) * cote * 100 )) / 100;
        }
        // Enregistrement le calcul de cote dans la BDD
        if ("CONSOLE".equals(normalize(typeProduit)))
        {
            //Remplissage des cote et prix Promo
            PromoConsole pc = new PromoConsole();
            pc.setVersionConsole(chercherVersionConsole(idProduit));
            pc.setCoteConsole(cote);
            pc.setPrixPromoConsole(prixPromo);

            //création de l'enregistrement dans la table PromoConsole
            modele.beginTransaction();
            modele.save(pc);
            modele.getTransaction().commit();
            modele.flush();
        }
        else if ("JEU".equals(normalize(typeProduit)))
        {
            //Remplissage des cote et prix Promo
            PromoJeu pj = new PromoJeu();
            pj.setVersionJeu(chercherVersionJeu(idProduit));
            pj.setCoteJeu(cote);
            pj.setPrixPromoJeu(prixPromo);

            //création de l'enregistrement dans la table PromoJeu
            modele.beginTransaction();
            modele.save(pj);
            modele.getTransaction().commit();
            modele.flush();
        }
        //return cote;
    }
    /**
     * Calcule et mis a jour dans la BDD le cote d'un produit pour Promotion à partir d'un prix donné
     * @param type de produit
     * @param id de produit
     * @param prix en promotion donné de produit
     * @return cote en float
     */
    public float calculCoteAPartirPrix(String typeProduit, Integer idProduit, Float prixPromo) throws EnregistrementInexistantException, DonneeInvalideException
    {
        
        float cote = (float) Math.round(prixPromo/getSellPrixProduct(typeProduit, idProduit))*100 / 100;
        return cote;
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
            vect.add(((Tag) modele.load(Tag.class, d.getId().getIdTag()))
                    .getLabelTag());
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
            //magical - do not touch
              /*@SuppressWarnings("unused")
                org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
                java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF); //or whatever level you need*/

        try {
            //initialisation de la session hibernate
            Controleur.modele = (HibernateUtil.getSessionFactory()).openSession();
            //lancement du programme
            new Controleur();}
        
        catch (ExceptionInInitializerError eiie)    {System.out.println("Erreur lors de l'initialisation du modèle.\n"
                + eiie.getMessage());}
    }
}
