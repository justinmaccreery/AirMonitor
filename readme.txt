Added a history activity.

   - The history screen will list the user's id and all associated
     Snapshots. This is not part of our original design, but I think it
     will be useful. At the very least it will help debugging because
     it will allow us to see what is actually stored in the database. A
     history button has been added to the main screen to access the
     history screen.

   - Got rid of the ExistingCondition class, as it was just a wrapper
     for a String. It's much simpler to handle these as Strings,
     especially when storing or accessing data in the database.

   - I'm having trouble generating Locations. The approach I've used so
     far just returns null. I'm also not sure how to store location info
     in the database, or how to reconstruct a Location from stored data.
     For now, Locations are null.

   - When you go to the EMA screen, the existing conditions are properly
     populated, but other EMA info is not. Hope to have this working
     tomorrow.