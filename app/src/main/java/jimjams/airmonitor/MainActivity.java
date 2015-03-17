package jimjams.airmonitor;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import jimjams.airmonitor.sensordata.SensorData;
import jimjams.airmonitor.sensordata.SensorDataGenerator;


public class MainActivity extends ActionBarActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      refreshAQInset();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_main, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();

      //noinspection SimplifiableIfStatement
      if(id == R.id.action_settings) {
         return true;
      }

      return super.onOptionsItemSelected(item);
   }

   /**
    * Refreshes current air quality data in the AirQualityInset.
    */
   private void refreshAQInset() {
      // Get updated data
      ArrayList<SensorData> data = SensorDataGenerator.getInstance().getData();

      // Create and populate a table
      TableLayout aqi = (TableLayout)findViewById(R.id.mainScreen_airQualityInset);
      aqi.removeAllViews();

      // Need to use LinearLayout instead of TableRow to get spanning to work
      LinearLayout header = new LinearLayout(this);
      header.setGravity(Gravity.CENTER_HORIZONTAL);

      TextView tv = new TextView(this);
      tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

      if(data == null || data.size() == 0) {
         tv.setText(getResources().getString(R.string.mainScreen_airQualityInset_no_data));
         header.addView(tv);
         aqi.addView(header);
      }
      else {
         tv.setText(getResources().getString(R.string.mainScreen_airQualityInset_data_header));
         header.addView(tv);
         aqi.addView(header);
         for(SensorData sd: data) {
            TableRow tr = new TableRow(this);
            TextView label = new TextView(this), value = new TextView(this);
            label.setText(sd.getDisplayName());
            label.setPadding(2, 2, 2, 5);
            value.setText(sd.getDisplayValue());
            value.setPadding(5, 2, 2, 2);
            value.setGravity(Gravity.END);
            tr.addView(label);
            tr.addView(value);
            aqi.addView(tr);
         }
      }
   }

   /**
    * Invoked when the EMA button on the main screen is clicked.
    * @param emaBtn The EMA button on the main screen
    */
   public void on_MainScreen_EMA_button_Click(View emaBtn) {
      Intent intent = new Intent(this, EMAScreenActivity.class);
      startActivity(intent);
   }

   /**
    * Invoked when the refresh button on the main screen is clicked.
    * @param rfrshBtn The refresh button on the main screen
    */
   public void on_MainScreen_refresh_button_Click(View rfrshBtn) {
      refreshAQInset();
   }
}