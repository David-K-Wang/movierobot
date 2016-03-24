package com.wk.common.vo;

import java.util.HashMap;
import java.util.Map;

public class BaseVo {
    Map<String, String> error = new HashMap<String, String>();

    Map<String, Object> result = new HashMap<String, Object>();

    public Map<String, Object> getResult() {
        return result;
    }

    public boolean isSuccess() {
        return error.isEmpty();
    }

    public void putResultItem(String key, Object value) {
        this.result.put(key, value);
    }

    public void putErrorMsg(String errorKey, String errorMsg) {
        this.error.put(errorKey, errorMsg);
    }
}
