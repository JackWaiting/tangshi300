package com.hpsvse.tangshi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.hpsvse.tangshi.entity.Poem;

public class XMLTools {

	/**
	 * 解析XML文件
	 * @param is	XML文件输入流
	 * @return	诗词的集合
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static List<Poem> readXML(InputStream is) throws XmlPullParserException, IOException{
		List<Poem> poems = null;
		Poem poem = null;
		// 构造一个pull解析器
		XmlPullParser parser = Xml.newPullParser();
		// 设置解析的资源以及格式
		parser.setInput(is,"utf-8");
		int type = parser.getEventType();
		while(type != XmlPullParser.END_DOCUMENT){
			switch (type) {
			case XmlPullParser.START_TAG:
				// 当标签名为root时
				if ("root".equals(parser.getName())) {
					poems = new ArrayList<Poem>();
				}else if ("node".equals(parser.getName())) {
					poem = new Poem();
				}else if ("title".equals(parser.getName())) {
					poem.setTitle(parser.nextText());
				}else if ("auth".equals(parser.getName())) {
					poem.setAuth(parser.nextText());
				}else if ("type".equals(parser.getName())) {
					poem.setType(parser.nextText());
				}else if ("content".equals(parser.getName())) {
					poem.setContent(parser.nextText());
				}else if ("desc".equals(parser.getName())) {
					poem.setDesc(parser.nextText());
				}
				break;

			case XmlPullParser.END_TAG:
				if ("node".equals(parser.getName())) {
					poems.add(poem);
					poem = null;
				}
				break;
			}
			// 解析下一个
			type = parser.next();
		}
		return poems;
	}
}
