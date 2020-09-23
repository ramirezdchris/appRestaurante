package com.fm.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.carrito.GlobalRestaurantes;
import com.fm.modules.app.carrito.PlatillosActivity;
import com.fm.modules.models.Menu;

import java.util.List;

public class MenuItemViewAdapter extends ItemViewAdapterImagen<Menu> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;
    private FragmentActivity fragmentActivity;


    public MenuItemViewAdapter(List<Menu> lista, Context context, int resource) {
        super(lista, context);
        this.context = context;
        this.resource = resource;
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        final HolderMenu holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(R.layout.holder_menu, parent, false);
                holder = new HolderMenu(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderMenu) convertView.getTag();
                System.out.println("Holder Inactivo");
            }

            final Menu menu = (Menu) getItem(position);

            holder.imageMenu.setImageResource(R.drawable.ic_flan);
            holder.txtMenu.setText(menu.getNombreMenu());
            holder.imageMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalRestaurantes.menuSeleccionado = menu;
                    //showFragment(new PlatillosActivity());
                    Intent i = new Intent(context, PlatillosActivity.class);
                    i.putExtra("idMenu", menu.getMenuId().intValue());
                    context.startActivity(i);
                }
            });

        } catch (Exception e) {

        }
        return convertView;
    }

    private void showFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
