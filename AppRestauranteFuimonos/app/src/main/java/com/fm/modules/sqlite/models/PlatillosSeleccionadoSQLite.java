package com.fm.modules.sqlite.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fm.modules.models.Pedido;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.sqlite.services.SqlIteServices;

public class PlatillosSeleccionadoSQLite extends SqlIteServices<PlatilloSeleccionado> {

    private int DB_VERSION = 1;
    private static final String TABLE_NAME = "platillosseleccionado";
    private static final String ID_TABLE = "id_platillo_seleccionado";
    private static final String CREATE = "CREATE TABLE platillosseleccionado (id_platillo_seleccionado INTEGER,id_platillo INTEGER,id_pedido INTEGER,nombre TEXT,precio TEXT);";
    private static final String DROP = "DROP TABLE IF EXIST platillosseleccionado;";

    public PlatillosSeleccionadoSQLite(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    protected String[] getColumns() {
        return new String[]{ID_TABLE, "id_platillo", "id_pedido", "nombre", "precio"};
    }

    @Override
    protected ContentValues getEntityValues(PlatilloSeleccionado entity) {
        ContentValues content = new ContentValues();
        content.put(ID_TABLE, entity.getPlatilloSeleccionadoId());
        content.put("id_platillo", entity.getPlatillo().getPlatilloId());
        content.put("id_pedido", entity.getPedido().getPedidoId());
        content.put("nombre", entity.getNombre());
        content.put("precio", entity.getPrecio());
        return content;
    }

    @Override
    protected PlatilloSeleccionado cursorToEntity(Cursor cursor) {
        PlatilloSeleccionado opcion = new PlatilloSeleccionado();
        try {
            while (cursor.moveToNext()) {
                opcion.setPlatilloSeleccionadoId(cursor.getLong(0));
                Platillo platillo = new Platillo();
                platillo.setPlatilloId(cursor.getLong(1));
                opcion.setPlatillo(platillo);
                Pedido pedido = new Pedido();
                pedido.setPedidoId(cursor.getLong(2));
                opcion.setPedido(pedido);
                opcion.setNombre(cursor.getString(3));
                opcion.setPrecio(cursor.getDouble(4));
            }
        } catch (Exception e) {
            System.out.println("error cursor to entity: "+e);
        }
        return opcion;
    }

    @Override
    protected String getId() {
        return ID_TABLE;
    }

    @Override
    protected PlatilloSeleccionado changeIdTable(Long newId, PlatilloSeleccionado entity) {
        entity.setPlatilloSeleccionadoId(newId);
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
