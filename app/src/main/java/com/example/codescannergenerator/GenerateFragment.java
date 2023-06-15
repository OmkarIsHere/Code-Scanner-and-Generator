package com.example.codescannergenerator;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
                Bitmap bitmap = barcodeEncoder.encodeBitmap(inputText.getText().toString().trim(), BarcodeFormat.QR_CODE, 500, 500);
                btnDownload.setVisibility(View.VISIBLE);
                encodeImg.setImageBitmap(bitmap);
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        btnDownload.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Downloading feature is under construction", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}