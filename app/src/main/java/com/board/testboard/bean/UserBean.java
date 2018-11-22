package com.board.testboard.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;


@Table(name="T_UserBean")
public class UserBean {

    @Id(column = "userId")
    private String userId;

    @Column(column = "userName")
    private String userName;
 
    @Column(column = "userPwd")
    private String userPwd;

    @Column(column = "curAccount")
    private String curAccount;

    @Column(column = "loginTime")
    private String loginTime;

    @Column(column = "savePwd")
    private String savePwd;

    @Column(column = "autoLogin")
    private String autoLogin;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getCurAccount() {
        return curAccount;
    }

    public void setCurAccount(String curAccount) {
        this.curAccount = curAccount;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getSavePwd() {
        return savePwd;
    }

    public void setSavePwd(String savePwd) {
        this.savePwd = savePwd;
    }

    public String getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(String autoLogin) {
        this.autoLogin = autoLogin;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", curAccount='" + curAccount + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", savePwd='" + savePwd + '\'' +
                ", autoLogin='" + autoLogin + '\'' +
                '}';
    }
}
