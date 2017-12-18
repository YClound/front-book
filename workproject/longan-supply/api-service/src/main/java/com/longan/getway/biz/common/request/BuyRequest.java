package com.longan.getway.biz.common.request;

import org.apache.commons.lang.StringUtils;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class BuyRequest extends Request{
	/**
	 * 参数修改
	 */
	/**
	 * 参数转换为旧参数方法
	 * @return
	 */
	public void convertToOld(){
		if(!StringUtils.isEmpty(this.goods)){
			this.itemId=this.goods;
			this.goods=null;
		}
		if(!StringUtils.isEmpty(this.price)){
			this.itemPrice=this.price;
			this.price=null;
		}
		if(!StringUtils.isEmpty(this.amount)){
			this.amt=this.amount;
			this.amount=null;
		}
		if(!StringUtils.isEmpty(this.attach1)){
			this.ext1=this.attach1;
			this.attach1=null;
		}
		if(!StringUtils.isEmpty(attach2)){
			this.ext2=this.attach2;
			this.attach2=null;
		}
		if(!StringUtils.isEmpty(this.number)){
			this.uid=this.number;
			this.number=null;
		}
		if(!StringUtils.isEmpty(this.order_no)){
			this.serialno=this.order_no;
			this.order_no=null;
		}
		if(!StringUtils.isEmpty(this.create_time)){
			this.dtCreate=this.create_time;
			this.create_time=null;
		}
	}
	/**
	 * 旧参数转为新参数
	 */
	public void convertToNew(){
		if(!StringUtils.isEmpty(this.itemId)){
			this.goods=this.itemId;
			this.itemId=null;
		}
		if(!StringUtils.isEmpty(this.itemPrice)){
			this.price=this.itemPrice;
			this.itemPrice=null;
		}
		if(!StringUtils.isEmpty(this.amt)){
			this.amount=this.amt;
			this.amt=null;
		}
		if(!StringUtils.isEmpty(this.ext1)){
			this.attach1=this.ext1;
			this.ext1=null;
		}
		if(!StringUtils.isEmpty(ext2)){
			this.attach2=this.ext2;
			this.ext2=null;
		}
		if(!StringUtils.isEmpty(this.uid)){
			this.number=this.uid;
			this.uid=null;
		}
		if(!StringUtils.isEmpty(this.serialno)){
			this.order_no=this.serialno;
			this.serialno=null;
		}
		if(!StringUtils.isEmpty(this.dtCreate)){
			this.create_time=this.dtCreate;
			this.dtCreate=null;
		}
	}
	
	//itemId
	private String goods;
	//itemPrice
	private String price;
	//amt
	private String  amount;
	//ext1
	private String attach1;
	//ext2
	private String attach2;
	//uid
	private String number;
	//serialno
	private String order_no;
	//dtCreate
	private String create_time;
	

	@NotBlank(message = "商品编号不能为空")
	@NotNull(message = "商品编号不能为空")
	@RegExp(value = "^\\d{1,8}$", message = "商品编号必须是1到8位数字")
	private String itemId;

	@RegExp(value = "^\\d{0,11}$", message = "价格必须是1到8位数字")
	private String itemPrice;

	@RegExp(value = "^\\d{1,4}$", message = "购买数量必须是1到4位数字")
	private String amt;

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
/**
 * 参数修改
 * @return
 */
	
	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAttach1() {
		return attach1;
	}

	public void setAttach1(String attach1) {
		this.attach1 = attach1;
	}

	public String getAttach2() {
		return attach2;
	}

	public void setAttach2(String attach2) {
		this.attach2 = attach2;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
