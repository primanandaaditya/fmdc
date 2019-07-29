package id.co.cp.mdc.interfaces;

import id.co.cp.mdc.model.approve.P4Model;

public interface IApprove {
    void onApproveError(String error);
    void onApproveSuccess(P4Model p4Model);
}
