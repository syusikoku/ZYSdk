package com.zysdk.vulture.clib.mvp.model;

import com.zysdk.vulture.clib.utils.LogListener;
import com.zysdk.vulture.clib.utils.RetorfitUtils;

/**
 * Created by zhiyang on 2018/4/11.
 */

public abstract class BaseModel implements LogListener {
    /**
     * 创建api service
     */
    public <T> T createApiService(String url, Class<T> service) {
        return RetorfitUtils.create(url, service);
    }

}
