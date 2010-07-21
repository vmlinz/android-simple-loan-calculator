package ee.smkv.calc.loan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * @author Andrei Samkov
 */
public class ErrorDialogWrapper implements  DialogInterface.OnClickListener{

  private AlertDialog dialog;

  public ErrorDialogWrapper(Activity activity ,Exception e) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setMessage("" + e.getMessage());
    builder.setPositiveButton("OK", this);
    dialog = builder.create();
  }

  public void show(){
    dialog.show();
  }

  public void onClick(DialogInterface dialogInterface, int i) {
     dialog.cancel();
  }
}
