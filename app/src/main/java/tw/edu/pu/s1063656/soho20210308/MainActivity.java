package tw.edu.pu.s1063656.soho20210308;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView date;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        date = findViewById(R.id.date);
        date.setText("Date : "+nowDate);
        operation();
    }

    public void order(View v){
        Intent intent = new Intent(this,order.class);
        startActivity(intent);
    }

    public void operation(){

        handler.removeCallbacks(updateTimer);//設定定時要執行的方法
        handler.postDelayed(updateTimer, 500);//設定Delay的時間
    }

    private Runnable updateTimer = new Runnable() {
        public void run() {

            handler.postDelayed(this,500);
            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            date.setText("Date : "+nowDate);

        }
    };
}