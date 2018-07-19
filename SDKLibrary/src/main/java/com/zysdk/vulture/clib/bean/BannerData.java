package com.zysdk.vulture.clib.bean;

import java.io.Serializable;

public class BannerData implements Serializable {
    private static final long serialVersionUID = 5010155938098565677L;

    /**
     * 图片url
     */
    private String imgUrl;

    /**
     * 描述信息
     */
    private String desc;

    public BannerData() {
    }

    public BannerData(String imgUrl, String desc) {
        this.imgUrl = imgUrl;
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BannerData{" +
                "imgUrl='" + imgUrl + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
