package id.co.cp.mdc.helper;

/**
 * Created by user on 31/10/2016.
 */
public class UserList {
    private String thumbnailUrl;
    private String username;
    private String tag;
    private String time;
    private String runnm;
    private String ztsid;

    public String getZtsid() {
        return ztsid;
    }

    public void setZtsid(String ztsid) {
        this.ztsid = ztsid;
    }

    public String getRunnm() {
        return runnm;
    }

    public void setRunnm(String runnm) {
        this.runnm = runnm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserList() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public UserList(String username, String thumbnailUrl, String tag) {
        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
        this.tag = tag;

    }


    public String getProfile() {
        return thumbnailUrl;
    }

    public void setProfile(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsrname(String username) {
        this.username = username;
    }
}
