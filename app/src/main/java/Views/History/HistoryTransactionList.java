package Views.History;

public class HistoryTransactionList {

    public String nameSender;
    public String nameReceiver;
    public String numberSender;
    public String numberReceiver;
    public String moneyHTL;
    public String date;

    public HistoryTransactionList(String numberSender, String nameSender, String numberReceiver, String nameReceiver, String moneyHTL, String date) {
        this.nameSender = nameSender;
        this.nameReceiver = nameReceiver;
        this.numberSender = numberSender;
        this.numberReceiver = numberReceiver;
        this.moneyHTL = moneyHTL;
        this.date = date;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getMoneyHTL() {
        return moneyHTL;
    }

    public void setMoneyHTL(String moneyHTL) {
        this.moneyHTL = moneyHTL;
    }

    public String getNameReceiver() {
        return nameReceiver;
    }

    public void setNameReceiver(String nameReceiver) {
        this.nameReceiver = nameReceiver;
    }

    public String getNumberSender() {
        return numberSender;
    }

    public void setNumberSender(String numberSender) {
        this.numberSender = numberSender;
    }

    public String getNumberReceiver() {
        return numberReceiver;
    }

    public void setNumberReceiver(String numberReceiver) {
        this.numberReceiver = numberReceiver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
