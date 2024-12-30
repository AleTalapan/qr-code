package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;

public class ScanActivity extends AppCompatActivity {

    private BarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan2);

        // Solicită permisiunea pentru cameră
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        // Inițializează BarcodeView
        barcodeView = findViewById(R.id.barcodeView);

        // Setează callback-ul pentru scanare
        barcodeView.decodeContinuous(new BarcodeCallback() {
            private String lastScannedText = "";

            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null) {
                    String scannedText = result.getText();

                    // Evită trimiterea repetată a aceluiași rezultat
                    if (scannedText != null && !scannedText.equals(lastScannedText)) {
                        lastScannedText = scannedText;

                        new Thread(() -> {
                            try {
                                EmailSender.sendEmail(
                                        "aletalapan@gmail.com", // Email-ul destinatarului
                                        "Cod QR Scanat",
                                        "Am scanat acest link: " + scannedText
                                );
                                runOnUiThread(() -> Toast.makeText(ScanActivity.this, "Email trimis cu succes!", Toast.LENGTH_SHORT).show());
                            } catch (Exception e) {
                                runOnUiThread(() -> Toast.makeText(ScanActivity.this, "Eroare la trimiterea emailului.", Toast.LENGTH_SHORT).show());
                                e.printStackTrace();
                            }
                        }).start();
                    }
                }
            }




            @Override
            public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {
                // Opțional: gestionarea punctelor detectate
            }
        });
    }

    // Reia activitatea camerei când utilizatorul revine în ScanActivity
    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeView != null) {
            barcodeView.resume();
        }
    }

    // Oprește camera pentru a economisi resurse când activitatea este în fundal
    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeView != null) {
            barcodeView.pause();
        }
    }

    // Gestionarea permisiunii pentru cameră
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (barcodeView != null) {
                    barcodeView.resume();
                }
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
