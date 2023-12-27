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
     * 主要执行方法，解析SRT文件并通知监听器进度。
     *
     * @param file 要解析的SRT文件。
     * @param listener 监听文件加载事件的监听器。
     * @throws FileNotFoundException 如果文件未找到。
     */
    public static void execute(File file, OnLoadSrtFileListener listener) throws FileNotFoundException {
        try (Scanner input = new Scanner(file)) {
            List<SrtNode> srtNodeList = new LinkedList<>();
            listener.onLoadSrtFileStart(); // 通知监听器文件加载开始

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
                // 解析单个字幕块并添加到列表中
                SrtNode srtNode = parseStrToSrtNode(node);
                srtNodeList.add(srtNode);
            }
            listener.onLoadSrtFileSuccess(srtNodeList); // 通知监听器文件加载成功
        } catch (SrtFileParseException e) {
            listener.onLoadSrtFileFail(e); // 通知监听器文件加载失败
        }
    }

    /**
     * 将字符串列表解析为SrtNode对象。
     *
     * @param strNode 字幕块的字符串列表。
     * @return 解析得到的SrtNode对象。
     * @throws SrtFileParseException 如果解析过程中遇到错误。
     */
    public static SrtNode parseStrToSrtNode(ArrayList<String> strNode) throws SrtFileParseException {
        SrtNode node = new SrtNode();
        if (strNode.size() > 2) {
            SrtTime begin = new SrtTime();
            SrtTime end = new SrtTime();

            try {
                // 解析字幕内容
                String content = "";
                for (int index = 2; index < strNode.size(); index++) {
                    content += strNode.get(index);
                }

                // 解析开始和结束时间
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
                // 如果解析过程中出现任何异常，抛出SrtFileParseException
                throw new SrtFileParseException(e.toString());
            }
        }
        return node;
    }
}
