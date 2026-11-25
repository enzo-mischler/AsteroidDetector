package com.example.asteroiddetector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.asteroiddetector.databinding.ItemAsteroideBinding;

import java.util.List;

public class AsteroideAdapteur extends ArrayAdapter<Asteroide> {

    public AsteroideAdapteur(Context context, List<Asteroide> asteroides) {
        super(context, 0, asteroides);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ItemAsteroideBinding b;
        if (convertView == null) {
            b = ItemAsteroideBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = b.getRoot();
            convertView.setTag(b);
        } else {
            b = (ItemAsteroideBinding) convertView.getTag();
        }

        Asteroide a = getItem(position);
        b.nom.setText(a.getNom());
        b.magnitude.setText(getContext().getString(R.string.magnitude,
                String.valueOf(a.getMagnitude())));
        b.distance.setText(getContext().getString(R.string.distance,
                String.valueOf((long) a.getDistance())));

        return convertView;
    }
}
