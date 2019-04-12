package com.rds.alcohol.code.regressionLine;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RegressionLine {

	/** sum of x */
	private double sumX;
	/** sum of y */
	private double sumY;
	/** sum of x*x */
	private double sumXX;
	/** sum of x*y */
	private double sumXY;
	/** sum of y*y */
	private double sumYY;
	private Double[] xy;

	private ArrayList<Double> listX;
	private ArrayList<Double> listY;

	private int XMin, XMax, YMin, YMax;

	/** line coefficient a0 */
	private double a0;
	/** line coefficient a1 */
	private double a1;

	/** number of data points */
	private int pn;
	/** true if coefficients valid */
	private boolean coefsValid;

	/**
	 * Constructor.
	 */
	public RegressionLine() {
		XMax = 0;
		YMax = 0;
		pn = 0;
		xy = new Double[2];
		listX = new ArrayList<Double>();
		listY = new ArrayList<Double>();
	}

	/**
	 * Constructor.
	 * 
	 * @param data
	 *            the array of data points
	 */
	public RegressionLine(DataPoint data[]) {
		pn = 0;
		xy = new Double[2];
		listX = new ArrayList<Double>();
		listY = new ArrayList<Double>();
		for (int i = 0; i < data.length; ++i) {
			addDataPoint(data[i]);
		}
	}

	/**
	 * Return the current number of data points.
	 * 
	 * @return the count
	 */
	public int getDataPointCount() {
		return pn;
	}

	/**
	 * Return the coefficient a0.
	 * 
	 * @return the value of a0
	 */
	public double getA0() {
		validateCoefficients();
		return a0;
	}

	/**
	 * Return the coefficient a1.
	 * 
	 * @return the value of a1
	 */
	public double getA1() {
		validateCoefficients();
		return a1;
	}

	/**
	 * Return the sum of the x values.
	 * 
	 * @return the sum
	 */
	public double getSumX() {
		return sumX;
	}

	/**
	 * Return the sum of the y values.
	 * 
	 * @return the sum
	 */
	public double getSumY() {
		return sumY;
	}

	/**
	 * Return the sum of the x*x values.
	 * 
	 * @return the sum
	 */
	public double getSumXX() {
		return sumXX;
	}

	/**
	 * Return the sum of the x*y values.
	 * 
	 * @return the sum
	 */
	public double getSumXY() {
		return sumXY;
	}

	public double getSumYY() {
		return sumYY;
	}

	public int getXMin() {
		return XMin;
	}

	public int getXMax() {
		return XMax;
	}

	public int getYMin() {
		return YMin;
	}

	public int getYMax() {
		return YMax;
	}

	/**
	 * Add a new data point: Update the sums.
	 * 
	 * @param dataPoint
	 *            the new data point
	 */
	public void addDataPoint(DataPoint dataPoint) {
		sumX += dataPoint.x;
		sumY += dataPoint.y;
		sumXX += dataPoint.x * dataPoint.x;
		sumXY += dataPoint.x * dataPoint.y;
		sumYY += dataPoint.y * dataPoint.y;

		if (dataPoint.x > XMax) {
			XMax = (int) dataPoint.x;
		}
		if (dataPoint.y > YMax) {
			YMax = (int) dataPoint.y;
		}

		// ��ÿ����ľ���������ArrayList�У�����

		xy[0] = dataPoint.x;
		xy[1] = dataPoint.y;
		if (dataPoint.x != 0 && dataPoint.y != 0) {
			System.out.print(xy[0] + ",");
			System.out.println(xy[1]);

			try {
				// System.out.println("n:"+n);
				listX.add(pn, xy[0]);
				listY.add(pn, xy[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * System.out.println("N:" + n);
			 * System.out.println("ArrayList listX:"+ listX.get(n));
			 * System.out.println("ArrayList listY:"+ listY.get(n));
			 */
		}
		++pn;
		coefsValid = false;
	}

	/**
	 * Return the value of the regression line function at x. (Implementation of
	 * Evaluatable.)
	 * 
	 * @param x
	 *            the value of x
	 * @return the value of the function at x
	 */
	public double at(int x) {
		if (pn < 2)
			return Double.NaN;

		validateCoefficients();
		return a0 + a1 * x;
	}

	public double at(double x) {
		if (pn < 2)
			return Double.NaN;

		validateCoefficients();
		return a0 + a1 * x;
	}

	/**
	 * Reset.
	 */
	public void reset() {
		pn = 0;
		sumX = sumY = sumXX = sumXY = 0;
		coefsValid = false;
	}

	/**
	 * Validate the coefficients. ���㷽��ϵ�� y=ax+b �е�a
	 */
	private void validateCoefficients() {
		if (coefsValid)
			return;

		if (pn >= 2) {
			double xBar = sumX / pn;
			double yBar = sumY / pn;

			a1 = ((pn * sumXY - sumX * sumY) / (pn * sumXX - sumX * sumX));
			a0 = (yBar - a1 * xBar);
		} else {
			a0 = a1 = Double.NaN;
		}

		coefsValid = true;
	}

	public double getR() {
		double fenmuX = 0;
		double fenmuY = 0;
		double fenzi = 0;
		double xBar = sumX / pn;
		double yBar = sumY / pn;
		for (int i = 0; i < pn - 1; i++) {
			fenmuX += (listX.get(i) - xBar) * (listX.get(i) - xBar);
			fenmuY += (listY.get(i) - yBar) * (listY.get(i) - yBar);
			fenzi += (listX.get(i) - xBar) * (listY.get(i) - yBar);
		}
		if (fenmuX * fenmuY == 0)
			return 0;
		else
			return (fenzi * fenzi) / (fenmuX * fenmuY);
	}

	public double round(double v, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	public float round(float v, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();

	}
}
