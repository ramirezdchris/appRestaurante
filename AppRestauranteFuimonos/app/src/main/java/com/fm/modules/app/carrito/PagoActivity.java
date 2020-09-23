package com.fm.modules.app.carrito;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Driver;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.service.DriverService;
import com.fm.modules.service.OpcionSubMenuSeleccionadoService;
import com.fm.modules.service.OpcionesDeSubMenuService;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.PlatilloSeleccionadoService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PagoActivity extends AppCompatActivity {

    TextView txtTotal;
    TextView txtDireccion1;
    TextView txtDireccion2;
    Button btnTarjeta;
    Button btnEfectivo;
    Button btnPagar;
    Pedido pedido;
    List<PlatilloSeleccionado> platilloSeleccionados;
    List<OpcionesDeSubMenuSeleccionado> opcionesSeleccionadas;
    GuardarPedidoAsync guardarPedidoAsync = new GuardarPedidoAsync();

    @Override
    protected void onRestart() {
        super.onRestart();
        reiniciarAsynk();
    }

    private void reiniciarAsynk() {
        guardarPedidoAsync.cancel(true);
        guardarPedidoAsync = new GuardarPedidoAsync();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);
        txtTotal = (TextView) findViewById(R.id.pagoTxtTotal);
        txtDireccion1 = (TextView) findViewById(R.id.pagoTxtDireccion);
        txtDireccion2 = (TextView) findViewById(R.id.pagoTxtDireccion2);
        btnTarjeta = (Button) findViewById(R.id.pagoBtnTarjeta);
        btnEfectivo = (Button) findViewById(R.id.pagoBtnEfectivo);
        btnPagar = (Button) findViewById(R.id.pagoBtnPagar);
        btnTarjeta.setBackgroundColor(getResources().getColor(R.color.lightGray));
        btnEfectivo.setBackgroundColor(getResources().getColor(R.color.lightGray));
        pedido = Logued.pedidoActual;
        btnPagar.setEnabled(false);
        btnPagar.setBackgroundColor(getResources().getColor(R.color.lightGray));
        platilloSeleccionados = new ArrayList<>();
        opcionesSeleccionadas = new ArrayList<>();
        mostrarDatos();
        listenerBotones();
    }

    public void obtenerPedido() {
        if (pedido != null) {
            platilloSeleccionados = Logued.platillosSeleccionadosActuales;
            opcionesSeleccionadas = Logued.opcionesDeSubMenusEnPlatillosSeleccionados;
        }
    }

    private void listenerBotones() {
        btnTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pedido != null) {
                    pedido.setFormaDePago("Tarjeta");
                    btnPagar.setEnabled(true);
                    btnPagar.setBackgroundColor(getResources().getColor(R.color.orange));
                }
                Logued.pedidoActual = pedido;
                btnTarjeta.setBackgroundColor(getResources().getColor(R.color.lime));
                btnEfectivo.setBackgroundColor(getResources().getColor(R.color.lightGray));
            }
        });
        btnEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pedido != null) {
                    pedido.setFormaDePago("Tarjeta");
                    btnPagar.setEnabled(true);
                    btnPagar.setBackgroundColor(getResources().getColor(R.color.orange));
                }
                Logued.pedidoActual = pedido;
                btnTarjeta.setBackgroundColor(getResources().getColor(R.color.lightGray));
                btnEfectivo.setBackgroundColor(getResources().getColor(R.color.lime));
            }
        });
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTarjeta.setEnabled(false);
                btnEfectivo.setEnabled(false);
                obtenerPedido();
                guardarPedidoAsync.execute();
            }
        });
    }

    private void mostrarDatos() {
        try {
            List<PlatilloSeleccionado> lista = Logued.platillosSeleccionadosActuales;
            double total1 = 0.00;
            double total2 = 0;
            double total3 = 0;
            double descuento = 0;
            double total4 = 0;
            String direccion = "";
            if (lista != null) {
                if (!lista.isEmpty()) {
                    for (PlatilloSeleccionado pl : lista) {
                        total1 = total1 + pl.getPrecio();
                    }
                    //descuento = lista.get(0).getPlatillo().getMenu().getRestaurante().getDescuento();
                    direccion = lista.get(0).getPedido().getDireccion();
                }
            }
            String[] strings = direccion.split(" ; ", 7);
            if (strings.length > 3) {
                txtDireccion1.setText(strings[0]);
                String dir2 = strings[1] + " , " + strings[3];
                txtDireccion2.setText(dir2);
            }
            total2 = total1 * 0.13;
            total3 = total1 * 0.05;
            // descuento = descuento * total1;
            total4 = total1 + total2 + total3 - descuento;
            pedido.setTotalDePedido(total1);
            pedido.setTotalEnRestautante(total4);
            pedido.setTotalDeCargosExtra(total3);
            pedido.setTotalEnRestautanteSinComision(total1 + total2 - descuento);
            DecimalFormat decimalFormat = new DecimalFormat("$ #,##0.00");
            txtTotal.setText(String.valueOf(decimalFormat.format(total4)));
        } catch (Exception e) {
            System.out.println("error carrito: " + e);
        }
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

    public class GuardarPedidoAsync extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            int b = 0;
            try {
                if (pedido != null) {
                    if (!platilloSeleccionados.isEmpty()) {
                        PedidoService pedidoService = new PedidoService();
                        pedido.setPedidoId(0L);
                        pedido.setFechaOrdenado(new Date());
                        DriverService driverService = new DriverService();
                        List<Driver> drivers = driverService.obtenerDrivers();
                        if (!drivers.isEmpty()) {
                            pedido.setDrivers(drivers.get(0));
                            pedido.setStatus(0);
                        }
                        if (isNetActive()) {
                            Pedido per = pedidoService.crearPedido(pedido);
                            if (per != null) {
                                b = 1;
                                GlobalCarrito.pedidoRegistrado = per;
                            } else {
                                b = 2;
                            }
                            if (b == 1) {
                                PlatilloSeleccionadoService platilloSeleccionadoService = new PlatilloSeleccionadoService();
                                for (PlatilloSeleccionado pla : platilloSeleccionados) {
                                    pla.setPedido(per);
                                    PlatilloSeleccionado pls = platilloSeleccionadoService.crearPlatilloSeleccionado(pla);
                                    if (pls != null) {
                                        b = 3;
                                        if (!opcionesSeleccionadas.isEmpty()) {
                                            OpcionSubMenuSeleccionadoService opcionSubMenuSeleccionadoService = new OpcionSubMenuSeleccionadoService();
                                            for (OpcionesDeSubMenuSeleccionado opc : opcionesSeleccionadas) {
                                                if (opc.getPlatilloSeleccionado().getPlatilloSeleccionadoId().intValue() == pla.getPlatilloSeleccionadoId().intValue()) {
                                                    opc.setPlatilloSeleccionado(pls);
                                                    OpcionesDeSubMenuSeleccionado pp = opcionSubMenuSeleccionadoService.crearOpcionSubMenu(opc);
                                                    if (pp != null) {
                                                        b = 3;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        b = 2;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("error asynk guardar pedido: " + e);
            }
            return b;
        }

        @Override
        protected void onPostExecute(Integer b) {
            super.onPostExecute(b);
            int procesed = b;
            switch (procesed) {
                case 0:
                    AlertDialog dialog = new AlertDialog.Builder(PagoActivity.this)
                            .setView(R.layout.dialog_server_err)
                            .setCancelable(true)
                            .setPositiveButton("Continuar", null)
                            .show();
                    break;
                case 2:
                    Intent intent = new Intent(PagoActivity.this, PedidoNoRegistrado.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case 3:
                    Intent intent2 = new Intent(PagoActivity.this, PedidoRegistrado.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                    break;
            }
            reiniciarAsynk();
        }
    }
}