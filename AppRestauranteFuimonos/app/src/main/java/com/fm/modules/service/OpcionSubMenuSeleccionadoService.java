package com.fm.modules.service;


import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;

import java.io.Serializable;
import java.util.List;

public class OpcionSubMenuSeleccionadoService extends RestTemplateEntity<OpcionesDeSubMenuSeleccionado> implements Serializable {

    public OpcionSubMenuSeleccionadoService() {
        super(new OpcionesDeSubMenuSeleccionado(), OpcionesDeSubMenuSeleccionado.class, OpcionesDeSubMenuSeleccionado[].class);
    }

    private static final long serialVersionUID = 1L;

    private final String url = Constantes.URL_OPCIONESDESUBMENUSELC;

    public List<OpcionesDeSubMenuSeleccionado> obtenerOpcionSubMenus() {
        List<OpcionesDeSubMenuSeleccionado> lista = getListURL(url);
        return lista;
    }

    public OpcionesDeSubMenuSeleccionado obtenerOpcionSubMenuPorId(Long id) {
        OpcionesDeSubMenuSeleccionado enti = getOneURL(url, id);
        return enti;
    }

    public OpcionesDeSubMenuSeleccionado obtenerOpcionSubMenuPorBody(OpcionesDeSubMenuSeleccionado objeto) {
        OpcionesDeSubMenuSeleccionado enti = getByBodyURL(url, objeto);
        return enti;
    }

    public OpcionesDeSubMenuSeleccionado crearOpcionSubMenu(OpcionesDeSubMenuSeleccionado objeto) {
        OpcionesDeSubMenuSeleccionado enti = createURL(url, objeto);
        return enti;
    }

    public OpcionesDeSubMenuSeleccionado actualizarOpcionSubMenuPorId(OpcionesDeSubMenuSeleccionado objeto) {
        OpcionesDeSubMenuSeleccionado enti = updateURL(url, objeto.getOpcionesDeSubMenuSeleccionadoId(), objeto);
        return enti;
    }

    public void eliminarOpcionSubMenuPorId(Long id) {
        deleteURL(url, id);
    }
}
