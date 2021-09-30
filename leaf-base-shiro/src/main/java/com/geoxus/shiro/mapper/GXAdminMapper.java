package com.geoxus.shiro.mapper;

import com.geoxus.shiro.entities.GXAdminEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface GXAdminMapper extends GXFrameworkBaseMapper<GXAdminEntity> {
}
