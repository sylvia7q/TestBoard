package com.board.testboard.bean;

/**
 * 线别产出
 */
public class LineNoProductionBean {  

    private String sLineNo;
    private String sdlQtyInput;
    private String sdlQtyOutput;

    public String getsLineNo() {
        return sLineNo;
    }

    public void setsLineNo(String sLineNo) {
        this.sLineNo = sLineNo;
    }

    public String getSdlQtyInput() {
        return sdlQtyInput;
    }

    public void setSdlQtyInput(String sdlQtyInput) {
        this.sdlQtyInput = sdlQtyInput;
    }

    public String getSdlQtyOutput() {
        return sdlQtyOutput;
    }

    public void setSdlQtyOutput(String sdlQtyOutput) {
        this.sdlQtyOutput = sdlQtyOutput;
    }

    @Override
    public String toString() {
        return "LineNoProductionBean{" +
                "sLineNo='" + sLineNo + '\'' +
                ", sdlQtyInput='" + sdlQtyInput + '\'' +
                ", sdlQtyOutput='" + sdlQtyOutput + '\'' +
                '}';
    }
}
