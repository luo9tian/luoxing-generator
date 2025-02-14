package com.luojiu.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.luojiu.maker.generator.JarGenerator;
import com.luojiu.maker.generator.ScriptGenerator;
import com.luojiu.maker.generator.file.DynamicFileGenerator;
import com.luojiu.maker.meta.Meta;
import com.luojiu.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class MainGenerator extends GeneratorTemplate{
    @Override
    protected void buildDest(String outputParentPath, String sourceOutputPath, String jarPath, String shellOutputPath) {
        System.out.println("不用精简");
    }

    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator mainGenerator=new MainGenerator();
//        args=new String[]{"generate --needGit=true"};
        mainGenerator.doGenerate();
    }

}