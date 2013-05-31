package com.bottle.analysis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Stack;

/**
 * 语法分析的操作类
 * 
 * @Author Bottle
 * 
 * @Date 2013-5-8 上午11:12:04
 */
public class GrammaticalAnalysis
{
	private ArrayList<String> word;

	/**
	 * 构造函数
	 * 
	 * @param filePath 预处理结果文件
	 */
	public GrammaticalAnalysis(String filePath)
	{
		File file = new File(filePath);
		init();

		//从文件中获取字符串，进行语法分析
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str = in.readLine();
			in.close();
			SynAna(str);
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}

	/**
	 * 执行初始化操作
	 * @Author Bottle
	 * 
	 * @Date 2013-5-20  下午8:10:14
	 *
	 */
	private void init()
	{
		word = new ArrayList<>();
		word.add("N+N");
		word.add("N-N");
		word.add("N*N");
		word.add("N/N");
		word.add(")N(");
		word.add("i");
	}

	/**
	 * 获取表达式，并对每个表达式执行语法分析
	 * @Author Bottle
	 * 
	 * @Date 2013-5-20  上午8:51:43
	 * 
	 * @param buf
	 * @return
	 */
	public void SynAna(String buf)
	{
		int count2 = 0;
		ArrayList<String> parts = new ArrayList<String>();

		//分解字符串
		while (count2 < buf.length())
		{
			//查询等于号【=】
			while (buf.charAt(count2) != '=' && count2 < buf.length())
			{
				count2++;
				if (count2 == buf.length())
				{
					break;
				}
			}

			//字符串结束，则退出
			if (count2 == buf.length())
			{
				break;
			}

			//如果当前位的前一位是字母，则判定=后面为表达式
			if (Character.isAlphabetic(buf.charAt(count2 - 1)))
			{
				count2++;
				String part = "";
				//判断下一位，如果是运算符或者是数字，则该位是表达式的一部分
				while (Character.isDigit(buf.charAt(count2)) || IsOpt(buf.charAt(count2)))
				{
					part += buf.charAt(count2);
					count2++;
				}
				if (part != "")
				{
					parts.add(part);
				}
				continue;
			} else
			{
				count2++;
			}
		}//end 分解字符串 while
		
		
		try
		{
			for (int i = 0; i < parts.size(); i++)
			{
				Analyse(parts.get(i));
			}
		} catch (Exception ex)
		{
			System.out.println(ex.toString());
		}
	}

	/**
	 * 对输入字符串进行语法分析
	 * 
	 * @Author Bottle
	 * 
	 * @Date 2013-5-20  下午7:31:41
	 * 
	 * @param s 输入字符串
	 */
	private void Analyse(String s)
	{
		ArrayList<OptStep> optSteps = new ArrayList<OptStep>();
		Stack<Character> ss = new Stack<Character>();
		int stepNo = 0;//记录操作步骤
		ss.push('#');//#第一个入栈
		String tmp = Trans2Syn(s);//将字符串转换成对应的公式符号
		tmp += "#";//在表达式结尾加上#
		
		TakeRecord(stepNo, GetStackValue(ss), tmp, "初始", optSteps);
		
		int i = 0;
		while (i < tmp.length())
        {
			stepNo++;
			
			int a = OperatorTool.Char2Int(ss.peek());//获取栈顶元素对应的矩阵坐标
			int b = OperatorTool.Char2Int(tmp.charAt(i));//获取tmp字符串中i位置元素对应的矩阵坐标

			if (CanStackRule(ss)) //先判断栈内是否可以规约
			{
				String tmps = "";
				tmps += ss.pop();
				tmps += ss.pop();
				tmps += ss.pop();
				tmps = Rule(tmps);
				for (int j = 0; j < tmps.length(); ++j)
				{
					ss.push(tmps.charAt(j));
				}

				String temp = "";
				if ((ss.peek() == '#') && (ss.size() > 1))
				{
					temp = "";
				} else
				{
					temp = tmp.substring(i);
				}
				TakeRecord(stepNo, GetStackValue(ss), temp, "规约", optSteps);
				continue;
			}else if (OperatorTool.matrix[a][b] < 0) //判断是否需要入栈
			{
				String temp = "";
				if ((ss.peek() == '#') && (ss.size() > 1))
				{
					temp = "";
				} else
				{
					temp = tmp.substring(i + 1);
				}
				
				ss.push(tmp.charAt(i));
				TakeRecord(stepNo, GetStackValue(ss), temp, "进栈", optSteps);
				i++;
				
				continue;
			} else
			{
				if (word.contains(ss.peek().toString()))
				{
					String tmps = "";
					tmps += ss.pop();
					tmps = Rule(tmps);
					for (int j = 0; j < tmps.length(); ++j)
					{
						ss.push(tmps.charAt(j));
					}

					String temp = "";
					if ((ss.peek().equals('#')) && (ss.size() > 1))
					{
						temp = "";
					} else
					{
						temp = tmp.substring(i);
					}
					TakeRecord(stepNo, GetStackValue(ss), temp, "规约", optSteps);
				} else
				{
					String temp = "";
					if ((ss.peek().equals('#')) && (ss.size() > 1))
					{
						temp = "";
					} else
					{
						temp = tmp.substring(i + 1);
					}
					
					ss.push(tmp.charAt(i));
					TakeRecord(stepNo, GetStackValue(ss), temp, "进栈", optSteps);
					
					i++;
				}
			}
		}
		
		System.out.println(">>>>分析的表达式:" + s);
		System.out.println("+----------+--------------------+--------------------+-----------+");
		System.out.println("|   步骤          |         栈                        |         输入串                |    操作          |");
		System.out.println("+----------+--------------------+--------------------+-----------+");
		for (i = 0; i < optSteps.size(); ++i)
		{
			System.out.printf("|%5d     |%14s      |%14s      |%6s          |\n", i, optSteps.get(i).getSymbolStack(), optSteps.get(i).getInputStack(), optSteps.get(i).getOpt());
			System.out.println("+----------+--------------------+--------------------+-----------+");
		}
		System.out.println(">>>>分析结束。\n\n\n");
	}

	/**
	 * 规约
	 * @Author Bottle
	 * 
	 * @Date 2013-5-20  下午8:12:22
	 * 
	 * @param s
	 * @return
	 */
	public String Rule(String s)
	{
		for (int i = 0; i < word.size(); ++i)
		{
			if (word.get(i).equals(s))
			{
				s = "N";
				return s;
			}
		}
		return s;
	}

	/**
	 * 可以进栈
	 * 
	 * @Author Bottle
	 * 
	 * @Date 2013-5-20  下午8:05:33
	 * 
	 * @param s
	 * @return
	 */
	public boolean CanStackRule(Stack<Character> s)
	{
		if (s.size() > 3)
		{
			int k = s.size();
			String tmp = "";
			for (int i = 0; i < 3; i++)
			{
				k -= 1;
				tmp += s.elementAt(k);
			}
			if (word.contains(tmp))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 记录一条规约记录
	 * @Author Bottle
	 * 
	 * @Date 2013-5-20  下午8:04:08
	 * 
	 */
	private void TakeRecord(int num, String symbolStack, String inputStack, String opt, ArrayList<OptStep> ll)
	{
		OptStep step = new OptStep();
		step.setStep(num);
		step.setOpt(opt);
		step.setInputStack(inputStack);
		step.setSymbolStack(symbolStack);
		ll.add(step);
	}

	/**
	 * 将字符转换成对应的公式符号
	 * 
	 * @Author Bottle
	 * 
	 * @Date 2013-5-20  下午7:36:36
	 * 
	 * @param s
	 * @return
	 */
	private String Trans2Syn(String s)
	{
		Stack<Character> ts = new Stack<Character>();
		for (int i = 0; i < s.length(); ++i)
		{
			if (Character.isDigit(s.charAt(i)))
			{
				if (ts.size() == 0 || !ts.peek().equals('i'))
				{
					ts.push('i');
				}
			} else
			{
				ts.push(s.charAt(i));
			}
		}
		String tmp = "";
		for (int i = 0; i < ts.size(); i++)
		{
			tmp += ts.elementAt(i);
		}
		return tmp;
	}

	/**
	 * 判断字符是否是运算符
	 * 
	 * @Author Bottle
	 * 
	 * @Date 2013-5-20 下午7:26:35
	 * 
	 * @param c
	 * @return
	 */
	public boolean IsOpt(char c)
	{
		if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')')
			return true;
		else
			return false;
	}

	/**
	 * 获取栈的值
	 * @Author Bottle
	 * 
	 * @Date 2013-5-20  下午7:59:38
	 * 
	 * @return
	 */
	public String GetStackValue(Stack<Character> s)
	{
		String tmp = "";
		for (int i = 0; i < s.size(); i++)
		{
			tmp += s.elementAt(i);
		}
		return tmp;
	}

}
