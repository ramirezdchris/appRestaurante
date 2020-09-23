package com.fm.modules.sqlite.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fm.modules.models.Driver;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.Usuario;
import com.fm.modules.sqlite.services.SqlIteServices;

import java.text.SimpleDateFormat;

public class PedidoSQLite extends SqlIteServices<Pedido> {

    private int DB_VERSION = 1;
    private static final String TABLE_NAME = "pedidos";
    private static final String ID_TABLE = "id_pedido";
    private static final String CREATE = "CREATE TABLE pedidos (id_pedido INTEGER,id_restaurante INTEGER,id_usuario INTEGER," +
            "id_driver INTEGER,status TEXT,forma_de_pago TEXT,total_de_pedido REAL,total_en_restaurante REAL," +
            "total_de_cargos_extra REAL,total_en_restaurante_sin_comision REAL,pedido_pagado INTEGER,fecha_ordenado TEXT," +
            "tiempo_prom_entrega TEXT,pedido_entregado INTEGER,notas TEXT, tiempo_adicional TEXT,direccion TEXT);";
    private static final String DROP = "DROP TABLE IF EXIST pedidos;";

    public PedidoSQLite(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    protected String[] getColumns() {
        return new String[]{ID_TABLE, "id_restaurante", "id_usuario", "id_driver", "status",
                "forma_de_pago", "total_de_pedido", "total_en_restaurante", "total_de_cargos_extra",
                "total_en_restaurante_sin_comision", "pedido_pagado", "fecha_ordenado", "tiempo_prom_entrega",
                "pedido_entregado", "notas", "tiempo_adicional", "direccion"};
    }

    @Override
    protected ContentValues getEntityValues(Pedido entity) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues content = new ContentValues();
        content.put(ID_TABLE, entity.getPedidoId());
        content.put("id_restaurante", entity.getRestaurante().getRestauranteId());
        content.put("id_usuario", entity.getUsuario().getUsuarioId());
        content.put("id_driver", entity.getDrivers().getDriverId());
        content.put("status", entity.getStatus());
        content.put("forma_de_pago", entity.getFormaDePago());
        content.put("total_de_pedido", entity.getTotalDePedido());
        content.put("total_en_restaurante", entity.getTotalEnRestautante());
        content.put("total_de_cargos_extra", entity.getTotalDeCargosExtra());
        content.put("total_en_restaurante_sin_comision", entity.getTotalEnRestautanteSinComision());
        // boolean
        content.put("pedido_pagado", entity.isPedidoEntregado() ? 1 : 0);
        content.put("fecha_ordenado", simpleDateFormat.format(entity.getFechaOrdenado()));
        content.put("tiempo_prom_entrega", entity.getTiempoPromedioEntrega().toString());
        content.put("pedido_entregado", entity.isPedidoEntregado() ? 1 : 0);
        // boolean
        content.put("notas", entity.getNotas());
        content.put("tiempo_adicional", entity.getTiempoAdicional().toString());
        content.put("direccion", entity.getDireccion());
        return content;
    }

    @Override
    protected Pedido cursorToEntity(Cursor cursor) {
        Pedido opcion = new Pedido();
        try {
            while (cursor.moveToNext()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleHourFormat = new SimpleDateFormat("HH:mm:ss");
                opcion.setPedidoId(cursor.getLong(0));
                Restaurante restaurante = new Restaurante();
                restaurante.setRestauranteId(cursor.getLong(1));
                opcion.setRestaurante(restaurante);
                Usuario usuario = new Usuario();
                usuario.setUsuarioId(cursor.getLong(2));
                opcion.setUsuario(usuario);
                Driver driver = new Driver();
                driver.setDriverId(cursor.getLong(3));
                opcion.setDrivers(driver);
                opcion.setStatus(cursor.getInt(4));
                opcion.setFormaDePago(cursor.getString(5));
                opcion.setTotalDePedido(cursor.getDouble(6));
                opcion.setTotalEnRestautante(cursor.getDouble(7));
                opcion.setTotalDeCargosExtra(cursor.getDouble(8));
                opcion.setTotalEnRestautanteSinComision(cursor.getDouble(9));
                opcion.setPedidoPagado(cursor.getInt(10) > 0);
                opcion.setFechaOrdenado(simpleDateFormat.parse(cursor.getString(11)));
                opcion.setTiempoPromedioEntrega(simpleHourFormat.parse(cursor.getString(12)));
                opcion.setPedidoEntregado(cursor.getInt(13) > 0);
                opcion.setNotas(cursor.getString(14));
                opcion.setTiempoAdicional(simpleHourFormat.parse(cursor.getString(15)));
                opcion.setDireccion(cursor.getString(16));
            }
        } catch (Exception e) {
            System.out.println("error pedidos bi id sqlite: " + e);
        }
        return opcion;
    }

    @Override
    protected String getId() {
        return ID_TABLE;
    }

    @Override
    protected Pedido changeIdTable(Long newId, Pedido entity) {
        entity.setPedidoId(newId);
        return entity;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getCreateTableScript() {
        return CREATE;
    }

    @Override
    protected String getDropTableScript() {
        return DROP;
    }

}
