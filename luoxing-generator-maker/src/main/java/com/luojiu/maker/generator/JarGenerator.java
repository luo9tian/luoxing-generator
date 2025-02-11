package com.luojiu.maker.generator;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JarGenerator {
    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        String winMavenCommand="mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand="mvn.cmd clean package -DskipTests=true";
        String mavenCommand=winMavenCommand;
        ProcessBuilder processBuilder=new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));
        Process process=processBuilder.start();
        //读取命令的输出
        InputStream inputStream=process.getInputStream();
        //目录乱码使用
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"GBK"));
//        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line=reader.readLine())!=null){
            System.out.println(line);
        }

        //等待命令执行完毕
        int exitCode=process.waitFor();
        System.out.println("命令执行完成，退出码："+exitCode);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        doGenerate("D:\\项目练习\\luoxing-generator\\luoxing-generator-maker\\autoGenerator\\acm-template-pro-generator");
    }
}
