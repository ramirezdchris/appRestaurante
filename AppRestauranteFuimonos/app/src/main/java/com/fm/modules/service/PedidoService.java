package com.fm.modules.service;

import android.widget.Toast;

import com.fm.modules.app.login.Logued;
import com.fm.modules.entities.RespuestaPedidosDriver;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.RespuestaGenerica;
import com.fm.modules.models.SubMenu;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PedidoService extends RestTemplateEntity<Pedido> implements Serializable {

    private static final long serialVersionUID = 1L;

    public PedidoService() {
        super(new Pedido(), Pedido.class, Pedido[].class);
    }

    private final String url = Constantes.URL_PEDIDO;

    public List<Pedido> obtenerPedidos() {
        List<Pedido> lista = getListURL(url);
        return lista;
    }

    public Pedido obtenerPedidoPorId(Long id) {
        Pedido enti = getOneURL(url, id);
        return enti;
    }

    public Pedido obtenerPedidoPorBody(Pedido objeto) {
        Pedido enti = getByBodyURL(url, objeto);
        return enti;
    }

    public Pedido crearPedido(Pedido objeto) {
        Pedido enti = createURL(url, objeto);
        return enti;
    }

    public Pedido actualizarPedidoPorId(Pedido objeto) {
        Pedido enti = updateURL(url, objeto.getPedidoId(), objeto);
        return enti;
    }

    public void eliminarPedidoPorId(Long id) {
        deleteURL(url, id);
    }


    public List<RespuestaPedidosDriver> obtenerPedidoDriver(String idDriver) {
        List<RespuestaPedidosDriver> list = new LinkedList<>();
        try {
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<RespuestaPedidosDriver[]> response = restTemplat.getForEntity(Constantes.DOMINIO.concat("/").concat("obtenerPedidoDriver/").concat(idDriver), RespuestaPedidosDriver[].class);

            list = Arrays.asList(response.getBody());
            System.out.println("BODYDDYDDYDYD: " +response.getBody().toString());
            System.out.println("Contenido Lista: " +list.size());
            System.out.println("Lista: " +list.get(0).toString());
        } catch (Exception e) {
            System.out.println("error absRest getListURL: " + e.getMessage() +" " +e.getClass());
        }
        return list;
    }

    public List<RespuestaPedidosDriver> obtenerPedidoAEntregar(String idDriver) {
        List<RespuestaPedidosDriver> list = new LinkedList<>();
        try {
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<RespuestaPedidosDriver[]> response = restTemplat.getForEntity(Constantes.DOMINIO.concat("/").concat("obtenerPedidoAEntregar/").concat(idDriver), RespuestaPedidosDriver[].class);

            list = Arrays.asList(response.getBody());
            System.out.println("Contenido Lista: " +list.size());
            System.out.println("Lista: " +list.get(0).toString());
        } catch (Exception e) {
            System.out.println("error absRest getListURL: " + e.getMessage() +" " +e.getClass());
        }
        return list;
    }

    public List<RespuestaPedidosDriver> obtenerPedidosRestaurante(String idRestaurante) {
        List<RespuestaPedidosDriver> list = new LinkedList<>();
        try {
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<RespuestaPedidosDriver[]> response = restTemplat.getForEntity(Constantes.DOMINIO.concat("/").concat("obtenerPedidoARestaurante/").concat(idRestaurante), RespuestaPedidosDriver[].class);

            list = Arrays.asList(response.getBody());
            System.out.println("Contenido Lista: " +list.size());
            System.out.println("Lista: " +list.get(0).toString());
        } catch (Exception e) {
            System.out.println("error absRest getListURL: " + e.getMessage() +" " +e.getClass());
        }
        return list;
    }
    public List<RespuestaPedidosDriver> obtenerPedidosParaEntregarDriver(String idRestaurante) {
        List<RespuestaPedidosDriver> list = new LinkedList<>();
        try {
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<RespuestaPedidosDriver[]> response = restTemplat.getForEntity(Constantes.DOMINIO.concat("/").concat("obtenerPedidosParaEntregarDriver/").concat(idRestaurante), RespuestaPedidosDriver[].class);

            list = Arrays.asList(response.getBody());
            System.out.println("Contenido Lista: " +list.size());
            System.out.println("Lista: " +list.get(0).toString());
        } catch (Exception e) {
            System.out.println("error absRest getListURL: " + e.getMessage() +" " +e.getClass());
        }
        return list;
    }

    public int actualizarPedidoDriver(Pedido pedido) {
        List<RespuestaPedidosDriver> list = new LinkedList<>();
        int respuesta = 0;
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
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<RespuestaGenerica> response = restTemplat.exchange(Constantes.DOMINIO.concat("/").concat("pedidos/actulizarStatus/").concat(String.valueOf(pedido.getStatus())).concat("/").concat(pedido.getPedidoId().toString()), HttpMethod.GET,entity, RespuestaGenerica.class);
            //list = Arrays.asList(response.getBody());
            RespuestaGenerica res = response.getBody();
            respuesta = Integer.parseInt(res.getCodigo());

            System.out.println("Codigo para Actualizar: " +respuesta);
        } catch (Exception e) {
            System.out.println("error absRest getListURL Actualizar: " + e.getMessage() +" " +e.getClass());
        }
        return respuesta;
    }
}
