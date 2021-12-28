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

	public void loadSrtFile(String path){
		if(onLoadSrtFileListener == null){
			loadSrtFileFail(new NullOnLoadSrtFileListenerException());
		}
		this.srtFilePath = path;

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
					if(!srtNodeLinkList.isEmpty()){
						srtNodeLinkList.clear();
					}
					srtNodeLinkList = list;
					loadSrtFileSuccess();
				}

				@Override
				public void onLoadSrtFileFail(Exception e) {
					loadSrtFileFail(e);
				}
			});
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			onLoadSrtFileListener.onLoadSrtFileFail(e);
		}
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
		listener.onSaveSrtFileSuccess();
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
			str+=convertString(node);
		}
		return str;
	}

	public static String convertString(SrtNode node){
		String str = "";
		str+=String.format("%d\n",node.getSid());
		str+=String.format("%s --> %s\n",node.getBegin().toString(),node.getEnd().toString());
		str+=String.format("%s\n",node.getContent());
		str+=LINE_SEPARATOR;
		return str;
	}

	public void operateSrtNodes(String opName, HashMap<String,Object> parameter, OnOperateSrtNodesListener listener) {
		try{
			SrtOperator operator = SrtOperatorFactory.getSrtOperator(opName,parameter);
			operator.execute(srtNodeLinkList,listener);
		} catch (NullOnOperateSrtNodesListenerException e) {
			listener.onOperationFail(e);
			//e.printStackTrace();
		} catch (JMException e) {
			listener.onOperationFail(e);
			//e.printStackTrace();
		}
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
		System.out.print("******************字幕内容开始******************\r\n");
		Console.log(SrtMain.convertString(srtNodeLinkList));
		System.out.print("******************字幕内容结束******************\r\n");
	}

	public void printSrtNode(SrtNode node){
		System.out.print("****************单个字幕内容开始****************\r\n");
		Console.log(SrtMain.convertString(node));
		System.out.print("****************单个字幕内容结束****************\r\n");
	}
}
