package com.fm.modules.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.login.Logon;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.pedidos.PlatillosSeleccionadoPorPedido;
import com.fm.modules.app.pedidos.Principal;
import com.fm.modules.entities.RespuestaPedidosDriver;
import com.fm.modules.models.Driver;
import com.fm.modules.models.Menu;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.RespuestaGenerica;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.DriverService;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.RestauranteService;
import com.fm.modules.service.UsuarioService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerPedidosRestauranteAdapter extends RecyclerView.Adapter<RecyclerPedidosRestauranteAdapter.ViewHolder> {

    List<RespuestaPedidosDriver> pedidosList;
    Context context;

    SimpleDateFormat ffecha = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fhora = new SimpleDateFormat("HH:mm:ss");

    public RecyclerPedidosRestauranteAdapter(Context context, List<RespuestaPedidosDriver> pedidosList){
        this.context = context;
        this.pedidosList = pedidosList;
    }

    @NonNull
    @Override
    public RecyclerPedidosRestauranteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_pedidos, parent, false);
        return new RecyclerPedidosRestauranteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPedidosRestauranteAdapter.ViewHolder holder, int position) {
        //if(this.pedidosList.isEmpty()){
        //    Toast.makeText(this.context, "No tienes pedidos a entregar", Toast.LENGTH_SHORT).show();
        //}else{
            holder.asignarDatos(pedidosList.get(position));
        //}

    }

    @Override
    public int getItemCount() {
        return pedidosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvNombreRestaurante;
        AppCompatTextView tvDireccionCliente;
        AppCompatTextView tvPrecioPedido;
        AppCompatTextView btnTomarPedido;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombreRestaurante = itemView.findViewById(R.id.tvNombreRestaurante);
            tvDireccionCliente = itemView.findViewById(R.id.tvDirecionCliente);
            tvPrecioPedido = itemView.findViewById(R.id.tvPrecioPedido);
            btnTomarPedido = itemView.findViewById(R.id.btnTomarPedido);
        }

        public void asignarDatos(final RespuestaPedidosDriver res){
            tvNombreRestaurante.setText(res.getRestaurante());
            tvDireccionCliente.setText(res.getDireccion());
            tvPrecioPedido.setText("$" +String.valueOf(res.getTotalDePedido()));
            btnTomarPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog(res);
                }
            });
        }

        public void dialog(final RespuestaPedidosDriver res){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Pedido");
            builder.setMessage("¿Desea tomar este pedido?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(context, PlatillosSeleccionadoPorPedido.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("idPedido" , res.getPedidoId());
                    //intent.putExtra("platillo" , res.get);
                    context.startActivity(intent);
                    //reiniciarAsynkProcess();
                    //Toast.makeText(context, "Ha decidido tomar este pedido..", Toast.LENGTH_SHORT).show();

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Toast.makeText(context, "No tomocesa este pedido..", Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
    }



}
