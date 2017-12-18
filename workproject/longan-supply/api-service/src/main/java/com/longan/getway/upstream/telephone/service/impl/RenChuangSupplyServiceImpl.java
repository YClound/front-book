package com.longan.getway.upstream.telephone.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.Utils;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.telephone.vo.RenChuangRequestPackage;
import com.longan.getway.utils.SocketConnection;

public class RenChuangSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = false;
	private final static String requestIp = Utils.getProperty("renChuangSupply.ip");
	private final static Integer requestPort = Integer.parseInt(Utils
			.getProperty("renChuangSupply.port"));

	@Resource
	private LocalCachedService localCachedService;

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
		RenChuangRequestPackage rcrp = new RenChuangRequestPackage(supplyOrder.getItemUid(),
				supplyOrder.getItemFacePrice() / 10 + "");
		String requestPkg = rcrp.toString();
		SupplyResult<String> supplyResult = SocketConnection.getInstance().sendPackage(requestIp,
				requestPort, requestPkg);

		if (supplyResult.isFailed()) {
			result.setResultMsg(supplyResult.getResultMsg());
			return result;
		}

		if (supplyResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("充值处理中");
			return result;
		}

		if (supplyResult.isUndifend()) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}

		if (supplyResult.isSuccess()) {
			supplyOrder.setUpstreamDate(new Date());
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			logger.warn("response: " + supplyResult.getModule());
			String responsePkg = supplyResult.getModule();
			if (!(responsePkg.length() >= 12)) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("上游返回信息错误");
				return result;
			}
			String resultCode = responsePkg.substring(4, 12);
			if (!("10000000".equals(resultCode) || "00000000".equals(resultCode)
					|| "21900005".equals(resultCode) || "21900004".equals(resultCode))) {
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultCode(resultCode);
				result.setResultMsg(resultCode + "_"
						+ responsePkg.substring(12, responsePkg.length()));
				return result;
			}
			if ("10000000".equals(resultCode) && responsePkg.trim().length() > 27) {
				String upstreamSerialno = responsePkg.substring(12, 28);
				supplyOrder.setUpstreamSerialno(upstreamSerialno);
			}
		}
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		return result;
	}

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		ChargeInfo chargeInfo = new ChargeInfo();
		chargeInfo.setStatusDesc("<a href=\"http://www.rcyt.net/sysframe.jsp\">点击进入上游平台核单</a>");
		result.setModule(chargeInfo);
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		balanceQueryInfo
				.setMsg("该上游不支持余额查询	 <a href=\"http://www.rcyt.net/sysframe.jsp\">点击进入上游平台查询</a>");
		return result;
	}
}
