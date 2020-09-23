package com.fm.modules.app.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.app.carrito.RestauranteMenuActivity;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.pedidos.Principal;
import com.fm.modules.models.Image;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.OpcionesDeSubMenuSeleccionadoService;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.RestauranteService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class PrincipalRestaurante extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private AppCompatImageView imageView;
    private TextView tvRestauranteNombre;
    private TextView tvRestauranteHorario;
    private Switch aSwitch;

    Restaurante restaurante = Logued.restauranteLogued;

    ActualizarDisponibilidad actualizar = new ActualizarDisponibilidad();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_restaurante);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), RestauranteMenuActivity.class);
                startActivity(i);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();




        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);

        imageView = headerView.findViewById(R.id.imageView);
        tvRestauranteNombre = headerView.findViewById(R.id.tvRestauranteNombre);
        tvRestauranteHorario = headerView.findViewById(R.id.tvRestauranteHorario);
        aSwitch = headerView.findViewById(R.id.swDisponible);

        tvRestauranteNombre.setText(restaurante.getNombreRestaurante());
        tvRestauranteHorario.setText(restaurante.getRepresentante());

        if(restaurante.getDisponible() == null){
            aSwitch.setChecked(true);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getApplicationContext(), "Esta: " +b, Toast.LENGTH_SHORT).show();
                restaurante.setDisponible(b);
                actualizar.execute();
            }
        });

        System.out.println(restaurante);
        verImagen(restaurante.getLogoDeRestaurante());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal_restaurante, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void reiniciarAsysncTask(){
        actualizar.cancel(true);
        actualizar = new ActualizarDisponibilidad();
    }

    public class ActualizarDisponibilidad extends AsyncTask<String, String, List<Restaurante>> {

        @Override
        protected List<Restaurante> doInBackground(String... strings) {
            List<Restaurante> restaurantes = new ArrayList<>();
            //Restaurante restaurante = Logued.restauranteLogued;
            try {
                RestauranteService restauranteService = new RestauranteService();
                restauranteService.actualizarRestaurantePorId(restaurante);
            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return restaurantes;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Restaurante> restaurantes) {
            super.onPostExecute(restaurantes);
            try {
                //Toast.makeText(getApplicationContext(), "Se actualizo" +opcionesDeSubMenuSelec.size(), Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), PrincipalRestaurante.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
                reiniciarAsysncTask();
            }catch (Throwable throwable){
                //System.out.println("Error Activity: " +throwable.getMessage());
                //throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
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
                Utilities.displayAppCompatImageFoodFromBytea(image.getContent(), imageView, getApplicationContext());
                System.out.println("asynk display image ! !!!!!!!!!!!!!!!!");
            }
        }
    }
}