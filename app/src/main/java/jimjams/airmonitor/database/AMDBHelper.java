package jimjams.airmonitor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * AirMonitor database helper
 * @author Sean
 */
public class AMDBHelper extends SQLiteOpenHelper implements AMDBContract {

   public AMDBHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {

   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

   }

   public void onOpen(SQLiteDatabase db) {

   }

}
