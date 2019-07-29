package id.co.cp.mdc.model.approve;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class P4Model {

    @SerializedName("sysrc")
    @Expose
    private Sysrc sysrc;
    @SerializedName("zlist")
    @Expose
    private List<ZList> zList = null;

    public Sysrc getSysrc() {
        return sysrc;
    }

    public void setSysrc(Sysrc sysrc) {
        this.sysrc = sysrc;
    }

    public List<ZList> getZList() {
        return zList;
    }

    public void setZList(List<ZList> zList) {
        this.zList = zList;
    }

}