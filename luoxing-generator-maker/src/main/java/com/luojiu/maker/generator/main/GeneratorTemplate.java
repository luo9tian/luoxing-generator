package com.luojiu.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.luojiu.maker.generator.JarGenerator;
import com.luojiu.maker.generator.ScriptGenerator;
import com.luojiu.maker.generator.file.DynamicFileGenerator;
import com.luojiu.maker.meta.Meta;
import com.luojiu.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class GeneratorTemplate {
    public void doGenerate() throws TemplateException, IOException, InterruptedException {
        Meta meta=MetaManager.getMetaObject();
        String projectPath=System.getProperty("user.dir");
        String outputParentPath=projectPath+ File.separator+"autoGenerator"+File.separator+meta.getName();
        if(!FileUtil.exist(outputParentPath)){
            FileUtil.mkdir(outputParentPath);
        }
        //将原项目复制到生成目录下
        String sourceOutputPath = copySource(meta, outputParentPath);
        //读取resources目录
        generateCode(meta, outputParentPath);
        String outputPath;
        //生成jar
        String jarPath = bulidJar(meta, outputParentPath);
        //封装脚本
        String shellOutputPath = buildScript(outputParentPath, jarPath);
        //精简版本
        buildDest(outputParentPath, sourceOutputPath, jarPath, shellOutputPath);
    }

    protected void buildDest(String outputParentPath, String sourceOutputPath, String jarPath, String shellOutputPath) {
        String destOutputPath= outputParentPath +"-dest";
        String targetOutputPath=destOutputPath+File.separator+"target";
        FileUtil.mkdir(targetOutputPath);
        String jarOutputPath= outputParentPath +File.separator+ jarPath;
        //复制原项目
        FileUtil.copy(sourceOutputPath,destOutputPath,true);
        //复制jar
        FileUtil.copy(jarOutputPath,targetOutputPath,true);
        //复制脚本
        FileUtil.copy(shellOutputPath,destOutputPath,true);
    }

    protected String buildScript(String outputParentPath, String jarPath) throws IOException {
        String outputPath;
        String shellOutputPath= outputParentPath +File.separator+"generator.bat";
        outputPath= outputParentPath +File.separator+"generator";
        ScriptGenerator.doGenerate(outputPath, jarPath);
        return shellOutputPath;
    }

    protected String bulidJar(Meta meta, String outputParentPath) throws IOException, InterruptedException {
        String jarName= meta.getName()+"-"+ meta.getVersion()+"-jar-with-dependencies.jar";
        String jarPath="target/"+jarName;
        JarGenerator.doGenerate(outputParentPath);
        return jarPath;
    }

    protected void generateCode(Meta meta, String outputParentPath) throws IOException, TemplateException {
        ClassPathResource classPathResource =new ClassPathResource("");
        String basePackage= meta.getBasePackage().replace(".",File.separator);
        String inputPath;
        String outputPath;
        //model生成
        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/java/model/DataModel.java.ftl";
        outputPath= outputParentPath +File.separator+"src/main/java"+File.separator+basePackage+File.separator+"model/DataModel.java";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);

        //main生成
        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/java/Main.java.ftl";
        outputPath= outputParentPath +File.separator+"src/main/java"+File.separator+basePackage+File.separator+"Main.java";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);
        //cli.command生成
        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/java/cli/command/GenerateCommand.java.ftl";
        outputPath= outputParentPath +File.separator+"src/main/java"+File.separator+basePackage+File.separator+"cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);

        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/java/cli/command/ListCommand.java.ftl";
        outputPath= outputParentPath +File.separator+"src/main/java"+File.separator+basePackage+File.separator+"cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);

        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/java/cli/command/ConfigCommand.java.ftl";
        outputPath= outputParentPath +File.separator+"src/main/java"+File.separator+basePackage+File.separator+"cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);

        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/java/cli/CommandExecutor.java.ftl";
        outputPath= outputParentPath +File.separator+"src/main/java"+File.separator+basePackage+File.separator+"cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);

        //generator生成
        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/java/generator/MainGenerator.java.ftl";
        outputPath= outputParentPath +File.separator+"src/main/java"+File.separator+basePackage+File.separator+"generator/MainGenerator.java";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);

        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/java/generator/DynamicGenerator.java.ftl";
        outputPath= outputParentPath +File.separator+"src/main/java"+File.separator+basePackage+File.separator+"generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);

        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/java/generator/StaticGenerator.java.ftl";
        outputPath= outputParentPath +File.separator+"src/main/java"+File.separator+basePackage+File.separator+"generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);
        //pom生成
        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/pom.xml.ftl";
        outputPath= outputParentPath +File.separator+"pom.xml";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);
        //README生成
        inputPath=classPathResource.getAbsolutePath()+File.separator+"templates/README.md.ftl";
        outputPath= outputParentPath +File.separator+"README.md";
        DynamicFileGenerator.doGenerate(inputPath,outputPath, meta);
    }

    protected String copySource(Meta meta, String outputParentPath) {
        String sourceRootPath= meta.getFileConfig().getSourceRootPath();
        String sourceOutputPath= outputParentPath +File.separator+".source";
        FileUtil.copy(sourceRootPath,sourceOutputPath,false);
        return sourceOutputPath;
    }

}