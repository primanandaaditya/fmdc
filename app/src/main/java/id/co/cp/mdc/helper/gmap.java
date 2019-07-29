package id.co.cp.mdc.helper;

/**
 * Created by user on 03/08/2016.
 */
public class gmap {
    //atribut dalam farmer
    String _mapcd;
    String _longi;
    String _latit;

    //kalau kosong
    public gmap(){
    }
//
//    //kalau mau di set awal code dan name
//    public gmap(String _famcd, String _famnm, String _famad){
//        this._famcd = _famcd;
//        this._famnm = _famnm;
//        this._famad = _famad;
//    }

    //set mapcd code
    public void setMapCode(String mapcd)
    {
        this._mapcd=mapcd;
    }
    //set longi
    public void setLongi(String longi)
    {
        this._longi=longi;
    }
    //set latit
    public void setLatit(String latit)
    {
        this._latit=latit;
    }

    //get map code
    public String getMapCode()
    {
        return this._mapcd;
    }
    //get longi
    public String getLongi()
    {
        return this._longi;
    }
    //get latit
    public String getLatit()
    {
        return this._latit;
    }

}
