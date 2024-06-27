package com.example.account;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Room数据库 使用注解来定义数据库实体、DAO（数据访问对象）接口以及数据库操作。
 * Room 会在编译时根据定义的 DAO 接口自动生成实现这些接口的代码。这些代码主要负责将数据库操作请求转换为实际的 SQL 查询，并与 SQLite 数据库进行交互。
 */

/**
 * @Dao 注解用于标识数据访问对象（DAO）接口。DAO 是一个接口，定义了访问数据库的方法。
 * Room 会自动生成实现这些方法的代码。DAO 提供了用于执行数据库操作的方法，例如插入、更新、删除和查询。
 */
@Dao
public interface AccountDao {
    String EXPENDITURE_TYPES = "'餐饮', '购物', '交通', '运动', '娱乐', '学习', '办公'";
    String INCOME_TYPES = "'工资', '兼职', '理财'";

    /**
     * @Insert 注解用于标识插入操作的方法。它告诉 Room 在调用该方法时，将传入的对象插入到数据库中。可以插入单个对象或对象的集合。
     */
    @Insert
    void insert(Account account);

    /**
     * @Delete 注解用于标识删除操作的方法。它告诉 Room 在调用该方法时，将传入的对象从数据库中删除。
     */
    @Delete
    void delete(Account account);

    /**
     * @Update 注解用于标识更新操作的方法。它告诉 Room 在调用该方法时，将传入的对象在数据库中更新。
     */
    @Update
    void update(Account account);

    /**
     * @Query 注解用于标识查询操作的方法。它允许定义一个查询，并将其结果映射到方法的返回类型。查询可以是任意的 SQL 语句
     */
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
