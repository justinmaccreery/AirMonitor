package jimjams.airmonitor.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

import jimjams.airmonitor.datastructure.Profile;

/**
 * Allows access to the database
 * @author Sean
 */
public class DBAccess implements AMDBContract {

   private static DBAccess access = null;

   /**
    * Used to identify source class for log
    */
   private String className = getClass().getSimpleName();

   /**
    * Constructor. Creates the database if it does not already exist, and populates it with the
    * necessary tables if they do not exist.
    */
   private DBAccess() {

      File path = new File(DB_PATH);

      Log.v(className, "Path: " + path.getAbsolutePath());

      Log.v(className, "Path exists: " + path.exists());
      if(!path.exists()) {
         Log.v(className, "Path created: " + path.mkdirs());
      }
      File dbFile = new File(path, DB_FILENAME);
      Log.v(className, "File: " + dbFile.getAbsolutePath());
      SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

      // Make sure the necessary tables exist
      // Profile table
      Log.v(className, "Query: " + getTableCreateString(ProfileTable.TABLE_NAME,
            ProfileTable.COLUMNS));
      database.execSQL(getTableCreateString(ProfileTable.TABLE_NAME, ProfileTable.COLUMNS));

      // EMA table
      Log.v(className, "Query: " + getTableCreateString(EMATable.TABLE_NAME, EMATable.COLUMNS));
      database.execSQL(getTableCreateString(EMATable.TABLE_NAME, EMATable.COLUMNS));

      // Snapshot table
      Log.v(className, "Query: " + getTableCreateString(SnapshotTable.TABLE_NAME,
            SnapshotTable.COLUMNS));
      database.execSQL(getTableCreateString(SnapshotTable.TABLE_NAME, SnapshotTable.COLUMNS));

      // SensorData table
      Log.v(className, "Query: " + getTableCreateString(SensorDataTable.TABLE_NAME,
            SensorDataTable.COLUMNS));
      database.execSQL(getTableCreateString(SensorDataTable.TABLE_NAME, SensorDataTable.COLUMNS));
   }

   private static String getTableCreateString(String tableName, String[][] cols) {
      String result = CREATE + tableName + "(";
      for(int i = 0; i < cols.length; i++) {
         if(i != 0) {
            result += ", ";
         }
         result += cols[i][0] + " " + cols[i][1];
      }
      result += PRIMARY_KEY + ");";
      return result;
   }


   public static DBAccess getDBAccess() {
      if(access == null) {
         access = new DBAccess();
      }
      return access;
   }

   public static void updateProfile() {
      Profile profile = Profile.getProfile();


   }
}