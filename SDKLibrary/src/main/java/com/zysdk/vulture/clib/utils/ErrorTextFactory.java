package com.zysdk.vulture.clib.utils;

import com.zysdk.vulture.clib.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 错误信息产生工厂
 *
 * @author zzg
 */
public class ErrorTextFactory {
    public static String createErrorMsgStr(Throwable e) {
        return createErrorMsgStr(e, false);
    }

    public static String createErrorMsgStr(Throwable e, boolean isShowReason) {
        String error = ResourceUtils.getStr(R.string.unknown_error);
        if (e instanceof ConnectException) {
            error = ResourceUtils.getStr(R.string.unable_to_access_server_interface);
        } else if (e instanceof UnknownHostException) {
            error = ResourceUtils.getStr(R.string.unknown_domain_name);
        } else if (e instanceof SocketTimeoutException) {
            error = ResourceUtils.getStr(R.string.connection_timeout);
        }
        if (isShowReason) {
            error = error + e.toString();
        }
        return error;
    }
}
