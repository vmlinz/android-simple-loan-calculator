package ee.smkv.calc.loan.export;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * @author samko
 */
public class Exporter {
  public static void sendToEmail(Loan loan, Resources resources, Activity activity) throws FileCreationException {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/text");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name) + " - export");

        StringBuilder sb = new StringBuilder();
        new TextScheduleCreator(loan, resources).appendTextScheduleTable(sb);
        emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportToCSVFile(loan,resources)));
        emailIntent.setType("text/csv");
        activity.startActivity(Intent.createChooser(emailIntent, resources.getString(R.string.sendEmail)));
    }

    public static File exportToCSVFile(Loan loan, Resources resources) throws FileCreationException {
        try {
            CSVScheduleCreator csvScheduleCreator = new CSVScheduleCreator(loan, resources);
            csvScheduleCreator.assertDataWriteEnabled();
            String fileName = csvScheduleCreator.getFileName();
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = new File(externalStorageDirectory.getPath() + File.separator + fileName);

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), CSVScheduleCreator.ENCODING);
            csvScheduleCreator.createSchedule(writer);
            writer.flush();
            writer.close();
            return file;
        } catch (Exception e) {
           throw new FileCreationException("Creating file on SD card fail: " +e.getMessage() , e);
        }
    }
}
