package com.luojiu.maker.generator.file;

import com.luojiu.maker.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 */
public class FileGenerator {

    /**
     * 生成
     *
     * @param model 数据模型
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(Object model) throws TemplateException, IOException {
        String projectPath = System.getProperty("user.dir");
        // 整个项目的根路径，当项目路径为luoxing-generator获取父路径
        File parentFile = new File(projectPath).getParentFile();
        // 输入路径当路径是luoxing-generator时用这个
//        String inputPath = new File(projectPath, "luoxing-generator-demo-projects/acm-template").getAbsolutePath();
        // 输入路径当路径是luoxing-generator-maker时用这个
        String inputPath = new File(parentFile, "luoxing-generator-demo-projects/acm-template").getAbsolutePath();
        String outputPath = projectPath;
        // 生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
        // 生成动态文件当项目目录是luoxing-generator时删去注释
//        String inputDynamicFilePath = projectPath + File.separator + "luoxing-generator-maker/src/main/resources/templates/MainTemplate.java.ftl";
        String inputDynamicFilePath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputDynamicFilePath = outputPath + File.separator + "acm-template/src/com/luojiu/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(inputDynamicFilePath, outputDynamicFilePath, model);
    }

    public static void main(String[] args) throws TemplateException, IOException {
        DataModel dataModel = new DataModel();
        dataModel.setAuthor("yupi");
        dataModel.setLoop(false);
        dataModel.setOutputText("求和结果：");
        doGenerate(dataModel);
    }
}

