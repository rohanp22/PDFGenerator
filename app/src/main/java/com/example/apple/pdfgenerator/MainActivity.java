package com.example.apple.pdfgenerator;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView name, contact, email, address, date, packagee, petName, paymentDate, petBreed, subscriptionStartDate, subscriptionEndDate, petAge, petWeight;
    private ListView items;
    private List<InvoiceItems> invoiceItems;

    private File pdfFile;
    Button btn;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private static final String TAG = "PdfCreatorActivity";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdfWrapper();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdfWrapper() throws IOException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf() throws IOException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        pdfFile = new File(docsFolder.getAbsolutePath(), "HelloWorld.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2178, 3106, 1).create();

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.layout_pdf, null);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Regular.ttf");

        TextView invoice =  content.findViewById(R.id.invoice);
        invoice.setTypeface(custom_font);

        invoiceItems = new ArrayList<>();
        items =  content.findViewById(R.id.listview);
        name = (TextView) content.findViewById(R.id.name);
        contact = (TextView) content.findViewById(R.id.contact);
        email = (TextView) content.findViewById(R.id.email);
        address = (TextView) content.findViewById(R.id.address);
        date = (TextView) content.findViewById(R.id.date);
        packagee = (TextView) content.findViewById(R.id.packagee);
        petName = (TextView) content.findViewById(R.id.pet_name);
        paymentDate = (TextView) content.findViewById(R.id.payment_date);
        petBreed = (TextView) content.findViewById(R.id.pet_breed);
        subscriptionEndDate = (TextView) content.findViewById(R.id.subscription_end_date);
        subscriptionStartDate = (TextView) content.findViewById(R.id.subscription_start_date);
        petAge = (TextView) content.findViewById(R.id.pet_age);
        petWeight = (TextView) content.findViewById(R.id.pet_weight);

        invoiceItems.add(new InvoiceItems("1", "Cap", "1kg", "200"));
        invoiceItems.add(new InvoiceItems("2", "cap", "2kg", "400"));

        InvoiceAdapter adapter = new InvoiceAdapter(getApplicationContext(), invoiceItems);

        items.setAdapter(adapter);

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);

        content.measure(measureWidth, measuredHeight);
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());

        content.draw(page.getCanvas());
        document.finishPage(page);
        document.writeTo(output);
        document.close();

    }

    private void viewPdf() {

        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/HelloWorld.pdf"), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            startActivity(intent);
            Log.e("IR", "No exception");
        } catch (ActivityNotFoundException e) {
            Log.e("IR", "error: " + e.getMessage());
            Toast.makeText(MainActivity.this,
                    "No Application Available to View PDF",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
