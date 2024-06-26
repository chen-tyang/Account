package com.example.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

public class AccountEditActivity extends AppCompatActivity {

    private Button buttonSave;
    private Button buttonDelete;
    private Button buttonCancel;
    private AccountViewModel accountViewModel;
    private Account currentAccount;
    private EditText editTextAmount;
    private EditText editTextRemarks;
    private Spinner spinner;
    String [] array;
    int [] imageArray;
    // 增加一个成员变量来存储 decimalPlaces
    private int decimalPlaces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//array读取res/strings里的type类型的选项
        //imageArray对应图片编号
        array = getResources().getStringArray(R.array.spinner_type);
        imageArray = new int[]{
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
        };
        setContentView(R.layout.activity_account_edit);
        // 用新的 ImageArrayAdapter 替换你现有的 ArrayAdapter
        spinner = findViewById(R.id.spinner);
        ImageArrayAdapter spinnerAdapter = new ImageArrayAdapter(this, R.layout.spinner_with_image, array, imageArray);
        spinner.setAdapter(spinnerAdapter);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextRemarks = findViewById(R.id.editTextRemarks);

        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonCancel = findViewById(R.id.buttonCancel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 从 Intent 中获取 decimalPlaces 的值
        Intent intent = getIntent();
        decimalPlaces = intent.getIntExtra("decimalPlaces", 2);
        int accountId = intent.getIntExtra("accountId", -1);
        // 初始化ViewModel
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        //accountId!=-1代表我点击的是已经存在于数据库的数据
        if (accountId != -1) {
            LiveData<Account> accountToEdit = accountViewModel.getAccount(accountId);
            // 观察LiveData的记账数据，在数据变化时更新UI
            accountToEdit.observe(this, new Observer<Account>() {
                @Override
                public void onChanged(@Nullable final Account account) {
                    if (account != null) {
                        //如果不加，currentAccount对象始终为null，它从未被赋值，就无法删除(为了删除已经存在的记录)
                        currentAccount = account;

                        // 将得到的记账信息设置到对应的EditText中
                        editTextAmount.setText(String.valueOf(account.getAmount()));
                        editTextRemarks.setText(account.getRemarks());
                        // 获取'spinnerOption'从account的'type'字段
                        String spinnerOption = account.getType();
                        // 在spinner的适配器中找到该选项对应的位置
                        int spinnerPosition = spinnerAdapter.getPosition(spinnerOption);
                        // 设定spinner的选项为该位置
                        spinner.setSelection(spinnerPosition);
                    }
                }
            });
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String remarks = editTextRemarks.getText().toString();
                    // 获取Spinner选择的选项
                    String type = (String) spinner.getSelectedItem();

                    // 更新至原有的Account对象
                    //先检查textview中的文本信息是否符合要求，然后再转换成double类型存储
                    String amountString = editTextAmount.getText().toString();
                    double amount = Double.parseDouble(editTextAmount.getText().toString());
                    if (!isValidAmount(amountString)) {
                        String toastMessage = String.format("金额应当最多包含%d位小数", decimalPlaces);
                        Toast.makeText(AccountEditActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    accountToEdit.getValue().setAmount(amount);
                    accountToEdit.getValue().setRemarks(remarks);
                    accountToEdit.getValue().setType(type);
                    Date currentTime = new Date();
                    accountToEdit.getValue().setTime(currentTime); // 设置time为当前系统时间
                    // 使用ViewModel更新Account
                    accountViewModel.update(accountToEdit.getValue());

                    // 返回主Activity
                    finish();
                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 返回主Activity
                    finish();
                }
            });
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如果currentAccount为null，说明观察的LiveData没有发生变化，或者Account不存在
                    //但这段代码不会执行，因为从列表点进来的都是已经存在的
                    if (currentAccount == null) {
                        Toast.makeText(AccountEditActivity.this, "No account to be deleted", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 通过ViewModel删除此Contact
                    accountViewModel.delete(currentAccount);
                    // 返回主Activity
                    finish();
                }
            });
        } else {//代表accountId==-1,说明是新建的数据,而不是修改或者删除数据
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //先检查textview中的文本信息是否符合要求，然后再转换成double类型存储
                    String amountString = editTextAmount.getText().toString();
                    double amount = Double.parseDouble(amountString);
                    if (!isValidAmount(amountString)) {
                        String toastMessage = String.format("金额应当最多包含%d位小数", decimalPlaces);
                        Toast.makeText(AccountEditActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String remarks = editTextRemarks.getText().toString();
                    String category = spinner.getSelectedItem().toString();
                    // 获取当前系统时间的时间戳
                    Date currentTime = new Date();
                    // 创建一个新的Account对象
                    currentAccount = new Account(amount, category, currentTime, remarks);
                    // 使用ViewModel插入新的Account
                    accountViewModel.insert(currentAccount);
                    // 返回主Activity
                    finish();
                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 直接返回到主Activity
                    finish();
                }
            });
            buttonDelete.setVisibility(View.GONE);
        }
    }

    //isValidAmount 方法直接检查字符串中是否存在小数点，并确保小数点后不超过两位小数
    private boolean isValidAmount(String amountString) {
        int decimalIndex = amountString.indexOf(".");//用于查找金额字符串中小数点的位置。如果不存在小数点，indexOf返回-1。
        //decimalIndex确保字符串中存在小数点,amountString.length() - decimalIndex - 1 > 2计算小数点后的位数，并检查是否超过两位。
        if (decimalIndex >= 0) {
            int decimalPlacesInAmount = amountString.length() - decimalIndex - 1;
            if ((decimalPlaces == 2 && decimalPlacesInAmount > 2) || (decimalPlaces == 6 && decimalPlacesInAmount > 6)) {
                return false;
            }
        }
        return true;
    }

}