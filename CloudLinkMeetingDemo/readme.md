# 华为会议sdk集成步骤
1. 将libs下的aar包拷贝到你的工程目录
2. 参考config.gradle, 在app/build.gradle添加开源包依赖

```
    //sdk 用到的一些三方框架
    api rootProject.ext.dependencies["palette-v7"]
    api rootProject.ext.dependencies["aspectjrt"]
    implementation ('com.jakewharton.rxbinding2:rxbinding:2.0.0') {
        exclude group: 'io.reactivex.rajava2'
    }
    implementation rootProject.ext.dependencies["rxjava"]
    implementation rootProject.ext.dependencies["gson"]
    implementation rootProject.ext.dependencies["eventbus"]
    implementation rootProject.ext.dependencies["okhttp"]
    implementation rootProject.ext.dependencies["tinypinyin"]
    implementation rootProject.ext.dependencies["autodispose"]
    implementation rootProject.ext.dependencies["autodispose-lifecycle"]
    implementation rootProject.ext.dependencies["lifecycle-extensions"]
```
3. 参考config.gradle, 在app/build.gradle添加会议sdk依赖

```
    //sdk 编译的aar
    implementation(name: 'HWMBizBase-release', ext: 'aar')
    implementation(name: 'HWMConf-release', ext: 'aar')
    implementation(name: 'HWMDB-release', ext: 'aar')
    implementation(name: 'HWMFoundation-release', ext: 'aar')
    implementation(name: 'HWMFramework-release', ext: 'aar')
    implementation(name: 'HWMHTTP-release', ext: 'aar')
    implementation(name: 'HWMLogger-release', ext: 'aar')
    implementation(name: 'HWMLogin-release', ext: 'aar')
    implementation(name: 'HWMMobileCommonUI-release', ext: 'aar')
    implementation(name: 'HWMMobileConfUI-release', ext: 'aar')
    implementation(name: 'HWMPermission-release', ext: 'aar')
    implementation(name: 'HWMSecurity-release', ext: 'aar')
    implementation(name: 'HWMSetting-release', ext: 'aar')
    implementation(name: 'HWMThreadPool-release', ext: 'aar')
    implementation(name: 'HWMTup-release', ext: 'aar')
    implementation(name: 'HWMContact-release', ext: 'aar')
    implementation(name: 'HWMUisdk-release', ext: 'aar')
    implementation(name: 'MobileHybrid-release', ext: 'aar')
    implementation(name: 'HWMClink-release', ext: 'aar')
```

4. 添依赖的一些权限：
```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    <uses-permission android:name="com.huawei.android.launcher.permission.CHANGE_BADGE" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />

    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />

    <!--会议中会启动前台服务器保活-->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

5. sdk初始化, 注意要放在application的oncreate里，尽量时许提前，sdk内部通过监听activity的生命获取当前activity，
跳转界面，否则可能出现界面无法跳转的情况
```
OpenSDKConfig sdkConfig = new OpenSDKConfig(this)
                .setAppId("openSDKDemo")
                .setServerAddress(DBConfig.Default.getServerAddress())
                .setServerPort(DBConfig.Default.getServerPort())
                .setNotifyHandler(notifyHandler);
        HWMSdk.init(this, sdkConfig);
```
# 华为会议接口说明
1. 登录
```
        LoginParam loginParam = new LoginParam()
                .setLoginAuthType(LoginAuthType.Account_And_Password)
                .setAccount(account)
                .setPassword(password);
        HWMSdk.getOpenApi(getActivity()).login(loginParam, new HwmCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                dismissLoading();
                if (loginResult != null ) {
                    DemoUtil.showToast(getContext(), "登录成功" + loginResult.getUserUuid());
                } else {
                    DemoUtil.showToast(getContext(), "您已经登录");
                }
            }

            @Override
            public void onFailed(int retCode, String desc) {
                dismissLoading();
                Error error = HWMBizSdk.getLoginApi().convertErrorCodeToUI(retCode);
                String errorTip = getLoginErrTips(error);
                new BaseDialogBuilder(getActivity())
                        .setMessage(getLoginErrTips(error))
                        .setMessagePosition(Gravity.CENTER)
                        .addAction("确认", (dialog, button, index) -> dialog.dismiss())
                        .show();
            }
        });
```
2. 创建会议
```
        CreateConfParam createConfParam = new CreateConfParam()
                            .setSubject(subject)
                            .setConfType(confType)
                            .setNeedPassword(needPassword);
        HWMSdk.getOpenApi(getActivity()).createConf(createConfParam, new HwmCallback<ConfInfo>() {
             @Override
             public void onSuccess(ConfInfo confInfo) {
                   DemoUtil.showToast(getContext(), "创建会议成功: 会议id：" + confInfo.getConfId() +
                                    ";会议密码：" + confInfo.getConfPwd());
             }
        
             @Override
             public void onFailed(int retCode, String desc) {
                            dismissLoading();
                  String err = ErrorMessageFactory.create(Utils.getApp(), retCode);
                  if (TextUtils.isEmpty(err)) {
                      err = Utils.getApp().getString(com.huawei.hwmmobileconfui.R.string.conf_create_error);
                  }
                  DemoUtil.showToast(getContext(), "创建会议失败: " + err);
             }
        });
```
3. 加入会议
```
        JoinConfParam joinConfParam = new JoinConfParam()
                .setConfId(mId)
                .setNickname(nickName)
                .setCameraOn(true)
                .setMicOn(true);
        HWMSdk.getOpenApi(getActivity()).joinConf(joinConfParam, new HwmCallback<Void>() {
            @Override
            public void onSuccess(Void ret) {
                DemoUtil.showToast(getContext(), "加入会议成功");
            }

            @Override
            public void onFailed(int retCode, String desc) {
                String err = ErrorMessageFactory.create(Utils.getApp(), retCode);
                if (TextUtils.isEmpty(err)) {
                    err = Utils.getApp().getString(com.huawei.hwmmobileconfui.R.string.conf_join_fail_tip);
                }
                DemoUtil.showToast(getContext(), "加入会议失败: " + err);
            }
        });
```
4. 呼叫
```
        CallParam callParam = new CallParam()
                .setNumber(number)
                .setAccount(account)
                .setCalleeUuid(accountUuid)
                .setVideo(isVideo);
        HWMSdk.getOpenApi(getActivity()).startCall(callParam, new HwmCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                DemoUtil.showToast(getContext(), "呼叫成功");
            }

            @Override
            public void onFailed(int retCode, String desc) {
                DemoUtil.showToast(getContext(), "呼叫失败："+ retCode + ", desc: " + desc);
            }
        });
```
5. 注销
```
    HWMSdk.getOpenApi(getActivity()).logout(new HwmCallback<LoginCompletedResult>() {
            @Override
            public void onSuccess(LoginCompletedResult result) {
                DemoUtil.showToast(getContext(), "登出成功");
            }

            @Override
            public void onFailed(int retCode, String desc) {
                DemoUtil.showToast(getContext(), "等出失败 : " + retCode + "; " + desc);
            }
        });
```


# FAQ
1. 是否支持64为的so库？
```
    64位的so库正在测试中，暂时不支持，将在下个版本中支持，注意build.gradle中的配置
    ndk {
        abiFilters "armeabi-v7a"
    }
```


2. 如果不想使用我们的im能力，可以在application project的build.gradle里增加以下配置，去掉im相关的so库，可以减少apk大小，
打包成apk后，可以解压查看下这些so是否已经删除

```
android {
        packagingOptions {
            exclude '**/libtup_im_basemessage.so'
            exclude '**/libtup_im_clib.so'
            exclude '**/libtup_im_clientimpl.so'
            exclude '**/libtup_im_core.so'
            exclude '**/libtup_im_ecsimp.so'
            exclude '**/libtup_im_eserverimpl.so'
            exclude '**/libtup_im_json.so'
            exclude '**/libtup_im_message.so'
            exclude '**/libtup_im_service.so'
            exclude '**/libtup_im_util.so'
        }
    }
```

3. 如果不想使用我们数据会议能力（共享，标注），可以在application project的build.gradle里增加以下配置，
去掉数据会议相关的so库，可以减少库大小；打包成apk后，可以解压查看下这些libTupConf.so是否已经删除

```
android {
        packagingOptions {
            excludes += ['**/libTupConf.so']
        }
    }
```

