package me.biezhi.wechat;

import com.blade.kit.base.Config;
import org.apache.log4j.BasicConfigurator;

public class Application {
	
	public static void main(String[] args) {
		try {
			BasicConfigurator.configure();
			Constant.config = Config.load("classpath:config.properties");
			WechatRobot wechatRobot = new WechatRobot();
			wechatRobot.showQrCode();
			while(!Constant.HTTP_OK.equals(wechatRobot.waitForLogin())){
				Thread.sleep(2000);
			}
			wechatRobot.closeQrWindow();
			wechatRobot.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}