package com.fm.modules.app.pedidos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.adapters.RecyclerOpcionesSeleccionadoAdapter;
import com.fm.modules.adapters.RecyclerPlatillosSeleccionadoAdapter;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.ui.PrincipalRestaurante;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.OpcionesDeSubMenuSeleccionadoService;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.PlatilloSeleccionadoService;

import java.util.ArrayList;
import java.util.List;

public class PlatillosSeleccionadoPorPedido extends AppCompatActivity {

    Long idPedido;
    PlatilloSelecPedido platilloSelecPedido = new PlatilloSelecPedido();
    RecyclerView rvPlatillosSeleccionados;
    Button btnTomarPedido;
    ActualizarPedido actualizarPedido = new ActualizarPedido();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_platillos_selecccionado_pedido);

        idPedido = getIntent().getLongExtra("idPedido", 1L);
        rvPlatillosSeleccionados = findViewById(R.id.rvPlatillosSeleccionados);
        btnTomarPedido = findViewById(R.id.btnTomarPedido);

        btnTomarPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Logued.pedidoLogued == null){
                        Toast.makeText(PlatillosSeleccionadoPorPedido.this, "Porfavor verifica si tiene complementos", Toast.LENGTH_SHORT).show();
                    }else{
                        actualizarPedido.execute();
                    }

                }});


        platilloSelecPedido.execute();
    }

    public void reiniciarAsynkProcess() {
        platilloSelecPedido.cancel(true);
        platilloSelecPedido = new PlatilloSelecPedido();
        Logued.pedidoLogued = null;
    }
    public class PlatilloSelecPedido extends AsyncTask<String, String, List<PlatilloSeleccionado>> {

        @Override
        protected List<PlatilloSeleccionado> doInBackground(String... strings) {
            List<PlatilloSeleccionado> platilloSeleccionados = new ArrayList<>();
            Restaurante restaurante = Logued.restauranteLogued;
            try {

                PlatilloSeleccionadoService platilloSeleccionadoService = new PlatilloSeleccionadoService();
                platilloSeleccionados = platilloSeleccionadoService.platilloSeleccionadoPorPedido(String.valueOf(idPedido));
                //Logued.pedidoLogued = platilloSeleccionados;

            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return platilloSeleccionados;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<PlatilloSeleccionado> platilloSeleccionados) {
            super.onPostExecute(platilloSeleccionados);
            try {
                if (!platilloSeleccionados.isEmpty()){
                    //tvFoodName.setText(opcionesDeSubMenuSelec.get(0).getPlatilloSeleccionado().getNombre());

                    RecyclerPlatillosSeleccionadoAdapter adapter = new RecyclerPlatillosSeleccionadoAdapter(getApplicationContext(), platilloSeleccionados);
                    rvPlatillosSeleccionados.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                    rvPlatillosSeleccionados.setAdapter(adapter);
                    reiniciarAsynkProcess();
                    adapter.notifyDataSetChanged();
                    //Toast.makeText(getApplicationContext(), "Platillos de un pedido Cargados" +platilloSeleccionados.size(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Platillos de un pedido Cargados No Cargados" +platilloSeleccionados.size(), Toast.LENGTH_SHORT).show();
                }
            }catch (Throwable throwable){
                System.out.println("Error Activity: " +throwable.getMessage());
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


    public class ActualizarPedido extends AsyncTask<String, String, List<OpcionesDeSubMenuSeleccionado>> {

        @Override
        protected List<OpcionesDeSubMenuSeleccionado> doInBackground(String... strings) {
            List<OpcionesDeSubMenuSeleccionado> opcionesDeSubMenuSelec = new ArrayList<>();
            Restaurante restaurante = Logued.restauranteLogued;
            try {
                OpcionesDeSubMenuSeleccionadoService opcionesDeSubMenuService = new OpcionesDeSubMenuSeleccionadoService();
                PedidoService pedidoService = new PedidoService();
                Logued.pedidoLogued.setStatus(1);
                //Pedido pedido = new Pedido();
                //Logued.pedidoLogued = pedido;

                //Logued.opcionesDeSubMenuSeleccionadoLogued.setNombre("papas");
                //opcionesDeSubMenuService.actializarOpcionesDeSubMenuSeleccionadoPorId(Logued.opcionesDeSubMenuSeleccionadoLogued);
                pedidoService.actualizarPedidoPorId(Logued.pedidoLogued);
                //pedidoService.actualizarPedidoDriver(pedido);
                //Logued.pedidoLogued = new Pedido();
                //pedido = new Pedido();
            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return opcionesDeSubMenuSelec;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<OpcionesDeSubMenuSeleccionado> opcionesDeSubMenuSelec) {
            super.onPostExecute(opcionesDeSubMenuSelec);
            try {
                //Toast.makeText(getApplicationContext(), "Se actualizo" +opcionesDeSubMenuSelec.size(), Toast.LENGTH_SHORT).show();
                reiniciarAsynkProcess();
                Intent intent = new Intent(getApplicationContext(), PrincipalRestaurante.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }catch (Throwable throwable){
                System.out.println("Error Activity: " +throwable.getMessage());
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
