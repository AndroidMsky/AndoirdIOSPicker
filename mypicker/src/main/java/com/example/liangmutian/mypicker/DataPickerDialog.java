package com.example.liangmutian.mypicker;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DataPickerDialog extends Dialog {

    private Params params;

    public DataPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(DataPickerDialog.Params params) {
        this.params = params;
    }


    public void setSelection(String itemValue) {
        if (params.dataList.size() > 0) {
            int idx = params.dataList.indexOf(itemValue);
            if (idx >= 0) {
                params.initSelection = idx;
                params.loopData.setCurrentItem(params.initSelection);
            }
        }
    }

    public interface OnDataSelectedListener {
        void onDataSelected(String itemValue, int position);
        void onCancel();
    }

    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopData;
        private String title;
        private String unit;
        private int initSelection;
        private OnDataSelectedListener callback;
        private final List<String> dataList = new ArrayList<>();
    }

    public static class Builder {
        private final Context context;
        private final DataPickerDialog.Params params;

        public Builder(Context context) {
            this.context = context;
            params = new DataPickerDialog.Params();
        }

        private final String getCurrDateValue() {
            return params.loopData.getCurrentItemValue();
        }

        public Builder setData(List<String> dataList) {
            params.dataList.clear();
            params.dataList.addAll(dataList);
            return this;
        }

        public Builder setTitle(String title) {
            params.title = title;
            return this;
        }

        public Builder setUnit(String unit) {
            params.unit = unit;
            return this;
        }

        public Builder setSelection(int selection) {
            params.initSelection = selection;
            return this;
        }

        public Builder setOnDataSelectedListener(OnDataSelectedListener onDataSelectedListener) {
            params.callback = onDataSelectedListener;
            return this;
        }


        public DataPickerDialog create() {
            final DataPickerDialog dialog = new DataPickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_data, null);

            if (!TextUtils.isEmpty(params.title)) {
                TextView txTitle = (TextView) view.findViewById(R.id.tx_title);
                txTitle.setText(params.title);
                txTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        params.callback.onCancel();
                    }
                });
            }
            if (!TextUtils.isEmpty(params.unit)) {
                TextView txUnit = (TextView) view.findViewById(R.id.tx_unit);
                txUnit.setText(params.unit);
            }

            final LoopView loopData = (LoopView) view.findViewById(R.id.loop_data);
            loopData.setArrayList(params.dataList);
            loopData.setNotLoop();
            if (params.dataList.size() > 0) loopData.setCurrentItem(params.initSelection);
            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDataSelected(getCurrDateValue(),loopData.getCurrentItem());
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

            params.loopData = loopData;
            dialog.setParams(params);

            return dialog;
        }
    }
}
