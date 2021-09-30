package com.geoxus.shiro.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.shiro.dao.GXAdminRoleDao;
import com.geoxus.shiro.entities.GXAdminRoleEntity;
import com.geoxus.shiro.mapper.GXAdminRoleMapper;
import com.geoxus.shiro.service.GXAdminRoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXAdminRoleServiceImpl extends com.geoxus.core.service.impl.GXDBBaseServiceImpl<GXAdminRoleEntity, GXAdminRoleMapper, GXAdminRoleDao> implements GXAdminRoleService, com.geoxus.core.service.GXValidateDBExistsService, com.geoxus.core.service.GXValidateDBUniqueService, com.geoxus.core.service.GXDBBaseService<GXAdminRoleEntity, GXAdminRoleMapper, GXAdminRoleDao> {
    @Override
    public Set<String> getAdminRoles(Long adminId) {
        final Dict condition = Dict.create().set("admin_id", adminId);
        return getBaseMapper().getAdminRoles(condition);
    }

    public long create(GXAdminRoleEntity target, Dict param) {
        baseDao.save(target);
        return target.getId();
    }
}
