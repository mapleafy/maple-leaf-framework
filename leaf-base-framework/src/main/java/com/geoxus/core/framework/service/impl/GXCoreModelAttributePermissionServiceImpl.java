package com.geoxus.core.framework.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.common.util.GXBaseCommonUtil;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.core.common.service.GXSessionService;
import com.geoxus.core.datasource.annotation.GXDataSource;
import com.geoxus.core.framework.constant.GXFrameWorkCommonConstant;
import com.geoxus.core.framework.dao.GXCoreModelAttributesPermissionDao;
import com.geoxus.core.framework.entity.GXCoreModelAttributesPermissionEntity;
import com.geoxus.core.framework.mapper.GXCoreModelAttributesPermissionMapper;
import com.geoxus.core.framework.service.GXCoreModelAttributePermissionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@GXDataSource("framework")
public class GXCoreModelAttributePermissionServiceImpl extends GXBaseServiceImpl<GXCoreModelAttributesPermissionEntity, GXCoreModelAttributesPermissionMapper, GXCoreModelAttributesPermissionDao> implements GXCoreModelAttributePermissionService {
    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #coreModelId")
    public Dict getModelAttributePermissionByCoreModelId(int coreModelId, Dict param) {
        GXSessionService sessionService = GXSpringContextUtil.getBean(GXSessionService.class);
        final List<Dict> attributes = baseMapper.getModelAttributePermissionByCondition(Dict.create().set(GXFrameWorkCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME, coreModelId));
        final Dict data = Dict.create();
        final Dict jsonFieldDict = Dict.create();
        final Dict dbFieldDict = Dict.create();
        List<String> roles = CollUtil.newArrayList();
        final List<String> users = CollUtil.newArrayList();
        final Long currentAdminId = Objects.requireNonNull(sessionService).currentSessionUserId(); // GXCommonUtils.getCurrentSessionUserId();
        final Long superAdminId = GXBaseCommonUtil.getEnvironmentValue("super.admin.id", Long.class);
        if (superAdminId > 0 && currentAdminId.equals(superAdminId)) {
            return Dict.create();
        }
        if (currentAdminId > 0) {
            users.add(currentAdminId.toString());
            // TODO 通过currentAdminId获取当前用户的角色列表
            // Objects.requireNonNull(GXSpringContextUtils.getBean(GXSAdminHasRolesService.class)).getAdminRoles(currentAdminId);
            final Dict adminRoles = sessionService.adminRoles(currentAdminId); //Dict.create();
            roles = adminRoles.keySet().stream().map(r -> Convert.toStr(r, "0")).collect(Collectors.toList());
        }
        for (Dict dict : attributes) {
            final String dbFieldName = dict.getStr("db_field_name");
            final List<String> tmpRoles = CharSequenceUtil.split(dict.getStr("roles"), ",");
            final List<String> tmpUsers = CharSequenceUtil.split(dict.getStr("users"), ",");
            if (!CollUtil.containsAny(tmpUsers, users) && !CollUtil.containsAny(tmpRoles, roles)) {
                continue;
            }
            if (CharSequenceUtil.contains(dbFieldName, "::")) {
                final String[] strings = CharSequenceUtil.split(dbFieldName, "::").toArray(new String[0]);
                final Dict convertDict = Convert.convert(Dict.class, jsonFieldDict.getOrDefault(strings[0], Dict.create()));
                convertDict.set(CharSequenceUtil.format("{}", strings[1]), CharSequenceUtil.format("{}", String.join("::", strings)));
                jsonFieldDict.set(strings[0], convertDict);
            }
            dbFieldDict.set(dbFieldName, dbFieldName);
        }
        return data.set("json_field", jsonFieldDict).set("db_field", dbFieldDict);
    }
}
