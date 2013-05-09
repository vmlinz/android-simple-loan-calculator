package ee.smkv.calc.loan.export;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ee.smkv.calc.loan.R;
import ee.smkv.calc.loan.ThemeResolver;

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
                return 3;
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
                    imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setPadding(8, 8, 8, 8);
                } else {
                    imageView = (ImageView) convertView;
                }

                switch (position) {
                    case 0:
                        imageView.setImageResource(ThemeResolver.isLight(getContext()) ? R.drawable.txt : R.drawable.txt_dark);
                        break;
                    case 1:
                        imageView.setImageResource(ThemeResolver.isLight(getContext()) ? R.drawable.html : R.drawable.html_dark);
                        break;
                    case 2:
                        imageView.setImageResource(ThemeResolver.isLight(getContext()) ? R.drawable.pdf : R.drawable.pdf_dark);
                        break;
                }


                return imageView;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}



