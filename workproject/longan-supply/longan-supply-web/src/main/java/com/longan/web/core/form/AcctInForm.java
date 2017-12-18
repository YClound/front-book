package com.longan.web.core.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class AcctInForm {
	@NotNull(message = "必须选择银行信息")
	@NotBlank(message = "必须选择银行信息")
	private String bank;

	@NotBlank(message = "请填写加款金额")
	@RegExp(value = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$", message = "金额格式不对")
	@Length(max = 11, message = "金额超过最大长度")
	private String amount;

	private String bankSerialNO;

	public String getBank() {
		return bank; 
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBankSerialNO() {
		return bankSerialNO;
	}

	public void setBankSerialNO(String bankSerialNO) {
		this.bankSerialNO = bankSerialNO;
	}

}
