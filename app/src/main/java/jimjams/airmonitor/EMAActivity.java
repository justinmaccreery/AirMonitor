package jimjams.airmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import jimjams.airmonitor.database.DBAccess;
import jimjams.airmonitor.datastructure.EcologicalMomentaryAssessment;
import jimjams.airmonitor.datastructure.Profile;
import jimjams.airmonitor.datastructure.Snapshot;
import jimjams.airmonitor.sensordata.SensorData;
import jimjams.airmonitor.sensordata.SensorDataGenerator;


public class
      EMAActivity extends ActionBarActivity {

   /**
    * Used to identify source class for log
    */
   private String className = getClass().getSimpleName();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_ema);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_ema_screen, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();

      // noinspection SimplifiableIfStatement
      if(id == R.id.action_settings) {
         return true;
      }

      return super.onOptionsItemSelected(item);
   }

   /**
    * Overrides default method to populate existing conditions list.
    */
   protected void onStart() {
      super.onStart();
      // Log.d(className, DBAccess.getDBAccess().toString(AMDBContract.ProfileTable.TABLE_NAME));
      refreshExistingConditions();
   }

   /**
    * Invoked when the save button on the EMA screen is clicked. Generates a Snapshot based on
    * current sensor data, the user's existing conditions, and the results of the EMA.
    * @param saveBtn The save button
    */
   public void on_EMA_Screen_save_button_click(View saveBtn) {
      // Allows database access
      DBAccess access = DBAccess.getDBAccess();

      // Current profile
      Profile profile = Profile.getProfile();

      // Current set of sensor data
      ArrayList<SensorData> data = SensorDataGenerator.getInstance().getData();

      // Current set of existing conditions, taken from the Profile (note that the Profile is
      // updated every time a condition is added or deleted)
      ArrayList<String> conditions = profile.getConditions();

      // Current EMA, taken from the EMA screen
      EcologicalMomentaryAssessment ema = getEma();

      Snapshot snapshot = new Snapshot(data, conditions, ema, this);
      long snapshotId = access.saveSnapshot(snapshot);
      // Log.d(className, snapshot.toString());
      // Log.d(className, DBAccess.getDBAccess().toString(AMDBContract.SnapshotTable.TABLE_NAME));

      // Return to main screen
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
   }

   /**
    * Builds an EcologicalMomentaryAssessment based on screen input.
    * @return An EcologicalMomentaryAssessment
    */
   private EcologicalMomentaryAssessment getEma() {

      // Log.d(className, "Creating EMA.");

      // Get the indoors value
      RadioGroup indoorsRg = (RadioGroup)findViewById(R.id.ema_screen_in_or_out_group);
      int indoorsCheckedId = indoorsRg.getCheckedRadioButtonId();
      boolean indoors = (indoorsCheckedId == R.id.ema_screen_in_radio);

      // Get reported location
      EditText repLoc = (EditText)findViewById(R.id.ema_screen_where_input);
      String reportedLocation = normalize(repLoc.getText().toString());

      // Get activity
      EditText act = (EditText)findViewById(R.id.ema_screen_what_input);
      String activity = normalize(act.getText().toString());

      // Get companions
      EditText comp = (EditText)findViewById(R.id.ema_screen_who_input);
      // Tokenize by CR, semicolon, or comma
      ArrayList<String> companions =
            new ArrayList<>(Arrays.asList(comp.getText().toString().split("[\\n;,]")));
      for(int i = 0; i < companions.size(); i++) {
         companions.add(i, normalize(companions.remove(i)));
      }

      // Get air quality
      SeekBar aqBar = (SeekBar)findViewById(R.id.ema_screen_aq_awareness_input);
      int aq = ((int)(aqBar.getProgress() *.99 + 10)) / 10;

      // Get belief
      SeekBar beliefBar = (SeekBar)findViewById(R.id.ema_screen_aq_belief_input);
      int belief = ((int)(beliefBar.getProgress() *.99 + 10)) / 10;

      // Get intention info
      SeekBar intentBar = (SeekBar)findViewById(R.id.ema_screen_aq_intent_input);
      int intention = ((int)(intentBar.getProgress() *.99 + 10)) / 10;

      // Get behavior info
      RadioGroup behaveRg = (RadioGroup)findViewById(R.id.ema_screen_aq_behave_group);
      int behaveCheckedId = behaveRg.getCheckedRadioButtonId();
      boolean behavior = (behaveCheckedId == R.id.ema_screen_aq_behave_yes_radio);

      // Get barrier info
      EditText barr = (EditText)findViewById(R.id.ema_screen_aq_barrier_input);
      String barrier = normalize(barr.getText().toString());

      // Create the EMA
      return new EcologicalMomentaryAssessment(indoors, reportedLocation,
            activity, companions, aq, belief, intention, behavior, barrier);
   }

   /**
    * Invoked when the cancel button on the EMA screen is clicked.
    * @param cancelBtn The cancel button
    */
   public void on_EMA_Screen_cancel_button_click(View cancelBtn) {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
   }

   /**
    * Invoked when the cancel button on the EMA screen is clicked.
    * @param addBtn The add button
    */
   public void on_EMA_Screen_add_button_click(View addBtn) {
      EditText input = (EditText)findViewById(R.id.ema_screen_existing_input);
      Profile.getProfile().addCondition(normalize(input.getText().toString()));
      ((EditText)findViewById(R.id.ema_screen_existing_input)).setText("");
      DBAccess.getDBAccess().updateProfile();
      refreshExistingConditions();
   }

   /**
    * Refreshes the list of existing conditions based on profile.
    */
   private void refreshExistingConditions() {
      // Log.d(className, "Refreshing existing conditions list.");
      Profile profile = Profile.getProfile();
      TableLayout layout = (TableLayout)findViewById(R.id.ema_screen_existing_table);

      // Remove all but the header row
      layout.removeViews(1, layout.getChildCount() - 1);

      // Create a listener
      DeleteButtonListener listener = new DeleteButtonListener();

      // Create a new TableRow for each existing condition
      for(String condition: profile.getConditions()) {
         TableRow tr = new TableRow(this);
         TextView label = new TextView(this);
         label.setText(condition);
         DeleteButton delBtn = new DeleteButton(this, condition);
         delBtn.setOnClickListener(listener);
         tr.addView(label);
         tr.addView(delBtn);
         layout.addView(tr);
      }
   }

   /**
    * Normalizes space. This means that whitespace is replaced with a single space, and leading and
    * trailing spaces are removed.
    * @return The original text, normalized
    */
   private static String normalize(String str) {
      return str.replaceAll("\\s", " ").trim();
   }

   /**
    * Listener for DeleteButtons
    */
   private class DeleteButtonListener implements View.OnClickListener {
      /**
       * Invoked when a DeleteButton is clicked
       * @param v The DeleteButton
       */
      public void onClick(View v) {
         DeleteButton btn = (DeleteButton)v;
         Profile.getProfile().removeCondition(btn.getCondition());
         DBAccess.getDBAccess().updateProfile();
         refreshExistingConditions();
      }
   }
   /**
    * Button subclass to associate a condition with the Button
    */
   private class DeleteButton extends Button {
      /**
       * The associated condition
       */
      private String condition;
      /**
       * Constructor.
       * @param context Context (the containing Activity)
       * @param condition The associated condition
       */
      DeleteButton(Context context, String condition) {
         super(context);
         this.condition = condition;
         setText("X");
      }
      /**
       * Returns the associated condition.
       * @return The associated condition
       */
      String getCondition() {
         return condition;
      }
   }
}