package cn.zora.superpoint.controller;

import cn.zora.superpoint.common.MsgTypeEnum;
import cn.zora.superpoint.model.superpoint.Player;
import cn.zora.superpoint.model.wechat.ReceiveMessage;
import cn.zora.superpoint.model.wechat.ResponseMessage;
import cn.zora.superpoint.service.LogService;
import cn.zora.superpoint.service.PlayerService;
import cn.zora.superpoint.service.SuperPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * RefactorController
 *
 * @author 阿左
 * @since 2024/03/02
 */
@Slf4j
@Controller
public class RefactorController {
    @Resource
    private SuperPointService superPointService;
    @Resource
    private LogService logService;
    @Resource
    private PlayerService playerService;

    @GetMapping("/")
    public ResponseEntity<String> get(@RequestParam(required = false) String signature, @RequestParam(required = false) Long timestamp, @RequestParam(required = false) Long nonce, @RequestParam(required = false) String echostr) {
        log.info("get: signature={}, timestamp={}, nonce={}, echostr={}", signature, timestamp, nonce, echostr);
        return ResponseEntity.ok(echostr);
    }

    @PostMapping(value = "/", consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<ResponseMessage> post(@RequestBody ReceiveMessage request) {
        log.info("receive: {}", request);
        ResponseMessage msg;
        Player user = playerService.getPlayerByWechat(request.getFromUserName());
        request.setUser(user);
        switch (MsgTypeEnum.fromName(request.getMsgType())) {
            case TEXT:
                msg = superPointService.handleTextMessage(request);
                break;
            default:
                msg = new ResponseMessage(request.getFromUserName(), request.getToUserName(), System.currentTimeMillis(), "text", "你好，很抱歉我还没有支持这种类型的消息，敬请期待（如果有需求可以直接对我留言，我会定期查看，感谢使用🙏");
        }
        log.info("response: {}", msg);
        logService.recordLog(request, msg);
        return ResponseEntity.ok(msg);
    }

}
