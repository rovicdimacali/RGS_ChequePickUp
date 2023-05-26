package SessionPackage;

public class ReceiptSession {
    String tin, amount, number, payee;

    public ReceiptSession(String tin, String amount, String number, String payee) {
        this.tin = tin;
        this.amount = amount;
        this.number = number;
        this.payee = payee;
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

    public void setPayee(String number) {
        this.payee = payee;
    }
}
