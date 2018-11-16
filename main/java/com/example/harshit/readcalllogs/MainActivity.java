package com.example.harshit.readcalllogs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                        {
                                Manifest.permission.READ_CALL_LOG
                        }, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                        {
                                Manifest.permission.READ_CALL_LOG
                        }, 1);
            }
        } else {
            //do stuff
            TextView textView=findViewById(R.id.textCall);
            textView.setText(getCallDetail());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "No permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private String getCallDetail() {
        StringBuffer sb = new StringBuffer();
        Cursor manageCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = manageCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = manageCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = manageCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = manageCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details:\n\n");
        while (manageCursor.moveToNext()) {
            String phoneNumber = manageCursor.getString(number);
            String callType = manageCursor.getString(type);
            String callDate = manageCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yy HH:mm");
            String dateString = formater.format(callDayTime);
            String callDuration = manageCursor.getString(duration);
            String dir = null;
            int dirCode = Integer.parseInt(callType);
            switch (dirCode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "Missed";
                    break;
            }
            sb.append("\n Phone Number:" + phoneNumber + " \n Call Type :" +dir +"\n Call Date:"+dateString +" \n Call Duration :"+callDuration);
            sb.append("\n--------------------------------------------------");


        }
        manageCursor.close();
        return sb.toString();
    }
}
