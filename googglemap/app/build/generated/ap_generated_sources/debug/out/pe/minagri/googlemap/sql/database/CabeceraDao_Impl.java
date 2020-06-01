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
import pe.minagri.googlemap.sql.Cabecera;

@SuppressWarnings("unchecked")
public final class CabeceraDao_Impl implements CabeceraDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfCabecera;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfCabecera;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfCabecera;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCabeceraById;

  public CabeceraDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCabecera = new EntityInsertionAdapter<Cabecera>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `CABECERA`(`uid`,`tipo_grafico`,`area`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Cabecera value) {
        if (value.uid == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.uid);
        }
        if (value.tipoGrafico == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.tipoGrafico);
        }
        if (value.area == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindDouble(3, value.area);
        }
      }
    };
    this.__deletionAdapterOfCabecera = new EntityDeletionOrUpdateAdapter<Cabecera>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `CABECERA` WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Cabecera value) {
        if (value.uid == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.uid);
        }
      }
    };
    this.__updateAdapterOfCabecera = new EntityDeletionOrUpdateAdapter<Cabecera>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `CABECERA` SET `uid` = ?,`tipo_grafico` = ?,`area` = ? WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Cabecera value) {
        if (value.uid == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.uid);
        }
        if (value.tipoGrafico == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.tipoGrafico);
        }
        if (value.area == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindDouble(3, value.area);
        }
        if (value.uid == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.uid);
        }
      }
    };
    this.__preparedStmtOfDeleteCabeceraById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM CABECERA WHERE uid = ?";
        return _query;
      }
    };
  }

  @Override
  public void addCabecera(Cabecera cabecera) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfCabecera.insert(cabecera);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteCabecera(Cabecera cabecera) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfCabecera.handle(cabecera);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateCabecera(Cabecera cabecera) {
    __db.beginTransaction();
    try {
      __updateAdapterOfCabecera.handle(cabecera);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteCabeceraById(String uid) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCabeceraById.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (uid == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, uid);
      }
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteCabeceraById.release(_stmt);
    }
  }

  @Override
  public List<Cabecera> getCabeceras() {
    final String _sql = "SELECT * FROM CABECERA";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUid = _cursor.getColumnIndexOrThrow("uid");
      final int _cursorIndexOfTipoGrafico = _cursor.getColumnIndexOrThrow("tipo_grafico");
      final int _cursorIndexOfArea = _cursor.getColumnIndexOrThrow("area");
      final List<Cabecera> _result = new ArrayList<Cabecera>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Cabecera _item;
        _item = new Cabecera();
        _item.uid = _cursor.getString(_cursorIndexOfUid);
        _item.tipoGrafico = _cursor.getString(_cursorIndexOfTipoGrafico);
        if (_cursor.isNull(_cursorIndexOfArea)) {
          _item.area = null;
        } else {
          _item.area = _cursor.getDouble(_cursorIndexOfArea);
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
  public Cabecera getCabecera(String uid) {
    final String _sql = "SELECT * FROM CABECERA WHERE uid LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUid = _cursor.getColumnIndexOrThrow("uid");
      final int _cursorIndexOfTipoGrafico = _cursor.getColumnIndexOrThrow("tipo_grafico");
      final int _cursorIndexOfArea = _cursor.getColumnIndexOrThrow("area");
      final Cabecera _result;
      if(_cursor.moveToFirst()) {
        _result = new Cabecera();
        _result.uid = _cursor.getString(_cursorIndexOfUid);
        _result.tipoGrafico = _cursor.getString(_cursorIndexOfTipoGrafico);
        if (_cursor.isNull(_cursorIndexOfArea)) {
          _result.area = null;
        } else {
          _result.area = _cursor.getDouble(_cursorIndexOfArea);
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
}
