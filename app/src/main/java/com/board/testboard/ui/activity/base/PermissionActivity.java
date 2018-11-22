package com.board.testboard.ui.activity.base;

import android.os.Bundle;


/**  
 * 权限设置
 */
public abstract class PermissionActivity extends BaseActivity {

    private final String TAG = PermissionActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }
    protected abstract int getLayoutId();

}
