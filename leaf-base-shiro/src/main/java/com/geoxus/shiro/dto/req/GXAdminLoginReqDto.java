package com.geoxus.shiro.dto.req;

import com.geoxus.common.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXAdminLoginReqDto extends GXBaseDto {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码必填")
    @NotEmpty(message = "密码必填")
    private String password;

    @Override
    protected void afterRepair() {
        System.out.println("对数据进行第二次修复");
    }

    @Override
    public void customizeProcess() {
        System.out.println("自定义处理");
    }

    @Override
    protected void verify() {
        System.out.println("对数据进行修复");
    }

    @Override
    protected void beforeRepair() {
        System.out.println("对数据进行第一次修复");
    }
}
