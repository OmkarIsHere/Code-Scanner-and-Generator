package com.example.codescannergenerator;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class ScanFragment extends Fragment {
    ImageButton btnCamera;
    String qrEncode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        btnCamera=view.findViewById(R.id.btnCamera);

        btnCamera.setOnClickListener(v -> {
            runCodeScanner();
        });
        return view;
    }
    private void runCodeScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(ScannerCapt.class);
        barLaucher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents()!= null){
            qrEncode=result.getContents();
            Toast.makeText(getActivity(), qrEncode, Toast.LENGTH_SHORT).show();
        }
    });
    }

