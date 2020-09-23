package com.fm.modules.app.ui.gallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.adapters.RecyclerPedidosRestauranteAdapter;
import com.fm.modules.adapters.RecyclerPlatillosPorRestaurante;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.ui.gallery.*;
import com.fm.modules.entities.RespuestaPedidosDriver;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.PlatilloService;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private RecyclerView rvPlatillosRestaurante;
    PlatillosRestaurante platillosRestaurante = new PlatillosRestaurante();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        rvPlatillosRestaurante = root.findViewById(R.id.rvPlatillosRestaurante);
        platillosRestaurante.execute();
        return root;
    }

    public void reiniciarAsynkProcess() {
        platillosRestaurante.cancel(true);

        platillosRestaurante = new PlatillosRestaurante();
    }

    public class PlatillosRestaurante extends AsyncTask<String, String, List<Platillo>> {

        @Override
        protected List<Platillo> doInBackground(String... strings) {
            List<Platillo> platilloPorRestaurante = new ArrayList<>();
            Restaurante restaurante = Logued.restauranteLogued;
            try {

                PlatilloService platilloService = new PlatilloService();
                platilloPorRestaurante = platilloService.obtenerPlatilloPorRestaurante(String.valueOf(restaurante.getRestauranteId()));
                Logued.platillosLoguedList = platilloPorRestaurante;

            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return platilloPorRestaurante;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Platillo> platilloPorRestaurante) {
            super.onPostExecute(platilloPorRestaurante);
            try {
                if (!platilloPorRestaurante.isEmpty()){
                    RecyclerPlatillosPorRestaurante adapter = new RecyclerPlatillosPorRestaurante(getContext(), platilloPorRestaurante, getChildFragmentManager());
                    rvPlatillosRestaurante.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    rvPlatillosRestaurante.setAdapter(adapter);
                    reiniciarAsynkProcess();
                    adapter.notifyDataSetChanged();
                    //Toast.makeText(getContext(), "Pedidso Cargados" +platilloPorRestaurante.size(), Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getContext(), "Pedidos No Cargados" +platilloPorRestaurante.size(), Toast.LENGTH_SHORT).show();
                    reiniciarAsynkProcess();
                    RecyclerPlatillosPorRestaurante adapter = new RecyclerPlatillosPorRestaurante(getContext(), platilloPorRestaurante, getChildFragmentManager());
                    rvPlatillosRestaurante.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvPlatillosRestaurante.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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

    public void myExecute(){
        reiniciarAsynkProcess();
        platillosRestaurante.execute();
    }
}