package com.fm.modules.app.ui.slideshow;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.adapters.RecyclerPedidosRestauranteAdapter;
import com.fm.modules.adapters.RecyclerPedidosRestauranteDriverAdapter;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.ui.home.HomeFragment;
import com.fm.modules.entities.RespuestaPedidosDriver;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.PedidoService;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private RecyclerView rvPedidosParaDriver;

    private PedidosParaDriver pedidosParaDriver = new PedidosParaDriver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pedidosParaDriver.execute();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        rvPedidosParaDriver = root.findViewById(R.id.rvPedidosParaDriver);
        return root;
    }

    public void reiniciarAsynkProcess() {
        pedidosParaDriver.cancel(true);
        pedidosParaDriver = new PedidosParaDriver();

    }

    public class PedidosParaDriver extends AsyncTask<String, String, List<RespuestaPedidosDriver>> {

        @Override
        protected List<RespuestaPedidosDriver> doInBackground(String... strings) {
            List<RespuestaPedidosDriver> pedidos = new ArrayList<>();
            Restaurante restaurante = Logued.restauranteLogued;
            try {
                PedidoService pedidoService = new PedidoService();
                pedidos = pedidoService.obtenerPedidosParaEntregarDriver(String.valueOf(restaurante.getRestauranteId()));

            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return pedidos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<RespuestaPedidosDriver> pedidos) {
            super.onPostExecute(pedidos);
            try {
                if (!pedidos.isEmpty()){
                    RecyclerPedidosRestauranteDriverAdapter adapter = new RecyclerPedidosRestauranteDriverAdapter(getContext(), pedidos);
                    rvPedidosParaDriver.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvPedidosParaDriver.setAdapter(adapter);
                    //rvPedidosRestaurante.setRefreshing(false);
                    //Toast.makeText(getContext(), "Pedidso Cargados" +pedidos.size(), Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getContext(), "Pedidos No Cargados" +pedidos.size(), Toast.LENGTH_SHORT).show();
                    //reiniciarAsynkProcess();
                    RecyclerPedidosRestauranteAdapter adapter = new RecyclerPedidosRestauranteAdapter(getContext(), pedidos);
                    rvPedidosParaDriver.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvPedidosParaDriver.setAdapter(adapter);
                    //swRefreshPedidos.setRefreshing(false);
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