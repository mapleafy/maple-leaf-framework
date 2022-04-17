package cn.maple.core.framework.dto.inner.condition;

public class GXConditionEQ extends GXCondition<Number> {
    public GXConditionEQ(String tableNameAlias, String fieldName, Number value) {
        super(tableNameAlias, fieldName, () -> value);
    }

    @Override
    String getOp() {
        return "=";
    }
}
