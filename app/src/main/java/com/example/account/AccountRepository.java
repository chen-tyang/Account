package com.example.account;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 *
 */
public class AccountRepository {
    private AccountDao mAccountDao;
    private LiveData<List<Account>> mAllAccounts;

    AccountRepository(Application application) {
        AccountRoomDatabase db = AccountRoomDatabase.getDatabase(application);
        mAccountDao = db.accountDao();
        mAllAccounts = mAccountDao.getAllAccounts();
    }

    LiveData<List<Account>> getAllAccounts() {
        return mAllAccounts;
    }

    public void insert(Account account) {
        new insertAsyncTask(mAccountDao).execute(account);
    }

    public LiveData<Account> getAccount(int id) {
        return mAccountDao.getAccount(id);
    }

    /**
     * 使用AsyncTask使用内部的线程池来执行后台任务并会根据需要分配和管理线程资源. 在任务完成后将结果传递回主线程
     * doInBackground(Params...)：在后台线程中执行实际的任务，不会阻塞主线程。从而防止应用界面无响应
     */
    private static class insertAsyncTask extends AsyncTask<Account, Void, Void> {
        private AccountDao mAsyncTaskDao;

        insertAsyncTask(AccountDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Account... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void delete(Account account) {
        new deleteAsyncTask(mAccountDao).execute(account);
    }

    private static class deleteAsyncTask extends AsyncTask<Account, Void, Void> {
        private AccountDao mAsyncTaskDao;

        deleteAsyncTask(AccountDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Account... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public void update(Account account) {
        new UpdateAccountTask(mAccountDao).execute(account);
    }

    // 使用 AsyncTask 处理数据库操作
    private static class UpdateAccountTask extends AsyncTask<Account, Void, Void> {
        private AccountDao accountDao;

        UpdateAccountTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.update(accounts[0]);
            return null;
        }
    }

    LiveData<List<Account>> getAccountsByType(String type) {
        return mAccountDao.getAccountsByType(type);
    }

    LiveData<List<Account>> getIncomeAccounts() {
        return mAccountDao.getIncomeAccounts();
    }

    LiveData<List<Account>> getExpenditureAccounts() {
        return mAccountDao.getExpenditureAccounts();
    }

    LiveData<List<Account>> getAccountsByTimeDesc() {
        return mAccountDao.getAccountsByTimeDesc();
    }

    LiveData<List<Account>> getAccountsByDate(String date) {
        return mAccountDao.getAccountsByDate(date);
    }

    LiveData<List<Account>> getAccountsByWeek(String date) {
        return mAccountDao.getAccountsByWeek(date);
    }

    LiveData<List<Account>> getAccountsByMonth(String date) {
        return mAccountDao.getAccountsByMonth(date);
    }

    LiveData<List<Account>> getAccountsByYear(String date) {
        return mAccountDao.getAccountsByYear(date);
    }

    LiveData<List<Account>> getExpenditureAccountsByDate(String date) {
        return mAccountDao.getExpenditureAccountsByDate(date);
    }

    LiveData<List<Account>> getExpenditureAccountsByWeek(String date) {
        return mAccountDao.getExpenditureAccountsByWeek(date);
    }

    LiveData<List<Account>> getExpenditureAccountsByMonth(String date) {
        return mAccountDao.getExpenditureAccountsByMonth(date);
    }

    LiveData<List<Account>> getExpenditureAccountsByYear(String date) {
        return mAccountDao.getExpenditureAccountsByYear(date);
    }

    LiveData<List<Account>> getIncomeAccountsByDate(String date) {
        return mAccountDao.getIncomeAccountsByDate(date);
    }

    LiveData<List<Account>> getIncomeAccountsByWeek(String date) {
        return mAccountDao.getIncomeAccountsByWeek(date);
    }

    LiveData<List<Account>> getIncomeAccountsByMonth(String date) {
        return mAccountDao.getIncomeAccountsByMonth(date);
    }

    LiveData<List<Account>> getIncomeAccountsByYear(String date) {
        return mAccountDao.getIncomeAccountsByYear(date);
    }
}
