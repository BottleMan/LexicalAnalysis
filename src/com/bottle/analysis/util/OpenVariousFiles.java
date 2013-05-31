package com.bottle.analysis.util;

import java.io.IOException;

/**
 * 调用系统程序打开文件
 * 
 * @author Bottle
 * 
 * @Date 2013-4-2 下午9:19:01
 */
public class OpenVariousFiles {

	/**
	 * 
	 * 通用:只要系统有打开相应文件的程序
	 * 
	 * @param filePath
	 *            文件路径路径==>注:如果打开目录或文件名含有空格, 将找不到
	 * @return 输入的路径格式是否是文件/打开文件的情况
	 */
	public static void commonOpen(String filePath) {
		try {
			/** 开启本地系统相应进程来打开文件 注意:两个参数的间的空格 */
			Runtime.getRuntime().exec("cmd /c start " + filePath);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
