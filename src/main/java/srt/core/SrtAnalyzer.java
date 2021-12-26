package srt.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import srt.exception.NullOnLoadSrtFileListenerException;
import srt.listener.OnLoadSrtFileListener;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SrtAnalyzer{
	//private List<SrtNode> srtNodeArrList = new ArrayList<SrtNode>(); //使用数组存储
	private List<SrtNode> srtNodeLinkList = new LinkedList<SrtNode>(); //使用链表存储
	private OnLoadSrtFileListener onLoadSrtFileListener;

	public SrtAnalyzer() {

	}

	public OnLoadSrtFileListener getOnLoadSrtFileListener() {
		return onLoadSrtFileListener;
	}

	public void setOnLoadSrtFileListener(OnLoadSrtFileListener onLoadSrtFileListener) {
		this.onLoadSrtFileListener = onLoadSrtFileListener;
	}

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


	public void loadSrtFile(String path) throws NullOnLoadSrtFileListenerException {
		if(onLoadSrtFileListener == null){
			throw new NullOnLoadSrtFileListenerException();
		}
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

				//IoUtil.
				//test func
				testLoadNodes();
			}
		});
		thread.start();


	}



	public void operateSrtNodes(){

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
		Console.log("loadSrtFileStart");
		this.onLoadSrtFileListener.onLoadSrtFileStart();
	}

	public void loadSrtFileSuccess() {
		Console.log("loadSrtFileSuccess");
		this.onLoadSrtFileListener.onLoadSrtFileSuccess(srtNodeLinkList);
	}

	public void loadSrtFileFail() {
		Console.log("loadSrtFileFail");
		this.onLoadSrtFileListener.onLoadSrtFileFail();
	}

}
