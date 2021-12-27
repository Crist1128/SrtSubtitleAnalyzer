package srt.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import srt.exception.NullOnLoadSrtFileListenerException;
import srt.exception.NullOnOperateSrtNodesListenerException;
import srt.listener.OnLoadSrtFileListener;
import srt.listener.OnOperateSrtNodesListener;
import srt.listener.OnSaveSrtFileListener;
import srt.operator.SrtOperator;
import srt.operator.SrtOperatorFactory;

import javax.management.JMException;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SrtMain {
	//private List<SrtNode> srtNodeArrList = new ArrayList<SrtNode>(); //使用数组存储
	private List<SrtNode> srtNodeLinkList = new LinkedList<SrtNode>(); //使用链表存储
	private OnLoadSrtFileListener onLoadSrtFileListener;
	private String srtFilePath;
	private static String LINE_SEPARATOR = System.lineSeparator();

	public SrtMain() {

	}

	/*
	public void testLoadNodes() {
		SrtTime b1 = new SrtTime(0,0,1,0);
		SrtTime e1 = new SrtTime(0,0,25,0);
		String c1 ="English subtitle by : Eduun\r\n";

		SrtTime b2 = new SrtTime(0,0,36,700);
		SrtTime e2 = new SrtTime(0,0,38,700);
		String c2 ="The late fourth century A.D. the \r\n"
				+ "Roman Empire began to crumble.\r\n";

		srtNodeLinkList.add(new SrtNode(0,b1,e1,c1));
		srtNodeLinkList.add(new SrtNode(1,b2,e2,c2));
		loadSrtFileSuccess();
	}
	 */

	public void loadSrtFile(String path) throws NullOnLoadSrtFileListenerException {
		if(onLoadSrtFileListener == null){
			throw new NullOnLoadSrtFileListenerException();
		}
		this.srtFilePath = path;

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				loadSrtFileStart();
				/*
				try{
					File srtFile = FileUtil.file(path);
				}catch (Exception e){
					e.printStackTrace();
				}

				 */
				SrtParser parser = new SrtParser();
				File file=new File(path);
				try {
					parser.execute(file, new OnLoadSrtFileListener() {
						@Override
						public void onLoadSrtFileStart() {
							loadSrtFileStart();
						}

						@Override
						public void onLoadSrtFileSuccess(List<SrtNode> list) {
							srtNodeLinkList = list;
							loadSrtFileSuccess();
						}

						@Override
						public void onLoadSrtFileFail(Exception e) {
							onLoadSrtFileFail(e);
						}
					});
				} catch (FileNotFoundException e) {
					//e.printStackTrace();
					onLoadSrtFileListener.onLoadSrtFileFail(e);
				}
			}
		});
		thread.start();
	}

	public void saveSrtFile(OnSaveSrtFileListener listener){
		File file = new File(this.srtFilePath);
		String content = convertString(srtNodeLinkList);
		//Console.log("----------------saving! content->--------------");
		//Console.log(content);

		try {
			FileUtil.writeString(content,file, CharsetUtil.systemCharset());
		}catch (IORuntimeException e){
			//e.printStackTrace();
			listener.onSaveSrtFileFail(e);
		}

	}

	public void saveSrtFile(String filePath,OnSaveSrtFileListener listener){
		File file = new File(filePath);
		String content = convertString(srtNodeLinkList);
		FileUtil.writeString(content,file, CharsetUtil.systemCharset());
	}

	public static String convertString(List<SrtNode> list){
		String str = "";
		Iterator it = list.iterator();
		while (it.hasNext()){
			SrtNode node = (SrtNode)it.next();
			str+=String.format("%d\n",node.getSid());
			str+=String.format("%s --> %s\n",node.getBegin().toString(),node.getEnd().toString());
			str+=String.format("%s\n",node.getContent());
			str+=LINE_SEPARATOR;
		}
		return str;
	}

	public void operateSrtNodes(String opName, HashMap<String,Object> parameter, OnOperateSrtNodesListener listener) throws JMException, NullOnOperateSrtNodesListenerException {
		SrtOperator operator = SrtOperatorFactory.getSrtOperator(opName,parameter);
		operator.execute(srtNodeLinkList,listener);
	}

	/**
	 * 输入：SrtTime t
	 * 返回：SrtNode n
	 * 功能说明：根据时间轴时间点查找该点所在的字幕节点SrtNode
	 * 实现说明：可以先考虑使用二叉查找，用t和SrtNode的begin比较
	 */
	public SrtNode getSrtNode(SrtTime t) {
		SrtNode node = null;

		if(srtNodeLinkList!=null && !srtNodeLinkList.isEmpty()) {

		}
		return node;
	}

	public void loadSrtFileStart() {
		//Console.log("loadSrtFileStart");
		this.onLoadSrtFileListener.onLoadSrtFileStart();
	}

	public void loadSrtFileSuccess() {
		//Console.log("loadSrtFileSuccess");
		this.onLoadSrtFileListener.onLoadSrtFileSuccess(srtNodeLinkList);
	}

	public void loadSrtFileFail(Exception e) {
		//Console.log("loadSrtFileFail");
		this.onLoadSrtFileListener.onLoadSrtFileFail(e);
	}

	public String getSrtFilePath() {
		return srtFilePath;
	}

	public void setSrtFilePath(String srtFilePath) {
		this.srtFilePath = srtFilePath;
	}

	public OnLoadSrtFileListener getOnLoadSrtFileListener() {
		return onLoadSrtFileListener;
	}

	public void setOnLoadSrtFileListener(OnLoadSrtFileListener onLoadSrtFileListener) {
		this.onLoadSrtFileListener = onLoadSrtFileListener;
	}

	public int getSrtNodeListSize(){
		return this.srtNodeLinkList.size();
	}

	public void printSrtNodeList(){
		System.out.print("******printing content******\r\n");
		Console.log(SrtMain.convertString(srtNodeLinkList));
		System.out.print("******end of content******\r\n");
	}
}
