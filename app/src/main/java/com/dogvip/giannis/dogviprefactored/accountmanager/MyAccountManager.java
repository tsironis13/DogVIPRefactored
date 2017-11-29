package com.dogvip.giannis.dogviprefactored.accountmanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;

import javax.inject.Inject;


/**
 * Created by giannis on 22/5/2017.
 */

public class MyAccountManager {

    @Inject
    Context mContext;
    @Inject
    AccountManager am;
    @Inject
    UserAccount userAccount;

    @Inject
    public MyAccountManager() {}

    private Account getAccountByType(String type) {
        Account[] mAccount = am.getAccountsByType(type);
        return mAccount.length == 1 ? mAccount[0] : null;
    }

    public boolean checkAccountExists() {
        return am.getAccountsByType(mContext.getResources().getString(R.string.account_type)).length != 0;
    }

    public boolean addAccount(String email, String token) {
        if (checkAccountExists()) return false;
        Account account = new Account(email, mContext.getResources().getString(R.string.account_type));

        Bundle extra = new Bundle();
        extra.putString(mContext.getResources().getString(R.string.email), email);
        extra.putString(mContext.getResources().getString(R.string.token), token); //used by me to get auth token easily without passing it between activities
        boolean account_added = am.addAccountExplicitly(account, null, extra);
        am.setAuthToken(account, "1", token); //used by the authenticator
        return account_added;
    }

    public UserAccount getAccountDetails() {
//        UserAccount userAccount = new UserAccount();//inject
        Account mAccount = getAccountByType(mContext.getResources().getString(R.string.account_type));
        try {
            String mtoken = AccountManager.get(mContext).getUserData(mAccount, mContext.getResources().getString(R.string.token));
            String email = AccountManager.get(mContext).getUserData(mAccount, mContext.getResources().getString(R.string.email));
            userAccount.setToken(mtoken);
            userAccount.setEmail(email);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return userAccount;
    }

    public void removeAccount() {
        Account mAccount = getAccountByType(mContext.getResources().getString(R.string.account_type));
        if (mAccount != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                AccountManager.get(mContext).removeAccount(mAccount, null, null);
            } else {
                AccountManager.get(mContext).removeAccount(mAccount, null, null, null);
            }
        }
    }


}
