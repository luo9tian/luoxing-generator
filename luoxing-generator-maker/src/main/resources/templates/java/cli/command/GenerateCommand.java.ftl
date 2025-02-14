package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine;
import java.util.concurrent.Callable;
<#macro generateOption modelInfo indent>
<#if modelInfo.description??>
${indent}/**
${indent}* ${modelInfo.description}
${indent}*/
</#if>
${indent}@Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}",</#if>"--${modelInfo.fieldName}"},<#if modelInfo.description??>description = "${modelInfo.description}",</#if>arity = "0..1",interactive = true,echo = true)
${indent}private ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;
</#macro>
<#--生成命令调用-->

<#macro generateCommand indent modelInfo>
${indent}System.out.println("输入${modelInfo.groupName}配置：");
${indent}CommandLine commandLine= new CommandLine(${modelInfo.type}Command.class);
${indent}commandLine.execute(${modelInfo.allArgsStr});
</#macro>

@Data
@Command(name = "generate",mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable {
<#list modelConfig.models as modelInfo>
<#if modelInfo.groupKey??>
    /**
    *${modelInfo.groupName}
    */
    static DataModel.${modelInfo.type} ${modelInfo.groupKey} =new DataModel.${modelInfo.type}();
    @Command(name="${modelInfo.groupKey}")
    @Data
    public static class ${modelInfo.type}Command implements Runnable{
        <#list modelInfo.models as modelInfo>
            <@generateOption modelInfo=modelInfo indent="        "/>
        </#list>
        @Override
        public void run(){
        <#list modelInfo.models as subModelInfo>
            ${modelInfo.groupKey}.${subModelInfo.fieldName}=${subModelInfo.fieldName};
        </#list>
        }
    }
<#else >
<@generateOption modelInfo=modelInfo indent="    "/>
</#if>
</#list>

    @Override
    public Object call() throws Exception {
        <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
        <#if modelInfo.condition??>
        if(${modelInfo.condition}){
        <@generateCommand modelInfo=modelInfo indent="            "/>
        }
        <#else>
        <@generateCommand modelInfo=modelInfo indent="        "/>
        </#if>
        </#if>
        </#list>
        DataModel dataModel =new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
        dataModel.${modelInfo.groupKey}=${modelInfo.groupKey};
        </#if>
        </#list>
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
