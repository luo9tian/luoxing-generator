package com.luojiu.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.luojiu.generator.MainGenerator;
import com.luojiu.model.MainTemplateConfig;
import lombok.Data;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.util.concurrent.Callable;
@Data
@Command(name = "generate",mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable {
    /**
     * 作者名称
     */
    @Option(names = {"-a","--author"},description = "作者名称",arity = "0..1",interactive = true,echo = true)
    private String author;

    /**
     * 输出信息
     */
    @Option(names = {"-o","--outputText"},description = "输出信息",arity = "0..1",interactive = true,echo = true)

    private String outputText;
    /**
     * 是否需要循环
     */
    @Option(names = {"-l","--loop"},description = "是否需要循环",arity = "0..1",interactive = true,echo = true)
    private boolean loop;

    @Override
    public Object call() throws Exception {
        MainTemplateConfig mainTemplateConfig=new MainTemplateConfig();
        BeanUtil.copyProperties(this,mainTemplateConfig);
        MainGenerator.doGenerate(mainTemplateConfig);
        return 0;
    }
}
