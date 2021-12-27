package srt.listener;

import srt.core.SrtNode;

import java.util.List;

public interface OnLoadSrtFileListener {
	/*开始读取srt字幕文件*/
	public void onLoadSrtFileStart();

	/*读取srt字幕文件完成且成功*/
	public void onLoadSrtFileSuccess(List<SrtNode> list);

	/*开始读取srt字幕文件失败*/
	public void onLoadSrtFileFail(Exception e);
}
