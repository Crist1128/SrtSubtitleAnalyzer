package test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import srt.core.SrtLexer;
import srt.core.SrtNode;
import srt.core.SrtTime;
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

	public static final int SEARCH_OPERTION_1 = 1;
	public static final int SEARCH_OPERTION_2 = 2;

	public static final int SINGLE_NODE_OPERTION_1 = 1;

	public static void main(String[] args) throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, FileNotFoundException {
		// 1.������Ļ�ļ�
		// 2.��ӡ���ò���
		// 3.ѡ��һ�����������ò����У�
					//a.��Ļ����ǰ��,����һ��00:00:00,000��ʽ������
					//b.��Ļ�������,����һ��00:00:00,000��ʽ������
		// 4.���ò�������Ӧ���ܣ��������
		// 5.������ִ�ж�Ӧ����
		// 6.��������ȡSrtAnalyzer

		Console.log("++++++++++++++++++��Ļ������ V2.0+++++++++++++++++++");
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
				Console.log("�볢����������");
				srtMain.loadSrtFile(scanInFilePath());
			}
		});

		//1
		srtMain.loadSrtFile(scanInFilePath());
	}

	public static void doLoop(SrtMain srtMain) throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, InterruptedException {

		Runnable mainRun = new Runnable() {
			@Override
			public void run() {
				int op = OPERTION_INIT;
				Scanner scan = new Scanner(System .in);

				outer:
				while (op != OPERTION_EXIT){
					//2
					printAvailableOperator();

					//3
					Console.log("�������������");
					op = Integer.parseInt(scan.nextLine());

					switch (op){
						//4
						case OPERTION_1:
							HashMap<String,Object> param = scanInTimeShiftParam();

							Runnable executeTimeShiftRun = new Runnable() {
								@Override
								public void run() {
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
										public void onOperationFail(Exception e) {
											Console.log("�ƶ���Ļʱ����ʧ�ܣ�ԭ��"+e.toString());
										}
									});

								}
							};
							try {
								Thread executeThread = new Thread(executeTimeShiftRun);
								executeThread.start();
								executeThread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
								break outer;
							}

							break;
						case OPERTION_2:
							printAvailableSearchOperator();
							Console.log("ѡ����ҷ�ʽ��");

							int searchOp = Integer.parseInt(scan.nextLine());
							HashMap<String,Object> searchParam = new HashMap<>();
							switch (searchOp){
								case SEARCH_OPERTION_1:
									searchParam.put("sid",scanInNodeSearchSid());
									break;
								case SEARCH_OPERTION_2:
									try{
										int time[] = scanInNodeSearchSrtTime();
										searchParam.put("hour",time[0]);
										searchParam.put("minute",time[1]);
										searchParam.put("second",time[2]);
										searchParam.put("msecond",time[3]);

									}catch (Exception e){
										Console.log("��������"+e.toString());
										continue;
									}
									break;
							}
							Thread searchNodeThread = new Thread(new Runnable() {
								@Override
								public void run() {
									srtMain.operateSrtNodes("SrtNodeSearch", searchParam, new OnOperateSrtNodesListener() {
										@Override
										public void onOperationStart() {

										}

										@Override
										public void onOperationSuccess(SrtNode node) {
											Console.log("���ҳɹ���������Ļ��Ϣ���£�");
											srtMain.printSrtNode(node);

											int singleNodeOp = OPERTION_INIT;
											while(singleNodeOp != OPERTION_EXIT){
												printAvailableSingleNodeOperator();
												singleNodeOp = Integer.parseInt(scan.nextLine());
												switch (singleNodeOp){
													case SINGLE_NODE_OPERTION_1:
														Thread thread = new Thread(new Runnable() {
															@Override
															public void run() {
																HashMap<String,Object> shiftParam = scanInTimeShiftParam();
																shiftParam.put("startNode",node);
																srtMain.operateSrtNodes("SrtTimeShift", shiftParam , new OnOperateSrtNodesListener() {
																	@Override
																	public void onOperationStart() {

																	}

																	@Override
																	public void onOperationSuccess(SrtNode node) {

																	}

																	@Override
																	public void onOperationSuccess(List<SrtNode> srtNodes) {
																		Console.log("�ɹ��ƶ����ֺ���Ӱ�����Ļ��ʱ���ᣡ");
																	}

																	@Override
																	public void onOperationFail(Exception e) {
																		Console.log("�ƶ���Ļʱ����ʧ�ܣ�ԭ��"+e.toString());
																	}
																});
															}
														});
														try {
															thread.start();
															thread.join();
														} catch (InterruptedException e) {
															e.printStackTrace();
														}

														break;
													default:
														break;
												}
											}
										}

										@Override
										public void onOperationSuccess(List<SrtNode> srtNodes) {

										}

										@Override
										public void onOperationFail(Exception e) {
											Console.log("����ʧ�ܣ�ԭ��"+e.toString());
										}
									});
								}
							});
							try {
								searchNodeThread.start();
								searchNodeThread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							break;
						case OPERTION_3:
							Thread loadNewFileThread = new Thread(new Runnable() {
								@Override
								public void run() {
									srtMain.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
										@Override
										public void onLoadSrtFileStart() {

										}

										@Override
										public void onLoadSrtFileSuccess(List<SrtNode> list) {
											Console.log("��ȡsrt�ļ��ɹ���������Ļ"+srtMain.getSrtNodeListSize()+"��");
										}

										@Override
										public void onLoadSrtFileFail(Exception e) {
											Console.log("��ȡsrt�ļ�ʧ�ܣ�ԭ��"+e.toString());
											Console.log("�볢����������");
											srtMain.loadSrtFile(scanInFilePath());
										}
									});
									srtMain.loadSrtFile(scanInFilePath());
								}
							});

							try {
								loadNewFileThread.start();
								loadNewFileThread.join();
							} catch (InterruptedException e) {
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

							try {
								saveFileThread.start();
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
		Console.log("***************���ò�����***************");
		Console.log(String.valueOf(OPERTION_1)+".��ǰ��Ļ����ʱ���ƶ�");
		Console.log(String.valueOf(OPERTION_2)+".����һ����Ļ");
		Console.log(String.valueOf(OPERTION_3)+".����ѡ����Ļ�ļ�");
		Console.log(String.valueOf(OPERTION_4)+".�����޸ĵ���Ļ�ļ�");
		Console.log(String.valueOf(OPERTION_5)+".��ӡ��ǰ��Ļ");
		Console.log(String.valueOf(OPERTION_EXIT)+".�˳�");
		Console.log("*************************************");
	}
	public static void printAvailableSingleNodeOperator(){
		Console.log("***************���ò�����***************");
		Console.log(String.valueOf(SINGLE_NODE_OPERTION_1)+".��ǰ��Ļʱ���ƶ�");
		Console.log(String.valueOf(OPERTION_EXIT)+".�˳������˵�");
		Console.log("*************************************");
	}

	public static void printAvailableSearchOperator(){
		Console.log("***************���ò�����***************");
		Console.log(String.valueOf(SEARCH_OPERTION_1)+".����sid������Ļ");
		Console.log(String.valueOf(SEARCH_OPERTION_2)+".����ʱ������ڵ���Ļ");
		Console.log("*************************************");
	}

	public static String scanInFilePath(){
		String path;
		Scanner scan = new Scanner(System.in);
		Console.log("�����ļ�·����");
		path = scan.nextLine();
		return path;
	}

	public static int scanInNodeSearchSid(){
		int inputSid;
		Scanner scan = new Scanner(System.in);
		Console.log("������ҵ�Sid��");
		inputSid = scan.nextInt();
		return inputSid;
	}

	public static int[] scanInNodeSearchSrtTime(){
		int time[] = new int[4];
		Scanner scan = new Scanner(System.in);
		Console.log("����ʱ��㣺����ʽ��Сʱ ���� �� ���룩");
		time[0] = scan.nextInt();
		time[1] = scan.nextInt();
		time[2] = scan.nextInt();
		time[3] = scan.nextInt();
		scan.nextLine();
		return time;
	}

	public static HashMap<String,Object> scanInTimeShiftParam(){
		HashMap<String,Object> param = new HashMap();
		Console.log("���������1��(������\"+\",ǰ������\"-\")");

		Scanner scan = new Scanner(System.in);
		String param1 = scan.nextLine();
		//Console.log("�����˲�����"+param1);

		param.put("shiftType",param1);
		Console.log("���������2��(Ҫ���ӻ���ٵ�ʱ�䣬��������λ����)");

		String param2 = scan.nextLine();
		//Console.log("�����˲�����"+param2);
		param.put("srtMsecond",Integer.parseInt(param2));
		return param;
	}



}
