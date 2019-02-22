package com.board.testboard.presenter;

import org.ksoap2.serialization.SoapObject;

public interface OnGetSoapObjectListener {

    void onGetData(SoapObject object);

    void onGetDataError(String error);
}
