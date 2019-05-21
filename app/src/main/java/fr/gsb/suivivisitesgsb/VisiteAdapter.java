package fr.gsb.suivivisitesgsb;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class VisiteAdapter extends BaseAdapter {

    private List<Visites> listeVisites;
    private LayoutInflater layoutInflater;

    public VisiteAdapter(Context c, List<Visites> lstVisite) {
        layoutInflater = LayoutInflater.from(c);
        listeVisites = lstVisite;
    }

    @Override
    public int getCount() {
        return listeVisites.size();
    }

    @Override
    public Object getItem(int arg0) {
        return listeVisites.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    private class ViewHolder {
        TextView textViewNom;
        TextView textViewPrenom;
        TextView textViewTelephone;
        TextView textViewAdresse;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.ligne_visite, null);
            holder.textViewNom = (TextView) convertView.findViewById(R.id.vueNom);
            holder.textViewPrenom = (TextView) convertView.findViewById(R.id.vuePrenom);
            holder.textViewAdresse = (TextView) convertView.findViewById(R.id.vueAdresse);
            holder.textViewTelephone = (TextView) convertView.findViewById(R.id.vueTel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Affichage des valeurs dans les emplacements récupérés précédemment
        holder.textViewNom.setText(listeVisites.get(position).getNom());
        holder.textViewPrenom.setText(listeVisites.get(position).getPrenom());
        holder.textViewAdresse.setText(listeVisites.get(position).getAdresse());
        String s = listeVisites.get(position).getTel();
        s = String.format("%s.%s.%s.%s.%s", s.substring(0, 2),
                s.substring(2, 4), s.substring(4, 6), s.substring(6, 8),
                s.substring(8, 10));
        holder.textViewTelephone.setText(s);

        // Colorie les lignes paires et impaires
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.rgb(238, 233, 233));
        } else {
            convertView.setBackgroundColor(Color.rgb(255, 255, 255));
        }

        // Colorie en bleu les lignes dont la visite a pu être effectuée
        if (listeVisites.get(position).getPresent())
            convertView.setBackgroundColor(Color.rgb(117, 154, 201));


        return convertView;
    }
}