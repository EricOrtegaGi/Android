package me.ericortegagisbert.dam.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonClick;
    private TextView textViewResult;
    private TextView textViewTimeRemaining;
    private TextView textViewTotalClicks;

    private int clickCount = 0;
    private int clickCountPerSecond = 0;
    private int totalClicks = 0;
    private int peakClicksPerSecond = 0;

    private Handler handler = new Handler();
    private boolean counting = false;

    private int totalTime = 5;  // Tiempo total en segundos
    private int currentTime = totalTime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonClick = findViewById(R.id.buttonClick);
        textViewResult = findViewById(R.id.textViewResult);
        textViewTimeRemaining = findViewById(R.id.textViewTimeRemaining);
        textViewTotalClicks = findViewById(R.id.textViewTotalClicks);
        ImageButton infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAppInfo();
            }

            private void showAppInfo() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Información de la aplicación");
                builder.setMessage("Creat per: Eric Ortega Gisbert " + "https://github.com/EricOrtegaGi/ComptadorAndroid");
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!counting) {
                    startCounting();
                }
                clickCount++;
                totalClicks++;
                updateClicksPerSecond();
                updateTotalClicks();
            }
        });
    }

    private void startCounting() {
        counting = true;
        clickCount = 0;
        clickCountPerSecond = 0;
        peakClicksPerSecond = 0;
        currentTime = totalTime;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                totalClicks = 0;
                counting = false;
                clickCountPerSecond = clickCount;

                // Muestra los clics totales donde antes se mostraba el pico de clics por segundo
                textViewResult.setText("Clicks Totales: " + totalClicks);

                // Congelar el botón durante 2 segundos
                buttonClick.setEnabled(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonClick.setEnabled(true);
                    }
                }, 2000);

                // Reiniciar el conteo
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textViewResult.setText("Clics por segundo: 0");
                        updateClicksPerSecond();
                    }
                }, 5000);

                // Muestra el pico de clics por segundo al final de la partida
                textViewTotalClicks.setText("Pico de clics por segundo: " + peakClicksPerSecond);
            }
        }, totalTime * 1000);

        // Actualizar el tiempo restante cada segundo
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentTime > 0) {
                    textViewTimeRemaining.setText("Tiempo restante: " + currentTime + " segundos");
                    currentTime--;
                    handler.postDelayed(this, 1000);
                } else {
                    textViewTimeRemaining.setText("Tiempo restante: 0 segundos");
                }
            }
        });
    }

    private void updateClicksPerSecond() {
        int clicksPerSecond = (int) (clickCount / (totalTime - currentTime + 1.0));
        textViewResult.setText("Clics por segundo: " + clicksPerSecond);
        // Verificar si el valor actual de CPS es mayor que el pico registrado
        if (clicksPerSecond > peakClicksPerSecond) {
            peakClicksPerSecond = clicksPerSecond;
        }
    }

    private void updateTotalClicks() {
        textViewTotalClicks.setText("Total de clics: " + totalClicks);
    }


}
