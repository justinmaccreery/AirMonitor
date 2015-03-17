package jimjams.airmonitor.database;

import android.provider.BaseColumns;

/**
 * AirMonitor database contract.
 * @author Sean
 */
public interface AMDBContract {

   /**
    * Database filename
    */
   public static String DB_NAME = "AirMonitor.db";

   /**
    * Database version number
    */
   public static int DB_VERSION = 1;

   /**
    * Column name for ID
    */
   public static final String COLUMN_NAME_ID = "id";

   /**
    * Column type for ID
    */
   public static final String COLUMN_TYPE_ID = "NOT NULL";

   /**
    * Primary key instruction
    */
   public static final String PRIMARY_KEY = "PRIMARY_KEY(" + COLUMN_NAME_ID + ");";

   public static final class ProfileTable implements BaseColumns {
      public static final String TABLE_NAME = "profile";

      public static final String COLUMN_NAME_CONDITIONS = "conditions";

      public static final String COLUMN_TYPE_CONDITIONS = "ARRAY";

      public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_NAME_ID + " " + COLUMN_TYPE_ID + ", " +
            COLUMN_NAME_CONDITIONS  + " " + COLUMN_TYPE_CONDITIONS + ", " +
            PRIMARY_KEY;
   }

   public static final class EMATable implements BaseColumns {
      public static final String TABLE_NAME = "ema";

      public static final String COLUMN_NAME_INDOORS = "indoors";
      public static final String COLUMN_NAME_REPORTED_LOCATION = "reportedLocation";
      public static final String COLUMN_NAME_ACTIVITY = "activity";
      public static final String COLUMN_NAME_COMPANIONS = "companions";
      public static final String COLUMN_NAME_AIR_QUALITY = "airQuality";
      public static final String COLUMN_NAME_BELIEF = "belief";
      public static final String COLUMN_NAME_INTENTION = "intention";
      public static final String COLUMN_NAME_BEHAVIOR = "behavior";
      public static final String COLUMN_NAME_BARRIER = "barrier";

      public static final String COLUMN_TYPE_INDOORS = "BOOLEAN";
      public static final String COLUMN_TYPE_REPORTED_LOCATION = "TEXT";
      public static final String COLUMN_TYPE_ACTIVITY = "TEXT";
      public static final String COLUMN_TYPE_COMPANIONS = "ARRAY";
      public static final String COLUMN_TYPE_AIR_QUALITY = "INTEGER";
      public static final String COLUMN_TYPE_BELIEF = "INTEGER";
      public static final String COLUMN_TYPE_INTENTION = "INTEGER";
      public static final String COLUMN_TYPE_BEHAVIOR = "BOOLEAN";
      public static final String COLUMN_TYPE_BARRIER = "TEXT";

      public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_NAME_ID + " " + COLUMN_TYPE_ID + ", " +
            COLUMN_NAME_INDOORS + " " + COLUMN_TYPE_INDOORS + ", " +
            COLUMN_NAME_REPORTED_LOCATION + " " + COLUMN_TYPE_REPORTED_LOCATION + ", " +
            COLUMN_NAME_ACTIVITY + " " + COLUMN_TYPE_ACTIVITY + ", " +
            COLUMN_NAME_COMPANIONS + " " + COLUMN_TYPE_COMPANIONS + ", " +
            COLUMN_NAME_AIR_QUALITY + " " + COLUMN_TYPE_AIR_QUALITY + ", " +
            COLUMN_NAME_BELIEF + " " + COLUMN_TYPE_BELIEF + ", " +
            COLUMN_NAME_INTENTION + " " + COLUMN_TYPE_INTENTION + ", " +
            COLUMN_NAME_BEHAVIOR + " " + COLUMN_TYPE_BEHAVIOR + ", " +
            COLUMN_NAME_BARRIER + " " + COLUMN_TYPE_BARRIER + ", " +
            PRIMARY_KEY;
   }


   public static final class SnapshotTable implements BaseColumns {
      public static final String TABLE_NAME = "snapshot";

      public static final String COLUMN_NAME_LOCATION = "location";
      public static final String COLUMN_NAME_DATA = "data";
      public static final String COLUMN_NAME_CONDITIONS = "conditions";
      public static final String COLUMN_NAME_EMA = "ema";

      public static final String COLUMN_TYPE_LOCATION = "location";
      public static final String COLUMN_TYPE_DATA = "data";
      public static final String COLUMN_TYPE_CONDITIONS = "conditions";
      public static final String COLUMN_TYPE_EMA = "ema";

      public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_NAME_LOCATION + " " + COLUMN_TYPE_LOCATION + ", " +
            COLUMN_NAME_DATA + " " + COLUMN_TYPE_DATA + ", " +
            COLUMN_NAME_CONDITIONS + " " + COLUMN_TYPE_CONDITIONS + ", " +
            COLUMN_NAME_EMA + " " + COLUMN_TYPE_EMA + ", " +
            PRIMARY_KEY;
   }
}
