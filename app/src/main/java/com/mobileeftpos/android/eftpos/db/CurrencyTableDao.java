package com.mobileeftpos.android.eftpos.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CURRENCY_TABLE".
*/
public class CurrencyTableDao extends AbstractDao<CurrencyTable, Long> {

    public static final String TABLENAME = "CURRENCY_TABLE";

    /**
     * Properties of entity CurrencyTable.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CURRENCY_ID = new Property(1, String.class, "CURRENCY_ID", false, "CURRENCY__ID");
        public final static Property CURR_LABEL = new Property(2, String.class, "CURR_LABEL", false, "CURR__LABEL");
        public final static Property CURR_EXPONENT = new Property(3, String.class, "CURR_EXPONENT", false, "CURR__EXPONENT");
        public final static Property CURR_CODE = new Property(4, String.class, "CURR_CODE", false, "CURR__CODE");
    }


    public CurrencyTableDao(DaoConfig config) {
        super(config);
    }
    
    public CurrencyTableDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CURRENCY_TABLE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CURRENCY__ID\" TEXT," + // 1: CURRENCY_ID
                "\"CURR__LABEL\" TEXT," + // 2: CURR_LABEL
                "\"CURR__EXPONENT\" TEXT," + // 3: CURR_EXPONENT
                "\"CURR__CODE\" TEXT);"); // 4: CURR_CODE
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CURRENCY_TABLE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CurrencyTable entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String CURRENCY_ID = entity.getCURRENCY_ID();
        if (CURRENCY_ID != null) {
            stmt.bindString(2, CURRENCY_ID);
        }
 
        String CURR_LABEL = entity.getCURR_LABEL();
        if (CURR_LABEL != null) {
            stmt.bindString(3, CURR_LABEL);
        }
 
        String CURR_EXPONENT = entity.getCURR_EXPONENT();
        if (CURR_EXPONENT != null) {
            stmt.bindString(4, CURR_EXPONENT);
        }
 
        String CURR_CODE = entity.getCURR_CODE();
        if (CURR_CODE != null) {
            stmt.bindString(5, CURR_CODE);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CurrencyTable entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String CURRENCY_ID = entity.getCURRENCY_ID();
        if (CURRENCY_ID != null) {
            stmt.bindString(2, CURRENCY_ID);
        }
 
        String CURR_LABEL = entity.getCURR_LABEL();
        if (CURR_LABEL != null) {
            stmt.bindString(3, CURR_LABEL);
        }
 
        String CURR_EXPONENT = entity.getCURR_EXPONENT();
        if (CURR_EXPONENT != null) {
            stmt.bindString(4, CURR_EXPONENT);
        }
 
        String CURR_CODE = entity.getCURR_CODE();
        if (CURR_CODE != null) {
            stmt.bindString(5, CURR_CODE);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CurrencyTable readEntity(Cursor cursor, int offset) {
        CurrencyTable entity = new CurrencyTable( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // CURRENCY_ID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // CURR_LABEL
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // CURR_EXPONENT
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // CURR_CODE
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CurrencyTable entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCURRENCY_ID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCURR_LABEL(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCURR_EXPONENT(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCURR_CODE(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CurrencyTable entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CurrencyTable entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CurrencyTable entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
