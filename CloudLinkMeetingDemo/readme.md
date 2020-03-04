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
3. 参考config.gradle, 在app/build.gradle添加开源包依赖

```
//sdk 编译的aar
    implementation(name : 'HWMBizBase-release', ext: 'aar')
    implementation(name : 'HWMConf-release', ext: 'aar')
    implementation(name : 'HWMDB-release', ext: 'aar')
    implementation(name : 'HWMFoundation-release', ext: 'aar')
    implementation(name : 'HWMFramework-release', ext: 'aar')
    implementation(name : 'HWMHTTP-release', ext: 'aar')
    implementation(name : 'HWMLogger-release', ext: 'aar')
    implementation(name : 'HWMLogin-release', ext: 'aar')
    implementation(name : 'HWMMobileCommonUI-release', ext: 'aar')
    implementation(name : 'HWMMobileConfUI-release', ext: 'aar')
    implementation(name : 'HWMPermission-release', ext: 'aar')
    implementation(name : 'HWMSecurity-release', ext: 'aar')
    implementation(name : 'HWMSetting-release', ext: 'aar')
    implementation(name : 'HWMThreadPool-release', ext: 'aar')
    implementation(name : 'HWMTup-release', ext: 'aar')
    implementation(name : 'HWMContact-release', ext: 'aar')
    implementation(name : 'HWMUisdk-release', ext: 'aar')
```
4. sdk初始化
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
        HWMSdk.getOpenApi(getActivity()).login(account, password, new HwmCallback<LoginResult>() {
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