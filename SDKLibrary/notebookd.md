# 注意
 
com.zysdk.vulture.clib.adapter.lgrcommon 为listview/gridview/recyclerview 公用的apdapter

# QuickAdapter使用示例代码:

## 单一类型条目示例
    protected QuickAdapter quickAdapter = new QuickAdapter<String>(UiUtils.getContext(),
            Const.DATA.TEST_LIST, R.layout.layout_item_list) {
        @Override
        protected void convert(QuickViewHolder holder, String data, int position) {
            holder.setText(R.id.textview, data);
        }
    };

## 多条目类型

    protected static final int COMMON_TEXT = 0;
    protected static final int COMMON_IMG = 1;
    protected static final int COMMON_IMGTEXT = 2;
    
    protected QuickAdapter mQuickAdapter = new QuickAdapter<HotBean>(UiUtils.getContext(), Const.DATA
            .TEST_HOT_BEAN_LIST, new
            QuickMultiSupport<HotBean>() {
                @Override
                public int getViewTypeCount() {
                    return 3;
                }
    
                @Override
                public int getLayoutId(HotBean data) {
                    int itemViewType = getItemViewType(data);
                    switch (itemViewType) {
                        case COMMON_TEXT:
                            return R.layout.layout_item_list;
                        case COMMON_IMG:
                            return R.layout.layout_item_img_list;
                        case COMMON_IMGTEXT:
                            return R.layout.layout_item_img_text_list;
                    }
                    return R.layout.layout_item_list;
                }
    
                @Override
                public int getItemViewType(HotBean data) {
                    switch (data.getType()) {
                        case 0:
                            return COMMON_TEXT;
                        case 1:
                            return COMMON_IMG;
                        case 2:
                            return COMMON_IMGTEXT;
                    }
                    return COMMON_TEXT;
                }
    
                @Override
                public boolean isSpan(HotBean data) {
                    return false;
                }
            }) {
        @Override
        protected void convert(QuickViewHolder holder, HotBean data, int position) {
            int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case COMMON_TEXT:
                    holder.setText(R.id.textview, data.getTitle());
                    break;
                case COMMON_IMG:
                    GlideApp.with(UiUtils.getContext()).load(data.getImgUrl()).into((ImageView) holder.getView(R.id.imageview));
                    break;
                case COMMON_IMGTEXT:
                    holder.setText(R.id.textview, data.getTitle());
                    holder.setText(R.id.tv_content, data.getContentTxt());
                    GlideApp.with(UiUtils.getContext()).load(data.getImgUrl()).into((ImageView) holder.getView(R.id.imageview));
                    break;
            }
        }
    };
    
    
## CommonToolbar使用
    
    <com.zysdk.vulture.clib.widget.CommonToolbar
        android:id="@+id/common_toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/black"
        app:ToolbarTitle="手机验证"
        app:ToolbarShowEmTitle="true"
        app:ToolbarEmTitleTextColor="@color/white"
        app:ToolbarHomeIconRes="@mipmap/places_ic_clear"
        app:ToolbarShowHomeIcon="true"
        app:ToolbarShowHomeIconSize="@dimen/dp_22"
        app:ToolbarEmTitle="确定"
        /> 