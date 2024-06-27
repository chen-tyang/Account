package com.example.account;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * ViewModel层
 * 管理业务逻辑：ViewModel 负责处理业务逻辑和与数据层的交互，将处理结果提供给 UI 层。
 * 解耦数据与 UI：ViewModel 将数据和业务逻辑与 UI 分离，确保数据层和 UI 层互不依赖。
 *
 * LiveData 是一个可观察的数据持有者，具有生命周期感知能力。其主要作用如下:
 * 数据持有：LiveData 持有数据，并提供数据的实时更新。
 * 观察者模式：LiveData 允许 UI 层（观察者）订阅数据的变化，当数据变化时，LiveData 会自动通知观察者并更新 UI。
 */
public class AccountViewModel extends AndroidViewModel {
    private final AccountRepository repository;
    private final LiveData<List<Account>> allAccounts;

    public AccountViewModel(Application application) {
        super(application);
        repository = new AccountRepository(application);
        allAccounts = repository.getAllAccounts();
    }

    public LiveData<List<Account>> getAllAccounts() {
        return allAccounts;
    }

    public void insert(Account account) {
        repository.insert(account);
    }

    public LiveData<Account> getAccount(int id) {
        return repository.getAccount(id);
    }

    public void delete(Account account) {
        repository.delete(account);
    }

    public void update(Account account) {
        repository.update(account);
    }

    public LiveData<List<Account>> getAccountsByType(String type) {
        return repository.getAccountsByType(type);
    }

    public LiveData<List<Account>> getIncomeAccounts() {
        return repository.getIncomeAccounts();
    }

    public LiveData<List<Account>> getExpenditureAccounts() {
        return repository.getExpenditureAccounts();
    }

    public LiveData<List<Account>> getAccountsByTimeDesc() {
        return repository.getAccountsByTimeDesc();
    }

    public LiveData<List<Account>> getAccountsByDate(String date) {
        return repository.getAccountsByDate(date);
    }

    public LiveData<List<Account>> getAccountsByWeek(String date) {
        return repository.getAccountsByWeek(date);
    }

    public LiveData<List<Account>> getAccountsByMonth(String date) {
        return repository.getAccountsByMonth(date);
    }

    public LiveData<List<Account>> getAccountsByYear(String date) {
        return repository.getAccountsByYear(date);
    }

    public LiveData<List<Account>> getExpenditureAccountsByDate(String date) {
        return repository.getExpenditureAccountsByDate(date);
    }

    public LiveData<List<Account>> getExpenditureAccountsByWeek(String date) {
        return repository.getExpenditureAccountsByWeek(date);
    }

    public LiveData<List<Account>> getExpenditureAccountsByMonth(String date) {
        return repository.getExpenditureAccountsByMonth(date);
    }

    public LiveData<List<Account>> getExpenditureAccountsByYear(String date) {
        return repository.getExpenditureAccountsByYear(date);
    }

    public LiveData<List<Account>> getIncomeAccountsByDate(String date) {
        return repository.getIncomeAccountsByDate(date);
    }

    public LiveData<List<Account>> getIncomeAccountsByWeek(String date) {
        return repository.getIncomeAccountsByWeek(date);
    }

    public LiveData<List<Account>> getIncomeAccountsByMonth(String date) {
        return repository.getIncomeAccountsByMonth(date);
    }

    public LiveData<List<Account>> getIncomeAccountsByYear(String date) {
        return repository.getIncomeAccountsByYear(date);
    }
}