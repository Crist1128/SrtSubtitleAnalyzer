package srt.core;

import cn.hutool.core.lang.Console;
import srt.exception.SrtTimeHourOutOfBoundaryException;
import srt.exception.SrtTimeOutOfBoundaryException;
import srt.listener.OnLoadSrtFileListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SrtParser {

     public static void execute(File file, OnLoadSrtFileListener listener) throws FileNotFoundException {

        try(Scanner input=new Scanner(file)) {
            List<SrtNode> srtNodeList = new LinkedList<>();

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

            listener.onLoadSrtFileStart();
            while (input.hasNextLine()){
                ArrayList<String> node = new ArrayList<>();
                String str=input.nextLine();
                while(!str.isEmpty()){
                    //Console.log("str is not empty:"+str);
                    node.add(str);
                    if(input.hasNextLine()){
                        str=input.nextLine();
                    }else{
                        break;
                    }
                }
                //Console.log("node parse start");
                SrtNode srtNode = parseStrToSrtNode(node);
                srtNodeList.add(srtNode);
            }
            listener.onLoadSrtFileSuccess(srtNodeList);
        } catch (SrtTimeOutOfBoundaryException e) {
            e.printStackTrace();
            listener.onLoadSrtFileFail(e);
        }

     }

    public static SrtNode parseStrToSrtNode(ArrayList<String> strNode) throws SrtTimeOutOfBoundaryException {
        SrtNode node = new SrtNode();
        if(strNode.size()>2){

            SrtTime begin = new SrtTime();
            SrtTime end = new SrtTime();

            String content = "";
            for(int index=2;index<strNode.size();index++){
                content+=strNode.get(index);
            }
            //Console.log("parsing strNode.get(0)="+strNode.get(0));
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
        }
        return node;
    }
}
