package cn.zora.superpoint.handler;

import cn.zora.superpoint.common.CommandEnum;
import cn.zora.superpoint.model.wechat.ReceiveMessage;
import org.springframework.stereotype.Component;
// import cn.zora.superpoint.model.superpoint.Team;
import cn.zora.superpoint.model.superpoint.PlayerTeamRelation;
import cn.zora.superpoint.repository.mapper.PlayerTeamRelationMapper;
import cn.zora.superpoint.repository.mapper.TeamMapper;

import javax.annotation.Resource;
/**
 * CombinePlayerTeamCommandHandler
 *
 * @author 阿左
 * @since 2024/03/03
 */
@Component
public class CombinePlayerTeamCommandHandler extends AbstractCommandHandler{

    @Resource
    private PlayerTeamRelationMapper relationMapper;
    @Resource
    private TeamMapper teamMapper;


    /**
     * 命令的使用说明
     *
     * @return 说明
     */
    @Override
    public String guidance() {
        return "想关联团队的话，请参考示例输入“关联团队 冲冲小队”";
    }

    @Override
    protected boolean argsIsValid(String[] args) {
        return args.length==1;
    }

    @Override
    public String handleCommand(ReceiveMessage message) {
       String teamName = message.getArgs()[0];
       //TODO:实现teamName的检索去重逻辑
       PlayerTeamRelation relation = new PlayerTeamRelation();
       relation.setTeamId(null);
       //TODO:实现关系绑定逻辑
       return "TODO";

    }

    /**
     * 响应命令
     *
     * @return 命令全写
     */
    @Override
    public CommandEnum respondCommand() {
        return CommandEnum.COMBINE_PLAYER_TEAM;
    }
}
