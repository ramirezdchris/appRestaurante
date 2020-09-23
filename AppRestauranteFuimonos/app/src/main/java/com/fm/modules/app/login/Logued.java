package com.fm.modules.app.login;

import com.fm.modules.models.Driver;
import com.fm.modules.models.Image;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.models.Restaurante;

import java.util.List;

public class Logued {

    public static Driver driverLogued;
    public static Restaurante restauranteLogued;
    public static OpcionesDeSubMenuSeleccionado opcionesDeSubMenuSeleccionadoLogued;
    public static Pedido pedidoLogued;

    public static Platillo platillosLogued;
    public static List<Platillo> platillosLoguedList;

    public static PlatilloSeleccionado platillosSeleccionadoLogued;
    public static List<PlatilloSeleccionado> platillosSeleccionadoLoguedList;

    public static List<Image> imagenes;
    public static List<Integer> imagenesIDs;

    public static List<PlatilloSeleccionado> platillosSeleccionadosActuales;
    public static List<OpcionesDeSubMenuSeleccionado> opcionesDeSubMenusEnPlatillosSeleccionados;
    public static Pedido pedidoActual;
}
