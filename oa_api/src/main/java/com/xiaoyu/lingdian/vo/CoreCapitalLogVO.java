package com.xiaoyu.lingdian.vo;

import com.xiaoyu.lingdian.entity.CoreCapitalLog;
import com.xiaoyu.lingdian.vo.BaseVO;
import com.xiaoyu.lingdian.tool.DateUtil;

/**
* 资金变动表
*/
public class CoreCapitalLogVO implements BaseVO {

	/**
	*标识UUID
	*/
	private String crcpaUuid;

	/**
	*创建日期
	*/
	private String crcpaCdate;

	/**
	*更新人
	*/
	private String crcpaUser;

	/**
	*更新对象
	*/
	private String crcpaBusi;

	/**
	*更新说明
	*/
	private String crcpaDesc;

	/**
	*更新类型:1集团2项目3管理员4投资客
	*/
	private Integer crcpaType;

	public void setCrcpaUuid(String crcpaUuid) {
		this.crcpaUuid = crcpaUuid;
	}

	public String getCrcpaUuid( ) {
		return crcpaUuid;
	}

	public void setCrcpaCdate(String crcpaCdate) {
		this.crcpaCdate = crcpaCdate;
	}

	public String getCrcpaCdate( ) {
		return crcpaCdate;
	}

	public void setCrcpaUser(String crcpaUser) {
		this.crcpaUser = crcpaUser;
	}

	public String getCrcpaUser( ) {
		return crcpaUser;
	}

	public void setCrcpaBusi(String crcpaBusi) {
		this.crcpaBusi = crcpaBusi;
	}

	public String getCrcpaBusi( ) {
		return crcpaBusi;
	}

	public void setCrcpaDesc(String crcpaDesc) {
		this.crcpaDesc = crcpaDesc;
	}

	public String getCrcpaDesc( ) {
		return crcpaDesc;
	}

	public void setCrcpaType(Integer crcpaType) {
		this.crcpaType = crcpaType;
	}

	public Integer getCrcpaType( ) {
		return crcpaType;
	}

	public CoreCapitalLogVO( ) { 
	}

	@Override
	public void convertPOToVO(Object poObj) {
		if (null == poObj) {
			return;
		}

		CoreCapitalLog po = (CoreCapitalLog) poObj;
		this.crcpaUuid = po.getCrcpaUuid();
		this.crcpaCdate = po.getCrcpaCdate()!=null?DateUtil.formatDefaultDate(po.getCrcpaCdate()):"";
		this.crcpaUser = po.getCrcpaUser();
		this.crcpaBusi = po.getCrcpaBusi();
		this.crcpaDesc = po.getCrcpaDesc();
		this.crcpaType = po.getCrcpaType();
	}
//<=================定制内容开始==============
//==================定制内容结束==============>

}