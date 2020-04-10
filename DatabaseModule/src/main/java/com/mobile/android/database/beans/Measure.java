package com.mobile.android.database.beans;

public class Measure {

    private long id;
    private String accountId;
    private String accountName;
    private String timestamp;
    private String weight;
    private String comment;
    private String type;

    public Measure(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getWeight() {
        return parseWeightData(weight);
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String parseWeightData(String weightStr){
        float weightFloat = 0;
        float weightVal = (weightStr.length() > 0) ? Float.parseFloat(weightStr) : 0;
        if(weightStr.startsWith("1")){
            switch(weightStr.length()){
                case 5:
                    weightFloat = weightVal / 1000f;
                    break;
                case 4:
                    weightFloat = weightVal / 100f;
                    break;
                case 3:
                    weightFloat = weightVal / 10f;
                    break;
            }
        } else {
            switch(weightStr.length()){
                case 5:
                    weightFloat = weightVal / 10000f;
                    break;
                case 4:
                    weightFloat = weightVal / 1000f;
                    break;
                case 3:
                    weightFloat = weightVal / 100f;
                    break;
                case 2:
                    weightFloat = weightVal / 10f;
                    break;
            }
        }
        return String.valueOf(weightFloat);
    }
}
