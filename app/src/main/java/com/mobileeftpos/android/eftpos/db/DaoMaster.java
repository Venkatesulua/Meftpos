package com.mobileeftpos.android.eftpos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 1): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(Database db, boolean ifNotExists) {
        HostTableDao.createTable(db, ifNotExists);
        CardBinTableDao.createTable(db, ifNotExists);
        CardTypeTableDao.createTable(db, ifNotExists);
        PasswordTableDao.createTable(db, ifNotExists);
        BatchDao.createTable(db, ifNotExists);
        ALIPAYDao.createTable(db, ifNotExists);
        ReceiptTableDao.createTable(db, ifNotExists);
        UtilityTableDao.createTable(db, ifNotExists);
        MaskingTableDao.createTable(db, ifNotExists);
        LimitTableDao.createTable(db, ifNotExists);
        EthernetTableDao.createTable(db, ifNotExists);
        CurrencyTableDao.createTable(db, ifNotExists);
        TransactionControlTableDao.createTable(db, ifNotExists);
        EZLINK_TABLEDao.createTable(db, ifNotExists);
        COMMS_TABLEDao.createTable(db, ifNotExists);
        TraceDao.createTable(db, ifNotExists);
        TABLE_HTTDao.createTable(db, ifNotExists);
        MerchantTableDao.createTable(db, ifNotExists);
        TABLE_REPORTDao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(Database db, boolean ifExists) {
        HostTableDao.dropTable(db, ifExists);
        CardBinTableDao.dropTable(db, ifExists);
        CardTypeTableDao.dropTable(db, ifExists);
        PasswordTableDao.dropTable(db, ifExists);
        BatchDao.dropTable(db, ifExists);
        ALIPAYDao.dropTable(db, ifExists);
        ReceiptTableDao.dropTable(db, ifExists);
        UtilityTableDao.dropTable(db, ifExists);
        MaskingTableDao.dropTable(db, ifExists);
        LimitTableDao.dropTable(db, ifExists);
        EthernetTableDao.dropTable(db, ifExists);
        CurrencyTableDao.dropTable(db, ifExists);
        TransactionControlTableDao.dropTable(db, ifExists);
        EZLINK_TABLEDao.dropTable(db, ifExists);
        COMMS_TABLEDao.dropTable(db, ifExists);
        TraceDao.dropTable(db, ifExists);
        TABLE_HTTDao.dropTable(db, ifExists);
        MerchantTableDao.dropTable(db, ifExists);
        TABLE_REPORTDao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     * Convenience method using a {@link DevOpenHelper}.
     */
    public static DaoSession newDevSession(Context context, String name) {
        Database db = new DevOpenHelper(context, name).getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(HostTableDao.class);
        registerDaoClass(CardBinTableDao.class);
        registerDaoClass(CardTypeTableDao.class);
        registerDaoClass(PasswordTableDao.class);
        registerDaoClass(BatchDao.class);
        registerDaoClass(ALIPAYDao.class);
        registerDaoClass(ReceiptTableDao.class);
        registerDaoClass(UtilityTableDao.class);
        registerDaoClass(MaskingTableDao.class);
        registerDaoClass(LimitTableDao.class);
        registerDaoClass(EthernetTableDao.class);
        registerDaoClass(CurrencyTableDao.class);
        registerDaoClass(TransactionControlTableDao.class);
        registerDaoClass(EZLINK_TABLEDao.class);
        registerDaoClass(COMMS_TABLEDao.class);
        registerDaoClass(TraceDao.class);
        registerDaoClass(TABLE_HTTDao.class);
        registerDaoClass(MerchantTableDao.class);
        registerDaoClass(TABLE_REPORTDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    /**
     * Calls {@link #createAllTables(Database, boolean)} in {@link #onCreate(Database)} -
     */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}
