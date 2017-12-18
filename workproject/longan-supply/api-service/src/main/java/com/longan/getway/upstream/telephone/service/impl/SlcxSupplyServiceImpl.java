package com.longan.getway.upstream.telephone.service.impl;

import java.io.StringReader;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.telephone.vo.SlcxChargeRequestVO;
import com.longan.getway.upstream.telephone.vo.SlcxQueryRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class SlcxSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;

	@Resource
	private LocalCachedService localCachedService;

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		SlcxChargeRequestVO vo = new SlcxChargeRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setUidCli(supplyOrder.getItemUid());
		vo.setPrice(supplyOrder.getItemFacePrice() / 1000 + "");
		vo.setJnoCli(supplyOrder.getId() + "");
		vo.createChargeSign();

		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByPost(vo.createChargeAciton(), vo.createChargeParam());
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
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String code = root.elementText("retcode");
				String desc = root.elementText("retmsg");

				if ("000000".equals(code)) { // 扣款成功
					String upstreamSerialno = root.elementText("oid_goodsorder");
					supplyOrder.setUpstreamSerialno(upstreamSerialno);
				} else {
					result.setResultCode(code);
					result.setResultMsg(code + "_" + desc);
					return result;
				}
			} catch (DocumentException e) {
				logger.error("parse document error", e);
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}

		result.setStatus(SupplyResult.STATUS_SUCCESS);
		return result;
	}

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		if (supplyOrder == null) {
			result.setResultMsg("参数错误");
			return result;
		}

		SlcxQueryRequestVO vo = new SlcxQueryRequestVO();
		vo.setJnoCli(supplyOrder.getId() + "");
		vo.createQuerySign();

		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByPost(vo.createQueryAciton(), vo.createQueryParam());
		ChargeInfo chargeInfo = new ChargeInfo();
		result.setModule(chargeInfo);

		if (supplyResult.isFailed()) {
			result.setResultMsg(supplyResult.getResultMsg());
			return result;
		}

		if (supplyResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("查询超时");
			return result;
		}

		if (supplyResult.isUndifend()) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}

		if (supplyResult.isSuccess()) {
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				logger.warn("response: " + root.asXML());

				String code = root.elementText("retcode");
				String desc = root.elementText("retmsg");

				if (StringUtils.isEmpty(code)) { // 查询成功
					chargeInfo.setStatus(root.elementText("stat_goodsorder"));
					chargeInfo.setStatusDesc(root.elementText("retmsg"));
					chargeInfo.setActualCost(BigDecimalUtils.multInteger(root
							.elementText("rprice_sale")));
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(code);
					result.setResultMsg(code + "_" + desc);
					return result;
				}
			} catch (DocumentException e) {
				logger.error("parse document error", e);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}

		result.setStatus(SupplyResult.STATUS_SUCCESS);
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		SlcxQueryRequestVO vo = new SlcxQueryRequestVO();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByPost(vo.createQueryBalanceAction(), vo.createQureyBalanceParam());
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
		if (supplyResult.isFailed()) {
			result.setResultMsg(supplyResult.getResultMsg());
			return result;
		}

		if (supplyResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("查询超时");
			return result;
		}

		if (supplyResult.isUndifend()) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}

		if (supplyResult.isSuccess()) {
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String balance = root.elementText("leftmoney");
				if (!StringUtils.isEmpty(balance)) { // 查询成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					balanceQueryInfo.setMsg("查询余额成功");
					balanceQueryInfo.setBalance(balance);
					return result;
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					return result;
				}
			} catch (DocumentException e) {
				logger.error("parse document error", e);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}
		return result;
	}
}
