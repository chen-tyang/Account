package com.example.account;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button buttonAdd;
    private Button chartButton;
    private AccountListAdapter accountListAdapter;
    private RecyclerView recyclerView;
    private AccountViewModel accountViewModel;
    private double totalAmount = 0.0;
    private double totalIncome = 0.0;
    private double totalExpenditure = 0.0;
    private Spinner spinner;
    private RadioGroup radioGroup;
    // 增加一个变量来表示保留的小数位数
    private int decimalPlaces = 2;
    String [] yourArray;
    int [] yourImageArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = findViewById(R.id.btn_add);

        yourArray = getResources().getStringArray(R.array.spinner_option);
        yourImageArray = new int[]{
                R.drawable.all,
                R.drawable.part_time_job,
                R.drawable.other,
                R.drawable.other,
                R.drawable.catering,
                R.drawable.shopping,
                R.drawable.transportation,
                R.drawable.sports,
                R.drawable.entertainment,
                R.drawable.learning,
                R.drawable.office_work,
                R.drawable.salary,
                R.drawable.part_time_job,
                R.drawable.financial_management,
                R.drawable.other
        };

        //添加按钮
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountEditActivity.class);
                intent.putExtra("decimalPlaces", decimalPlaces);
                startActivity(intent);
            }
        });

        // 用新的 ImageArrayAdapter 替换你现有的 ArrayAdapter
        spinner = findViewById(R.id.spinner);
        ImageArrayAdapter spinnerAdapter = new ImageArrayAdapter(this, R.layout.spinner_with_image, yourArray, yourImageArray);
        spinner.setAdapter(spinnerAdapter);

        //使用AccountListAdapter的显示风格显示到recyclerview中
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountListAdapter = new AccountListAdapter(new ArrayList<>());
        recyclerView.setAdapter(accountListAdapter);
        accountListAdapter.setOnItemClickListener(new AccountListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Account account) {
                Intent intent = new Intent(MainActivity.this, AccountEditActivity.class);
                intent.putExtra("accountId", account.getId()); // 当我点击已经存在于列表的项需要传递点击的记录的id属性
                startActivity(intent);
            }
        });

        //初始化accountViewModel
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        //通过AccountViewModel显示所有的数据
        accountViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable final List<Account> accounts) {
                updateAmounts();
            }
        });

        //下拉列表的选项
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedType = (String) parent.getItemAtPosition(pos);
                LiveData<List<Account>> accountsByType;
                //选时间倒序排序,收入会显示收入的所有记录,支出会显示所有支出记录
                switch (selectedType) {
                    case "时间":
                        accountsByType = accountViewModel.getAccountsByTimeDesc();
                        break;
                    case "收入":
                        accountsByType = accountViewModel.getIncomeAccounts();
                        break;
                    case "支出":
                        accountsByType = accountViewModel.getExpenditureAccounts();
                        break;
                    case "所有":
                        accountsByType = accountViewModel.getAllAccounts();
                        break;
                    default:
                        accountsByType = accountViewModel.getAccountsByType(selectedType);
                }

                //通过accountsByType的accountViewModel所有符合条件的记录,然后把记录更新
                accountsByType.observe(MainActivity.this, new Observer<List<Account>>() {
                    @Override
                    public void onChanged(@Nullable final List<Account> accounts) {
                        // 修改Adapter的数据，而不是创建新的Adapter
                        accountListAdapter.setAccounts(accounts);
                        // 通知Adapter数据已经改变，让它更新视图
                        accountListAdapter.notifyDataSetChanged();
                    }
                });
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //视图按钮
        chartButton = findViewById(R.id.btn_chart);
        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });

        // 初始化 RadioGroup
        radioGroup = findViewById(R.id.radioGroup);

        // 设置监听器
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId 是当前选中的 RadioButton 的 id
                if (checkedId == R.id.radio_standard) {
                    decimalPlaces = 2;
                    accountListAdapter.updateDecimalPlaces(2);
                } else if (checkedId == R.id.radio_scientific) {
                    decimalPlaces = 6;
                    accountListAdapter.updateDecimalPlaces(6);
                }

                updateAmounts();
            }
        });

    }
    //更新金额的显示
    private void updateAmounts() {
        List<Account> currentAccounts = accountViewModel.getAllAccounts().getValue();
        if(currentAccounts != null) {
            // 重新计算总金额
            totalAmount = 0.0;
            totalIncome = 0.0;
            totalExpenditure = 0.0;

            // 计算 totalIncome 和 totalExpenditure
            for (Account account : currentAccounts) {
                if (account.getType().equals("工资") || account.getType().equals("兼职") || account.getType().equals("理财")) {
                    totalIncome += account.getAmount();
                } else {
                    totalExpenditure += account.getAmount();
                }
            }

            // 计算总金额
            totalAmount = totalIncome - totalExpenditure;

            // 根据 decimalPlaces 来格式化金额
            String format = "%." + decimalPlaces + "f";
            String formattedIncome = String.format(format, totalIncome);
            String formattedExpenditure = String.format(format, totalExpenditure);
            String formattedTotalAmount = String.format(format, totalAmount);

            // 获取 TextViews
            TextView tvIncome = findViewById(R.id.tv_income);
            TextView tvExpenditure = findViewById(R.id.tv_expenditure);
            TextView tvTotalAmount = findViewById(R.id.tv_total_amount);

            // 修改 TextView 的文本值
            tvIncome.setText("收入：" + formattedIncome);
            tvExpenditure.setText("支出：" + formattedExpenditure);
            tvTotalAmount.setText("总金额：" + formattedTotalAmount);
        }
    }
}
