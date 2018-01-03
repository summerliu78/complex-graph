package com.yinker.tinyv.vo;

/**
 * Created by ThinkPad on 2017/6/7.
 */
public class TrustLogBean {
    private String mobile;
    private String realStatus;
    private String midleStatus;
    private String score;
    
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(String realStatus) {
        this.realStatus = realStatus;
    }

    public String getMidleStatus() {
        return midleStatus;
    }

    public void setMidleStatus(String midleStatus) {
        this.midleStatus = midleStatus;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "TrustLogBean{" +
                "mobile='" + mobile + '\'' +
                ", realStatus='" + realStatus + '\'' +
                ", midleStatus='" + midleStatus + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
