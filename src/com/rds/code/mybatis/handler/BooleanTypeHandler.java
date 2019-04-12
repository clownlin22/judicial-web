package com.rds.code.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

@SuppressWarnings("rawtypes")
public class BooleanTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet arg0, String arg1) throws SQLException {
		String str = arg0.getString(arg1);  
        Boolean rt = Boolean.FALSE;  
        if (str.equalsIgnoreCase("true")){  
            rt = Boolean.TRUE;  
        }  
        return rt;
	}

	@Override
	public Object getResult(ResultSet arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getResult(CallableStatement arg0, int arg1) throws SQLException {
		Boolean b = arg0.getBoolean(arg1);  
        return b == true ? "true" : "false";  
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, Object arg2,
			JdbcType arg3) throws SQLException {
		Boolean b = (Boolean) arg2;  
        String value = (Boolean) b == true ? "true" : "false";  
        arg0.setString(arg1, value);
		
	}

}
