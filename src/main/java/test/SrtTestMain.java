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
		// 1.������Ļ�ļ�
		// 2.��ӡ���ò���
		// 3.ѡ��һ�����������ò����У�
					//a.��Ļ����ǰ��,����һ��00:00:00,000��ʽ������
					//b.��Ļ�������,����һ��00:00:00,000��ʽ������
		// 4.���ò�������Ӧ���ܣ��������
		// 5.������ִ�ж�Ӧ����
		// 6.��������ȡSrtAnalyzer


		SrtLexer lexer = new SrtLexer();
		File file = FileUtil.file("D:\\OneDrive - ��ͷ��ѧ\\ѧϰ����\\21��ڶ�ѧ�ڿγ�\\����ԭ��\\subtitle.srt");
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
		Console.log("�����ļ�·����");//"D:\\OneDrive - ��ͷ��ѧ\\ѧϰ����\\21��ڶ�ѧ�ڿγ�\\����ԭ��\\subtitle.srt"
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
					Console.log("�������������");
					op = Integer.parseInt(scan.nextLine());
					Console.log("�����˲������֣�"+op);

					switch (op){
						//4
						case OPERTION_1:
							Console.log("���������1��(ǰ����\"+\",��������\"-\")");

							String param1 = scan.nextLine();
							Console.log("�����˲�����"+param1);

							param.put("shiftType",param1);
							Console.log("���������2��(Ҫ���ӻ���ٵ�ʱ�䣬��������λ����)");

							String param2 = scan.nextLine();
							Console.log("�����˲�����"+param2);
							param.put("srtMsecond",Integer.parseInt(param2));

							break;
						case OPERTION_2:
							Console.log("�����������");
							break;
						default:
							Console.log("����Ƿ�");
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
		Console.log("���ò�����");
		Console.log(String.valueOf(OPERTION_1)+".��Ļ����ʱ���ƶ�");
		Console.log(String.valueOf(OPERTION_2)+".������Ļ�ڵ�");
		Console.log(String.valueOf(OPERTION_EXIT)+".�˳�");
	}
}
