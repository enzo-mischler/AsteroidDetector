package com.example.asteroiddetector;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import com.example.asteroiddetector.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    public static final String CLE_API = "prLQM8wCKFpdKGtwgzD9ThhtbbPLJnrGQTKgzmMt";
    private static final String ADRESSE = "https://api.nasa.gov/neo/rest/v1/feed";
    private ActivityHomeBinding binding;
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_DISTANCE = "distance";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        chargerAsteroides();
    }

    private void chargerAsteroides() {
        String jour = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        String url = ADRESSE + "?start_date=" + jour + "&end_date=" + jour + "&api_key=" + CLE_API;
        RequestQueue file = Volley.newRequestQueue(this);
        JsonObjectRequest requete = new JsonObjectRequest(
                Request.Method.GET, url, null,
                json -> afficher(json,jour),
                erreur -> Toast.makeText(this, R.string.erreur, Toast.LENGTH_SHORT).show());
        file.add(requete);
    }

    private void afficher(JSONObject json, String jour) {
        try {
            JSONArray tableau = json.getJSONObject("near_earth_objects").getJSONArray(jour);

            List<Asteroide> asteroides = new ArrayList<>();
            for (int i = 0; i < tableau.length(); i++) {
                JSONObject objet = tableau.getJSONObject(i);
                String id = objet.getString("id");
                String nom = objet.getString("name");
                double magnitude = objet.getDouble("absolute_magnitude_h");
                double distance = objet.getJSONArray("close_approach_data")
                        .getJSONObject(0)
                        .getJSONObject("miss_distance")
                        .getDouble("kilometers");
                asteroides.add(new Asteroide(id, nom, magnitude, distance));
            }

            binding.liste.setAdapter(new AsteroideAdapteur(this, asteroides));
            binding.liste.setOnItemClickListener((parent, vue, position, id) -> {
                Asteroide a = asteroides.get(position);
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(EXTRA_ID, a.getId());
                intent.putExtra(EXTRA_DISTANCE, a.getDistance());
                startActivity(intent);
            });
            binding.nombre.setText(getString(R.string.nombre, asteroides.size()));
            Toast.makeText(this, R.string.recu, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this, R.string.erreur, Toast.LENGTH_SHORT).show();
        }
    }

}