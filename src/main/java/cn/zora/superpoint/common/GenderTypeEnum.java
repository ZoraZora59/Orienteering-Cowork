package cn.zora.superpoint.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * GenderTypeEnum
 *
 * @author 阿左
 * @since 2024/03/02
 */
@Getter
public enum GenderTypeEnum {
    MALE("male", "男"), FEMALE("female", "女"), UNKNOWN("unknown", "未知");
    @EnumValue
    private final String value;

    private final String description;

    GenderTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

}
