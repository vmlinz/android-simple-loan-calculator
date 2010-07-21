package ee.smkv.calc.loan;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

/**
 * @author Andrei Samkov
 */
public class Settings {
  SharedPreferences settings;

  public Settings(SharedPreferences settings) {
    this.settings = settings;
  }

  public void loadTextViews(TextView... views){
    for (TextView view : views){
      view.setText( settings.getString("" + view.getId() , "" ) );
    }
  }

  public void storeTextViews(TextView... views){
    SharedPreferences.Editor editor = settings.edit();
    for (TextView view : views){
      editor.putString("" + view.getId() , view.getText().toString());
    }
    editor.commit();
  }
}
