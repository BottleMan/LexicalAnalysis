package com.bottle.analysis;

import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.bottle.analysis.util.Config;
import com.bottle.analysis.util.GrammaticalAnalysis;
import com.bottle.analysis.util.LexicalAnalysis;
import com.bottle.analysis.util.OpenVariousFiles;

/**
 * 程序主界面
 * 
 * @author Bottle
 * 
 * @Date 2013-3-31 上午11:56:18
 */
public class MainWindow extends JFrame
{
	private static final long serialVersionUID = -4167760577033141511L;
	private Label label = new Label();
	private Label labelVersion = new Label();
	private Label labelAuthor = new Label();
	//	private JButton btnHelp = new JButton("说明");
	private JButton btnShowSource = new JButton("打开源文件");
	private JButton btnAnalysisDefault = new JButton("编译默认源程序");
	private JButton btnAnalysisCustom = new JButton("编译自定义源程序");
	private JButton btnShowSysLable = new JButton("打开系统符号表");
	private JButton btnShowPre = new JButton("打开预处理文件");
	private JButton btnShowBinary = new JButton("打开二元式表");
	private JButton btnGrammaticalAnalysis = new JButton("执行语法分析");
	//	private JButton btnShowGAResult = new JButton("显示语法分析结果");
	//	private JButton btnShowLable = new JButton("显示各类符号表");
	private ArrayList<JButton> buttons = new ArrayList<JButton>();

	private String filePath;//文件路径

	public MainWindow()
	{
		super("简易词法&语法分析器");
		this.setBounds(0, 0, 500, 260);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);

		//初始化控件
		buttons.add(btnAnalysisDefault);
		buttons.add(btnAnalysisCustom);
		buttons.add(btnShowSysLable);
		buttons.add(btnShowSource);
		//		buttons.add(btnShowLable);
		buttons.add(btnShowPre);
		buttons.add(btnShowBinary);
		buttons.add(btnGrammaticalAnalysis);
		//		buttons.add(btnShowGAResult);
		//		buttons.add(btnHelp);

		label.setBounds(100, 10, 260, 30);
		label.setText("Easy Lexical Analysis Tool");
		label.setFont(new java.awt.Font("MS Song", Font.BOLD, 20));
		this.getContentPane().add(label);

		labelVersion.setBounds(360, 18, 50, 20);
		labelVersion.setText("v2.0");
		labelVersion.setFont(new java.awt.Font("MS Song", Font.BOLD, 15));
		//		labelVersion.setBackground(Color.RED);
		this.getContentPane().add(labelVersion);

		labelAuthor.setBounds(350, 30, 150, 30);
		labelAuthor.setText("2013.5    by Bottle");
		labelAuthor.setFont(new java.awt.Font("MS Song", Font.ITALIC, 10));
		this.getContentPane().add(labelAuthor);

		btnAnalysisDefault.setBounds(50, 60, 180, 30);
		this.getContentPane().add(btnAnalysisDefault);

		btnAnalysisCustom.setBounds(250, 60, 180, 30);
		this.getContentPane().add(btnAnalysisCustom);

		btnShowSysLable.setBounds(50, 100, 180, 30);
		this.getContentPane().add(btnShowSysLable);

		btnShowSource.setBounds(250, 100, 180, 30);
		this.getContentPane().add(btnShowSource);

		//		btnShowLable.setBounds(50, 100, 180, 30);
		//		this.getContentPane().add(btnShowLable);

		btnShowPre.setBounds(50, 140, 180, 30);
		this.getContentPane().add(btnShowPre);

		btnShowBinary.setBounds(250, 140, 180, 30);
		this.getContentPane().add(btnShowBinary);

		btnGrammaticalAnalysis.setBounds(50, 180, 180, 30);
		this.getContentPane().add(btnGrammaticalAnalysis);

		//		btnShowGAResult.setBounds(250, 180, 180, 30);
		//		this.getContentPane().add(btnShowGAResult);

		//		btnHelp.setBounds(250, 140, 180, 30);
		//		this.getContentPane().add(btnHelp);

		//设置其他按钮不可点击
		for (int i = 3; i < buttons.size(); i++)
		{
			buttons.get(i).setEnabled(false);
		}

		//设置监听
		for (int i = 0; i < buttons.size(); i++)
		{
			buttons.get(i).addActionListener(new MyListener(i));
		}

	}

	/**
	 * 主界面按钮的监听类
	 * 
	 * @author Bottle
	 * 
	 * @Date 2013-3-31 上午11:56:34
	 */
	private class MyListener implements ActionListener
	{
		private int ActionFLag = -1;

		public MyListener(int pActionFLag)
		{
			this.ActionFLag = pActionFLag;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			switch (this.ActionFLag)
			{
			case 0://编译默认源程序
				filePath = getValidFileName(Config.DEFAULTSOURCEFILEPATH);
				new LexicalAnalysis(filePath);
				showDialog();
				break;

			case 1://编译自定义源程序
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int r = chooser.showOpenDialog(MainWindow.this);
				if (r == JFileChooser.APPROVE_OPTION)
				{
					//设置文件路径
					filePath = chooser.getSelectedFile().getPath();
					new LexicalAnalysis(filePath);
					showDialog();
				}
				break;

			case 2://显示系统符号表
				OpenVariousFiles.commonOpen(getValidFileName(Config.KEYWORDFILEPATH));
				OpenVariousFiles.commonOpen(getValidFileName(Config.INTERPUNCTIONFILEPATH));
				OpenVariousFiles.commonOpen(getValidFileName(Config.OPERATORFILEPATH));
				OpenVariousFiles.commonOpen(getValidFileName(Config.COMPARISONFILEPATH));
				break;

			case 3://显示源文件
				OpenVariousFiles.commonOpen(filePath);
				break;

			case 4://显示预处理文件
				OpenVariousFiles.commonOpen(getValidFileName(Config.PREPROCESSFILEPATH));
				break;

			case 5://显示二元式表
				OpenVariousFiles.commonOpen(getValidFileName(Config.BINARYFILEPATH));
				break;

			case 6://执行语法分析
				new GrammaticalAnalysis(Config.PREPROCESSFILEPATH);
				break;

			case 7://显示语法分析结果
				break;

			default:
				break;
			}
		}

		/**
		 * 显示编译成功对话框
		 */
		private void showDialog()
		{
			//设置其他按钮可以点击
			for (int i = 3; i < buttons.size(); i++)
			{
				buttons.get(i).setEnabled(true);
			}
			JOptionPane.showMessageDialog(null, "编译成功", "消息", JOptionPane.INFORMATION_MESSAGE);
		}

		/**
		 * 获取到合法的文件路径
		 * 
		 * @return
		 */
		private String getValidFileName(String path)
		{
			String relativelyPath = System.getProperty("user.dir");
			return relativelyPath + "\\" + path;
		}

	}

	public static void main(String[] args)
	{
		new MainWindow();
	}
}
