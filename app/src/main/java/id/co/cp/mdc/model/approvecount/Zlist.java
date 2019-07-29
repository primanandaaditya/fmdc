package id.co.cp.mdc.model.approvecount;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Zlist {

    @SerializedName("nama_TS")
    @Expose
    private String namaTS;
    @SerializedName("jmlappr")
    @Expose
    private String jmlappr;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("ztsid")
    @Expose
    private String ztsid;
    @SerializedName("emailts")
    @Expose
    private String emailts;

    public String getNamaTS() {
        return namaTS;
    }

    public void setNamaTS(String namaTS) {
        this.namaTS = namaTS;
    }

    public String getJmlappr() {
        return jmlappr;
    }

    public void setJmlappr(String jmlappr) {
        this.jmlappr = jmlappr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getZtsid() {
        return ztsid;
    }

    public void setZtsid(String ztsid) {
        this.ztsid = ztsid;
    }

    public String getEmailts() {
        return emailts;
    }

    public void setEmailts(String emailts) {
        this.emailts = emailts;
    }

}