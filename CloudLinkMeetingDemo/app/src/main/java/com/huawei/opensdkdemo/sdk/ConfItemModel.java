package com.huawei.opensdkdemo.sdk;

import android.text.TextUtils;

import com.huawei.hwmconf.presentation.util.DateUtil;
import com.huawei.hwmconf.sdk.constant.ConfConstants;

public class ConfItemModel {
    private String confSubject;
    private int mediaType;
    private String scheduserName;
    private String startTime;
    private String endTime;
    private String confId;
    private String vmrConferenceId;
    private String chairmanPwd;
    private String accessNumber;
    private String generalPwd;

    public String getConfSubject() {
        return confSubject;
    }

    public void setConfSubject(String confSubject) {
        this.confSubject = confSubject;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getScheduserName() {
        return scheduserName;
    }

    public void setScheduserName(String scheduserName) {
        this.scheduserName = scheduserName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getHMStartTime() {
        return DateUtil.formatTime(startTime, DateUtil.FMT_YMDHM, DateUtil.FMT_HM);
    }

    public String getHMEndTime() {
        return DateUtil.formatTime(endTime, DateUtil.FMT_YMDHM, DateUtil.FMT_HM);
    }

    public int getDiffDays() {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return 0;
        }

        return DateUtil.differentDays(DateUtil.convertStringToDate(startTime, DateUtil.FMT_YMDHM), DateUtil.convertStringToDate(endTime, DateUtil.FMT_YMDHM));
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getConfId() {
        return confId;
    }

    public void setConfId(String confId) {
        this.confId = confId;
    }

    public String getChairmanPwd() {
        return chairmanPwd;
    }

    public void setChairmanPwd(String chairmanPwd) {
        this.chairmanPwd = chairmanPwd;
    }

    public String getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber) {
        this.accessNumber = accessNumber;
    }

    public String getGeneralPwd() {
        return generalPwd;
    }

    public void setGeneralPwd(String generalPwd) {
        this.generalPwd = generalPwd;
    }

    public int getMediaTypeImg() {
        if ((mediaType & ConfConstants.CONF_MEDIA_TYPE.CONF_MEDIA_TYPE_VIDEO) != 0 ||
                (mediaType & ConfConstants.CONF_MEDIA_TYPE.CONF_MEDIA_TYPE_HDVIDEO) != 0) {
            return com.huawei.hwmmobileconfui.R.drawable.conf_type_video_img;
        }

        return com.huawei.hwmmobileconfui.R.drawable.conf_type_audio_img;
    }

    public String getVmrConferenceId() {
        return vmrConferenceId;
    }

    public void setVmrConferenceId(String vmrConferenceId) {
        this.vmrConferenceId = vmrConferenceId;
    }

    public boolean isVideo() {
        return ((mediaType & ConfConstants.CONF_MEDIA_TYPE.CONF_MEDIA_TYPE_VIDEO) != 0 ||
                (mediaType & ConfConstants.CONF_MEDIA_TYPE.CONF_MEDIA_TYPE_HDVIDEO) != 0);
    }
}
