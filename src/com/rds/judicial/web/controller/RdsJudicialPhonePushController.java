package com.rds.judicial.web.controller;
/**
 * @description 手机推送
 * @author fushaoming
 */
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

@Controller
@RequestMapping("judicial/push")
public class RdsJudicialPhonePushController {
		private String masterSecret = "010398d21f2a11846a29bca8";
		private String appKey = "d6a6d6259a78dfae4dc4d059";

		public static PushPayload buildPushObject_android_alias_alertWithTitle(
				String person, String content) {
			return PushPayload
					.newBuilder()
					.setPlatform(Platform.android())
					.setAudience(Audience.alias(person))
					.setNotification(
							Notification.android("您有新消息", content, null))
					.build();
		}
		@RequestMapping("dopush")
		@ResponseBody
		public Map<String, Object> phonePush(@RequestParam String msg,
				@RequestParam String person) {
			Map<String, Object> map = new HashMap<String, Object>();
			JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
			PushPayload payload = buildPushObject_android_alias_alertWithTitle(
					person, msg);

			try {
				PushResult result = jpushClient.sendPush(payload);
				System.out.print("Got result - " + result);
				map.put("success", true);

			} catch (APIConnectionException e) {
				// Connection error, should retry later
				System.out.printf("Connection error, should retry later",
						e.getMessage());
				map.put("success", false);

			} catch (APIRequestException e) {
				// Should review the error, and fix the request
				System.out.printf(
						"Should review the error, and fix the request",
						e.getMessage());
				System.out.printf("HTTP Status: " + e.getStatus());
				System.out.printf("Error Code: " + e.getErrorCode());
				System.out.printf("Error Message: " + e.getErrorMessage());
				map.put("success", false);
			}
			return map;
		}
}