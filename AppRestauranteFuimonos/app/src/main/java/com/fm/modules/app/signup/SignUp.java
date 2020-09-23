package com.fm.modules.app.signup;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.commons.conectivity.Conectividad;
import com.fm.modules.app.login.Logon;
import com.fm.modules.models.Driver;
import com.fm.modules.models.Image;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.DriverService;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.UsuarioService;
import com.google.android.material.textfield.TextInputEditText;

import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUp extends AppCompatActivity {

    private TextInputEditText inputNombre;
    private TextInputEditText inputApellido;
    private TextInputEditText inputUsuario;
    private TextInputEditText inputPass;
    private TextInputEditText inputHoraEntrada;
    private TextInputEditText inputHoraSalida;
    private Button buttonSign;
    private boolean networking;

    SimpleDateFormat ffHora = new SimpleDateFormat("HH:mm");

    Registrar registrar = new Registrar();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_signup);

        inputNombre = (TextInputEditText) findViewById(R.id.sgnupName);
        inputApellido = (TextInputEditText) findViewById(R.id.sgnupLastName);
        inputUsuario = (TextInputEditText) findViewById(R.id.sgnupUsername);
        inputPass = (TextInputEditText) findViewById(R.id.sgnupPassword);
        inputHoraEntrada = (TextInputEditText) findViewById(R.id.sgnupHoraEntrada);
        inputHoraSalida = (TextInputEditText) findViewById(R.id.sgnupHoraSalida);
        buttonSign = (Button) findViewById(R.id.btnSignUp);

        inputHoraEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);


                /*CustomTimePickerDialog customTimePickerDialog = new CustomTimePickerDialog(AgregarCitas.this,R.style.DialogTheme, new CustomTimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaFormateada = (hourOfDay < 10) ? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);
                        String minutoFormateado = (minute < 10) ? String.valueOf("0" + minute) : String.valueOf(minute);
                        edtHora.setText(horaFormateada +":" +minutoFormateado);
                    }
                }, mHour, mMinute, false);
                customTimePickerDialog.show();
                */

                final TimePickerDialog timePickerDialog = new TimePickerDialog(SignUp.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaFormateada = (hourOfDay < 10) ? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);
                        String minutoFormateado = (minute < 10) ? String.valueOf("0" + minute) : String.valueOf(minute);

                        int hora = Integer.parseInt(horaFormateada);
                        if (!(minutoFormateado.equals("00") || minutoFormateado.equals("30"))) {
                            //edtHora.setText(horaFormateada +":" +minutoFormateado);
                            //edtHora.setTextColor(Color.parseColor("#28A530"));
                            //edtHora.setText("Intervalo de 00 y 30 minutos");
                            //edtHora.setTextColor(Color.parseColor("#CF0707"));
                        } else if (!(hourOfDay > 06 && hourOfDay < 18)) {
                            //edtHora.setText("Solo horarios de 7 a.m a 5:00pm");
                            //edtHora.setTextColor(Color.parseColor("#CF0707"));
                        } else {
                            //edtHora.setText("Solo horarios de 7 a.m a 5:00pm");
                            //edtHora.setTextColor(Color.parseColor("#CF0707"));

                            //Toast.makeText(getApplicationContext(), "Horarios de 7:00 A.M a 5:00 p.m." , Toast.LENGTH_LONG).show();
                            //edtHora.setText(horaFormateada + ":" + minutoFormateado);
                            //edtHora.setTextColor(Color.parseColor("#28A530"));
                        }

                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        networking = Conectividad.isNetActive(getSystemService(CONNECTIVITY_SERVICE));
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networking) {
                    Toast.makeText(getApplicationContext(), "Registrando" , Toast.LENGTH_SHORT).show();
                    validadRegistro();
                }else{
                    Toast.makeText(getApplicationContext(), "Registrando2" , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void validadRegistro() {
        Driver driver = new Driver();
        String b = "";
        if (b.equals(inputNombre.getText().toString())) {
            //Toast.makeText(SignUp.this, "Ingrese Nombre", Toast.LENGTH_LONG).show();
            inputNombre.setError("Ingrese nombre");
            return;
        }
        if (b.equals(inputApellido.getText().toString())) {
            //Toast.makeText(SignUp.this, "Ingrese Apellido", Toast.LENGTH_LONG).show();
            inputApellido.setError("Ingrese apellido");
            return;
        }
        if (b.equals(inputUsuario.getText().toString())) {
            //Toast.makeText(SignUp.this, "Ingrese usuario", Toast.LENGTH_LONG).show();
            inputUsuario.setError("Ingrese usuario");
            return;
        }
        if (b.equals(inputPass.getText().toString())) {
            //Toast.makeText(SignUp.this, "Ingrese contraseña", Toast.LENGTH_LONG).show();
            inputPass.setError("Ingrese contraseña");
            return;
        }
        if (b.equals(inputHoraEntrada.getText().toString())) {
            //Toast.makeText(SignUp.this, "Ingrese Correo", Toast.LENGTH_LONG).show();
            inputHoraEntrada.setError("Ingrese hora de entrada");
            return;
        }
        if (b.equals(inputHoraSalida.getText().toString())) {
            //Toast.makeText(SignUp.this, "Ingrese Telefono", Toast.LENGTH_LONG).show();
            inputHoraSalida.setError("Ingrese hora de salida");
            return;
        }

        registrar.execute();
    }

    public void limpiar() {
        inputNombre.setText("");
        inputApellido.setText("");
        inputUsuario.setText("");
        inputPass.setText("");
        inputHoraEntrada.setText("");
        inputHoraSalida.setText("");
        buttonSign.setText("");
    }

    public void reiniciarAsynkProcess() {
        registrar.cancel(true);
        registrar = new Registrar();
    }

    public void dialogo1(){
        AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
                .setView(R.layout.dialog_user_regstd)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent logon = new Intent(SignUp.this, Logon.class);
                        startActivity(logon);
                    }
                })
                .show();
    }
    public void dialogo2(){
        AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
                .setView(R.layout.dialog_user_no_regstd)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public class Registrar extends AsyncTask<String, String, Boolean> {


        @Override
        protected Boolean doInBackground(String... strings) {
            boolean v = false;
            try {
                Driver d = new Driver();
                System.out.println("comienza a leer vistas");
                d.setDriverId(Long.valueOf(0));
                d.setNombreDriver(inputNombre.getText().toString() + " " +inputApellido.getText().toString());
                d.setUsername(inputUsuario.getText().toString());
                d.setPassword(inputPass.getText().toString());
                d.setHoraDeEntrada(ffHora.parse(inputHoraEntrada.getText().toString()));
                d.setHoraDeSalida(ffHora.parse(inputHoraSalida.getText().toString()));
                d.setFechaCreado(new Date());
                d.setHabilitado(true);

                DriverService driverService = new DriverService();
                Driver registrado = driverService.crearDriver(d);
                if (registrado != null) {
                    v = true;
                }
            } catch (Exception ex) {
                System.out.println("*** errrr***: " + ex);
                ex.printStackTrace();
            }
            return v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            try {
                // usuario registrado, compartir en pantalla
                if (res) {
                    dialogo1();
                } else {
                    dialogo2();
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
