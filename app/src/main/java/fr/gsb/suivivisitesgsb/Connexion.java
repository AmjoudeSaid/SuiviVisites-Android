package fr.gsb.suivivisitesgsb;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connexion extends AsyncTask<String, String, Boolean> {

    // Référence à l'activité qui appelle
    private WeakReference<Activity> activiteAppelante = null;
    private String nomClasseActiviteAppelante;
    private StringBuilder stringBuilder = new StringBuilder();

    // Constructeur
    public Connexion(Activity activiteAppelante) {
        this.activiteAppelante = new WeakReference<Activity>(activiteAppelante);// Référence à l'activité appelante
        nomClasseActiviteAppelante = activiteAppelante.getClass().toString();	// Nom de la classe (activité) appelante
    }

    // Méthode exécutée au démarrage
    @Override
    protected void onPreExecute() {
        // Au lancement, on envoie un message à l'appelant
        if (activiteAppelante.get() != null)
            Toast.makeText(activiteAppelante.get(), "AsyncTask démarre", Toast.LENGTH_SHORT).show();
    }

    // Méthode exécutée en arrière plan
    @Override
    protected Boolean doInBackground(String... params){
        String identifiant = "", mdp = "", urlServiceGSB = "", listeVisites = "";

        // Affectation des paramètres adéquats en fonction de l'activité appelante
        if (nomClasseActiviteAppelante.contains("Authentification") || nomClasseActiviteAppelante.contains("Import")) {
            //Toast.makeText(activiteAppelante.get(), "AsyncTask paramètres", Toast.LENGTH_SHORT).show();
            identifiant = params[0];
            mdp = params[1];
            urlServiceGSB = params[2];
        }

        // Paramètres utiles à l'activité Exportation
        if (nomClasseActiviteAppelante.contains("Export")) {
            listeVisites = params[3];
        }

        // Connexion au serveur en POST et envoi des données d'authentification au format JSON
        HttpURLConnection urlConnexion = null;
        try {
            URL url = new URL(urlServiceGSB);
            urlConnexion = (HttpURLConnection) url.openConnection();
            urlConnexion.setRequestProperty("Content-Type", "application/json");
            urlConnexion.setRequestProperty("Accept", "application/json");
            urlConnexion.setRequestMethod("POST");
            urlConnexion.setDoOutput(true);
            urlConnexion.setConnectTimeout(5000);

            //Toast.makeText(activiteAppelante.get(), "AsyncTask connexion ok", Toast.LENGTH_SHORT).show();
            OutputStreamWriter out = new OutputStreamWriter(urlConnexion.getOutputStream());

            // Selon l'activité appelante on peut passer des paramètres en JSON
            if (nomClasseActiviteAppelante.contains("Authentification") || nomClasseActiviteAppelante.contains("Import")) {

                // Création objet JSON clé->valeur pour l'activité Authentification
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("login", identifiant);
                jsonParam.put("mdp", mdp);
                Log.i("versServeur", "Envoyé au serveur : " + jsonParam);
                out.write(jsonParam.toString());
                out.flush();
            }

            // Création objet JSON clé->valeur pour l'activité Export
            if (nomClasseActiviteAppelante.contains("Export")) {
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("login", identifiant);
                jsonParam.put("mdp", mdp);
                jsonParam.put("listeVisites", listeVisites);
                Log.i("versServeur", "Envoyé au serveur : " + jsonParam.toString());
                out.write(jsonParam.toString());
                out.flush();
            }

            out.close();

            // Traitement du script PHP correspondant sur le serveur

            // Récupération du résultat de la requête au format JSON depuis le serveur
            Log.i("marche", "allo");
            int HttpResult = urlConnexion.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnexion.getInputStream(), "utf-8"));

                String line = null;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
                br.close();
                Log.i("retourServeur", "Reçu du serveur :" + stringBuilder.toString());

            } else
                Log.i("retourServeur", "Erreur :" + urlConnexion.getResponseMessage());

            // Gestion des exceptions pouvant survenir pendant la connexion vers le serveur
        } catch (MalformedURLException e) {
            Toast.makeText(activiteAppelante.get(), "URL malformée", Toast.LENGTH_SHORT).show();
            return false;

        } catch (java.net.SocketTimeoutException e) {
            Toast.makeText(activiteAppelante.get(), "Time out", Toast.LENGTH_SHORT).show();
            return false;

        } catch (IOException e) {
            Toast.makeText(activiteAppelante.get(), "Erreur E/S", Toast.LENGTH_SHORT).show();
            return false;

        } catch (JSONException e) {
            Toast.makeText(activiteAppelante.get(), "Erreur JSON", Toast.LENGTH_SHORT).show();
            return false;

        } finally {
            // Ferme la connexion vers le serveur dans tous les cas
            if (urlConnexion != null)
                urlConnexion.disconnect();
        }

        return true;
    }

    // Utilisation de onProgress pour afficher des messages pendant le doInBackground
    @Override
    protected void onProgressUpdate(String... param) {
    }

    // Méthode exécutée à la fin de la méthode doInBackGroud()
    @Override
    protected void onPostExecute(Boolean result) {
        if (activiteAppelante.get() != null) {
            if (result) {
                Toast.makeText(activiteAppelante.get(), "Fin AsynckTask", Toast.LENGTH_SHORT).show();

                // Retour vers une méthode de la classe appelante
                if (nomClasseActiviteAppelante.contains("Authentification")) {
                    ((Authentification) activiteAppelante.get()).retourVersAuthentification(stringBuilder);
                }

                if (nomClasseActiviteAppelante.contains("Import")) {
                    ((Import) activiteAppelante.get()).retourImport(stringBuilder);
                }

            } else
                Toast.makeText(activiteAppelante.get(), "Fin ko", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCancelled() {
        if (activiteAppelante.get() != null)
            Toast.makeText(activiteAppelante.get(), "Annulation", Toast.LENGTH_SHORT).show();
    }
}
