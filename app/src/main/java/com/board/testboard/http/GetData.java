package com.board.testboard.http;

import android.content.Context;


import com.board.testboard.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * [调用WebService]
 * Created by YANGT
 * 2018/4/10.
 */

public class GetData {

    private static final String NAMESPACE = "http://tempuri.org/";
    private String sUrl; //请求后台接口地址


    //返回SoapObject格式
    public static SoapObject getDataByObject(String[] keys, String[] values, String METHODNAME) {
        String sUrl = MyApplication.sUrl;
        String[] tkeys = keys;
        String[] tValues = values;
        String tMETHODNAME = METHODNAME;
        String SOAP_ACTION = "http://tempuri.org/" + METHODNAME;
        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(NAMESPACE,
                METHODNAME);
        SoapObject object = null;
        // 设置需调用WebService接口需要传入的参数
        if (!keys.equals("")) {
            for (int i = 0; i < keys.length; i++) {
                rpc.addProperty(keys[i], values[i]);
            }
        }
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(sUrl, 1000 * 10);

        try {
            // 调用WebService
            transport.call(SOAP_ACTION, envelope);
            System.out.println("Call Successful+++++++++++++!");
            // 获取返回的数据
            if (METHODNAME.equals("GetSmtFeedlistFidItemBind")) { //获取料站表(方法)
                object = (SoapObject) envelope.getResponse();
            } else {
                object = (SoapObject) envelope.bodyIn;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    //返回SoapObject格式
    public static SoapObject getDataByObject(String sJson, String METHODNAME) {
        String sUrl = MyApplication.sUrl;
        boolean isBodyIn=true;
        switch (METHODNAME) {
            case "TimsGetDutyPersonLineUserInfo":
                isBodyIn = false;
                break;
            default:
                break;
        }
        JSONObject jsonObject = null;
        try {
            if(sJson!=null)
            {
                jsonObject = new JSONObject(sJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String SOAP_ACTION = "http://tempuri.org/" + METHODNAME;
        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(NAMESPACE,
                METHODNAME);
        SoapObject object = null;
        // 设置需调用WebService接口需要传入的参数
        if (jsonObject!=null) {
            try {
                for (int i = 0; i <jsonObject.length(); i++) {
                    rpc.addProperty(jsonObject.names().get(i).toString(), jsonObject.getString(jsonObject.names().get(i).toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(sUrl, 1000 * 10);
        try {
            // 调用WebService
            transport.call(SOAP_ACTION, envelope);
            System.out.println("Call Successful+++++++++++++!");
            // 获取返回的数据
            if(isBodyIn) {
                object = (SoapObject) envelope.bodyIn;
            }
            else{
                object =(SoapObject)envelope.getResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static String getDataByJson(Context context,String sJson, String methodName) {
        String sUrl = MyApplication.sUrl;
        JSONObject jsonObject = null;
        String result = "";
        String eException = "";
        try {
            if (sJson != null) {
                jsonObject = new JSONObject(sJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            eException = "[{\"status\":\"ERROR\",\"msg\":\"上传的json异常！" + e.getMessage() + "\"}]";
            return eException;
        }
        if(CheckNetwork.isWifiConnected(context)){
            String SOAP_ACTION = NAMESPACE + methodName;
            SoapObject rpc = new SoapObject(NAMESPACE, methodName);// 指定WebService的命名空间和调用的方法名

            if (jsonObject != null) { // 设置需调用WebService接口需要传入的参数
                try {
                    for (int i = 0; i < jsonObject.length(); i++) {
                        rpc.addProperty(jsonObject.names().get(i).toString(), jsonObject.getString(jsonObject.names().get(i).toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    eException = "[{\"status\":\"ERROR\",\"msg\":\"上传的json异常！" + e.getMessage() + "\"}]";
                    return eException;
                }
            }
            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;// 设置是否调用的是dotNet开发的WebService
            envelope.setOutputSoapObject(rpc);// 等价于envelope.bodyOut = rpc;

            HttpTransportSE transport = new HttpTransportSE(sUrl, 1000 * 15);
            try {

                transport.call(SOAP_ACTION, envelope);// 调用WebService
                System.out.println("Call Successful+++++++++++++!");

                SoapObject object = (SoapObject) envelope.bodyIn;// 获取返回的数据
                result = object.getProperty(0).toString();

            } catch (Exception e) {
                e.printStackTrace();
                eException = "[{\"status\":\"ERROR\",\"msg\":\"数据已提交,网络异常或服务器超时无返回结果,请重试!" + e.getMessage() + "\"}]";
                return eException;
            }
        }else {
            result = "[{\"status\":\"ERROR\",\"msg\":\"网络异常,请重试!" + "\"}]";
        }

        return result;
    }
}
