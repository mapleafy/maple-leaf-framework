package com.geoxus.shiro.dto.req;

import com.geoxus.common.annotation.GXMergeSingleField;
import com.geoxus.common.annotation.GXValidateExtData;
import com.geoxus.common.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXAdminConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXAdminReqDto extends GXBaseDto {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实名字
     */
    private String realName;

    /**
     * 账号状态
     */
    private Integer status;

    /**
     * 是否超级管理员
     * 1:是
     * 2:否
     */
    private Integer superAdmin = 2;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 扩展数据
     */
    @GXValidateExtData(tableName = GXAdminConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleField(fieldName = "author")
    private String author;
}
