package com.geoxus.commons.constant;

import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.common.util.GXBaseCommonUtil;
import com.geoxus.core.common.util.GXFrameworkCommonUtils;

public class GXDataDictConstant {
    @GXFieldComment(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "id";

    @GXFieldComment(zhDesc = "表名")
    public static final String TABLE_NAME = "data_dict";

    @GXFieldComment(zhDesc = "数据表别名")
    public static final String TABLE_ALIAS_NAME = "data_dict";

    @GXFieldComment("模型在数据库中的表述[一般跟数据表的名字相同]")
    public static final String MODEL_IDENTIFICATION_VALUE = "data_dict";

    @GXFieldComment("数据源")
    public static final String DATASOURCE = "framework";

    @GXFieldComment("核心模型ID")
    public static final int CORE_MODEL_ID = GXBaseCommonUtil.getEnvironmentValue("app.com.geoxus.core-model-id.data_dict", Integer.class);

    private GXDataDictConstant() {

    }
}
