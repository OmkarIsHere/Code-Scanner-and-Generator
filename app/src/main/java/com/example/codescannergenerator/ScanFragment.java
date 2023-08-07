package com.example.codescannergenerator;

import static android.content.Context.CLIPBOARD_SERVICE;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


public class ScanFragment extends Fragment {
    private static final String TAG = "ScanFragment";
    ImageButton btnCamera;
    ListView listView;
    TextView txtScanned;
    String qrEncode;
    DBhelper dBhelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        btnCamera=view.findViewById(R.id.btnCamera);
        txtScanned=view.findViewById(R.id.txtScanned);
        listView=view.findViewById(R.id.listView);

        btnCamera.setOnClickListener(v -> {
            runCodeScanner();
        });

        dBhelper = new DBhelper(getActivity());
        ArrayList<EncodeData> arrayList = dBhelper.getData();
        ListAdapter listAdapter = new ListAdapter(getActivity(), arrayList);
        listView.setAdapter(listAdapter);

        txtScanned.setOnLongClickListener(v -> {
            String s = txtScanned.getText().toString().trim();
            if(!s.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", s);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Text Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
                return false;
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
            if(qrEncode.contains("https://") || qrEncode.contains("http://")){
                txtScanned.setText(qrEncode);
                txtScanned.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue));
                txtScanned.setOnClickListener(v -> {
                    Intent i = new Intent(getActivity(), WebViewActivity.class);
                    i.putExtra("link", qrEncode);
                    startActivity(i);
                });
            }else{
                txtScanned.setText(qrEncode);
                txtScanned.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            }

            LocalDate today = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                today = LocalDate.now();
            }

            try {
                dBhelper.addData(qrEncode, String.valueOf(today));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
            ArrayList<EncodeData> arrayList = dBhelper.getData();
            ListAdapter listAdapter = new ListAdapter(getActivity(), arrayList);
            listView.setAdapter(listAdapter);

    });
}

