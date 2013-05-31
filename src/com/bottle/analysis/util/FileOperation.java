package com.bottle.analysis.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 文件操作类
 * 
 * @author Bottle
 * 
 * @Date 2013-3-31 下午2:44:26
 */
public class FileOperation {
	/**
	 * 将字符串写入文件
	 * 
	 * @param result
	 */
	public static void WriteFile(String result, String filePath) {
		try {
			File tofile = new File(filePath);
			FileWriter fw = new FileWriter(tofile);
			BufferedWriter buffw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(buffw);
			String str = result;
			pw.println(str);

			pw.close();
			buffw.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析xml配置文件
	 * 
	 * @param filePath
	 * @return
	 */
	public ArrayList<String> ParaseXML(String filePath) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			XMLReader parser = XMLReaderFactory.createXMLReader();
			MyHandler myHandler = new MyHandler();
			parser.setContentHandler(myHandler);
			parser.parse(filePath);
//			System.out.println(myHandler.getNameList());
			list = (ArrayList<String>) myHandler.getNameList();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return list;
	}

	private class MyHandler extends DefaultHandler {
		private List<String> nameList;
		private boolean title = false;

		public List<String> getNameList() {
			return nameList;
		}

		// Called at start of an XML document
		@Override
		public void startDocument() throws SAXException {
//			System.out.println("Start parsing document...");
			nameList = new ArrayList<String>();
		}

		// Called at end of an XML document
		@Override
		public void endDocument() throws SAXException {
//			System.out.println("End");
		}

		/**
		 * Start processing of an element.
		 * 
		 * @param namespaceURI
		 *            Namespace URI
		 * @param localName
		 *            The local name, without prefix
		 * @param qName
		 *            The qualified name, with prefix
		 * @param atts
		 *            The attributes of the element
		 */
		@Override
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
			// Using qualified name because we are not using xmlns prefixes here.
			if (qName.equals("String")) {
				title = true;
			}
		}

		@Override
		public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
			// End of processing current element
			if (title) {
				title = false;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			// Processing character data inside an element
			if (title) {
				String value = new String(ch, start, length);
//				System.out.println("Data value: " + value);
				nameList.add(value);
			}
		}

	}

}
