package com.example.codescannergenerator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class GenerateFragment extends Fragment {

    private static final String TAG = "GenerateFragment";
    ImageView encodeImg;
    EditText inputText;
    ProgressBar progressBar;
    Button btnGenerate, btnDownload;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate, container, false);

        encodeImg = view.findViewById(R.id.imgCode);
        inputText = view.findViewById(R.id.txtInputText);
        btnDownload = view.findViewById(R.id.btnDownload);
        btnGenerate = view.findViewById(R.id.btnGenerate);
        progressBar = view.findViewById(R.id.progressBar);

        btnGenerate.setOnClickListener(v -> {
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(inputText.getText().toString().trim(), BarcodeFormat.QR_CODE, 400, 400);
                btnDownload.setVisibility(View.VISIBLE);
                encodeImg.setImageBitmap(bitmap);
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        btnDownload.setOnClickListener(v -> {
            BitmapDrawable draw = (BitmapDrawable)encodeImg.getDrawable();
            Bitmap bitmap = draw.getBitmap();

            FileOutputStream outStream = null;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/Download");
            dir.mkdirs();
            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            try {
                File outFile = new File(dir, fileName);
                outStream = new FileOutputStream(outFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(outFile));
                getActivity().sendBroadcast(intent);
                Log.d(TAG, "Done");
            } catch (IOException exception){
                Log.d(TAG, "IOException: "+ exception);
                exception.printStackTrace();
            }
            Toast.makeText(getActivity(), "QR Code download successfully !!", Toast.LENGTH_SHORT).show();
            btnDownload.setVisibility(View.GONE);
        });

        return view;
    }
}