package srt.core;

import cn.hutool.core.lang.Console;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SrtLexer {
    //关键字
    static String []keyWord={"..."};

    //界符
    static String []symbol={"-->",":",",","."};

    //换行符
    static String lineSeparator = System.lineSeparator();

    static ArrayList<String> keyWords=null;
    static ArrayList<String> symbols=null;

    //指向当前所读到字符串的位置的指针
    static int p,lines,nodeIndex;

    public static void execute(File file) throws FileNotFoundException {
        init();
        //File file=new File("E:\\code\\bytest\\test11\\test2.txt");
        lines=1;
        nodeIndex=1;
        //Console.log("lineSeparator is "+lineSeparator);



        try(Scanner input=new Scanner(file)) {
            ArrayList<ArrayList<String>> rawStrNodes = new ArrayList<>();
            /*
            while (input.hasNext()){
                String str = input.next();
                Console.log(str);

                rawBytes.add(str);

                if(str == "\n"){
                    Console.log("line "+lines+":found lineSeparator \\n");
                }
                if(str == "\r"){
                    Console.log("line "+lines+":found lineSeparator \\r");
                }

            }
             */
            while (input.hasNextLine()){
                String str=input.nextLine();
                ArrayList<String> node = new ArrayList<>();
                while(!str.isEmpty()){
                    node.add(str);
                    rawStrNodes.add(node);
                }
                analyzeStrNode(node);
                lines++;
            }
        }

    }

    //初始化把数组转换为ArrayList
    public static void init(){
        keyWords=new ArrayList<>();
        symbols=new ArrayList<>();
        Collections.addAll(keyWords, keyWord);
        //Collections.addAll(breakers, breaker);
        Collections.addAll(symbols, symbol);
    }

    public static void analyzeStrNode(ArrayList<String> strNode){
        if(strNode.size()>2){
            nodePartOneCheck(strNode.get(0));
            nodePartTwoCheck(strNode.get(1));

            String content = "";
            for(int index=2;index<strNode.size();index++){
                content+=strNode.get(index);
            }
            nodeContentCheck(content);
        }
          /*
        p=0;
        char ch;
        str=str.trim();
        for (;p<str.length();p++){
            ch=str.charAt(p);


            if (Character.isDigit(ch)){
                digitCheck(str);
            }else if (Character.isLetter(ch)||ch=='_'){
                letterCheck(str);
            }else if (ch=='"'){
                stringCheck(str);
            }
            else if (ch==' '){
                continue;
            }else {
                //symbolCheck(str);
            }
             */
    }

    private static void nodeContentCheck(String content) {

    }

    private static void nodePartTwoCheck(String s) {
    }

    private static void nodePartOneCheck(String s) {

    }


    /*数字的识别
     * 1、识别退出：
     *   1.1、遇到空格符或换行符
     *   1.2、遇到界符
     * 2、错误情况：
     *  无
     * */
    public static void digitCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        //判断数字的小数点是否有且是否大于1
        int flag=0;
        boolean err=false;
        char ch;
        for (;p<str.length();p++) {
            ch = str.charAt(p);
            if (ch==' '||(!Character.isLetterOrDigit(ch)&&ch!='.')) {
                break;
            }else if (err){
                token+=ch;
            }
            else {
                token+=ch;
                if (ch == '.') {
                    if (flag == 1) {
                        err = true;
                    } else {
                        flag++;
                    }
                }else if (Character.isLetter(ch)){
                    err=true;
                }
            }
        }
        if (token.charAt(token.length()-1)=='.'){
            err=true;
        }
        if (err){
            System.out.println(lines+"line"+": "+token+" is wrong");
        }else {
            System.out.println("("+3+","+token+")");
        }
        if (p!=str.length()-1||(p==str.length()-1&&!Character.isDigit(str.charAt(p)))){
            p--;
        }
    }

    //标识符，关键字的识别
    public static void letterCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        char ch;
        for (;p<str.length();p++){
            ch=str.charAt(p);
            if (!Character.isLetterOrDigit(ch)&&ch!='_'){
                break;
            }else{
                token+=ch;
            }
        }
        if (keyWords.contains(token)){
            System.out.println("("+1+","+token+")");
        }else {
            System.out.println("("+2+","+token+")");
        }
        if (p!=str.length()-1||(p==str.length()-1&&(!Character.isLetterOrDigit(str.charAt(p))&&str.charAt(p)!='_'))){
            p--;
        }
    }

    //符号的识别
    /*
    public static void symbolCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        char ch;
        if (symbols.contains(token)){
            System.out.println("("+5+","+token+")");
            p--;
        }else {
            if (operations.contains(token)){
                if (p<str.length()){
                    ch=str.charAt(p);
                    if (operations.contains(token+ch)){
                        token+=ch;
                        p++;
                        if (p<str.length()){
                            ch=str.charAt(p);
                            if (operations.contains(token+ch)){
                                token+=ch;
                                System.out.println("("+4+","+token+")");
                            }else{
                                p--;
                                System.out.println("("+4+","+token+")");
                            }
                        }else{
                            System.out.println("("+4+","+token+")");
                        }
                    }else {
                        p--;
                        System.out.println("("+4+","+token+")");
                    }
                }
            }else {
                p--;
                System.out.println(lines+"line"+": "+token+" is wrong");
            }
        }
    }
     */

    //字符串检查
    public static void stringCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        char ch;
        for (;p<str.length();p++){
            ch=str.charAt(p);
            token+=ch;
            if (ch=='"'){
                break;
            }
        }
        if (token.charAt(token.length()-1)!='"'){
            System.out.println(lines+"line"+": "+token+" is wrong");
        }else {
            System.out.println("("+6+","+token+")");
        }
    }
}
