package com.example.disasterapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements Serializable {
    Button btnEmergency;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);

        btnEmergency = view.findViewById(R.id.btnEmergency);


        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String contact_number="123456789";
                    Intent callIntent = new Intent(Intent.ACTION_CALL_BUTTON);
                    callIntent.setData(Uri.parse("tel:" + contact_number));
                    startActivity(callIntent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"No Calling App found",Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }
}