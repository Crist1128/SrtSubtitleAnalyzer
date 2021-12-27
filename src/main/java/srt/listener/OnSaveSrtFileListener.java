package srt.listener;

import srt.core.SrtNode;

import java.util.List;

public interface OnSaveSrtFileListener {
	/*保存srt字幕文件成功*/
	public void onSaveSrtFileSuccess();

	/*保存srt字幕文件失败*/
	public void onSaveSrtFileFail(Exception e);
}
