package com.fm.modules.service;


import com.fm.modules.entities.RespuestaPedidosDriver;
import com.fm.modules.models.Platillo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PlatilloService extends RestTemplateEntity<Platillo> implements Serializable {

    private static final long serialVersionUID = 1L;

    public PlatilloService() {
        super(new Platillo(), Platillo.class, Platillo[].class);
    }

    private final String url = Constantes.URL_PLATILLO;

    public List<Platillo> obtenerPlatillos() {
        List<Platillo> lista = getListURL(url);
        return lista;
    }

    public Platillo obtenerPlatilloPorId(Long id) {
        Platillo enti = getOneURL(url, id);
        return enti;
    }

    public Platillo obtenerPlatilloPorBody(Platillo objeto) {
        Platillo enti = getByBodyURL(url, objeto);
        return enti;
    }

    public Platillo crearPlatillo(Platillo objeto) {
        Platillo enti = createURL(url, objeto);
        return enti;
    }

    public Platillo actualizarPlatillo(Platillo objeto) {
        Platillo enti = updateURL(url, objeto.getPlatilloId(), objeto);
        return enti;
    }

    public void eliminarPlatilloPorId(Long id) {
        deleteURL(url, id);
    }


    public List<Platillo> obtenerPlatilloPorRestaurante(String idRestaurante) {
        List<Platillo> list = new LinkedList<>();
        try {
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<Platillo[]> response = restTemplat.getForEntity(Constantes.DOMINIO.concat("/").concat("platilloPorRestaurante/").concat(idRestaurante), Platillo[].class);

            list = Arrays.asList(response.getBody());
            System.out.println("Contenido Lista: " +list.size());
            System.out.println("Lista: " +list.get(0).toString());
        } catch (Exception e) {
            System.out.println("error absRest getListURL: " + e.getMessage() +" " +e.getClass());
        }
        return list;
    }
}
