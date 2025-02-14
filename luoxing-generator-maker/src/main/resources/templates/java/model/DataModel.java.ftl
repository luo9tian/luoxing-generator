package ${basePackage}.model;

import lombok.Data;
<#macro generateModel modelInfo indent>
<#if modelInfo.description??>
${indent}/**
${indent}* ${modelInfo.description}
${indent}*/
</#if>
${indent}public ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;

</#macro>
@Data
public class DataModel {
<#list modelConfig.models as modelInfo>

<#if modelInfo.groupKey??>
    <#if modelInfo.groupName??>
    /**
    * ${modelInfo.groupName}
    */
    </#if>
    public ${modelInfo.type} ${modelInfo.groupKey}=new ${modelInfo.type}();

    <#if modelInfo.description??>
    /**
    * ${modelInfo.description}
    */
    </#if>
    @Data
    public static class ${modelInfo.type}{
    <#list modelInfo.models as modelInfo>
     <@generateModel modelInfo=modelInfo indent="        "/>
    </#list>
    }
<#else>

    <@generateModel modelInfo=modelInfo indent="    "/>

</#if>

</#list>
}
