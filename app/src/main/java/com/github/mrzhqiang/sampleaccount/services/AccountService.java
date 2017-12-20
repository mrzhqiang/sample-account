package com.github.mrzhqiang.sampleaccount.services;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.mrzhqiang.sampleaccount.AddAccountActivity;

public class AccountService extends Service {

    private AbstractAccountAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticator = new AccountAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }

    private static class AccountAuthenticator extends AbstractAccountAuthenticator {

        private final Context context;

        AccountAuthenticator(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            Bundle bundle = new Bundle();

            Intent intent = new Intent(context, AddAccountActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
            intent.putExtra(AddAccountActivity.KEY_IS_NEW_ACCOUNT, true);
            intent.putExtra(AccountManager.KEY_AUTHENTICATOR_TYPES, authTokenType);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            // 获取Token时，先检查存在与否
            AccountManager accountManager = AccountManager.get(context);
            String token = accountManager.peekAuthToken(account, authTokenType);
            if (token == null) {
                String password = accountManager.getPassword(account);
                if (password != null) {
                    // TODO 重新登陆，拿到token
                }
            }

            // 再次检查token，因为有可能密码已经失效
            if (token != null) {
                Bundle bundle = new Bundle();
                bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                bundle.putString(AccountManager.KEY_AUTHTOKEN, token);
                return bundle;
            }

            // token和密码都无效的情况下，那就只能告诉用户，你需要重新登陆
            return addAccount(response, account.type, authTokenType, null, options);
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
            return null;
        }
    }
}
