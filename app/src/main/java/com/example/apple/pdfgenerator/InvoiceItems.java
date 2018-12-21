package com.example.apple.pdfgenerator;

public class InvoiceItems {

    private String sno,particulars,quantity,amount;

    InvoiceItems(String sno,String particulars,String quantity, String amount){
        this.sno = sno;
        this.particulars = particulars;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public String getParticulars() {
        return particulars;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSno() {
        return sno;
    }
}

