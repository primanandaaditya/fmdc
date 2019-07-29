package id.co.cp.mdc.interfaces;

import id.co.cp.mdc.model.processapprove.ProcessApproveModel;

public interface IProcessApprove {

    void onProcessError(String error);
    void onProcessSuccess(ProcessApproveModel processApproveModel);
    void doProcess();
}
