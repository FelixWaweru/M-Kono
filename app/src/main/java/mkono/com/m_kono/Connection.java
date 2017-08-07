package mkono.com.m_kono;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by USER PC on 06-Jul-17.
 */
public class Connection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);
        ImageView Connection = (ImageView)findViewById(R.id.loading);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(4000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);Connection.startAnimation(rotateAnimation);

    }

    public void checkConnection(Context context) throws InterruptedException {
        Intent intent = context.registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        boolean status = intent.getExtras().getBoolean("connected");
        String StringStatus = Boolean.toString(status);
        if (StringStatus.equals("connected")) {
            TextView text = (TextView)findViewById(R.id.text);
            text.setText("Connected");
            Thread.sleep(2000);
            Toast.makeText(Connection.this, "connected", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }
        else
        {
            int a=1;
            int b=2;
            while(a!=b){
                Thread.sleep(10000);
                Toast.makeText(Connection.this, "Please check your connection and try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
