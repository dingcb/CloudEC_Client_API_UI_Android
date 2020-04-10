package com.huawei.opensdkdemo;

import com.huawei.conflogic.HwmConfListInfo;

import java.util.List;

public interface IConfListUpdate {

   void onConfListUpdateNotify(List<HwmConfListInfo> list);
}
