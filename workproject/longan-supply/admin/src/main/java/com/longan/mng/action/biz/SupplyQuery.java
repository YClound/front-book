package com.longan.mng.action.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.CardChargeInfo;
import com.longan.client.remote.domain.CardCheck;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.client.remote.service.SupplyQueryService;
import com.longan.mng.action.common.BaseController;

@Controller
@RequestMapping("biz/supplyQuery")
public class SupplyQuery extends BaseController {

	@Resource
	private SupplyQueryService supplyQueryService;

	@Resource
	private BizOrderService bizOrderService;

	@Resource
	private SupplyOrderService supplyOrderService;

	@RequestMapping(params = "type=cardCheck")
	public void cardCheck(@RequestParam("stockId") Long stockId, Model model) {
		Result<CardCheck> result = null;
		try {
			result = supplyQueryService.cardCheck(stockId);
		} catch (Exception e) {
			logger.error("cardCheck 查询失败", e);
			model.addAttribute("result", result);
			model.addAttribute("stockId", stockId);
			return;
		}

		model.addAttribute("result", result);
		model.addAttribute("stockId", stockId);
	}

	@RequestMapping(params = "type=cardChargeInfoQuery")
	public void cardChargeInfoQuery(@RequestParam("stockId") Long stockId, Model model) {
		Result<List<CardChargeInfo>> result = null;
		try {
			result = supplyQueryService.cardChargeInfoQuery(stockId);
		} catch (Exception e) {
			logger.error("cardInfoQuery 查询失败", e);
			model.addAttribute("msg", "查询失败");
			return;
		}

		String msg = "";
		if (result.isSuccess()) {
			if (result.getModule() != null) {
				for (CardChargeInfo unicomFlowCardInfo : result.getModule()) {
					msg = msg + unicomFlowCardInfo;
				}
			}
		} else {
			msg = msg + result.getResultMsg();
		}
		model.addAttribute("msg", msg);
		model.addAttribute("result", result);
		model.addAttribute("stockId", stockId);
	}

	@RequestMapping(params = "type=chargeQuery")
	public void chargeQuery(@RequestParam("id") Long id, Model model) {
		Result<ChargeInfo> result = new Result<ChargeInfo>();
		model.addAttribute("result", result);
		Result<SupplyOrder> bizOrderQuery = supplyOrderService.getSupplyOrderById(id);
		if (!bizOrderQuery.isSuccess()) {
			result.setResultMsg(bizOrderQuery.getResultMsg());
			return;
		}
		SupplyOrder supplyOrder = bizOrderQuery.getModule();
		if (supplyOrder == null) {
			result.setResultMsg("没有该供货单");
			return;
		}

		try {
			result = supplyQueryService.chargeInfoQuery(supplyOrder);
		} catch (Exception e) {
			logger.error("chargeInfoQuery 查询失败", e);
			return;
		}

		model.addAttribute("result", result);
	}

	@RequestMapping(params = "type=balanceQuery")
	public void balanceQuery(@RequestParam("userId") String userId, Model model) {
		Result<BalanceQueryInfo> result = new Result<BalanceQueryInfo>();
		model.addAttribute("result", result);
		try {
			result = supplyQueryService.balanceQuery(userId);
		} catch (Exception e) {
			logger.error("balanceQuery 查询失败", e);
			return;
		}
		model.addAttribute("result", result);
	}
}
