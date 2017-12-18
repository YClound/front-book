package com.longan.getway.upstream.game.vo;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5GB2312;
import com.longan.biz.utils.UrlEncoding;
import com.longan.biz.utils.Utils;

public class DianDianRequestVO {
	private final static String url = Utils.getProperty("dianDianSupply.URL");
	private final static String chargeAction = Utils.getProperty("dianDianSupply.chargeAction");
	private final static String queryAction = Utils.getProperty("dianDianSupply.queryAction");
	private final static String queryBalanceAction = Utils
			.getProperty("dianDianSupply.queryBalanceAction");
	private final static String key = Utils.getProperty("dianDianSupply.key");

	private final static String coopid = Utils.getProperty("dianDianSupply.coopid");
	private final static String asyncurl = Utils.getProperty("dianDianSupply.asyncurl");
	private final static String ipaddr = Utils.getProperty("dianDianSupply.ipaddr");
	private String tranid;
	private String proid;
	private String quantity;
	private String price;
	private String account;
	private String sign;
	private String acctype;
	private String chargetype;
	private String gamename;
	private String gamearea;
	private String gameserver;
	private String infoa;
	private String infob;
	private String infoc;

	private Map<String, String> tempMap = new TreeMap<String, String>();

	public String getTranid() {
		return tranid;
	}

	public void setTranid(String tranid) {
		this.tranid = tranid;
	}

	public String getProid() {
		return proid;
	}

	public void setProid(String proid) {
		this.proid = proid;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAcctype() {
		return acctype;
	}

	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}

	public String getChargetype() {
		return chargetype;
	}

	public void setChargetype(String chargetype) {
		this.chargetype = chargetype;
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public String getGamearea() {
		return gamearea;
	}

	public void setGamearea(String gamearea) {
		this.gamearea = gamearea;
	}

	public String getGameserver() {
		return gameserver;
	}

	public void setGameserver(String gameserver) {
		this.gameserver = gameserver;
	}

	public String getInfoa() {
		return infoa;
	}

	public void setInfoa(String infoa) {
		this.infoa = infoa;
	}

	public String getInfob() {
		return infob;
	}

	public void setInfob(String infob) {
		this.infob = infob;
	}

	public String getInfoc() {
		return infoc;
	}

	public void setInfoc(String infoc) {
		this.infoc = infoc;
	}

	public String createChargeParam() {
		tempMap.put("coopid", coopid);
		tempMap.put("tranid", tranid);
		tempMap.put("proid", proid);
		tempMap.put("quantity", quantity);
		tempMap.put("price", price);
		tempMap.put("account", account);
		tempMap.put("acctype", acctype);
		tempMap.put("chargetype", chargetype);
		tempMap.put("gamename", gamename);
		tempMap.put("gamearea", gamearea);
		tempMap.put("gameserver", gameserver);
		tempMap.put("infoa", infoa);
		tempMap.put("infob", infob);
		tempMap.put("infoc", infoc);
		tempMap.put("asyncurl", asyncurl);
		tempMap.put("ipaddr", ipaddr);
		StringBuffer signStr = new StringBuffer("");
		for (Map.Entry<String, String> e : tempMap.entrySet()) {
			if (StringUtils.isNotEmpty(e.getValue())) {
				signStr.append(e.getKey() + e.getValue());
			}
		}
		signStr.append(key);
		sign = Md5GB2312.md5(signStr.toString());
		tempMap.put("sign", sign);
		StringBuffer sbf = new StringBuffer("");
		for (Map.Entry<String, String> e : tempMap.entrySet()) {
			if (StringUtils.isNotEmpty(e.getValue())) {
				sbf.append(e.getKey() + "=" + UrlEncoding.urlGB2312(e.getValue()) + "&");
			}
		}
		String reqBody = sbf.toString().substring(0, sbf.toString().length() - 1);
		return createChargeAction() + reqBody;
	}

	public String createChargeAction() {
		return url + chargeAction;
	}

	public String createQueryParam() {
		String signStr = "coopid" + coopid + "tranid" + tranid + key;
		sign = Md5GB2312.md5(signStr);
		tempMap.put("coopid", coopid);
		tempMap.put("tranid", tranid);
		tempMap.put("sign", sign);
		StringBuffer sbf = new StringBuffer("");
		for (Map.Entry<String, String> e : tempMap.entrySet()) {
			if (StringUtils.isNotEmpty(e.getValue())) {
				sbf.append(e.getKey() + "=" + UrlEncoding.urlGB2312(e.getValue()) + "&");
			}
		}
		String reqBody = sbf.toString().substring(0, sbf.toString().length() - 1);
		return createQueryAction() + reqBody;
	}

	public String createQueryAction() {
		return url + queryAction;
	}

	public String createBalanceParam() {
		String signStr = "coopId" + coopid + key;
		sign = Md5GB2312.md5(signStr);
		tempMap.put("coopid", coopid);
		tempMap.put("sign", sign);
		StringBuffer sbf = new StringBuffer("");
		for (Map.Entry<String, String> e : tempMap.entrySet()) {
			if (StringUtils.isNotEmpty(e.getValue())) {
				sbf.append(e.getKey() + "=" + UrlEncoding.urlGB2312(e.getValue()) + "&");
			}
		}
		String reqBody = sbf.toString().substring(0, sbf.toString().length() - 1);
		return createQueryBalanceAction() + reqBody;
	}

	public String createQueryBalanceAction() {
		return url + queryBalanceAction;
	}
}
