package cn.zora.superpoint.handler;

import cn.zora.superpoint.common.CommandEnum;
import cn.zora.superpoint.model.wechat.ReceiveMessage;
import org.springframework.stereotype.Component;

/**
 * RebootCommandHandler
 *
 * @author 阿左
 * @since 2024/03/03
 */
@Component
public class RebootCommandHandler extends AbstractCommandHandler{
    /**
     * 命令的使用说明
     *
     * @return 说明
     */
    @Override
    public String guidance() {
        return "占位符";
    }

    @Override
    protected boolean argsIsValid(String[] args) {
        return true;
    }

    @Override
    public String handleCommand(ReceiveMessage message) {
       return "现在不支持系统重启啦，数据很安全哦！";

    }

    /**
     * 响应命令
     *
     * @return 命令全写
     */
    @Override
    public CommandEnum respondCommand() {
        return CommandEnum.REBOOT;
    }
}
