package id.co.cp.mdc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZList {

    @SerializedName("cek")
    @Expose
    private Boolean cek;

    @SerializedName("clien")
    @Expose
    private String clien;
    @SerializedName("batch")
    @Expose
    private String batch;
    @SerializedName("zfmid")
    @Expose
    private String zfmid;
    @SerializedName("zdate")
    @Expose
    private String zdate;
    @SerializedName("ztime")
    @Expose
    private String ztime;
    @SerializedName("recdt")
    @Expose
    private String recdt;
    @SerializedName("feeduse_fmdc")
    @Expose
    private String feeduseFmdc;
    @SerializedName("morta_fmdc")
    @Expose
    private String mortaFmdc;
    @SerializedName("feeduse_pims")
    @Expose
    private String feedusePims;
    @SerializedName("morta_pims")
    @Expose
    private String mortaPims;
    @SerializedName("chkin")
    @Expose
    private String chkin;
    @SerializedName("aprdt")
    @Expose
    private Object aprdt;
    @SerializedName("aprtm")
    @Expose
    private Object aprtm;
    @SerializedName("aprby")
    @Expose
    private Object aprby;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("isneedapprove")
    @Expose
    private String isneedapprove;
    @SerializedName("selisih_feed")
    @Expose
    private String selisihFeed;
    @SerializedName("selisih_morta")
    @Expose
    private String selisihMorta;
    @SerializedName("adj_feed")
    @Expose
    private String adjFeed;
    @SerializedName("adj_mort")
    @Expose
    private String adjMort;
    @SerializedName("%feeduse")
    @Expose
    private String feeduse;
    @SerializedName("%morta")
    @Expose
    private String morta;

    public Boolean getCek(){
        return cek;
    }

    public void setCek(Boolean cek){
        this.cek=cek;
    }

    public String getClien() {
        return clien;
    }

    public void setClien(String clien) {
        this.clien = clien;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getZfmid() {
        return zfmid;
    }

    public void setZfmid(String zfmid) {
        this.zfmid = zfmid;
    }

    public String getZdate() {
        return zdate;
    }

    public void setZdate(String zdate) {
        this.zdate = zdate;
    }

    public String getZtime() {
        return ztime;
    }

    public void setZtime(String ztime) {
        this.ztime = ztime;
    }

    public String getRecdt() {
        return recdt;
    }

    public void setRecdt(String recdt) {
        this.recdt = recdt;
    }

    public String getFeeduseFmdc() {
        return feeduseFmdc;
    }

    public void setFeeduseFmdc(String feeduseFmdc) {
        this.feeduseFmdc = feeduseFmdc;
    }

    public String getMortaFmdc() {
        return mortaFmdc;
    }

    public void setMortaFmdc(String mortaFmdc) {
        this.mortaFmdc = mortaFmdc;
    }

    public String getFeedusePims() {
        return feedusePims;
    }

    public void setFeedusePims(String feedusePims) {
        this.feedusePims = feedusePims;
    }

    public String getMortaPims() {
        return mortaPims;
    }

    public void setMortaPims(String mortaPims) {
        this.mortaPims = mortaPims;
    }

    public String getChkin() {
        return chkin;
    }

    public void setChkin(String chkin) {
        this.chkin = chkin;
    }

    public Object getAprdt() {
        return aprdt;
    }

    public void setAprdt(Object aprdt) {
        this.aprdt = aprdt;
    }

    public Object getAprtm() {
        return aprtm;
    }

    public void setAprtm(Object aprtm) {
        this.aprtm = aprtm;
    }

    public Object getAprby() {
        return aprby;
    }

    public void setAprby(Object aprby) {
        this.aprby = aprby;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getIsneedapprove() {
        return isneedapprove;
    }

    public void setIsneedapprove(String isneedapprove) {
        this.isneedapprove = isneedapprove;
    }

    public String getSelisihFeed() {
        return selisihFeed;
    }

    public void setSelisihFeed(String selisihFeed) {
        this.selisihFeed = selisihFeed;
    }

    public String getSelisihMorta() {
        return selisihMorta;
    }

    public void setSelisihMorta(String selisihMorta) {
        this.selisihMorta = selisihMorta;
    }

    public String getAdjFeed() {
        return adjFeed;
    }

    public void setAdjFeed(String adjFeed) {
        this.adjFeed = adjFeed;
    }

    public String getAdjMort() {
        return adjMort;
    }

    public void setAdjMort(String adjMort) {
        this.adjMort = adjMort;
    }

    public String getFeeduse() {
        return feeduse;
    }

    public void setFeeduse(String feeduse) {
        this.feeduse = feeduse;
    }

    public String getMorta() {
        return morta;
    }

    public void setMorta(String morta) {
        this.morta = morta;
    }

}
