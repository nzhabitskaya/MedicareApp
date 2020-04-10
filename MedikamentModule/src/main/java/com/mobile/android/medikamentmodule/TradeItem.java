package com.mobile.android.medikamentmodule;

//import webservice.erp.Item;

import java.math.BigInteger;

public class TradeItem /*extends Item*/ {

    public BigInteger getPharmaCode() {
        return pharmaCode;
    }

    public void setPharmaCode(BigInteger pharmaCode) {
        this.pharmaCode = pharmaCode;
    }

    private BigInteger pharmaCode;

    public TradeItem() {
        super();
    }



    /*@Override
    public String toString() {
        return  "Name: " + getName() + '\n' +
                "Menge: " + getMenge() + '\n' +
                "GTIN: " + getGTIN() + '\n' +
                "Lot: " + getLot() + '\n' +
                "Serial: " + getSerial() + '\n' +
                "Beschreibung: " + getBeschreibung() + '\n' +
                "Zusatz='" + getZusatz() + '\n' +
                "ATC: " + getATC() + '\n' +
                "Hersteller: " + producer;
    }*/
}
