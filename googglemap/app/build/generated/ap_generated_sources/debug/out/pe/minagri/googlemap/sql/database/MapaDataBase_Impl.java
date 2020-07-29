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
  private volatile PolygonDao _polygonDao;

  private volatile PointDao _pointDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Polygon` (`uid` TEXT NOT NULL, `area` REAL NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `uidParent` TEXT, PRIMARY KEY(`uid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Point` (`uid` TEXT NOT NULL, `uidPolygon` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, PRIMARY KEY(`uid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"921d7d41f24ce1c8af9e1f45c86b2133\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Polygon`");
        _db.execSQL("DROP TABLE IF EXISTS `Point`");
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
        final HashMap<String, TableInfo.Column> _columnsPolygon = new HashMap<String, TableInfo.Column>(5);
        _columnsPolygon.put("uid", new TableInfo.Column("uid", "TEXT", true, 1));
        _columnsPolygon.put("area", new TableInfo.Column("area", "REAL", true, 0));
        _columnsPolygon.put("name", new TableInfo.Column("name", "TEXT", true, 0));
        _columnsPolygon.put("type", new TableInfo.Column("type", "TEXT", true, 0));
        _columnsPolygon.put("uidParent", new TableInfo.Column("uidParent", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPolygon = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPolygon = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPolygon = new TableInfo("Polygon", _columnsPolygon, _foreignKeysPolygon, _indicesPolygon);
        final TableInfo _existingPolygon = TableInfo.read(_db, "Polygon");
        if (! _infoPolygon.equals(_existingPolygon)) {
          throw new IllegalStateException("Migration didn't properly handle Polygon(pe.minagri.googlemap.sql.Polygon).\n"
                  + " Expected:\n" + _infoPolygon + "\n"
                  + " Found:\n" + _existingPolygon);
        }
        final HashMap<String, TableInfo.Column> _columnsPoint = new HashMap<String, TableInfo.Column>(4);
        _columnsPoint.put("uid", new TableInfo.Column("uid", "TEXT", true, 1));
        _columnsPoint.put("uidPolygon", new TableInfo.Column("uidPolygon", "TEXT", true, 0));
        _columnsPoint.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0));
        _columnsPoint.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPoint = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPoint = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPoint = new TableInfo("Point", _columnsPoint, _foreignKeysPoint, _indicesPoint);
        final TableInfo _existingPoint = TableInfo.read(_db, "Point");
        if (! _infoPoint.equals(_existingPoint)) {
          throw new IllegalStateException("Migration didn't properly handle Point(pe.minagri.googlemap.sql.Point).\n"
                  + " Expected:\n" + _infoPoint + "\n"
                  + " Found:\n" + _existingPoint);
        }
      }
    }, "921d7d41f24ce1c8af9e1f45c86b2133", "04b56c274d640819d341a7f406ff77fd");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Polygon","Point");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Polygon`");
      _db.execSQL("DELETE FROM `Point`");
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
  public PolygonDao getPolygonDao() {
    if (_polygonDao != null) {
      return _polygonDao;
    } else {
      synchronized(this) {
        if(_polygonDao == null) {
          _polygonDao = new PolygonDao_Impl(this);
        }
        return _polygonDao;
      }
    }
  }

  @Override
  public PointDao getPointDao() {
    if (_pointDao != null) {
      return _pointDao;
    } else {
      synchronized(this) {
        if(_pointDao == null) {
          _pointDao = new PointDao_Impl(this);
        }
        return _pointDao;
      }
    }
  }
}
