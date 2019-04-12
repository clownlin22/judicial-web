package com.rds.judicial.web.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rds.judicial.model.AlgModel;
import com.rds.judicial.model.AlgReturnValueModel;
import com.rds.judicial.web.common.FloatComparator;
import com.rds.judicial.web.common.SortSizeComparator;

public class AlgMain {

	public static void main(String[] args) {
		
		String father = "11.2,11.3";
		String son ="12.2,";
	
		AlgMain main = new AlgMain(son, father);
		AlgReturnValueModel model = main.getAlgResult();
		
		System.out.println("    "+son+" "+father+"--"+model.getStep()+", "+model.getPossibility()+"   function:"+model.getFunction());
		

	}
	
	private Double genonebyone = 0.0;
	private Double genonebytwo = 0.0;
	private Double gentwobyone = 0.0;
	private Double gentwobytwo = 0.0;
	
	private double A1 = 0.0;
	private double A2 = 0.0;
	
	public AlgMain(String childStr, String parentStr){
		String[] childs = childStr.split(",");
		String[] parents = parentStr.split(",");
		  if (childs.length==1)  {
			  A1 = Double.parseDouble(childs[0]);
				A2 = Double.parseDouble(childs[0]); 
		  }else {
			  A1 = Double.parseDouble(childs[0]);
				A2 = Double.parseDouble(childs[1]);
		}
		

		if (childs.length == 1) {
			if (parents.length == 1) {
				genonebyone = Math.abs(Double.parseDouble(childs[0]) - Double.parseDouble(parents[0]));
				genonebytwo = genonebyone;
				gentwobyone = genonebyone;
				gentwobytwo = genonebyone;
			} else {
				genonebyone = Math.abs(Double.parseDouble(childs[0]) - Double.parseDouble(parents[0]));
				genonebytwo = Math.abs(Double.parseDouble(childs[0]) - Double.parseDouble(parents[1]));
				gentwobyone = genonebyone;
				gentwobytwo = genonebytwo;
			}
		} else {
			if (parents.length == 1) {
				genonebyone = Math.abs(Double.parseDouble(childs[0]) - Double.parseDouble(parents[0]));
				genonebytwo = genonebyone;
				gentwobyone = Math.abs(Double.parseDouble(childs[1]) - Double.parseDouble(parents[0]));
				gentwobytwo = gentwobyone;
			} else {
				genonebyone = Math.abs(Double.parseDouble(childs[0]) - Double.parseDouble(parents[0]));
				genonebytwo = Math.abs(Double.parseDouble(childs[0]) - Double.parseDouble(parents[1]));
				gentwobyone = Math.abs(Double.parseDouble(childs[1]) - Double.parseDouble(parents[0]));
				gentwobytwo = Math.abs(Double.parseDouble(childs[1]) - Double.parseDouble(parents[1]));
			}
		}

		DecimalFormat df = new DecimalFormat("#.0");
		genonebyone = Double.valueOf(df.format(genonebyone));
		genonebytwo = Double.valueOf(df.format(genonebytwo));
		gentwobyone = Double.valueOf(df.format(gentwobyone));
		gentwobytwo = Double.valueOf(df.format(gentwobytwo));
	}

	public AlgReturnValueModel getAlgResult() {

		List<AlgModel> list = new ArrayList<AlgModel>();
		AlgModel algModel = new AlgModel();
		algModel.setIndex(1);
		algModel.setV_dif(genonebyone);
		list.add(algModel);

		algModel = new AlgModel();
		algModel.setIndex(2);
		algModel.setV_dif(genonebytwo);
		list.add(algModel);

		algModel = new AlgModel();
		algModel.setIndex(3);
		algModel.setV_dif(gentwobyone);
		list.add(algModel);

		algModel = new AlgModel();
		algModel.setIndex(4);
		algModel.setV_dif(gentwobytwo);
		list.add(algModel);
		
		//����
		sort(list);
		
		for (AlgModel aa : list) {
			System.out.print(aa.getIndex()+"-"+aa.getV_dif()+", ");
		}
		//��ȡλ�ÿ�����
		return algPossibility(list);
		
	}

	/**
	 * ����
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<AlgModel> sort(List<AlgModel> list) {
		SortSizeComparator comparator = new SortSizeComparator();
		Collections.sort(list, comparator);
		FloatComparator comparator2 = new FloatComparator();
		Collections.sort(list, comparator2);
		return list;
	}

	/**
	 * ��ȡ������������
	 * 
	 * @param list
	 * @return
	 */
	private AlgReturnValueModel algPossibility(List<AlgModel> list) {
		int first = 0;
		int second = 0;
		Double v_first = list.get(0).getV_dif();
		//��һ���Ƿ�ΪС��
		boolean isint = isIntegerForDouble(v_first);
		if(!isint){
			v_first = Math.ceil(v_first);
		}
		for (int i = 0; i < list.size(); i++) {
			AlgModel model = (AlgModel) list.get(i);
			double value = 0;
			if(!isint){
				value = Math.ceil(model.getV_dif());
			}else{
				value = model.getV_dif();
			}
			if(value == v_first){
				if (model.getIndex() <= 2) {
					first++;
				} else {
					second++;
				}
			}
		}
		
		AlgReturnValueModel model = new AlgReturnValueModel();
		model.setStep(v_first);
		String possibility = "";
		if(A1==A2  &&(list.get(0).getV_dif()==list.get(1).getV_dif()&&list.get(0).getV_dif()==list.get(2).getV_dif()&&list.get(0).getV_dif()==list.get(3).getV_dif()  )){
			possibility = 4 + "+" + 0;
		}else {
			possibility = first + "+" + second;
		}
		model.setPossibility(possibility);
		model.setFunction(getFormulaTranslation(possibility));
		return model;
	}
	
	/**
	 * �ж�double�Ƿ�������
	 * @param obj
	 * @return
	 */
	public static boolean isIntegerForDouble(double obj) {
		double eps = 1e-10;  // ���ȷ�Χ
		return obj - (double)((int)obj) < eps;
	}

	/**
	 * ��ȡ��ʽ
	 * 
	 * @return
	 */
	public String getFormulaTranslation(String possibility) {
		String formulaTranslation = null;

		if ("1+0".equals(possibility)) {
			formulaTranslation ="μ/8p*10(n-1)";
		} else if ("0+1".equals(possibility)) {
			formulaTranslation = "μ/8q*10(n-1)";
		} else if ("2+0".equals(possibility)) {
			formulaTranslation = "μ/4p*10(n-1)";
		} else if ("1+1".equals(possibility)) {
			formulaTranslation = "μ(p+q)/8pq*10(n-1)";
		} else if ("0+2".equals(possibility)) {
			formulaTranslation = "μ/4q*10(n-1)";
		} else if ("1+2".equals(possibility)) {
			formulaTranslation = "μ(2p+q)/8pq*10(n-1)";
		} else if ("2+1".equals(possibility)) {
			formulaTranslation = "μ(p+2q)/8pq*10(n-1)";
		} else if ("2+2".equals(possibility)) {
			formulaTranslation = "μ(p+q)/4pq*10(n-1)";
		}else if ("4+0".equals(possibility)) {
			formulaTranslation = "μ/2p*10(n-1)";
		}
		return formulaTranslation;
	}

}
