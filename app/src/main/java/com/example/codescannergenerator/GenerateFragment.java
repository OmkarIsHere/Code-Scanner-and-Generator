package com.example.codescannergenerator;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;


public class GenerateFragment extends Fragment {

    private static final String TAG = "GenerateFragment";
    private static final int STORAGE_PERMISSION_CODE = 100;

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
            progressBar.setVisibility(View.VISIBLE);
            encodeImg.setVisibility(View.GONE);
            btnDownload.setVisibility(View.GONE);
            Log.d(TAG, "progress bar: set");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "progress bar: code run");
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.encodeBitmap(inputText.getText().toString().trim(), BarcodeFormat.QR_CODE, 800, 800);
                        progressBar.setVisibility(View.GONE);
                        btnDownload.setVisibility(View.VISIBLE);
                        Log.d(TAG, "progress bar: gone");
                        encodeImg.setVisibility(View.VISIBLE);
                        encodeImg.setImageBitmap(bitmap);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);


        });

        btnDownload.setOnClickListener(v -> {
            BitmapDrawable draw = (BitmapDrawable)encodeImg.getDrawable();
            Bitmap bitmap = draw.getBitmap();

            FileOutputStream outStream = null;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getPath() + "/Download");
            dir.mkdirs();
            String fileName = String.format("%d.jpg", System.currentTimeMillis());

            if (checkPermission()) {
                Log.d(TAG, "onClick: Permissions already granted...");
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
            } else {
                Log.d(TAG, "onClick: Permissions was not granted, request...");
                requestPermission();
            }


        });

        return view;
    }

    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            try {
                Log.d(TAG, "requestPermission: try");

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            }
            catch (Exception e){
                Log.e(TAG, "requestPermission: catch", e);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }
        else {
            //Android is below 11(R)
            ActivityCompat.requestPermissions(
                   requireActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE
            );
        }
    }

    private final ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult: ");
                    //here we will handle the result of our intent
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        //Android is 11(R) or above
                        if (Environment.isExternalStorageManager()){
                            //Manage External Storage Permission is granted
                            Log.d(TAG, "onActivityResult: Manage External Storage Permission is granted");
                        }
                        else{
                            //Manage External Storage Permission is denied
                            Log.d(TAG, "onActivityResult: Manage External Storage Permission is denied");
                            Toast.makeText(getActivity(), "Manage External Storage Permission is denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        //Android is below 11(R)
                    }
                }
            }
    );

    public boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            return Environment.isExternalStorageManager();
        }
        else{
            //Android is below 11(R)
            int write = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
        }
    }

    /*Handle permission request results*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0){
                //check each permission if granted or not
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (write && read){
                    //External Storage permissions granted
                    Log.d(TAG, "onRequestPermissionsResult: External Storage permissions granted");
                }
                else{
                    //External Storage permission denied
                    Log.d(TAG, "onRequestPermissionsResult: External Storage permission denied");
                    Toast.makeText(getActivity(), "External Storage permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}