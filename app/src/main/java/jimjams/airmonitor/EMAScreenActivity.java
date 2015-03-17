package jimjams.airmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import java.util.ArrayList;

import jimjams.airmonitor.datastructure.EcologicalMomentaryAssessment;
import jimjams.airmonitor.datastructure.ExistingCondition;
import jimjams.airmonitor.datastructure.Profile;
import jimjams.airmonitor.datastructure.Snapshot;
import jimjams.airmonitor.sensordata.SensorData;
import jimjams.airmonitor.sensordata.SensorDataGenerator;


public class EMAScreenActivity extends ActionBarActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_ema_screen);
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
    * Invoked when the save button on the EMA screen is clicked. Generates a Snapshot based on
    * current sensor data, the user's existing conditions, and the results of the EMA.
    * @param saveBtn The save button
    */
   public void on_EMA_Screen_save_button_Click(View saveBtn) {
      ArrayList<SensorData> data = SensorDataGenerator.getInstance().getData();
      ArrayList<ExistingCondition> conditions = Profile.getProfile().getConditions();
      EcologicalMomentaryAssessment ema = getEma();
      Snapshot snapshot = new Snapshot(data, conditions, ema, this);
   }

   /**
    * Builds an EcologicalMomentaryAssessment based on screen input.
    * @return An EcologicalMomentaryAssessment
    */
   private EcologicalMomentaryAssessment getEma() {
      // Get the indoors value
      boolean indoors;
      RadioGroup indoorsRg = (RadioGroup)findViewById(R.id.ema_screen_in_or_out_group);
      int indoorsCheckedId = indoorsRg.getCheckedRadioButtonId();
      if(indoorsCheckedId == R.id.ema_screen_in_radio) {
         indoors = true;
      }
      else {
         indoors = false;
      }

      // Get reported location
      EditText repLoc = (EditText)findViewById(R.id.ema_screen_where_input);
      String reportedLocation = repLoc.getText().toString();

      // Get activity
      EditText act = (EditText)findViewById(R.id.ema_screen_what_input);
      String activity = act.getText().toString();

      // Get companions
      EditText comp = (EditText)findViewById(R.id.ema_screen_who_input);
      ArrayList<String> companions = new ArrayList<>();
      for(String c: comp.getText().toString().split("\n")) {
         companions.add(c);
      }

      // Get air quality
      SeekBar aqBar = (SeekBar)findViewById(R.id.ema_screen_aq_awareness_input);
      int aq = aqBar.getProgress();

      // Get belief
      SeekBar beliefBar = (SeekBar)findViewById(R.id.ema_screen_aq_belief_input);
      int belief = beliefBar.getProgress();

      // Get intention info
      SeekBar intentBar = (SeekBar)findViewById(R.id.ema_screen_aq_intent_input);
      int intention = intentBar.getProgress();

      // Get behavior info
      boolean behavior;
      RadioGroup behaveRg = (RadioGroup)findViewById(R.id.ema_screen_in_or_out_group);
      int behaveCheckedId = behaveRg.getCheckedRadioButtonId();
      if(behaveCheckedId == R.id.ema_screen_aq_behave_yes_radio) {
         behavior = true;
      }
      else {
         behavior = false;
      }

      // Get barrier info
      EditText barr = (EditText)findViewById(R.id.ema_screen_aq_barrier_input);
      String barrier = barr.toString();

      // Create the EMA
      return new EcologicalMomentaryAssessment(indoors, reportedLocation,
            activity, companions, aq, belief, intention, behavior, barrier);
   }

   /**
    * Invoked when the cancel button on the EMA screen is clicked.
    * @param cancelBtn The cancel button
    */
   public void on_EMA_Screen_cancel_button_Click(View cancelBtn) {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
   }
}