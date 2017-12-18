package com.longan.biz.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	public final static Map<Integer, String> BIZ_MAP = new HashMap<Integer, String>();

	static {
		BIZ_MAP.put(BizInfo.CODE_FLOW_UNICOM, "联通流量包");
		BIZ_MAP.put(BizInfo.CODE_PHONE_UNICOM, "联通话费");
		BIZ_MAP.put(BizInfo.CODE_PHONE_MOBILE, "移动话费");
		BIZ_MAP.put(BizInfo.CODE_FLOW_MOBILE, "移动流量包");
		BIZ_MAP.put(BizInfo.CODE_PHONE_TELECOM, "电信话费");
		BIZ_MAP.put(BizInfo.CODE_FLOW_TELECOM, "电信流量包");
		BIZ_MAP.put(BizInfo.CODE_QB_TENCENT, "QB充值");
		BIZ_MAP.put(BizInfo.CODE_GAME, "游戏充值");
	}

	public static Map<Integer, String> getBizMap() {
		return BIZ_MAP;
	}

	public interface BizInfo {
		public final static int CODE_FLOW_UNICOM = 100;
		public final static int CODE_FLOW_TELECOM = 101;
		public final static int CODE_PHONE_UNICOM = 200;
		public final static int CODE_PHONE_MOBILE = 201;
		public final static int CODE_FLOW_MOBILE = 102;
		public final static int CODE_PHONE_TELECOM = 202;
		public final static int CODE_QB_TENCENT = 400;
		public final static int CODE_GAME = 500;
	}

	public interface Item {
		public final static int STATUS_INIT = 1; // 初始
		public final static int STATUS_UP = 2; // 上架
		public final static int STATUS_DOWN = 3; // 下架
		public final static int STATUS_DEL = -1; // 删除

		public final static String STATUS_INIT_DESC = "初始";
		public final static String STATUS_UP_DESC = "上架";
		public final static String STATUS_DOWN_DESC = "下架";
		public final static String STATUS_DEL_DESC = "删除";

		public final static int TYPE_ITEM_CHARGE = 1; // 直充
		public final static int TYPE_ITEM_CARD = 2; // 卡密直充

		public final static String TYPE_ITEM_CHARGE_DESC = "直充";
		public final static String TYPE_ITEM_CARD_DESC = "卡密";

		public final static int CARRIER_TYPE_UNICOM = 1; // 联通
		public final static int CARRIER_TYPE_TELECOM = 2; // 电信
		public final static int CARRIER_TYPE_MOBILE = 3; // 移动
		public final static int CARRIER_TYPE_OTHER = 4; // 其他

		public final static String CARRIER_TYPE_UNICOM_DESC = "联通";
		public final static String CARRIER_TYPE_TELECOM_DESC = "电信";
		public final static String CARRIER_TYPE_MOBILE_DESC = "移动";
		public final static String CARRIER_TYPE_OTHER_DESC = "其他";

		public final static String SALES_AREA_SPLIT = ",";

		public final static int SALE_TYPE_NATIONWIDE = 1;
		public final static int SALE_TYPE_AREA = 2;

		public final static String SALE_TYPE_NATIONWIDE_DESC = "全国";
		public final static String SALE_TYPE_AREA_DESC = "区域";

		public final static int SUPPLY_FILTER_TYPE_PRIORITY = 0;
		public final static int SUPPLY_FILTER_TYPE_COSTPRICE = 1;

	}

	public interface BizFlow {
		public final static int ITEM_USABLE_NATIONWIDE = 0;
		public final static int ITEM_USABLE_AREA = 1;

		public final static String ITEM_USABLE_NATIONWIDE_DESC = "全国";
		public final static String ITEM_USABLE_AREA_DESC = "省内";
	}

	public interface BizOrder {
		// 1 创建 处理中 2 未确认 3 成功 4失败
		public final static int STATUS_INIT = 0; // 创建
		public final static int STATUS_CHARGING = 1; // 处理中
		public final static int STATUS_SUCCESS = 2; // 成功
		public final static int STATUS_FAILED = 3; // 失败
		public final static int STATUS_LOCK = 4; // 锁定充值中
		public final static int STATUS_EXCEPTION = 8; // 异常
		public final static int STATUS_UNCONFIRMED = 9; // 未确认

		public final static String STATUS_INIT_DESC = "创建";
		public final static String STATUS_CHARGING_DESC = "处理中";
		public final static String STATUS_SUCCESS_DESC = "成功";
		public final static String STATUS_FAILED_DESC = "失败";
		public final static String STATUS_LOCK_DESC = "充值中";
		public final static String STATUS_EXCEPTION_DESC = "异常"; // 未确认
		public final static String STATUS_UNCONFIRMED_DESC = "未确认";

		public final static int CHANNEL_SUPPLY = 1; // 接口外放
		public final static int CHANNEL_WEB = 2; // WEB端

		public final static String CHANNEL_SUPPLY_DESC = "接口";
		public final static String CHANNEL_WEB_DESC = "门店";

		public static final int DOWNSTREAM_SUPPLY_WAY_SYNC = 0;
		public static final int DOWNSTREAM_SUPPLY_WAY_ASYNC = 1;

	}

	public interface PayOrder {
		public final static int STATUS_INIT = 1; // 创建
		public final static int STATUS_SUCCESS = 2; // 成功
		public final static int STATUS_FAILED = 3; // 失败
		public final static int STATUS_REFUND = 4; // 已退款
		public final static int STATUS_UNCONFIRMED = 9; // 未确认

		public final static String STATUS_INIT_DESC = "创建";
		public final static String STATUS_SUCCESS_DESC = "成功";
		public final static String STATUS_FAILED_DESC = "失败";
		public final static String STATUS_REFUND_DESC = "已退款";
		public final static String STATUS_UNCONFIRMED_DESC = "未确认";

		public final static int MODE_TRUST_PAY = 1; // 信任支付
		public final static int MODE_PWD_PAY = 2; // 密码验证支付

		public final static int TYPE_PAY_BALANCE = 1; // 余额支付

		public final static String CODE_NO_NEED_REFUND = "NO_NEED_REFUND"; // 不用退款

	}

	public interface ChargeOrder {
		public final static int STATUS_INIT = 1; // 待审核
		public final static int STATUS_SUCCESS = 2; // 成功
		public final static int STATUS_FAILED = 3; // 取消
		public final static int STATUS_UNCONFIRMED = 9; // 未确认

		public final static int PAY_TYPE_MAN = 1; // 手动充值
		public final static int PAY_TYPE_SYSTEM = 2; // 系统充值

		public final static String STATUS_INIT_DESC = "待审核";
		public final static String STATUS_SUCCESS_DESC = "通过";
		public final static String STATUS_FAILED_DESC = "取消";
		public final static String STATUS_UNCONFIRMED_DESC = "未确认";
	}

	public interface CashOrder {
		public final static int STATUS_INIT = 1; // 待审核
		public final static int STATUS_SUCCESS = 2; // 成功
		public final static int STATUS_FAILED = 3; // 取消
		public final static int STATUS_UNCONFIRMED = 9; // 未确认

		public final static int PAY_TYPE_MAN = 1; // 手动充值
		public final static int PAY_TYPE_SYSTEM = 2; // 系统充值

		public final static String STATUS_INIT_DESC = "待审核";
		public final static String STATUS_SUCCESS_DESC = "通过";
		public final static String STATUS_FAILED_DESC = "取消";
		public final static String STATUS_UNCONFIRMED_DESC = "未确认";
	}

	public interface RefundOrder {
		public final static int STATUS_INIT = 1; // 创建
		public final static int STATUS_SUCCESS = 2; // 成功
		public final static int STATUS_FAILED = 3; // 失败
		public final static int STATUS_UNCONFIRMED = 9; // 未确认

		public final static String STATUS_INIT_DESC = "创建";
		public final static String STATUS_SUCCESS_DESC = "成功";
		public final static String STATUS_FAILED_DESC = "失败";
		public final static String STATUS_UNCONFIRMED_DESC = "未确认";

		public final static int PAY_TYPE_SYSTEM = 1;// 系统自动退款
		public final static int PAY_TYPE_OPERATOR = 2;// 操作员退款

		public final static String PAY_TYPE_SYSTEM_DESC = "失败自动退款";
		public final static String PAY_TYPE_OPERATOR_DESC = "操作员退款";
	}

	public interface AcctInfo {
		public final static int STATUS_NORMAL = 1; // 正常
		public final static int STATUS_FREEZE = 2; // 冻结
		public final static int STATUS_DEL = 9; // 删除

		public final static String STATUS_NORMAL_DESC = "正常";
		public final static String STATUS_FREEZE_DESC = "冻结";
		public final static String STATUS_DEL_DESC = "删除";

		public final static int SALES_PRICE_1 = 1; // 价格1
		public final static int SALES_PRICE_2 = 2; // 价格2
		public final static int SALES_PRICE_3 = 3; // 价格3
		public final static int SALES_PRICE_4 = 4;
		public final static int SALES_PRICE_5 = 5; // 暂时不用
		public final static int SALES_PRICE_6 = 6; // 暂时不用

		public final static String SALES_PRICE_1_DESC = "价格1";
		public final static String SALES_PRICE_2_DESC = "价格2";
		public final static String SALES_PRICE_3_DESC = "价格3";
		public final static String SALES_PRICE_4_DESC = "价格4";
		public final static String SALES_PRICE_5_DESC = "价格5";
		public final static String SALES_PRICE_6_DESC = "价格6";
	}

	public interface AcctLog {
		public final static int STATUS_NORMAL = 1; // 正常
		public final static int STATUS_EXCEPTION = 2; // 异常

		public final static String STATUS_NORMAL_DESC = "正常"; // 正常
		public final static String STATUS_EXCEPTION_DESC = "异常"; // 异常

		public final static int TRADE_TYPE_IN = 1; // 入账
		public final static int TRADE_TYPE_OUT = 2; // 出账

		public final static String TRADE_TYPE_IN_DESC = "入账"; // 入账
		public final static String TRADE_TYPE_OUT_DESC = "出账"; // 出账

		public final static int BILL_TYPE_PAY_ORDER = 1; // 支付单
		public final static int BILL_TYPE_CHARGE_ORDER = 2; // 充值单
		public final static int BILL_TYPE_REFUND_ORDER = 3; // 退款单
		public final static int BILL_TYPE_CASH_ORDER = 4; // 提现单

		public final static String BILL_TYPE_PAY_ORDER_DESC = "支付单";
		public final static String BILL_TYPE_CHARGE_ORDER_DESC = "充值单";
		public final static String BILL_TYPE_REFUND_ORDER_DESC = "退款单";
		public final static String BILL_TYPE_CASH_ORDER_DESC = "提现单";
	}

	public interface UserInfo {
		public final static int STATUS_NORMAL = 1; // 正常
		public final static int STATUS_CANCEL = 2; // 注销
		public final static int STATUS_DEL = -1; // 删除

		public final static String STATUS_NORMAL_DESC = "正常";
		public final static String STATUS_CANCEL_DESC = "注销";
		public final static String STATUS_DEL_DESC = "删除";

		public final static int TYPE_ADMIN = 1; // 内部用户
		public final static int TYPE_DOWNSTREAM = 2; // 下游代理商
		public final static int TYPE_UPSTREAM = 3; // 上游供货商
		public final static int TYPE_BUSINESS = 4; // B
		public final static int TYPE_CUSTOM = 5; // C

		public final static String TYPE_ADMIN_DESC = "内部用户";
		public final static String TYPE_DOWNSTREAM_DESC = "下游代理商";
		public final static String TYPE_UPSTREAM_DESC = "上游供货商";
		public final static String TYPE_BUSINESS_DESC = "企业商户";
		public final static String TYPE_CUSTOM_DESC = "门店商户";

		public final static String CODE_PWD_ERROR = "pwd_error"; // 密码错误CODE；
	}

	public interface Stock {
		public final static int STATUS_INIT = 0; // 初始化
		public final static int STATUS_NORMAL = 1; // 正常
		public final static int STATUS_INV_ALLOCATED = 2; // 锁定，或者出库中
		public final static int STATUS_DELIVERY = 3; // 已出库
		public final static int STATUS_EXCEPTION = 4; // 异常
		public final static int STATUS_INVALID = 5; // 失效
		public final static int STATUS_DEL = -1; // 删除

		public final static String STATUS_INIT_DESC = "未激活";
		public final static String STATUS_NORMAL_DESC = "正常销售";
		public final static String STATUS_INV_ALLOCATED_DESC = "锁定(出库中)";
		public final static String STATUS_DELIVERY_DESC = "已出库";
		public final static String STATUS_EXCEPTION_DESC = "异常隔离";
		public final static String STATUS_INVALID_DESC = "失效";
		public final static String STATUS_DEL_DESC = "删除";

		public final static String FLAG_CARD_VALID = "_InValid";
	}

	public interface StockLog {
		public final static int TYPE_IN = 1; // 入库;
		public final static int TYPE_OUT = 2; // 出库;

		public final static String TYPE_IN_DESC = "入库";
		public final static String TYPE_OUT_DESC = "出库";

		public final static int STATUS_INIT = 0;
		public final static int STATUS_SUCCESS = 1;
		public final static int STATUS_FAILED = 2;
		public final static int STATUS_ACTIVATED = 3;
		public final static int STATUS_INVALID = 4;

		public final static String STATUS_INIT_DESC = "创建";
		public final static String STATUS_SUCCESS_DESC = "成功";
		public final static String STATUS_FAILED_DESC = "失败";
		public final static String STATUS_ACTIVATED_DESC = "已激活";
		public final static String STATUS_INVALID_DESC = "已失效";
	}

	public interface OperationInfo {
		public final static int TYPE_CATALOG = 1; // 目录操作
		public final static int TYPE_URL = 2; // 链接操作
		public final static int TYPE_BIZ = 3; // 业务操作

		public final static String TYPE_CATALOG_DESC = "目录操作";
		public final static String TYPE_URL_DESC = "链接操作";
		public final static String TYPE_BIZ_DESC = "业务操作";

		public final static int STATUS_NORMAL = 1; // 正常
		public final static int STATUS_CANCEL = 2; // 注销
		public final static int STATUS_DEL = -1; // 删除

		public final static String STATUS_NORMAL_DESC = "正常";
		public final static String STATUS_CANCEL_DESC = "注销";
		public final static String STATUS_DEL_DESC = "删除";
	}

	public interface AdminCatalog {
		public final static String CATALOG_FLOW = "adminFlow"; // 流量包目录
		public final static String CATALOG_UNICOMAYNC = "adminUnicomAync"; // 联通异步充值目录
		public final static String CATALOG_MONEY = "adminMoney"; // 资金管理目录
		public final static String CATALOG_USER = "adminUser"; // 用户目录
		public final static String CATALOG_STOCK = "adminStock"; // 库存目录
		public final static String CATALOG_MOBILE = "adminMobile"; // 移动话费
		public final static String CATALOG_TELECOM = "adminTelecom"; // 电信话费
		public final static String CATALOG_SYSTEMOPERATION = "adminSystemOperation"; // 系统管理目录
		public final static String CATALOG_TELECOMFLOW = "adminTelecomFlow"; // 电信流量
		public final static String CATALOG_MOBILEFLOW = "adminMobileFlow"; // 移动流量
		public final static String CATALOG_PRICE = "adminPrice";
		public final static String CATALOG_STATISTIC = "adminStatistic";
		public final static String CATALOG_QB = "adminQb"; // Q币充值
		public final static String CATALOG_MANUAL = "adminManual"; // 人工处理目录
		public final static String CATALOG_GAME = "adminGame"; // 游戏目录
	}

	public interface ItemSupply {
		public final static int STATUS_INIT = 1; // 初始
		public final static int STATUS_UP = 2; // 上架
		public final static int STATUS_DOWN = 3; // 下架
		public final static int STATUS_DEL = -1; // 删除

		public final static String STATUS_INIT_DESC = "初始";
		public final static String STATUS_UP_DESC = "上架";
		public final static String STATUS_DOWN_DESC = "下架";
		public final static String STATUS_DEL_DESC = "删除";

		public final static int TYPE_CARD_FORWARD_CHARGE = 1; // 卡密直充
		public final static int TYPE_DIRECT_CHARGE = 2; // 直充
		public final static int TYPE_MAN = 3; // 人工
		public final static int TYPE_CARD = 4; // 开密
		public final static int TYPE_CARD_CHARGE = 5; // 开密

		public final static String TYPE_CARD_FORWARD_CHARGE_DESC = "卡密转直充";
		public final static String TYPE_DIRECT_CHARGE_DESC = "直充";
		public final static String TYPE_MAN_DESC = "人工充值";
		public final static String TYPE_CARD_DESC = "卡密";
		public final static String TYPE_CARD_CHARGE_DESC = "卡密充值";

		public final static int LOSSTYPE_CANNOT = 0;// 不允许损失
		public final static int LOSSTYPE_CAN = 1;// 允许损失

		public final static String LOSSTYPE_CANNOT_DESC = "不允许损失";
		public final static String LOSSTYPE_CAN_DESC = "允许损失";

	}

	public interface ErrorCode {
		public final static String SUCCESS = "success";
		public final static String FAILED = "failed";

		public final static String CODE_SUCCESS = "00";
		public final static String DESC_SUCCESS = "交易成功";

		public final static String CODE_ERROR_VALIDATE = "10";
		public final static String CODE_ERROR_SIGN = "11";
		public final static String DESC_ERROR_SIGN = "校验码验证失败";
		public final static String DESC_ERROR_SIGN_NONE = "用户校验码验证获取失败";

		public final static String CODE_ERROR_ITEM = "20";
		public final static String DESC_ERROR_ITEM = "商品查询失败";

		public final static String CODE_ERROR_ITEM_PRICE = "21";

		public final static String CODE_ERROR_BIZORDER = "22";

		public final static String CODE_ERROR_SERIALNO = "23";

		public final static String CODE_ERROR_UID_AMOUNT = "24";

		public final static String CODE_ERROR_PHONE_AREA = "25";

		public final static String CODE_ERROR_GAME_QUERY = "26";
		public final static String DESC_ERROR_GAME_QUERY = "网游咨询查失败";

		public final static String CODE_ERROR_SUPPLY_FAILD = "30";
		public final static String DESC_ERROR_SUPPLY_FAILD = "供货失败";

		public final static String CODE_ERROR_SUPPLY_UNCONFIRM = "31";
		public final static String DESC_ERROR_SUPPLY_UNCONFIRM = "供货未确认(此状态下有可能成功也有可能失败)";

		public final static String CODE_ERROR_USER = "40";
		public final static String DESC_ERROR_USER = "代理商用户不存在或代理商用户类型不对";

		public final static String CODE_ERROR_ACCT = "41";
		public final static String DESC_ERROR_ACCT = "代理商账户不存在或状态不对";

		public final static String CODE_ERROR_BALANCE = "42";

		public final static String CODE_ERROR_PAYORDER = "43";

		public final static String CODE_ERROR_AUTH = "44";
		public final static String DESC_ERROR_AUTH = "没有该业务权限";

		public final static String CODE_ERROR_SYSTEM = "50";
		public final static String DESC_ERROR_SYSTEM = "系统异常";

		public final static String CODE_ERROR_BUSI = "51";
		public final static String DESC_ERROR_BUSI = "交易繁忙";

	}

	public interface MemcachedKey {
		public final static String UID_AMOUNT = "UID_AMOUNT_"; // UID_COUNT_12
																// 加上用户id
																// 手机号当天充值次数
		public final static int UID_AMOUNT_EXPTIME = 60 * 60 * 24;
	}

	public interface MobileBelong {
		public final static int CARRIER_TYPE_UNICOM = 1; // 联通
		public final static int CARRIER_TYPE_TELECOM = 2; // 电信
		public final static int CARRIER_TYPE_MOBILE = 3; // 移动

		public final static String CARRIER_TYPE_UNICOM_DESC = "联通";
		public final static String CARRIER_TYPE_TELECOM_DESC = "电信";
		public final static String CARRIER_TYPE_MOBILE_DESC = "移动";
	}

	public interface AreaInfo {
		public final static int AREA_TYPE_PROVINCE = 1; // 省
		public final static int AREA_TYPE_CITY = 2; // 市
	}

	public interface TraderInfo {
		public final static int STATUS_NORMAL = 0;
		public final static int STATUS_CANCEL = 1;

		public final static String STATUS_NORMAL_DESC = "正常";
		public final static String STATUS_CANCEL_DESC = "注销";

		public final static int TRADER_TYPE_UPSTREAM = 1; // 上游
		public final static int TRADER_TYPE_DOWNSTREAM = 2; // 下游

		public final static int SUPPLY_WAY_SYNC = 0; // 同步
		public final static int SUPPLY_WAY_ASYNC = 1; // 异步

		public final static String SUPPLY_WAY_SYNC_DESC = "同步"; // 同步
		public final static String SUPPLY_WAY_ASYNC_DESC = "异步"; // 异步

		public final static int TYPE_CARD_FORWARD_CHARGE = 1; // 卡密直充
		public final static int TYPE_DIRECT_CHARGE = 2; // 直充
		public final static int TYPE_MAN = 3; // 人工
		public final static int TYPE_CARD = 4; // 开密
		public final static int TYPE_CARD_CHARGE = 5; // 开密

		public final static String TYPE_CARD_FORWARD_CHARGE_DESC = "卡密转直充";
		public final static String TYPE_DIRECT_CHARGE_DESC = "直充";
		public final static String TYPE_MAN_DESC = "人工充值";
		public final static String TYPE_CARD_DESC = "卡密";
		public final static String TYPE_CARD_CHARGE_DESC = "卡密充值";
	}

	public interface OperationLog {
		public static final int TYPE_ADD = 1;
		public static final int TYPE_UPDATE = 2;
		public static final int TYPE_OTHER = 3;
		public static final int TYPE_DELETE = -1;

		public static final String TYPE_ADD_DESC = "新增";
		public static final String TYPE_UPDATE_DESC = "修改";
		public static final String TYPE_OTHER_DESC = "其它";
		public static final String TYPE_DELETE_DESC = "删除";
	}

	public interface Task {
		public static final int STATUS_UNDONE = 1; // 未执行
		public static final int STATUS_DONE = 2; // 已执行
		public static final int STATUS_CANCEL = 3; // 已取消
		public static final int STATUS_EXCEPTION = 4; // 异常

		public static final String STATUS_UNDONE_DESC = "未执行";
		public static final String STATUS_DONE_DESC = "已执行";
		public static final String STATUS_CANCEL_DESC = "已取消";
		public static final String STATUS_EXCEPTION_DESC = "异常";

		public static final int COMMIT_TYPE_REAL_TIME = 1; // 即时生效
		public static final int COMMIT_TYPE_TASK = 2; // 定时生效
		public static final String COMMIT_TYPE_REAL_TIME_DESC = "即时生效";
		public static final String COMMIT_TYPE_TASK_DESC = "定时生效";
	}

	public interface RoleInfo {
		public final static int STATUS_NORMAL = 1; // 正常
		public final static int STATUS_CANCEL = 2; // 注销
		public final static int STATUS_DEL = -1; // 删除

		public final static String STATUS_NORMAL_DESC = "正常";
		public final static String STATUS_CANCEL_DESC = "注销";
		public final static String STATUS_DEL_DESC = "删除";
	}

	public interface Memcached {

	}

	public interface SupplyOrder {
		public final static int STATUS_INIT = 0; // 创建
		public final static int STATUS_CHARGING = 1; // 处理中
		public final static int STATUS_SUCCESS = 2; // 成功
		public final static int STATUS_FAILED = 3; // 失败
		public final static int STATUS_LOCK = 4; // 锁定充值中
		public final static int STATUS_EXCEPTION = 8; // 异常
		public final static int STATUS_UNCONFIRMED = 9; // 未确认

		public final static String STATUS_INIT_DESC = "创建";
		public final static String STATUS_CHARGING_DESC = "处理中";
		public final static String STATUS_SUCCESS_DESC = "成功";
		public final static String STATUS_FAILED_DESC = "失败";
		public final static String STATUS_LOCK_DESC = "充值中";
		public final static String STATUS_EXCEPTION_DESC = "异常"; // 未确认
		public final static String STATUS_UNCONFIRMED_DESC = "未确认";

		public final static int REPEAT_TYPE_NO = 0;
		public final static int REPEAT_TYPE_YES = 1;

		public final static String REPEAT_TYPE_NO_DESC = "否";
		public final static String REPEAT_TYPE_YES_DESC = "是";

		public final static int COMBINE_TYPE_NO = 0;
		public final static int COMBINE_TYPE_YES = 1;

		public final static int FINAL_TYPE_NO = 0;
		public final static int FINAL_TYPE_YES = 1;

		public final static String FINAL_TYPE_NO_DESC = "否";
		public final static String FINAL_TYPE_YES_DESC = "是";

		public final static int MANUAL_TYPE_NO = 0;
		public final static int MANUAL_TYPE_YES = 1;

		public final static String MANUAL_TYPE_NO_DESC = "否";
		public final static String MANUAL_TYPE_YES_DESC = "是";
	}

	public interface GameCarrier {
		public final static int STATUS_NORMAL = 1; // 正常
		public final static int STATUS_CANCEL = 2; // 注销

		public final static String STATUS_NORMAL_DESC = "正常";
		public final static String STATUS_CANCEL_DESC = "注销";
	}

	public interface Game {
		public final static int STATUS_NORMAL = 1; // 正常
		public final static int STATUS_CANCEL = 2; // 注销

		public final static String STATUS_NORMAL_DESC = "正常";
		public final static String STATUS_CANCEL_DESC = "注销";
	}

	public interface ShortMsg {
		// 1 初始 2成功 3失败 4未知
		public final static int STATUS_INIT = 1;
		public final static int STATUS_SUCCESS = 2;
		public final static int STATUS_FAILED = 3;
		public final static int STATUS_UNKNOWN = 4;

		public final static int TYPE_REG = 1;
		
		public final static int TYPE_BALANCE_WARNING = 30;
		public final static int TYPE_SUPPLY_WARNING = 31;
	}

	public interface Config {
		/* key */
		public final static String KEY_WHITE_LIST_SWITCH = "white_list_switch"; // 白名单开关on:开off:关

		public final static String KEY_SUPPLY_SAMPLE_COUNT = "supply_sample_count"; // 默认全局供货质量检查采样数

		public final static String KEY_SUPPLY_SUCCESS_RATE = "supply_success_rate"; // 默认全局供货质检合格率

		public final static String KEY_SUPPLY_WARING_PHONE = "supply_waring_phone"; // 供货告警电话

		public final static String KEY_SUPPLY_FLOW_CHOOSE = "supply_flow_choose"; // 流量充值筛选，如果碰到全国的商品和省域的商品如何做选择

		public final static String KEY_SUPPLY_MAX_REPEAT_COUNT = "supply_max_repeat_count"; // 全局最大补充次数.

		public final static String KEY_SUPPLY_MAX_REPEAT_TIME = "supply_max_repeat_time"; // 全局最大补充供货时间
		
		public final static String KEY_SUPPLY_TMALL_USER_ID = "supply_tmall_user_id";  //天猫用户编号 用逗号隔开。
		
		public final static String KEY_SUPPLY_TMALL_WARNING_COUNT = "supply_tmall_warning_count";  //天猫商品 1小时失败预警数
		
		public final static String KEY_SUPPLY_FORCE_REPEAT = "supply_force_repeat";  //手动补充强制补充，无视补充次数和时间的条件。

		/* value */

		public final static String VALUE_SUPPLY_FLOW_CHOOSE_AREA = "area"; // 流量充值筛选选省域

		public final static String VALUE_SUPPLY_FLOW_CHOOSE_ALL = "all"; // 流量充值筛选选全国
	}
}
