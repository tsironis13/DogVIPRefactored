package com.dogvip.giannis.dogviprefactored.accountmanager;

import android.accounts.AccountManager;
import android.content.Context;

import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;

import dagger.Module;
import dagger.Provides;

/**
 * Created by giannis on 4/11/2017.
 */
@Module
public abstract class AccountManagerModule {

    @Provides
    static AccountManager provideAccountManager(Context context) {
        return AccountManager.get(context);
    }

}
