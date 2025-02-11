package com.luojiu.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.luojiu.maker.meta.enums.FileGenerateTypeEnum;
import com.luojiu.maker.meta.enums.FileTypeEnum;
import com.luojiu.maker.meta.enums.ModelTypeEnum;
import freemarker.template.utility.StringUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MetaValidator {
    public static void doValidAndFill(Meta meta) {
        validAndFillMetaRoot(meta);

        validAndFillFileConfig(meta);
        //验证modelConfig
        validAndFillModelConfig(meta);
    }

    private static void validAndFillModelConfig(Meta meta) {
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        List<Meta.ModelConfig.ModelInfo> models = modelConfig.getModels();
        if (CollectionUtil.isEmpty(models)) {
            return;
        }
        for (Meta.ModelConfig.ModelInfo modelInfo:models) {
            //字段名必填
            String fieldName = modelInfo.getFieldName();
            if(StrUtil.isBlank(fieldName)){
                throw new MetaException("未填写 models[].fieldName");
            }
            String fieldType = modelInfo.getType();
            if(StrUtil.isBlank(fieldType)){
                modelInfo.setType(ModelTypeEnum.STRING.getValue());
            }
        }
    }

    private static void validAndFillFileConfig(Meta meta) {
        //验证fileConfig
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        //sourceRootPath必填
        String sourceRootPath = fileConfig.getSourceRootPath();
        if(StrUtil.isBlank(sourceRootPath)){
            throw new MetaException("未填写 sourceRootPath");
        }
        String inputRootPath = fileConfig.getInputRootPath();
        String defaultInputRootPath=".source/"+FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
        if (StrUtil.isBlank(inputRootPath)) {
            fileConfig.setInputRootPath(defaultInputRootPath);
        }
        String outputRootPath = fileConfig.getOutputRootPath();
        String defaultOutputRootPath="autoGenerator";
        if(StrUtil.isBlank(outputRootPath)){
            fileConfig.setOutputRootPath(defaultOutputRootPath);
        }
        String fileConfigType= fileConfig.getType();
        String defaultFileConfigType = FileTypeEnum.DIR.getValue();
        if(StrUtil.isEmpty(fileConfigType)){
            fileConfig.setType(defaultFileConfigType);
        }
        List<Meta.FileConfig.FileInfo> files = fileConfig.getFiles();
        if (CollectionUtil.isEmpty(files)) {
            return;
        }
        for (Meta.FileConfig.FileInfo fileInfo:files) {
            //输入路径必填
            String inputPath = fileInfo.getInputPath();
            if(StrUtil.isBlank(inputPath)){
                throw new MetaException("未填写 files[].inputPath");
            }
            String outputPath = fileInfo.getOutputPath();
            String defaultOutputPath=inputPath;
            if(StrUtil.isBlank(outputPath)){
                fileInfo.setOutputPath(defaultOutputPath);
            }
            String fileType = fileInfo.getType();
            if(StrUtil.isBlank(fileType)){
                if(StrUtil.isEmpty(FileUtil.getSuffix(inputPath))){
                    fileInfo.setType(FileTypeEnum.DIR.getValue());
                }
                else {
                    fileInfo.setType(FileTypeEnum.FILE.getValue());
                }
            }
            String generateType = fileInfo.getGenerateType();
            if(StrUtil.isBlank(generateType)){
                if(inputPath.endsWith("ftl")){
                    fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
                }
                else{
                    fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
                }
            }
        }
    }

    private static void validAndFillMetaRoot(Meta meta) {
        //校验并填充默认值
        String name =StrUtil.blankToDefault(meta.getName(),"my-generator");
        String description=StrUtil.emptyToDefault(meta.getDescription(),"我的模板代码生成器");
        String author= StrUtil.emptyToDefault(meta.getAuthor(),"xxx");
        String basePackage= StrUtil.blankToDefault(meta.getBasePackage(),"com.luojiu");
        String version= StrUtil.emptyToDefault(meta.getVersion(),"1.0");
        String createTime =StrUtil.emptyToDefault(meta.getCreateTime(),DateUtil.now());
        meta.setName(name);
        meta.setDescription(description);
        meta.setAuthor(author);
        meta.setBasePackage(basePackage);
        meta.setVersion(version);
        meta.setCreateTime(createTime);

    }
}

