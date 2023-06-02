package SessionPackage;

import android.os.Bundle;

import java.util.ArrayList;

public class ReceiptSession {
    String tin, amount, number, payee, bcode, ornum, date;
    //ArrayList<String> tinList, amountList, numberList, payeeList, bcodeList, ornumList, dateList;

    public ReceiptSession(String tin, String amount, String number, String payee, String bcode,
                          String ornum, String date) {
        this.tin = tin;
        this.amount = amount;
        this.number = number;
        this.payee = payee;
        this.bcode = bcode;
        this.ornum = ornum;
        this.date = date;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getBcode() {
        return bcode;
    }

    public void setBcode(String bcode) {
        this.bcode = bcode;
    }

    public String getOrnum() {
        return ornum;
    }

    public void setOrnum(String ornum) {
        this.ornum = ornum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
