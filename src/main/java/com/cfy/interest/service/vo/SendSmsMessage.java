package com.cfy.interest.service.vo;

/**
 * 用于发送短信的数据传递
 */
public class SendSmsMessage {
    //发送验证码得手机号
    private String phone;
    //短信验证码
    private String authCode;
    //发送短信状态信息
    private String message;
    //是否成功
    private boolean success;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "SendSmsMessage{" +
                "phone='" + phone + '\'' +
                ", authCode='" + authCode + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
