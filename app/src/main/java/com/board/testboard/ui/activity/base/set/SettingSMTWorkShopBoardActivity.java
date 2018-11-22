package com.board.testboard.ui.activity.base.set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.ui.activity.base.PattemMenuActivity;
import com.board.testboard.ui.activity.base.TitlebarBaseActivity;
import com.board.testboard.ui.activity.base.module.WorkshopLineSettingActivity;
import com.board.testboard.utils.DialogUtil;
import com.board.testboard.utils.LogUtil;
import com.board.testboard.utils.SharedPreferencesUtil;

import butterknife.BindView;

/**
 * 车间看板设置
 */

public class SettingSMTWorkShopBoardActivity extends TitlebarBaseActivity {


    @BindView(R.id.rl_smt_more_line_set_layout)
    RelativeLayout rl_smt_more_line_set_layout; //多线别设置

    @BindView(R.id.tv_smt_more_line_set)
    TextView tvSmtMoreLineSet;

    @BindView(R.id.rl_color_set_layout)
    RelativeLayout rl_color_set_layout;//颜色设置

    @BindView(R.id.rl_menu_set_layout)
    RelativeLayout rl_menu_set_layout;//菜单设置

    @BindView(R.id.rl_set_layout)
    RelativeLayout rl_set_layout;//设置



    private Dialog setMaterialWarningDialog;
    private EditText etMaterialWarningPercent;
    private EditText etMaterialWarningStation;

    private Context context;
    private Dialog dialog;

    String[] colorTitleLabels = new String[6];
    //标题字体颜色设置
    private Dialog setBackgroundDialog = null;
    private TextView tv_title; //标题
    private TextView tv_show_background_color1; //显示背景颜色
    private TextView tv_group_title1; //
    private SeekBar progress_red1; //红色
    private SeekBar progress_green1; //绿色
    private SeekBar progress_blue1; //蓝色
    private TextView tv_red_data1;
    private TextView tv_green_data1;
    private TextView tv_blue_data1;

    private LinearLayout ll_group2;
    private TextView tv_show_background_color2; //显示背景颜色
    private TextView tv_group_title2; //
    private SeekBar progress_red2; //红色
    private SeekBar progress_green2; //绿色
    private SeekBar progress_blue2; //蓝色
    private TextView tv_red_data2;
    private TextView tv_green_data2;
    private TextView tv_blue_data2;
    private Button btn_confirm; //确定
    private Button btn_cancel; //取消

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SettingSMTWorkShopBoardActivity.this;

    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_setting;
    }

    //标题信息
    @Override
    protected void initTitleView() {
        super.initTitleView();
        mTitle.setTitleText(R.string.smt_total_setting);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setListenerView();

    }

    //监听事件
    private void setListenerView() {


        //多线别设置
        rl_smt_more_line_set_layout.setVisibility(View.VISIBLE);
        tvSmtMoreLineSet.setText(getString(R.string.more_work_shop_line_setting));
        rl_smt_more_line_set_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(context, WorkshopLineSettingActivity.class);
            }
        });


        //颜色设置
        rl_color_set_layout.setVisibility(View.VISIBLE);
        rl_color_set_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorAlert();
            }
        });

        //菜单设置
        rl_menu_set_layout.setVisibility(View.VISIBLE);
        rl_menu_set_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(context, PattemMenuActivity.class);
            }
        });
        //设置
        rl_set_layout.setVisibility(View.VISIBLE);
        rl_set_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(context, SettingActivity.class);
            }
        });

    }

    /**
     * 模块颜色设置弹出框
     */
    private void showColorAlert() {
        colorTitleLabels = new String[]{getString(R.string.smt_line_title_font_color_set),
                getString(R.string.smt_line_table_title_color_set),
                getString(R.string.smt_line_background_color_set),
                getString(R.string.smt_line_chart_background_color_set),
                getString(R.string.smt_line_chart_product_color_set),
                getString(R.string.smt_line_default_color_set)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.icon_gear);
        builder.setTitle(R.string.smt_line_color_set);
        builder.setItems(colorTitleLabels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which != 5) {
                    showDetailColorSetDialog(which);
                } else {
                    //保存数据至本地文件中-恢复默认颜色设置
                    SharedPreferencesUtil.saveValue(getBaseContext(), "nSMTTotalTitleColor", MyApplication.nDefaultSMTTotalTitleColor);
                    SharedPreferencesUtil.saveValue(getBaseContext(), "nSMTTotalOtherTextColor", MyApplication.nDefaultSMTTotalOtherTextColor);
                    SharedPreferencesUtil.saveValue(getBaseContext(), "nSMTTotalWholeBgColor", MyApplication.nDefaultSMTTotalWholeBgColor);
                    SharedPreferencesUtil.saveValue(getBaseContext(), "nSMTTotalChartBgColor", MyApplication.nDefaultSMTTotalChartBgColor);
                    SharedPreferencesUtil.saveValue(getBaseContext(), "nChartLineProductColor1", MyApplication.nDefaultChartLineProductColor1);
                    SharedPreferencesUtil.saveValue(getBaseContext(), "nChartLineProductColor2", MyApplication.nDefaultChartLineProductColor2);
                }
            }
        });
        builder.show();
    }

    //弹出窗-共用
    private void showDetailColorSetDialog(int which) {
        if (setBackgroundDialog == null) {
            setBackgroundDialog = new Dialog(context, R.style.myDialog);
            setBackgroundDialog.setCancelable(false); //dialog弹出后会点击屏幕或物理返回键，dialog不消失
            setBackgroundDialog.setCanceledOnTouchOutside(false); //dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
            initDetailColorSetDialog(setBackgroundDialog); //初始化Dialog-标题字体颜色设置
            setBackgroundDialog.show();
            DialogUtil.setDialogWidthHeight(context, setBackgroundDialog, 0.50f);//设置对话框的宽和高
            initialization(which); //对话框初始化数据
        }
    }

    //初始化Dialog-标题字体颜色设置
    private void initDetailColorSetDialog(Dialog dialog) {
        LinearLayout layout = (LinearLayout) (LayoutInflater.from(context).inflate(R.layout.dialog_set_color, null));
        tv_title = layout.findViewById(R.id.tv_title); //显示背景颜色
        tv_show_background_color1 = layout.findViewById(R.id.tv_show_background_color1); //显示背景颜色
        tv_group_title1 = layout.findViewById(R.id.tv_group_title1);  //
        progress_red1 = layout.findViewById(R.id.progress_red1);  //红色
        progress_green1 = layout.findViewById(R.id.progress_green1);  //绿色
        progress_blue1 = layout.findViewById(R.id.progress_blue1);  //蓝色
        tv_red_data1 = layout.findViewById(R.id.tv_red_data1);
        tv_green_data1 = layout.findViewById(R.id.tv_green_data1);
        tv_blue_data1 = layout.findViewById(R.id.tv_blue_data1);
        ll_group2 = layout.findViewById(R.id.ll_group2); //显示背景颜色
        tv_show_background_color2 = layout.findViewById(R.id.tv_show_background_color2); //显示背景颜色
        tv_group_title2 = layout.findViewById(R.id.tv_group_title2);  //
        progress_red2 = layout.findViewById(R.id.progress_red2);  //红色
        progress_green2 = layout.findViewById(R.id.progress_green2);  //绿色
        progress_blue2 = layout.findViewById(R.id.progress_blue2);  //蓝色
        tv_red_data2 = layout.findViewById(R.id.tv_red_data2);
        tv_green_data2 = layout.findViewById(R.id.tv_green_data2);
        tv_blue_data2 = layout.findViewById(R.id.tv_blue_data2);
        btn_confirm = (Button) layout.findViewById(R.id.btn_confirm); //确定
        btn_cancel = (Button) layout.findViewById(R.id.btn_cancel); //取消
        dialog.setContentView(layout);
    }

    private void showGroup2(int visibility) {
        ll_group2.setVisibility(visibility);
    }

    //对话框初始化数据
    private void initialization(int which) {
        tv_title.setText(colorTitleLabels[which]);
        int color = Color.rgb(255, 255, 255);
        int color2 = Color.rgb(255, 255, 255);
        // TitelFontColor(标题字体颜色设置)  OtherFontColor(其它字体颜色设置)   BackgroundColor(背景字体颜色设置)
        switch (which) {
            case 0:
                showGroup2(View.GONE);
                color = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalTitleColor", MyApplication.nDefaultSMTTotalTitleColor);
                break;
            case 1:
                showGroup2(View.GONE);
                color = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalOtherTextColor", MyApplication.nDefaultSMTTotalOtherTextColor);
                break;
            case 2:
                showGroup2(View.GONE);
                color = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalWholeBgColor", MyApplication.nDefaultSMTTotalWholeBgColor);
                break;
            case 3:
                showGroup2(View.GONE);
                color = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalChartBgColor", MyApplication.nDefaultSMTTotalChartBgColor);
                break;
            case 4:
                showGroup2(View.VISIBLE);
                tv_group_title1.setText(R.string.smt_line_input_qty);
                tv_group_title2.setText(R.string.smt_line_output_qty);
                color = SharedPreferencesUtil.getValue(getBaseContext(), "nChartLineProductColor1", MyApplication.nDefaultChartLineProductColor1);
                color2 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartLineProductColor2", MyApplication.nDefaultChartLineProductColor2);
                break;
        }
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int r2 = Color.red(color2);
        int g2 = Color.green(color2);
        int b2 = Color.blue(color2);
        progress_red1.setProgress(r);//红色-值
        progress_green1.setProgress(g);//绿色-值
        progress_blue1.setProgress(b);//蓝色-值
        tv_red_data1.setText(r + "");
        tv_green_data1.setText(g + "");
        tv_blue_data1.setText(b + "");

        progress_red2.setProgress(r2);//红色-值
        progress_green2.setProgress(g2);//绿色-值
        progress_blue2.setProgress(b2);//蓝色-值
        tv_red_data2.setText(r2 + "");
        tv_green_data2.setText(g2 + "");
        tv_blue_data2.setText(b2 + "");
        tv_group_title1.setTextColor(color);
        tv_group_title2.setTextColor(color2);
        tv_show_background_color1.setBackgroundColor(color);
        tv_show_background_color2.setBackgroundColor(color2);
        setListenerByColor(which);
    }

    //弹出窗事件
    private void setListenerByColor(final int which) {
        //红色
        progress_red1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                tv_red_data1.setText(Integer.toString(progress));
                int nGreen = Integer.valueOf(tv_green_data1.getText().toString());
                int nBlue = Integer.valueOf(tv_blue_data1.getText().toString());
                tv_group_title1.setTextColor(Color.rgb(progress, nGreen, nBlue));
                tv_show_background_color1.setBackgroundColor(Color.rgb(progress, nGreen, nBlue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "停止滑动！");
            }
        });
        //绿色
        progress_green1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                tv_green_data1.setText(Integer.toString(progress));
                int nRed = Integer.valueOf(tv_red_data1.getText().toString());
                int nBlue = Integer.valueOf(tv_blue_data1.getText().toString());
                tv_group_title1.setTextColor(Color.rgb(nRed, progress, nBlue));
                tv_show_background_color1.setBackgroundColor(Color.rgb(nRed, progress, nBlue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "停止滑动！");
            }
        });
        //蓝色
        progress_blue1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                tv_blue_data1.setText(Integer.toString(progress));
                int nRed = Integer.valueOf(tv_red_data1.getText().toString());
                int nGreen = Integer.valueOf(tv_green_data1.getText().toString());
                tv_group_title1.setTextColor(Color.rgb(nRed, progress, nGreen));
                tv_show_background_color1.setBackgroundColor(Color.rgb(nRed, progress, nGreen));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "停止滑动！");
            }
        });
        //红色
        progress_red2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                tv_red_data2.setText(Integer.toString(progress));
                int nGreen = Integer.valueOf(tv_green_data2.getText().toString());
                int nBlue = Integer.valueOf(tv_blue_data2.getText().toString());
                tv_group_title2.setTextColor(Color.rgb(progress, nGreen, nBlue));
                tv_show_background_color2.setBackgroundColor(Color.rgb(progress, nGreen, nBlue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "停止滑动！");
            }
        });
        //绿色
        progress_green2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                tv_green_data2.setText(Integer.toString(progress));
                int nRed = Integer.valueOf(tv_red_data2.getText().toString());
                int nBlue = Integer.valueOf(tv_blue_data2.getText().toString());
                tv_group_title2.setTextColor(Color.rgb(nRed, progress, nBlue));
                tv_show_background_color2.setBackgroundColor(Color.rgb(nRed, progress, nBlue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "停止滑动！");
            }
        });
        //蓝色
        progress_blue2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                tv_blue_data2.setText(Integer.toString(progress));
                int nRed = Integer.valueOf(tv_red_data2.getText().toString());
                int nGreen = Integer.valueOf(tv_green_data2.getText().toString());
                tv_group_title2.setTextColor(Color.rgb(nRed, nGreen, progress));
                tv_show_background_color2.setBackgroundColor(Color.rgb(nRed, nGreen, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.i("Topcee", "停止滑动！");
            }
        });
        //确定
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sProgressRed1 = tv_red_data1.getText().toString().trim();
                int nProgressRed1 = Integer.parseInt(sProgressRed1);

                String sProgressGreen1 = tv_green_data1.getText().toString().trim();
                int nProgressGreen1 = Integer.parseInt(sProgressGreen1);

                String sProgressBlue1 = tv_blue_data1.getText().toString().trim();
                int nProgressBlue1 = Integer.parseInt(sProgressBlue1);

                String sProgressRed2 = tv_red_data2.getText().toString().trim();
                int nProgressRed2 = Integer.parseInt(sProgressRed2);

                String sProgressGreen2 = tv_green_data2.getText().toString().trim();
                int nProgressGreen2 = Integer.parseInt(sProgressGreen2);

                String sProgressBlue2 = tv_blue_data2.getText().toString().trim();
                int nProgressBlue2 = Integer.parseInt(sProgressBlue2);

                int color = Color.rgb(nProgressRed1, nProgressGreen1, nProgressBlue1);
                int color2 = Color.rgb(nProgressRed2, nProgressGreen2, nProgressBlue2);
                switch (which) {
                    case 0:
                        showGroup2(View.GONE);
                        SharedPreferencesUtil.saveValue(getBaseContext(), "nSMTTotalTitleColor", color);
                        break;
                    case 1:
                        showGroup2(View.GONE);
                        SharedPreferencesUtil.saveValue(getBaseContext(), "nSMTTotalOtherTextColor", color);
                        break;
                    case 2:
                        showGroup2(View.GONE);
                        SharedPreferencesUtil.saveValue(getBaseContext(), "nSMTTotalWholeBgColor", color);
                        break;
                    case 3:
                        showGroup2(View.GONE);
                        SharedPreferencesUtil.saveValue(getBaseContext(), "nSMTTotalChartBgColor", color);
                        break;
                    case 4:
                        showGroup2(View.VISIBLE);
                        tv_group_title1.setText(R.string.smt_line_input_qty);
                        tv_group_title2.setText(R.string.smt_line_output_qty);
                        SharedPreferencesUtil.saveValue(getBaseContext(), "nChartLineProductColor1", color);
                        SharedPreferencesUtil.saveValue(getBaseContext(), "nChartLineProductColor2", color2);
                        break;
                }
                setBackgroundDialog.dismiss();
                setBackgroundDialog = null;
            }
        });

        //取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundDialog.dismiss();
                setBackgroundDialog = null;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
