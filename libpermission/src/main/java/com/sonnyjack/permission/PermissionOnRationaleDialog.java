package com.sonnyjack.permission;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by SonnyJack on 2018/3/19.
 */

public class PermissionOnRationaleDialog implements View.OnClickListener {
    private Activity mActivity;
    private Dialog mDialog;

    private TextView mTvMessage;

    public PermissionOnRationaleDialog(Activity activity) {
        mActivity = activity;
        init();
    }

    private void init() {
        mDialog = new Dialog(mActivity, R.style.permission_rationale_dialog_style);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_permission_rationale, null);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);

        mDialog.findViewById(R.id.tv_permission_rationale_left).setOnClickListener(this);
        mDialog.findViewById(R.id.tv_permission_rationale_right).setOnClickListener(this);

        mTvMessage = mDialog.findViewById(R.id.tv_permission_rationale_message);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_permission_rationale_left) {
            //next time
        } else {
            //打开设置
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", mActivity.getPackageName(), null));
            mActivity.startActivity(localIntent);
        }
        dismiss();
    }

    public void setMessage(String message) {
        if (null == mTvMessage) {
            return;
        }
        mTvMessage.setText(message);
    }
}
