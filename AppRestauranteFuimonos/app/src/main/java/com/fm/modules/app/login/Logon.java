package com.fm.modules.app.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.MainActivity;
import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.pedidos.Principal;
import com.fm.modules.app.signup.SignUp;
import com.fm.modules.app.ui.PrincipalRestaurante;
import com.fm.modules.models.Driver;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.DriverService;
import com.fm.modules.service.RestauranteService;
import com.google.android.material.textfield.TextInputEditText;

public class Logon extends AppCompatActivity {

    private String usuario = "", passw = "";
    private TextInputEditText userInput;
    private TextInputEditText passInput;
    private Button buttonLogin;
    private ImageView buttonsing;


    ProgressBar progressBar;
    Acceder acceder = new Acceder();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        //getSupportActionBar().hide();
        userInput = (TextInputEditText) findViewById(R.id.etEmaillogin);
        passInput = (TextInputEditText) findViewById(R.id.etPasswordlogin);
        buttonLogin = (Button) findViewById(R.id.btnLogin);

        reiniciarAsynkProcess();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Logon.this, "Cargando ", Toast.LENGTH_SHORT).show();
                if (validUserAndPass()) {
                    acceder.execute();
                }
            }
        });
        sharedListener();


    }


    public boolean isNetActive() {
        boolean c = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                c = true;
            }
        } catch (Exception e) {
            Log.e("error", "" + "error al comprobar conexion");
            Log.e("error", "" + e);
            c = false;
        }
        return c;
    }

    public boolean validUserAndPass() {
        boolean b = false;
        usuario = userInput.getText().toString();
        passw = passInput.getText().toString();
        try {
            if ("".equals(usuario)) {
                //Toast.makeText(Logon.this, "Igrese usuario", Toast.LENGTH_LONG).show();
                userInput.setError("Ingrese usuario");
                return false;
            }
            if ("".equals(passw)) {
                //Toast.makeText(Logon.this, "Igrese contraseña", Toast.LENGTH_LONG).show();
                passInput.setError("Ingrese contraseña");
                return false;
            }
            return true;
        } catch (Exception e) {
        }
        return b;
    }

    private void sharedListener() {
        sharedPreferences = getSharedPreferences("LogonData", MODE_PRIVATE);
        String usuarioPref = sharedPreferences.getString("email", "neles");
        String passwPref = sharedPreferences.getString("password", "neles");
        if (!"neles".equals(usuarioPref) && !"neles".equals(passwPref)) {
            usuario = usuarioPref;
            passw = passwPref;
            acceder.execute();
        }
    }

    public void limpiar() {
        userInput.setText("");
        passInput.setText("");
    }

    public void reiniciarAsynkProcess() {
        acceder.cancel(true);
        acceder = new Acceder();
    }

    public class Acceder extends AsyncTask<String, String, Integer> {


        @Override
        protected Integer doInBackground(String... strings) {
            int v = 0;

            try {
                if (isNetActive()) {
                    System.out.println("comienza a leer vistas");
                    Restaurante r = new Restaurante();
                    r.setUsername(usuario);
                    r.setPassword(passw);

                    RestauranteService restauranteService = new RestauranteService();
                    v = restauranteService.signInRestaurante(r);
                    if (v > 0){
                        r = restauranteService.obtenerRestaurantePorId((long) v);
                        if (r != null) {
                            editor = sharedPreferences.edit();
                            editor.putString("email", usuario);
                            editor.putString("password", passw);
                            editor.apply();
                        } else {
                            editor = sharedPreferences.edit();
                            editor.putString("email", "neles");
                            editor.putString("password", "neles");
                            editor.commit();
                        }
                    }
                    Logued.restauranteLogued = r;
                    System.out.println("Restaurante Iniciado:" +Logued.restauranteLogued.toString());
                }
            } catch (Exception ex) {
                System.out.println("*** errrr***: " + ex);
                ex.printStackTrace();
            }
            return v;
        }


        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            try {
                switch (res) {
                    case 0:
                        Toast.makeText(Logon.this, "Error de servidor", Toast.LENGTH_LONG).show();
                        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_server_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                        break;
                    case -1:
                        Toast.makeText(Logon.this, "Error de Username", Toast.LENGTH_LONG).show();
                        AlertDialog dialog1 = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_user_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                        break;
                    case -2:
                        Toast.makeText(Logon.this, "Error de Password", Toast.LENGTH_LONG).show();
                        AlertDialog dialog2 = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_pass_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                        break;
                    case -4:
                        Toast.makeText(Logon.this, "Usuario Inactivo", Toast.LENGTH_LONG).show();
                        break;
                }
                if (res > 0){
                    Intent intent = new Intent(Logon.this, PrincipalRestaurante.class);
                    startActivity(intent);
                }
                limpiar();
                reiniciarAsynkProcess();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
