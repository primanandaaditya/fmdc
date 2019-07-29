package id.co.cp.mdc.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostUserList {

    public PostUserList(String fcrud, String zfunc, String ztsid, String clien) {
        this.fcrud = fcrud;
        this.zfunc = zfunc;
        this.ztsid = ztsid;
        this.clien = clien;
    }

    @SerializedName("fcrud")
    @Expose
    private String fcrud;
    @SerializedName("zfunc")
    @Expose
    private String zfunc;
    @SerializedName("ztsid")
    @Expose
    private String ztsid;
    @SerializedName("clien")
    @Expose
    private String clien;

    public String getFcrud() {
        return fcrud;
    }

    public void setFcrud(String fcrud) {
        this.fcrud = fcrud;
    }

    public String getZfunc() {
        return zfunc;
    }

    public void setZfunc(String zfunc) {
        this.zfunc = zfunc;
    }

    public String getZtsid() {
        return ztsid;
    }

    public void setZtsid(String ztsid) {
        this.ztsid = ztsid;
    }

    public String getClien() {
        return clien;
    }

    public void setClien(String clien) {
        this.clien = clien;
    }

}
