package com.github.mrzhqiang.sampleaccount;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mrZQ.
 */

public class CheckAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_account);

        RecyclerView recyclerView = findViewById(R.id.recycler_accounts);

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccounts();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AccountsAdapter(accounts));
    }

    private static class AccountsAdapter extends RecyclerView.Adapter<ItemAccountViewHolder> {

        private final List<Account> dataList = new ArrayList<>();

        public AccountsAdapter(Account[] accounts) {
            Collections.addAll(dataList, accounts);
        }

        @Override
        public ItemAccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
            return new ItemAccountViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemAccountViewHolder holder, int position) {
            holder.bind(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    private static class ItemAccountViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView type;
        private final TextView password;
        private final AccountManager accountManager;

        public ItemAccountViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            type = itemView.findViewById(R.id.text_type);
            password = itemView.findViewById(R.id.text_password);
            accountManager = AccountManager.get(itemView.getContext());
        }

        public void bind(Account account) {
            name.setText(account.name);
            type.setText(account.type);
            try {
                password.setText(accountManager.getPassword(account));
            } catch (Exception r) {
                password.setText(r.getMessage());
            }
        }
    }
}
