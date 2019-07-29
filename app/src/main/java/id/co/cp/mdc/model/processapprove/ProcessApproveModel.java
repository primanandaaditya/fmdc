package id.co.cp.mdc.model.processapprove;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcessApproveModel {

    @SerializedName("sysrc")
    @Expose
    private Sysrc sysrc;

    public Sysrc getSysrc() {
        return sysrc;
    }

    public void setSysrc(Sysrc sysrc) {
        this.sysrc = sysrc;
    }

}
