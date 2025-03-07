package com.luojiu.web.model.dto.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.luojiu.web.common.PageRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.luojiu.web.meta.Meta;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GeneratorQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * id
     */

    private Long notId;
    /**
     * 搜索词
     */
    private String searchText;
    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 基础包
     */
    private String basePackage;

    /**
     * 版本
     */
    private String version;

    /**
     * 作者
     */
    private String author;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;
    /**
     * 至少有一个标签
     */
    private List<String> orTags;
    /**
     * 代码生成器产物路径
     */
    private String distPath;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}