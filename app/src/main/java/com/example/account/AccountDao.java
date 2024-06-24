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

    @Query("SELECT * FROM account_table WHERE type IN ('工资', '兼职', '理财')")
    LiveData<List<Account>> getIncomeAccounts();

    @Query("SELECT * FROM account_table WHERE type IN ('餐饮', '购物', '交通', '运动', '娱乐', '学习', '办公')")
    LiveData<List<Account>> getExpenditureAccounts();

    @Query("SELECT * FROM account_table ORDER BY time DESC")
    LiveData<List<Account>> getAccountsByTimeDesc();
    //需要把日期转化为"YYYY-MM-DD"的形式才能使用这些函数。因为SQLite的strftime函数可以处理Unix时间戳。
    //在SQLite中，Unix时间戳是以秒为单位的，而Java中的时间戳是以毫秒为单位的，所以在使用strftime函数时，你需要将时间戳除以1000转换为秒。
    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') = :date")
    LiveData<List<Account>> getAccountsByDate(String date);

    /*@Query("SELECT * FROM account_table WHERE strftime('%Y-%W', time / 1000 , 'unixepoch') = :week")
    LiveData<List<Account>> getAccountsByWeek(String week);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m', time / 1000 , 'unixepoch') = :month")
    LiveData<List<Account>> getAccountsByMonth(String month);

    @Query("SELECT * FROM account_table WHERE strftime('%Y', time / 1000 , 'unixepoch') = :year")
    LiveData<List<Account>> getAccountsByYear(String year);*/
    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') BETWEEN date(:date, '-3 day') AND date(:date, '+3 day')")
    LiveData<List<Account>> getAccountsByWeek(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m', time / 1000 , 'unixepoch') = strftime('%Y-%m', :date)")
    LiveData<List<Account>> getAccountsByMonth(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y', time / 1000 , 'unixepoch') = strftime('%Y', :date)")
    LiveData<List<Account>> getAccountsByYear(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') = :date AND type IN ('餐饮', '购物', '交通', '运动', '娱乐', '学习', '办公')")
    LiveData<List<Account>> getExpenditureAccountsByDate(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') BETWEEN date(:date, '-3 day') AND date(:date, '+3 day') AND type IN ('餐饮', '购物', '交通', '运动', '娱乐', '学习', '办公')")
    LiveData<List<Account>> getExpenditureAccountsByWeek(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m', time / 1000 , 'unixepoch') = strftime('%Y-%m', :date) AND type IN ('餐饮', '购物', '交通', '运动', '娱乐', '学习', '办公')")
    LiveData<List<Account>> getExpenditureAccountsByMonth(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y', time / 1000 , 'unixepoch') = strftime('%Y', :date) AND type IN ('餐饮', '购物', '交通', '运动', '娱乐', '学习', '办公')")
    LiveData<List<Account>> getExpenditureAccountsByYear(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') = :date AND type IN ('工资', '兼职', '理财')")
    LiveData<List<Account>> getIncomeAccountsByDate(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m-%d', time / 1000 , 'unixepoch') BETWEEN date(:date, '-3 day') AND date(:date, '+3 day') AND type IN ('工资', '兼职', '理财')")
    LiveData<List<Account>> getIncomeAccountsByWeek(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y-%m', time / 1000 , 'unixepoch') = strftime('%Y-%m', :date) AND type IN ('工资', '兼职', '理财')")
    LiveData<List<Account>> getIncomeAccountsByMonth(String date);

    @Query("SELECT * FROM account_table WHERE strftime('%Y', time / 1000 , 'unixepoch') = strftime('%Y', :date) AND type IN ('工资', '兼职', '理财')")
    LiveData<List<Account>> getIncomeAccountsByYear(String date);
}
