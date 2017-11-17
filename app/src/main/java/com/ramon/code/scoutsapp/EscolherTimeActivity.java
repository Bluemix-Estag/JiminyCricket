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

import com.ramon.code.scoutsapp.models.Partida;
import com.ramon.code.scoutsapp.models.TimeInfo;

import java.sql.Time;
import java.util.ArrayList;

public class EscolherTimeActivity extends AppCompatActivity {

    private ListView listViewTimes;
    private ArrayAdapter<TimeInfo> adapter;
    private ArrayList arrayListTimes = new ArrayList<TimeInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_time);

        listViewTimes = findViewById(R.id.listView_Times);

        adapter = new ArrayAdapter<TimeInfo>(this, android.R.layout.simple_list_item_1,arrayListTimes);

        listViewTimes.setAdapter(adapter);

        loadListView();

        listViewTimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TimeInfo t = adapter.getItem(i);

                Intent intent = new Intent(EscolherTimeActivity.this, MainActivity.class);
                intent.putExtra("time",t);
                startActivity(intent);
                finish();

            }
        });
    }

    public void loadListView(){
        Intent in = getIntent();

        Bundle extras = in.getExtras();

        if(extras != null){
            if(extras.containsKey("partida")) {
                Partida p = (Partida) extras.getSerializable("partida");

                TimeInfo timeMandante = new TimeInfo(p.getTimeMandante(),p.getCampeonato(),p.getAno(),p.getData());
                TimeInfo timeVisitante = new TimeInfo(p.getTimeVisitante(),p.getCampeonato(),p.getAno(),p.getData());

                adapter.addAll(timeMandante,timeVisitante);
            }
        }
    }
}
