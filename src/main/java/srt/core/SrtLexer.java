package srt.core;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SrtLexer {
    // 预定义的关键字
    static String[] keyWord = {"..."};
    // 预定义的界符
    static String[] symbol = {"-->", ":", ",", "."};
    // 系统换行符
    static String lineSeparator = System.lineSeparator();

    // 关键字和界符的ArrayList表示
    static ArrayList<String> keyWords = null;
    static ArrayList<String> symbols = null;

    // 用于追踪解析位置、行数和节点索引
    static int p, lines, nodeIndex;

    // 主执行方法，接受一个文件对象作为参数，初始化解析器
    public static void execute(File file) throws FileNotFoundException {
        init();  // 初始化关键字和界符
        lines = 1;
        nodeIndex = 1;

        try (Scanner input = new Scanner(file)) {
            ArrayList<ArrayList<String>> rawStrNodes = new ArrayList<>();
            ArrayList<String> node = new ArrayList<>();
            while (input.hasNextLine()) {
                String str = input.nextLine();
                while (!str.isEmpty()) {
                    node.add(str);
                    str = input.nextLine();
                }
                rawStrNodes.add(node);
                analyzeStrNode(node); // 分析每个节点
                lines++;
            }
        }
    }

    // 初始化把数组转换为ArrayList
    public static void init() {
        keyWords = new ArrayList<>();
        symbols = new ArrayList<>();
        Collections.addAll(keyWords, keyWord);
        Collections.addAll(symbols, symbol);
    }

    // 分析单个SRT节点的内容
    public static void analyzeStrNode(ArrayList<String> strNode) {
        if (strNode.size() > 2) {
            nodePartOneCheck(strNode.get(0));
            nodePartTwoCheck(strNode.get(1));

            String content = "";
            for (int index = 2; index < strNode.size(); index++) {
                content += strNode.get(index);
            }
            nodeContentCheck(content);
        }
    }

    // 以下方法为占位符，待实现具体的节点检查逻辑
    private static void nodeContentCheck(String content) {
    }

    private static void nodePartTwoCheck(String s) {
    }

    private static void nodePartOneCheck(String s) {
    }

    // 识别数字并处理相关错误
    public static void digitCheck(String str) {
        // 省略了具体实现，建议添加详细注释说明逻辑
    }

    // 识别标识符和关键字
    public static void letterCheck(String str) {
        // 省略了具体实现，建议添加详细注释说明逻辑
    }

    // 检查字符串并处理错误
    public static void stringCheck(String str) {
        // 省略了具体实现，建议添加详细注释说明逻辑
    }
}
