package com.geoxus.commons.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.commons.services.GXDataDictService;
import com.geoxus.common.util.GXResultUtil;
import com.geoxus.common.pojo.response.GXPagination;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/generate/dict")
public class GXDataDictController {
    @Resource
    private GXDataDictService dataDictService;

    /**
     * 获取所有区域树
     *
     * @return GXResultUtils
     */
    @PostMapping("/list-or-search")
    public GXResultUtil<GXPagination<Dict>> listOrSearchPage(@RequestBody Dict condition) {
        final GXPagination<Dict> pagination = dataDictService.listOrSearchPage(condition, Dict.class);
        return GXResultUtil.ok(pagination);
    }
}