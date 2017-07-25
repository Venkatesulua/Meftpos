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
 * DAO for table "CARD_TYPE_TABLE".
*/
public class CardTypeTableDao extends AbstractDao<CardTypeTable, Long> {

    public static final String TABLENAME = "CARD_TYPE_TABLE";

    /**
     * Properties of entity CardTypeTable.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CTT_ID = new Property(1, String.class, "CTT_ID", false, "CTT__ID");
        public final static Property CTT_CARD_TYPE = new Property(2, String.class, "CTT_CARD_TYPE", false, "CTT__CARD__TYPE");
        public final static Property CTT_CARD_LABEL = new Property(3, String.class, "CTT_CARD_LABEL", false, "CTT__CARD__LABEL");
        public final static Property CTT_CARD_FORMAT = new Property(4, String.class, "CTT_CARD_FORMAT", false, "CTT__CARD__FORMAT");
        public final static Property CTT_MASK_FORMAT = new Property(5, String.class, "CTT_MASK_FORMAT", false, "CTT__MASK__FORMAT");
        public final static Property CTT_MAGSTRIPE_FLOOR_LIMIT = new Property(6, String.class, "CTT_MAGSTRIPE_FLOOR_LIMIT", false, "CTT__MAGSTRIPE__FLOOR__LIMIT");
        public final static Property CTT_DISABLE_LUHN = new Property(7, String.class, "CTT_DISABLE_LUHN", false, "CTT__DISABLE__LUHN");
        public final static Property CTT_CUSTOM_OPTIONS = new Property(8, String.class, "CTT_CUSTOM_OPTIONS", false, "CTT__CUSTOM__OPTIONS");
        public final static Property CTT_CVV_FDBC_ENABLE = new Property(9, String.class, "CTT_CVV_FDBC_ENABLE", false, "CTT__CVV__FDBC__ENABLE");
        public final static Property CTT_PAN_MASK_ARRAY = new Property(10, String.class, "CTT_PAN_MASK_ARRAY", false, "CTT__PAN__MASK__ARRAY");
        public final static Property CTT_EXPIRY_MASK_ARRAY = new Property(11, String.class, "CTT_EXPIRY_MASK_ARRAY", false, "CTT__EXPIRY__MASK__ARRAY");
        public final static Property CTT_QPSL = new Property(12, String.class, "CTT_QPSL", false, "CTT__QPSL");
        public final static Property CTT_DISABLE_EXPIRY_CHECK = new Property(13, String.class, "CTT_DISABLE_EXPIRY_CHECK", false, "CTT__DISABLE__EXPIRY__CHECK");
        public final static Property CTT_MC501 = new Property(14, String.class, "CTT_MC501", false, "CTT__MC501");
    }


    public CardTypeTableDao(DaoConfig config) {
        super(config);
    }
    
    public CardTypeTableDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CARD_TYPE_TABLE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CTT__ID\" TEXT," + // 1: CTT_ID
                "\"CTT__CARD__TYPE\" TEXT," + // 2: CTT_CARD_TYPE
                "\"CTT__CARD__LABEL\" TEXT," + // 3: CTT_CARD_LABEL
                "\"CTT__CARD__FORMAT\" TEXT," + // 4: CTT_CARD_FORMAT
                "\"CTT__MASK__FORMAT\" TEXT," + // 5: CTT_MASK_FORMAT
                "\"CTT__MAGSTRIPE__FLOOR__LIMIT\" TEXT," + // 6: CTT_MAGSTRIPE_FLOOR_LIMIT
                "\"CTT__DISABLE__LUHN\" TEXT," + // 7: CTT_DISABLE_LUHN
                "\"CTT__CUSTOM__OPTIONS\" TEXT," + // 8: CTT_CUSTOM_OPTIONS
                "\"CTT__CVV__FDBC__ENABLE\" TEXT," + // 9: CTT_CVV_FDBC_ENABLE
                "\"CTT__PAN__MASK__ARRAY\" TEXT," + // 10: CTT_PAN_MASK_ARRAY
                "\"CTT__EXPIRY__MASK__ARRAY\" TEXT," + // 11: CTT_EXPIRY_MASK_ARRAY
                "\"CTT__QPSL\" TEXT," + // 12: CTT_QPSL
                "\"CTT__DISABLE__EXPIRY__CHECK\" TEXT," + // 13: CTT_DISABLE_EXPIRY_CHECK
                "\"CTT__MC501\" TEXT);"); // 14: CTT_MC501
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CARD_TYPE_TABLE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CardTypeTable entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String CTT_ID = entity.getCTT_ID();
        if (CTT_ID != null) {
            stmt.bindString(2, CTT_ID);
        }
 
        String CTT_CARD_TYPE = entity.getCTT_CARD_TYPE();
        if (CTT_CARD_TYPE != null) {
            stmt.bindString(3, CTT_CARD_TYPE);
        }
 
        String CTT_CARD_LABEL = entity.getCTT_CARD_LABEL();
        if (CTT_CARD_LABEL != null) {
            stmt.bindString(4, CTT_CARD_LABEL);
        }
 
        String CTT_CARD_FORMAT = entity.getCTT_CARD_FORMAT();
        if (CTT_CARD_FORMAT != null) {
            stmt.bindString(5, CTT_CARD_FORMAT);
        }
 
        String CTT_MASK_FORMAT = entity.getCTT_MASK_FORMAT();
        if (CTT_MASK_FORMAT != null) {
            stmt.bindString(6, CTT_MASK_FORMAT);
        }
 
        String CTT_MAGSTRIPE_FLOOR_LIMIT = entity.getCTT_MAGSTRIPE_FLOOR_LIMIT();
        if (CTT_MAGSTRIPE_FLOOR_LIMIT != null) {
            stmt.bindString(7, CTT_MAGSTRIPE_FLOOR_LIMIT);
        }
 
        String CTT_DISABLE_LUHN = entity.getCTT_DISABLE_LUHN();
        if (CTT_DISABLE_LUHN != null) {
            stmt.bindString(8, CTT_DISABLE_LUHN);
        }
 
        String CTT_CUSTOM_OPTIONS = entity.getCTT_CUSTOM_OPTIONS();
        if (CTT_CUSTOM_OPTIONS != null) {
            stmt.bindString(9, CTT_CUSTOM_OPTIONS);
        }
 
        String CTT_CVV_FDBC_ENABLE = entity.getCTT_CVV_FDBC_ENABLE();
        if (CTT_CVV_FDBC_ENABLE != null) {
            stmt.bindString(10, CTT_CVV_FDBC_ENABLE);
        }
 
        String CTT_PAN_MASK_ARRAY = entity.getCTT_PAN_MASK_ARRAY();
        if (CTT_PAN_MASK_ARRAY != null) {
            stmt.bindString(11, CTT_PAN_MASK_ARRAY);
        }
 
        String CTT_EXPIRY_MASK_ARRAY = entity.getCTT_EXPIRY_MASK_ARRAY();
        if (CTT_EXPIRY_MASK_ARRAY != null) {
            stmt.bindString(12, CTT_EXPIRY_MASK_ARRAY);
        }
 
        String CTT_QPSL = entity.getCTT_QPSL();
        if (CTT_QPSL != null) {
            stmt.bindString(13, CTT_QPSL);
        }
 
        String CTT_DISABLE_EXPIRY_CHECK = entity.getCTT_DISABLE_EXPIRY_CHECK();
        if (CTT_DISABLE_EXPIRY_CHECK != null) {
            stmt.bindString(14, CTT_DISABLE_EXPIRY_CHECK);
        }
 
        String CTT_MC501 = entity.getCTT_MC501();
        if (CTT_MC501 != null) {
            stmt.bindString(15, CTT_MC501);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CardTypeTable entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String CTT_ID = entity.getCTT_ID();
        if (CTT_ID != null) {
            stmt.bindString(2, CTT_ID);
        }
 
        String CTT_CARD_TYPE = entity.getCTT_CARD_TYPE();
        if (CTT_CARD_TYPE != null) {
            stmt.bindString(3, CTT_CARD_TYPE);
        }
 
        String CTT_CARD_LABEL = entity.getCTT_CARD_LABEL();
        if (CTT_CARD_LABEL != null) {
            stmt.bindString(4, CTT_CARD_LABEL);
        }
 
        String CTT_CARD_FORMAT = entity.getCTT_CARD_FORMAT();
        if (CTT_CARD_FORMAT != null) {
            stmt.bindString(5, CTT_CARD_FORMAT);
        }
 
        String CTT_MASK_FORMAT = entity.getCTT_MASK_FORMAT();
        if (CTT_MASK_FORMAT != null) {
            stmt.bindString(6, CTT_MASK_FORMAT);
        }
 
        String CTT_MAGSTRIPE_FLOOR_LIMIT = entity.getCTT_MAGSTRIPE_FLOOR_LIMIT();
        if (CTT_MAGSTRIPE_FLOOR_LIMIT != null) {
            stmt.bindString(7, CTT_MAGSTRIPE_FLOOR_LIMIT);
        }
 
        String CTT_DISABLE_LUHN = entity.getCTT_DISABLE_LUHN();
        if (CTT_DISABLE_LUHN != null) {
            stmt.bindString(8, CTT_DISABLE_LUHN);
        }
 
        String CTT_CUSTOM_OPTIONS = entity.getCTT_CUSTOM_OPTIONS();
        if (CTT_CUSTOM_OPTIONS != null) {
            stmt.bindString(9, CTT_CUSTOM_OPTIONS);
        }
 
        String CTT_CVV_FDBC_ENABLE = entity.getCTT_CVV_FDBC_ENABLE();
        if (CTT_CVV_FDBC_ENABLE != null) {
            stmt.bindString(10, CTT_CVV_FDBC_ENABLE);
        }
 
        String CTT_PAN_MASK_ARRAY = entity.getCTT_PAN_MASK_ARRAY();
        if (CTT_PAN_MASK_ARRAY != null) {
            stmt.bindString(11, CTT_PAN_MASK_ARRAY);
        }
 
        String CTT_EXPIRY_MASK_ARRAY = entity.getCTT_EXPIRY_MASK_ARRAY();
        if (CTT_EXPIRY_MASK_ARRAY != null) {
            stmt.bindString(12, CTT_EXPIRY_MASK_ARRAY);
        }
 
        String CTT_QPSL = entity.getCTT_QPSL();
        if (CTT_QPSL != null) {
            stmt.bindString(13, CTT_QPSL);
        }
 
        String CTT_DISABLE_EXPIRY_CHECK = entity.getCTT_DISABLE_EXPIRY_CHECK();
        if (CTT_DISABLE_EXPIRY_CHECK != null) {
            stmt.bindString(14, CTT_DISABLE_EXPIRY_CHECK);
        }
 
        String CTT_MC501 = entity.getCTT_MC501();
        if (CTT_MC501 != null) {
            stmt.bindString(15, CTT_MC501);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CardTypeTable readEntity(Cursor cursor, int offset) {
        CardTypeTable entity = new CardTypeTable( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // CTT_ID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // CTT_CARD_TYPE
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // CTT_CARD_LABEL
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // CTT_CARD_FORMAT
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // CTT_MASK_FORMAT
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // CTT_MAGSTRIPE_FLOOR_LIMIT
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // CTT_DISABLE_LUHN
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // CTT_CUSTOM_OPTIONS
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // CTT_CVV_FDBC_ENABLE
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // CTT_PAN_MASK_ARRAY
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // CTT_EXPIRY_MASK_ARRAY
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // CTT_QPSL
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // CTT_DISABLE_EXPIRY_CHECK
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14) // CTT_MC501
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CardTypeTable entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCTT_ID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCTT_CARD_TYPE(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCTT_CARD_LABEL(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCTT_CARD_FORMAT(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCTT_MASK_FORMAT(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCTT_MAGSTRIPE_FLOOR_LIMIT(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCTT_DISABLE_LUHN(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCTT_CUSTOM_OPTIONS(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCTT_CVV_FDBC_ENABLE(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCTT_PAN_MASK_ARRAY(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCTT_EXPIRY_MASK_ARRAY(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCTT_QPSL(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCTT_DISABLE_EXPIRY_CHECK(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setCTT_MC501(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CardTypeTable entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CardTypeTable entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CardTypeTable entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}