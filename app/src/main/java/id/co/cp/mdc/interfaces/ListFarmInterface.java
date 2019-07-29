package id.co.cp.mdc.interfaces;

import id.co.cp.mdc.model.ListFarmModel;
import id.co.cp.mdc.model.ListFarmModel.Datum;
import id.co.cp.mdc.model.ListFarmModel.Model;

public interface ListFarmInterface {
    void onError(String error);
    void onSuccess(Model model);
}
