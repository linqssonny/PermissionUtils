package com.sonnyjack.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SonnyJack on 2018/3/18.
 */

public class PermissionUtils {

    private Activity mActivity;
    private IRequestPermissionCallBack mIRequestPermissionCallBack;

    private PermissionUtils() {

    }

    private static class PermissionUtilsManager {
        private static PermissionUtils sPermissionUtils = new PermissionUtils();
    }

    public static PermissionUtils getInstances() {
        return PermissionUtilsManager.sPermissionUtils;
    }

    private void executeIRequestPermissionCallBackForOnGranted(IRequestPermissionCallBack iRequestPermissionCallBack) {
        if (null == iRequestPermissionCallBack) {
            return;
        }
        iRequestPermissionCallBack.onGranted();
    }

    private void executeIRequestPermissionCallBackForOnDenied(IRequestPermissionCallBack iRequestPermissionCallBack) {
        if (null == iRequestPermissionCallBack) {
            return;
        }
        iRequestPermissionCallBack.onDenied();
    }

    private boolean executeIRequestPermissionCallBackForOnRationale(IRequestPermissionCallBack iRequestPermissionCallBack) {
        boolean result = false;
        if (null != iRequestPermissionCallBack) {
            result = iRequestPermissionCallBack.onRationale();
        }
        return result;
    }

    /***
     * return true if have the permission,otherwise false
     *
     * @param context
     * @param permission
     * @return
     */
    public boolean checkSelfPermission(Context context, String permission) {
        int hasPermission = ContextCompat.checkSelfPermission(context, permission);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * execute ActivityCompat shouldShowRequestPermissionRationale method
     *
     * @param activity
     * @param permission
     * @return
     */
    public boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * return true if us request permission success,otherwise false
     *
     * @param grantResults
     * @return
     */
    public boolean isRequestPermissionGranted(int[] grantResults) {
        boolean granted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        return granted;
    }

    /**
     * start request permission
     *
     * @param activity
     * @param permissions
     * @param iRequestPermissionCallBack
     */
    public void requestPermission(Activity activity, List<String> permissions, IRequestPermissionCallBack iRequestPermissionCallBack) {
        // if the os version < 23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            executeIRequestPermissionCallBackForOnGranted(iRequestPermissionCallBack);
            return;
        }
        if (null == activity) {
            throw new NullPointerException("the activity is empty");
        }

        if (null == permissions || permissions.size() <= 0) {
            throw new NullPointerException("the permissions list is empty");
        }
        //filter have permissions
        ArrayList<String> permissionsList = new ArrayList<>();
        for (int i = 0; i < permissions.size(); i++) {
            String permission = permissions.get(i);
            if (!checkSelfPermission(activity, permission)) {
                permissionsList.add(permission);
            }
        }
        //have permission
        if (permissionsList.size() <= 0) {
            executeIRequestPermissionCallBackForOnGranted(iRequestPermissionCallBack);
            return;
        }
        //判断permission是否曾经拒绝过
        for (int i = 0; i < permissionsList.size(); i++) {
            String permission = permissionsList.get(i);
            if (shouldShowRequestPermissionRationale(activity, permission)) {
                if (!executeIRequestPermissionCallBackForOnRationale(iRequestPermissionCallBack)) {
                    showPermissionAskDialog(activity, buildMessageByPermission(activity, permission));
                }
                return;
            }
        }
        mActivity = activity;
        mIRequestPermissionCallBack = iRequestPermissionCallBack;
        PermissionActivity.startRequestPermission(mActivity, permissionsList);
    }

    /**
     * show permission ask dialog when return true of execute shouldShowRequestPermissionRationale method
     * and return false of IRequestPermissionCallBack execute method onRationale
     *
     * @param activity
     * @param message
     */
    private void showPermissionAskDialog(Activity activity, String message) {
        PermissionOnRationaleDialog permissionOnRationaleDialog = new PermissionOnRationaleDialog(activity);
        permissionOnRationaleDialog.setMessage(message);
        permissionOnRationaleDialog.show();
    }

    /**
     * return show message for PermissionOnRationaleDialog
     *
     * @param activity
     * @param permission
     * @return
     */
    private String buildMessageByPermission(Activity activity, String permission) {
        String message;
        switch (permission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                //外部存储器
                message = activity.getString(R.string.permission_message_storage);
                break;
            case Manifest.permission.WRITE_CALENDAR:
            case Manifest.permission.READ_CALENDAR:
                //日历
                message = activity.getString(R.string.permission_message_calendar);
                break;
            case Manifest.permission.CAMERA:
                //相机
                message = activity.getString(R.string.permission_message_camera);
                break;
            case Manifest.permission.WRITE_CONTACTS:
            case Manifest.permission.READ_CONTACTS:
            case Manifest.permission.GET_ACCOUNTS:
                //通讯录
                message = activity.getString(R.string.permission_message_contacts);
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                //定位
                message = activity.getString(R.string.permission_message_location);
                break;
            case Manifest.permission.RECORD_AUDIO:
                //麦克风
                message = activity.getString(R.string.permission_message_audio);
                break;
            case Manifest.permission.READ_PHONE_STATE:
            case Manifest.permission.CALL_PHONE:
            case Manifest.permission.READ_CALL_LOG:
            case Manifest.permission.WRITE_CALL_LOG:
            case Manifest.permission.ADD_VOICEMAIL:
            case Manifest.permission.USE_SIP:
            case Manifest.permission.PROCESS_OUTGOING_CALLS:
                //通话记录
                message = activity.getString(R.string.permission_message_phone);
                break;
            case Manifest.permission.BODY_SENSORS:
                //传感器
                message = activity.getString(R.string.permission_message_sensors);
                break;
            case Manifest.permission.READ_SMS:
            case Manifest.permission.RECEIVE_WAP_PUSH:
            case Manifest.permission.RECEIVE_MMS:
            case Manifest.permission.RECEIVE_SMS:
            case Manifest.permission.SEND_SMS:
            case Manifest.permission.BROADCAST_SMS:
                //短信
                message = activity.getString(R.string.permission_message_sms);
                break;
            default:
                //相关权限
                message = activity.getString(R.string.permission_message_unknown);
                break;
        }
        return activity.getString(R.string.permission_rationale_dialog_message, message);
    }

    public void finish() {
        mIRequestPermissionCallBack = null;
        mActivity = null;
    }

    public static class PermissionActivity extends Activity {

        private static final String INTENT_PERMISSION_KEY = "permission_key";

        public static void startRequestPermission(Activity activity, ArrayList<String> permissionList) {
            Intent intent = new Intent(activity, PermissionActivity.class);
            intent.putExtra(INTENT_PERMISSION_KEY, permissionList);
            activity.startActivity(intent);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ArrayList<String> permissionList = getIntent().getStringArrayListExtra(INTENT_PERMISSION_KEY);
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

        @Override
        protected void onDestroy() {
            PermissionUtils.getInstances().finish();
            super.onDestroy();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (1 == requestCode) {
                if (PermissionUtils.getInstances().isRequestPermissionGranted(grantResults)) {
                    PermissionUtils.getInstances().executeIRequestPermissionCallBackForOnGranted(
                            PermissionUtils.getInstances().mIRequestPermissionCallBack
                    );
                } else {
                    PermissionUtils.getInstances().executeIRequestPermissionCallBackForOnDenied(
                            PermissionUtils.getInstances().mIRequestPermissionCallBack
                    );
                }
            }
            finish();
        }
    }
}
