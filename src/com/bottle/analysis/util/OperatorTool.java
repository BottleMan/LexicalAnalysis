package com.bottle.analysis.util;

/**
 * 操作符工具类
 * 
 * @Author Bottle
 *
 * @Date 2013 下午7:49:15
 */
public class OperatorTool
{
	/**
	 * 顺序：
	  +  -  *  /   (   )   i  #
	 */
	public static final int[][] matrix =
	{
	{ 1, 1, -1, -1, -1, 1, -1, 1 },
	{ 1, 1, -1, -1, -1, 1, -1, 1 },
	{ 1, 1, 1, 1, -1, 1, -1, 1 },
	{ 1, 1, 1, 1, -1, 1, -1, 1 },
	{ -1, -1, -1, -1, -1, 0, -1, -2 },
	{ 1, 1, 1, 1, -2, 1, -2, 1 },
	{ 1, 1, 1, 1, -2, 1, -2, 1 },
	{ -1, -1, -1, -1, -1, -2, -1, 0 } };

	/**
	 * 根据相应的符号判断矩阵下标
	 * @Author Bottle
	 * 
	 * @Date 2013 下午7:56:15
	 * 
	 * @param c
	 * @return
	 */
	public static final int Char2Int(char c)
	{
		switch (c)
		{
		case '+':
			return 0;
		case '-':
			return 1;
		case '*':
			return 2;
		case '/':
			return 3;
		case '(':
			return 4;
		case ')':
			return 5;
		case 'i':
			return 6;
		case '#':
			return 7;
		default:
			return 0;
		}
	}
}
