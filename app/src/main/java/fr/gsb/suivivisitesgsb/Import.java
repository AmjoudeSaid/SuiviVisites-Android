package fr.gsb.suivivisitesgsb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class Import extends AppCompatActivity {

    private Button btnImport, btnRetour;
    private String identifiant, mdp;
    private AsyncTask<String, String, Boolean> connexionAsynchrone;
    private SharedPreferences jetonAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        btnImport = (Button) findViewById(R.id.bt_Import);
        btnRetour = (Button) findViewById(R.id.bt_Retour);

        jetonAuth = getSharedPreferences("AppData", Context.MODE_PRIVATE);
        identifiant = jetonAuth.getString("login", null);
        mdp = jetonAuth.getString("mdp", null);

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        // Gestion de l'évènement sur le clic du bouton Authe ntification
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Création d'un tableau de paramètres à passer à l'AsyncTask
                String[] mesParams = {identifiant, mdp, "http://gsb.dynu.net:8081/suivivisitesgsb/import.php"};
                Toast.makeText(getApplicationContext(), "clic bouton", Toast.LENGTH_SHORT).show();
                // Lance l'AsyncTask
                connexionAsynchrone = new Connexion(Import.this).execute(mesParams);
            }
        });
    }

    // --> Retour
    // Méthode exécutée après la fin de l'AsynTask
    public void retourImport(StringBuilder donneesJSONServeur) {
        ArrayList<Visites> listeVisites = new ArrayList<Visites>();

        // Récupération des éléments au format JSON (dans un tableau)
        JsonElement json = new JsonParser().parse(donneesJSONServeur.toString());
        JsonArray tabJSON = json.getAsJsonArray();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
        for (JsonElement obj : tabJSON) {
            // Désérialisation (chaine JSON -> Objet Visite)
            Visites uneVisite = gson.fromJson(obj.getAsJsonObject(), Visites.class);
            listeVisites.add(uneVisite);
        }

        // Si la liste contient visites alors on la sauvegarde dans la base DB4o
        if (!listeVisites.isEmpty()) {
            Modele modele = Modele.getModele();
            modele.deleteVisites();
            modele.addVisites(listeVisites);
            Toast.makeText(this, "Importation de données effectuée", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }
}
