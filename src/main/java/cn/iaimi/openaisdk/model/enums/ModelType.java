package cn.iaimi.openaisdk.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2024/3/24
 */
public enum ModelType {

    @Deprecated
    QWEN_V1("qwen-v1", "qwen-v1"),
    QWEN_TURBO("通义千问-Turbo", "qwen-turbo"),
    BAILIAN_V1("百炼-v1", "bailian-v1"),
    DOLLY_12B_V2("dolly-12b-v2", "dolly-12b-v2"),
    @Deprecated
    QWEN_PLUS_V1("通义千问-plus-v1", "qwen-plus-v1"),
    QWEN_PLUS("通义千问-Plus", "qwen-plus"),
    QWEN_MAX("通义千问-Max", "qwen-max");


    private final String name;

    private final String value;


    ModelType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ModelType getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ModelType anEnum : ModelType.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
