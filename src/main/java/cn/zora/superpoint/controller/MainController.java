//package cn.zora.superpoint.controller;
//
//import cn.zora.superpoint.common.CommandConstants;
//import cn.zora.superpoint.common.RuleUtils;
//import cn.zora.superpoint.model.wechat.ReceiveMessage;
//import cn.zora.superpoint.model.wechat.ResponseMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * MainController
// *
// * @author 阿左
// * @since 2024/03/01
// */
//@Slf4j
////@Controller
//public class MainController {
//
//    //用户：已完成的点位
//    private final Map<String, List<String>> userTargetMap = new ConcurrentHashMap<>();
//
//    //点位：完成状态
//    private final Map<String, Boolean> targetMap = new ConcurrentHashMap<>();
//
//    //分配的点位：用户
//    private final Map<String, String> targetRelationMap = new ConcurrentHashMap<>();
//
//    private final Map<String, String> userIDtoNameMap = new ConcurrentHashMap<>();
//
//
//    private final Lock lock = new ReentrantLock();
//
//    @GetMapping("/")
//    public ResponseEntity<String> get(@RequestParam(required = false) String signature, @RequestParam(required = false) Long timestamp, @RequestParam(required = false) Long nonce, @RequestParam(required = false) String echostr) {
//        log.info("get: signature={}, timestamp={}, nonce={}, echostr={}", signature, timestamp, nonce, echostr);
//        return ResponseEntity.ok(echostr);
//    }
//
//    @PostMapping(value = "/", consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
//    public ResponseEntity<ResponseMessage> post(@RequestBody ReceiveMessage request) {
//        log.info("post: {}", request);
//        String responseMessage = "收到啦～";
//        switch (RuleUtils.getRule(request.getContent())) {
//            case CommandConstants.REBOOT:
//                responseMessage = "重启失败，请重试";
//                lock.lock();
//                try {
//                    userIDtoNameMap.clear();
//                    userTargetMap.clear();
//                    targetMap.clear();
//                    targetRelationMap.clear();
//                    responseMessage = "系统重启";
//                } finally {
//                    lock.unlock();
//                }
//                break;
//            case CommandConstants.SIGN_UP:
//                log.info("sign up");
//                lock.lock();
//                try {
//                    responseMessage = signUp(request);
//                } finally {
//                    lock.unlock();
//                }
//                break;
//            case CommandConstants.ACHIEVE:
//                log.info("achieve");
//                lock.lock();
//                try {
//                    responseMessage = achieve(request);
//                } finally {
//                    lock.unlock();
//                }
//                break;
//            case CommandConstants.CANCEL:
//                log.info("cancel");
//                try {
//                    responseMessage = cancel(request);
//                } finally {
//                    lock.unlock();
//                }
//                break;
//            case CommandConstants.TEAM_STATE:
//                log.info("team state");
//                responseMessage = teamState(request);
//                break;
//            case CommandConstants.TEAM_STATE_DETAIL:
//                log.info("team state detail");
//                responseMessage = teamStateDetail(request);
//                break;
//            case CommandConstants.MY_STATE:
//                log.info("my state");
//                responseMessage = myState(request);
//                break;
//            case CommandConstants.CONFIG_NODES:
//                log.info("config nodes");
//                lock.lock();
//                try {
//                    responseMessage = configNodes(request);
//                } finally {
//                    lock.unlock();
//                }
//                break;
//            default:
//                log.info("communicate");
//                responseMessage = "收到啦～\n如果你需要超级定点赛相关的功能，以下是示例命令（注意每个输入之间有空格，不能同时进行多个命令哦）：\n" +
//                        "报名 阿左\n" +
//                        "分配点位 21 45 33...\n" +
//                        "完成点位 63 32 21...\n" +
//                        "撤回点位 55 42...\n" +
//                        "团队进展\n" +
//                        "成员进展\n" +
//                        "我的进展";
//                break;
//        }
//
//        ResponseMessage message = new ResponseMessage();
//        message.setContent(responseMessage);
//        message.setCreateTime(System.currentTimeMillis());
//        message.setFromUserName(request.getToUserName());
//        message.setToUserName(request.getFromUserName());
//        message.setMsgType("text");
//        log.info("response: {}", message);
//        return ResponseEntity.ok(message);
//    }
//
//    private String signUp(ReceiveMessage content) {
//        String userID = content.getFromUserName();
//        String[] args = content.getContent().split(" ");
//        if (args.length < 2) {
//            return "请填写你要使用的名字";
//        }
//        String name = args[1];
//        if (userIDtoNameMap.containsKey(userID) || userTargetMap.containsKey(name)) {
//            return "已经报名";
//        } else {
//            userIDtoNameMap.put(userID, name);
//        }
//        userTargetMap.put(name, new CopyOnWriteArrayList<>());
//        return "报名成功";
//    }
//
//    private boolean needSignUp(String user) {
//        return !userIDtoNameMap.containsKey(user);
//    }
//
//    private String achieve(ReceiveMessage content) {
//        if (needSignUp(content.getFromUserName())) {
//            return "未报名";
//        } else {
//            content.setNickName(userIDtoNameMap.get(content.getFromUserName()));
//        }
//
//        String[] args = content.getContent().split(" ");
//        if (args.length < 2) {
//            return "请填写你完成的点位，用空格分割";
//        }
//        Set<String> notExist = new TreeSet<>();
//        Set<String> alreadyAchieve = new TreeSet<>();
//        Set<String> othersAlreadyAchieve = new TreeSet<>();
//        Set<String> success = new TreeSet<>();
//        for (int i = 1; i < args.length; i++) {
//            if (!targetMap.containsKey(args[i])) {
//                notExist.add(args[i]);
//                continue;
//            }
//            if (targetMap.get(args[i])) {
//                if (targetRelationMap.get(args[i]).equals(content.getNickName())) {
//                    alreadyAchieve.add(args[i]);
//                    continue;
//                }
//                othersAlreadyAchieve.add(args[i]);
//                continue;
//            }
//
//            success.add(args[i]);
//            targetMap.put(args[i], true);
//            userTargetMap.get(content.getNickName()).add(args[i]);
//        }
//
//        List<String> needAchieve = new LinkedList<>();
//        for (String target : targetMap.keySet()) {
//            if (targetMap.get(target)) {
//                continue;
//            }
//            if (targetRelationMap.get(target).equals(content.getNickName())) {
//                needAchieve.add(target);
//            }
//        }
//
//        StringBuilder sb = new StringBuilder();
//        if (!success.isEmpty()) {
//            sb.append("成功记录你的完成点位：").append(String.join(",", success)).append("\n");
//        }
//        if (!notExist.isEmpty()) {
//            sb.append("不存在的点位：").append(String.join(",", notExist)).append("\n");
//        }
//        if (!alreadyAchieve.isEmpty()) {
//            sb.append("你之前已经完成的点位：").append(String.join(",", alreadyAchieve)).append("\n");
//        }
//        if (!othersAlreadyAchieve.isEmpty()) {
//            sb.append("其他人已经完成的点位：").append(String.join(",", othersAlreadyAchieve)).append("\n");
//        }
//        if (needAchieve.isEmpty()) {
//            sb.append("恭喜你完成所有点位！");
//        } else {
//            sb.append("你还需要完成的点位：").append(String.join(",", needAchieve)).append("\n");
//            sb.append("加油！你完成的每一个点都很重要！");
//        }
//        return sb.toString();
//    }
//
//    private String cancel(ReceiveMessage content) {
//        if (needSignUp(content.getFromUserName())) {
//            return "未报名";
//        } else {
//            content.setNickName(userIDtoNameMap.get(content.getFromUserName()));
//        }
//        String[] args = content.getContent().split(" ");
//        if (args.length < 2) {
//            return "请填写你撤回的点位，用空格分割";
//        }
//        Set<String> notExist = new TreeSet<>();
//        Set<String> unAchieved = new TreeSet<>();
//        Set<String> others = new TreeSet<>();
//        Set<String> success = new TreeSet<>();
//        for (int i = 1; i < args.length; i++) {
//            if (!targetMap.containsKey(args[i])) {
//                notExist.add(args[i]);
//                continue;
//            }
//            if (!targetMap.get(args[i])) {
//                unAchieved.add(args[i]);
//                continue;
//            }
//            if (!targetRelationMap.get(args[i]).equals(content.getNickName())) {
//                others.add(args[i]);
//                continue;
//            }
//            targetMap.put(args[i], false);
//            userTargetMap.get(content.getNickName()).remove(args[i]);
//            success.add(args[i]);
//        }
//        StringBuilder sb = new StringBuilder();
//        if (!success.isEmpty()) {
//            sb.append("成功撤回你完成的点位：").append(String.join(",", success)).append("\n");
//        }
//        if (!notExist.isEmpty()) {
//            sb.append("不存在的点位：").append(String.join(",", notExist)).append("\n");
//        }
//        if (!unAchieved.isEmpty()) {
//            sb.append("你之前未完成的点位无法被撤回：").append(String.join(",", unAchieved)).append("\n");
//        }
//        if (!others.isEmpty()) {
//            sb.append("其他人已经完成的点位：").append(String.join(",", others)).append("\n");
//        }
//        return sb.toString();
//    }
//
//    private String teamState(ReceiveMessage content) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("当前已报名的团队成员：");
//        if (userTargetMap.isEmpty()) {
//            sb.append("无").append("\n");
//        } else {
//            sb.append(String.join(",", userTargetMap.keySet())).append("\n");
//        }
//
//        List<String> achievedTargets = new ArrayList<>();
//        List<String> unAchievedTargets = new ArrayList<>();
//        for (String target : targetMap.keySet()) {
//            if (targetMap.get(target)) {
//                achievedTargets.add(target);
//            } else {
//                unAchievedTargets.add(target);
//            }
//        }
//        sb.append("团队当前已完成的点位：");
//        if (achievedTargets.isEmpty()) {
//            sb.append("无").append("\n");
//        } else {
//            sb.append(String.join(",", achievedTargets)).append("\n");
//        }
//        sb.append("团队还未已完成的点位：");
//        if (unAchievedTargets.isEmpty()) {
//            sb.append("无").append("\n");
//        } else {
//            sb.append(String.join(",", unAchievedTargets)).append("\n");
//        }
//
//        if (!unAchievedTargets.isEmpty()) {
//            sb.append("革命尚未成功，同志仍需努力！\n");
//        }
//        sb.append("团队点位完成进度：").append(achievedTargets.size()).append("/").append(achievedTargets.size() + unAchievedTargets.size()).append("\n");
//        if (achievedTargets.isEmpty()) {
//            sb.append("准备启动，加油！");
//        }
//        return sb.toString();
//    }
//
//    private String teamStateDetail(ReceiveMessage content) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("成员进展：\n");
//        Map<String, List<String>> userNeedAchieve = new HashMap<>();
//        targetMap.forEach((target, done) -> {
//            if (done) {
//                return;
//            }
//            String userNickName =  targetRelationMap.get(target);
//            if (!userNeedAchieve.containsKey(userNickName)) {
//                userNeedAchieve.put(userNickName, new LinkedList<>());
//            }
//            userNeedAchieve.get(userNickName).add(target);
//        });
//
//        if (userTargetMap.isEmpty()) {
//            sb.append("无").append("\n");
//        }
//        userTargetMap.forEach((k, v) -> {
//            sb.append(k).append("已完成").append(v.size()).append("个点位：");
//            if (v.isEmpty()) {
//                sb.append("无").append("\n");
//            } else {
//                sb.append(String.join(",", v)).append("\n");
//            }
//            sb.append("还需完成").append(userNeedAchieve.get(k)).append("个点位：");
//            if (!userNeedAchieve.containsKey(k) || userNeedAchieve.get(k).isEmpty()) {
//                sb.append("无").append("\n");
//            } else {
//                sb.append(String.join(",", userNeedAchieve.get(k))).append("\n");
//            }
//            sb.append("成员进度：").append(v.size()).append("/").append(targetMap.size()).append("\n");
//        });
//        return sb.toString();
//    }
//
//    private String myState(ReceiveMessage content) {
//        if (needSignUp(content.getFromUserName())) {
//            return "未报名";
//        } else {
//            content.setNickName(userIDtoNameMap.get(content.getFromUserName()));
//        }
//        List<String> unAchievedTargets = new ArrayList<>();
//        targetRelationMap.forEach((k, v) -> {
//            if (v.equals(content.getNickName())) {
//                unAchievedTargets.add(k);
//            }
//        });
//        List<String> achievedTargets = userTargetMap.get(content.getNickName());
//        unAchievedTargets.removeAll(achievedTargets);
//        StringBuilder sb = new StringBuilder();
//        sb.append("你已完成").append(achievedTargets.size()).append("个点位：");
//        if (achievedTargets.isEmpty()) {
//            sb.append("无").append("\n");
//        } else {
//            sb.append(String.join(",", achievedTargets)).append("\n");
//        }
//        sb.append("还需完成").append(unAchievedTargets.size()).append("个点位：");
//        if (unAchievedTargets.isEmpty()) {
//            sb.append("无").append("\n");
//        } else {
//            sb.append(String.join(",", unAchievedTargets)).append("\n");
//        }
//        if (unAchievedTargets.isEmpty()) {
//            sb.append("你已经完成所有点位，恭喜！");
//        } else {
//            sb.append("完成进度：").append(achievedTargets.size()).append("/").append(achievedTargets.size() + unAchievedTargets.size());
//        }
//        return sb.toString();
//    }
//
//    private String configNodes(ReceiveMessage content) {
//        if (needSignUp(content.getFromUserName())) {
//            return "未报名";
//        } else {
//            content.setNickName(userIDtoNameMap.get(content.getFromUserName()));
//        }
//        String[] args = content.getContent().split(" ");
//        if (args.length < 2) {
//            return "给自己分配点位时，请直接输入点位序号，用空格分割。";
//        }
//        List<String> alreadyAchieved = new LinkedList<>();
//        List<String> alreadyYours = new LinkedList<>();
//        List<String> others = new LinkedList<>();
//        List<String> success = new LinkedList<>();
//        for (int i = 1; i < args.length; i++) {
//            if (targetMap.containsKey(args[i])) {
//                if (targetMap.get(args[i])) {
//                    alreadyAchieved.add(args[i]);
//                    continue;
//                }
//            } else {
//                targetMap.put(args[i], false);
//            }
//
//            if (targetRelationMap.containsKey(args[i])) {
//                if (targetRelationMap.get(args[i]).equals(content.getNickName())) {
//                    alreadyYours.add(args[i]);
//                } else {
//                    others.add(args[i]);
//                }
//            } else {
//                targetRelationMap.put(args[i], content.getNickName());
//                success.add(args[i]);
//            }
//        }
//        StringBuilder sb = new StringBuilder();
//        if (!success.isEmpty()) {
//            sb.append("成功分配给你的点位：").append(String.join(",", success)).append("\n");
//        }
//        if (!alreadyAchieved.isEmpty()) {
//            sb.append("已经完成的点位：").append(String.join(",", alreadyAchieved)).append("\n");
//        }
//        if (!alreadyYours.isEmpty()) {
//            sb.append("已经分配给你的点位：").append(String.join(",", alreadyYours)).append("\n");
//        }
//        if (!others.isEmpty()) {
//            sb.append("其他人已经分配的点位：").append(String.join(",", others));
//        }
//
//        return sb.toString();
//    }
//
//
//}
