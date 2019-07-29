package id.co.cp.mdc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApprModel {

    @SerializedName("farmer_name")
    @Expose
    private String farmerName;
    @SerializedName("p4_mortality")
    @Expose
    private Double p4Mortality;
    @SerializedName("p4_feed_used")
    @Expose
    private Double p4FeedUsed;
    @SerializedName("lpab_mortality")
    @Expose
    private Double lpabMortality;
    @SerializedName("lpab_feed_used")
    @Expose
    private Double lpabFeedUsed;
    @SerializedName("adj_mortality")
    @Expose
    private Double adjMortality;
    @SerializedName("adj_feed_used")
    @Expose
    private Double adjFeedUsed;
    @SerializedName("switch")
    @Expose
    private Boolean _switch;

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Double getP4Mortality() {
        return p4Mortality;
    }

    public void setP4Mortality(Double p4Mortality) {
        this.p4Mortality = p4Mortality;
    }

    public Double getP4FeedUsed() {
        return p4FeedUsed;
    }

    public void setP4FeedUsed(Double p4FeedUsed) {
        this.p4FeedUsed = p4FeedUsed;
    }

    public Double getLpabMortality() {
        return lpabMortality;
    }

    public void setLpabMortality(Double lpabMortality) {
        this.lpabMortality = lpabMortality;
    }

    public Double getLpabFeedUsed() {
        return lpabFeedUsed;
    }

    public void setLpabFeedUsed(Double lpabFeedUsed) {
        this.lpabFeedUsed = lpabFeedUsed;
    }

    public Double getAdjMortality() {
        return adjMortality;
    }

    public void setAdjMortality(Double adjMortality) {
        this.adjMortality = adjMortality;
    }

    public Double getAdjFeedUsed() {
        return adjFeedUsed;
    }

    public void setAdjFeedUsed(Double adjFeedUsed) {
        this.adjFeedUsed = adjFeedUsed;
    }

    public Boolean getSwitch() {
        return _switch;
    }

    public void setSwitch(Boolean _switch) {
        this._switch = _switch;
    }

}