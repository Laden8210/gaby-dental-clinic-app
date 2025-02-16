package com.example.gabaydentalclinic.view;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.gabaydentalclinic.R;
import com.example.gabaydentalclinic.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.concurrent.Executor;

public class HeroActivity extends AppCompatActivity {

    private static final int LOCK_SCREEN_REQUEST_CODE = 1001;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        if (SessionManager.getInstance(this).isLockEnabled()) {
            checkBiometricSupport();
        } else {
            loadUI();
        }
    }

    private void checkBiometricSupport() {
        BiometricManager biometricManager = BiometricManager.from(this);

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                == BiometricManager.BIOMETRIC_SUCCESS) {

            Executor executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    loadUI();
                }

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(HeroActivity.this, "Authentication Error: " + errString, Toast.LENGTH_SHORT).show();
                    fallbackToLockScreen();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(HeroActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            });


            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Unlock App")
                    .setSubtitle("Use fingerprint or face recognition to continue")
                    .setNegativeButtonText("Use PIN/Password")
                    .build();

            biometricPrompt.authenticate(promptInfo);
        } else {

            fallbackToLockScreen();
        }
    }

    private void fallbackToLockScreen() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        if (keyguardManager.isDeviceSecure()) {
            Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("Unlock App", "Enter your PIN, password, or pattern to continue");
            if (intent != null) {
                startActivityForResult(intent, LOCK_SCREEN_REQUEST_CODE);
            }
        } else {
            Toast.makeText(this, "No lock screen security set up. Please enable it in settings.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS)); 
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCK_SCREEN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                loadUI();
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void loadUI() {
        setContentView(R.layout.activity_hero);
        BottomNavigationView bnvHero = findViewById(R.id.bnvHero);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nfvHero);

        if (fragment instanceof NavHostFragment) {
            NavHostFragment nvfHero = (NavHostFragment) fragment;
            NavigationUI.setupWithNavController(bnvHero, nvfHero.getNavController());
        }
    }
}
