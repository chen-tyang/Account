package com.example.account;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ChartActivity extends AppCompatActivity {
    private String date = "";  // 选定的日期
    private String period = "日";  // 选定的时期（日/周/月/年）
    private String type = "所有";  // 选定的类型（所有/支出/收入）
    private TextView tvSetDate;
    private RadioGroup radioGroupPeriod;
    private RadioGroup radioGroupType;
    private AccountViewModel accountViewModel;
    PieChart pieChart;
    BarChart barChart;
    LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // 初始化ViewModel
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        // 初始化PieChart
        pieChart = findViewById(R.id.pie_chart);

        // 显示饼图
        showPieChart();

        // 初始化BarChart
        barChart = findViewById(R.id.bar_chart);

        showBarChart();

        lineChart = findViewById(R.id.line_chart);

        showLineChart();

        // 获取按钮
        Button buttonBack = findViewById(R.id.btn_back);

        // 设置点击监听事件
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当点击按钮时，结束当前Activity，返回MainActivity
                finish();
            }
        });

        tvSetDate = findViewById(R.id.tv_set_date);
        tvSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ChartActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                        tvSetDate.setText(date);

                        showPieChart();  // 更新饼图
                        showBarChart();
                        showLineChart();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        // 初始化RadioGroup
        radioGroupPeriod = findViewById(R.id.radio_group_period);
        radioGroupType = findViewById(R.id.radio_group_type);

        radioGroupPeriod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(date));  // 将基准日期设置为用户选择的日期
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String startDate = sdf.format(c.getTime());  // 默认值为用户选择的日期
                String endDate = startDate;  // 默认值为用户选择的日期
                if (checkedId == R.id.radio_day) {
                    period = "日";
                } else if (checkedId == R.id.radio_week) {
                    period = "周";
                    c.add(Calendar.DATE, -3);
                    startDate = sdf.format(c.getTime());
                    c.add(Calendar.DATE, 6);
                    endDate = sdf.format(c.getTime());
                } else if (checkedId == R.id.radio_month) {
                    period = "月";
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    startDate = sdf.format(c.getTime());
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                    endDate = sdf.format(c.getTime());
                } else if (checkedId == R.id.radio_year) {
                    period = "年";
                    c.set(Calendar.DAY_OF_YEAR, 1);
                    startDate = sdf.format(c.getTime());
                    c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
                    endDate = sdf.format(c.getTime());
                }

                // 如果开始日期和结束日期相同，只显示一个日期
                if (startDate.equals(endDate)) {
                    tvSetDate.setText(startDate);
                } else {
                    tvSetDate.setText(startDate + " - " + endDate);
                }

                showPieChart();  // 更新饼图
                showBarChart();
                showLineChart();
            }
        });
        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 根据选中的 RadioButton 的 id 来判断用户选择的类型
                if (checkedId == R.id.radio_all) {
                    type = "所有";
                } else if (checkedId == R.id.radio_income) {
                    type = "收入";
                } else if (checkedId == R.id.radio_expenditure) {
                    type = "支出";
                }
                showPieChart();  // 更新饼图
                showBarChart();
                showLineChart();
            }
        });
    }

    private void showPieChart() {
        LiveData<List<Account>> accountsLiveData;
        // 根据用户选择的类型和时期，从数据库中获取对应的账户信息
        if (type.equals("所有")) {
            if (period.equals("日")) {
                accountsLiveData = accountViewModel.getAccountsByDate(date);
            } else if (period.equals("周")) {
                accountsLiveData = accountViewModel.getAccountsByWeek(date);
            } else if (period.equals("月")) {
                accountsLiveData = accountViewModel.getAccountsByMonth(date);
            } else {  // period.equals("年")
                accountsLiveData = accountViewModel.getAccountsByYear(date);
            }
        } else if (type.equals("收入")) {
            if (period.equals("日")) {
                accountsLiveData = accountViewModel.getIncomeAccountsByDate(date);
            } else if (period.equals("周")) {
                accountsLiveData = accountViewModel.getIncomeAccountsByWeek(date);
            } else if (period.equals("月")) {
                accountsLiveData = accountViewModel.getIncomeAccountsByMonth(date);
            } else {  // period.equals("年")
                accountsLiveData = accountViewModel.getIncomeAccountsByYear(date);
            }
        } else {  // type.equals("支出")
            if (period.equals("日")) {
                accountsLiveData = accountViewModel.getExpenditureAccountsByDate(date);
            } else if (period.equals("周")) {
                accountsLiveData = accountViewModel.getExpenditureAccountsByWeek(date);
            } else if (period.equals("月")) {
                accountsLiveData = accountViewModel.getExpenditureAccountsByMonth(date);
            } else {  // period.equals("年")
                accountsLiveData = accountViewModel.getExpenditureAccountsByYear(date);
            }
        }

        // 观察账户数据的变化，并在数据变化时更新饼图
        accountsLiveData.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable final List<Account> accounts) {
                // 更新饼图的数据
                updatePieChartData(accounts);
            }
        });
    }

    private void updatePieChartData(List<Account> accounts) {
        List<PieEntry> entries = new ArrayList<>();
        Map<String, Float> typeAmountMap = new HashMap<>();

        for (Account account : accounts) {
            String type = account.getType();
            double amount = account.getAmount();
            if (typeAmountMap.containsKey(type)) {
                typeAmountMap.put(type, typeAmountMap.get(type) + (float) amount);
            } else {
                typeAmountMap.put(type, (float) amount);
            }
        }

        for (String type : typeAmountMap.keySet()) {
            entries.add(new PieEntry(typeAmountMap.get(type), type));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        pieChart.setData(data);
        pieChart.invalidate();  // 刷新图表
    }

    private void showBarChart() {
        LiveData<List<Account>> accountsLiveData;
        // 根据用户选择的类型和时期，从数据库中获取对应的账户信息
        if (type.equals("所有")) {
            if (period.equals("日")) {
                accountsLiveData = accountViewModel.getAccountsByDate(date);
            } else if (period.equals("周")) {
                accountsLiveData = accountViewModel.getAccountsByWeek(date);
            } else if (period.equals("月")) {
                accountsLiveData = accountViewModel.getAccountsByMonth(date);
            } else {  // period.equals("年")
                accountsLiveData = accountViewModel.getAccountsByYear(date);
            }
            accountsLiveData.observe(this, new Observer<List<Account>>() {
                @Override
                public void onChanged(@Nullable final List<Account> accounts) {
                    // 更新柱状图的数据
                    updateBarChartData(accounts);
                }
            });
        } else if (type.equals("收入")) {
            if (period.equals("日")) {
                accountsLiveData = accountViewModel.getIncomeAccountsByDate(date);
            } else if (period.equals("周")) {
                accountsLiveData = accountViewModel.getIncomeAccountsByWeek(date);
            } else if (period.equals("月")) {
                accountsLiveData = accountViewModel.getIncomeAccountsByMonth(date);
            } else {  // period.equals("年")
                accountsLiveData = accountViewModel.getIncomeAccountsByYear(date);
            }
            accountsLiveData.observe(this, new Observer<List<Account>>() {
                @Override
                public void onChanged(@Nullable final List<Account> accounts) {
                    // 更新柱状图的数据
                    updateBarChartData(accounts);
                }
            });
        } else {  // type.equals("支出")
            if (period.equals("日")) {
                accountsLiveData = accountViewModel.getExpenditureAccountsByDate(date);
            } else if (period.equals("周")) {
                accountsLiveData = accountViewModel.getExpenditureAccountsByWeek(date);
            } else if (period.equals("月")) {
                accountsLiveData = accountViewModel.getExpenditureAccountsByMonth(date);
            } else {  // period.equals("年")
                accountsLiveData = accountViewModel.getExpenditureAccountsByYear(date);
            }

            // 观察账户数据的变化，并在数据变化时更新柱状图
            accountsLiveData.observe(this, new Observer<List<Account>>() {
                @Override
                public void onChanged(@Nullable final List<Account> accounts) {
                    // 更新柱状图的数据
                    updateBarChartData(accounts);
                }
            });
        }

    }
    private void updateBarChartData(List<Account> accounts) {
        List<BarEntry> entries = new ArrayList<>();
        Map<String, Float> typeAmountMap = new HashMap<>();

        for (Account account : accounts) {
            String type = account.getType();
            double amount = account.getAmount();
            if (typeAmountMap.containsKey(type)) {
                typeAmountMap.put(type, typeAmountMap.get(type) + (float)amount);
            } else {
                typeAmountMap.put(type, (float)amount);
            }
        }

        int i = 0;
        for (String type : typeAmountMap.keySet()) {
            entries.add(new BarEntry(i++, typeAmountMap.get(type), type));
        }

        BarDataSet dataSet = new BarDataSet(entries, "账户类型");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.1f);  // 设置柱子的宽度为0.5
        data.setValueTextSize(16f);  // 设置柱状图上的数据字体大小为16
        barChart.setData(data);
        barChart.invalidate();  // 刷新图表
    }

    private void showLineChart() {
        LiveData<List<Account>> accountsLiveData;
        // 根据用户选择的类型和时期，从数据库中获取对应的账户信息
        if (type.equals("所有")) {
            if (period.equals("日")) {
                accountsLiveData = accountViewModel.getAccountsByDate(date);
            } else if (period.equals("周")) {
                accountsLiveData = accountViewModel.getAccountsByWeek(date);
            } else if (period.equals("月")) {
                accountsLiveData = accountViewModel.getAccountsByMonth(date);
            } else {  // period.equals("年")
                accountsLiveData = accountViewModel.getAccountsByYear(date);
            }
        } else if (type.equals("收入")) {
            if (period.equals("日")) {
                accountsLiveData = accountViewModel.getIncomeAccountsByDate(date);
            } else if (period.equals("周")) {
                accountsLiveData = accountViewModel.getIncomeAccountsByWeek(date);
            } else if (period.equals("月")) {
                accountsLiveData = accountViewModel.getIncomeAccountsByMonth(date);
            } else {  // period.equals("年")
                accountsLiveData = accountViewModel.getIncomeAccountsByYear(date);
            }
        } else {  // type.equals("支出")
            if (period.equals("日")) {
                accountsLiveData = accountViewModel.getExpenditureAccountsByDate(date);
            } else if (period.equals("周")) {
                accountsLiveData = accountViewModel.getExpenditureAccountsByWeek(date);
            } else if (period.equals("月")) {
                accountsLiveData = accountViewModel.getExpenditureAccountsByMonth(date);
            } else {  // period.equals("年")
                accountsLiveData = accountViewModel.getExpenditureAccountsByYear(date);
            }
        }
        // 观察账户数据的变化，并在数据变化时更新折线图
        accountsLiveData.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable final List<Account> accounts) {
                // 更新折线图的数据
                updateLineChartData(accounts);
            }
        });
    }
    private void updateLineChartData(List<Account> accounts) {
        List<Entry> entries = new ArrayList<>();

        List<String> dates = new ArrayList<>();
        for (Account account : accounts) {
            String dateString = convertLongToDateString(account.getTime());
            dates.add(dateString);
            float x = convertDateToX(dateString);
            float y = (float) account.getAmount();
            entries.add(new Entry(x, y));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);


        lineChart.getXAxis().setValueFormatter(new DateAxisValueFormatter(dates));
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh chart
    }
    private String convertLongToDateString(Date time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(time);
    }
    private float convertDateToX(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = format.parse(dateString);
            return date.getTime() / (1000 * 60 * 60 * 24);  // 将时间戳转换为天数
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public class DateAxisValueFormatter extends ValueFormatter {
        private List<String> dates;

        public DateAxisValueFormatter(List<String> dates) {
            this.dates = dates;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int index = Math.round(value);
            if (index < 0 || index >= dates.size()) {
                return "";
            } else {
                return dates.get(index);
            }
        }
    }
}