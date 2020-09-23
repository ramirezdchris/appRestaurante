package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.PlatilloSeleccionado;

import java.util.ArrayList;
import java.util.List;

public class RecyclerPlatillosSeleccionadosAdapter extends RecyclerView.Adapter<RecyclerPlatillosSeleccionadosAdapter.ViewHolder> {

    private List<PlatilloSeleccionado> items;
    private Context context;

    public RecyclerPlatillosSeleccionadosAdapter(List<PlatilloSeleccionado> platillosSeleccionadoList, Context context) {
        this.items = platillosSeleccionadoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_food_selected, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int posicion = position;
        final PlatilloSeleccionado p = items.get(position);
        holder.asignarDatos(p);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<OpcionesDeSubMenuSeleccionado> opcionesModificadas = Logued.opcionesDeSubMenusEnPlatillosSeleccionados;

                List<Integer> posicionesEliminar = new ArrayList<>();
                for (int i = 0; i < opcionesModificadas.size(); i++) {
                    OpcionesDeSubMenuSeleccionado opcion = opcionesModificadas.get(i);
                    if (opcion.getPlatilloSeleccionado().getPlatilloSeleccionadoId().intValue() == p.getPlatilloSeleccionadoId().intValue()) {
                        posicionesEliminar.add(i);
                    }
                }
                if (!posicionesEliminar.isEmpty()) {
                    for (int indice : posicionesEliminar) {
                        posicionesEliminar.remove(indice);
                    }
                }
                items.remove(posicion);
                Logued.opcionesDeSubMenusEnPlatillosSeleccionados = opcionesModificadas;
                Logued.platillosSeleccionadosActuales = items;
                notifyItemRemoved(posicion);
                notifyItemRangeChanged(posicion, items.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoodName;
        TextView tvFoodDescription;
        TextView tvFoodPrice;
        TextView tvFoodQuantity;
        AppCompatTextView btnDelete;

        public ViewHolder(View view) {
            super(view);
            tvFoodName = view.findViewById(R.id.tvFoodNameSelected);
            tvFoodDescription = view.findViewById(R.id.tvFoodDescriptionSelected);
            tvFoodPrice = view.findViewById(R.id.tvFoodPriceSelected);
            tvFoodQuantity = view.findViewById(R.id.tvFoodQuantitySelected);
            btnDelete = view.findViewById(R.id.btnDelPlatillo);
        }

        public void asignarDatos(final PlatilloSeleccionado platilloSeleccionado) {
            tvFoodName.setText(platilloSeleccionado.getNombre());

            List<OpcionesDeSubMenuSeleccionado> ll = Logued.opcionesDeSubMenusEnPlatillosSeleccionados;
            StringBuilder stb = new StringBuilder();
            stb.append("");
            double adicionales = 0;
            if (ll != null) {
                if (!ll.isEmpty()) {
                    for (OpcionesDeSubMenuSeleccionado op : ll) {
                        if (op.getPlatilloSeleccionado().getPlatilloSeleccionadoId().intValue() == platilloSeleccionado.getPlatilloSeleccionadoId().intValue()) {
                            stb.append(op.getNombre());
                            // adicionales = adicionales + op.getOpcionesDeSubMenu().getPrecio();
                        }
                    }
                }
            }
            /*
             * en caso se agregue cantidad a la tabla platillo seleccionado
             * se usara el codigo que esta como comentario en este metodo
             * */
            String opcioness = stb.toString();
            tvFoodDescription.setText(opcioness);
            String precio = "$ " + platilloSeleccionado.getPrecio();
            String cantidad = "x" + platilloSeleccionado.getCantidad();
            tvFoodPrice.setText(precio);
            tvFoodQuantity.setText(cantidad);
        }
    }
}
