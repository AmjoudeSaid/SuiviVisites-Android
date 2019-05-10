package fr.gsb.suivivisitesgsb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewAuthentification, imageViewVisites, imageViewImport, imageViewExport;
    private SharedPreferences jetonAuth;
    private String identifiant, mdp;
    private static final int REQUEST_WRITE_STORAGE = 100;
    private static Modele m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Supprimer les shared preferences au démarrage de l'application
        jetonAuth = getSharedPreferences("AppData", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = jetonAuth.edit();
        //edit.clear().commit();

        // Vérifier les permissions (nécéssaire à partir d'Android 6.0)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);

        // Récupération des images du menu de l'application
        imageViewAuthentification = (ImageView) findViewById(R.id.imgAuth);
        imageViewVisites = (ImageView) findViewById(R.id.imgVisites);
        imageViewImport = (ImageView) findViewById(R.id.imgImport);
        imageViewExport = (ImageView) findViewById(R.id.imgExport);

        // Désactiver les boutons Import, Visites et Export
        //imageViewVisites.setEnabled(true);
        //imageViewImport.setEnabled(true);
        //imageViewExport.setEnabled(true);
        //imageViewAuthentification.setEnabled(true);


        // Gestion des clics sur les images du menu
        imageViewAuthentification.setOnClickListener(imageClick);
        imageViewVisites.setOnClickListener(imageClick);
        imageViewImport.setOnClickListener(imageClick);
        imageViewExport.setOnClickListener(imageClick);

        m = Modele.getModele();

    }

    // Vérification des permissions accordées par l'utilisateur
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission accordée
                    Modele m = Modele.getModele();
                    m.deleteVisites();
                } else {
                    // Permission refusée
                    Toast.makeText(getApplicationContext(), "L'application ne peut pas fonctionner sans cette autorisation", Toast.LENGTH_SHORT).show();
                    this.finishAffinity();
                }
            }
        }
    }

    // Création de l'écouteur imageClick et du traitement associé
    private View.OnClickListener imageClick = new View.OnClickListener(){
        public void onClick(View v){
            Intent i;

            switch (v.getId()){
                // Cas du clic sur l'image Visite
                case R.id.imgVisites:
                    i = new Intent(getApplicationContext(), AfficherListeVisites.class);
                    startActivity(i);
                    break;
                // Cas du clic sur l'image Authentification
                case R.id.imgAuth:
                    i = new Intent(getApplicationContext(), Authentification.class);
                    startActivity(i);
                    break;
                // Cas du clic sur l'image Exporter
                case R.id.imgExport:
                    i = new Intent(getApplicationContext(), Export.class);
                    startActivity(i);
                    break;
                // Cas du clic sur l'image Importer
                case R.id.imgImport:
                    i = new Intent(getApplicationContext(), Import.class);
                    startActivity(i);
                    break;
            }
        }
    };

    /*@Override
    protected void onResume(){
        super.onResume();
        identifiant = jetonAuth.getString("login", null);
        mdp = jetonAuth.getString("mdp", null);
        Log.i("menu", "Login :" + identifiant + "MDP :" + mdp);

        if((identifiant != null) && (mdp != null)){
            // Activation des boutons si l'utilisateur a été authentifié
            imageViewVisites.setEnabled(true);
            imageViewImport.setEnabled(true);
            imageViewExport.setEnabled(true);
        }else{
            //imageViewVisites.setEnabled(false);
            //imageViewImport.setEnabled(false);
            //imageViewExport.setEnabled(false);
        }
    }*/
}