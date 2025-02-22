package com.example.gabaydentalclinic.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.gabaydentalclinic.R;
import com.example.gabaydentalclinic.util.Messenger;
import com.example.gabaydentalclinic.util.SessionManager;
import com.google.android.material.button.MaterialButton;


public class SettingFragment extends Fragment {


    private MaterialButton btnLogout, btnChangePassword, btnEditProfile;

    private Switch switchLockScreen;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        switchLockScreen = view.findViewById(R.id.switchLockScreen);


        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnChangePassword.setOnClickListener(this::changePassword);
        btnLogout.setOnClickListener(this::logoutAction);
        btnEditProfile.setOnClickListener(this::changeProfileAction);

        switchLockScreen.setChecked(SessionManager.getInstance(getContext()).isLockEnabled());

        switchLockScreen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Messenger.showAlertDialog(getContext(), "Lock Screen", "Do you want to enable the lockscreen?",
                        "Yes", "No",
                        (dialog, which) -> {
                            SessionManager.getInstance(getContext()).setLockEnabled(true);
                        },
                        (dialog, which) -> {
                            switchLockScreen.setChecked(false);
                        }).show();
            } else {
                Messenger.showAlertDialog(getContext(), "Lock Screen", "Do you want to disable the lockscreen?",
                        "Yes", "No",
                        (dialog, which) -> {
                            SessionManager.getInstance(getContext()).setLockEnabled(false);
                        },
                        (dialog, which) -> {
                            switchLockScreen.setChecked(true);
                        }).show();
            }
        });
        return view;
    }

    private void changeProfileAction(View view) {


    }

    private void logoutAction(View view) {
        
    }

    private void changePassword(View view) {
    }
}