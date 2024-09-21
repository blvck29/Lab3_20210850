package com.app.lab3_20210850;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.lab3_20210850.api.ApiClient;
import com.app.lab3_20210850.api.ApiService;
import com.app.lab3_20210850.model.RequestLogin;
import com.app.lab3_20210850.model.ToDo;
import com.app.lab3_20210850.model.ToDosResponse;
import com.app.lab3_20210850.model.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText userInput;
    EditText passwordInput;

    Button loginButton;

    ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    Usuario usuario;

    Bundle datosUsuarioBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userInput = findViewById(R.id.usuario_login);
        passwordInput = findViewById(R.id.contra_login);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> authenticateUser(userInput.getText().toString(), passwordInput.getText().toString()));
    }

    private void authenticateUser(String username, String password) {

        if (username.isEmpty() || password.isEmpty()) {
            new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("Error")
                    .setMessage("Debe introducir su usuario y contraseña.")
                    .setPositiveButton("Entendido", null)
                    .show();

            return;
        }

        RequestLogin loginRequest = new RequestLogin(username, password);

        apiService.login(loginRequest).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if (response.isSuccessful() && response.body() != null) {

                    usuario = response.body();
                    Log.d("SUCCESFULL LOGIN", "Autenticado: " + usuario.getUsername());

                    datosUsuarioBundle.putInt("idUser", usuario.getId());
                    datosUsuarioBundle.putString("firstName", usuario.getFirstName());
                    datosUsuarioBundle.putString("lastName", usuario.getLastName());
                    datosUsuarioBundle.putString("email", usuario.getEmail());
                    datosUsuarioBundle.putString("gender", usuario.getGender());


                    apiService.getTodosByUserId(usuario.getId()).enqueue(new Callback<ToDosResponse>() {
                        @Override
                        public void onResponse(Call<ToDosResponse> call, Response<ToDosResponse> response) {

                            if (response.isSuccessful()) {

                                ToDosResponse toDosResponse = response.body();
                                ArrayList<ToDo> toDos = toDosResponse.getTodos();
                                datosUsuarioBundle.putBoolean("existToDos", true);
                                datosUsuarioBundle.putSerializable("todosList", (Serializable) toDos);


                                Log.d("TO DO", "EXISTEN TAREAS: " + toDos.get(1).getTodo());

                                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                                intent.putExtras(datosUsuarioBundle);
                                startActivity(intent);
                                finish();

                            } else {
                                datosUsuarioBundle.putBoolean("existToDos", false);

                                Log.d("NO TO DO", "NO EXISTEN TAREAS");
                            }
                        }

                        @Override
                        public void onFailure(Call<ToDosResponse> call, Throwable t) {
                            Log.e("ERROR", "Error de red: " + t.getMessage());
                        }
                    });


                } else {

                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage("El usuario o contraseña ingresada son incorrectas.")
                            .setPositiveButton("Entendido", null)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("BAD LOGIN", "Error: " + t.getMessage());
            }
        });

    }

}