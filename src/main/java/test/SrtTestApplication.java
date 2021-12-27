package test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import srt.core.SrtLexer;
import srt.core.SrtNode;
import srt.exception.NullOnLoadSrtFileListenerException;
import srt.exception.NullOnOperateSrtNodesListenerException;
import srt.listener.OnLoadSrtFileListener;
import srt.core.SrtMain;
import srt.listener.OnOperateSrtNodesListener;
import srt.listener.OnSaveSrtFileListener;
import srt.operator.SrtOperator;
import srt.operator.SrtOperatorFactory;

import javax.management.JMException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class SrtTestApplication {
	public static final int OPERTION_INIT = -1;
	private static final int OPERTION_EXIT = 0;
	public static final int OPERTION_1 = 1;
	public static final int OPERTION_2 = 2;
	public static final int OPERTION_3 = 3;
	public static final int OPERTION_4 = 4;
	public static final int OPERTION_5 = 5;

	public static void main(String[] args) throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, FileNotFoundException {
		// 1.读入字幕文件
		// 2.打印可用操作
		// 3.选择一个操作，可用操作有：
					//a.字幕整体前移,输入一个00:00:00,000格式的数字
					//b.字幕整体后移,输入一个00:00:00,000格式的数字
		// 4.调用操作器对应功能，传入参数
		// 5.操作器执行对应操作
		// 6.操作器调取SrtAnalyzer


		//SrtLexer lexer = new SrtLexer();
		SrtMain srtMain = new SrtMain();

		srtMain.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
			public void onLoadSrtFileStart() {

			}
			@Override
			public void onLoadSrtFileSuccess(List<SrtNode> list) {
				Console.log("读取srt文件成功！共有字幕"+srtMain.getSrtNodeListSize()+"条");
				//Console.log("srtMain.printSrtNodeList()");
				//printSrtNodeList(list);
				try{
					doLoop(srtMain);
				}catch (Exception e){
					e.printStackTrace();
				}

			}
			public void onLoadSrtFileFail(Exception e) {
				Console.log("读取srt文件失败！原因："+e.toString());
			}
		});

		//1
		srtMain.loadSrtFile(scanInFilePath());

	}

	public static String scanInFilePath(){
		String path;
		Scanner scan = new Scanner(System.in);
		Console.log("输入文件路径：");//"D:\\OneDrive - 汕头大学\\学习资料\\21年第二学期课程\\编译原理\\subtitle.srt"
		path = scan.nextLine();
		return path;
	}

	public static void doLoop(SrtMain srtMain) throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, InterruptedException {
		HashMap<String,Object> param = new HashMap();

		Runnable executeTimeShiftRun = new Runnable() {
			@Override
			public void run() {
				try {
					srtMain.operateSrtNodes("SrtTimeShift",param,new OnOperateSrtNodesListener() {
						@Override
						public void onOperationStart() {

						}

						@Override
						public void onOperationSuccess(SrtNode node) {

						}

						@Override
						public void onOperationSuccess(List<SrtNode> srtNodeList) {
							Console.log("成功移动所有字幕的时间轴！");
							//srtMain.printSrtNodeList();
						}

						@Override
						public void onOperationFail() {
							Console.log("移动字幕时间轴失败！");
						}
					});
				} catch (JMException | NullOnOperateSrtNodesListenerException e) {
					e.printStackTrace();
					Console.log("字幕时间轴移动失败！原因："+e.toString());
				}

			}
		};

		Runnable mainRun = new Runnable() {
			@Override
			public void run() {
				int op = OPERTION_INIT;
				Scanner scan = new Scanner(System .in);

				while (op != OPERTION_EXIT){
					//2
					printAvailableOperator();

					//3
					Console.log("请输入操作数字");
					op = Integer.parseInt(scan.nextLine());

					switch (op){
						//4
						case OPERTION_1:
							Console.log("请输入参数1：(前移输\"+\",后移输入\"-\")");

							String param1 = scan.nextLine();
							//Console.log("输入了参数："+param1);

							param.put("shiftType",param1);
							Console.log("请输入参数2：(要增加或减少的时间，整数，单位毫秒)");

							String param2 = scan.nextLine();
							//Console.log("输入了参数："+param2);
							param.put("srtMsecond",Integer.parseInt(param2));

							Thread executeThread = new Thread(executeTimeShiftRun);
							executeThread.start();
							try {
								executeThread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							break;
						case OPERTION_2:
							//Console.log("请输入参数：");
							break;
						case OPERTION_3:
							try {
								srtMain.loadSrtFile(scanInFilePath());
							} catch (NullOnLoadSrtFileListenerException e) {
								e.printStackTrace();
							}
							break;
						case OPERTION_4:
							Thread saveFileThread = new Thread(new Runnable() {
								@Override
								public void run() {
									srtMain.saveSrtFile(new OnSaveSrtFileListener() {
										@Override
										public void onSaveSrtFileSuccess() {
											Console.log("保存成功！保存路径："+srtMain.getSrtFilePath());
										}

										@Override
										public void onSaveSrtFileFail(Exception e) {
											Console.log("保存失败!原因："+e.toString());
										}
									});
								}
							});
							saveFileThread.start();
							try {
								saveFileThread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							break;
						case OPERTION_5:
							srtMain.printSrtNodeList();
							break;
						case OPERTION_EXIT:
							break;
						default:
							Console.log("输入非法");
							break;
					}

				}
			}
		};

		Thread mainThread = new Thread(mainRun);
		mainThread.start();
	}

	public static void printAvailableOperator(){
		Console.log("可用操作：");
		Console.log(String.valueOf(OPERTION_1)+".当前字幕整体时间移动");
		//Console.log(String.valueOf(OPERTION_2)+".查找某一时间点的字幕");
		Console.log(String.valueOf(OPERTION_3)+".重新选择字幕文件");
		Console.log(String.valueOf(OPERTION_4)+".保存修改到字幕文件");
		Console.log(String.valueOf(OPERTION_5)+".打印当前字幕");
		Console.log(String.valueOf(OPERTION_EXIT)+".退出");


	}


}
