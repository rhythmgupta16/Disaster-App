package com.example.disasterapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class FavoritesFragment extends Fragment {
    private static final int RESULT_PICK_CONTACT = 0;
    private TextView tvPhone, tvName, tvPicked;
    private Button btnPick, btnSend;
    String message;
    private ContactInfo contact;
    private ArrayList<ContactInfo> contactList = new ArrayList<ContactInfo>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_favorites, null);

        btnPick = view.findViewById(R.id.btnPick);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvName = view.findViewById(R.id.tvName);
        tvPicked = view.findViewById(R.id.tvPicked);
        btnSend = view.findViewById(R.id.btnSend);
        btnSend.setVisibility(View.INVISIBLE);
        tvPhone.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.INVISIBLE);
        tvPicked.setVisibility(View.INVISIBLE);


        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Ask for permision
                    ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.SEND_SMS}, 1);
                }
                else {

                    SmsManager sms = SmsManager.getDefault();

                    /*ArrayList to Array Conversion */
                    String numbers[] = new String[contactList.size()];
                    for(int j =0;j<contactList.size();j++){
                        numbers[j] = String.valueOf(contactList.get(j));
                    }

                    for(String number : numbers) {
                        message = "URGENT! Please help me.";
                        sms.sendTextMessage(number, null, message, null, null);
                        Log.i(TAG, "onClick: SENT to "+ number + " with message: " + message);
                    }
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Cursor cursor = null;
                    try {
                        String phoneNo = null ;
                        String name = null;
                        Uri uri = data.getData();
                        cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                        int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phoneNo = cursor.getString(phoneIndex);

                        //Set Text View for selected element
                        tvPicked.setVisibility(View.VISIBLE);
                        tvName.setText(contactName);
                        tvName.setVisibility(View.VISIBLE);
                        tvPhone.setText(phoneNo);
                        tvPhone.setVisibility(View.VISIBLE);
                        btnSend.setVisibility(View.VISIBLE);

                        //Making List
                        contact = new ContactInfo();
                        contact.setName(contactName);
                        contact.setPhone(phoneNo);
                        contactList.add(contact);


                        Toast.makeText(getActivity(),""+contactList.toString(),Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }


}