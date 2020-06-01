package pe.minagri.googlemap.sql.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class MapaDataBase_Impl extends MapaDataBase {
  private volatile CabeceraDao _cabeceraDao;

  private volatile DetalleDao _detalleDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `CABECERA` (`uid` TEXT NOT NULL, `tipo_grafico` TEXT NOT NULL, `area` REAL NOT NULL, PRIMARY KEY(`uid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `DETALLE` (`uidDetalle` TEXT NOT NULL, `uidCabecera` TEXT NOT NULL, `latitud` REAL NOT NULL, `longitud` REAL NOT NULL, PRIMARY KEY(`uidDetalle`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"1f410d3c217f48e26038de24f3820718\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `CABECERA`");
        _db.execSQL("DROP TABLE IF EXISTS `DETALLE`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsCABECERA = new HashMap<String, TableInfo.Column>(3);
        _columnsCABECERA.put("uid", new TableInfo.Column("uid", "TEXT", true, 1));
        _columnsCABECERA.put("tipo_grafico", new TableInfo.Column("tipo_grafico", "TEXT", true, 0));
        _columnsCABECERA.put("area", new TableInfo.Column("area", "REAL", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCABECERA = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCABECERA = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCABECERA = new TableInfo("CABECERA", _columnsCABECERA, _foreignKeysCABECERA, _indicesCABECERA);
        final TableInfo _existingCABECERA = TableInfo.read(_db, "CABECERA");
        if (! _infoCABECERA.equals(_existingCABECERA)) {
          throw new IllegalStateException("Migration didn't properly handle CABECERA(pe.minagri.googlemap.sql.Cabecera).\n"
                  + " Expected:\n" + _infoCABECERA + "\n"
                  + " Found:\n" + _existingCABECERA);
        }
        final HashMap<String, TableInfo.Column> _columnsDETALLE = new HashMap<String, TableInfo.Column>(4);
        _columnsDETALLE.put("uidDetalle", new TableInfo.Column("uidDetalle", "TEXT", true, 1));
        _columnsDETALLE.put("uidCabecera", new TableInfo.Column("uidCabecera", "TEXT", true, 0));
        _columnsDETALLE.put("latitud", new TableInfo.Column("latitud", "REAL", true, 0));
        _columnsDETALLE.put("longitud", new TableInfo.Column("longitud", "REAL", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDETALLE = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDETALLE = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDETALLE = new TableInfo("DETALLE", _columnsDETALLE, _foreignKeysDETALLE, _indicesDETALLE);
        final TableInfo _existingDETALLE = TableInfo.read(_db, "DETALLE");
        if (! _infoDETALLE.equals(_existingDETALLE)) {
          throw new IllegalStateException("Migration didn't properly handle DETALLE(pe.minagri.googlemap.sql.Detalle).\n"
                  + " Expected:\n" + _infoDETALLE + "\n"
                  + " Found:\n" + _existingDETALLE);
        }
      }
    }, "1f410d3c217f48e26038de24f3820718", "ecc77cee9744cf294aa12878df08097a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "CABECERA","DETALLE");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `CABECERA`");
      _db.execSQL("DELETE FROM `DETALLE`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public CabeceraDao getCabeceraDao() {
    if (_cabeceraDao != null) {
      return _cabeceraDao;
    } else {
      synchronized(this) {
        if(_cabeceraDao == null) {
          _cabeceraDao = new CabeceraDao_Impl(this);
        }
        return _cabeceraDao;
      }
    }
  }

  @Override
  public DetalleDao getDetalleDao() {
    if (_detalleDao != null) {
      return _detalleDao;
    } else {
      synchronized(this) {
        if(_detalleDao == null) {
          _detalleDao = new DetalleDao_Impl(this);
        }
        return _detalleDao;
      }
    }
  }
}
