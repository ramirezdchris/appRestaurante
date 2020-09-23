package com.fm.modules.sqlite.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class SqlIteServices<T> extends SQLiteOpenHelper {

    public SqlIteServices(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * set table fields from entity fields
     *
     * <p> example:
     * String[] fields = {"id","name"}
     * return fields[]</p>
     *
     * @return a String Array
     * @throws NullPointerException if te content is null
     */
    protected abstract String[] getColumns();

    /**
     * set table content values from entity data
     *
     * <p> example:
     * content values . put ("id", "1")
     * return content value</p>
     *
     * @param entity the entity to perform
     * @return a fulled content
     * @throws NullPointerException if te content is null
     */
    protected abstract ContentValues getEntityValues(T entity);

    /**
     * vice versa of {@link #getEntityValues}
     *
     * @param cursor the cursor to perform
     * @return a String
     * @throws NullPointerException if te content is null
     */
    protected abstract T cursorToEntity(Cursor cursor);

    /**
     * set table name de la tabla
     *
     * <p> example: return "users"; </p>
     *
     * @return a String whit te name Table
     * @throws NullPointerException if te nameTable not exist
     */
    protected abstract String getTableName();

    /**
     * set table id in String field name
     *
     * <p> example:
     * return "id"</p>
     *
     * @return a String
     * @throws NullPointerException if te content is null
     */
    protected abstract String getId();

    /**
     * set newId in entity.id(..)
     * it is used in create method
     *
     * <p> example:
     * entity.setId(newId)
     * return entity</p>
     *
     * @param entity the entity to modify
     * @param newId  the id to set
     * @return the entity modified
     * @throws NullPointerException if te content is null
     */
    protected abstract T changeIdTable(Long newId, T entity);

    /**
     * register in the db
     *
     * @param entity the entity to save
     * @return the id of new Data
     * @throws NullPointerException if te entity is null
     */
    public Long create(T entity) {
        Long b = null;
        try {
            entity = changeIdTable(0L, entity);
            ContentValues contentValues = getEntityValues(entity);
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            b = sqLiteDatabase.insert(getTableName(), null, contentValues);
            entity = changeIdTable(b, entity);
            update(entity, 0L);
        } catch (Exception e) {
            System.out.println("error SQLite Service Create: " + e);
        }
        return b;
    }

    /**
     * register in the db
     *
     * @param entity the entity to save
     * @return the id of new Data
     * @throws NullPointerException if te entity is null
     */
    public boolean createNonIdentity(T entity) {
        boolean b = false;
        try {
            ContentValues contentValues = getEntityValues(entity);
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.insert(getTableName(), null, contentValues);
            b = true;
        } catch (Exception e) {
            System.out.println("error SQLite Service Create non id: " + e);
        }
        return b;
    }

    /**
     * read in the db
     *
     * @return a list with Entity data
     * @throws android.database.CursorIndexOutOfBoundsException if te query result null
     */
    public List<T> readAll() {
        List<T> lista = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + getTableName(), null);
            while (cursor.moveToNext()) {
                T entity = cursorToEntity(cursor);
                if (entity != null) {
                    lista.add(entity);
                }
            }
        } catch (Exception e) {
            System.out.println("error SQLite Service Read: " + e);
        }
        return lista;
    }

    /**
     * read in the db
     *
     * @return a list with Entity data
     * @throws NullPointerException if te query result null
     */
    public T readId(Long id) {
        T entity = null;
        try {
            String[] params = {id.toString()};
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(getTableName(), getColumns(), getId() + "=?", params, null, null, null);
            if (cursor.moveToFirst()) {
                entity = cursorToEntity(cursor);
                System.out.println("sqlite find by id: " + entity);
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println("error SQLite Service ReadId: " + e);
        }
        return entity;
    }

    /**
     * update in the db
     *
     * @param entity the entity to update
     * @param id     the entity id to edit
     * @return true id good
     * @throws NullPointerException if te entity is null
     */
    public boolean update(T entity, Long id) {
        boolean b = false;
        try {
            String[] params = {id.toString()};
            ContentValues contentValues = getEntityValues(entity);
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            int up = sqLiteDatabase.update(getTableName(), contentValues, getId() + "=?", params);
            System.out.println(up + "rows afected");
            b = up > 0;
        } catch (Exception e) {
            System.out.println("error SQLite Service Update: " + e);
        }
        return b;
    }

    /**
     * delete in the db
     *
     * @param id the entity id to delete
     * @return true id good
     * @throws NullPointerException if te entity is null
     */
    public boolean delete(Long id) {
        boolean b = false;
        try {
            String[] params = {id.toString()};
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.delete(getTableName(), getId() + "=?", params);
            b = true;
        } catch (Exception e) {
            System.out.println("error SQLite Service Delete: " + e);
        }
        return b;
    }

    protected abstract String getCreateTableScript();

    protected abstract String getDropTableScript();

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateTableScript());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(getDropTableScript());
        onCreate(db);
    }

    /**
     * delete data in te table
     * is a clear
     */
    public void truncate() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + getTableName() + ";");
    }
}
