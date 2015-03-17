package jimjams.airmonitor.datastructure;

import java.util.ArrayList;

/**
 * User profile.
 * @author Sean
 */
public class Profile {

   /**
    * The user's ID number. Initially this is set to 0; when the profile is first uploaded to the
    * nonlocal database, a new, unique id is assigned.
    */
   private int id;

   /**
    * Existing conditions
    */
   private ArrayList<ExistingCondition> conditions;

   private static Profile profile = null;

   /**
    * Constructor.
    */
   private Profile() {
      id = 0;
      conditions = new ArrayList<>();
   }

   public static Profile getProfile() {
      if(profile == null) {
         profile = new Profile();
      }
      return profile;
   }

   /**
    * Attempts to add a new condition to the user's Profile. Will not add a duplicate condition.
    * @param condition The condition to be added
    */
   public void addCondition(ExistingCondition condition) {
      boolean duplicate = false;
      for(ExistingCondition existing: conditions) {
         if(existing.equals(condition)) {
            duplicate = true;
         }
      }
      if(!duplicate) {
         conditions.add(condition);
      }
   }

   /**
    * Returns the ArrayList of existing conditions.
    * @return ArrayList of existing conditions
    */
   public ArrayList<ExistingCondition> getConditions() {
      return conditions;
   }
}