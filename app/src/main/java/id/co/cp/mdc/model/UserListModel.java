package id.co.cp.mdc.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserListModel {

    public class Datum {

        @SerializedName("ltdte")
        @Expose
        private String ltdte;
        @SerializedName("lttme")
        @Expose
        private String lttme;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("ztsid")
        @Expose
        private String ztsid;
        @SerializedName("zuser")
        @Expose
        private String zuser;
        @SerializedName("remain")
        @Expose
        private String remain;
        @SerializedName("mytag")
        @Expose
        private String mytag;

        public String getLtdte() {
            return ltdte;
        }

        public void setLtdte(String ltdte) {
            this.ltdte = ltdte;
        }

        public String getLttme() {
            return lttme;
        }

        public void setLttme(String lttme) {
            this.lttme = lttme;
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

        public String getZuser() {
            return zuser;
        }

        public void setZuser(String zuser) {
            this.zuser = zuser;
        }

        public String getRemain() {
            return remain;
        }

        public void setRemain(String remain) {
            this.remain = remain;
        }

        public String getMytag() {
            return mytag;
        }

        public void setMytag(String mytag) {
            this.mytag = mytag;
        }

    }


    public class UserList {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("message")
        @Expose
        private Object message;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

    }


}
