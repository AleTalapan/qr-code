package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.ui.login.LoginActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Obține utilizatorul conectat din SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String loggedInUser = sharedPreferences.getString("username", "Niciun utilizator conectat");

        // Log utilizator conectat
        Log.d("UserSession", "Utilizator conectat: " + loggedInUser);

        Button buttonScan = findViewById(R.id.buttonBottom);
        Button buttonGenerate = findViewById(R.id.buttonTop);
        Button buttonLogout = findViewById(R.id.button); // ID-ul butonului de logout
        EditText inputText = findViewById(R.id.inputText);
        ImageView qrCodeImageView = findViewById(R.id.imageView2);

        buttonScan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        });

        buttonGenerate.setOnClickListener(v -> {
            String textToEncode = inputText.getText().toString();

            if (textToEncode.isEmpty()) {
                Toast.makeText(MainActivity.this, "Introduceți un text!", Toast.LENGTH_SHORT).show();
            } else {
                // Generare cod QR
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    Bitmap qrCodeBitmap = generateQRCode(textToEncode);
                    qrCodeImageView.setImageBitmap(qrCodeBitmap);
                    Toast.makeText(MainActivity.this, "Cod QR generat cu succes!", Toast.LENGTH_SHORT).show();
                } catch (WriterException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Eroare la generarea codului QR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Logica butonului de logout
        buttonLogout.setOnClickListener(v -> {
            // Șterge datele utilizatorului din SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Șterge toate datele stocate
            editor.apply();

            // Log mesaj de delogare
            Log.d("UserSession", "Utilizator delogat. Sesiunea a fost ștearsă.");

            // Navighează către LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

            // Închide MainActivity pentru a preveni revenirea înapoi
            finish();

            // Mesaj pentru utilizator
            Toast.makeText(MainActivity.this, "Te-ai deconectat cu succes!", Toast.LENGTH_SHORT).show();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private Bitmap generateQRCode(String text) throws WriterException {
        int width = 500;
        int height = 500;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bitmap;
    }
}