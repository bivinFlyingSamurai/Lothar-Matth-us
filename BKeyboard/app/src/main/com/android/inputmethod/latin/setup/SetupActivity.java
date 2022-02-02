

package com.android.inputmethod.latin.setup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.Matthaus.Lothar.keyboard.MainActivity;
import com.Matthaus.Lothar.keyboard.R;

public final class SetupActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            final Intent intent = new Intent();
            intent.setClass(SetupActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            if (!isFinishing()) {
                finish();
            }
        }, 3000);
    }
}
