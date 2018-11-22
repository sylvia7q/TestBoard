package com.board.testboard.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 线别展示时长
 */
@Table(name = "LINE_DISPLAY_TIME")
public class LINE_DISPLAY_TIME {

    @Id(column = "LINE_NO")
    private String LINE_NO;

    @Column(column = "DISPLAY_TIME")
    private String DISPLAY_TIME;

    @Column(column = "DISPLAY_TIME_SELECTED")
    private String DISPLAY_TIME_SELECTED;

    public String getLINE_NO() {
        return LINE_NO;
    }

    public void setLINE_NO(String LINE_NO) {
        this.LINE_NO = LINE_NO;
    }

    public String getDISPLAY_TIME() {
        return DISPLAY_TIME;
    }

    public void setDISPLAY_TIME(String DISPLAY_TIME) {
        this.DISPLAY_TIME = DISPLAY_TIME;
    }

    public String getDISPLAY_TIME_SELECTED() {
        return DISPLAY_TIME_SELECTED;
    }

    public void setDISPLAY_TIME_SELECTED(String DISPLAY_TIME_SELECTED) {
        this.DISPLAY_TIME_SELECTED = DISPLAY_TIME_SELECTED;
    }

    @Override
    public String toString() {
        return "LINE_DISPLAY_TIME{" +
                "LINE_NO='" + LINE_NO + '\'' +
                ", DISPLAY_TIME='" + DISPLAY_TIME + '\'' +
                ", DISPLAY_TIME_SELECTED='" + DISPLAY_TIME_SELECTED + '\'' +
                '}';
    }
}
