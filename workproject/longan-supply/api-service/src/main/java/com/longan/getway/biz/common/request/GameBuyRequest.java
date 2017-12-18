package com.longan.getway.biz.common.request;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class GameBuyRequest extends Request {
	@NotBlank(message = "商品编号不能为空")
	@NotNull(message = "商品编号不能为空")
	@RegExp(value = "^\\d{1,8}$", message = "商品编号必须是1到8位数字")
	private String itemId;

	@RegExp(value = "^\\d{0,11}$", message = "价格必须是1到8位数字")
	private String itemPrice;

	@NotBlank(message = "购买数量不能为空")
	@NotNull(message = "购买数量不能为空")
	@RegExp(value = "^\\d{1,4}$", message = "购买数量必须是1到4位数字")
	private String amt;

	private String ext1;
	private String ext2;
	private String ext3;

	@NotBlank(message = "网游账号不能为空")
	@NotNull(message = "网游账号不能为空")
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

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
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

}
