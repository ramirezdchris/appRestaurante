package com.fm.modules.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Image;
import com.fm.modules.service.ImageService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class ItemViewAdapterImagen<T> extends BaseAdapter {

    private List<T> lista = new LinkedList<>();

    public List<T> getLista() {
        return lista;
    }

    private Context context;


    public ItemViewAdapterImagen(List<T> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @Override
    public int getCount() {
        return getLista().size();
    }

    @Override
    public Object getItem(int position) {
        return getLista().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return absView(position, convertView, parent);
    }

    public abstract View absView(int position, View convertView, ViewGroup parent);

    public class HolderImagen {

        TextView title;
        TextView description;
        ImageView imageView;

        public HolderImagen(View view) {
            title = (TextView) view.findViewById(R.id.itemTitlei);
            description = (TextView) view.findViewById(R.id.itemTexti);
            imageView = (ImageView) view.findViewById(R.id.itemImage);
        }
    }

    public class HolderTexto {
        TextView title;
        TextView description;

        public HolderTexto(View view) {
            title = (TextView) view.findViewById(R.id.itemTitle);
            description = (TextView) view.findViewById(R.id.itemText);
        }
    }

    public class HolderRestaurantes {
        AppCompatImageView ivOutstandingImage;
        AppCompatImageView ivRestaurantLogo;
        TextView tvRestaurantName;
        TextView tvMinimalMount;
        TextView tvLabelMinimalMount;

        public HolderRestaurantes(View view) {
            ivOutstandingImage = (AppCompatImageView) view.findViewById(R.id.ivOutstandingImage);
            ivRestaurantLogo = (AppCompatImageView) view.findViewById(R.id.ivRestaurantLogo);
            tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantName);
            tvMinimalMount = (TextView) view.findViewById(R.id.tvMinimalMount);
            tvLabelMinimalMount = (TextView) view.findViewById(R.id.tvLabelMinimalMount);
        }

        protected void verImagen(Long id) {
            CargarImagen cargarImagen = new CargarImagen();
            cargarImagen.execute(id);

        }

        protected void verLogo(Long id) {
            CargarLogo cargarLogo = new CargarLogo();
            cargarLogo.execute(id);
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
                    Utilities.displayImageFromBytea(image.getContent(), ivOutstandingImage, context);
                    System.out.println("asynk display image ! !!!!!!!!!!!!!!!!");
                }
            }
        }

        private class CargarLogo extends AsyncTask<Long, String, Image> {

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
                } catch (Exception e) {
                    System.out.println("error asynk image: " + e);
                }
                return image;
            }

            @Override
            protected void onPostExecute(Image image) {
                super.onPostExecute(image);
                if (image != null) {
                    Utilities.displayImageFromBytea(image.getContent(), ivRestaurantLogo, context);
                    System.out.println("asynk display image ! !!!!!!!!!!!!!!!!");
                }
            }
        }
    }

    public class HolderItemFood {
        AppCompatImageView ivFoodImage;
        TextView tvFoodName;
        TextView tvFoodDescription;
        TextView tvFoodPrice;
        Button btnAdd;

        public HolderItemFood(View view) {
            ivFoodImage = view.findViewById(R.id.ivFoodImage);
            tvFoodName = view.findViewById(R.id.tvFoodName);
            tvFoodDescription = view.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = view.findViewById(R.id.tvFoodPrice);
            btnAdd = view.findViewById(R.id.btnAddPlatillo);
        }
    }

    public class HolderMenu {

        public ImageView imageMenu;
        public TextView txtMenu;

        public HolderMenu(View view) {
            imageMenu = view.findViewById(R.id.hdMenuImage);
            txtMenu = view.findViewById(R.id.hdMenuText);
        }
    }

    public class HolderItemCategorias {
        public AppCompatImageView catImage;
        public AppCompatTextView catName;
        public AppCompatTextView catCountRestaurants;

        public HolderItemCategorias(View view) {
            catImage = view.findViewById(R.id.ivCategoryIcon1);
            catName = view.findViewById(R.id.tvCategoryName1);
            catCountRestaurants = view.findViewById(R.id.tvRestaurantsCount1);
        }
    }

    public class HolderItemOption {
        public AppCompatImageView image;
        public AppCompatTextView name;

        public HolderItemOption(View view) {
            image = (AppCompatImageView) view.findViewById(R.id.ivOptionIcon);
            name = (AppCompatTextView) view.findViewById(R.id.tvOptionName);
        }
    }
}
