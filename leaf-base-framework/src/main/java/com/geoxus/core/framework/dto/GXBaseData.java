package com.geoxus.core.framework.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class GXBaseData implements Serializable {
    private static final long serialVersionUID = -8579692140225188157L;

    /**
     * @author britton
     * 在验证请求参数之前进行数据修复(自动填充一些信息)
     */
    protected void beforeRepair() {
    }

    /**
     * @author britton
     * 对请求参数进行补充校验
     */
    protected void verify() {
    }

    /**
     * @author britton
     * 参数
     * 验证请求参数之后进行数据修复(自动填充一些信息)
     */
    protected void afterRepair() {
    }

    /**
     * @author britton
     * 调用自定义的方法进行参数的处理
     */
    public void customizeProcess() {
        this.beforeRepair();
        this.verify();
        this.afterRepair();
    }
}