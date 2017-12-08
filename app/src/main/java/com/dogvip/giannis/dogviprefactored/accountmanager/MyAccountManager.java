package com.dogvip.giannis.dogviprefactored.accountmanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.subjects.MaybeSubject;


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

    public boolean addAccount(String email, String token, int user_id) {
        if (checkAccountExists()) return false;
        Account account = new Account(email, mContext.getResources().getString(R.string.account_type));

        Bundle extra = new Bundle();
        extra.putString(mContext.getResources().getString(R.string.email), email);
//        Log.e("kjaskas", user_id + " user id");
        extra.putString(mContext.getResources().getString(R.string.user_id), String.valueOf(user_id));
        extra.putString(mContext.getResources().getString(R.string.token), token); //used by me to get auth token easily without passing it between activities
        boolean account_added = am.addAccountExplicitly(account, null, extra);
        am.setAuthToken(account, "1", token); //used by the authenticator
        return account_added;
    }

    public UserAccount getAccountDetails() {
        Account mAccount = getAccountByType(mContext.getResources().getString(R.string.account_type));
        if (mAccount == null) return null;

        String mtoken = AccountManager.get(mContext).getUserData(mAccount, mContext.getResources().getString(R.string.token));
        String email = AccountManager.get(mContext).getUserData(mAccount, mContext.getResources().getString(R.string.email));
        String userId = AccountManager.get(mContext).getUserData(mAccount, mContext.getResources().getString(R.string.user_id));
        userAccount.setToken(mtoken);
        userAccount.setEmail(email);
        userAccount.setUserId(Integer.parseInt(userId));

        return userAccount;
    }

    public MaybeSubject<Boolean> removeAccount() {
        MaybeSubject<Boolean> maybeSubject = MaybeSubject.create();
        Account mAccount = getAccountByType(mContext.getResources().getString(R.string.account_type));
        if (mAccount != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                AccountManager.get(mContext).removeAccount(mAccount, accountManagerFuture -> {
                    try {
                        Log.e("aaaa", accountManagerFuture.getResult() + " result");
                        maybeSubject.onSuccess(true);
                    } catch (OperationCanceledException e) {
                        maybeSubject.onError(new OperationCanceledException());
                    } catch (IOException e) {
                        maybeSubject.onError(new IOException());
                    } catch (AuthenticatorException e) {
                        maybeSubject.onError(new AuthenticatorException());
                    }
                    maybeSubject.onComplete();
                }, null);
            } else {
                AccountManager.get(mContext).removeAccount(mAccount, null, accountManagerFuture -> {
                    try {
                        Log.e("aaaa", accountManagerFuture.getResult().getBoolean("booleanResult") + " result");
                        maybeSubject.onSuccess(true);
                    } catch (OperationCanceledException e) {
                        maybeSubject.onError(new OperationCanceledException());
                    } catch (IOException e) {
                        maybeSubject.onError(new IOException());
                    } catch (AuthenticatorException e) {
                        maybeSubject.onError(new AuthenticatorException());
                    }
                    maybeSubject.onComplete();
                }, null);
            }
        }
        return maybeSubject;
    }

}
