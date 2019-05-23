package fr.gsb.suivivisitesgsb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Export extends AppCompatActivity {

    private Button btnExporter, btnRetourExport;
    private AsyncTask<String, String, Boolean> connexionAsynchrone;
    private String identifiant, mdp;
    private SharedPreferences jetonAuth;

    // Chaine qui contiendra l'ensemble des objets de type visite au format JSON
    private String chaineJSONVisites = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        // Récupère les identifiants du visiteur depuis les Shared Preferences
        jetonAuth = getSharedPreferences("AppData", Context.MODE_PRIVATE);
        identifiant = jetonAuth.getString("login", null);
        mdp = jetonAuth.getString("mdp", null);

        // Récupère les boutons depuis le layout
        btnExporter = (Button) findViewById(R.id.bt_Export);
        btnRetourExport = (Button) findViewById(R.id.bt_Retour);

        // Récupère la liste des visites depuis DB4o (en local)
        Modele modele = Modele.getModele();
        ArrayList<Visites> listeVisites = modele.listeVisites();

        // Création d'un objet GSON personnalisé (GsonBuilder()) yyyy-MM-dd hh:mm:ss.S
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        // Sélection des visites faites dans un ArrayList
        ArrayList<Visites> listeVisitesFaites = new ArrayList<Visites>();
        for (Visites v : listeVisites) {
            if(v.getPresent())
                listeVisitesFaites.add(v);
        }
        Log.i("bonjour", "" + listeVisitesFaites);

        // Sérialisation de la liste des Visites au format JSON (en chaine de caractères)
        chaineJSONVisites = gson.toJson(listeVisitesFaites);

        // Gestion de l'évènement sur le clic du bouton Retour
        btnRetourExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        // Gestion de l'évènement sur le clic du bouton Exporter
        btnExporter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String[] mesParams = {identifiant, mdp, "http://gsb.dynu.net:8081/suivivisitesgsb/export.php", chaineJSONVisites};
                connexionAsynchrone = new Connexion(Export.this).execute(mesParams); // --> Serveur
            }
        });
    }

    // --> Retour
    // Méthode exécutée après la fin de l'AsynTask
    public void retourExport(StringBuilder codeRetour) {
        // Si l'exportation s'est bien déroulée toutes les visites sont supprimées la base locale DB4o
        if (codeRetour.toString().compareTo("OK") == 0) {
            Modele modele = Modele.getModele();
            modele.deleteVisites();
            Toast.makeText(this, "Exportation des données effectuée", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Echec de l'exportation des données", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }
}
