package com.luojiu.maker.template.model;

import com.luojiu.maker.meta.Meta;
import lombok.Data;

@Data
public class TemplateMakerConfig {
    /**
     * @param newMeta
     * @param originProjectPath
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param id
     * @return
     */

    private Meta meta=new Meta();
    private String originProjectPath;
    private Long id;
    private TemplateMakerFileConfig fileConfig=new TemplateMakerFileConfig();
    private TemplateMakerModelConfig modelConfig=new TemplateMakerModelConfig();
    private TemplateMakerOutputConfig outputConfig=new TemplateMakerOutputConfig();
}
