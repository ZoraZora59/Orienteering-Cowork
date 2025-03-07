package cn.zora.superpoint.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * BooleanMysqlEnum
 *
 * @author 阿左
 * @since 2024/03/02
 */
@Getter
public enum BooleanMysqlEnum {
    TRUE("true"),FALSE("false");
    @EnumValue
    private final String value;
    BooleanMysqlEnum(String value){
        this.value=value;
    }

}
