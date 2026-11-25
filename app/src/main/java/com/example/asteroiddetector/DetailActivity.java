package com.example.asteroiddetector;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asteroiddetector.databinding.ActivityDetailBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private static final String CLE_CENTRE = "centre";

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String id = getIntent().getStringExtra(HomeActivity.EXTRA_ID);
        double distance = getIntent().getDoubleExtra(HomeActivity.EXTRA_DISTANCE, 0);
        chargerDetail(id, distance);
        binding.system.setDistance(distance);

        binding.centre.addOnButtonCheckedListener((groupe, idBouton, coche) -> {
            if (!coche) return;
            choisirCentre(idBouton);
        });
        if (savedInstanceState != null) {
            int idBouton = savedInstanceState.getInt(CLE_CENTRE);
            binding.centre.check(idBouton);
            choisirCentre(idBouton);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle etat) {
        super.onSaveInstanceState(etat);
        etat.putInt(CLE_CENTRE, binding.centre.getCheckedButtonId());
    }

    private void choisirCentre(int idBouton) {
        if (idBouton == R.id.terre) {
            binding.system.setCentre(SystemView.Centre.TERRE);
        } else if (idBouton == R.id.asteroide) {
            binding.system.setCentre(SystemView.Centre.ASTEROIDE);
        } else {
            binding.system.setCentre(SystemView.Centre.SOLEIL);
        }
    }



    private void chargerDetail(String id, double distance) {
        String url = "https://api.nasa.gov/neo/rest/v1/neo/" + id + "?api_key=" + HomeActivity.CLE_API;
        RequestQueue file = Volley.newRequestQueue(this);
        JsonObjectRequest requete = new JsonObjectRequest(
                Request.Method.GET, url, null,
                json -> afficher(json, distance),
                erreur -> Toast.makeText(this, R.string.erreur, Toast.LENGTH_SHORT).show());
        file.add(requete);
    }

    private void afficher(JSONObject json, double distance) {
        try {
            String nom = json.getString("name");
            double magnitude = json.getDouble("absolute_magnitude_h");
            double periode = json.getJSONObject("orbital_data").getDouble("orbital_period");

            binding.nom.setText(nom);
            binding.magnitude.setText(getString(R.string.magnitude, String.valueOf(magnitude)));
            binding.distance.setText(getString(R.string.distance, String.valueOf((long) distance)));
            binding.periode.setText(getString(R.string.periode, String.valueOf(Math.round(periode))));
            binding.system.setPeriode(periode);
        } catch (JSONException e) {
            Toast.makeText(this, R.string.erreur, Toast.LENGTH_SHORT).show();
        }
    }
}
