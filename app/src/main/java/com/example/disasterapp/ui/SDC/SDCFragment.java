package com.example.disasterapp.ui.SDC;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.disasterapp.R;

import static android.Manifest.permission.CALL_PHONE;

public class SDCFragment extends Fragment {

    private SDCViewModel SDCViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SDCViewModel =
                ViewModelProviders.of(this).get(SDCViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sdc, container, false);


        root.findViewById(R.id.btnTest).setOnClickListener(mListener);
        root.findViewById(R.id.btnPolice).setOnClickListener(mListener);
        root.findViewById(R.id.btnFire).setOnClickListener(mListener);
        root.findViewById(R.id.btnAmbulance).setOnClickListener(mListener);
        root.findViewById(R.id.btnRoad).setOnClickListener(mListener);

        SDCViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    private final View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnTest:
                    Intent callIntent1 = new Intent(Intent.ACTION_CALL);
                    callIntent1.setData(Uri.parse("tel:" + "123456789" ));

                    if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent1);
                    } else {
                        requestPermissions(new String[]{CALL_PHONE}, 1);
                    }
                    break;
                case R.id.btnPolice:
                    Intent callIntent2 = new Intent(Intent.ACTION_CALL);
                    callIntent2.setData(Uri.parse("tel:" + "100" ));

                    if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent2);
                    } else {
                        requestPermissions(new String[]{CALL_PHONE}, 1);
                    }
                    break;
                case R.id.btnFire:
                    Intent callIntent3 = new Intent(Intent.ACTION_CALL);
                    callIntent3.setData(Uri.parse("tel:" + "101" ));

                    if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent3);
                    } else {
                        requestPermissions(new String[]{CALL_PHONE}, 1);
                    }
                    break;
                case R.id.btnAmbulance:
                    Intent callIntent4 = new Intent(Intent.ACTION_CALL);
                    callIntent4.setData(Uri.parse("tel:" + "102" ));

                    if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent4);
                    } else {
                        requestPermissions(new String[]{CALL_PHONE}, 1);
                    }
                    break;
                case R.id.btnRoad:
                    Intent callIntent5 = new Intent(Intent.ACTION_CALL);
                    callIntent5.setData(Uri.parse("tel:" + "1073" ));

                    if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent5);
                    } else {
                        requestPermissions(new String[]{CALL_PHONE}, 1);
                    }
                    break;
            }
        }
    };
}