package jimjams.airmonitor.sensordata;

import java.util.ArrayList;

/**
 * Generates random sensor sensor data to test app. This class uses the
 * singleton pattern and a private constructor. Use {@link #getInstance()} to
 * access the <code>SensorDataGenerator</code> object.
 * @author Sean McCooey
 */
public class SensorDataGenerator {

   /**
    * The current instance of <code>SensorDataGenerator</code>
    */
   private static SensorDataGenerator instance = null;

   /**
    * Data categories to be generated
    */
   DataCategory[] dataCats = {
      new DataCategory("Carbon monoxide", "co", 0D, 1D, 2),
      new DataCategory("Temperature", "temp", 0D, 95D, 0),
      new DataCategory("Humidity", "humid", 10D, 100D, 0),
      new DataCategory("Nitrogen dioxide", "no2", 0D, 2D, 1)
   };

   /**
    * Constructor. Private to prevent unauthorized instantiation.
    */
   private SensorDataGenerator() {
   }

   /**
    * Returns the current instance of <code>SensorDataGenerator</code>.
    * @return The current instance of <code>SensorDataGenerator</code>
    */
   public static SensorDataGenerator getInstance() {
      if(instance == null) {
         instance = new SensorDataGenerator();
      }
      return instance;
   }

   /**
    * Returns sensor data as an array of <code>SensorData</code> objects.
    * Categories for which there is no data are not returned.
    * @return Sensor data
    */
   public SensorData[] getData() {
      ArrayList<SensorData> list = new ArrayList<>();
      for(DataCategory dataCat: dataCats) {
         if(rand(0, 1) > 0.2) {
            list.add(new SensorData(dataCat.displayName, dataCat.shortName,
                  rand(dataCat.min, dataCat.max), dataCat.decimalPlaces));
         }
      }
      SensorData[] data = new SensorData[list.size()];
      list.toArray(data);
      return data;
   }

   /**
    * Generates a random double in the specified range.
    * @param min Minimum value
    * @param max Maximum value
    * @return random double in the range (min, max)
    */
   private double rand(double min, double max) {
      double rand = Math.random();
      rand *= (max - min);
      rand += min;
      return rand;
   }

   /**
    * Groups data for a single type of reading. Used to construct
    * <code>SensorData</code>. Instance variable are package-private to allow
    * access from <code>SensorDataGenerator</code>.
    */
   private class DataCategory {

      /**
       * Display name for the data
       */
      String displayName;

      /**
       * Short name for the data
       */
      String shortName;

      /**
       * Minimum value
       */
      double min;

      /**
       * Maximum value
       */
      double max;

      /**
       * Number of decimal places to be displayed
       */
      int decimalPlaces;

      /**
       * Constructor.
       *
       * @param displayName   Display name for the data
       * @param shortName     Short name for the data
       * @param min           Minimum value
       * @param max           Maximum value
       * @param decimalPlaces Number of decimal places to be displayed
       */
      DataCategory(String displayName, String shortName, double min, double max, int decimalPlaces) {
         this.displayName = displayName;
         this.shortName = shortName;
         this.min = min;
         this.max = max;
         this.decimalPlaces = decimalPlaces;
      }
   }
}