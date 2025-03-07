package cn.zora.superpoint.model.superpoint;

import cn.zora.superpoint.common.GenderTypeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "player")
@Getter
public class Player {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "nick_name")
    private String nickName;

    @TableField(value = "wechat_user")
    private String wechatUser;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "gender")
    private GenderTypeEnum gender;

    @TableField(value = "phone_number")
    private String phoneNumber;

    @TableField(value = "id_card_number")
    private String idCardNumber;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}