package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

public class GXConditionLikeFull extends GXCondition<String> {
    public GXConditionLikeFull(String tableNameAlias, String fieldName, String value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "like";
    }

    @Override
    public String getFieldValue() {
        if (CharSequenceUtil.startWith(value.toString(), '\'') && CharSequenceUtil.endWith(value.toString(), '\'')) {
            return value.toString();
        }
        return CharSequenceUtil.format("'%{}%'", value);
    }
}
