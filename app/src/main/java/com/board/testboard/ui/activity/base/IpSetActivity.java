package com.board.testboard.ui.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**  
 * [设置-IP]
 */

public class IpSetActivity extends TitlebarBaseActivity {

    @BindView(R.id.ed_ip_1)
    EditText ed_ip_1;
    @BindView(R.id.ed_ip_2)
    EditText ed_ip_2;
    @BindView(R.id.ed_ip_3)
    EditText ed_ip_3;
    @BindView(R.id.ed_ip_4)
    EditText ed_ip_4;
    @BindView(R.id.ed_port)
    EditText ed_port;

    private Context context;
    private String sIP1 = "";
    private String sIP2 = "";
    private String sIP3 = "";
    private String sIP4 = "";
    private String sPort = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = IpSetActivity.this;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_ip_set;
    }

    @Override
    protected void onResume() {
        super.onResume();

        String ip = SharedPreferencesUtil.getValue(getBaseContext(), "sServerIp", "127.0.0.1");
        int sServerPort = SharedPreferencesUtil.getValue(getBaseContext(), "sServerPort", 80);
        String IP[] = ip.split("\\.");
        ed_ip_1.setText(IP[0]);
        ed_ip_2.setText(IP[1]);
        ed_ip_3.setText(IP[2]);
        ed_ip_4.setText(IP[3]);
        ed_port.setText(String.valueOf(sServerPort));
        checkAll(); //EditText全选(获得焦点时全选文本)
        editTextListener(); //输入IP监听
        enterListener(); //PDA Enter键监听
    }

    /**
     * 初始化标题信息
     */
    @Override
    protected void initTitleView() {
        super.initTitleView();
        mTitle.setTitleText(R.string.ip_setting);
    }

    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        //IP/端口保存
        ipPortSave();
    }
    //EditText全选(获得焦点时全选文本)
    private void checkAll() {
        ed_ip_1.setSelectAllOnFocus(true);
        ed_ip_2.setSelectAllOnFocus(true);
        ed_ip_3.setSelectAllOnFocus(true);
        ed_ip_4.setSelectAllOnFocus(true);
        ed_port.setSelectAllOnFocus(true);
        ed_ip_1.requestFocus();
    }

    //输入IP监听
    private void editTextListener() {

        TextChangeListen[] mTextWatcher = new TextChangeListen[4];
        EditText[] editTexts_List = new EditText[4];
        editTexts_List[0] = ed_ip_1;
        editTexts_List[1] = ed_ip_2;
        editTexts_List[2] = ed_ip_3;
        editTexts_List[3] = ed_ip_4;

        //循环添加监听事件
        for (int i = 0; i < 4; i++) {
            mTextWatcher[i] = new TextChangeListen(editTexts_List[i]);
            editTexts_List[i].addTextChangedListener(mTextWatcher[i]);
        }
    }



    //IP录入监听
    private class TextChangeListen implements TextWatcher {

        public EditText IP_Edit;

        public TextChangeListen(EditText IP_Edit) {
            super();
            this.IP_Edit = IP_Edit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 3) {
                if (Integer.parseInt(s.toString()) <= 255) {
                    if (this.IP_Edit == ed_ip_1) {
                        ed_ip_2.requestFocus();
                    }
                    if (this.IP_Edit == ed_ip_2) {
                        ed_ip_3.requestFocus();
                    }
                    if (this.IP_Edit == ed_ip_3) {
                        ed_ip_4.requestFocus();
                    }
                } else {
                    ToastUtil.showShortToast(IpSetActivity.this, String.format(getString(R.string.ip_error), s.toString().trim()));
                    this.IP_Edit.setText("0");
                }
            } else if (s.length() == 0) {
                if (this.IP_Edit == ed_ip_1) {
                    ed_ip_1.setText("0");
                }
                if (this.IP_Edit == ed_ip_2) {
                    ed_ip_1.requestFocus();
                    ed_ip_2.setText("0");
                }
                if (this.IP_Edit == ed_ip_3) {
                    ed_ip_2.requestFocus();
                    ed_ip_3.setText("0");
                }
                if (this.IP_Edit == ed_ip_4) {
                    ed_ip_3.requestFocus();
                    ed_ip_4.setText("0");
                }
            }
        }
    }

    //PDA Enter键监听
    public void enterListener() {
        ed_ip_1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_ip_1.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    ed_ip_2.requestFocus();
                    return true;
                } else {
                    return false;
                }

            }
        });

        ed_ip_2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_ip_2.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    ed_ip_3.requestFocus();
                    return true;
                } else {
                    return false;
                }

            }
        });

        ed_ip_3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_ip_3.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    ed_ip_4.requestFocus();
                    return true;
                } else {
                    return false;
                }
            }
        });

        ed_ip_4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_ip_4.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    ed_port.requestFocus();
                    return true;
                } else {
                    return false;
                }
            }
        });

        ed_port.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ed_port.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    mTitle.setBtnRightRequestFocus();
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    //IP/端口保存
    private void ipPortSave() {
        sIP1 = ed_ip_1.getText().toString().trim();
        sIP2 = ed_ip_2.getText().toString().trim();
        sIP3 = ed_ip_3.getText().toString().trim();
        sIP4 = ed_ip_4.getText().toString().trim();
        sPort = ed_port.getText().toString().trim();
        String sServerIp = sIP1 + "." + sIP2 + "." + sIP3 + "." + sIP4;
        Integer sServerPort = 0;
        if (TextUtils.isEmpty(sIP1) || TextUtils.isEmpty(sIP2) || TextUtils.isEmpty(sIP3) || TextUtils.isEmpty(sIP4)) {
            ToastUtil.showShortToast(context, getString(R.string.ip_error_whole));
            return;
        } else if (TextUtils.equals(sServerIp, "0.0.0.0") || TextUtils.equals(sServerIp, "00.00.00.00") || TextUtils.equals(sServerIp, "000.000.000.000") || TextUtils.equals(sServerIp, "255.255.255.255")) {
            ToastUtil.showShortToast(context, String.format(getString(R.string.ip_error_not_a_valid_address), sServerIp));
            return;
        } else if (TextUtils.isEmpty(sPort)) {
            ToastUtil.showShortToast(context, getString(R.string.port_error_whole));
            return;
        }
        try {
            sServerPort = Integer.parseInt(sPort);
        } catch (Exception e) {
            ToastUtil.showShortToast(context, getString(R.string.port_isnum));
            return;
        }
        //保存服务器IP、端口至本地文件中
        SharedPreferencesUtil.saveValue(getBaseContext(), "sServerIp", sServerIp);
        SharedPreferencesUtil.saveValue(getBaseContext(), "sServerPort", sServerPort);
        String sServiceBese = "http://" + sServerIp + ":" + sServerPort; //IP+端口
        String sWeService = SharedPreferencesUtil.getValue(getBaseContext(), "sWebService", "");
        MyApplication.sUrl = sServiceBese + sWeService;
        ToastUtil.showShortToast(context, getString(R.string.save_success));
        IpSetActivity.this.finish();
    }

}
