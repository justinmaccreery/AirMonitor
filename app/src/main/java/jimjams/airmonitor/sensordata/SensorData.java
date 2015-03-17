package jimjams.airmonitor.sensordata;

import java.text.DecimalFormat;

/**
 * Represents a single data reading from a single sensor.
 * @author Sean
 */
public class SensorData {

   /**
    * <code>DecimalFormat</code>s used to format values for display
    */
   private static DecimalFormat[] df = {
      new DecimalFormat("0"),
      new DecimalFormat("0.0"),
      new DecimalFormat("0.00"),
      new DecimalFormat("0.000")
   };

   /**
    * Display name for the data
    */
   private String displayName;

   /**
    * Short name for the data
    */
   private String shortName;

   /**
    * Value of the reading
    */
   private double value;

   /**
    * Rounded value to be displayed
    */
   private String displayValue;

   /**
    * Constructor.
    * @param displayName   Display name for the data
    * @param shortName     Short name for the data
    * @param value         Value of the reading
    * @param decimalPlaces Number of decimal places to be used in displayValue
    * @param unit          Unit of measure
    */
   public SensorData(String displayName, String shortName, double value, int decimalPlaces,
                     String unit) {
      this.displayName = displayName;
      this.shortName = shortName;
      this.value = value;
      this.displayValue = df[decimalPlaces].format(value) + unit;
   }

   /**
    * Returns the display name for the data
    */
   public String getDisplayName() {
      return displayName;
   }

   /**
    * Returns the short name for the data
    */
   public String getShortName() {
      return shortName;
   }

   /**
    * Returns the value of the reading
    */
   public double getValue() {
      return value;
   }

   /**
    * Returns the display value of the reading
    */
   public String getDisplayValue() {
      return displayValue;
   }
}