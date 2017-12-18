package com.longan.getway.biz.common.response;

public class NewBizOrderData extends BaseData {
	private String serviceorder;//Id
	private String orderno;//serialno
	private Long amount;//amount
	private Integer count;//amt
	private Integer status;//status
	private String statusdesc;//statusDesc
	private Integer goods;//itemId
	private String createtime;//gmtCreate
	private String finishtime;//gmtModify
	
	public NewBizOrderData(){
		super();
	}
	public NewBizOrderData(BizOrderData bod){
		super();
		this.serviceorder=bod.getId().toString();
		this.orderno=bod.getSerialno();
		this.amount=bod.getAmount();
		this.count=bod.getAmt();
		this.status=bod.getStatus();
		this.statusdesc=bod.getStatusDesc();
		this.goods=bod.getItemId();
		this.createtime=bod.getGmtCreate();
		this.finishtime=bod.getGmtModify();
	}
	public String getServiceorder() {
		return serviceorder;
	}
	public void setServiceorder(String serviceorder) {
		this.serviceorder = serviceorder;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getGoods() {
		return goods;
	}
	public void setGoods(Integer goods) {
		this.goods = goods;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getFinishtime() {
		return finishtime;
	}
	public void setFinishtime(String finishtime) {
		this.finishtime = finishtime;
	}
	public String getStatusdesc() {
		return statusdesc;
	}
	public void setStatusdesc(String statusdesc) {
		this.statusdesc = statusdesc;
	}
	
}
