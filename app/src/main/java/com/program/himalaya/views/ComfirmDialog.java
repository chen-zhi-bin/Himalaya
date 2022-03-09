package com.program.himalaya.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.program.himalaya.R;

public class ComfirmDialog extends Dialog{

    private View mCancelSub;
    private View mGiveUo;
    private OnDialogActionClickListener mClickListener=null;

    public ComfirmDialog(@NonNull Context context) {
        this(context,0);
    }

    public ComfirmDialog(@NonNull Context context, int themeResId) {
                            //true表示可以点击空白处取消
        this(context, true,null);
    }

    protected ComfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        initView();
        initListener();
    }

    private void initListener() {
        mGiveUo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onGiveUpClick();
                    dismiss();
                }
            }
        });

        mCancelSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onCancelSubClick();
                    dismiss();
                }
            }
        });
    }

    private void initView() {
        mCancelSub = this.findViewById(R.id.dialog_check_box_cancel);
        mGiveUo = this.findViewById(R.id.dialog_check_box_confirm);
    }


    public void setOnDialogActionClickListener(OnDialogActionClickListener listener){
        this.mClickListener =listener;
    }
    public interface OnDialogActionClickListener{
        void onCancelSubClick();
        void onGiveUpClick();
    }
}
