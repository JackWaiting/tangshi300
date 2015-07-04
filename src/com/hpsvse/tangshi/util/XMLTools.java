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
	 * ����XML�ļ�
	 * @param is	XML�ļ�������
	 * @return	ʫ�ʵļ���
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static List<Poem> readXML(InputStream is) throws XmlPullParserException, IOException{
		List<Poem> poems = null;
		Poem poem = null;
		// ����һ��pull������
		XmlPullParser parser = Xml.newPullParser();
		// ���ý�������Դ�Լ���ʽ
		parser.setInput(is,"utf-8");
		int type = parser.getEventType();
		while(type != XmlPullParser.END_DOCUMENT){
			switch (type) {
			case XmlPullParser.START_TAG:
				// ����ǩ��Ϊrootʱ
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
			// ������һ��
			type = parser.next();
		}
		return poems;
	}
}
