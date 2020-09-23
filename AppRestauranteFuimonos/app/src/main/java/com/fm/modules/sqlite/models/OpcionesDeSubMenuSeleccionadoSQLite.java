package com.fm.modules.sqlite.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.sqlite.services.SqlIteServices;

public class OpcionesDeSubMenuSeleccionadoSQLite extends SqlIteServices<OpcionesDeSubMenuSeleccionado> {

    private int DB_VERSION = 1;
    private static final String TABLE_NAME = "opcionessubmenuseleccionado";
    private static final String ID_TABLE = "id_opcion_submenu_seleccionado";
    private static final String CREATE = "CREATE TABLE opcionessubmenuseleccionado (id_opcion_submenu_seleccionado INTEGER,id_opcion_submenu INTEGER,id_platillo_seleccionado INTEGER,nombre TEXT);";
    private static final String DROP = "DROP TABLE IF EXIST opcionessubmenuseleccionado;";

    public OpcionesDeSubMenuSeleccionadoSQLite(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    protected String[] getColumns() {
        return new String[]{ID_TABLE, "id_opcion_submenu", "id_platillo_seleccionado", "nombre"};
    }

    @Override
    protected ContentValues getEntityValues(OpcionesDeSubMenuSeleccionado entity) {
        ContentValues content = new ContentValues();
        content.put(ID_TABLE, entity.getOpcionesDeSubMenuSeleccionadoId());
        content.put("id_opcion_submenu", entity.getOpcionesDeSubMenu().getOpcionesDeSubmenuId());
        content.put("id_platillo_seleccionado", entity.getPlatilloSeleccionado().getPlatilloSeleccionadoId());
        content.put("nombre", entity.getNombre());
        return content;
    }

    @Override
    protected OpcionesDeSubMenuSeleccionado cursorToEntity(Cursor cursor) {
        OpcionesDeSubMenuSeleccionado opcion = new OpcionesDeSubMenuSeleccionado();
        try {
            while (cursor.moveToNext()) {
                opcion.setOpcionesDeSubMenuSeleccionadoId(cursor.getLong(0));
                OpcionesDeSubMenu subMenu = new OpcionesDeSubMenu();
                subMenu.setOpcionesDeSubmenuId(cursor.getLong(1));
                opcion.setOpcionesDeSubMenu(subMenu);
                PlatilloSeleccionado platillo = new PlatilloSeleccionado();
                platillo.setPlatilloSeleccionadoId(cursor.getLong(2));
                opcion.setPlatilloSeleccionado(platillo);
                opcion.setNombre(cursor.getString(3));
            }
        } catch (Exception e) {
        }
        return opcion;
    }

    @Override
    protected String getId() {
        return ID_TABLE;
    }

    @Override
    protected OpcionesDeSubMenuSeleccionado changeIdTable(Long newId, OpcionesDeSubMenuSeleccionado entity) {
        entity.setOpcionesDeSubMenuSeleccionadoId(newId);
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
