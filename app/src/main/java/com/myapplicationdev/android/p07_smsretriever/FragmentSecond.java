package com.myapplicationdev.android.p07_smsretriever;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSecond extends Fragment {

    TextView tvSms, tvRetrieve;
    Button btnRetrieve;
    EditText etNumber;

    public FragmentSecond() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_second, container, false);
        tvSms = view.findViewById(R.id.tvSMS);
        tvRetrieve = view.findViewById(R.id.tvRetrieve);
        btnRetrieve = view.findViewById(R.id.btnRetrieve);
        etNumber = view.findViewById(R.id.etSMS);

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = etNumber.getText().toString();
                String text;
                String[] array;

                text = word;
                array = text.split("\\s+");

                if (word.length() == 0) {
                    Toast.makeText(getContext(), "Please enter a word", Toast.LENGTH_LONG).show();
                }
                else {
                    int permissionCheck = PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_SMS);

                    if (permissionCheck != PermissionChecker.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, 0);
                        return;
                    }
                    Uri uri = Uri.parse("content://sms");
                    String[] reqCols = new String[]{"date", "address", "body", "type"};

                    ContentResolver cr = getActivity().getContentResolver();
                    String filter=" body LIKE ? ";
                    String[] filterArgs = new String[array.length];
                    for (int i = 0; i < array.length; i ++){
                        filterArgs[i] = "%" + array[i] + "%";
                        filter +=  "OR body LIKE ?";
                    }

                    Cursor cursor = cr.query(uri, reqCols, filter,filterArgs,null);
                    String smsBody = "";
                    if (cursor.moveToFirst()) {
                        do {
                            long dateInMillis = cursor.getLong(0);
                            String date = (String) DateFormat.format("dd MM yyyy h:mm:ss aa", dateInMillis);
                            String address = cursor.getString(1);
                            String body = cursor.getString(2);
                            String type = cursor.getString(3);
                            if (type.equalsIgnoreCase("1")) {
                                type = "Inbox:";
                            } else {
                                type = "Sent:";
                            }
                            smsBody += type + " " + address + "\n at " + date
                                    + "\n\"" + body + "\"\n\n";
                        } while (cursor.moveToNext());
                    }
                    tvRetrieve.setText(smsBody);
                }
            }
        });

        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnRetrieve.performClick();

                } else {
                    Toast.makeText(getContext(), "Permission not granted",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
