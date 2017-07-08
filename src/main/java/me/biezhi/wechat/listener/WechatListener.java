package me.biezhi.wechat.listener;

import com.blade.kit.json.JSONArray;
import me.biezhi.wechat.Constant;
import me.biezhi.wechat.service.WechatServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONObject;

import me.biezhi.wechat.model.WechatMeta;
import me.biezhi.wechat.service.WechatService;

public class WechatListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatListener.class);
	int playWeChat = 0;
	String content = "欢迎访问百度http://www.baidu.com";
	
	public void start(final WechatService wechatService, final WechatMeta wechatMeta){
		new Thread(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("进入消息监听模式 ...");
				wechatService.choiceSyncLine(wechatMeta);
				while(true){
					int[] arr = wechatService.syncCheck(wechatMeta);
					LOGGER.info("retcode={}, selector={}", arr[0], arr[1]);
					if(arr[0] == 1100){
						LOGGER.info("你在手机上登出了微信，再见");
						break;
					}

					if(arr[0] == 0){
						if(arr[1] == 2){
							//2 新的消息
							JSONObject data = wechatService.webwxsync(wechatMeta);
							wechatService.handleMsg(wechatMeta, data);
						} else if(arr[1] == 6){
							JSONObject data = wechatService.webwxsync(wechatMeta);
							wechatService.handleMsg(wechatMeta, data);
						} else if(arr[1] == 7){
							//7 进入/离开聊天界面
							playWeChat += 1;
							LOGGER.info("你在手机上玩微信被我发现了 {} 次", playWeChat);
							wechatService.webwxsync(wechatMeta);
						} else if(arr[1] == 3){
							continue;
						} else if(arr[1] == 0){
							//0 正常
							//TODO 在此群发消息
							JSONArray ja = Constant.CONTACT.getContactList();
							LOGGER.debug("联系人信息：");
							LOGGER.debug(""+ja.toString());
							//蒸发身体的温度 UserName
							String to = "@4ba313f5b1af5719003b8e39c110aa7738a8f75d7bd4d3d467d122fca48e053e";
							WechatServiceImpl.webwxsendmsg(wechatMeta,content,to);
							continue;
						}
					} else {
						// 
					}

					try {
						LOGGER.info("等待2000ms...");
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, "wechat-listener-thread").start();
	}
	
}
