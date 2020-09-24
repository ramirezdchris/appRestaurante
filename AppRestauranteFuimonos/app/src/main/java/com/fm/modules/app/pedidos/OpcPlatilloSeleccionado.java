package com.fm.modules.app.pedidos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.adapters.RecyclerOpcionesSeleccionadoAdapter;
import com.fm.modules.adapters.RecyclerPlatillosPorRestaurante;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.ui.PrincipalRestaurante;
import com.fm.modules.app.ui.gallery.GalleryFragment;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.OpcionesDeSubMenuSeleccionadoService;

import com.fm.modules.service.PedidoService;
import com.fm.modules.service.PlatilloService;

import java.util.ArrayList;
import java.util.List;

public class OpcPlatilloSeleccionado extends AppCompatActivity {

    private OpcionesDeSubMenuSeleccionado opcionesDeSubMenuSeleccionado;

    Long idPlatilloSeleccionado;
    AppCompatTextView tvFoodName;
    RecyclerView rvComplementosArea;

    OpcionesPlatillo opcionesPlatillo = new OpcionesPlatillo();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_food_complements2);

        idPlatilloSeleccionado = getIntent().getLongExtra("idPlatilloSeleccionado", 1L);
        Toast.makeText(this, "Platillo id: " +idPlatilloSeleccionado, Toast.LENGTH_SHORT).show();

        tvFoodName = findViewById(R.id.tvFoodName);
        rvComplementosArea = findViewById(R.id.rvComplementsArea);

        opcionesPlatillo.execute();

    }


    public void reiniciarAsynkProcess() {
        opcionesPlatillo.cancel(true);
        opcionesPlatillo = new OpcionesPlatillo();
    }

    public class OpcionesPlatillo extends AsyncTask<String, String, List<OpcionesDeSubMenuSeleccionado>> {

        @Override
        protected List<OpcionesDeSubMenuSeleccionado> doInBackground(String... strings) {
            List<OpcionesDeSubMenuSeleccionado> opcionesDeSubMenuSelec = new ArrayList<>();
            Restaurante restaurante = Logued.restauranteLogued;
            try {
                OpcionesDeSubMenuSeleccionadoService opcionesDeSubMenuService = new OpcionesDeSubMenuSeleccionadoService();
                opcionesDeSubMenuSelec = opcionesDeSubMenuService.obtenerOpcionesPorPlatillo(String.valueOf(idPlatilloSeleccionado));

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
                if (!opcionesDeSubMenuSelec.isEmpty()){
                    tvFoodName.setText(opcionesDeSubMenuSelec.get(0).getPlatilloSeleccionado().getPlatillo().getNombre());

                    RecyclerOpcionesSeleccionadoAdapter adapter = new RecyclerOpcionesSeleccionadoAdapter(getApplicationContext(), opcionesDeSubMenuSelec);
                    rvComplementosArea.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                    rvComplementosArea.setAdapter(adapter);
                    reiniciarAsynkProcess();
                    adapter.notifyDataSetChanged();
                    //Toast.makeText(getApplicationContext(), "Pedidso Cargados" +opcionesDeSubMenuSelec.size(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Pedidos No Cargados" +opcionesDeSubMenuSelec.size(), Toast.LENGTH_SHORT).show();
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


}
