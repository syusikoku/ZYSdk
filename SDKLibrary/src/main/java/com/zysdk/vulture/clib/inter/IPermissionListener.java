package com.zysdk.vulture.clib.inter;

import com.zysdk.vulture.clib.utils.LogListener;

public interface IPermissionListener extends LogListener {
    void onGrant(int code);

    void onDeny(int code);
}