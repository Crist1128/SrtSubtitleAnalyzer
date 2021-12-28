package srt.operator.imp;

import cn.hutool.core.lang.Console;
import srt.core.SrtNode;
import srt.core.SrtTime;

public class SrtNodeTimeOperate {

    public static final String SHIFT_TYPE_PLUS = "+";
    public static final String SHIFT_TYPE_MINUS = "-";

    protected static Boolean plusBoth(SrtNode node, SrtTime time){
        SrtTime begin = node.getBegin();
        SrtTime end = node.getEnd();

        try{
            begin.setHour(begin.getHour()+time.getHour());
            begin.setMinute(begin.getMinute()+time.getMinute());
            begin.setSecond(begin.getSecond()+time.getSecond());
            begin.setMsecond(begin.getMsecond()+time.getMsecond());

            end.setHour(end.getHour()+time.getHour());
            end.setMinute(end.getMinute()+time.getMinute());
            end.setSecond(end.getSecond()+time.getSecond());
            end.setMsecond(end.getMsecond()+time.getMsecond());

            node.setBegin(begin);
            node.setEnd(end);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected static Boolean plusBoth(SrtNode node, int mSecond){
        SrtTime begin = node.getBegin();
        SrtTime end = node.getEnd();
        try{
            //Console.log("node"+node.getSid()+" begin.asMsecondInt()+mSecond="+(begin.asMsecondInt()+mSecond));
            //Console.log("node"+node.getSid()+" end.asMsecondInt()+mSecond="+(end.asMsecondInt()+mSecond));
            begin.setMsecondInt(begin.asMsecondInt()+mSecond);
            end.setMsecondInt(end.asMsecondInt()+mSecond);

            node.setBegin(begin);
            node.setEnd(end);

            //Console.log("node"+node.getSid()+" after begin.asMsecondInt()="+node.getBegin().asMsecondInt());
            //Console.log("node"+node.getSid()+" after end.asMsecondInt()="+node.getEnd().asMsecondInt());
        }catch (Exception e){
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    protected static Boolean minusBoth(SrtNode node, SrtTime time){
        SrtTime begin = node.getBegin();
        SrtTime end = node.getEnd();

        try{
            begin.setHour(begin.getHour()-time.getHour());
            begin.setMinute(begin.getMinute()-time.getMinute());
            begin.setSecond(begin.getSecond()-time.getSecond());
            begin.setMsecond(begin.getMsecond()-time.getMsecond());

            end.setHour(end.getHour()-time.getHour());
            end.setMinute(end.getMinute()-time.getMinute());
            end.setSecond(end.getSecond()-time.getSecond());
            end.setMsecond(end.getMsecond()-time.getMsecond());

            node.setBegin(begin);
            node.setEnd(end);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected static Boolean minusBoth(SrtNode node, int mSecond){
        SrtTime begin = node.getBegin();
        SrtTime end = node.getEnd();

        try{
            begin.setMsecondInt(begin.asMsecondInt()-mSecond);
            end.setMsecondInt(end.asMsecondInt()-mSecond);
            node.setBegin(begin);
            node.setEnd(end);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected static Boolean isGreaterThan(SrtTime t1,SrtTime t2){
        if(t1.asMsecondInt()>t2.asMsecondInt()){
            return true;
        }
        return false;
    }

    protected static Boolean isIn(SrtTime begin,SrtTime end,SrtTime t){
        if(t.asMsecondInt()>=begin.asMsecondInt() && t.asMsecondInt() <= end.asMsecondInt()){
            return true;
        }
        return false;
    }


}
