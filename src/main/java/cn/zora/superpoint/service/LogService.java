package cn.zora.superpoint.service;

import cn.zora.superpoint.common.PlatformTypeEnum;
import cn.zora.superpoint.model.superpoint.Log;
import cn.zora.superpoint.model.wechat.ReceiveMessage;
import cn.zora.superpoint.model.wechat.ResponseMessage;
import cn.zora.superpoint.repository.mapper.LogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * LogService
 *
 * @author 阿左
 * @since 2024/03/02
 */
@Service
public class LogService {

    @Resource
    private LogMapper logMapper;
    public void recordLog(ReceiveMessage receive , ResponseMessage response){

        Log log = new Log();
        log.setUser(receive.getFromUserName());
        log.setReceiveMessageJson(receive.getContent());
        log.setResponseMessageJson(response.getContent());
        log.setPlatform(PlatformTypeEnum.WECHAT_MP);

        logMapper.insert(log);
    }
}
