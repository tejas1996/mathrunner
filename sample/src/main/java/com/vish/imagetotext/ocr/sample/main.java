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

/**
 * Created by VisH on 19-06-2016.
 */
public class main  extends Activity {

    private CropImageView mCropImageView;
    Bitmap converted;
    EditText textView;
    float ansss;
    int a,b,c;

    double x,y;
    anss an=new anss();

    private TessOCR mTessOCR;
    public static Matcher matcher;
    private Uri mCropImageUri;
    String fin;
    public static final String lang = "eng";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/DemoOCR/";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        textView = (EditText)findViewById(R.id.editText);

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
                            int type=testeqn(t);
                            t=" "+type;
                            switch(type) {
                                case 1 :
                                    an=new anss();
                                    if(matcher.group(1).length()==0){
                                        a=1;
                                    }
                                    else a=Integer.parseInt(matcher.group(1));
                                    if(matcher.group(3).length()==0){
                                        b=1;
                                    }
                                    else b=Integer.parseInt(matcher.group(4));
                                    c=Integer.parseInt(matcher.group(7))-Integer.parseInt(matcher.group(8));
                                    an=an.solquad(a,b,c);
                                    x=an.aa;
                                    y=an.bb;
                                    fin="x="+x+" "+"y="+y;
                                    textView.setText(fin);
                                    break; // optional

                                case 2 :
                                    if(matcher.group(1).length()==0){
                                        a=1;
                                    }
                                    else
                                        a=Integer.parseInt(matcher.group(1));
                                    if(matcher.group(4).length()==0){
                                        b=1;
                                    }
                                    else b=Integer.parseInt(matcher.group(4));
                                    c=Integer.parseInt(matcher.group(7));
                                    c=-c;
                                    an=new anss();

                                    an=an.solquad(a,b,c);
                                    x=an.aa;
                                    y=an.bb;
                                    fin="x="+x+" "+"y="+y;
                                    textView.setText(fin);


                                    // Statements
                                    break; // optional

                                case 3 :

                                    if(matcher.group(1).length()==0){
                                        a=1;
                                    }
                                    else  a=Integer.parseInt(matcher.group(1));

                                    b=0;
                                    c=Integer.parseInt(matcher.group(4));
                                    c=-c;
                                    an=new anss();

                                    an=an.solquad(a,b,c);
                                    x=an.aa;
                                    y=an.bb;
                                    fin="x="+x+" "+"y="+y;
                                    textView.setText(fin);



                                    // Statements
                                    break; // optional

                                case 4 :


                                    if(matcher.group(1).length()==0){
                                        a=1;
                                    }
                                    else  a=Integer.parseInt(matcher.group(1));

                                    b=Integer.parseInt(matcher.group(3));
                                    c=Integer.parseInt(matcher.group(4));
                                    c=b-c;
                                    b=0;
                                    an=new anss();

                                    an=an.solquad(a,b,c);
                                    x=an.aa;
                                    y=an.bb;
                                    fin="x="+x+" "+"y="+y;
                                    textView.setText(fin);





                                    // Statements
                                    break; // optional
                                case 5 :

                                if(matcher.group(1).length()==0){
                                    a=1;
                                }
                                else
                                    a=Integer.parseInt(matcher.group(1));
                                if(matcher.group(5).length()==0){
                                    b=1;
                                }
                                else b=Integer.parseInt(matcher.group(5));
                                b=-b;
                                c=Integer.parseInt(matcher.group(8));
                                int d=Integer.parseInt(matcher.group(4));
                                c=d-c;


                                an=new anss();

                                an=an.solquad(a,b,c);
                                x=an.aa;
                                y=an.bb;
                                    fin="x="+x+" "+"y="+y;
                                    textView.setText(fin);




                                // Statements
                                break; // optional
                                case 6 :

                                    if(matcher.group(1).length()==0){
                                        a=1;
                                    }
                                    else
                                        a=Integer.parseInt(matcher.group(1));
                                    if(matcher.group(4).length()==0){
                                        b=1;
                                    }
                                    else b=Integer.parseInt(matcher.group(4));
                                    b=-b;
                                    c=Integer.parseInt(matcher.group(7));
                                    c=-c;


                                    an=new anss();

                                    an=an.solquad(a,b,c);
                                    x=an.aa;
                                    y=an.bb;
                                    fin="x="+x+" "+"y="+y;
                                    textView.setText(fin);







                                    // Statements
                                    break; // optional




                                default : // Optional
                                    // Statements
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
        final Pattern quadeq7 = Pattern.compile("([+-]?[\\d]+)?\\s*([a-zA-Z])\\s*=\\s*([+-]?[\\d]+)?\\s*([a-zA-Z])2\\s*([+-]?[\\d]+)?\\s*");
        final Pattern quadeq8 = Pattern.compile("([+-]?[\\d]+)?\\s*=\\s*([+-]?[\\d]+)?\\s*([a-zA-Z])2\\s*([+-]?[\\d]+)?\\s*([a-zA-Z])\\s*");
        final Pattern quadeq9 = Pattern.compile("([+-]?[\\d]+)?\\s*([a-zA-Z])\\s*([+-]?[\\d]+)?\\s*=\\s*([+-]?[\\d]+)?\\s*([a-zA-Z])2\\s*");
        final Pattern quadeq10 = Pattern.compile("([+-]?[\\d]+)?\\s*([a-zA-Z])\\s*([+-]?[\\d]+)?\\s*=\\s*([+-]?[\\d]+)?\\s*([a-zA-Z])2\\s*([+-]?[\\d]+)?\\s*");
        final Pattern quadeq11 = Pattern.compile("([+-]?[\\d]+)?\\s*=\\s*([+-]?[\\d]+)?\\s*([a-zA-Z])2\\s*([+-]?[\\d]+)?\\s*([a-zA-Z])\\s*([+-]?[\\d]+)?\\s*");

        Pattern p[]= {quadeq1,quadeq2,quadeq3,quadeq4,quadeq5,quadeq6,quadeq7,quadeq8,quadeq9,quadeq10,quadeq11};


        for(int i=0;i<11;i++){
            matcher=p[i].matcher(str);
            if (matcher.matches()) {
                return i+1;
            }
        }




        return 0;
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