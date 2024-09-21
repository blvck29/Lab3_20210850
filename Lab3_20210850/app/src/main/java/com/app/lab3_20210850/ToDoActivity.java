package com.app.lab3_20210850;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.lab3_20210850.model.ToDo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ToDoActivity extends AppCompatActivity {

    TextView toDoNombre;
    Spinner spinnerTodo;
    Button cambiarEstadoButton;

    private Bundle datosUsuarioLogeado;
    private int userId;
    private String nombre;
    private String apellido;
    private String correo;
    private String genero;

    private boolean existToDos;
    private List<ToDo> toDos;

    List<String> todoStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_to_do);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeDataBundle();

        toDoNombre = findViewById(R.id.tareas_label);
        toDoNombre.setText("Ver tareas de : " + nombre);


        spinnerTodo = findViewById(R.id.spinner_todo);

        for (ToDo todo : toDos) {
            String status = todo.isCompleted() ? "Completado" : "No Completado";
            todoStrings.add(todo.getTodo() + " - " + status);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, todoStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTodo.setAdapter(adapter);

        spinnerTodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cambiarEstadoButton = findViewById(R.id.cambiar_estado_button);

        cambiarEstadoButton.setOnClickListener(view -> {

            int position = spinnerTodo.getSelectedItemPosition();
            ToDo selectedTodo = toDos.get(position);

            selectedTodo.setCompleted(!selectedTodo.isCompleted());


            todoStrings.set(position, selectedTodo.getTodo() + " - " + (selectedTodo.isCompleted() ? "Completado" : "No Completado"));
            adapter.notifyDataSetChanged();

            new MaterialAlertDialogBuilder(ToDoActivity.this)
                    .setTitle("Cambio Exitoso")
                    .setMessage("Se cambió el estado de la tarea")
                    .setPositiveButton("Entendido", null)
                    .show();

        });

    }

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.exit_off) {
            Log.d("EXIT", "Session cerrada.");

            Intent intent = new Intent(ToDoActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(ToDoActivity.this, TimerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer,menu);
        return true;
    }

}