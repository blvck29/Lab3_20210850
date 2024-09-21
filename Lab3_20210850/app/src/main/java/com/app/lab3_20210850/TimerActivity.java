package com.app.lab3_20210850;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.lab3_20210850.api.ApiClient;
import com.app.lab3_20210850.api.ApiService;
import com.app.lab3_20210850.model.ToDo;
import com.app.lab3_20210850.model.ToDosResponse;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerActivity extends AppCompatActivity {

    ImageButton controlButton;
    TextView timerCountdown;

    TextView estadoText;

    int timerOn = 1;

    CountDownTimer pomodoroCountDownTimer;
    long pomodoroTimer = TimeUnit.MINUTES.toMillis(25);

    CountDownTimer descansoCountDownTimer;
    long descansoTimer = TimeUnit.MINUTES.toMillis(5);

    private Bundle datosUsuarioLogeado;
    private int userId;
    private String nombre;
    private String apellido;
    private String correo;
    private String genero;

    private boolean existToDos;
    private List<ToDo> toDos;


    public void initializeDataBundle() {
        Intent intent = getIntent();
        datosUsuarioLogeado = intent.getExtras();

        userId = datosUsuarioLogeado.getInt("idUser", 0);
        nombre = datosUsuarioLogeado.getString("firstName", "Username");
        apellido = datosUsuarioLogeado.getString("lastName", "LastName");
        correo = datosUsuarioLogeado.getString("email", "Email");
        genero = datosUsuarioLogeado.getString("gender", "male");

        existToDos = datosUsuarioLogeado.getBoolean("existToDos");
        toDos = (List<ToDo>) datosUsuarioLogeado.getSerializable("todosList");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        datosUsuarioLogeado = intent.getExtras();

        if (datosUsuarioLogeado != null) {
            initializeDataBundle();
        } else {
            Log.e("BUNDLE INFOOO", "El Bundle es null");
        }

        if (datosUsuarioLogeado != null) {


            TextView nombreUsuario = findViewById(R.id.nombre_usuario);
            nombreUsuario.setText(nombre + ' ' + apellido);

            TextView correoUsuario = findViewById(R.id.correo_usuario);
            correoUsuario.setText(correo);

            ImageView generoUsuario = findViewById(R.id.genero_usuario);

            if (genero.equals("male")){
                generoUsuario.setImageResource(R.drawable.baseline_man_24);
            } else if (genero.equals("female")){
                generoUsuario.setImageResource(R.drawable.baseline_woman_24);
            }

        } else {
            Log.e("Bundle Error", "No se recibieron datos");
        }


        timerCountdown = findViewById(R.id.timer_countdown);

        estadoText = findViewById(R.id.estado_text);

        controlButton = findViewById(R.id.control_timer_button);
        controlButton.setOnClickListener(view -> handleControlButton());

    }

    private void handleControlButton() {

        if (timerOn == 1){
            controlButton.setImageResource(R.drawable.baseline_restart_24);
        } else if (timerOn == 0) {
            controlButton.setImageResource(R.drawable.play_24);
        }

        startRestartPomodoro();
    }

    private void startRestartPomodoro(){

        if (pomodoroCountDownTimer != null){
            pomodoroCountDownTimer.cancel();
        }

        estadoText.setText("Descanso: 5:00");
        timerCountdown.setText("25:00");

        pomodoroCountDownTimer = new CountDownTimer(pomodoroTimer, 1000) {
            @Override
            public void onTick(long milisegundosRestantes) {
                int minutos = (int) ((milisegundosRestantes) / 1000 % 3600)/60;
                int segundos = (int) ((milisegundosRestantes) / 1000 % 60);

                String tiempoRestante = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
                timerCountdown.setText(tiempoRestante);
            }

            @Override
            public void onFinish() {
                timerCountdown.setText("00:00");
                new MaterialAlertDialogBuilder(TimerActivity.this)
                        .setTitle("¡Felicidades!")
                        .setMessage("Empezó el tiempo de descanso")
                        .setPositiveButton("Entendido", null)
                        .show();


                startDescanso();
            }
        }.start();
    }

    private void startDescanso(){

        if (pomodoroCountDownTimer != null){
            pomodoroCountDownTimer.cancel();
        }

        estadoText.setText("En descanso");

        timerCountdown.setText("5:00");
        controlButton.setClickable(false);

        descansoCountDownTimer = new CountDownTimer(descansoTimer, 1000) {
            @Override
            public void onTick(long milisegundosRestantes) {
                int minutos = (int) ((milisegundosRestantes) / 1000 % 3600)/60;
                int segundos = (int) ((milisegundosRestantes) / 1000 % 60);

                String tiempoRestante = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
                timerCountdown.setText(tiempoRestante);
            }

            @Override
            public void onFinish() {
                timerCountdown.setText("00:00");

                Log.d("TO DO INFO TIMER", existToDos + " : " + toDos.size());


                if (existToDos) {

                    controlButton.setClickable(true);

                    Intent intent = new Intent(TimerActivity.this, ToDoActivity.class);
                    intent.putExtras(datosUsuarioLogeado);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);

                } else {

                    estadoText.setText("Fin del descanso");
                    controlButton.setClickable(true);

                    new MaterialAlertDialogBuilder(TimerActivity.this)
                            .setTitle("Atención")
                            .setMessage("Terminó el tiempo de descanso. Dale al botón de reinicio para comenzar otro ciclo.")
                            .setPositiveButton("Entendido", null)
                            .show();
                }

            }
        }.start();



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.exit_off){
            Log.d("EXIT", "Session cerrada.");

            Intent intent = new Intent(TimerActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer,menu);
        return true;
    }

}