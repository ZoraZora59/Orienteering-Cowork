package cn.zora.superpoint.handler;

import cn.zora.superpoint.common.CommandEnum;
import cn.zora.superpoint.common.GenderTypeEnum;
import cn.zora.superpoint.model.superpoint.Player;
import cn.zora.superpoint.model.wechat.ReceiveMessage;
import cn.zora.superpoint.service.PlayerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * SignUpPlayerCommandHandler
 *
 * @author 阿左
 * @since 2024/03/02
 */
@Component
public class SignUpPlayerCommandHandler extends AbstractCommandHandler{
    @Resource
    private PlayerService playerService;

    @Override
    protected boolean needRegisterPlayer(){
        return false;
    }

    /**
     * 命令的使用说明
     *
     * @return 说明
     */
    @Override
    public String guidance() {
        return "需要注册的话，发送的消息内容请参考示例 “报名 阿左 男 13012349876”，" +
                "其中“阿左”是昵称，“男”是性别，“13012349876”是手机号码\n" +
                "昵称是必填项，性别和手机号码可以不填，只发送“阿左”也是可以的。";
    }

    @Override
    protected boolean argsIsValid(String[] args) {
        return args.length >= 1;
    }

    @Override
    public String handleCommand(ReceiveMessage message) {
        Player player = new Player();
        player.setWechatUser(message.getFromUserName());
        player.setNickName(message.getArgs()[0]);
        player.setGender(message.getArgs().length > 1 ? GenderTypeEnum.valueOf(message.getArgs()[1]) : GenderTypeEnum.UNKNOWN);
        player.setPhoneNumber(message.getArgs().length > 2 ? message.getArgs()[2] : null);
        player.setIdCardNumber(message.getArgs().length > 2 ? message.getArgs()[2] : null);
        boolean isNewUser = playerService.registerPlayer(player);
        if (isNewUser) {
            return "欢迎，" + player.getNickName() + "，注册成功，请开始关联你的团队吧。";
        }
        return "你已经注册过啦，这次帮你替换了信息。";
    }

    /**
     * CommandHandler对应响应的命令
     *
     * @return 命令全写
     */
    @Override
    public CommandEnum respondCommand() {
        return CommandEnum.SIGN_UP;
    }
}
