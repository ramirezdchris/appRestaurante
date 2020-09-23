package com.fm.modules.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.carrito.GlobalRestaurantes;
import com.fm.modules.models.OpcionesDeSubMenu;

import java.util.List;

public class RecyclerOpcionesDeSubMenuAdapter extends RecyclerView.Adapter<RecyclerOpcionesDeSubMenuAdapter.ViewHolder> {

    List<OpcionesDeSubMenu> opcioneSubMenus;

    public RecyclerOpcionesDeSubMenuAdapter(List<OpcionesDeSubMenu> opcionesSubMenus){
        this.opcioneSubMenus = opcionesSubMenus;
    }

    @NonNull
    @Override
    public RecyclerOpcionesDeSubMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sub_menu_opciones, parent, false);
        return new RecyclerOpcionesDeSubMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerOpcionesDeSubMenuAdapter.ViewHolder holder, final int position) {
        holder.tvOpcionSubMenu.setText(opcioneSubMenus.get(position).getNombre());
        holder.rdbOpcionSubMenuSelec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<OpcionesDeSubMenu> list = GlobalRestaurantes.opcionesDeSubMenusSeleccionados;
                list.add(opcioneSubMenus.get(position));
                GlobalRestaurantes.opcionesDeSubMenusSeleccionados = list;
            }
        });
    }

    @Override
    public int getItemCount() {
        return opcioneSubMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvOpcionSubMenu;
        RadioButton rdbOpcionSubMenuSelec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOpcionSubMenu = itemView.findViewById(R.id.tvOpcionSubMenu);
            rdbOpcionSubMenuSelec = itemView.findViewById(R.id.rdbOpcionSubMenuSelec);
        }
    }


}
