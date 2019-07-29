package id.co.cp.mdc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Comparator;

import java.util.List;


public class ListFarmModel{


    public class Datum {

        @SerializedName("longi")
        @Expose
        private String longi;
        @SerializedName("latit")
        @Expose
        private String latit;
        @SerializedName("mapcd")
        @Expose
        private String mapcd;
        @SerializedName("famnm")
        @Expose
        private String famnm;
        @SerializedName("arenm")
        @Expose
        private String arenm;
        @SerializedName("brcnm")
        @Expose
        private String brcnm;
        @SerializedName("fmadr")
        @Expose
        private String fmadr;
        @SerializedName("tsnme")
        @Expose
        private String tsnme;

        public String getLongi() {
            return longi;
        }

        public void setLongi(String longi) {
            this.longi = longi;
        }

        public String getLatit() {
            return latit;
        }

        public void setLatit(String latit) {
            this.latit = latit;
        }

        public String getMapcd() {
            return mapcd;
        }

        public void setMapcd(String mapcd) {
            this.mapcd = mapcd;
        }

        public String getFamnm() {
            return famnm;
        }

        public void setFamnm(String famnm) {
            this.famnm = famnm;
        }

        public String getArenm() {
            return arenm;
        }

        public void setArenm(String arenm) {
            this.arenm = arenm;
        }

        public String getBrcnm() {
            return brcnm;
        }

        public void setBrcnm(String brcnm) {
            this.brcnm = brcnm;
        }

        public String getFmadr() {
            return fmadr;
        }

        public void setFmadr(String fmadr) {
            this.fmadr = fmadr;
        }

        public String getTsnme() {
            return tsnme;
        }

        public void setTsnme(String tsnme) {
            this.tsnme = tsnme;
        }






    }

    public static Comparator<Datum> datumComparator = new Comparator<Datum>() {

        public int compare(Datum s1, Datum s2) {
            String dt1 = s1.getFamnm().toUpperCase();
            String dt2 = s2.getFamnm().toUpperCase();

            //ascending order
            return dt1.compareTo(dt2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };




    public class Model {

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
