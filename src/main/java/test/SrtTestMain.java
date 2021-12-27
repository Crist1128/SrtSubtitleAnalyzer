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
import srt.operator.SrtOperator;
import srt.operator.SrtOperatorFactory;

import javax.management.JMException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class SrtTestMain {
	public static final int OPERTION_INIT = 0;
	public static final int OPERTION_1 = 1;
	public static final int OPERTION_2 = 2;
	private static final int OPERTION_EXIT = 3;

	private static List<SrtNode> srtNodes;

	public static void main(String[] args) throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, FileNotFoundException {
		// 1.读入字幕文件
		// 2.打印可用操作
		// 3.选择一个操作，可用操作有：
					//a.字幕整体前移,输入一个00:00:00,000格式的数字
					//b.字幕整体后移,输入一个00:00:00,000格式的数字
		// 4.调用操作器对应功能，传入参数
		// 5.操作器执行对应操作
		// 6.操作器调取SrtAnalyzer


		SrtLexer lexer = new SrtLexer();
		File file = FileUtil.file("D:\\OneDrive - 汕头大学\\学习资料\\21年第二学期课程\\编译原理\\subtitle.srt");
		lexer.execute(file);

		/*
		SrtMain analyzer = new SrtMain();
		analyzer.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
			public void onLoadSrtFileStart() {

			}

			@Override
			public void onLoadSrtFileSuccess(List<SrtNode> list) {
				Console.log("analyzer.printSrtNodeList()");
				srtNodes = list;
				printSrtNodeList(list);
				try{
					doLoop(analyzer);
				}catch (Exception e){
					e.printStackTrace();
				}

			}
			public void onLoadSrtFileFail() {

			}
		});

		Scanner scan = new Scanner(System.in);
		Console.log("输入文件路径：");//"D:\\OneDrive - 汕头大学\\学习资料\\21年第二学期课程\\编译原理\\subtitle.srt"
		String path=scan.nextLine();

		//1
		analyzer.loadSrtFile(path);

		 */
	}

	public static void doLoop(SrtMain analyzer) throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, InterruptedException {
		HashMap<String,Object> param = new HashMap();

		Runnable executeRun = new Runnable() {
			@Override
			public void run() {
				try {
					SrtOperator operator = SrtOperatorFactory.getSrtOperator("SrtTimeShift",param);
					operator.execute(srtNodes, new OnOperateSrtNodesListener() {
						@Override
						public void onOperationStart() {

						}

						@Override
						public void onOperationSuccess(SrtNode node) {

						}

						@Override
						public void onOperationSuccess(List<SrtNode> srtNodes) {
							printSrtNodeList(srtNodes);
						}

						@Override
						public void onOperationFail() {

						}
					});
				} catch (JMException e) {
					e.printStackTrace();
				} catch (NullOnOperateSrtNodesListenerException e) {
					e.printStackTrace();
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
					Console.log("输入了操作数字："+op);

					switch (op){
						//4
						case OPERTION_1:
							Console.log("请输入参数1：(前移输\"+\",后移输入\"-\")");

							String param1 = scan.nextLine();
							Console.log("输入了参数："+param1);

							param.put("shiftType",param1);
							Console.log("请输入参数2：(要增加或减少的时间，整数，单位毫秒)");

							String param2 = scan.nextLine();
							Console.log("输入了参数："+param2);
							param.put("srtMsecond",Integer.parseInt(param2));

							break;
						case OPERTION_2:
							Console.log("请输入参数：");
							break;
						default:
							Console.log("输入非法");
							break;
					}


					Thread executeThread = new Thread(executeRun);
					executeThread.start();
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread mainThread = new Thread(mainRun);
		mainThread.start();
	}

	public static void printSrtNodeList(List<SrtNode> list){
		Console.log("printSrtNodeList,list.size="+list.size());
		System.out.print("******printing content******\r\n");
		for(int i=0;i<list.size();i++) {
			System.out.printf("%d\n",list.get(i).getSid());
			System.out.printf("%s ---> %s\n",list.get(i).getBegin().toString(),list.get(i).getEnd().toString());
			System.out.printf("%s\n",list.get(i).getContent());
			System.out.print("\r\n");
		}
		System.out.print("******end of content******\r\n");
	}

	public static void printAvailableOperator(){
		Console.log("可用操作：");
		Console.log(String.valueOf(OPERTION_1)+".字幕整体时间移动");
		Console.log(String.valueOf(OPERTION_2)+".查找字幕节点");
		Console.log(String.valueOf(OPERTION_EXIT)+".退出");
	}
}
