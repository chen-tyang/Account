package com.example.account;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDao {
    String EXPENDITURE_TYPES = "'餐饮', '购物', '交通', '运动', '娱乐', '学习', '办公'";
    String INCOME_TYPES = "'工资', '兼职', '理财'";

    @Insert
    void insert(Account account);

    @Delete
    void delete(Account account);

    @Update
    void update(Account account);

    @Query("SELECT * FROM account_table")
    LiveData<List<Account>> getAllAccounts();

    @Query("SELECT * FROM account_table WHERE id = :id")
    LiveData<Account> getAccount(int id);

    @Query("SELECT * FROM account_table WHERE type = :type")
    LiveData<List<Account>> getAccountsByType(String type);

    @Query("SELECT * FROM account_table WHERE type IN (" + INCOME_TYPES + ")")
    LiveData<List<Account>> getIncomeAccounts();

    @Query("SELECT * FROM account_table WHERE type IN (" + EXPENDITURE_TYPES + ")")
    LiveData<List<Account>> getExpenditureAccounts();

    @Query("SELECT * FROM account_table ORDER BY time DESC")
    LiveData<List<Account>> getAccountsByTimeDesc();

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') = :date")
    LiveData<List<Account>> getAccountsByDate(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') BETWEEN date(:date, '-3 day') AND date(:date, '+3 day')")
    LiveData<List<Account>> getAccountsByWeek(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m', time / 1000 , 'unixepoch') = strftime('%Y-%m', :date)")
    LiveData<List<Account>> getAccountsByMonth(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y', time / 1000 , 'unixepoch') = strftime('%Y', :date)")
    LiveData<List<Account>> getAccountsByYear(String date);

    // 以下是优化后的收入和支出查询方法

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') = :date AND type IN (" + EXPENDITURE_TYPES + ")")
    LiveData<List<Account>> getExpenditureAccountsByDate(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') BETWEEN date(:date, '-3 day') AND date(:date, '+3 day') AND type IN (" + EXPENDITURE_TYPES + ")")
    LiveData<List<Account>> getExpenditureAccountsByWeek(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m', time / 1000 , 'unixepoch') = strftime('%Y-%m', :date) AND type IN (" + EXPENDITURE_TYPES + ")")
    LiveData<List<Account>> getExpenditureAccountsByMonth(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y', time / 1000 , 'unixepoch') = strftime('%Y', :date) AND type IN (" + EXPENDITURE_TYPES + ")")
    LiveData<List<Account>> getExpenditureAccountsByYear(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') = :date AND type IN (" + INCOME_TYPES + ")")
    LiveData<List<Account>> getIncomeAccountsByDate(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') BETWEEN date(:date, '-3 day') AND date(:date, '+3 day') AND type IN (" + INCOME_TYPES + ")")
    LiveData<List<Account>> getIncomeAccountsByWeek(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m', time / 1000 , 'unixepoch') = strftime('%Y-%m', :date) AND type IN (" + INCOME_TYPES + ")")
    LiveData<List<Account>> getIncomeAccountsByMonth(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y', time / 1000 , 'unixepoch') = strftime('%Y', :date) AND type IN (" + INCOME_TYPES + ")")
    LiveData<List<Account>> getIncomeAccountsByYear(String date);
}
