package com.example.liangmutian.mypicker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DatePickerDialog extends Dialog {

    private static  int MIN_YEAR = 1900;
    private static  int MAX_YEAR = 2100;
    private Params params;

    public DatePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(DatePickerDialog.Params params) {
        this.params = params;
    }

    public interface OnDateSelectedListener {
         void onDateSelected(int[] dates);
         void onCancel();
    }



    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopYear, loopMonth, loopDay;
        private OnDateSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private final DatePickerDialog.Params params;
        private Integer minYear;
        private Integer maxYear;
        private Integer selectYear;
        private Integer selectMonth;
        private Integer selectDay;
        private Integer minMonth;
        private Integer maxMonth;
        private Integer minDay;
        private Integer maxDay;

        public Builder(Context context) {
            this.context = context;
            params = new DatePickerDialog.Params();
        }

        public Builder setMinYear(int year){
            minYear=year;
            return this;
        }

        public Builder setMaxYear(int year){
            maxYear=year;
            return this;
        }

        public Builder setMinMonth(int month){
            minMonth=month;
            return this;
        }

        public Builder setMaxMonth(int month){
            maxMonth=month;
            return this;
        }

        public Builder setMinDay(int day){
            minDay=day;
            return this;
        }

        public Builder setMaxDay(int day){
            maxDay=day;
            return this;
        }

        public Builder setSelectYear(int year){
            this.selectYear=year;
            return this;
        }

        public Builder setSelectMonth(int month){
            this.selectMonth=month;
            return this;
        }

        public Builder setSelectDay(int day){
            this.selectDay=day;
            return this;
        }

        /**
         * 获取当前选择的日期
         *
         * @return int[]数组形式返回。例[1990,6,15]
         */
        private final int[] getCurrDateValues() {
            int currYear = Integer.parseInt(params.loopYear.getCurrentItemValue());
            int currMonth = Integer.parseInt(params.loopMonth.getCurrentItemValue());
            int currDay = Integer.parseInt(params.loopDay.getCurrentItemValue());
            return new int[]{currYear, currMonth, currDay};
        }

        public Builder setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
            params.callback = onDateSelectedListener;
            return this;
        }


        public DatePickerDialog create() {
            final DatePickerDialog dialog = new DatePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_date, null);

            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    params.callback.onCancel();
                }
            });



            Calendar c = Calendar.getInstance();

            final LoopView loopDay = (LoopView) view.findViewById(R.id.loop_day);
            loopDay.setArrayList(d(1, 30));
            if(selectDay!=null){
                loopDay.setCurrentItem(selectDay);
            }else{
                loopDay.setCurrentItem(c.get(Calendar.DATE));
            }
            //loopDay.setNotLoop();

            final LoopView loopYear = (LoopView) view.findViewById(R.id.loop_year);
            loopYear.setArrayList(d(MIN_YEAR, MAX_YEAR - MIN_YEAR + 1));
            if(selectYear!=null){
                loopYear.setCurrentItem(selectYear-MIN_YEAR+1);
            }else{
                loopYear.setCurrentItem(MAX_YEAR);
            }
            loopYear.setNotLoop();

            final LoopView loopMonth = (LoopView) view.findViewById(R.id.loop_month);
            loopMonth.setArrayList(d(1, 12));
            if(selectMonth!=null){
                loopMonth.setCurrentItem(selectMonth);
            }else{
                loopMonth.setCurrentItem(c.get(Calendar.MONTH));
            }
            loopMonth.setNotLoop();



            final LoopListener maxDaySyncListener = new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    Calendar c = Calendar.getInstance();
                    boolean needFixed=true;
                    if(minYear!=null){
                        if(Integer.parseInt(loopYear.getCurrentItemValue())==minYear ){
                            if(minMonth!=null){
                                if(Integer.parseInt(loopMonth.getCurrentItemValue())<minMonth){
                                    loopMonth.setCurrentItem(minMonth - 1);
                                }
                            }
                        }else if(Integer.parseInt(loopYear.getCurrentItemValue())<minYear){
                            loopYear.setCurrentItem(minYear-MIN_YEAR);
                        }
                    }

                    if(maxYear!=null){
                        if(Integer.parseInt(loopYear.getCurrentItemValue())==maxYear ){
                            if(maxMonth!=null){
                                if(Integer.parseInt(loopMonth.getCurrentItemValue())>maxMonth){
                                    loopMonth.setCurrentItem(maxMonth - 1);
                                }
                            }
                        }else if(Integer.parseInt(loopYear.getCurrentItemValue())>maxYear){
                            loopYear.setCurrentItem(maxYear-MIN_YEAR);
                        }
                    }

                    c.set(Integer.parseInt(loopYear.getCurrentItemValue()), Integer.parseInt(loopMonth.getCurrentItemValue()) - 1, 1);
                    c.roll(Calendar.DATE, false);

                    if(needFixed){
                        int maxDayOfMonth = c.get(Calendar.DATE);
                        int fixedCurr = loopDay.getCurrentItem();
                        loopDay.setArrayList(d(1, maxDayOfMonth));
                        // 修正被选中的日期最大值
                        if (fixedCurr > maxDayOfMonth) fixedCurr = maxDayOfMonth - 1;
                        loopDay.setCurrentItem(fixedCurr);
                    }
                }
            };

            final LoopListener dayLoopListener=new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    if(minYear!=null && minMonth!=null && minDay!=null
                            && Integer.parseInt(loopYear.getCurrentItemValue())==minYear
                            && Integer.parseInt(loopMonth.getCurrentItemValue())==minMonth
                            && Integer.parseInt(loopDay.getCurrentItemValue())<minDay
                            ){
                        loopDay.setCurrentItem(minDay-1);
                    }

                    if(maxYear!=null && maxMonth!=null && maxDay!=null
                            && Integer.parseInt(loopYear.getCurrentItemValue())==maxYear
                            && Integer.parseInt(loopMonth.getCurrentItemValue())==maxMonth
                            && Integer.parseInt(loopDay.getCurrentItemValue())>maxDay
                            ){
                        loopDay.setCurrentItem(maxDay-1);
                    }
                }
            };
            loopYear.setListener(maxDaySyncListener);
            loopMonth.setListener(maxDaySyncListener);
            loopDay.setListener(dayLoopListener);

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDateSelected(getCurrDateValues());
                }
            });

            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
            win.setWindowAnimations(R.style.Animation_Bottom_Rising);

            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(params.canCancel);
            dialog.setCancelable(params.canCancel);

            params.loopYear = loopYear;
            params.loopMonth = loopMonth;
            params.loopDay = loopDay;
            dialog.setParams(params);

            return dialog;
        }

        /**
         * 将数字传化为集合，并且补充0
         *
         * @param startNum 数字起点
         * @param count    数字个数
         * @return
         */
        private static List<String> d(int startNum, int count) {
            String[] values = new String[count];
            for (int i = startNum; i < startNum + count; i++) {
                String tempValue = (i < 10 ? "0" : "") + i;
                values[i - startNum] = tempValue;
            }
            return Arrays.asList(values);
        }

    }
}
