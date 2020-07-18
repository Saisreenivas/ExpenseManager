package model;

/**
 * Created by Sai sreenivas on 12/30/2016.
 */

// provides data to SQL after taking it from the activity
public class ExpenseDone {

    private int _id;
    private String _month;
    private String _name;
    private int _amount;
    private boolean isSelected;

    private String _date;
    private String _count;

    public ExpenseDone(){

    }

    public ExpenseDone(String date, String count){
        this._date = date;
        this._count = count;
    }

    public ExpenseDone(String month) {
        this._month = month;
    }

    public ExpenseDone(String month, String name, int amount, String date) {
        this._month = month;
        this._name = name;
        this._amount = amount;
        this._date = date;
    }

    public String get_month() {
        return _month;
    }

    public void set_month(String _month) {
        this._month = _month;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_amount() {
        return _amount;
    }

    public void set_amount(int _amount) {
        this._amount = _amount;
    }

    public int get_id() {return _id;}

    public void set_id(int _id) {this._id = _id;}

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_count() {
        return _count;
    }

    public void set_count(String _count) {
        this._count = _count;
    }

    public boolean isSelected(){return isSelected;}

    public void setSelected(boolean isSelected){this.isSelected = isSelected;}
}
