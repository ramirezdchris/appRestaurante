package com.fm.modules.sqlite.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fm.modules.sqlite.services.SqlIteServices;

public class TarjetasSQLite extends SqlIteServices<Tarjetas> {

    private int DB_VERSION = 1;
    private static final String TABLE_NAME = "tarjetas";
    private static final String ID_TABLE = "id_tarjeta";
    private static final String CREATE = "CREATE TABLE tarjetas (id_tarjeta INTEGER,nombre_banco TEXT,numero TEXT);";
    private static final String DROP = "DROP TABLE IF EXIST tarjetas;";

    public TarjetasSQLite(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    protected String[] getColumns() {
        return new String[]{ID_TABLE, "nombre_banco", "numero"};
    }

    @Override
    protected ContentValues getEntityValues(Tarjetas entity) {
        ContentValues content = new ContentValues();
        content.put(ID_TABLE, entity.getIdTarjeta());
        content.put("nombre_banco", entity.getNombreBanco());
        content.put("numero", entity.getNumero());
        return content;
    }

    @Override
    protected Tarjetas cursorToEntity(Cursor cursor) {
        Tarjetas entty = new Tarjetas();
        try {
            while (cursor.moveToNext()) {
                entty.setIdTarjeta(cursor.getInt(0));
                entty.setNombreBanco(cursor.getString(1));
                entty.setNumero(cursor.getString(2));
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
    protected Tarjetas changeIdTable(Long newId, Tarjetas entity) {
        entity.setIdTarjeta(newId.intValue());
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
