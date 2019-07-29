package id.co.cp.mdc.model.approvecount;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApproveCountModel {

    @SerializedName("sysrc")
    @Expose
    private Sysrc sysrc;
    @SerializedName("zlist")
    @Expose
    private List<Zlist> zlist = null;

    public Sysrc getSysrc() {
        return sysrc;
    }

    public void setSysrc(Sysrc sysrc) {
        this.sysrc = sysrc;
    }

    public List<Zlist> getZlist() {
        return zlist;
    }

    public void setZlist(List<Zlist> zlist) {
        this.zlist = zlist;
    }

}
