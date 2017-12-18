package com.longan.getway.biz.common.request;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class TelephoneBuyRequest extends Request {
	@RegExp(value = "^\\d{1,8}$", message = "商品编号必须是1到8位数字")
	private String itemId;

	@RegExp(value = "^\\d{0,11}$", message = "成本价格必须是1到8位数字")
	private String itemPrice;

	@RegExp(value = "^\\d{1,4}$", message = "购买数量必须是1到4位数字")
	private String amt;

	@RegExp(value = "^\\d{0,11}$", message = "商品面值必须是1到8位数字")
	private String itemFacePrice;

	private String ext1;
	private String ext2;

	@NotBlank(message = "客户手机号不能为空")
	@NotNull(message = "客户手机号不能为空")
	@RegExp(value = "^\\d{1,11}$", message = "手机号不正确")
	private String uid;

	@NotBlank(message = "合作方商户的流水号不能为空")
	@NotNull(message = "合作方商户的流水号不能为空")
	private String serialno;

	private String dtCreate;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getDtCreate() {
		return dtCreate;
	}

	public void setDtCreate(String dtCreate) {
		this.dtCreate = dtCreate;
	}

	public String getItemFacePrice() {
		return itemFacePrice;
	}

	public void setItemFacePrice(String itemFacePrice) {
		this.itemFacePrice = itemFacePrice;
	}

}
