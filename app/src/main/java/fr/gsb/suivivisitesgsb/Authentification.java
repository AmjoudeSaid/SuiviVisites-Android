package fr.gsb.suivivisitesgsb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Authentification extends AppCompatActivity {

    private Button btnSeConnecter, btnAnnuler;
    private EditText editTextLogin, editTextMDP;
    private String identifiant, mdp, mdpsha1;
    private AsyncTask<String, String, Boolean> connexionAsynchrone;
    private SharedPreferences jetonAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        jetonAuth = getSharedPreferences("AppData", Context.MODE_PRIVATE);
        identifiant = jetonAuth.getString("login", null);
        mdp = jetonAuth.getString("mdp", null);

        // Récupère les boutons depuis le layout
        btnSeConnecter = (Button) findViewById(R.id.buttonAuthentification);
        btnAnnuler=(Button) findViewById(R.id.buttonAnnuler);

        // Gestion de l'évènement sur le clic du bouton Annuler
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        // Gestion de l'évènement sur le clic du bouton Authentification
        btnSeConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Récupère les informations saisies par l'utilisateur
                editTextLogin = (EditText) findViewById(R.id.login);
                editTextMDP = (EditText) findViewById(R.id.mdp);
                identifiant = editTextLogin.getText().toString();
                mdp = editTextMDP.getText().toString();

                // Création d'un tableau de paramètres à passer à l'AsyncTask
                String[] mesParams = {identifiant, mdp, "http://gsb.dynu.net:8081/suivivisitesgsb/authentification.php"};
                // Lance l'AsyncTask
                connexionAsynchrone = new Connexion(Authentification.this);
                connexionAsynchrone.execute(mesParams); // --> Serveur
            }
        });
    }

    // --> Retour des données du serveur
    // Méthode éxécutée après l'AsyncTask Connexion
    public void retourVersAuthentification(StringBuilder codeRetour) {
        SharedPreferences.Editor edit = jetonAuth.edit();

        // Authentification réussie si la valeur de retour est 1 (voir script PHP)
        if (codeRetour.toString().compareTo("1") == 0) {
            Toast.makeText(this, "Authentification réussie", Toast.LENGTH_SHORT).show();

            // On stocke dans les Shared Preference le login et le mot de passe si l'utilisateur a été authentifié
            edit.putString("login", identifiant);
            edit.putString("mdp", mdpsha1);
            edit.commit();
            Modele.getModele().deleteVisites();

            setResult(Activity.RESULT_OK);
            finish();
        } else {
            // Echec Authentification
            Toast.makeText(this, "Echec authentification", Toast.LENGTH_SHORT).show();
            edit.clear();
            edit.commit();
            setResult (Activity.RESULT_CANCELED);
            finish();
        }
    }
}