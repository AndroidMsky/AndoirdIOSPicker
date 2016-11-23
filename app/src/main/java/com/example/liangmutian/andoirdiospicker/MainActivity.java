package com.example.liangmutian.andoirdiospicker;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.liangmutian.mypicker.DataPickerDialog;
import com.example.liangmutian.mypicker.DatePickerDialog;
import com.example.liangmutian.mypicker.DateUtil;
import com.example.liangmutian.mypicker.TimePickerDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Dialog dateDialog, timeDialog, chooseDialog;
    private TextView mTextView;
    private List<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView);
        String[] data = getResources().getStringArray(R.array.list);
        for (String str : data) {
            list.add(str);
        }
    }

    /**
     * chooseDialog
     */
    private void showChooseDialog(List<String> mlist) {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        chooseDialog = builder.setData(mlist).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        mTextView.setText(itemValue);

                    }

                    @Override
                    public void onCancel() {

                    }
                }).create();

        chooseDialog.show();
    }


    private void showDateDialog(List<Integer> date) {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this);
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {

                mTextView.setText(dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                        + (dates[2] > 9 ? dates[2] : ("0" + dates[2])));

            }

            @Override
            public void onCancel() {

            }
        })

                .setSelectYear(date.get(0) - 1)
                .setSelectMonth(date.get(1) - 1)
                .setSelectDay(date.get(2) - 1);

        builder.setMaxYear(DateUtil.getYear());
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        dateDialog = builder.create();
        dateDialog.show();
    }

    private void showTimePick() {

        if (timeDialog == null) {

            TimePickerDialog.Builder builder = new TimePickerDialog.Builder(this);
            timeDialog = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
                @Override
                public void onTimeSelected(int[] times) {

                    mTextView.setText(times[0] + ":" + times[1]);

                }
            }).create();
        }

        timeDialog.show();

    }


    public void time(View v) {

        showTimePick();

    }

    public void date(View v) {

        showDateDialog(DateUtil.getDateForString("1990-01-01"));


    }

    public void zidingyi(View v) {

        showChooseDialog(list);

    }
}
