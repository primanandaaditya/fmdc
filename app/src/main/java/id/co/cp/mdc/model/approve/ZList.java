package id.co.cp.mdc.model.approve;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZList implements Parcelable {

    private int noUrut;
    private String p4_date;
    private String p4_no;
    private String famnm;

    public String getFamnm() {
        return famnm;
    }

    public void setFamnm(String famnm) {
        this.famnm = famnm;
    }

    public String getP4_date() {
        return p4_date;
    }

    public void setP4_date(String p4_date) {
        this.p4_date = p4_date;
    }

    public String getP4_no() {
        return p4_no;
    }

    public void setP4_no(String p4_no) {
        this.p4_no = p4_no;
    }

    public int getNoUrut() {
        return noUrut;
    }

    public void setNoUrut(int noUrut) {
        this.noUrut = noUrut;
    }

    @SerializedName("cek")
    @Expose
    private Boolean cek = false;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.noUrut);
        dest.writeString(this.p4_date);
        dest.writeString(this.p4_no);
        dest.writeString(this.famnm);
        dest.writeValue(this.cek);
        dest.writeString(this.clien);
        dest.writeString(this.batch);
        dest.writeString(this.zfmid);
        dest.writeString(this.zdate);
        dest.writeString(this.ztime);
        dest.writeString(this.recdt);
        dest.writeString(this.feeduseFmdc);
        dest.writeString(this.mortaFmdc);
        dest.writeString(this.feedusePims);
        dest.writeString(this.mortaPims);
        dest.writeString(this.chkin);

        dest.writeString(this.isneedapprove);
        dest.writeString(this.selisihFeed);
        dest.writeString(this.selisihMorta);
        dest.writeString(this.adjFeed);
        dest.writeString(this.adjMort);
        dest.writeString(this.feeduse);
        dest.writeString(this.morta);
    }

    public ZList() {
    }

    protected ZList(Parcel in) {
        this.noUrut = in.readInt();
        this.p4_date = in.readString();
        this.p4_no = in.readString();
        this.famnm = in.readString();
        this.cek = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.clien = in.readString();
        this.batch = in.readString();
        this.zfmid = in.readString();
        this.zdate = in.readString();
        this.ztime = in.readString();
        this.recdt = in.readString();
        this.feeduseFmdc = in.readString();
        this.mortaFmdc = in.readString();
        this.feedusePims = in.readString();
        this.mortaPims = in.readString();
        this.chkin = in.readString();
        this.isneedapprove = in.readString();
        this.selisihFeed = in.readString();
        this.selisihMorta = in.readString();
        this.adjFeed = in.readString();
        this.adjMort = in.readString();
        this.feeduse = in.readString();
        this.morta = in.readString();
    }

    public static final Parcelable.Creator<ZList> CREATOR = new Parcelable.Creator<ZList>() {
        @Override
        public ZList createFromParcel(Parcel source) {
            return new ZList(source);
        }

        @Override
        public ZList[] newArray(int size) {
            return new ZList[size];
        }
    };
}
