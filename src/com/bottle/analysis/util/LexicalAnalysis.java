package com.bottle.analysis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 词法分析的类
 * 
 * @author Bottle
 * 
 * @Date 2013-3-31 下午1:12:33
 */
public class LexicalAnalysis
{

	/**
	 * 构造函数
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public LexicalAnalysis(String filePath)
	{
		super();
		String result = Pretreatment(filePath);
		FileOperation.WriteFile(result, Config.PREPROCESSFILEPATH);
		scanner(result);
	}

	/**
	 * 分解出二元组
	 * 
	 * @param result
	 */
	private void scanner(String result)
	{
		FileOperation operation = new FileOperation();
		ArrayList<String> keywordList = operation.ParaseXML(Config.KEYWORDFILEPATH); //关键字list
		ArrayList<String> operatorList = operation.ParaseXML(Config.OPERATORFILEPATH);//运算符list
		ArrayList<String> interpunctionList = operation.ParaseXML(Config.INTERPUNCTIONFILEPATH);//界符list
		ArrayList<String> comparisonList = operation.ParaseXML(Config.COMPARISONFILEPATH);//关系符list
		ArrayList<String> binaryList = new ArrayList<String>();
		char[] chars = result.toCharArray();

		String token = "";
		for (int i = 0; i < chars.length; i++)
		{
			char ch = chars[i];
			if (Character.isLetter(ch) || Character.isDigit(ch))
			{//判断是否为字母或者数字
				token = token + ch;
				continue;
			} else
			{
				boolean isDeal = false;//是否已经确认

				if (!token.equals(""))
				{
					if (isKeyWord(token, keywordList))
					{
						String temp = "(K," + token + ")";//关键字
						binaryList.add(temp);
					} else
					{
						String temp = "(C," + token + ")";//用户标识符（变量）
						binaryList.add(temp);
					}
					token = "";
				}

				//判断是否为特殊符号
				String str = String.valueOf(ch); //一位符号
				String str2 = "";//二位符号(本位+下一位)

				if (i + 1 < chars.length)
				{
					str2 = str + chars[i + 1];//二位符号(本位+下一位)
				}

				//判断是否为2位运算符
				for (int j = 0; j < operatorList.size(); j++)
				{
					if (str2.equals(operatorList.get(j)))
					{
						String temp = "(I," + str2 + ")";//运算符
						binaryList.add(temp);
						isDeal = true;
						i++;
						break;
					}
				}

				//判断是否为2位界符
				for (int j = 0; j < interpunctionList.size() && !isDeal; j++)
				{
					if (str2.equals(interpunctionList.get(j)))
					{
						String temp = "(P," + str2 + ")";//界符
						binaryList.add(temp);
						isDeal = true;
						i++;
						break;
					}
				}

				//判断是否为2位关系符
				for (int j = 0; j < comparisonList.size() && !isDeal; j++)
				{
					if (str2.equals(comparisonList.get(j)))
					{
						String temp = "(I," + str2 + ")";//关系符
						binaryList.add(temp);
						isDeal = true;
						i++;
						break;
					}
				}

				//判断是否为1位运算符
				for (int j = 0; j < operatorList.size() && !isDeal; j++)
				{
					if (str.equals(operatorList.get(j)))
					{
						String temp = "(I," + str + ")";//运算符
						binaryList.add(temp);
						isDeal = true;
						break;
					}
				}

				//判断是否为1位界符
				for (int j = 0; j < interpunctionList.size() && !isDeal; j++)
				{
					if (str.equals(interpunctionList.get(j)))
					{
						String temp = "(P," + str + ")";//界符
						binaryList.add(temp);
						isDeal = true;
						break;
					}
				}

				//判断是否为1位关系符
				for (int j = 0; j < comparisonList.size() && !isDeal; j++)
				{
					if (str.equals(comparisonList.get(j)))
					{
						String temp = "(I," + str + ")";//关系符
						binaryList.add(temp);
						isDeal = true;
						break;
					}
				}

			}//else

		}//for

		String inputString = "";
		for (int i = 0; i < binaryList.size(); i++)
		{
			inputString += (binaryList.get(i) + "\r\n");
		}
		FileOperation.WriteFile(inputString, Config.BINARYFILEPATH);
	}

	/**
	 * 判断是否是关键字
	 * 
	 * @param token
	 * @return
	 */
	private boolean isKeyWord(String token, ArrayList<String> keywordList)
	{
		for (int i = 0; i < keywordList.size(); i++)
		{
			if (token.equals(keywordList.get(i)))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 对输入文件进行预处理
	 * 
	 * @param filePath
	 * @return
	 */
	private String Pretreatment(String filePath)
	{
		String result = "";
		String FileName = filePath;
		File myFile = new File(FileName);
		if (!myFile.exists())
		{
			System.err.println("Can't Find " + FileName);
		}

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(myFile));
			String str;
			while ((str = in.readLine()) != null)
			{
				//System.out.println(replaceBlank(str));
				result += replaceBlank(str);
			}
			in.close();
		} catch (IOException e)
		{
			e.getStackTrace();
		}

		//		System.out.println(result);
		return result;
	}

	/**
	 * 去除回车,换行,制表,注释,续行
	 * 
	 * @param str
	 * @return
	 */
	private String replaceBlank(String str)
	{
		String dest = "";
		if (str != null)
		{
			//清除制表，回车，换行
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll(" ");
			//清除注释和续行
			dest = dest.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*\\/|\\\\", "");
		}
		return dest;
	}

}
