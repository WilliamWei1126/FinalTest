package com.william.finaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textview;

    LoaderCallbackInterface loaderCallbackInterface = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {

        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final TextView text = (TextView) findViewById(R.id.textView);
        if (!OpenCVLoader.initDebug()) {
            boolean success =
                    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0
                            , this, loaderCallbackInterface
                    );


        } else {
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);


        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView imageview;




        textview = findViewById(R.id.textView);
        Button draw = findViewById(R.id.button);

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView imgView = findViewById(R.id.qr);
                ImageView blank = findViewById(R.id.blank);
                Bitmap orig = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                Mat img = new Mat(orig.getHeight(), orig.getWidth(), CvType.CV_8UC4);
                Utils.bitmapToMat(orig, img);
                Bitmap origblank = ((BitmapDrawable) blank.getDrawable()).getBitmap();
                Mat imgblank = new Mat(origblank.getHeight(), origblank.getWidth(), CvType.CV_8UC4);
                Utils.bitmapToMat(origblank, imgblank);
                QRCodeDetector qrCodeDetector = new QRCodeDetector();
                String result =  qrCodeDetector.detectAndDecode(img);
                textview.setText(result);

                Log.d("MainActivity", "onCLick" + result);

                String [] points = result.split(" ");
                Scalar LineColor = new Scalar(255, 0, 0, 255);
                int linewidth = 3;
                int count = 0;
                List <Point> Pointarray = new ArrayList<>();

                for (String currpoint : points){
                    String [] pxy = currpoint.split(",");


                    String px = pxy[0];
                    String py = pxy[1];


                    int point1x = Integer.parseInt(px) ;
                    int point1y = Integer.parseInt(py) ;


                    Point pointa = new Point(point1x, point1y);

                    Pointarray.add(pointa);



                    count = count + 1;



                }

                for (int i = 0 ; i < Pointarray.size() - 1 ; i++){


                    Imgproc.line(imgblank, Pointarray.get(i), Pointarray.get(i+1), LineColor, linewidth);
                    Bitmap resultBitmap = Bitmap.createBitmap(imgblank.cols(),imgblank.rows(),Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(imgblank, resultBitmap);
                    blank.setImageBitmap(resultBitmap);


                }




            }
        });
    }
}

