#使用方式
    api 'com.sonnyjack.permission:PermissionUtils:0.1.0'  或者
    implementation 'com.sonnyjack.permission:PermissionUtils:0.1.0'
    
该库引用com.android.support:appcompat-v7:27.1.0，如果你想统一你项目中的appcompat-v7的版本，可像这样引用：

       implementation ('com.sonnyjack.permission:PermissionUtils:0.1.0'){
            exclude(group: 'com.android.support', module: 'appcompat-v7')
        }

如果你的项目混淆：

-dontwarn com.sonnyjack.permission.**

-keep class com.sonnyjack.permission.** {*;}

用法：

        List<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.CAMERA);
        PermissionUtils.getInstances().requestPermission(this, permissionList, new IRequestPermissionCallBack() {
            @Override
            public void onGranted() {
                 Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
            }
    
            @Override
            public void onDenied() {
                 Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
         });

如果你想自己在用户拒绝权限后换种方法提示用户，可重写onRationale方法并返回true告知PermissionUtils，代码如下：

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
        
        
如果遇到什么问题可以加我Q：252624617  或者issues反馈