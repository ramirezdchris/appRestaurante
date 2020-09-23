package com.fm.modules.service;

import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OpcionesDeSubMenuSeleccionadoService extends RestTemplateEntity<OpcionesDeSubMenuSeleccionado> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public OpcionesDeSubMenuSeleccionadoService() {
        super(new OpcionesDeSubMenuSeleccionado(), OpcionesDeSubMenuSeleccionado.class, OpcionesDeSubMenuSeleccionado[].class);
    }

    private final String url = Constantes.URL_OPCIONESDESUBMENUSELC;

    public List<OpcionesDeSubMenuSeleccionado> obtenerOpcionesDeSubMenuSeleccionado() {
        List<OpcionesDeSubMenuSeleccionado> lista = getListURL(url);
        return lista;
    }

    public OpcionesDeSubMenuSeleccionado obtenerOpcionesDeSubMenuSeleccionadoPorId(Long id) {
        OpcionesDeSubMenuSeleccionado enti = getOneURL(url, id);
        return enti;
    }

    public OpcionesDeSubMenuSeleccionado obtenerOpcionesDeSubMenuSeleccionadoPorBody(OpcionesDeSubMenuSeleccionado objeto) {
        OpcionesDeSubMenuSeleccionado enti = getByBodyURL(url, objeto);
        return enti;
    }

    public OpcionesDeSubMenuSeleccionado crearOpcionesDeSubMenuSeleccionadoPorId(OpcionesDeSubMenuSeleccionado objeto) {
        OpcionesDeSubMenuSeleccionado enti = createURL(url, objeto);
        return enti;
    }

    public OpcionesDeSubMenuSeleccionado actializarOpcionesDeSubMenuSeleccionadoPorId(OpcionesDeSubMenuSeleccionado objeto) {
        OpcionesDeSubMenuSeleccionado enti = updateURL(url, objeto.getOpcionesDeSubMenuSeleccionadoId(), objeto);
        return enti;
    }

    public void eliminarOpcionesDeSubMenuSeleccionadoPorId(Long id) {
        deleteURL(url, id);
    }

    public List<OpcionesDeSubMenuSeleccionado> obtenerOpcionesPorPlatillo(String idPlatilloSeleccionado) {
        List<OpcionesDeSubMenuSeleccionado> list = new LinkedList<>();
        try {
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<OpcionesDeSubMenuSeleccionado[]> response = restTemplat.getForEntity(Constantes.DOMINIO.concat("/").concat("opcionesDePlatilloSeleccionado/").concat(idPlatilloSeleccionado), OpcionesDeSubMenuSeleccionado[].class);

            list = Arrays.asList(response.getBody());
            System.out.println("Contenido Lista: " +list.size());
            System.out.println("Lista: " +list.get(0).toString());
        } catch (Exception e) {
            System.out.println("error absRest getListURL: " + e.getMessage() +" " +e.getClass());
        }
        return list;
    }
}
