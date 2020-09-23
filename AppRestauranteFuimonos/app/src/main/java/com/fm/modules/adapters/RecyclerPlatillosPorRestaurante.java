package com.fm.modules.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.pedidos.Principal;
import com.fm.modules.app.ui.PrincipalRestaurante;
import com.fm.modules.app.ui.gallery.GalleryFragment;
import com.fm.modules.entities.RespuestaPedidosDriver;
import com.fm.modules.models.Driver;
import com.fm.modules.models.Image;
import com.fm.modules.models.Menu;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.MenuService;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.PlatilloService;
import com.fm.modules.service.RestauranteService;

import java.util.ArrayList;
import java.util.List;

public class RecyclerPlatillosPorRestaurante  extends RecyclerView.Adapter<RecyclerPlatillosPorRestaurante.ViewHolder> {

    private List<Platillo> items;
    private Context context;
    private FragmentManager frg;


    private Menu buscarMenu = new Menu();
    private ObtenerMenu obtenerMenu = new ObtenerMenu();
    private ActualizarPlatillo actualizarPlatillo = new ActualizarPlatillo();
    private ActualizarPlatillo2 actualizarPlatillo2 = new ActualizarPlatillo2();
    private Platillo obtPlatillo;

    private Platillo updPlatillo = new Platillo();

    public RecyclerPlatillosPorRestaurante(Context context, List<Platillo> platillos, FragmentManager frg){
        this.items = platillos;
        this.context = context;
        this.frg = frg;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_food2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.asignarDatos(items.get(position));
        if(!items.get(position).getDisponible()){
            holder.cvPlatillos.setBackgroundColor(Color.parseColor("#FFFFD3D3"));
        }else{
            holder.cvPlatillos.setBackgroundColor(Color.parseColor("#FFDAFAD5"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        CardView cvPlatillos;
        AppCompatImageView ivFoodImage;
        TextView tvFoodName;
        TextView tvFoodDescription;
        TextView tvFoodPrice;
        AppCompatTextView btnAdd;

        public ViewHolder(View view){
            super(view);
            cvPlatillos = view.findViewById(R.id.cvPlatillo);
            ivFoodImage = view.findViewById(R.id.ivFoodImage);
            tvFoodName = view.findViewById(R.id.tvFoodName);
            tvFoodDescription = view.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = view.findViewById(R.id.tvPrecioPedido);
            btnAdd = view.findViewById(R.id.btn);
        }

        public void asignarDatos(final Platillo platillo) {
            //ivFoodImage.setImageResource(R.drawable.not_found);
            tvFoodName.setText(platillo.getNombre());
            tvFoodDescription.setText(platillo.getDescripcion());
            tvFoodPrice.setText("$ " +String.valueOf(platillo.getPrecioBase()));
            //btnAdd.setText(platillo.getOrden());
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog(platillo);
                }
            });
            /*ivFoodImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, Platillo.class);
                    i.putExtra("idRestaurante", platillo.getPlatilloId());
                    System.out.println("ID RESTAURANTE: " + platillo.getPlatilloId());
                    context.startActivity(i);
                }
            });*/
            verImagen(platillo.getImagen());

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

    public void dialog(final Platillo platillo){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Platillo");
        builder.setMessage("¿Qué desea hacer con el platillo " +platillo.getNombre() +"?").setPositiveButton("Habilitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                buscarMenu.setMenuId(platillo.getMenu().getMenuId());
                obtPlatillo = platillo;
                obtenerMenu.execute();
                actualizarPlatillo.execute();


                Intent intent = new Intent(context, PrincipalRestaurante.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                //reiniciarAsynkProcess();
                Toast.makeText(context, "Cambiando estado..", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Deshabilidatar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(context, "..", Toast.LENGTH_SHORT).show();
                buscarMenu.setMenuId(platillo.getMenu().getMenuId());
                obtPlatillo = platillo;
                obtenerMenu.execute();
                actualizarPlatillo2.execute();


                Intent intent = new Intent(context, PrincipalRestaurante.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }).show();
    }


    public void reiniciarAsynkProcess() {
        obtenerMenu.cancel(true);
        obtenerMenu = new ObtenerMenu();

    }

    public class ObtenerMenu extends AsyncTask<String, String, List<Menu>> {

        @Override
        protected List<Menu> doInBackground(String... strings) {
            List<Menu> menuList = new ArrayList<>();
            //Driver driver = Logued.driverLogued;
            try {

                MenuService menuService = new MenuService();
                Menu menu = menuService.obtenerMenuPorId(buscarMenu.getMenuId());

                buscarMenu = menu;

            }catch (Exception e){
                System.out.println("Error en UnderThreash ObtenerRestaurante:" +e.getMessage() +" " +e.getClass());
            }
            return menuList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Menu> menu) {
            super.onPostExecute(menu);
            try {
                if (!menu.isEmpty()){

                }else{
                    //Toast.makeText(context, "Pedidos No Cargados" +restaurantes.size(), Toast.LENGTH_SHORT).show();
                }
                reiniciarAsynkProcess();
                //Toast.makeText(context, "Restaurante: " +buscarRestaurante.getRepresentante(), Toast.LENGTH_SHORT).show();
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

    public class ActualizarPlatillo extends AsyncTask<String, String, List<Platillo>> {

        @Override
        protected List<Platillo> doInBackground(String... strings) {
            List<Platillo> usuarios = new ArrayList<>();
            Driver driver = Logued.driverLogued;
            try {

                PlatilloService platilloService = new PlatilloService();

                updPlatillo.setPlatilloId(obtPlatillo.getPlatilloId());
                updPlatillo.setMenu(buscarMenu);
                updPlatillo.setNombre(obtPlatillo.getNombre());
                updPlatillo.setPrecioBase(obtPlatillo.getPrecioBase());
                updPlatillo.setImagen(obtPlatillo.getImagen());
                updPlatillo.setDescripcion(obtPlatillo.getDescripcion());
                updPlatillo.setOrden(obtPlatillo.getOrden());
                updPlatillo.setDisponible(true);
                platilloService.actualizarPlatillo(updPlatillo);
                //Intent intent = new Intent(context, GalleryFragment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //context.startActivity(intent);
                //frg.beginTransaction().replace(R.id.nav_host_fragment_container, frg).commit();

                reiniciarAsynkProcess();

            }catch (Exception e){
                System.out.println("Error en UnderThreash ActualizarPedidoce:" +e.getMessage() +" " +e.getClass());
                System.out.println(updPlatillo.toString());
            }
            return usuarios;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Platillo> platillos) {
            super.onPostExecute(platillos);
            try {
                reiniciarAsynkProcess();


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

    public class ActualizarPlatillo2 extends AsyncTask<String, String, List<Platillo>> {

        @Override
        protected List<Platillo> doInBackground(String... strings) {
            List<Platillo> usuarios = new ArrayList<>();
            Driver driver = Logued.driverLogued;
            try {

                PlatilloService platilloService = new PlatilloService();

                updPlatillo.setPlatilloId(obtPlatillo.getPlatilloId());
                updPlatillo.setMenu(buscarMenu);
                updPlatillo.setNombre(obtPlatillo.getNombre());
                updPlatillo.setPrecioBase(obtPlatillo.getPrecioBase());
                updPlatillo.setImagen(obtPlatillo.getImagen());
                updPlatillo.setDescripcion(obtPlatillo.getDescripcion());
                updPlatillo.setOrden(obtPlatillo.getOrden());
                updPlatillo.setDisponible(false);
                platilloService.actualizarPlatillo(updPlatillo);
                //Intent intent = new Intent(context, GalleryFragment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //context.startActivity(intent);
                //frg.beginTransaction().replace(R.id.nav_host_fragment_container, frg).commit();

                reiniciarAsynkProcess();

            }catch (Exception e){
                System.out.println("Error en UnderThreash ActualizarPedidoce:" +e.getMessage() +" " +e.getClass());
                System.out.println(updPlatillo.toString());
            }
            return usuarios;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Platillo> platillos) {
            super.onPostExecute(platillos);
            try {
                reiniciarAsynkProcess();


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
