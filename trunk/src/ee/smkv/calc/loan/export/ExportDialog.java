package ee.smkv.calc.loan.export;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ee.smkv.calc.loan.R;
import ee.smkv.calc.loan.StartActivity;
import ee.smkv.calc.loan.ThemeResolver;
import ee.smkv.calc.loan.model.Loan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExportDialog extends Dialog {


    public ExportDialog(Context context) {
        super(context);
    }

    public ExportDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ExportDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_dialog);
        setTitle(R.string.exportToEmail);
        GridView gridView = (GridView) findViewById(R.id.exportGridView);
        findViewById(R.id.exportCloseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                if (convertView == null) {  // if it's not recycled, initialize some attributes
                    imageView = new ImageView(getContext());
//                    imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setPadding(8, 8, 8, 8);
                } else {
                    imageView = (ImageView) convertView;
                }

                switch (position) {
                    case 0:
                        imageView.setImageResource(R.drawable.txt );
                        break;
                    case 1:
                        imageView.setImageResource( R.drawable.html);
                        break;

                }


                return imageView;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
              try {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                Uri uri  = null;
                switch (position) {
                  case 0:
                    uri = createTxtExport(StartActivity.loan);
                    break;
                  case 1:
                    uri = createHtmlExport(StartActivity.loan);
                    break;

                }

                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getContext().getString(R.string.app_name));
                sendIntent.putExtra(Intent.EXTRA_TEXT, "File attached.");
                sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                getContext().startActivity(Intent.createChooser(sendIntent, "Select application"));
              }
              catch (IOException e) {
                e.printStackTrace();
              }
            }


        });
    }

  private Uri createHtmlExport(Loan loan) throws IOException {
    FileOutputStream fos = null;
    try {
      String name = "loan_export.html";
      fos = getContext().openFileOutput(name, Context.MODE_WORLD_READABLE);
      File file = new File(getContext().getFilesDir(), name);

      fos.write("Test".getBytes());

      return  Uri.fromFile(file);
    }
    finally {
      if (fos != null) {
        fos.close();
      }
    }


  }

  private Uri createTxtExport(Loan loan) {
    return null;
  }
}



