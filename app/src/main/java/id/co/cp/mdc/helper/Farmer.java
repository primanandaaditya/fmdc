package id.co.cp.mdc.helper;

/**
 * Created by user on 02/08/2016.
 */

//wajib dibuat ketika mau buat sqlite (Setter, getter, dan construct)
public class Farmer {
    //atribut dalam farmer
    String _famcd;
    String _famnm;
    String _famad;
    String _longi;
    String _latit;

    public String get_longi() {
        return _longi;
    }

    public void set_longi(String _longi) {
        this._longi = _longi;
    }

    public String get_latit() {
        return _latit;
    }

    public void set_latit(String _latit) {
        this._latit = _latit;
    }

    //kalau kosong
    public Farmer(){
    }

    //kalau mau di set awal code dan name
    public Farmer(String _famcd, String _famnm, String _famad){
        this._famcd = _famcd;
        this._famnm = _famnm;
        this._famad = _famad;
    }

    //set farm code
    public void setFarmCode(String famcd){
        this._famcd=famcd;
    }

    //set farm name
    public void setFarmName(String famnm){
        this._famnm=famnm;
    }
    //set farm address
    public void setFarmAddress(String famad){
        this._famad=famad;
    }

    //get farm code
    public String getFarmCode(){
        return this._famcd;
    }

    //get farm name
    public String getFarmName(){
        return this._famnm;
    }
    //get farm address
    public String getFarmAddress(){
        return this._famad;
    }

}
