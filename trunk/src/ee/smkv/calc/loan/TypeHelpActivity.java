package ee.smkv.calc.loan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TypeHelpActivity extends Activity {
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.help);
	    
	    Button closeButton = (Button)findViewById(R.id.typeHelpCloseButton);
      closeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.close) ,null,null,null);
	    closeButton.setOnClickListener( new  View.OnClickListener(){

	      public void onClick(View view) {
	        finish();
	      }
	    });
	  }
}
