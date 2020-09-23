package com.fm.modules.service;

import com.fm.modules.models.RespuestaGenerica;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.Usuario;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;

public class RestauranteService extends RestTemplateEntity<Restaurante> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public RestauranteService() {
        super(new Restaurante(), Restaurante.class, Restaurante[].class);
    }

    private final String url = Constantes.URL_RESTAURANTES;

    public List<Restaurante> obtenerRestaurantes() {
        List<Restaurante> lista = getListURL(url);
        return lista;
    }

    public Restaurante obtenerRestaurantePorId(Long id) {
        Restaurante enti = getOneURL(url, id);
        return enti;
    }

    public Restaurante obtenerRestaurantePorBody(Restaurante objeto) {
        Restaurante enti = getByBodyURL(url, objeto);
        return enti;
    }

    public Restaurante crearRestaurante(Restaurante objeto) {
        Restaurante enti = createURL(url, objeto);
        return enti;
    }

    public Restaurante actualizarRestaurantePorId(Restaurante objeto) {
        Restaurante enti = updateURL(url, objeto.getRestauranteId(), objeto);
        return enti;
    }

    public void eliminarRestaurantePorId(Long id) {
        deleteURL(url, id);
    }

    public int signInRestaurante(Restaurante restaurante){
        int i = 0;
        /*
         * 0 = server problem
         * -1 = bad username
         * -2 = bad password
         * x = signed in
         * -4 = inactive
         * */
        try {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
                    @Override
                    protected boolean hasError(HttpStatus statusCode) {
                        // desactivar el error loader
                        // al recivir una respuesta de tipo error
                        return false;
                    }
                });
                HttpEntity<Object> entity = new HttpEntity<Object>(headers);
                ResponseEntity<RespuestaGenerica> responses = restTemplate.exchange(Constantes.DOMINIO.concat("/restauranteLogin/").concat(restaurante.getUsername()).concat("/").concat(restaurante.getPassword()), HttpMethod.GET,entity, RespuestaGenerica.class);
                RespuestaGenerica response = responses.getBody();
                if (response != null){
                    if ("1".equals(response.getCodigo())){}
                    switch (response.getCodigo()){
                        case "1": i = -1;break;
                        case "2": i = -2;break;
                        case "3": i = Integer.parseInt(response.getMensaje());break;
                        case "4": i = -4;break;
                    }
                }
            } catch (Exception e) {
                System.out.println("error absRest s: " + e);

            }
        }catch (Exception e){}
        return i;
    }
}
