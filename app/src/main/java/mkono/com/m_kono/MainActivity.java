package mkono.com.m_kono;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {


    private TextView txtSpeechInput;
    TextView text;
    public final String ACTION_USB_PERMISSION = "mkono.com.m_kono.USB_PERMISSION";
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnections();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);
        txtSpeechInput = (TextView) findViewById(R.id.textView);
        ImageButton help = (ImageButton) findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Help.class));
            }
        });
        ImageButton b = (ImageButton)findViewById(R.id.voice);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String command= result.get(0).toString();
                    Toast.makeText(MainActivity.this,"You have chosen the action "+ command, Toast.LENGTH_SHORT).show();
                    switch(command)
                    {
                        case "open":
                            try {
                                command.toLowerCase();
                                serialPort.write(command.getBytes());
                                Toast.makeText(MainActivity.this, "Open hand", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, "Please check your connection to the device and try again.", Toast.LENGTH_SHORT).show();
                                checkConnections();
                            }

                        case"close":
                            try {
                                command.toLowerCase();
                                serialPort.write(command.getBytes());
                                Toast.makeText(MainActivity.this, "Close hand", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, "Please check your connection to the device and try again.", Toast.LENGTH_SHORT).show();
                                checkConnections();
                            }

                        case"greet":
                            try {
                                command.toLowerCase();
                                serialPort.write(command.getBytes());
                                Toast.makeText(MainActivity.this, "Greeting", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, "Please check your connection to the device and try again.", Toast.LENGTH_SHORT).show();
                                checkConnections();
                            }
                        case"point":
                            try {
                                command.toLowerCase();
                                serialPort.write(command.getBytes());
                                Toast.makeText(MainActivity.this, "Pointing", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, "Please check your connection to the device and try again.", Toast.LENGTH_SHORT).show();
                                checkConnections();
                            }
                        case"good":
                            try {
                                command.toLowerCase();
                                serialPort.write(command.getBytes());
                                Toast.makeText(MainActivity.this, "Good", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, "Please check your connection to the device and try again.", Toast.LENGTH_SHORT).show();
                                checkConnections();
                            }
                    }
                }
                break;
            }

        }
    }


    public void checkConnections() {
        while (usbManager == null) {
            startActivity(new Intent(MainActivity.this, Pop.class));
            Toast.makeText(MainActivity.this, "Waiting for connection.", Toast.LENGTH_SHORT).show();
            return;
        }
        while(usbManager!= null)
        {
            HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
            if (!usbDevices.isEmpty()) {
                Toast.makeText(MainActivity.this, "Connected.", Toast.LENGTH_SHORT).show();
                boolean keep = true;
                for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                    device = entry.getValue();
                    int deviceVID = device.getVendorId();
                    Toast.makeText(MainActivity.this, deviceVID, Toast.LENGTH_SHORT).show();
                    if (deviceVID == 0x2341)//Arduino Vendor ID
                    {
                        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                        usbManager.requestPermission(device, pi);
                        keep = false;
                        menuNotification();
                    } else {
                        connection = null;
                        device = null;
                        Toast.makeText(MainActivity.this, "Please check your connection and try again.", Toast.LENGTH_SHORT).show();
                    }

                    if (!keep)
                        break;
                }
            }
        }
    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);

                        } else {
                            checkConnections();
                            Toast.makeText(MainActivity.this, "Please check your connection and try again.", Toast.LENGTH_SHORT).show();
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        checkConnections();
                        Toast.makeText(MainActivity.this, "Please check your connection and try again.", Toast.LENGTH_SHORT).show();
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    checkConnections();
                    Toast.makeText(MainActivity.this, "Please enable USB permissions and try again.", Toast.LENGTH_SHORT).show();
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                    checkConnections();
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                serialPort.close();
                Toast.makeText(MainActivity.this, "Connection Closed", Toast.LENGTH_SHORT).show();

            }
        }


    };

    public void menuNotification()
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("M-Kono")
                .setContentText("You have connected your Phone to the prosthetic.");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }
}
