package jzwl.com.comzhmyapp.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jzwl.com.comzhmyapp.R;


/**
 * @version V_5.0.0
 * @date 2016年02月25日
 * @description 确定dialog
 */
public class CustomSureDialog {
    private static CustomSureDialog instance;
    private Dialog alertDialog;

    private CustomSureDialog(){

    }

    public static CustomSureDialog getInstance() {
        if (instance == null) {
            instance = new CustomSureDialog();
        }
        return instance;
    }

    public Dialog createAlertDialog(Context context, String content, String btncontent, View.OnClickListener listener) {
        alertDialog = new Dialog(context, R.style.textDialogStyle);// 创建自定义样式dialog
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.layout_sure_dialog, null);// 得到加载view
            TextView tv_content = (TextView) v.findViewById(R.id.content);
            Button btnSure = (Button) v.findViewById(R.id.btnSure);
            tv_content.setText(content);
            btnSure.setText(btncontent);

            btnSure.setOnClickListener(listener);

            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);// 不可以用“返回键”取消
            alertDialog.setContentView(v);// 设置布局
        }catch (Exception e){
            e.printStackTrace();
        }
        return alertDialog;
    }

    public void cancelAlertDialog() {
        if (alertDialog != null) {
            alertDialog.cancel();
        }
    }

    public boolean isAlertDialog() {
        boolean flag;
        flag = alertDialog != null && alertDialog.isShowing();
        return flag;
    }

}
