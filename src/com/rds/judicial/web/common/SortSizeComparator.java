/**
 * ��һ������Ӵ�С
 * 
 */
package com.rds.judicial.web.common;

import java.util.Comparator;

import com.rds.judicial.model.AlgModel;

@SuppressWarnings("rawtypes")
public class SortSizeComparator implements Comparator  {
	
	@Override
	public int compare(Object o1, Object o2) {
		AlgModel a1 = (AlgModel)o1;
		AlgModel a2 = (AlgModel)o2;
		Double v_dif1 = a1.getV_dif();
		Double v_dif2 = a2.getV_dif();
		if (v_dif1  > v_dif2) {
			return 1;
		} else {
			return -1;
		}
	}
}
