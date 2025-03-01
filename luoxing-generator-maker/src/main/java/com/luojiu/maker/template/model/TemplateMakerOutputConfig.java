package com.luojiu.maker.template.model;

import lombok.Data;

@Data
public class TemplateMakerOutputConfig {
    /**
     * 从未分组的文件中移除分组中重复的文件
     */
    private boolean removeGroupFilesFromRoot=true;
}
