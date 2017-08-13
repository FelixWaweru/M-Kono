package mkono.com.m_kono;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by USER PC on 06-Jul-17.
 */
public class Connection extends AppCompatActivity {
    TextView text;
    public final String ACTION_USB_PERMISSION = "mkono.com.m_kono.USB_PERMISSION";
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);
        ImageView Connection = (ImageView)findViewById(R.id.loading);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(4000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);Connection.startAnimation(rotateAnimation);

    }

//    public void checkConnection(Context context) throws InterruptedException {
//        Intent intent = context.registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
//        boolean status = intent.getExtras().getBoolean("connected");
//        String StringStatus = Boolean.toString(status);
//        if (StringStatus.equals("connected")) {
//
//            text.setText(StringStatus);
//            Thread.sleep(2000);
//            Toast.makeText(Connection.this, "connected", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, MainActivity.class));
//        }
//        else
//        {
//            int a=1;
//            int b=2;
//            while(a!=b){
//                Thread.sleep(10000);
//                Toast.makeText(Connection.this, "Please check your connection and try again.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
        public void checkConnections() throws InterruptedException {
        HashMap usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Object entry : usbDevices.entrySet()) {
                device = (UsbDevice) entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 0x2341)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                            new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                    Thread.sleep(10000);
                    Toast.makeText(Connection.this, "Please check your connection and try again.", Toast.LENGTH_SHORT).show();
                }

                if (!keep)
                    break;
            }
        }
    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
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
                            text.setText("Serial Connection Opened!\n");

                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                try {
                    checkConnections();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                serialPort.close();
                Toast.makeText(Connection.this, "Connection Closed", Toast.LENGTH_SHORT).show();

            }
        }


    };


}
