package com.board.testboard.bean;

/**
 * [线别产能]
 * Created by YANGT
 * 2018/4/16.
 */

public class LineNoProductionEntity {     
    private String sLineNo; //线别
    private String sDlQtyInput; //当班投入-投产数量
    private String sDlQtyOutput; //当班投入-实际产出

    public String getsLineNo() {
        return sLineNo;
    }

    public void setsLineNo(String sLineNo) {
        this.sLineNo = sLineNo;
    }

    public String getsDlQtyInput() {
        return sDlQtyInput;
    }

    public void setsDlQtyInput(String sDlQtyInput) {
        this.sDlQtyInput = sDlQtyInput;
    }

    public String getsDlQtyOutput() {
        return sDlQtyOutput;
    }

    public void setsDlQtyOutput(String sDlQtyOutput) {
        this.sDlQtyOutput = sDlQtyOutput;
    }
}
