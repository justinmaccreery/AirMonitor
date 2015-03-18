package jimjams.airmonitor.datastructure;

import android.util.Log;

import java.util.ArrayList;

/**
 * User profile.
 * @author Sean
 */
public class Profile {

   private String className = getClass().getSimpleName();

   /**
    * The user's ID number. Initially this is set to 0; when the profile is first uploaded to the
    * nonlocal database, a new, unique id is assigned.
    */
   private int id;

   /**
    * Existing conditions
    */
   private ArrayList<ExistingCondition> conditions;

   /**
    * Current instance of Profile
    */
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
         Log.i(className, "Adding " + condition.getName() + ".");
      }
      else {
         Log.i(className, "Failed to add " + condition.getName() + " (duplicate).");
      }
   }

   /**
    * Attempts to add a new condition to the user's Profile. Will not add a duplicate condition.
    * @param name The name of the condition to be added
    */
   public void addCondition(String name) {
      addCondition(new ExistingCondition(name));
   }

   /**
    * Attempts to remove the specified ExistingCondition from the Profile.
    * @param condition The condition to be removed
    */
   public void removeCondition(ExistingCondition condition) {
      if(conditions.remove(condition)) {
         Log.i(className, "Removing " + condition.getName() + ".");
      }
      else {
         Log.i(className, "Failed to remove " + condition.getName() + ".");
      }
   }

   /**
    * Attempts to remove the specified ExistingCondition from the Profile.
    * @param name The name of the condition to be removed
    */
   public void removeCondition(String name) {
      removeCondition(new ExistingCondition(name));
   }

   /**
    * Returns the ArrayList of existing conditions.
    * @return ArrayList of existing conditions
    */
   public ArrayList<ExistingCondition> getConditions() {
      return conditions;
   }
}