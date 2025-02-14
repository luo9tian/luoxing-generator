package ${basePackage}.generator;
import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;
import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.IOException;
<#macro generateFile indent fileInfo>
${indent}inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
${indent}outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
<#if fileInfo.generateType == "static">
${indent}StaticGenerator.copyFilesByHutool(inputPath, outputPath);
<#else>
${indent}DynamicGenerator.doGenerate(inputPath, outputPath, model);
</#if>
</#macro>

/**
 * 核心生成器
 */
public class MainGenerator {

    /**
     * 生成
     *
     * @param model 数据模型
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(DataModel model) throws TemplateException, IOException {
        //项目的根路径
        String inputRootPath = "${fileConfig.inputRootPath}";
        //生成输出的根路径
        String outputRootPath = "${fileConfig.outputRootPath}";
        if(!FileUtil.exist(outputRootPath)){
            FileUtil.mkdir(outputRootPath);
        }
        //根据文件的配置循环生成动态静态文件
        String inputPath;
        String outputPath;
    <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
        <#list modelInfo.models as subModelInfo>
        ${subModelInfo.type} ${subModelInfo.fieldName} = model.${modelInfo.groupKey}.${subModelInfo.fieldName};
        </#list>
        <#else>
        ${modelInfo.type} ${modelInfo.fieldName} = model.${modelInfo.fieldName};
        </#if>

    </#list>
    <#list fileConfig.files as fileInfo>
    <#if fileInfo.groupKey??>
    <#if fileInfo.condition??>
        if(${fileInfo.condition}){
        <#list fileInfo.files as fileInfo>
            <@generateFile fileInfo=fileInfo indent="            "/>
        </#list>
        }
    <#else>
        <#list fileInfo.files as fileInfo>
            <@generateFile fileInfo=fileInfo indent="        "/>
        </#list>
    </#if>
    <#else>
    <#if fileInfo.condition??>
        if(${fileInfo.condition}){
        <@generateFile fileInfo=fileInfo indent="            "/>
        }
    <#else>
        <@generateFile fileInfo=fileInfo indent="        "/>
    </#if>
    </#if>
    </#list>
    }

}



<#--<#if fileInfo.condition??>-->
<#--    if(${fileInfo.condition}){-->
<#--    inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();-->
<#--    outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();-->
<#--    <#if fileInfo.generateType=="static">-->
<#--        // 生成静态文件-->
<#--        StaticGenerator.copyFilesByHutool(inputPath, outputPath);-->
<#--    <#else>-->
<#--        // 生成动态文件-->
<#--        DynamicGenerator.doGenerate(inputPath, outputPath, model);-->
<#--    </#if>-->
<#--    }-->
<#--<#else>-->
<#--    inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();-->
<#--    outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();-->
<#--    <#if fileInfo.generateType=="static">-->
<#--        // 生成静态文件-->
<#--        StaticGenerator.copyFilesByHutool(inputPath, outputPath);-->
<#--    <#else>-->
<#--        // 生成动态文件-->
<#--        DynamicGenerator.doGenerate(inputPath, outputPath, model);-->
<#--    </#if>-->
<#--</#if>-->