package com.xiaoyu.lingdian.enums;

public enum CrflwType {

	APPLY("申请资金流程"),
	FUNDING("拨款流程");

	private String description;

	CrflwType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
