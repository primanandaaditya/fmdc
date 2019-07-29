package id.co.cp.mdc.helper;

import java.text.DecimalFormat;

/**
 * Created by user on 23/11/2016.
 */
public class zGeneralFunction {
    public zGeneralFunction() {
    }

    public static String pemisah_ribuan(String angka){
        String hasil = "";
        Integer iAngka = Integer.parseInt(angka);
        hasil = String.format("%,d", iAngka).replace(',', '.');
        return  hasil;

    }

    public static String formatCurrent(String input){
        if(!input.matches("")) {
            float longval;
            longval = Float.parseFloat(input);
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String formattedString = formatter.format(longval);
            return formattedString;
        }
        else{
            return "0";
        }
    }
    public static String formatCurrentDecimal(String input){
        if(!input.matches("")) {
            float longval;
            longval = Float.parseFloat(input);
            DecimalFormat formatter = new DecimalFormat("#,###,###.##");
            String formattedString = formatter.format(longval);
            return formattedString;
        }
        else{
            return "0";
        }
    }
}
