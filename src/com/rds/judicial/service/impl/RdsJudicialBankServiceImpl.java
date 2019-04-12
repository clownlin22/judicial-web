package com.rds.judicial.service.impl;

import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.judicial.mapper.RdsJudicialBankMapper;
import com.rds.judicial.service.RdsJudicialBankService;
/**
 * @description 银行账户管理
 * @author 傅少明
 * 2015年4月29日
 */
@Service("RdsJudicialBankService")
@Transactional
public class RdsJudicialBankServiceImpl implements RdsJudicialBankService{

	@Setter
	@Autowired
	private RdsJudicialBankMapper bankMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return bankMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		// TODO Auto-generated method stub
		return bankMapper.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return bankMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		// TODO Auto-generated method stub
		return bankMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		// TODO Auto-generated method stub
		return bankMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return bankMapper.delete(params);
	}

	@Override
	public List<Map<String, Object>> getBankname() {
		return bankMapper.getBankname();
	}

	@Override
	public List<Map<String, Object>> getCompany() {
		return bankMapper.getCompany();
	}


}
