package com.fm.modules.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.pedidos.PlatillosSeleccionadoPorPedido;
import com.fm.modules.app.pedidos.PlatillosSeleccionadoRevisar;
import com.fm.modules.entities.RespuestaPedidosDriver;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerPedidosRestauranteDriverAdapter extends RecyclerView.Adapter<RecyclerPedidosRestauranteDriverAdapter.ViewHolder> {

    List<RespuestaPedidosDriver> pedidosList;
    Context context;

    SimpleDateFormat ffecha = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fhora = new SimpleDateFormat("HH:mm:ss");

    public RecyclerPedidosRestauranteDriverAdapter(Context context, List<RespuestaPedidosDriver> pedidosList){
        this.context = context;
        this.pedidosList = pedidosList;
    }

    @NonNull
    @Override
    public RecyclerPedidosRestauranteDriverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_pedidos, parent, false);
        return new RecyclerPedidosRestauranteDriverAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPedidosRestauranteDriverAdapter.ViewHolder holder, int position) {
        //if(this.pedidosList.isEmpty()){
        //    Toast.makeText(this.context, "No tienes pedidos a entregar", Toast.LENGTH_SHORT).show();
        //}else{
        if(pedidosList.get(position).getStatus() == 2){
            holder.asignarDatos(pedidosList.get(position));
            holder.tvDireccionCliente.setText("Driver Asignado: " +pedidosList.get(position).getDriver());
            //holder.btnTomarPedido.setText(pedidosList.get(position).getDriver());
            holder.cvPedido.setBackgroundColor(Color.parseColor("#F4CBA2"));
            holder.btnTomarPedido.setVisibility(View.INVISIBLE);
        }else{
            holder.asignarDatos(pedidosList.get(position));
        }

        //}

    }

    @Override
    public int getItemCount() {
        return pedidosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cvPedido;
        AppCompatTextView tvNombreRestaurante;
        AppCompatTextView tvDireccionCliente;
        AppCompatTextView tvPrecioPedido;
        AppCompatTextView btnTomarPedido;
        AppCompatTextView btnNumeroOrden;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvPedido = itemView.findViewById(R.id.cvPedido);
            tvNombreRestaurante = itemView.findViewById(R.id.tvNombreRestaurante);
            tvDireccionCliente = itemView.findViewById(R.id.tvDirecionCliente);
            tvPrecioPedido = itemView.findViewById(R.id.tvPrecioPedido);
            btnTomarPedido = itemView.findViewById(R.id.btnTomarPedido);
            btnNumeroOrden = itemView.findViewById(R.id.btnNumeroOrden);
        }

        public void asignarDatos(final RespuestaPedidosDriver res){
            tvNombreRestaurante.setText(res.getRestaurante());
            tvDireccionCliente.setText(res.getDireccion());
            tvPrecioPedido.setText("$" +String.valueOf(res.getTotalDePedido()));
            btnNumeroOrden.setText("Orden #" +res.getPedidoId());
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
                    Intent intent = new Intent(context, PlatillosSeleccionadoRevisar.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
