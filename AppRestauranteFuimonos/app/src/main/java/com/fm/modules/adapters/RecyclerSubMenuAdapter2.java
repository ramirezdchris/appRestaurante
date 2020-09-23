package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.OpcionSubMenuService;

import java.util.ArrayList;
import java.util.List;

public class RecyclerSubMenuAdapter2 extends RecyclerView.Adapter<RecyclerSubMenuAdapter2.ViewHolder> {

    private Context context;
    private List<SubMenu> subMenuList;
    private List<OpcionesDeSubMenu> opcionesDeSubMenus;

    private OpcionSubMenuService opcionesSubMenuService = new OpcionSubMenuService();

    public RecyclerSubMenuAdapter2(List<SubMenu> subMenuList, List<OpcionesDeSubMenu> opcionesDeSubMenus, Context context) {
        this.context = context;
        this.subMenuList = subMenuList;
        this.opcionesDeSubMenus = opcionesDeSubMenus;
    }

    @NonNull
    @Override
    public RecyclerSubMenuAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sub_menu2, parent, false);
        return new RecyclerSubMenuAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerSubMenuAdapter2.ViewHolder holder, int position) {
        //holder.asignarDatos(opcionesDeSubMenus.get(position));
        System.out.println("POSICION: " +position);

        holder.tvSubMenu.setText(subMenuList.get(position).getTitulo());

        List<OpcionesDeSubMenu> opciones = new ArrayList<>();
        Long idSub = subMenuList.get(position).getSubMenuId();
        for(OpcionesDeSubMenu listaOpcionesSubMenu : opcionesDeSubMenus){
            Long id = listaOpcionesSubMenu.getSubMenu().getSubMenuId();
            if(id.intValue() == idSub.intValue()){
                opciones.add(listaOpcionesSubMenu);
            }
        }
        RecyclerOpcionesDeSubMenuAdapter adapter = new RecyclerOpcionesDeSubMenuAdapter(opciones);
        holder.rvOpcionesDeSubMenu.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.rvOpcionesDeSubMenu.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return subMenuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubMenu;
        RecyclerView rvOpcionesDeSubMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubMenu = itemView.findViewById(R.id.tvSubMenu2);
            rvOpcionesDeSubMenu = itemView.findViewById(R.id.rvOpcionesDeSubMenu);
        }


    }

}
