package cn.zora.superpoint.common;

import lombok.Getter;

/**
 * CommandEnum
 *
 * @author 阿左
 * @since 2024/03/02
 */
@Getter
public enum CommandEnum {
    REBOOT("系统重启"),
    REGISTER_FACILITY("注册雪场"),
    REGISTER_TEAM("创建团队"),
    COMBINE_PLAYER_TEAM("关联团队"),
    COMBINE_MATCH_TEAM("关联比赛"),
    COMMUNICATE("通信"),
    SIGN_UP("报名"),
    CONFIG_NODES("分配点位"),
    ACHIEVE("完成点位"),
    CANCEL("撤回点位"),
    TEAM_STATE("团队进展"),
    TEAM_STATE_DETAIL("成员进展"),
    MY_STATE("我的进展");

    CommandEnum(String value){
        this.value=value;
    }

    private final String value;
}
