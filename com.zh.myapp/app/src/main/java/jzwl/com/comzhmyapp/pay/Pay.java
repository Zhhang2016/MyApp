package jzwl.com.comzhmyapp.pay;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by zh on 2017/10/11.
 * describe:
 */

public class Pay {
    private static class PayHolder {
        private static Pay pay = new Pay();

    }

    private Pay() {
    }

    public static Pay getInstance() {
        return PayHolder.pay;
    }

}
