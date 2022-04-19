package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.Set;
import java.util.stream.Collectors;

public class GXConditionStrNotIn extends GXCondition<String> {
    public GXConditionStrNotIn(String tableNameAlias, String fieldName, Set<String> value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "not in";
    }

    @Override
    public String getFieldValue() {
        String str = ((Set<String>) value).stream().map(v -> CharSequenceUtil.format("'{}'", v)).collect(Collectors.joining(","));
        return CharSequenceUtil.format("({})", str);
    }
}
