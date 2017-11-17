package com.ramon.code.scoutsapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.ramon.code.scoutsapp.models.TimeInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView speechText;
    private Button recordButton, stopButton;
    private RequestQueue queue;
    private boolean keepRecording = false;
    private SpeechRecognizer speechRecognizer;

    private TimeInfo timeSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechText =  findViewById(R.id.textView_speech);
        recordButton = findViewById(R.id.recordButton);
        stopButton = findViewById(R.id.stopButton);

//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
//        speechRecognizer.setRecognitionListener(MainActivity.this);

        Intent in = getIntent();

        Bundle extras = in.getExtras();

        if(extras != null){
            if(extras.containsKey("time")){

                timeSelecionado = (TimeInfo) extras.get("time");

                Toast.makeText(this, timeSelecionado.getEquipe(), Toast.LENGTH_SHORT).show();
            }
        }

        queue = Volley.newRequestQueue(MainActivity.this);


    }

    public void stopSpeechInput(View v){
        speechRecognizer.cancel();
        keepRecording = false;
        recordButton.setEnabled(true);
        Toast.makeText(this, "Stop Recording...", Toast.LENGTH_SHORT).show();
    }

    public void getSpeechInput(View v){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(60000));
        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(60000));
//        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 20000);

        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, 10);

        }else{
            Toast.makeText(this, "Device don't support", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result =  data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechText.setText(result.get(0));

                    try {
                        JSONObject body = new JSONObject();
                        body.put("text",result.get(0));
                        body.put("ano",timeSelecionado.getAno());
                        body.put("campeonato",timeSelecionado.getCampeonato());
                        body.put("equipe",timeSelecionado.getEquipe());
                        body.put("data",timeSelecionado.getData());

                        Log.i("input text: ", body.getString("text"));
                        this.getResponse(body);
                    }
                    catch (JSONException e){
                        Log.i("input text: ", "Error"+e.getMessage());
                    }
                }

//                if(keepRecording){
//                    getSpeechInput(recordButton);
//                }

                break;

        }
    }

    public void getResponse(JSONObject body) throws JSONException {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://jimmycricket-orquestrador.mybluemix.net/jimmycricket/api/v1/extract", body, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response Object ", "Response Arrived: "+response.toString());
                        Toast toast = Toast.makeText(MainActivity.this, "Backend Received", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i("Conversation Object ", "Request Error: "+error.getMessage());

                    }



                });

        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);

    }

//    @Override
//    public void onReadyForSpeech(Bundle bundle) {
//        //Log.i(TAG,"onReadyForSpeech");
//        //recordButton.setEnabled(false);
//    }
//
//    @Override
//    public void onBeginningOfSpeech() {
//        Log.i("speechRecognizer","onBeginningOfSpeech");
//    }
//
//    @Override
//    public void onRmsChanged(float v) {
//
//    }
//
//    @Override
//    public void onBufferReceived(byte[] bytes) {
//        //Log.i(TAG,"onBufferReceived");
//    }
//
//    @Override
//    public void onEndOfSpeech() {
//        //Toast.makeText(this, "Pausa", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onError(int error) {
//        //Log.i(TAG,"error: " + error);
//
//        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(60000));
//        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(60000));
////        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 20000);
//
//
//        speechRecognizer.startListening(i);
//    }
//
//    @Override
//    public void onResults(Bundle results) {
//        Log.i("Speech", "onResults");
//        ArrayList strlist = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        speechText.setText(speechText.getText()+String.valueOf(strlist.get(0)));
//
//        try {
//            JSONObject body = new JSONObject("{\"text\": \""+strlist.get(0)+"\"}");
//
//            Log.i("input text: ", body.getString("text"));
//            this.getResponse(body);
//        }
//        catch (JSONException e){
//            Log.i("input text: ", "Error"+e.getMessage());
//        }
//
//        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(60000));
//        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(60000));
////        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 20000);
//
//        speechRecognizer.startListening(i);
//    }
//
//    @Override
//    public void onPartialResults(Bundle bundle) {
//        //Log.i(TAG,"onPartialResults");
//    }
//
//    @Override
//    public void onEvent(int i, Bundle bundle) {
//        //Log.i(TAG,"onEvent");
//    }
}
