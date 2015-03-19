package jimjams.airmonitor.datastructure;

import android.database.sqlite.SQLiteFullException;
import android.util.Log;

import java.util.ArrayList;

import jimjams.airmonitor.database.DBAccess;

/**
 * User profile.
 * @author Sean
 */
public class Profile {

   /**
    * Used to identify source class for log
    */
   private String className = getClass().getSimpleName();

   /**
    * The user's ID number. Initially this is set to 0; when the profile is first uploaded to the
    * nonlocal database, a new, unique id is assigned.
    */
   private long id;

   /**
    * Existing conditions
    */
   private ArrayList<ExistingCondition> conditions;

   /**
    * Current instance of Profile
    */
   private static Profile profile = null;

   /**
    * Constructor. Will attempt to load a Profile from the database; otherwise an empty Profile with
    * id 0 will be generated.
    */
   private Profile() {
      DBAccess access = DBAccess.getDBAccess();
      try {
         id = access.getProfileId();
         conditions = access.getProfileConditions();
         Log.d(className, "Constructor: database accessed.");
      }
      catch(SQLiteFullException sqlfe) {
         id = 0;
         conditions = new ArrayList<ExistingCondition>();
         Log.w(className, "Too many Profiles in database; generating empty Profile.");
      }
      Log.d(className, toString());
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
      boolean empty = false;
      if(condition.getName().trim().length() == 0) {
         empty = true;
      }
      for(ExistingCondition existing: conditions) {
         if(existing.equals(condition)) {
            duplicate = true;
         }
      }

      if(duplicate) {
         Log.d(className, "Failed to add " + condition.getName() + " (duplicate).");
      }
      else if(empty) {
         Log.d(className, "Failed to add " + condition.getName() + " (empty).");
      }
      else {
         conditions.add(condition);
         Log.d(className, "Adding " + condition.getName() + ".");

         // Update database
         DBAccess.getDBAccess().updateProfile();
      }
      Log.d(className, toString());
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
         Log.d(className, "Removing " + condition.getName() + ".");
         DBAccess.getDBAccess().updateProfile();
      }
      else {
         Log.d(className, "Failed to remove " + condition.getName() + ".");
      }
      Log.d(className, toString());
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

   /**
    * Returns the profile's id.
    * @return The profile's id
    */
   public long getId() {
      return id;
   }

   /**
    * Returns a String representation of the Profile.
    * @return A String representation of the Profile
    */
   @Override
   public String toString() {
      String result = "id=" + id + "; conditions=";
      if(conditions.size() == 0) {
         result += "none";
      }
      else {
         for(int i = 0; i < conditions.size(); i++) {
            if(i != 0) {
               result += ", ";
            }
            result += conditions.get(i);
         }
      }
      return result;
   }
}