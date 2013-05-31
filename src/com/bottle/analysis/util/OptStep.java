package com.bottle.analysis.util;

/**
 * 语法分析每步的记录类
 * 
 * @Author Bottle
 *
 * @Date 2013 下午7:33:44
 */
public class OptStep
{
	private int step;
	private String symbolStack;
	private String inputStack;
	private String opt;

	public int getStep()
	{
		return step;
	}

	public void setStep(int step)
	{
		this.step = step;
	}

	public String getSymbolStack()
	{
		return symbolStack;
	}

	public void setSymbolStack(String symbolStack)
	{
		this.symbolStack = symbolStack;
	}

	public String getInputStack()
	{
		return inputStack;
	}

	public void setInputStack(String inputStack)
	{
		this.inputStack = inputStack;
	}

	public String getOpt()
	{
		return opt;
	}

	public void setOpt(String opt)
	{
		this.opt = opt;
	}
}
