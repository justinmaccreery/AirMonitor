package jimjams.airmonitor.datastructure;

/**
 * Represents an existing condition at the time a Snapshot is taken.
 * @author Sean
 */
public class ExistingCondition {

   /**
    * The name of the condition
    */
   private String name;

   /**
    * Returns the name of the condition.
    * @return The name of the condition
    */
   public String getName() {
      return name;
   }

   /**
    * Returns true if the specified condition has the same name as this condition.
    * @param other The ExistingCondition that this condition is to be compared to
    * @return true if the two conditions are the same
    */
   public boolean equals(ExistingCondition other) {
      return this.name.equalsIgnoreCase(other.name);
   }
}
