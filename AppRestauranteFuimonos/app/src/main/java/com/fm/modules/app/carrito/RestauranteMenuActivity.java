package com.fm.modules.app.carrito;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.fm.apprestaurantefuimonos.R;
import com.fm.modules.adapters.MenuItemViewAdapter;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Image;
import com.fm.modules.models.Menu;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.OpcionesDeSubMenuService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RestauranteMenuActivity extends AppCompatActivity {

    UnderThreads underThreads = new UnderThreads();

    private boolean conectec;
    private TabLayout menuTab;
    private ViewPager viewPager;
    private Bundle bundle;
    private Intent intent;
    private RecyclerView rvFoods;
    private ListView listView;
    private AppCompatImageView imagenLogo;


    int idRestaurante;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_restaurant_menu);
        //idRestaurante = getIntent().getIntExtra("idRestaurante", 0);
        //rvFoods = findViewById(R.id.rvFoods);
        //initTab();
        idRestaurante = Logued.restauranteLogued.getRestauranteId().intValue();
        listView = (ListView) findViewById(R.id.lvMenus);
        imagenLogo = (AppCompatImageView) findViewById(R.id.ivRestaurantLogo);
        if (isNetActive()) {
            cargarDatos();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        verLogo();
    }

    public void verLogo() {
        Image image = null;
        List<Integer> integers = Logued.imagenesIDs;
        Restaurante res = GlobalRestaurantes.restauranteSelected;
        if (res != null) {
            if (integers != null && !integers.isEmpty()) {
                for (int i = 0; i < integers.size(); i++) {
                    if (res.getLogoDeRestaurante().intValue() == integers.get(i)) {
                        image = Logued.imagenes.get(i);
                    }
                }
            }
        }
        if (image != null) {
            Utilities.displayAppCompatImageFromBytea(image.getContent(), imagenLogo, RestauranteMenuActivity.this);
        } else {
            Utilities.displayAppCompatImageFromBytea(null, imagenLogo, RestauranteMenuActivity.this);
        }
    }

    private void cargarDatos() {
        final Date anteriorDate = GlobalRestaurantes.horaActualizado;
        Date actualDate = new Date();
        if (anteriorDate == null) {
            underThreads.execute();
            actualDate = getHour(new Date());
        } else {
            if (anteriorDate.getTime() < actualDate.getTime()) {
                underThreads.execute();
            }
        }
    }

    public Date getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR);
        calendar.set(year, month, day, hour, 0, 0);
        return calendar.getTime();
    }

    public void reiniciarAsync() {
        underThreads.cancel(true);
        underThreads = new UnderThreads();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reiniciarAsync();
    }

    public boolean isNetActive() {
        boolean c = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                c = true;
            }
        } catch (Exception e) {
            Log.e("error", "" + "error al comprobar conexion");
            Log.e("error", "" + e);
            c = false;
        }
        return c;
    }

    public class UnderThreads extends AsyncTask<String, String, List<Menu>> {


        @Override
        protected List<Menu> doInBackground(String... strings) {
            List<Menu> subM = new ArrayList<>();
            try {
                OpcionesDeSubMenuService opcionesDeSubMenuService = new OpcionesDeSubMenuService();
                List<OpcionesDeSubMenu> opciones = opcionesDeSubMenuService.obtenerOpcionesDeSubMenu();
                if (!opciones.isEmpty()) {
                    System.out.println("********* opciones cargadas ***************");
                    List<SubMenu> subMenus = new ArrayList<>();
                    List<Integer> ints = new ArrayList<>();
                    for (OpcionesDeSubMenu op : opciones) {
                        if (!ints.contains(op.getSubMenu().getSubMenuId().intValue())) {
                            subMenus.add(op.getSubMenu());
                            ints.add(op.getSubMenu().getSubMenuId().intValue());
                        }
                    }
                    List<Platillo> platillos = new ArrayList<>();
                    ints = new ArrayList<>();
                    for (SubMenu sb : subMenus) {
                        if (!ints.contains(sb.getPlatillo().getPlatilloId().intValue())) {
                            platillos.add(sb.getPlatillo());
                            ints.add(sb.getPlatillo().getPlatilloId().intValue());
                        }
                    }
                    List<Menu> menus = new ArrayList<>();
                    ints = new ArrayList<>();
                    for (Platillo pa : platillos) {
                        if (!ints.contains(pa.getMenu().getMenuId().intValue())) {
                            menus.add(pa.getMenu());
                            ints.add(pa.getMenu().getMenuId().intValue());
                        }
                    }
                    System.out.println("subMenus " + subMenus.size());
                    System.out.println("platillos " + platillos.size());
                    System.out.println("menus " + menus.size());
                    GlobalRestaurantes.opcionesDeSubMenuList = opciones;
                    if (!subMenus.isEmpty()) {
                        GlobalRestaurantes.subMenuList = subMenus;
                    }
                    if (!platillos.isEmpty()) {
                        GlobalRestaurantes.platilloList = platillos;
                    }
                    if (!menus.isEmpty()) {
                        GlobalRestaurantes.menuList = menus;
                        subM = menus;
                    }

                }
            } catch (Exception e) {
                System.out.println("Error en UnderThreash:" + e.getMessage() + " " + e.getClass());
            }
            return subM;
        }

        @Override
        protected void onPostExecute(List<Menu> menus) {
            super.onPostExecute(menus);
            try {
                if (!menus.isEmpty()) {
                    List<Menu> menuList = new ArrayList<>();
                    for (Menu m : menus) {
                        if (m.getRestaurante().getRestauranteId().intValue() == idRestaurante) {
                            if (!menuList.contains(m)) {
                                menuList.add(m);
                            }
                        }
                    }
                    MenuItemViewAdapter adapter = new MenuItemViewAdapter(menuList, RestauranteMenuActivity.this, R.layout.holder_menu);
                    listView.setAdapter(adapter);
                    Toast.makeText(RestauranteMenuActivity.this, "Menus Cargados" + menus.size(), Toast.LENGTH_SHORT).show();
                }
            } catch (Throwable throwable) {
                System.out.println("Error Activity: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
