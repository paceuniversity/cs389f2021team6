package com.example.onehomereset;

public class Upload {
    private String mName, mPhoneNum, mDescription;
    private String mImageUrl;

    public Upload(String trim, String s, String toString){/*empty constructor needed*/}
    public Upload(String name,String imageUrl,String PhoneNumber,String Description){
        mName = name;
        mPhoneNum= PhoneNumber;
        mDescription = Description;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getmImageUrl() { return mImageUrl; }

    public void setmImageUrl(String imageUrl) { mImageUrl = imageUrl; }

    public String getmPhoneNum() { return mPhoneNum; }

    public void setmPhoneNum(String PhoneNumber) { mPhoneNum = PhoneNumber; }

    public String getmDescription() { return mDescription; }

    public void setDescription(String Description) { mImageUrl = Description; }

}