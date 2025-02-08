package com.luojiu.model;

import lombok.Data;

@Data
public class MainTemplateConfig {
    /**
     * 作者名称
     */
    private String author;
    /**
     * 输出信息
     */
    private String outputText;
    /**
     * 是否需要循环
     */
    private boolean loop;
}
