package com.example.onehomereset;

public class HUIhelper {



    String mtitle, mfeedback;
    public HUIhelper() {
    }



    public HUIhelper(String title, String feedback) {
        this.mtitle = title;
        this.mfeedback = feedback;
    }

    public String getTitle() {
        return mtitle;
    }

    public void setTitle(String title) {
        this.mtitle = title;
    }

    public String getMfeedback() {
        return mfeedback;
    }

    public void setMfeedback(String mfeedback) {
        this.mfeedback = mfeedback;
    }


}