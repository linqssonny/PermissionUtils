package com.sonnyjack.permission;

/**
 * Created by SonnyJack on 2018/3/18.
 */

public abstract class IRequestPermissionCallBack {

    //请求权限成功
    public abstract void onGranted();

    //请求权限失败
    public abstract void onDenied();

    //ActivityCompat.shouldShowRequestPermissionRationale为true执行该方法
    //onRationale如果自己处理，则需把返回值改为true，否则(false)默认为弹出dialog询问是否跳到设置页面
    public boolean onRationale() {
        return false;
    }
}
