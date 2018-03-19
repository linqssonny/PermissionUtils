package com.sonnyjack.mpermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sonnyjack.permission.IRequestPermissionCallBack;
import com.sonnyjack.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        List<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.CAMERA);
        PermissionUtils.getInstances().requestPermission(this, permissionList, new IRequestPermissionCallBack() {

            @Override
            public boolean onRationale() {
                //you  can   do   what  you  want to do
                return true;
            }

            @Override
            public void onGranted() {
                Toast.makeText(MainActivity.this, "request success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied() {
                Toast.makeText(MainActivity.this, "request fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
