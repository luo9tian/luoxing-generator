package com.luojiu.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

@Getter
public enum FileFilterRuleEnum {
    CONTAINS("包含","contains"),
    STARTS_WItH("前缀匹配","startsWith"),
    ENDS_WITH("后缀匹配","endsWith"),
    REGEX("正则","regex"),
    EQUALS("相等","equals");

    private final String text;
    private final String value;

    FileFilterRuleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }
    public static FileFilterRuleEnum getEnumByValue(String value){
        if(ObjectUtil.isEmpty(value)){
            return null;
        }
        for (FileFilterRuleEnum anEnum:FileFilterRuleEnum.values()) {
            if(anEnum.getValue().equals(value)){
                return anEnum;
            }
        }
        return null;
    }
}
