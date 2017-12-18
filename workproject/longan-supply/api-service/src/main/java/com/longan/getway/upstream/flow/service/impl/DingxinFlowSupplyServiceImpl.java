package com.longan.getway.upstream.flow.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.vo.DingxinFlowTransVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

/**
 * 鼎信流量充值供货类
 * 
 * @author Administrator
 *
 */
public class DingxinFlowSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;

	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	@Resource
	private LocalCachedService localCachedService;
	static {
		codeResultMap.put("0", "查询成功");
		codeResultMap.put("1003", "用户ID错误");
		codeResultMap.put("1004", "用户IP错误");
		codeResultMap.put("1005", "用户接口已关闭");
		codeResultMap.put("1006", "加密结果错误");
		codeResultMap.put("1007", "订单号不存在");// 需在该订单提交2分钟后方可处理失败
		codeResultMap.put("1011", "号码归属地未知");
		codeResultMap.put("1013", "手机对应的商品有误或者没有上架");
		codeResultMap.put("1014", "无法找到手机归属地");
		codeResultMap.put("1015", "余额不足");
		codeResultMap.put("1016", "QQ号格式错误");
		codeResultMap.put("1017", "产品未分配用户，联系商务");
		codeResultMap.put("1018", "订单生成失败");
		codeResultMap.put("1019", "充值号码与产品不匹配");
		codeResultMap.put("1020", "号码运营商未知");
		codeResultMap.put("9998", "参数有误");
		codeResultMap.put("9999", "系统错误");// 人工处理
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		Item item = localCachedService.getItem(supplyOrder.getItemId());
		if (item == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("商品为空");
			return result;
		}
		// int gprs = Integer.parseInt(itemSupply.getSupplyProductCode());
		int gprs = getFlowSize(item.getItemExt1());

		DingxinFlowTransVO vo = new DingxinFlowTransVO(supplyOrder.getId() + "", supplyOrder.getItemUid(), gprs);
		vo.setArea(Integer.valueOf(item.getItemExt2()));
		String url = vo.createChargeParam();
		logger.warn("鼎信流量充值请求:" + url);
		// 发送供货请求
		SupplyResult<Document> httpResult = MultiThreadedHttpConnection.getInstance().sendDataByGet(url);
		// SupplyResult<Document> httpResult = mockResponse();//模拟返回
		result.setStatus(httpResult.getStatus());
		result.setResultMsg(httpResult.getResultMsg());
		if (httpResult.isSuccess()) {
			Document doc = httpResult.getModule();
			Element root = doc.getRootElement();
			logger.warn("鼎信流量充值返回: " + root.asXML());
			System.out.println(doc.asXML());
			String porderid = root.elementText("Porderid");// 第三方订单号
			String state = root.elementText("state");// 订单状态
			String error = root.elementText("error");// 提交状态
			if (DingxinFlowTransVO.isChargeing(state)) {
				if (isSubmitSuccess(error)) {
					// 充值提交成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					supplyOrder.setUpstreamSerialno(porderid);
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg(codeResultMap.get(error));
				}
			} else if (DingxinFlowTransVO.isWaitPay(state)) {
				// 等待扣款
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("充值处理中");
			} else if (DingxinFlowTransVO.isChargeFailed(state)) {
				// 充值失败
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(codeResultMap.get(error));
			} else {
				// 未知状态
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("充值处理中");
			}
		} else if (httpResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("充值处理中");
		} else {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("网络连接失败");
		}
		return result;
	}

	/**
	 * 模拟返回
	 */
	// private SupplyResult<Document> mockResponse(){
	// SupplyResult<Document> result = new SupplyResult<Document>();
	// try{
	// Element root = DocumentHelper.createElement("response");
	// Document document = DocumentHelper.createDocument(root);
	// root.addElement("Porderid").addText("888888");
	// root.addElement("state").addText("0");
	// result.setStatus(SupplyResult.STATUS_SUCCESS);
	// result.setModule(document);
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// return result;
	// }

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		String queryOrderUrl = DingxinFlowTransVO.createQueryOrderUrl(String.valueOf(supplyOrder.getId()));
		logger.warn("鼎信订单查询请求:" + queryOrderUrl);
		SupplyResult<Document> queryResult = MultiThreadedHttpConnection.getInstance().sendDataByGet(queryOrderUrl);
		if (queryResult.isSuccess()) {
			Document doc = queryResult.getModule();
			Element root = doc.getRootElement();
			logger.warn("鼎信订单查询返回:" + doc.asXML());
			System.out.println(doc.asXML());
			String state = root.elementText("state");// 订单状态
			String error = root.elementText("error");
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			ChargeInfo chargeInfo = new ChargeInfo();
			StringBuffer sb = new StringBuffer();
			chargeInfo.setStatus(state);
			if (!"0".equals(error)) {// 单子错误了
				sb.append("【状态:充值失败】").append("【错误码:").append(error).append(",错误描述:").append(codeResultMap.get(error)).append("】");
			} else {
				if (DingxinFlowTransVO.isChargeSuccess(state)) {
					sb.append("【状态:").append(state).append(",描述:充值成功】");
				} else if (DingxinFlowTransVO.isChargeing(state)) {
					sb.append("【状态:").append(state).append(",描述:充值中】");
				} else if (DingxinFlowTransVO.isWaitPay(state)) {
					sb.append("【状态:").append(state).append(",描述:等待扣款】");
				} else if (DingxinFlowTransVO.isChargeFailed(state)) {
					sb.append("【状态:").append(state).append(",描述:充值失败】");
				} else {
					sb.append("【状态:").append(state).append(",描述:未知】");
				}
			}
			chargeInfo.setStatusDesc(sb.toString());
			result.setModule(chargeInfo);
		} else {
			result.setResultMsg("网络连接失败");
		}
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		String queryBalanceUrl = DingxinFlowTransVO.createQueryBalanceUrl();
		logger.warn("鼎信余额查询请求:" + queryBalanceUrl);
		SupplyResult<Document> queryResult = MultiThreadedHttpConnection.getInstance().sendDataByGet(queryBalanceUrl);
		if (queryResult.isSuccess()) {
			Document doc = queryResult.getModule();
			Element root = doc.getRootElement();
			logger.warn("鼎信余额查询返回:" + doc.asXML());
			System.out.println(doc.asXML());
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
			StringBuffer sb = new StringBuffer();
			String lastMoney = root.elementText("lastMoney");
			String tag = root.elementText("tag");
			String error = root.elementText("error");
			sb.append("【账户余额:" + lastMoney + "】");
			balanceQueryInfo.setBalance(lastMoney);
			if ("0".equals(tag)) {
				sb.append("【用户状态:正常】");
			} else if ("1".equals(tag)) {
				sb.append("【用户状态:暂停】");
			}
			if (error != null && error.length() > 0) {
				sb.append("【错误码:" + error + ",错误描述:" + codeResultMap.get(error) + "】");
			}
			balanceQueryInfo.setBalance(sb.toString());
			result.setModule(balanceQueryInfo);
		} else {
			result.setResultMsg("网络连接失败");
		}
		return result;
	}

	/**
	 * 是否提交成功
	 * 
	 * @param error
	 * @return
	 */
	private boolean isSubmitSuccess(String error) {
		if (error == null || error.length() < 1 || "0".equals(error))
			return true;
		return false;
	}

	/**
	 * 获取流量值
	 * 
	 * @param sizeString
	 * @return
	 */
	public static int getFlowSize(String sizeString) {
		if (sizeString.contains("M")) { // 将带有M的字符串（如10M，50M）转为数字
			String s = sizeString.substring(0, sizeString.length() - 1);
			int size = Integer.parseInt(s);
			return size;
		} else if (sizeString.contains("G")) {// 将带有G的面值转为数字
			String s = sizeString.substring(0, sizeString.length() - 1);
			int size = Integer.parseInt(s) * 1024;
			return size;
		}
		return 0;
	}

	public static void main(String[] args) {
		DingxinFlowTransVO vo = new DingxinFlowTransVO("123456", "15168318002", 10);
		System.out.println("充值请求");
		System.out.println(vo.createChargeParam());
		System.out.println("余额查询");
		System.out.println(DingxinFlowTransVO.createQueryBalanceUrl());
		System.out.println("订单查询");
		System.out.println(DingxinFlowTransVO.createQueryOrderUrl("123456"));
	}

}
