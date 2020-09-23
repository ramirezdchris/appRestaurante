package com.fm.modules.sqlite.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fm.modules.sqlite.services.SqlIteServices;

public class DireccionesSQLite extends SqlIteServices<Direcciones> {

    private int DB_VERSION = 1;
    private static final String TABLE_NAME = "direcciones";
    private static final String ID_TABLE = "id_direccion";
    private static final String CREATE = "CREATE TABLE direcciones (id_direccion INTEGER,nombre_direccion TEXT,coordenadas TEXT);";
    private static final String DROP = "DROP TABLE IF EXIST direcciones;";

    public DireccionesSQLite(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    protected String[] getColumns() {
        return new String[]{ID_TABLE, "nombre_direccion", "coordenadas"};
    }

    @Override
    protected ContentValues getEntityValues(Direcciones entity) {
        ContentValues content = new ContentValues();
        content.put(ID_TABLE, entity.getIdDireccion());
        content.put("nombre_direccion", entity.getNombreDireccion());
        content.put("coordenadas", entity.getCoordenadas());
        return content;
    }

    @Override
    protected Direcciones cursorToEntity(Cursor cursor) {
        Direcciones entty = new Direcciones();
        try {
            while (cursor.moveToNext()) {
                entty.setIdDireccion(cursor.getInt(0));
                entty.setNombreDireccion(cursor.getString(1));
                entty.setCoordenadas(cursor.getString(2));
            }
        } catch (Exception e) {
        }
        return entty;
    }

    @Override
    protected String getId() {
        return ID_TABLE;
    }

    @Override
    protected Direcciones changeIdTable(Long newId, Direcciones entity) {
        entity.setIdDireccion(newId.intValue());
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
