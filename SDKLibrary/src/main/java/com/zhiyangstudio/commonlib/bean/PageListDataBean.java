package com.zhiyangstudio.commonlib.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyang on 2018/4/11.
 * 分页数据的bean
 */

public class PageListDataBean<T> {
    private int curPage;

    private int offset;

    // 数据是否加载完毕
    private boolean over;

    private int pageCount;

    private int size;

    private int total;

    private List<T> datas = new ArrayList<>();

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
