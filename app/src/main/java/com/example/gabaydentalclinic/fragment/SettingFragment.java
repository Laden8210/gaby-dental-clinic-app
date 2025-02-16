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


public class SettingFragment extends Fragment {



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
                SessionManager.getInstance(getContext()).setLockEnabled(false);
            }
        });
        return view;
    }
}