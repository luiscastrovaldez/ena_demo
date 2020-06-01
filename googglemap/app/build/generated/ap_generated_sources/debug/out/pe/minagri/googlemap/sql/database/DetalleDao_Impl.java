package pe.minagri.googlemap.sql.database;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import pe.minagri.googlemap.sql.Detalle;

@SuppressWarnings("unchecked")
public final class DetalleDao_Impl implements DetalleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfDetalle;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfDetalle;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfDetalle;

  private final SharedSQLiteStatement __preparedStmtOfDeleteDetalleByCabeceraId;

  public DetalleDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDetalle = new EntityInsertionAdapter<Detalle>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `DETALLE`(`uidDetalle`,`uidCabecera`,`latitud`,`longitud`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Detalle value) {
        if (value.uidDetalle == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.uidDetalle);
        }
        if (value.uidCabecera == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.uidCabecera);
        }
        if (value.latitud == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindDouble(3, value.latitud);
        }
        if (value.longitud == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindDouble(4, value.longitud);
        }
      }
    };
    this.__deletionAdapterOfDetalle = new EntityDeletionOrUpdateAdapter<Detalle>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `DETALLE` WHERE `uidDetalle` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Detalle value) {
        if (value.uidDetalle == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.uidDetalle);
        }
      }
    };
    this.__updateAdapterOfDetalle = new EntityDeletionOrUpdateAdapter<Detalle>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `DETALLE` SET `uidDetalle` = ?,`uidCabecera` = ?,`latitud` = ?,`longitud` = ? WHERE `uidDetalle` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Detalle value) {
        if (value.uidDetalle == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.uidDetalle);
        }
        if (value.uidCabecera == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.uidCabecera);
        }
        if (value.latitud == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindDouble(3, value.latitud);
        }
        if (value.longitud == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindDouble(4, value.longitud);
        }
        if (value.uidDetalle == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.uidDetalle);
        }
      }
    };
    this.__preparedStmtOfDeleteDetalleByCabeceraId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM DETALLE WHERE uidCabecera = ?";
        return _query;
      }
    };
  }

  @Override
  public void addDetalle(Detalle detalle) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfDetalle.insert(detalle);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteDetalle(Detalle detalle) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfDetalle.handle(detalle);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateDetalle(Detalle detalle) {
    __db.beginTransaction();
    try {
      __updateAdapterOfDetalle.handle(detalle);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteDetalleByCabeceraId(String uidCabecera) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteDetalleByCabeceraId.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (uidCabecera == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, uidCabecera);
      }
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteDetalleByCabeceraId.release(_stmt);
    }
  }

  @Override
  public List<Detalle> getDetalles() {
    final String _sql = "SELECT * FROM DETALLE";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUidDetalle = _cursor.getColumnIndexOrThrow("uidDetalle");
      final int _cursorIndexOfUidCabecera = _cursor.getColumnIndexOrThrow("uidCabecera");
      final int _cursorIndexOfLatitud = _cursor.getColumnIndexOrThrow("latitud");
      final int _cursorIndexOfLongitud = _cursor.getColumnIndexOrThrow("longitud");
      final List<Detalle> _result = new ArrayList<Detalle>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Detalle _item;
        _item = new Detalle();
        _item.uidDetalle = _cursor.getString(_cursorIndexOfUidDetalle);
        _item.uidCabecera = _cursor.getString(_cursorIndexOfUidCabecera);
        if (_cursor.isNull(_cursorIndexOfLatitud)) {
          _item.latitud = null;
        } else {
          _item.latitud = _cursor.getDouble(_cursorIndexOfLatitud);
        }
        if (_cursor.isNull(_cursorIndexOfLongitud)) {
          _item.longitud = null;
        } else {
          _item.longitud = _cursor.getDouble(_cursorIndexOfLongitud);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Detalle getDetalle(String uidDetalle) {
    final String _sql = "SELECT * FROM DETALLE WHERE uidDetalle LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uidDetalle == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uidDetalle);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUidDetalle = _cursor.getColumnIndexOrThrow("uidDetalle");
      final int _cursorIndexOfUidCabecera = _cursor.getColumnIndexOrThrow("uidCabecera");
      final int _cursorIndexOfLatitud = _cursor.getColumnIndexOrThrow("latitud");
      final int _cursorIndexOfLongitud = _cursor.getColumnIndexOrThrow("longitud");
      final Detalle _result;
      if(_cursor.moveToFirst()) {
        _result = new Detalle();
        _result.uidDetalle = _cursor.getString(_cursorIndexOfUidDetalle);
        _result.uidCabecera = _cursor.getString(_cursorIndexOfUidCabecera);
        if (_cursor.isNull(_cursorIndexOfLatitud)) {
          _result.latitud = null;
        } else {
          _result.latitud = _cursor.getDouble(_cursorIndexOfLatitud);
        }
        if (_cursor.isNull(_cursorIndexOfLongitud)) {
          _result.longitud = null;
        } else {
          _result.longitud = _cursor.getDouble(_cursorIndexOfLongitud);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Detalle> getDetalleByCabeceraId(String uidCabecera) {
    final String _sql = "SELECT * FROM DETALLE WHERE uidCabecera = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uidCabecera == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uidCabecera);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUidDetalle = _cursor.getColumnIndexOrThrow("uidDetalle");
      final int _cursorIndexOfUidCabecera = _cursor.getColumnIndexOrThrow("uidCabecera");
      final int _cursorIndexOfLatitud = _cursor.getColumnIndexOrThrow("latitud");
      final int _cursorIndexOfLongitud = _cursor.getColumnIndexOrThrow("longitud");
      final List<Detalle> _result = new ArrayList<Detalle>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Detalle _item;
        _item = new Detalle();
        _item.uidDetalle = _cursor.getString(_cursorIndexOfUidDetalle);
        _item.uidCabecera = _cursor.getString(_cursorIndexOfUidCabecera);
        if (_cursor.isNull(_cursorIndexOfLatitud)) {
          _item.latitud = null;
        } else {
          _item.latitud = _cursor.getDouble(_cursorIndexOfLatitud);
        }
        if (_cursor.isNull(_cursorIndexOfLongitud)) {
          _item.longitud = null;
        } else {
          _item.longitud = _cursor.getDouble(_cursorIndexOfLongitud);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
