package SessionPackage;

public class ReceiptSession {
    final String tin, amount, number, payee, bcode, ornum, date;
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

    public String getAmount() {
        return amount;
    }

    public String getNumber() {
        return number;
    }

    public String getPayee() {
        return payee;
    }


    public String getBcode() {
        return bcode;
    }


    public String getOrnum() {
        return ornum;
    }


    public String getDate() {
        return date;
    }

}
