package cn.zora.superpoint.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * PointTypeEnum
 *
 * @author 阿左
 * @since 2024/03/02
 */
@Getter
public enum PointTypeEnum {
    NECESSARY("necessary"),UNNECESSARY("unnecessary");
    @EnumValue
    private final String value;
    PointTypeEnum(String value){
        this.value=value;
    }
}
