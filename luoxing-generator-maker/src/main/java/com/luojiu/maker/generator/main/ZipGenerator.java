package com.luojiu.maker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;

public class ZipGenerator extends GeneratorTemplate{
    @Override
    protected String buildDest(String outputParentPath, String sourceOutputPath, String jarPath, String shellOutputPath) {
        String destOutputPath = super.buildDest(outputParentPath, sourceOutputPath, jarPath, shellOutputPath);
        buildZip(destOutputPath);
        return "";
    }

}