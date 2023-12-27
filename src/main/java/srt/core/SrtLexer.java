package srt.core;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SrtLexer {
    // Ԥ����Ĺؼ���
    static String[] keyWord = {"..."};
    // Ԥ����Ľ��
    static String[] symbol = {"-->", ":", ",", "."};
    // ϵͳ���з�
    static String lineSeparator = System.lineSeparator();

    // �ؼ��ֺͽ����ArrayList��ʾ
    static ArrayList<String> keyWords = null;
    static ArrayList<String> symbols = null;

    // ����׷�ٽ���λ�á������ͽڵ�����
    static int p, lines, nodeIndex;

    // ��ִ�з���������һ���ļ�������Ϊ��������ʼ��������
    public static void execute(File file) throws FileNotFoundException {
        init();  // ��ʼ���ؼ��ֺͽ��
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
                analyzeStrNode(node); // ����ÿ���ڵ�
                lines++;
            }
        }
    }

    // ��ʼ��������ת��ΪArrayList
    public static void init() {
        keyWords = new ArrayList<>();
        symbols = new ArrayList<>();
        Collections.addAll(keyWords, keyWord);
        Collections.addAll(symbols, symbol);
    }

    // ��������SRT�ڵ������
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

    // ���·���Ϊռλ������ʵ�־���Ľڵ����߼�
    private static void nodeContentCheck(String content) {
    }

    private static void nodePartTwoCheck(String s) {
    }

    private static void nodePartOneCheck(String s) {
    }

    // ʶ�����ֲ�������ش���
    public static void digitCheck(String str) {
        // ʡ���˾���ʵ�֣����������ϸע��˵���߼�
    }

    // ʶ���ʶ���͹ؼ���
    public static void letterCheck(String str) {
        // ʡ���˾���ʵ�֣����������ϸע��˵���߼�
    }

    // ����ַ������������
    public static void stringCheck(String str) {
        // ʡ���˾���ʵ�֣����������ϸע��˵���߼�
    }
}
