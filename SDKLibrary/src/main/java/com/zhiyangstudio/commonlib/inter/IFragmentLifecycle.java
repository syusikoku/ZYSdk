package com.zhiyangstudio.commonlib.inter;

import android.os.Bundle;

/**
 * Created by example on 2018/4/18.
 */

public interface IFragmentLifecycle extends ILifecycle {
    void initArguments(Bundle bundle);
}