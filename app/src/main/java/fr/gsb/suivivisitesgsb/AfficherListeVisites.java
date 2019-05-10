package fr.gsb.suivivisitesgsb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class AfficherListeVisites extends AppCompatActivity {

    private ListView listView;
    private List<Visites> listeVisites;
    private Modele modele = Modele.getModele();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_liste_visites);

        // Affichage de la liste de visites
        listeVisites = modele.listeVisites();

        Log.i("bd", listeVisites.toString());
        listView = (ListView) findViewById(R.id.ListeVisite);
        VisiteAdapter adaptVisite = new VisiteAdapter(this, listeVisites);
        listView.setAdapter(adaptVisite);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Toast.makeText(getApplicationContext(), "Choix : " + listeVisites.get(position).getId(), Toast.LENGTH_SHORT).show();

                // Appel de l'activité AfficheVisite
                Intent myIntent = new Intent(getApplicationContext(), AfficheVisite.class);
                myIntent.putExtra("paramIdVisite", listeVisites.get(position).getId());
                startActivity(myIntent);


            }
        });
    }

}
