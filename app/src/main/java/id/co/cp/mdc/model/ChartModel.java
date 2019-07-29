package id.co.cp.mdc.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChartModel {


    public class Chart {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

    }

    public class Datum {

        @SerializedName("mytag")
        @Expose
        private String mytag;
        @SerializedName("remain")
        @Expose
        private Integer remain;

        public String getMytag() {
            return mytag;
        }

        public void setMytag(String mytag) {
            this.mytag = mytag;
        }

        public Integer getRemain() {
            return remain;
        }

        public void setRemain(Integer remain) {
            this.remain = remain;
        }

    }



}
