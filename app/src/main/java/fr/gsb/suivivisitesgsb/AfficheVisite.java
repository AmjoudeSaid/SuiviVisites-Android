package fr.gsb.suivivisitesgsb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AfficheVisite extends AppCompatActivity {

    private String idVisite;
    private Visites visite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche_visite);

        // Récupération des données de l'activité appelante
        Bundle bundle = getIntent().getExtras();
        idVisite = bundle.getString("paramIdVisite");
        Toast.makeText(getApplicationContext(), idVisite, Toast.LENGTH_SHORT).show();

        // Récupére la visite ayant l'id passé en intent
        visite = (Modele.getModele()).trouveVisite(idVisite);

        // Données du prospect
        TextView textView = (TextView) findViewById(R.id.nomProspect);
        textView.setText(visite.getNom() + " " + visite.getPrenom());

        textView = (TextView) findViewById(R.id.adresseProspect);
        textView.setText(visite.getAdresse());

        textView = (TextView) findViewById(R.id.telProspect);
        String sTelephone = visite.getTel().toString();

        sTelephone = String.format("%s.%s.%s.%s.%s",
                sTelephone.substring(0, 2), sTelephone.substring(2, 4),
                sTelephone.substring(4, 6), sTelephone.substring(6, 8),
                sTelephone.substring(8, 10));
        textView.setText(sTelephone);

        // Remplissage des données si un avis est déjà enregistré
        if (visite.getPresent()){
            Switch sw = ((Switch) findViewById(R.id.presenceProspect));
            sw.setChecked(true);

            Calendar c = Calendar.getInstance();
            c.setTime(visite.getDate());
            int annee = c.get(Calendar.YEAR);
            int mois = c.get(Calendar.MONTH);
            int jour = c.get(Calendar.DAY_OF_MONTH);
            DatePicker datePicker = (DatePicker) findViewById(R.id.dateVisite);
            datePicker.updateDate(annee, mois, jour);

            EditText editTextMotif = (EditText) findViewById(R.id.motifVisite);
            editTextMotif.setText(visite.getMotif());

            RatingBar ratingBar = ((RatingBar) findViewById(R.id.confianceGSB));
            ratingBar.setRating(visite.getNiveauconfiance());

            RadioGroup rgLisi = (RadioGroup) findViewById(R.id.rgLisibilite);
            switch (visite.getLisibilite())
            {
                case "Parfaitement lisible":
                    rgLisi.check(R.id.rbLisibleOk);
                    break;
                case "Il faut mettre des lunettes":
                    rgLisi.check(R.id.rbLisibleMoyen);
                    break;
                case "Incompréhensible !":
                    rgLisi.check(R.id.rbLisibleKo);
                    break;
            }

            EditText editTextBilan = (EditText) findViewById(R.id.bilan);
            editTextBilan.setText(visite.getBilan());
        }

        // Gestion des évènements des boutons
        Button boutonValider = (Button) findViewById(R.id.validerAvis);
        boutonValider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAvis();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        Button boutonAnnuler = (Button) findViewById(R.id.annulerAvis);
        boutonAnnuler.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

    }

    // Méthode qui sauvegarde les modifications saisies
    public void saveAvis() {

        // On récupère les valeurs saisies
        Switch sw = ((Switch) findViewById(R.id.presenceProspect));

        int day = ((DatePicker) findViewById(R.id.dateVisite)).getDayOfMonth();
        int month = ((DatePicker) findViewById(R.id.dateVisite)).getMonth();
        int year = ((DatePicker) findViewById(R.id.dateVisite)).getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        EditText editTextMotif = (EditText) findViewById(R.id.motifVisite);

        RatingBar ratingBar = ((RatingBar) findViewById(R.id.confianceGSB));

        RadioGroup rgLisi = (RadioGroup) findViewById(R.id.rgLisibilite);

        // On s'assure qu'un bouton a été sélectionné
        String radioValue = "";
        if(rgLisi.getCheckedRadioButtonId() != -1)
            radioValue = ((RadioButton)findViewById(rgLisi.getCheckedRadioButtonId())).getText().toString();

        EditText editTextBilan = (EditText) findViewById(R.id.bilan);

        // Si le prospect est présent on sauvegarde tous les champs
        if(sw.isChecked()){
            visite.setPresent(true);
            visite.setDate(calendar.getTime());
            visite.setMotif(editTextMotif.getText().toString());
            visite.setNiveauconfiance(ratingBar.getRating());
            visite.setLisibilite(radioValue);

            visite.setBilan(editTextBilan.getText().toString());
            Toast.makeText(getApplicationContext(), "Sauvegarde avis ok", Toast.LENGTH_SHORT).show();
        } else{
            visite.setPresent(false);
            visite.setDate(calendar.getTime());
            Toast.makeText(getApplicationContext(), "Medecin abs", Toast.LENGTH_SHORT).show();
        }
        Log.i("avisvisite", visite.toString());
        Modele.getModele().saveVisite(visite);
    }

}
