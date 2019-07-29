package id.co.cp.mdc.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CobaModel {

    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("flag")
    @Expose
    private Boolean flag;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

}
