package srt.core;

public class SrtNode {
	int sid;
	SrtTime begin;
	SrtTime end;
	String content;

	SrtNode() {

	}

	SrtNode(int sid,SrtTime begin,SrtTime end,String content) {
		this.sid=sid;
		this.begin=begin;
		this.end=end;
		this.content=content;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public SrtTime getBegin() {
		return begin;
	}

	public void setBegin(SrtTime begin) {
		this.begin = begin;
	}

	public SrtTime getEnd() {
		return end;
	}

	public void setEnd(SrtTime end) {
		this.end = end;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "字幕序号: " + sid + "\n" +
				"开始时间: " + begin.toString() + "\n" +
				"结束时间: " + end.toString() + "\n" +
				"内容: " + content + "\n";
	}
}
