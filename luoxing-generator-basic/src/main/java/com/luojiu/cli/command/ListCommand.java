package com.luojiu.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.*;

import java.io.File;
import java.util.List;

@Command(name = "list",mixinStandardHelpOptions = true)
public class ListCommand implements Runnable{
    @Override
    public void run() {
        String projectPath=System.getProperty("user.dir");
        File parentFile=new File(projectPath).getParentFile();
        String inputPath=new File(parentFile,"luoxing-generator-demo-projects/acm-template").getAbsolutePath();
        List<File>files= FileUtil.loopFiles(inputPath);
        for (File file:files){
            System.out.println(file);
        }
    }
}
