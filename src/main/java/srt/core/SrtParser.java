package srt.core;

import cn.hutool.core.lang.Console;
import srt.exception.SrtFileParseException;
import srt.listener.OnLoadSrtFileListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SrtParser {

    /**
     * ��Ҫִ�з���������SRT�ļ���֪ͨ���������ȡ�
     *
     * @param file Ҫ������SRT�ļ���
     * @param listener �����ļ������¼��ļ�������
     * @throws FileNotFoundException ����ļ�δ�ҵ���
     */
    public static void execute(File file, OnLoadSrtFileListener listener) throws FileNotFoundException {
        try (Scanner input = new Scanner(file)) {
            List<SrtNode> srtNodeList = new LinkedList<>();
            listener.onLoadSrtFileStart(); // ֪ͨ�������ļ����ؿ�ʼ

            while (input.hasNextLine()) {
                ArrayList<String> node = new ArrayList<>();
                String str = input.nextLine();
                while (!str.isEmpty()) {
                    node.add(str);
                    if (input.hasNextLine()) {
                        str = input.nextLine();
                    } else {
                        break;
                    }
                }
                // ����������Ļ�鲢��ӵ��б���
                SrtNode srtNode = parseStrToSrtNode(node);
                srtNodeList.add(srtNode);
            }
            listener.onLoadSrtFileSuccess(srtNodeList); // ֪ͨ�������ļ����سɹ�
        } catch (SrtFileParseException e) {
            listener.onLoadSrtFileFail(e); // ֪ͨ�������ļ�����ʧ��
        }
    }

    /**
     * ���ַ����б����ΪSrtNode����
     *
     * @param strNode ��Ļ����ַ����б�
     * @return �����õ���SrtNode����
     * @throws SrtFileParseException ���������������������
     */
    public static SrtNode parseStrToSrtNode(ArrayList<String> strNode) throws SrtFileParseException {
        SrtNode node = new SrtNode();
        if (strNode.size() > 2) {
            SrtTime begin = new SrtTime();
            SrtTime end = new SrtTime();

            try {
                // ������Ļ����
                String content = "";
                for (int index = 2; index < strNode.size(); index++) {
                    content += strNode.get(index);
                }

                // ������ʼ�ͽ���ʱ��
                String[] timeStr = strNode.get(1).split("-->");
                String[] beginStr = timeStr[0].split(":");
                String[] endStr = timeStr[1].split(":");

                begin.setHour(Integer.parseInt(beginStr[0].trim()));
                begin.setMinute(Integer.parseInt(beginStr[1].trim()));
                begin.setSecond(Integer.parseInt(beginStr[2].split(",")[0].trim()));
                begin.setMsecond(Integer.parseInt(beginStr[2].split(",")[1].trim()));

                end.setHour(Integer.parseInt(endStr[0].trim()));
                end.setMinute(Integer.parseInt(endStr[1].trim()));
                end.setSecond(Integer.parseInt(endStr[2].split(",")[0].trim()));
                end.setMsecond(Integer.parseInt(endStr[2].split(",")[1].trim()));

                node.setSid(Integer.parseInt(strNode.get(0).trim()));
                node.setContent(content);
                node.setBegin(begin);
                node.setEnd(end);
            } catch (Exception e) {
                // ������������г����κ��쳣���׳�SrtFileParseException
                throw new SrtFileParseException(e.toString());
            }
        }
        return node;
    }
}
