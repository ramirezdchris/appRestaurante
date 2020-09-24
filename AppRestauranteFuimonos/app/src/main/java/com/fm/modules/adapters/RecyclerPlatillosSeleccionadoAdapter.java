package com.fm.modules.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.pedidos.OpcPlatilloSeleccionado;
import com.fm.modules.entities.RespuestaPedidosDriver;
import com.fm.modules.models.Image;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.service.ImageService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerPlatillosSeleccionadoAdapter extends RecyclerView.Adapter<RecyclerPlatillosSeleccionadoAdapter.ViewHolder> {

    List<PlatilloSeleccionado> pedidosList;
    Context context;

    SimpleDateFormat ffecha = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fhora = new SimpleDateFormat("HH:mm:ss");

    public RecyclerPlatillosSeleccionadoAdapter(Context context, List<PlatilloSeleccionado> pedidosList){
        this.context = context;
        this.pedidosList = pedidosList;
    }

    @NonNull
    @Override
    public RecyclerPlatillosSeleccionadoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_food3, parent, false);
        return new RecyclerPlatillosSeleccionadoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPlatillosSeleccionadoAdapter.ViewHolder holder, int position) {
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

        AppCompatImageView ivFoodImage;
        AppCompatTextView tvNombrePlatillo;
        AppCompatTextView tvDescripcion;

        AppCompatTextView btn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFoodImage = itemView.findViewById(R.id.ivFoodImage);

            tvNombrePlatillo = itemView.findViewById(R.id.tvFoodName);
            tvDescripcion = itemView.findViewById(R.id.tvFoodDescription);
            btn = itemView.findViewById(R.id.btn);
        }

        public void asignarDatos(final PlatilloSeleccionado platilloSeleccionado) {

            //ivFoodImage.setImageResource(R.drawable.not_found);
            tvNombrePlatillo.setText(platilloSeleccionado.getPlatillo().getNombre());
            tvDescripcion.setText(platilloSeleccionado.getPlatillo().getDescripcion());

            //Logued.pedidoLogued = opciones.getPlatilloSeleccionado().getPedido();
            //Logued.opcionesDeSubMenuSeleccionadoLogued = opciones;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OpcPlatilloSeleccionado.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("idPlatilloSeleccionado", platilloSeleccionado.getPlatilloSeleccionadoId());
                    intent.putExtra("nombrePlatillo", platilloSeleccionado.getPlatillo().getNombre());
                    context.startActivity(intent);
                    //Logued.pedidoLogued = platilloSeleccionado.getPedido();
                    Logued.platillosSeleccionadoLogued = platilloSeleccionado;
                }
            });
            verImagen(platilloSeleccionado.getPlatillo().getImagen());
        }

        protected void verImagen(Long id) {
            CargarImagen cargarImagen = new CargarImagen();
            cargarImagen.execute(id);

        }

        private class CargarImagen extends AsyncTask<Long, String, Image> {

            @Override
            protected Image doInBackground(Long... longs) {
                Image image = null;
                try {
                    if (Logued.imagenesIDs == null) {
                        Logued.imagenes = new ArrayList<>();
                        Logued.imagenesIDs = new ArrayList<>();
                    }
                    List<Integer> integers = Logued.imagenesIDs;
                    if (!integers.contains(longs[0].intValue())) {
                        ImageService imageService = new ImageService();
                        image = imageService.obtenerImagenPorId(longs[0]);
                        if (image != null) {
                            Logued.imagenesIDs.add(image.getId().intValue());
                            Logued.imagenes.add(image);
                        }
                    } else {
                        for (int i = 0; i < integers.size(); i++) {
                            if (integers.get(i) == longs[0].intValue()) {
                                image = Logued.imagenes.get(i);
                            }
                        }
                    }
                } catch (
                        Exception e) {
                    System.out.println("error asynk image: " + e);
                }
                return image;
            }

            @Override
            protected void onPostExecute(Image image) {
                super.onPostExecute(image);
                if (image != null) {
                    Utilities.displayAppCompatImageFoodFromBytea(image.getContent(), ivFoodImage, context);
                    System.out.println("asynk display image ! !!!!!!!!!!!!!!!!");
                }
            }
        }
    }

}
