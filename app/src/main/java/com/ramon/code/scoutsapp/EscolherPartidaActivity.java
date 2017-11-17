package com.ramon.code.scoutsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ramon.code.scoutsapp.models.Partida;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EscolherPartidaActivity extends AppCompatActivity {


    private static final String TAG = "REQUEST INFO:";
    private ListView listViewPartidas;
    private ArrayAdapter<Partida> adapter;
    private ArrayList itemListPartidas = new ArrayList<String>();



    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_partida);

        listViewPartidas = findViewById(R.id.listView_Partidas);


        adapter = new ArrayAdapter<Partida>(this,android.R.layout.simple_list_item_1,itemListPartidas);

        listViewPartidas.setAdapter(adapter);

        listViewPartidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Partida p = adapter.getItem(i);

                Intent intent = new Intent(EscolherPartidaActivity.this,EscolherTimeActivity.class);
                intent.putExtra("partida",p);
                startActivity(intent);


            }
        });

        queue = Volley.newRequestQueue(EscolherPartidaActivity.this);

        try {
            getMatches();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getMatches() throws JSONException{

        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (Request.Method.GET, "https://jimmycricket-orquestrador.mybluemix.net/jimmycricket/api/v1/partida/getAll", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Response Arrived: " + response.toString());
                        Toast toast = Toast.makeText(EscolherPartidaActivity.this, "Backend Received", Toast.LENGTH_SHORT);
                        toast.show();

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONArray equipes = (JSONArray) response.getJSONObject(i).get("equipes");

                                String timeMandante = (String) equipes.getJSONObject(0).get("nome");
                                String timeVisitante = (String) equipes.getJSONObject(1).get("nome");
                                String campeonato = (String) response.getJSONObject(i).get("campeonato");
                                int rodada = (int) response.getJSONObject(i).get("rodada");
                                int ano = (int) response.getJSONObject(i).get("ano");
                                String data = response.getJSONObject(i).getString("data");


                                Partida partida = new Partida(timeMandante,timeVisitante,campeonato,rodada,ano,data);



                                String itemTextPartida = partida.getTimeMandante() + " x " + partida.getTimeVisitante() + ": " + partida.getData();

                                adapter.add(partida);

                                Log.i(TAG, itemTextPartida);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i(TAG, "Request Error: "+error.getMessage());
                    }
                });

        queue.add(jsArrRequest);

    }
}