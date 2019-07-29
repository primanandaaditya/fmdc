package id.co.cp.mdc.interfaces;

import id.co.cp.mdc.model.UserListModel.Datum;
import id.co.cp.mdc.model.UserListModel.UserList;

public interface IUserList {
    void onUserListError(String error);
    void onUserListSuccess(UserList userList);
}
