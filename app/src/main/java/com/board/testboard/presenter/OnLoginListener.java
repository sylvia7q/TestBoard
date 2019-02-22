package com.topcee.kanban.presenter;

public interface OnLoginListener {

    void loginingStart();

    void onLoginEnd(String result);

    void onLoginError(String error);
}
