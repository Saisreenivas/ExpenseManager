package model;

/**
 * Created by Sai sreenivas on 1/1/2017.
 */

public class MessageEach {

    private String mAddress;
    private String mBody;
    private String mName;
    private String mDate;
    private String mMoney;

    public MessageEach() {
    }

    public MessageEach(String Address, String Body, String Name, String Date, String Money) {
        this.mAddress = Address;
        this.mBody = Body;
        this.mName = Name;
        this.mDate = Date;
        this.mMoney = Money;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmBody() {
        return mBody;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }

    public String getmMoney() {
        return mMoney;
    }

    public void setmMoney(String mMoney) {
        this.mMoney = mMoney;
    }
}
