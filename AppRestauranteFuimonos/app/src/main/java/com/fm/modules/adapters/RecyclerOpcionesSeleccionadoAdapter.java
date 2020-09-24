package com.fm.modules.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.pedidos.OpcPlatilloSeleccionado;
import com.fm.modules.entities.RespuestaPedidosDriver;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerOpcionesSeleccionadoAdapter extends RecyclerView.Adapter<RecyclerOpcionesSeleccionadoAdapter.ViewHolder> {

    List<OpcionesDeSubMenuSeleccionado> pedidosList;
    Context context;

    SimpleDateFormat ffecha = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fhora = new SimpleDateFormat("HH:mm:ss");

    public RecyclerOpcionesSeleccionadoAdapter(Context context, List<OpcionesDeSubMenuSeleccionado> pedidosList){
        this.context = context;
        this.pedidosList = pedidosList;
    }

    @NonNull
    @Override
    public RecyclerOpcionesSeleccionadoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sub_menu_opciones2, parent, false);
        return new RecyclerOpcionesSeleccionadoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerOpcionesSeleccionadoAdapter.ViewHolder holder, int position) {
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

        TextView tvOpcionSubMenu;
        TextView tvOpcion;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //tvNombreRestaurante = itemView.findViewById(R.id.tvNombreRestaurante);
            //tvDireccionCliente = itemView.findViewById(R.id.tvDirecionCliente);
            //tvPrecioPedido = itemView.findViewById(R.id.tvPrecioPedido);
            //btnTomarPedido = itemView.findViewById(R.id.btnTomarPedido);

            tvOpcionSubMenu = itemView.findViewById(R.id.tvOpcionSubMenu);
            tvOpcion = itemView.findViewById(R.id.tvOpcion);
        }

        public void asignarDatos(final OpcionesDeSubMenuSeleccionado opciones){
            if(opciones == null){

            }
            tvOpcionSubMenu.setText(opciones.getOpcionesDeSubMenu().getSubMenu().getTitulo());
            tvOpcion.setText(opciones.getOpcionesDeSubMenu().getNombre());
            Logued.pedidoLogued = opciones.getPlatilloSeleccionado().getPedido();
            Logued.opcionesDeSubMenuSeleccionadoLogued = opciones;
        }

        public void dialog(final RespuestaPedidosDriver res){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Pedido");
            builder.setMessage("¿Desea tomar este pedido?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(context, OpcPlatilloSeleccionado.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("platillo" , res.getPedidoId());
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
