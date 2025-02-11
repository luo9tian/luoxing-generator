package ${basePackage}.generator;
import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;
import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.IOException;

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
    public static void doGenerate(Object model) throws TemplateException, IOException {
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
    <#list fileConfig.files as fileInfo>
        inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
        outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
        <#if fileInfo.generateType=="static">
        // 生成静态文件
        StaticGenerator.copyFilesByHutool(inputPath, outputPath);
        <#else>
        // 生成动态文件
        DynamicGenerator.doGenerate(inputPath, outputPath, model);
        </#if>
    </#list>
    }

}

