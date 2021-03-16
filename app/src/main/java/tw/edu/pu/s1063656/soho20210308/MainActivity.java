package tw.edu.pu.s1063656.soho20210308;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private TextView date,tv_result;
    private Handler handler = new Handler();
    private ZXingScannerView zXingScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        date = findViewById(R.id.date);
        date.setText("Date : "+nowDate);
        Display display = getWindowManager().getDefaultDisplay();
        Point outSize = new Point();
        display.getRealSize(outSize);//不能省略,必须有
        int screenWidth = outSize.x;//得到屏幕的宽度
        int screenHeight = outSize.y;//得到屏幕的高度

        Log.d("size",""+screenHeight+"*"+screenWidth);
        operation();

    }
    public void openQRCamera(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this
                        , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    100);
        }else{
            View v = getLayoutInflater().inflate(R.layout.dialog_camera,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(v)
                    .create();
            zXingScannerView = v.findViewById(R.id.ZXingScannerView_QRCode);
            tv_result = v.findViewById(R.id.tv_Result);
            builder.show();
            QRCamera();
        }
    }

    /**開啟QRCode相機*/
    private void QRCamera(){
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
    /**取得權限回傳*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults[0] ==0){
            QRCamera();
        }else{
            Toast.makeText(this, "權限未開啟", Toast.LENGTH_SHORT).show();
        }
    }



    /**取得QRCode掃描到的物件回傳*/
    @Override
    public void handleResult(Result rawResult) {
        tv_result.setText(rawResult.getText());
        //ZXing相機預設掃描到物件後就會停止，以此這邊再次呼叫開啟，使相機可以為連續掃描之狀態
        QRCamera();
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

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        } catch (ActivityNotFoundException e) {
            // display error state to the user

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            View v = getLayoutInflater().inflate(R.layout.dialog_out,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            Dialog dialog = builder.setView(v).create();
            ImageView imageView = v.findViewById(R.id.iv_out);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            Button re_btn = v.findViewById(R.id.re_btn),ok_btn = v.findViewById(R.id.ok_btn);
            re_btn.setEnabled(true);
            ok_btn.setEnabled(true);
            re_btn.setOnClickListener(v1 -> dispatchTakePictureIntent());
            dialog.show();
        }else {

        }
    }

    public void out_btn(View view){
        dispatchTakePictureIntent();
    }


}