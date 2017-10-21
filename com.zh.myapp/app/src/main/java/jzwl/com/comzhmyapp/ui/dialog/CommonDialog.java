package jzwl.com.comzhmyapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import jzwl.com.comzhmyapp.R;


/**
 * 创建日期：2017/10/21
 * 描述:  新项目中共用的一些Dialog弹窗。
 *
 * @author: zhaoh
 */
public class CommonDialog {

    private Dialog dialog;

    private static CommonDialog instance = null;

    private CommonDialog() {

    }

    public static CommonDialog getInstance() {
        if (instance == null) {
            instance = new CommonDialog();
        }
        return instance;
    }

    /**
     * @param context    上下文
     * @param content    标题
     * @param message    消息内容
     * @param confirmRes 确认的资源图片
     * @param canlelRes  取消的资源图片
     * @param listener   点击的回调监听
     * @return
     */
    public Dialog createDiaog(Context context, String content, String message, int confirmRes, int canlelRes, CommonDialogClickListener listener) {
//        dialog = new Dialog(context, R.style.common_textDialogStyle);
        dialog = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        try {
            View view = inflater.inflate(R.layout.dialog_common, null);
            TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
            TextView tvMessage = (TextView) view.findViewById(R.id.tv_message);
            ImageView ivConfirm = (ImageView) view.findViewById(R.id.iv_confrim);
            ImageView ivCancel = (ImageView) view.findViewById(R.id.iv_cancel);
            if (!TextUtils.isEmpty(content)) {
                tvContent.setText(content);
            } else {
                tvContent.setText("");
            }
            if (!TextUtils.isEmpty(message)) {
                tvMessage.setText(message);
            } else {
                tvMessage.setText("");
            }
            ivConfirm.setImageResource(confirmRes);
            ivConfirm.setOnClickListener(v -> {
                if (listener != null) {
                    listener.confrim();
                }
            });
            ivCancel.setImageResource(canlelRes);
            ivCancel.setOnClickListener(v -> {
                if (listener != null) {
                    listener.cancle();
                }
            });
            dialog.setContentView(view);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dialog;
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void cancel() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

}
