package srt.core;

public class SrtParser {

    
    
     public static void execute(File file,List<SrtNode> srtNodeList) throws FileNotFoundException {
        
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
             
            ArrayList<String> node = new ArrayList<>();
            while (input.hasNextLine()){
                String str=input.nextLine();
                while(!str.isEmpty()){
                    node.add(str);
                    str=input.nextLine();
                }
                rawStrNodes.add(node);
                SrtNode srtNode = parseStrToSrtNode(node);
                srtNodeList.add(srtNode);
                
                lines++;
            }
        }

    }
    
    public static SrtNode parseStrToSrtNode(ArrayList<String> strNode){
        SrtNode node = new SrtNode();
        if(srtNode.size()>2){
            
            SrtTime begin = new SrtTime();
            SrtTime end = new SrtTime();
            
            String content = "";
            for(int index=2;index<strNode.size();index++){
                content+=strNode.get(index);
            }
            
            String[] time = srtNode.get(1).split("-->");
            String[] begin = time[0].split(":");
            String[] end = time[1].split(":");
            
            begin.setHour(Integer.parseInt(begin[0]));
            begin.setMinute(Integer.parseInt(begin[1]));
            begin.setSecond(Integer.parseInt(begin[2].split(",")[0]));
            begin.setMsecond(Integer.parseInt(begin[2].split(",")[1]));
            
            end.setHour(Integer.parseInt(end[0]));
            end.setMinute(Integer.parseInt(end[1]));
            end.setSecond(Integer.parseInt(end[2].split(",")[0]));
            end.setMsecond(Integer.parseInt(end[2].split(",")[1]));
            

            node.setSid(strNode.get(0));
        
            node.setContent(content);
        
            node.setBegin(begin);
        
            node.setEnd(end);
            
        }
        return node;
    }
}
