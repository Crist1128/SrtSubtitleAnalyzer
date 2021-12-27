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
		// 1.������Ļ�ļ�
		// 2.��ӡ���ò���
		// 3.ѡ��һ�����������ò����У�
					//a.��Ļ����ǰ��,����һ��00:00:00,000��ʽ������
					//b.��Ļ�������,����һ��00:00:00,000��ʽ������
		// 4.���ò�������Ӧ���ܣ��������
		// 5.������ִ�ж�Ӧ����
		// 6.��������ȡSrtAnalyzer


		//SrtLexer lexer = new SrtLexer();
		SrtMain srtMain = new SrtMain();

		srtMain.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
			public void onLoadSrtFileStart() {

			}
			@Override
			public void onLoadSrtFileSuccess(List<SrtNode> list) {
				Console.log("��ȡsrt�ļ��ɹ���������Ļ"+srtMain.getSrtNodeListSize()+"��");
				//Console.log("srtMain.printSrtNodeList()");
				//printSrtNodeList(list);
				try{
					doLoop(srtMain);
				}catch (Exception e){
					e.printStackTrace();
				}

			}
			public void onLoadSrtFileFail(Exception e) {
				Console.log("��ȡsrt�ļ�ʧ�ܣ�ԭ��"+e.toString());
			}
		});

		//1
		srtMain.loadSrtFile(scanInFilePath());

	}

	public static String scanInFilePath(){
		String path;
		Scanner scan = new Scanner(System.in);
		Console.log("�����ļ�·����");//"D:\\OneDrive - ��ͷ��ѧ\\ѧϰ����\\21��ڶ�ѧ�ڿγ�\\����ԭ��\\subtitle.srt"
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
							Console.log("�ɹ��ƶ�������Ļ��ʱ���ᣡ");
							//srtMain.printSrtNodeList();
						}

						@Override
						public void onOperationFail() {
							Console.log("�ƶ���Ļʱ����ʧ�ܣ�");
						}
					});
				} catch (JMException | NullOnOperateSrtNodesListenerException e) {
					e.printStackTrace();
					Console.log("��Ļʱ�����ƶ�ʧ�ܣ�ԭ��"+e.toString());
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

					switch (op){
						//4
						case OPERTION_1:
							Console.log("���������1��(ǰ����\"+\",��������\"-\")");

							String param1 = scan.nextLine();
							//Console.log("�����˲�����"+param1);

							param.put("shiftType",param1);
							Console.log("���������2��(Ҫ���ӻ���ٵ�ʱ�䣬��������λ����)");

							String param2 = scan.nextLine();
							//Console.log("�����˲�����"+param2);
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
							//Console.log("�����������");
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
											Console.log("����ɹ�������·����"+srtMain.getSrtFilePath());
										}

										@Override
										public void onSaveSrtFileFail(Exception e) {
											Console.log("����ʧ��!ԭ��"+e.toString());
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
							Console.log("����Ƿ�");
							break;
					}

				}
			}
		};

		Thread mainThread = new Thread(mainRun);
		mainThread.start();
	}

	public static void printAvailableOperator(){
		Console.log("���ò�����");
		Console.log(String.valueOf(OPERTION_1)+".��ǰ��Ļ����ʱ���ƶ�");
		//Console.log(String.valueOf(OPERTION_2)+".����ĳһʱ������Ļ");
		Console.log(String.valueOf(OPERTION_3)+".����ѡ����Ļ�ļ�");
		Console.log(String.valueOf(OPERTION_4)+".�����޸ĵ���Ļ�ļ�");
		Console.log(String.valueOf(OPERTION_5)+".��ӡ��ǰ��Ļ");
		Console.log(String.valueOf(OPERTION_EXIT)+".�˳�");


	}


}
