package com.example.account;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private AccountRepository mRepository;
    private LiveData<List<Account>> mAllAccounts;

    public AccountViewModel(Application application) {
        super(application);
        mRepository = new AccountRepository(application);
        mAllAccounts = mRepository.getAllAccounts();
    }
    LiveData<List<Account>> getAllAccounts() {
        return mAllAccounts;
    }
    public void insert(Account account) {
        mRepository.insert(account);
    }
    public LiveData<Account> getAccount(int id) {
        return mRepository.getAccount(id);
    }
    public void delete(Account account) {
        mRepository.delete(account);
    }

    public void update(Account account) {
        mRepository.update(account);
    }

    LiveData<List<Account>> getAccountsByType(String type) {
        return mRepository.getAccountsByType(type);
    }

    LiveData<List<Account>> getIncomeAccounts() {
        return mRepository.getIncomeAccounts();
    }

    LiveData<List<Account>> getExpenditureAccounts() {
        return mRepository.getExpenditureAccounts();
    }

    LiveData<List<Account>> getAccountsByTimeDesc() {
        return mRepository.getAccountsByTimeDesc();
    }

    LiveData<List<Account>> getAccountsByDate(String date) {
        return mRepository.getAccountsByDate(date);
    }

    LiveData<List<Account>> getAccountsByWeek(String date) {
        return mRepository.getAccountsByWeek(date);
    }

    LiveData<List<Account>> getAccountsByMonth(String date) {
        return mRepository.getAccountsByMonth(date);
    }

    LiveData<List<Account>> getAccountsByYear(String date) {
        return mRepository.getAccountsByYear(date);
    }

    LiveData<List<Account>> getExpenditureAccountsByDate(String date) {
        return mRepository.getExpenditureAccountsByDate(date);
    }

    LiveData<List<Account>> getExpenditureAccountsByWeek(String date) {
        return mRepository.getExpenditureAccountsByWeek(date);
    }

    LiveData<List<Account>> getExpenditureAccountsByMonth(String date) {
        return mRepository.getExpenditureAccountsByMonth(date);
    }

    LiveData<List<Account>> getExpenditureAccountsByYear(String date) {
        return mRepository.getExpenditureAccountsByYear(date);
    }

    LiveData<List<Account>> getIncomeAccountsByDate(String date) {
        return mRepository.getIncomeAccountsByDate(date);
    }

    LiveData<List<Account>> getIncomeAccountsByWeek(String date) {
        return mRepository.getIncomeAccountsByWeek(date);
    }

    LiveData<List<Account>> getIncomeAccountsByMonth(String date) {
        return mRepository.getIncomeAccountsByMonth(date);
    }

    LiveData<List<Account>> getIncomeAccountsByYear(String date) {
        return mRepository.getIncomeAccountsByYear(date);
    }
}