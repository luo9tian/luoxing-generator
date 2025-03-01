package com.luojiu.maker.template;

import cn.hutool.core.util.StrUtil;
import com.luojiu.maker.meta.Meta;
import com.luojiu.maker.template.model.TemplateMakerFileConfig;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TemplateMakerUtils {
    /**
     * 从未分组的文件中移除分组中重复的文件
     * @param fileInfoList
     * @return
     */
    public static List<Meta.FileConfig.FileInfo> removeGroupFilesFromRoot(List<Meta.FileConfig.FileInfo> fileInfoList){
        //先获取所有的分组
        List<Meta.FileConfig.FileInfo> groupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());

        //获取所有分组内的文件的集合
        List<Meta.FileConfig.FileInfo> groupInnerFileInfoList = groupFileInfoList.stream()
                .flatMap(fileInfo -> fileInfo.getFiles().stream())
                .collect(Collectors.toList());

        //获取所有分组内文件的输入路径集合
        Set<String> fileInputPathSet = groupInnerFileInfoList.stream()
                .map(Meta.FileConfig.FileInfo::getInputPath)
                .collect(Collectors.toSet());

        //移除所有在集合内的外层文件
        return fileInfoList.stream()
                .filter(fileInfo ->!fileInputPathSet.contains(fileInfo.getInputPath()))
                .collect(Collectors.toList());
    }
}
