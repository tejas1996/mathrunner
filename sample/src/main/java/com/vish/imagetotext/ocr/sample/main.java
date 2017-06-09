package com.vish.imagetotext.ocr.sample;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;

import android.provider.Settings;
import android.system.ErrnoException;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import com.example.croppersample.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.*;

/**
 * Created by VisH on 19-06-2016.
 */
public class main  extends Activity {

    private CropImageView mCropImageView;
    Bitmap converted;
    public static EditText textView;
    public static EditText textV2;
    public String st;
    float ansss;
    int a,b,c;

    double x,y;
    anss an=new anss();

    private TessOCR mTessOCR;
    public static Matcher matcher;
    private Uri mCropImageUri;
    public static Matcher matcher1 = null,matcher2=null;
    public static int v1,v2,v3,v4,v5,v6;
    String fin;
    public static final String lang = "eng";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/DemoOCR/";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        textView = (EditText)findViewById(R.id.editText);
        textV2 = (EditText)findViewById(R.id.editText1);

        mCropImageView = (CropImageView) findViewById(R.id.CropImageView);
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v("Main", "ERROR: Creation of directory " + path + " on sdcard failed");
                    break;
                } else {
                    Log.v("Main", "Created directory " + path + " on sdcard");
                }
            }

        }
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();

                InputStream in = assetManager.open(lang + ".traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                // Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                // Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }


        }
        mTessOCR =new TessOCR();
    }

    /**
     * On load image button click, start pick image chooser activity.
     */
    public void onLoadImageClick(View view) {
        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    /**
     * Crop the image and set it back to the cropping view.
     */

    public void onCropImageClick(View view) {
       Bitmap cropped = mCropImageView.getCroppedImage(500, 500);
        if (cropped != null)
            mCropImageView.setImageBitmap(cropped);

        //mImage.setImageBitmap(converted);
       doOCR(convertColorIntoBlackAndWhiteImage(cropped) );

    }

    public void onCropImageClick1(View view) {
        Bitmap cropped = mCropImageView.getCroppedImage(500, 500);
        if (cropped != null)

        //mImage.setImageBitmap(converted);
        doOCR1(convertColorIntoBlackAndWhiteImage(cropped) );

    }

    public void doOCR1(final Bitmap bitmap) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "Please wait...", true);
            // mResult.setVisibility(V.ViewISIBLE);


        } else {
            mProgressDialog.show();
        }

        new Thread(new Runnable() {
            public void run() {

                final String result = mTessOCR.getOCRResult(bitmap).toLowerCase();


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (result != null && !result.equals("")) {
                            String s = result;


                            textV2.setText(s);




                        }

                        mProgressDialog.dismiss();
                    }

                });

            }

            ;
        }).start();


    }


    public void doOCR(final Bitmap bitmap) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "Please wait...", true);
            // mResult.setVisibility(V.ViewISIBLE);


        }
        else {
            mProgressDialog.show();
        }

        new Thread(new Runnable() {
            public void run() {

                final String result = mTessOCR.getOCRResult(bitmap).toLowerCase();


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (result != null && !result.equals("")) {
                            String t = result;
                            Editable j=textV2.getText();
                            t=j.toString();
                            if (t.contains("|")) {
                                int index = t.indexOf("|");
                                String str1 = t.substring(0, index);
                                String str2 = t.substring(index + 1, t.length());
                                testeqn1(str1, str2);

                            }
                            else{
                            int type = testeqn(t);
                            t = " " + type;
                            switch (type) {
                                case 1:
                                    an = new anss();
                                    if (matcher.group(1).length() == 0) {
                                        a = 1;
                                    } else a = Integer.parseInt(matcher.group(1));
                                    if (matcher.group(3).length() == 0) {
                                        b = 1;
                                    } else b = Integer.parseInt(matcher.group(4));
                                    c = Integer.parseInt(matcher.group(7)) - Integer.parseInt(matcher.group(8));
                                    an = an.solquad(a, b, c);
                                    x = an.aa;
                                    y = an.bb;
                                    fin = "x=" + x + " " + "y=" + y;
                                    textView.setText(fin);
                                    break; // optional

                                case 2:
                                    if (matcher.group(1).length() == 0) {
                                        a = 1;
                                    } else
                                        a = Integer.parseInt(matcher.group(1));
                                    if (matcher.group(4).length() == 0) {
                                        b = 1;
                                    } else b = Integer.parseInt(matcher.group(4));
                                    c = Integer.parseInt(matcher.group(7));
                                    c = -c;
                                    an = new anss();

                                    an = an.solquad(a, b, c);
                                    x = an.aa;
                                    y = an.bb;
                                    fin = "x=" + x + " " + "y=" + y;
                                    textView.setText(fin);


                                    // Statements
                                    break; // optional

                                case 3:

                                    if (matcher.group(1).length() == 0) {
                                        a = 1;
                                    } else a = Integer.parseInt(matcher.group(1));

                                    b = 0;
                                    c = Integer.parseInt(matcher.group(4));
                                    c = -c;
                                    an = new anss();

                                    an = an.solquad(a, b, c);
                                    x = an.aa;
                                    y = an.bb;
                                    fin = "x=" + x + " " + "y=" + y;
                                    textView.setText(fin);


                                    // Statements
                                    break; // optional

                                case 4:


                                    if (matcher.group(1).length()==0) {
                                        a = 1;
                                    } else a = Integer.parseInt(matcher.group(1));

                                    b = Integer.parseInt(matcher.group(3));
                                    c = Integer.parseInt(matcher.group(4));
                                    c = b - c;
                                    b = 0;
                                    an = new anss();

                                    an = an.solquad(a, b, c);
                                    x = an.aa;
                                    y = an.bb;
                                    fin = "x=" + x + " " + "y=" + y;
                                    textView.setText(fin);


                                    // Statements
                                    break; // optional
                                case 5:

                                    if (matcher.group(1).length() == 0) {
                                        a = 1;
                                    } else
                                        a = Integer.parseInt(matcher.group(1));
                                    if (matcher.group(5).length() == 0) {
                                        b = 1;
                                    } else b = Integer.parseInt(matcher.group(5));
                                    b = -b;
                                    c = Integer.parseInt(matcher.group(8));
                                    int d = Integer.parseInt(matcher.group(4));
                                    c = d - c;


                                    an = new anss();

                                    an = an.solquad(a, b, c);
                                    x = an.aa;
                                    y = an.bb;
                                    fin = "x=" + x + " " + "y=" + y;
                                    textView.setText(fin);


                                    // Statements
                                    break; // optional
                                case 6:

                                    if (matcher.group(1).length() == 0) {
                                        a = 1;
                                    } else
                                        a = Integer.parseInt(matcher.group(1));
                                    if (matcher.group(4).length() == 0) {
                                        b = 1;
                                    } else b = Integer.parseInt(matcher.group(4));
                                    b = -b;
                                    c = Integer.parseInt(matcher.group(7));
                                    c = -c;


                                    an = new anss();

                                    an = an.solquad(a, b, c);
                                    x = an.aa;
                                    y = an.bb;
                                    fin = "x=" + x + " " + "y=" + y;
                                    textView.setText(fin);

                                    // Statements
                                    break; // optional

                                case 7:

                                    String z = matcher.group(4);
                                    String zx = "-";
                                    if (matcher.group(4).length() == 0) {
                                        a = 1;
                                    } else if (z.equalsIgnoreCase(zx)) {
                                        a = -1;

                                    } else {
                                        a = Integer.parseInt(matcher.group(4));
                                    }
                                    a = -a;
                                    if (matcher.group(1).length() == 0) {
                                        b = 1;
                                    } else b = Integer.parseInt(matcher.group(1));

                                    c = Integer.parseInt(matcher.group(7));
                                    c = -c;
                                    b = -b;

                                    an = new anss();

                                    an = an.solquad(a, b, c);
                                    x = an.aa;
                                    y = an.bb;
                                    fin = "x=" + x + " " + "y=" + y;
                                    textView.setText(fin);

                                case 8:

                                    String z1 = matcher.group(2);
                                    String zx1 = "-";
                                    if (matcher.group(2).length() == 0) {
                                        a = 1;
                                    } else if (z1.equalsIgnoreCase(zx1)) {
                                        a = -1;
                                    } else {
                                        a = Integer.parseInt(matcher.group(2));
                                    }
                                    a = -a;

                                    z1 = matcher.group(5);
                                    zx1 = "+";
                                    String zx2 = "-";
                                    if (z1.equalsIgnoreCase(zx1)) {
                                        b = 1;
                                    } else if (z1.equalsIgnoreCase(zx2)) {
                                        b = -1;
                                    } else b = Integer.parseInt(matcher.group(5));

                                    b = -b;
                                    c = Integer.parseInt(matcher.group(1));


                                    an = new anss();

                                    an = an.solquad(a, b, c);
                                    x = an.aa;
                                    y = an.bb;
                                    fin = "x=" + x + " " + "y=" + y;
                                    textView.setText(fin);


                                    // Statements
                                    break; // optional


                                case 9:
                                    // Statements
                                    float o, aa3;

                                    String z2 = matcher.group(1);
                                    String zx3 = "-";
                                    if (matcher.group(1).length() == 0) {
                                        aa3 = 1;
                                    } else if (z2.equalsIgnoreCase(zx3)) {
                                        aa3 = -1;
                                    } else {
                                        aa3 = Integer.parseInt(matcher.group(1));
                                    }


                                    float bb3 = Integer.parseInt(matcher.group(5));

                                    float cc3 = Integer.parseInt(matcher.group(6));
                                    String xc = String.valueOf(matcher.group(4));
                                    String r = "+";
                                    String r1 = "-";
                                    String r2 = "*";
                                    String r4 = "/";
                                    if (xc.equalsIgnoreCase(r)) {
                                        o = (cc3 - bb3) / aa3;
                                        fin = o + " ";
                                        textView.setText(fin);

                                    } else if (xc.equalsIgnoreCase(r1)) {
                                        o = (cc3 + bb3) / aa3;
                                        fin = o + " ";
                                        textView.setText(fin);

                                    } else if (xc.equalsIgnoreCase(r2)) {
                                        o = (cc3 / bb3) / aa3;
                                        fin = o + " ";
                                        textView.setText(fin);

                                    } else {
                                        o = (cc3 * bb3) / aa3;
                                        fin = o + " ";
                                        textView.setText(fin);

                                    }


                                    break; // optional


                                case 10:
                                    // Statements
                                    float aa2 = Integer.parseInt(matcher.group(1));
                                    float bb2 = Integer.parseInt(matcher.group(3));
                                    float ann2 = aa2 / bb2;
                                    fin = ann2 + " ";
                                    textView.setText(fin);


                                    break; // optional


                                case 11:
                                    // Statements
                                    float aa1 = Integer.parseInt(matcher.group(1));
                                    float bb1 = Integer.parseInt(matcher.group(3));
                                    float ann1 = aa1 * bb1;
                                    fin = ann1 + " ";
                                    textView.setText(fin);


                                    break; // optional


                                case 12:
                                    float aa = Integer.parseInt(matcher.group(1));
                                    float bb = Integer.parseInt(matcher.group(2));
                                    float ann = aa + bb;
                                    fin = ann + " ";
                                    textView.setText(fin);


                                    break;

                                case 13 :
                                    double angle=Integer.parseInt(matcher.group(2));

                                    double rad=Math.toRadians(angle);
                                    double ann5=Math.sin(rad);
                                    fin = ann5 + " ";
                                    textView.setText(fin);


                                    break;

                                case 14 :
                                    double angle1=Integer.parseInt(matcher.group(2));

                                    double rad1=Math.toRadians(angle1);
                                    double ann51=Math.cos(rad1);
                                    fin = ann51 + " ";
                                    textView.setText(fin);


                                    break;

                                case 15 :
                                    double angle2=Integer.parseInt(matcher.group(2));

                                    double rad2=Math.toRadians(angle2);
                                    double ann52=Math.tan(rad2);
                                    fin = ann52 + " ";
                                    textView.setText(fin);


                                    break;

                                case 16 :
                                    double angle3=Integer.parseInt(matcher.group(2));

                                    double rad3=Math.toRadians(angle3);
                                    double ann53=Math.sin(rad3);
                                    ann53=1.0/ann53;
                                    fin = ann53 + " ";
                                    textView.setText(fin);


                                    break;

                                case 17 :
                                    double angle4=Integer.parseInt(matcher.group(2));

                                    double rad4=Math.toRadians(angle4);
                                    double ann54=Math.cos(rad4);
                                    ann54=1.0/ann54;
                                    fin = ann54 + " ";
                                    textView.setText(fin);


                                    break;

                                case 18 :
                                    double angle5=Integer.parseInt(matcher.group(2));

                                    double rad5=Math.toRadians(angle5);
                                    double ann55=Math.sin(rad5);
                                    ann55=1.0/ann55;
                                    fin = ann55 + " ";
                                    textView.setText(fin);


                                    break;

                                case 19 :
                                    double ang=Integer.parseInt(matcher.group(1));
                                    double ang1=Integer.parseInt(matcher.group(2));


                                    double ant=ang+ang1;
                                    fin = ant + " ";

                                    textView.setText(fin);


                                    break;

                                case 20 :
                                    double ang12=Integer.parseInt(matcher.group(1));
                                    double ang11=Integer.parseInt(matcher.group(2));
                                    double ang131=Integer.parseInt(matcher.group(3));



                                    double ant1=ang12+ang11+ang131;
                                    fin = ant1 + " ";

                                    textView.setText(fin);


                                    break;
                                case 21 :
                                    double ang121=Integer.parseInt(matcher.group(1));
                                    double ang111=Integer.parseInt(matcher.group(2));
                                    double ang1311=Integer.parseInt(matcher.group(3));
                                    double ang1341=Integer.parseInt(matcher.group(4));


                                    double ant12=ang121+ang111+ang1311+ang1341;
                                    fin = ant12 + " ";

                                    textView.setText(fin);


                                    break;

                                case 22 :
                                    double ang122=Integer.parseInt(matcher.group(1));
                                    double ang112=Integer.parseInt(matcher.group(2));
                                    double ang1312=Integer.parseInt(matcher.group(3));
                                    double ang1342=Integer.parseInt(matcher.group(4));
                                    double ang13422=Integer.parseInt(matcher.group(5));

                                    double ant122=ang122+ang112+ang1312+ang1342+ang13422;
                                    fin = ant122 + " ";

                                    textView.setText(fin);


                                    break;
                                case 23 :
                                    double ang123=Integer.parseInt(matcher.group(1));
                                    double ang113=Integer.parseInt(matcher.group(2));
                                    double ang1313=Integer.parseInt(matcher.group(3));
                                    double ang1343=Integer.parseInt(matcher.group(4));
                                    double ang13423=Integer.parseInt(matcher.group(5));
                                    double ang134233=Integer.parseInt(matcher.group(6));

                                    double ant123=ang123+ang113+ang1313+ang1343+ang13423+ang134233;
                                    fin = ant123 + " ";

                                    textView.setText(fin);


                                    break;

                                case 24 :
                                    double ang124=Integer.parseInt(matcher.group(1));
                                    double ang114=Integer.parseInt(matcher.group(2));
                                    double ang1314=Integer.parseInt(matcher.group(3));
                                    double ang1344=Integer.parseInt(matcher.group(4));
                                    double ang13424=Integer.parseInt(matcher.group(5));
                                    double ang134234=Integer.parseInt(matcher.group(6));
                                    double ang134235=Integer.parseInt(matcher.group(7));
                                    double ant124=ang124+ang114+ang1314+ang1344+ang13424+ang134234+ang134235;
                                    fin = ant124 + " ";

                                    textView.setText(fin);


                                    break;














                                default: // Optional
                                    // Statements
                                    textView.setText("We are not ready for this..Sorry!!");
                            }


                        }
                        }

                        mProgressDialog.dismiss();
                    }

                });

            };
        }).start();


    }

    public static int testeqn(String str) {
        final Pattern quadeq1 = Pattern.compile("([+-]?([\\d]+)?)\\s*([a-zA-Z])2\\s*([+-]([\\d]+)?)\\s*([a-zA-Z])\\s*([+-][\\d]+)\\s*=\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq2 = Pattern.compile("([+-]?([\\d]+)?)\\s*([a-zA-Z])2\\s*([+-]([\\d+]+)?)\\s*([a-zA-Z])\\s*=\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq3 = Pattern.compile("([+-]?([\\d]+)?)\\s*([a-zA-Z])2\\s*=\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq4 = Pattern.compile("([+-]?[\\d]+)?\\s*([a-zA-Z])2\\s*([+-][\\d]+)?\\s*=\\s*([+-]?[\\d]+)?\\s*");
        final Pattern quadeq5 = Pattern.compile("([+-]?([\\d]+)?)\\s*([a-zA-Z])2\\s*([+-][\\d]+)\\s*=\\s*([+-]?([\\d]+)?)\\s*([a-zA-Z])\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq6 = Pattern.compile("([+-]?([\\d]+)?)?\\s*([a-zA-Z])2\\s*=\\s*([+-]?([\\d]+)?)?\\s*([a-zA-Z])\\s*([+-][\\d]+)?\\s*");
        final Pattern quadeq7 = Pattern.compile("([+-]?([\\d]+)?)?\\s*([a-zA-Z])\\s*=\\s*([+-]?([\\d]+)?)?\\s*([a-zA-Z])2\\s*([+-]?\\s*[\\d]+)?\\s*");
        final Pattern quadeq8 = Pattern.compile("([+-]?[\\d]+)\\s*=\\s*([+-]?([\\d]+)?)\\s*([a-zA-Z])2\\s*([+-]?([\\d]+)?)\\s*([a-zA-Z])\\s*");
        final Pattern quadeq9 = Pattern.compile("([+-]?([\\d]+)?)\\s*([a-zA-Z])\\s*([*]?[+-]?[/]?)\\s*([\\d]+)\\s*=\\s*(\\d+)\\s*");
        final Pattern quadeq10 = Pattern.compile("([+-]?[\\d]+)\\s*([/])\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq11 = Pattern.compile("([+-]?[\\d]+)\\s*([*])\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq12 = Pattern.compile("([+-]?[\\d]+)\\s*([+-][\\d]+)\\s*");
        final Pattern quadeq13 = Pattern.compile("(sin)\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq14 = Pattern.compile("(cos)\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq15 = Pattern.compile("(tan)\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq16 = Pattern.compile("(cosec)\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq17 = Pattern.compile("(sec)\\s*([+-]?[\\d]+)\\s*");
        final Pattern quadeq18 = Pattern.compile("(cot)\\s*([+-]?[\\d]+)\\s*");
        final Pattern eqn2 = Pattern.compile("(\\d+)\\s*(\\d+)");
        final Pattern eqn3 = Pattern.compile("(\\d+)\\s(\\d+)\\s(\\d+)");
        final Pattern eqn4 = Pattern.compile("(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)");
        final Pattern eqn5 = Pattern.compile("(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)");
        final Pattern eqn6 = Pattern.compile("(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)");
        final Pattern eqn7 = Pattern.compile("(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)");







        Pattern p[]= {quadeq1,quadeq2,quadeq3,quadeq4,quadeq5,quadeq6,quadeq7,quadeq8,quadeq9,quadeq10,quadeq11,quadeq12,quadeq13,quadeq14,quadeq15,quadeq16,quadeq17,quadeq18,eqn2,eqn3,eqn4,eqn5,eqn6,eqn7};


        for(int i=0;i<24;i++){
            matcher=p[i].matcher(str);
            if (matcher.matches()) {
                return i+1;
            }
        }




        return 0;
    }

    public static void testeqn1(String str1,String str2) {
        int test1 = 0,test2 = 0;
        final Pattern lineq1 = Pattern.compile("\\s*([+-]?[\\d]+)\\s*([a-zA-Z])\\s*=\\s*(\\d+)\\s*");
        final Pattern lineq2 = Pattern.compile("\\s*([+-]?[\\d]+)\\s*([a-zA-Z])\\s*([+-]\\s*[\\d]+)\\s*=\\s*(\\d+)\\s*");
        final Pattern lineq3 = Pattern.compile("\\s*([+-]?[\\d]+)\\s*([a-zA-Z])\\s*([+-]\\s*[\\d]+)\\s*([a-zA-Z])\\s*=\\s*([\\d]+)\\s*");
        final Pattern lineq4 = Pattern.compile("\\s*([+-]?[\\d]+)\\s*([a-zA-Z])\\s*=([+-]?[\\d]+)\\s*([a-zA-Z])\\s*([+-][\\d]+)\\s*");
        final Pattern lineq5 = Pattern.compile("\\s*([+-]?[\\d]+)\\s*([a-zA-Z])\\s*=\\s*([+-]?[\\d]+)([+-]?[\\d]+)\\s*([a-zA-Z])\\s*");
        final Pattern lineq6 = Pattern.compile("\\s*([+-]?[\\d]+)\\s*([a-zA-Z])\\s*=([+-]?[\\d]+)\\s*([a-zA-Z])\\s*([+-][\\d]+)\\s*");
        final Pattern lineq7 = Pattern.compile("\\s*([+-]?[\\d]+)\\s*([a-zA-Z])\\s*=\\s*([+-]?[\\d]+)([+-]?[\\d]+)\\s*([a-zA-Z])\\s*");

        Pattern p[]= {lineq1,lineq2,lineq3,lineq4,lineq5,lineq6,lineq7};


        for(int i=0;i<5;i++){
            matcher1=p[i].matcher(str1);
            if (matcher1.matches()) {
                test1=i+1;
                break;
            }
        }
        for(int i=0;i<5;i++){
            matcher2=p[i].matcher(str2);
            if (matcher2.matches()) {
                test2=i+1;
                break;
            }
        }
        System.out.println(test1+" "+test2);
        System.out.println(str1+" "+str2);
        switch(test1){
            case 1:
                if("x".equals(matcher1.group(2))){
                    v1=Integer.parseInt(matcher1.group(1));
                    v2=0;
                } else {
                    v1=0;
                    v2=Integer.parseInt(matcher1.group(1));
                }
                v3=Integer.parseInt(matcher1.group(3));
                break;
            case 2:
                if("x".equals(matcher1.group(2))){
                    v1=Integer.parseInt(matcher1.group(1));
                    v2=0;
                } else {
                    v1=0;
                    v2=Integer.parseInt(matcher1.group(1));
                }
                v3=Integer.parseInt(matcher1.group(4))-Integer.parseInt(matcher1.group(3));
                break;
            case 3:
                if("x".equals(matcher1.group(2))){
                    v1=Integer.parseInt(matcher1.group(1));
                    v2=Integer.parseInt(matcher1.group(3));
                } else {
                    v1=Integer.parseInt(matcher1.group(3));
                    v2=Integer.parseInt(matcher1.group(1));
                }
                v3=Integer.parseInt(matcher1.group(5));
                break;
            case 4:
                if("x".equals(matcher1.group(2))){
                    v1=Integer.parseInt(matcher1.group(1));
                    v2=-1*Integer.parseInt(matcher1.group(3));
                } else {
                    v1=-1*Integer.parseInt(matcher1.group(3));
                    v2=Integer.parseInt(matcher1.group(1));
                }
                v3=Integer.parseInt(matcher1.group(5));
                break;
            case 5:
                if("x".equals(matcher1.group(2))){
                    v1=Integer.parseInt(matcher1.group(1));
                    v2=-1*Integer.parseInt(matcher1.group(5));
                } else {
                    v1=-1*Integer.parseInt(matcher1.group(5));
                    v2=Integer.parseInt(matcher1.group(1));
                }
                v3=Integer.parseInt(matcher1.group(3));
                break;


        }
        switch(test2){
            case 1:
                if("x".equals(matcher2.group(2))){
                    v4=Integer.parseInt(matcher2.group(1));
                    v5=0;
                } else {
                    v4=0;
                    v5=Integer.parseInt(matcher2.group(1));
                }
                v6=Integer.parseInt(matcher2.group(3));
                break;
            case 2:
                if("x".equals(matcher2.group(2))){
                    v4=Integer.parseInt(matcher2.group(1));
                    v5=0;
                } else {
                    v4=0;
                    v5=Integer.parseInt(matcher2.group(1));
                }
                v6=Integer.parseInt(matcher2.group(4))-Integer.parseInt(matcher2.group(3));
                break;
            case 3:
                if("x".equals(matcher2.group(2))){
                    v4=Integer.parseInt(matcher2.group(1));
                    v5=Integer.parseInt(matcher2.group(3));
                } else {
                    v4=Integer.parseInt(matcher2.group(3));
                    v5=Integer.parseInt(matcher2.group(1));
                }
                v6=Integer.parseInt(matcher2.group(5));
                break;
            case 4:
                if("x".equals(matcher2.group(2))){
                    v4=Integer.parseInt(matcher2.group(1));
                    v5=Integer.parseInt(matcher2.group(3));
                } else {
                    v4=Integer.parseInt(matcher2.group(3));
                    v5=Integer.parseInt(matcher2.group(1));
                }
                v6=Integer.parseInt(matcher2.group(5));
                break;
            case 5:
                if("x".equals(matcher2.group(2))){
                    v4=Integer.parseInt(matcher2.group(1));
                    v5=-1*Integer.parseInt(matcher2.group(4));
                } else {
                    v4=-1*Integer.parseInt(matcher2.group(4));
                    v5=Integer.parseInt(matcher2.group(1));
                }
                v6=Integer.parseInt(matcher2.group(3));
                break;


        }
        solvelin(v1,v2,v3,v4,v5,v6);
    }

    public static void solvelin(int a, int b, int c, int d, int e, int f) {
        if((a*e)-(b*d) != 0){
            double x = ((c*e)-(b*f))/((a*e)-(b*d));
            double y = ((a*f)-(c*d))/((a*e)-(b*d));
            String v7="x = "+x + "\ny = "+y;
            textView.setText(v7);
        }
        else
        {
            textView.setText("Equation has no solutions");
        }
    }

    private Bitmap convertColorIntoBlackAndWhiteImage(Bitmap orginalBitmap) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);

        Bitmap blackAndWhiteBitmap = orginalBitmap.copy(
                Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setColorFilter(colorMatrixFilter);

        Canvas canvas = new Canvas(blackAndWhiteBitmap);
        canvas.drawBitmap(blackAndWhiteBitmap, 0, 0, paint);

        return blackAndWhiteBitmap;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                mCropImageView.setImageUriAsync(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCropImageView.setImageUriAsync(mCropImageUri);
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}