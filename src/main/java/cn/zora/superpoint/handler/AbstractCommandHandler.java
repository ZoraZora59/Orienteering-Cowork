package cn.zora.superpoint.handler;

import cn.zora.superpoint.common.CommandEnum;
import cn.zora.superpoint.model.wechat.ReceiveMessage;
import cn.zora.superpoint.model.wechat.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * AbstractCommandHandler
 *
 * @author 阿左
 * @since 2024/03/02
 */
@Slf4j
public abstract class AbstractCommandHandler implements ICommandHandler {

    /**
     * 命令的使用说明
     *
     * @return 说明
     */
    public abstract String guidance();

    protected abstract boolean argsIsValid(String[] args);

    @Override
    public ResponseMessage runCommand(ReceiveMessage message) {
        String responseContent;
        try {
            log.info("执行命令: {}", respondCommandString());
            long start = System.currentTimeMillis();

            if (!argsIsValid(message.getArgs())) {
                responseContent = guidance();
            } else {
                responseContent = handleCommand(message);
            }
            log.info("执行命令成功: {}，耗时: {}ms", respondCommandString(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("执行命令失败", e);
            ResponseMessage msg = new ResponseMessage();
            responseContent = "抱歉，执行失败，请稍后再试.(或者直接留言告诉我你遇到的问题，我会尽快解决，非常感谢！🥹";
        }

        ResponseMessage msg = new ResponseMessage();
        msg.setContent(responseContent);
        msg.setCreateTime(System.currentTimeMillis());
        msg.setFromUserName(message.getToUserName());
        msg.setToUserName(message.getFromUserName());
        msg.setMsgType("text");
        return msg;

    }

    public abstract String handleCommand(ReceiveMessage message);

    /**
     * CommandHandler对应响应的命令
     *
     * @return 命令全写
     */
    public String respondCommandString() {
        return respondCommand().getValue();
    }

    /**
     * CommandHandler对应响应的命令枚举
     *
     * @return 命令全写
     */
    public abstract CommandEnum respondCommand();


}
