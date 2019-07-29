package id.co.cp.mdc.interfaces;

import id.co.cp.mdc.model.approvecount.ApproveCountModel;

public interface IApproveCount {
    void onApproveCountError(String error);
    void onApproveCountSuccess(ApproveCountModel approveCountModel);
}
