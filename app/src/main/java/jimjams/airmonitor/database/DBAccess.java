package jimjams.airmonitor.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import jimjams.airmonitor.datastructure.EcologicalMomentaryAssessment;
import jimjams.airmonitor.datastructure.ExistingCondition;
import jimjams.airmonitor.datastructure.Profile;
import jimjams.airmonitor.datastructure.Snapshot;
import jimjams.airmonitor.sensordata.SensorData;

/**
 * Allows access to the database
 * @author Sean
 */
public class DBAccess implements AMDBContract {

   /**
    * The current instance of this class
    */
   private static DBAccess access = null;

   /**
    * The SQLiteDatabase containing saved data for the AirMonitor app
    */
   private SQLiteDatabase database;

   /**
    * Used to identify source class for log
    */
   private String className = getClass().getSimpleName();

   /**
    * If this is true, the existing database will be deleted and rebuilt from scratch.
    */
   private static boolean deleteDatabase = false;

   /**
    * Constructor. Creates the database if it does not already exist, and populates it with the
    * necessary tables if they do not exist.
    */
   private DBAccess() {

      File path = new File(DB_PATH);

      Log.d(className, "Path: " + path.getAbsolutePath());

      Log.d(className, "Path exists: " + path.exists());
      if(!path.exists()) {
         Log.d(className, "Path created: " + path.mkdirs());
      }
      File dbFile = new File(path, DB_FILENAME);
      if(dbFile.exists() && deleteDatabase) {
         SQLiteDatabase.deleteDatabase(dbFile);
         deleteDatabase = false;
      }
      Log.d(className, "File: " + dbFile.getAbsolutePath());
      database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

      // Make sure the necessary tables exist
      // Profile table
      Log.d(className, "Instruction: " + getTableCreateString(ProfileTable.TABLE_NAME,
            ProfileTable.COLUMNS));
      database.execSQL(getTableCreateString(ProfileTable.TABLE_NAME, ProfileTable.COLUMNS));

      // EMA table
      Log.d(className, "Instruction: " + getTableCreateString(EMATable.TABLE_NAME, EMATable.COLUMNS));
      database.execSQL(getTableCreateString(EMATable.TABLE_NAME, EMATable.COLUMNS));

      // Snapshot table
      Log.d(className, "Instruction: " + getTableCreateString(SnapshotTable.TABLE_NAME,
            SnapshotTable.COLUMNS));
      database.execSQL(getTableCreateString(SnapshotTable.TABLE_NAME, SnapshotTable.COLUMNS));

      // SensorData table
      Log.d(className, "Instruction: " + getTableCreateString(SensorDataTable.TABLE_NAME,
            SensorDataTable.COLUMNS));
      database.execSQL(getTableCreateString(SensorDataTable.TABLE_NAME, SensorDataTable.COLUMNS));
   }

   /**
    * Generates a String to be passed to generate a table in the database.
    * @param tableName The name of the table
    * @param cols Array of paired column names and types
    * @return String to be passed to generate a table in the database
    */
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

   /**
    * Gets the current instance of DBAccess.
    * @return The current DBAccess
    */
   public static DBAccess getDBAccess() {
      if(access == null) {
         access = new DBAccess();
      }
      return access;
   }

   /**
    * Updates the ProfileTable based on current Profile.
    */
   public void updateProfile() {
      Profile profile = Profile.getProfile();
      ContentValues cv = new ContentValues(2);
      cv.put("id", profile.getId());
      ArrayList<ExistingCondition> conditions = profile.getConditions();
      ArrayList<String> conditionStrings = new ArrayList<>(conditions.size());
      for(ExistingCondition cond: conditions) {
         conditionStrings.add(cond.toString());
      }
      cv.put("conditions", flatten(conditionStrings));
      database.insertWithOnConflict(ProfileTable.TABLE_NAME, null, cv,
            SQLiteDatabase.CONFLICT_REPLACE);
   }

   /**
    * Saves a set of SensorData to the database.
    * @param data The set of SensorData to be saved
    * @return IDs of the records inserted into the table
    */
   public long[] saveSensorData(ArrayList<SensorData> data) {
      // Create array of IDs to be used for this set of data
      long[] ids = new long[data.size()];

      // Insert a new row into the table
      for(int i = 0; i < data.size(); i++) {
         SensorData datum = data.get(i);
         ContentValues cv = new ContentValues(SensorDataTable.COLUMNS.length - 1);
         cv.put("displayName", datum.getDisplayName());
         cv.put("shortName", datum.getShortName());
         cv.put("value", datum.getValue());
         cv.put("displayValue", datum.getDisplayValue());
         ids[i] = database.insert(SensorDataTable.TABLE_NAME, null, cv);
      }

      Log.d(className, "lastId: " + ids[ids.length - 1]);

      // Return the IDs of the inserted rows
      return ids;
   }

   /**
    * Gets the highest int used as an ID in the specified table.
    * @param name The name of the table
    * @return The highest int used as an ID in the table
    */
   private int getHighestId(String name) {
      Cursor cursor = database.rawQuery("SELECT id FROM " + name, null);
      int highest = 0;
      boolean keepGoing = cursor.getCount() > 0;
      while(keepGoing) {
         highest = Math.max(highest, cursor.getInt(0));
         keepGoing = cursor.moveToNext();
      }
      cursor.close();
      Log.d(className, "Highest id: " + highest);
      return highest;
   }

   /**
    * Saves an EcologicalMomentaryAssessment to the database.
    * @param ema The EMA to be saved
    */
   public long saveEMA(EcologicalMomentaryAssessment ema) {
      // Populate the column values
      ContentValues cv = new ContentValues(EMATable.COLUMNS.length - 1);
      cv.put("indoors", ema.getIndoors());
      cv.put("reportedLocation", ema.getReportedLocation());
      cv.put("activity", ema.getActivity());
      cv.put("companions", flatten(ema.getCompanions()));
      cv.put("airQuality", ema.getAirQuality());
      cv.put("belief", ema.getBelief());
      cv.put("intention", ema.getIntention());
      cv.put("behavior", ema.getBehavior());
      cv.put("barrier", ema.getBarrier());

      // Return the id of the inserted row
      return database.insert(EMATable.TABLE_NAME, null, cv);
   }

   public long saveSnapshot(Snapshot snapshot) {
      // Save data structures from Snapshot
      ArrayList<SensorData> data = snapshot.getData();

      // Save this set of data to the database and get their IDs
      long[] sensorIds = access.saveSensorData(data);
      String sensorIdString = flatten(sensorIds);

      // Get existing conditions as a String
      ArrayList<ExistingCondition> conditions = snapshot.getConditions();
      ArrayList<String> conditionList = new ArrayList<>(conditions.size());
      for(ExistingCondition ec: conditions) {
         conditionList.add(ec.toString());
      }
      String conditionString = flatten(conditionList);

      // Save the EMA and get its ID
      EcologicalMomentaryAssessment ema = snapshot.getEma();
      // Save the EMA to the database
      long emaId = access.saveEMA(ema);

      // Populate the column values
      ContentValues cv = new ContentValues(SnapshotTable.COLUMNS.length - 1);
      cv.put("userId", Profile.getProfile().getId());
      cv.put("timestamp", snapshot.getTimestamp().getTime());
      if(snapshot.getLocation() != null) {
         cv.put("location", snapshot.getLocation().toString());
      }
      cv.put("sensorData", sensorIdString);
      cv.put("conditions", conditionString);
      cv.put("ema", emaId);

      // Return the id of the inserted row
      return database.insert(SnapshotTable.TABLE_NAME, null, cv);
   }

   /**
    * Converts an ArrayList into a String of semicolon-separated values.
    * @param list The ArrayList
    * @return ArrayList, as a single String
    */
   private static String flatten(ArrayList<String> list) {
      String result = "";
      for(int i = 0; i < list.size(); i++) {
         if(i > 0) {
            result += ";";
         }
         result += list.get(i);
      }
      return result;
   }

   /**
    * Converts an array of longs into a String of semicolon-separated values.
    * @param array The array of longs
    * @return array, as a single String
    */
   private static String flatten(long[] array) {
      String result = "";
      for(int i = 0; i < array.length; i++) {
         if(i > 0) {
            result += ";";
         }
         result += array[i];
      }
      return result;
   }

   /**
    * Gets the ID from the current Profile. If no Profile exists, 0 is returned. If more than one
    * Profile exists, an SQLiteFullException is thrown, as the database should not contain more than
    * one Profile.
    * @return The ID of the current Profile, or 0 if no Profile exists
    * @throws SQLiteFullException if the database contains more than one Profile
    */
   public long getProfileId() throws SQLiteFullException {
      Cursor cursor = database.rawQuery("SELECT id FROM " + ProfileTable.TABLE_NAME, null);
      cursor.moveToFirst();
      long result;
      int count = cursor.getCount();
      Log.d(className, "cursor count: " + count);
      if(count == 0) {
         result = 0;
      }
      else if(count == 1) {
         result = cursor.getInt(0);
      }
      else {
         throw new SQLiteFullException("Too many Profiles in database.");
      }
      return result;
   }

   /**
    * Attempts to populate an ArrayList of ExistingConditions from the Profile table. If no Profile
    * exists, an empty ArrayList is returned. If more than one Profile exists, an
    * SQLiteFullException is thrown, as there should not be more than one Profile in the database.
    * @return An ArrayList of ExistingConditions
    * @throws SQLiteFullException
    */
   public ArrayList<ExistingCondition> getProfileConditions() throws SQLiteFullException {
      Cursor cursor = database.rawQuery("SELECT conditions FROM " + ProfileTable.TABLE_NAME, null);
      cursor.moveToFirst();
      ArrayList<ExistingCondition> conditions;
      if(cursor.getCount() == 0) {
         conditions = new ArrayList<>();
         Log.d(className, "No profile in database.");
      }
      else if(cursor.getCount() == 1) {
         String conditionString = cursor.getString(0);
         String[] conditionStrings = conditionString.split(";");
         conditions = new ArrayList<>(conditionStrings.length);
         for(String cond : conditionStrings) {
            conditions.add(new ExistingCondition(cond));
         }
         Log.d(className, "Profile in database; " + conditions.size() + " existing conditions.");
      }
      else {
         throw new SQLiteFullException("Too many Profiles in database.");
      }
      return conditions;
   }

   public String toString(String dbName) {
      Cursor cursor = database.rawQuery("SELECT * FROM " + dbName, null);
      String[] cols = cursor.getColumnNames();
      String result = "";
      for(String col: cols) {
         result += col + "   ";
      }
      result += "\n";
      while(cursor.moveToNext()) {
         for(int i = 0; i < cursor.getColumnCount(); i++) {
            result += cursor.getString(i) + "   ";
         }
         result += "\n";
      }
      return result;
   }
}