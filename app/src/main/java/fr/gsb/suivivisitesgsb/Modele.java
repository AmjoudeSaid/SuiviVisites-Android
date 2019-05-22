package fr.gsb.suivivisitesgsb;

import android.os.Environment;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.io.File;
import java.util.ArrayList;


public class Modele {

    private String db4oFileName;
    private ObjectContainer dataBase;
    private File appDir;
    private static Modele db4oModel = null;

    /**
     * Constructeur de l'objet.
     * Design pattern Singleton
     * La présence d'un constructeur privé supprime le constructeur public par défaut.
     */
    private Modele() {
        createDirectory();
        deleteVisites();
        //chargeJeuEssai();  // Si partie identification et import non développée
    };

    /**
     * Méthode permettant de renvoyer une instance de la classe Modele
     * @return Retourne l'instance du singleton.
     */
    public final static Modele getModele(){
        // Le mot-clé synchronized sur ce bloc empêche toute instanciation multiple même par différents "threads".
        if (Modele.db4oModel == null) {
            synchronized (Modele.class) {
                if (Modele.db4oModel == null) {
                    Modele.db4oModel = new Modele();
                }
            }
        }
        return Modele.db4oModel;
    }

    public void createDirectory() {
        // Vérifier si le media est bien monté
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            appDir = new File(Environment.getExternalStorageDirectory() + "/baseDB4o");
            // Media accessible en lecture et écriture
            if (!appDir.exists() && !appDir.isDirectory()) {
                appDir.mkdir();
            }
        }
    }

    public void open() {
        db4oFileName = Environment.getExternalStorageDirectory() + "/baseDB4o" + "/BaseSuiviVisites.db4o";
        dataBase = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), db4oFileName);
    }

    public void close() {
        dataBase.close();
    }


    // Retourne tous les objets de la classe Visite
    public ArrayList<Visites> listeVisites() {
        open();
        ArrayList<Visites> visites = new ArrayList<Visites>();
        ObjectSet<Visites> result = dataBase.queryByExample(Visites.class);

        while (result.hasNext())
            visites.add(result.next());

        close();
        Log.i("Visitesallo", "" + visites);
        return visites;
    }

    // Retourne une instance de Visite à partir de son identifiant
    public Visites trouveVisite(String id) {
        open();
        Visites laVisite = new Visites();
        laVisite.setId(id);
        ObjectSet<Visites> result = dataBase.queryByExample(laVisite);
        laVisite = (Visites) result.next();
        close();
        return laVisite;
    }

    // Ajoute l'objet Visite passé en paramètre s'il n'existe pas déjà une visite avec le même identifiant
    public void saveVisite(Visites v) {
        open();
        Visites uneVisite = new Visites();
        uneVisite.setId(v.getId());
        ObjectSet<Visites> result = dataBase.queryByExample(uneVisite);

        if (result.size() == 0) {
            dataBase.store(v);
        } else {
            // Mise à jour des données de la visite déjà présente dans la base
            uneVisite = (Visites) result.next();
            uneVisite.copieVisite(v);
            dataBase.store(uneVisite);	// Car le pointeur est positionné sur uneVisite
        }
        close();
    }


    // Pour la gestion de l'import et l'export
    // Supprime toutes les instances de la classe Visite
    public void deleteVisites() {
        open();
        ObjectSet<Visites> result = dataBase.queryByExample(Visites.class);
        while (result.hasNext()) {
            dataBase.delete(result.next());
        }
        close();
    }

    //Ajoute les éléments de la collection à la base DB4o
    public void addVisites(ArrayList<Visites> lesVisites) {
        open();
        for (Visites v : lesVisites) {
            dataBase.store(v);
        }
        close();
    }


    // Constitution d'un jeu d'essai dans la base de données locale
    public void chargeJeuEssai() {
        open();
        ObjectSet<Visites> result = dataBase.queryByExample(Visites.class);
        if (result.size() == 0) {
            dataBase.store(new Visites("1001", "Riera", "Alain", "14 Boulevard Maglioli 20000 Ajaccio", "0495238757"));
            dataBase.store(new Visites("1002", "Eiden", "Pierre", "14 Rue Docteur Del Pellegrino 20090 Ajaccio", "0495208585"));
            dataBase.store(new Visites("1003", "Ferrandi", "Frédéric", "20 Cours Napoléon 20090 Ajaccio", "0495213371"));
            dataBase.store(new Visites("1004", "Ferrara", "Jean-Jacques", "2 Cours Grandval 20000 Ajaccio", "0495212447"));
            dataBase.store(new Visites("1005", "Flori", "Alexandre", "Résidence Acqualonga 20167 Ajaccio", "0495100808"));
            dataBase.store(new Visites("1006", "Franceschini", "Antoine", "14 Rue Docteur Del Pellegrino 20090 Ajaccio", "0495208585"));
            dataBase.store(new Visites("1007", "Franchini", "Marc", "19 Cours Général Leclerc 20000 Ajaccio", "0495103695"));
            dataBase.store(new Visites("1008", "Le Breton", "Geneviève", "28 Boulevard Pascal Rossini 20000 Ajaccio", "0495200220"));
            dataBase.commit();
        }
        close();
    }

}