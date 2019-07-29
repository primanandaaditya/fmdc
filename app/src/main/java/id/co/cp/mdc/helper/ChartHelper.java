package id.co.cp.mdc.helper;

import android.graphics.Color;
import android.os.Build;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChartHelper {

    public  static void PieChartFormat(PieChart pieChart, final HashMap<Integer, String> SumbuX, List<Float> SumbuY, String label){

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { pieChart.setHardwareAccelerationEnabled(false); }

        List<PieEntry> entries1 = new ArrayList<PieEntry>();
        int awal,akhir;
        awal= 1 ;
        akhir=SumbuX.size();

        for(int num =awal; num <= akhir; num++){
            entries1.add(new PieEntry( SumbuY.get(num-1), SumbuX.get(num) ));
        }

        PieDataSet pieDataSet = new PieDataSet(entries1, label);
        PieData data = new PieData(pieDataSet);

        if (SumbuX.size()==2){
            final int[] MY_COLORS = {Color.rgb(255, 153, 102), Color.rgb(51, 204, 255)};
            ArrayList<Integer> colors = new ArrayList<Integer>();

            for(int c: MY_COLORS) colors.add(c);
            pieDataSet.setColors(colors);

        }else{
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        }
        //pieChart.
        //pieChart.setTransparentCircleColor(Color.rgb(255,153,102));
        pieChart.setData(data);
        pieChart.invalidate();
    }

}
