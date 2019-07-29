package id.co.cp.mdc.interfaces;

import id.co.cp.mdc.model.ListFarmModel;
import id.co.cp.mdc.model.ChartModel;
import id.co.cp.mdc.model.ChartModel.Chart;
import id.co.cp.mdc.model.ChartModel.Datum;

public interface ChartTagInterface {

    void onChartError(String error);
    void onChartSuccess(Chart chart);
}
