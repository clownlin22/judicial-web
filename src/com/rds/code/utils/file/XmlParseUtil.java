package com.rds.code.utils.file;

import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlParseUtil {

	public static String getXmlValue(String path, String key) {
		SAXReader saxReader = new SAXReader();
		try {
			Document dom = saxReader.read(path);
			Element element = dom.getRootElement();
			String value = element.element(key).getText();
			return value;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void updateXmlValue(String path,String key,String value){
		SAXReader saxReader = new SAXReader();
		OutputFormat out = OutputFormat.createPrettyPrint();
		out.setEncoding("UTF-8");	
		XMLWriter xmlWriter=null;
			try {
				Document dom = saxReader.read(path);
				xmlWriter = new XMLWriter(new FileWriter(path), out);
				Element element = dom.getRootElement();
				element.element(key).setText(value);
				xmlWriter.write(dom);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					xmlWriter.close();
				} catch (IOException e) {
				    e.printStackTrace();
				}
			}
	}
}
