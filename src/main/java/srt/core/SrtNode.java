package srt.core;

public class SrtNode {
	// ��Ļ���
	int sid;
	// ��ʼʱ��
	SrtTime begin;
	// ����ʱ��
	SrtTime end;
	// ��Ļ����
	String content;

	// Ĭ�Ϲ��캯��
	SrtNode() {

	}

	/**
	 * �������Ĺ��캯�������ڴ���������ϸ��Ϣ��SrtNode����
	 *
	 * @param sid ��Ļ��š�
	 * @param begin ��Ļ�Ŀ�ʼʱ�䡣
	 * @param end ��Ļ�Ľ���ʱ�䡣
	 * @param content ��Ļ�����ݡ�
	 */
	SrtNode(int sid, SrtTime begin, SrtTime end, String content) {
		this.sid = sid;
		this.begin = begin;
		this.end = end;
		this.content = content;
	}

	// ��ȡ��Ļ���
	public int getSid() {
		return sid;
	}

	// ������Ļ���
	public void setSid(int sid) {
		this.sid = sid;
	}

	// ��ȡ��Ļ�Ŀ�ʼʱ��
	public SrtTime getBegin() {
		return begin;
	}

	// ������Ļ�Ŀ�ʼʱ��
	public void setBegin(SrtTime begin) {
		this.begin = begin;
	}

	// ��ȡ��Ļ�Ľ���ʱ��
	public SrtTime getEnd() {
		return end;
	}

	// ������Ļ�Ľ���ʱ��
	public void setEnd(SrtTime end) {
		this.end = end;
	}

	// ��ȡ��Ļ����
	public String getContent() {
		return content;
	}

	// ������Ļ����
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * ��дtoString�������ṩSrtNode���ַ�����ʾ��ʽ�����ڴ�ӡ�Ͳ鿴��
	 *
	 * @return ��Ļ��Ϣ���ַ�����ʾ��������š���ʼʱ�䡢����ʱ������ݡ�
	 */
	@Override
	public String toString() {
		return "��Ļ���: " + sid + "\n" +
				"��ʼʱ��: " + begin.toString() + "\n" +
				"����ʱ��: " + end.toString() + "\n" +
				"����: " + content + "\n";
	}
}
