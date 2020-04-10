package com.mobile.android.medikamentmodule;

/*import model.entities.ProducerCompany;
import model.entities.SwissIndexResult;
import model.entities.TradeItem;
import webservice.erp.Company;
import webservice.swissindex.*;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;*/

public class SwissIndexClient {

   /* public static SwissIndexResult getItemInformationFromGTIN(String gtin) {

        Ws_Pharma_V101Locator locator = new Ws_Pharma_V101Locator();
        Ws_Pharma_V101Soap_PortType service = null;
        SwissIndexResult response = new SwissIndexResult();
        List<TradeItem> tradeItemList = null;
        try {
            service = locator.getws_Pharma_V101Soap();
            PHARMA de = service.getByGTIN(gtin, "de");
            PHARMARESULT result = de.getRESULT();

            if(result.getNBR_RECORD()==1){
                if (de.getITEM() != null) {
                    tradeItemList = new ArrayList<TradeItem>();
                    for (PHARMAITEM pharmaitem : de.getITEM()) {
                        if (pharmaitem != null) {
                            PHARMAITEMCOMP comp = pharmaitem.getCOMP();
                            Company c = CompanyConstructor(comp.getNAME(),comp.getGLN(), "ABTEILUNG");
                            tradeItemList.add(ItemConstructor(pharmaitem.getDSCR(), pharmaitem.getADDSCR(),gtin, "BATCH/LOT", "SERIAL", "DESC", "Zusatz",
                                    pharmaitem.getATC(), c, pharmaitem.getPHAR()));
                        } else {
                            response.setResult(false);
                            return response;
                        }
                    }
                    response.setResult(true);
                    response.setTradeItems(tradeItemList);
                    response.setMessage("OK");
                    return response;
                } else {
                    response.setResult(false);
                    return response;
                }
            }else{
                response.setResult(false);
                response.setMessage("Zur GTIN: " + gtin +" konnten keine Informationen auf SwissINDEX gefunden werden. \n");
                return response;
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            response.setResult(false);
            response.setMessage("Fatal Error: Service Exception");
            return response;
        } catch (RemoteException e) {
            e.printStackTrace();
            response.setResult(false);
            response.setMessage("Fatal Error: Remote Exception");
            return response;
        }
    }

    public static TradeItem ItemConstructor(String name, String menge, String gtin, String lot,
                                       String serial, String beschreibung, String zusatz, String atc, Company company, BigInteger pharmaCode) {
        TradeItem i = new TradeItem();
        i.setName(name);
        i.setATC(atc);
        i.setPharmaCode(pharmaCode);
        i.setBeschreibung(beschreibung);
        i.setGTIN(gtin);
        i.setLot(lot);
        i.setMenge(menge);
        i.setProducer(company);
        i.setSerial(serial);
        i.setZusatz(zusatz);


        return i;
    }

    public static ProducerCompany CompanyConstructor(String name, String gln, String abteilung) {
        ProducerCompany c = new ProducerCompany();
        c.setName(name);
        c.setGLN(gln);
        c.setAbteilung(abteilung);

        return c;
    }*/
}
