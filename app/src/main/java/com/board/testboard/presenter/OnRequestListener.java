package com.board.testboard.presenter;

import java.util.List;

public interface OnRequestListener<T> {
 
    void onSuccess(List<T> list);

    void onFail(String result);
}
