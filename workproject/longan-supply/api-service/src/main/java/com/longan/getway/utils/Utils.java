package com.longan.getway.utils;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Utils {
	private static final String UTF = "UTF-8";

	public Utils() {
	}

	public static String getSysDate() {
		SimpleDateFormat formdate = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date();
		String ss = formdate.format(curDate);
		return ss;
	}

	public static String getSysTime() {
		SimpleDateFormat formdate = new SimpleDateFormat("HH:mm:ss");
		Date curDate = new Date();
		String ss = formdate.format(curDate);
		return ss;
	}

	public static boolean WriteFile(String fileName, String StrBuf) throws IOException {
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(StrBuf, 0, StrBuf.length());
		bw.newLine();
		bw.close();
		fw.close();
		return true;
	}

	public static String FormatStringAddBlank(String sReturnBuf, int length) throws Exception {
		StringBuffer tempBuffer = new StringBuffer();
		if (sReturnBuf == null || sReturnBuf.equals("") || sReturnBuf.equals("null")) {
			for (int i = 0; i < length; i++)
				tempBuffer.append(" ");

			return tempBuffer.toString();
		}
		String s2 = new String(sReturnBuf.getBytes(UTF), "ISO8859_1");
		int iLength = s2.length();
		if (length > iLength) {
			tempBuffer.append(sReturnBuf);
			for (int j = 0; j < length - iLength; j++)
				tempBuffer.append(" ");

			sReturnBuf = tempBuffer.toString();
		} else if (length < iLength)
			sReturnBuf = absoluteSubstring(sReturnBuf, 0, length);
		return sReturnBuf;
	}

	public static String FormatStrbackAddBlank(String sReturnBuf, int length) {
		StringBuffer tempBuffer = new StringBuffer();
		if (sReturnBuf == null || sReturnBuf.equals("") || sReturnBuf.equals("null")) {
			for (int i = 0; i < length; i++)
				tempBuffer.append(" ");

			return tempBuffer.toString();
		}
		String s2 = null;
		try {
			s2 = new String(sReturnBuf.getBytes(UTF), "ISO8859_1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int iLength = s2.length();
		if (length > iLength) {
			for (int j = 0; j < length - iLength; j++)
				sReturnBuf = " " + sReturnBuf;

		}
		return sReturnBuf;
	}

	public static int getStringLength(String s1) {
		if (s1 == null || s1.equals("")) {
			return 0;
		} else {
			String s2 = new String();
			try {
				s2 = new String(s1.getBytes(UTF), "ISO8859_1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return s2.length();
		}
	}

	public static String FormatStringAddZero(String sReturnBuf, int length) {
		StringBuffer tempBuffer = new StringBuffer();
		if (sReturnBuf == null || sReturnBuf.equals("") || sReturnBuf.equals("null")) {
			for (int i = 0; i < length; i++)
				tempBuffer.append("0");

			return tempBuffer.toString();
		}
		int iLength = sReturnBuf.length();
		if (length > iLength) {
			for (int j = 0; j < length - iLength; j++)
				sReturnBuf = "0" + sReturnBuf;

		}
		return sReturnBuf;
	}

	public static String FormatStringSubZero(String sReturnBuf) {
		int sretu = Integer.parseInt(sReturnBuf);
		String sReturn = Integer.toString(sretu);
		return sReturn;
	}

	public static String PackString(String sPackHead, String sPackBody) {
		StringBuffer tempBuf = new StringBuffer();
		tempBuf.append(sPackBody);
		String sLeng = null;
		try {
			sLeng = FormatStringAddZero(String.valueOf(getStringLength(tempBuf.toString())), 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sLeng + sPackHead + tempBuf.toString();
	}

	public static String MultString(String sMoney, int iEr, String flag) {
		if (sMoney == null || sMoney.equals(""))
			return "";
		if (flag == null || flag.equals(""))
			return "";
		sMoney = stringMoveZero(sMoney);
		// add by zap
		if (sMoney.indexOf(",") != -1) {
			sMoney = sMoney.replaceAll(",", "");
		}
		double iTemp = (new Double(sMoney)).doubleValue();
		if (flag.equals("+")) {
			iTemp *= iEr;
			// int aa = (new Double(iTemp)).intValue();
			int aa = (int) Math.rint(iTemp);
			return String.valueOf(aa);
		}
		if (flag.equals("-"))
			iTemp /= iEr;
		return String.valueOf(iTemp);
	}

	public static String MultStringExt(String sMoney, int iEr, String flag) {
		if (sMoney == null || sMoney.equals(""))
			return "";
		if (flag == null || flag.equals(""))
			return "";
		sMoney = stringMoveZero(sMoney);
		double iTemp = (new Double(sMoney)).doubleValue();
		if (flag.equals("+")) {
			iTemp *= iEr;
			double aa = (new Double(iTemp)).doubleValue();
			return String.valueOf((new BigDecimal(aa)).setScale(0, 4));
		}
		if (flag.equals("-"))
			iTemp /= iEr;
		DecimalFormat df = new DecimalFormat("################.00");
		return df.format(iTemp);
	}

	public static String stringMoveZero(String sMoney) {
		if (sMoney == null || sMoney.equals(""))
			return "";
		int ilen = sMoney.indexOf("-");
		if (ilen > 0)
			return sMoney.substring(ilen, sMoney.length());
		else
			return sMoney;
	}

	public static String stringMoveZeroAddSign(String sMoney) {
		if (sMoney == null || sMoney.equals(""))
			return "";
		int ilen = sMoney.indexOf("-");
		if (ilen >= 0) {
			// String ssss = sMoney.substring(ilen, sMoney.length());
			String s2 = stringToThouMony(MultString(sMoney, 100, "-"));
			return s2;
		} else {
			String s3 = "+" + stringToThouMony(MultString(sMoney, 100, "-"));
			return s3;
		}
	}

	public static String stringToThouMony(String sMoney) {
		if (sMoney == null || sMoney.equals("")) {
			return "";
		} else {
			double d = (new Double(sMoney)).doubleValue();
			DecimalFormat df = new DecimalFormat("##,###,###,###,##0.00");
			return df.format(d);
		}
	}

	public static String MobileAddBlank(String sMobile) {
		if (sMobile == null || sMobile.equals(""))
			return "";
		if (sMobile.length() != 11)
			return sMobile;
		else
			return sMobile.substring(0, 3) + " " + sMobile.substring(3, 7) + " "
					+ sMobile.subSequence(7, 11);
	}

	public static String absoluteSubstring(String s, int start, int end) {
		if (s == null || s.equals(""))
			return "";
		try {
			String s2 = new String(s.getBytes(UTF), "ISO8859_1");
			s2 = s2.substring(start, end);
			return new String(s2.getBytes("ISO8859_1"), UTF);
		} catch (Exception e) {
			return "";
		}
	}

	public static String[] getAbsoluteSubstringArray(String s, int ilength) throws Exception {
		if (s == null || s.equals(""))
			return new String[0];
		try {
			String s2 = new String(s.getBytes(UTF), "ISO8859_1");
			int ilen = s2.length() / ilength;
			if (ilen == 0)
				return new String[0];
			int start = 0;
			int end = ilength;
			String returnarray[] = new String[ilen];
			for (int i = 0; i < ilen; i++) {
				String s1 = s2.substring(start, end);
				start = end;
				end += ilength;
				returnarray[i] = new String(s1.getBytes("ISO8859_1"), UTF);
			}

			return returnarray;
		} catch (Exception e) {
			return new String[0];
		}
	}

	public static String getAbsoluteArrayToString(String s[]) {
		int l = s.length;
		String str = "";
		if (l == 0)
			return str;
		Arrays.sort(s);
		for (int i = 0; i < l; i++)
			str = str + s[i];

		return str;
	}

	public static boolean getIsSameElements(String s[]) {
		boolean isSame = false;
		int l = s.length;
		if (l < 2)
			return false;
		Arrays.sort(s);
		for (int i = 0; i < l - 1; i++) {
			if (!s[i].equals(s[i + 1]))
				continue;
			isSame = true;
			break;
		}

		return isSame;
	}

	public static String[] getAbsoluteSubstringArrayExt(String s, int ilength, int extlen)
			throws Exception {
		if (s == null || s.equals(""))
			return new String[0];
		try {
			String s2 = new String(s.getBytes(UTF), "ISO8859_1");
			int zlen = getStringLength(s);
			int start = 0;
			int end = 0;
			int iRow = (new Integer(s2.substring(start, 2))).intValue();
			String returnarray[] = new String[iRow];
			start += 2;
			for (int i = 0; i < iRow; i++) {
				if (start >= zlen)
					break;
				int ilen = (new Integer(s2.substring(start, start + ilength))).intValue();
				end = start + extlen + ilen;
				String s1 = s2.substring(start, end);
				start = end;
				returnarray[i] = new String(s1.getBytes("ISO8859_1"), UTF);
			}

			return returnarray;
		} catch (Exception e) {
			return new String[0];
		}
	}

	public static String[] spiltStr(String fieldsru, String tag) {
		char dot = tag.charAt(0);
		String field = fieldsru + dot;
		int num = 0;
		int field_len = field.length();
		for (int i = 0; i < field_len; i++)
			if (field.charAt(i) == dot)
				num++;

		String returnarray[] = new String[num];
		int begin = 0;
		for (int j = 0; j < num; j++) {
			int end = field.indexOf(dot, begin);
			returnarray[j] = field.substring(begin, end);
			begin = end + 1;
		}

		return returnarray;
	}

	// public static boolean checkMobile(String sMobile) {
	// boolean flag = false;
	// if (sMobile != null && !sMobile.equals(""))
	// sMobile = sMobile.trim();
	// // String str = sMobile.substring(0, 2);
	// // int num = Integer.parseInt(sMobile.substring(2, 3));
	// if (DataTypeUtil.isNumeric(sMobile))
	// flag = true;
	// return flag;
	// }

	public static byte[] hex2byte(String strhex) {
		if (strhex == null)
			return null;
		int l = strhex.length();
		if (l % 2 == 1)
			return null;
		byte b[] = new byte[l / 2];
		for (int i = 0; i != l / 2; i++)
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);

		return b;
	}

	public static String sortString(String s, String tag, int i, int n) throws Exception {
		if (s.length() != n * 2 + 1 || s.indexOf(tag) != n * 2 - 2) {
			return "";
		} else {
			String sary1[] = spiltStr(s, tag);
			String sary[] = getAbsoluteSubstringArray(sary1[0], i);
			String str = getAbsoluteArrayToString(sary) + "+" + sary1[1];
			return str;
		}
	}

	public static String sortBigFiveToString(String s, int i, int n) throws Exception {
		if (s.length() != n * 2) {
			return "";
		} else {
			String sary[] = getAbsoluteSubstringArray(s, i);
			String str = getAbsoluteArrayToString(sary);
			return str;
		}
	}
	public static  int getFlowSize(String sizeString) {
		if (sizeString.contains("M")) { // 将带有M的字符串（如10M，50M）转为数字
			String s = sizeString.substring(0, sizeString.length() - 1);
			int size = Integer.parseInt(s);
			return size;
		} else if (sizeString.contains("G")) {// 将带有G的面值转为数字
			String s = sizeString.substring(0, sizeString.length() - 1);
			int size = Integer.parseInt(s) * 1024;
			return size;
		}
		return 0;
	}
}
