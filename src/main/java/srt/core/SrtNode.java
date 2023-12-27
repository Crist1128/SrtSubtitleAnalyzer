package srt.core;

public class SrtNode {
	// 字幕序号
	int sid;
	// 开始时间
	SrtTime begin;
	// 结束时间
	SrtTime end;
	// 字幕内容
	String content;

	// 默认构造函数
	SrtNode() {

	}

	/**
	 * 带参数的构造函数，用于创建带有详细信息的SrtNode对象。
	 *
	 * @param sid 字幕序号。
	 * @param begin 字幕的开始时间。
	 * @param end 字幕的结束时间。
	 * @param content 字幕的内容。
	 */
	SrtNode(int sid, SrtTime begin, SrtTime end, String content) {
		this.sid = sid;
		this.begin = begin;
		this.end = end;
		this.content = content;
	}

	// 获取字幕序号
	public int getSid() {
		return sid;
	}

	// 设置字幕序号
	public void setSid(int sid) {
		this.sid = sid;
	}

	// 获取字幕的开始时间
	public SrtTime getBegin() {
		return begin;
	}

	// 设置字幕的开始时间
	public void setBegin(SrtTime begin) {
		this.begin = begin;
	}

	// 获取字幕的结束时间
	public SrtTime getEnd() {
		return end;
	}

	// 设置字幕的结束时间
	public void setEnd(SrtTime end) {
		this.end = end;
	}

	// 获取字幕内容
	public String getContent() {
		return content;
	}

	// 设置字幕内容
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 重写toString方法，提供SrtNode的字符串表示形式，便于打印和查看。
	 *
	 * @return 字幕信息的字符串表示，包含序号、开始时间、结束时间和内容。
	 */
	@Override
	public String toString() {
		return "字幕序号: " + sid + "\n" +
				"开始时间: " + begin.toString() + "\n" +
				"结束时间: " + end.toString() + "\n" +
				"内容: " + content + "\n";
	}
}
