package com.longan.getway.utils;

import com.longan.biz.utils.Utils;

public class Constants {
	public interface UnicomFlow {
		public static String SIGN_KEY = Utils.getProperty("unicomFlow.SignKey");
		public static String url = Utils.getProperty("unicomFlow.URL");

		public static String CHARGEQUERY_ACTION = url
				+ Utils.getProperty("unicomFlow.chargeQueryAction");
		public static String CARDQUERY_ACTION = url
				+ Utils.getProperty("unicomFlow.cardQueryAction");
		public static String CHARGE_ACTION = url + Utils.getProperty("unicomFlow.chargeAction");
		public static String CARDCHECK_ACTION = url
				+ Utils.getProperty("unicomFlow.cardCheckAction");
	}
}
