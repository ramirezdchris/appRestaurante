package com.fm.modules.service;

import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.PlatilloSeleccionado;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PlatilloSeleccionadoService extends RestTemplateEntity<PlatilloSeleccionado> implements Serializable {

    public PlatilloSeleccionadoService() {
        super(new PlatilloSeleccionado(), PlatilloSeleccionado.class, PlatilloSeleccionado[].class);
        // TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 1L;

    private final String url = Constantes.URL_PLATILLOSELC;

    public List<PlatilloSeleccionado> obtenerPlatilloSeleccionados() {
        List<PlatilloSeleccionado> lista = getListURL(url);
        return lista;
    }

    public PlatilloSeleccionado obtenerPlatilloSeleccionadoPorId(Long id) {
        PlatilloSeleccionado enti = getOneURL(url, id);
        return enti;
    }

    public PlatilloSeleccionado obtenerPlatilloSeleccionadoPorBody(PlatilloSeleccionado objeto) {
        PlatilloSeleccionado enti = getByBodyURL(url, objeto);
        return enti;
    }

    public PlatilloSeleccionado crearPlatilloSeleccionado(PlatilloSeleccionado objeto) {
        PlatilloSeleccionado enti = createURL(url, objeto);
        return enti;
    }

    public PlatilloSeleccionado actualizarPlatilloSeleccionadoPorId(PlatilloSeleccionado objeto) {
        PlatilloSeleccionado enti = updateURL(url, objeto.getPlatilloSeleccionadoId(), objeto);
        return enti;
    }

    public void eliminarPlatilloSeleccionadoPorId(Long id) {
        deleteURL(url, id);
    }


    public List<PlatilloSeleccionado> platilloSeleccionadoPorPedido(String idPedido) {
        List<PlatilloSeleccionado> list = new LinkedList<>();
        try {
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<PlatilloSeleccionado[]> response = restTemplat.getForEntity(Constantes.DOMINIO.concat("/").concat("platilloSeleccionadoPorPedido/").concat(idPedido), PlatilloSeleccionado[].class);

            list = Arrays.asList(response.getBody());
            System.out.println("Contenido Lista: " +list.size());
            System.out.println("Lista: " +list.get(0).toString());
        } catch (Exception e) {
            System.out.println("error absRest getListURL: " + e.getMessage() +" " +e.getClass());
        }
        return list;
    }
}
