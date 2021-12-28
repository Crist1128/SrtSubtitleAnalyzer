package srt.operator.imp;

import cn.hutool.core.lang.Console;
import srt.core.SrtNode;
import srt.core.SrtTime;
import srt.exception.SrtTimeOutOfBoundaryException;
import srt.listener.OnOperateSrtNodesListener;
import srt.operator.SrtOperator;

import javax.management.JMException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SrtNodeSearch extends SrtOperator {
    private int sid;
    private int hour;
    private int minute;
    private int second;
    private int msecond;


    public SrtNodeSearch(HashMap<String, Object> parameters) {
        super(parameters);
        if (parameters.get("sid") != null) {
            this.sid = (int) parameters.get("sid");
        }

        if (parameters.get("hour") != null) {
            this.hour = (int)parameters.get("hour");
        }
        if (parameters.get("minute") != null) {
            this.minute = (int) parameters.get("minute");
        }
        if (parameters.get("second") != null) {
            this.second = (int) parameters.get("second");
        }
        if (parameters.get("msecond") != null) {
            this.msecond = (int) parameters.get("msecond");
        }
    }

    @Override
    public Object execute(List<SrtNode> srtNodes, OnOperateSrtNodesListener onOperateSrtNodesListener) throws JMException {
        if (this.parameters_.get("sid") != null) {
            //此处进行遍历查找，可修改为sid与索引相关联的查找等更高效的查找方式
            boolean isFound = false;
            Iterator it = srtNodes.iterator();
            while (it.hasNext()){
                SrtNode node = (SrtNode)it.next();
                if(node.getSid() == sid){
                    isFound = true;
                    onOperateSrtNodesListener.onOperationSuccess(node);
                    break;
                }
            }
            if(!isFound){
                onOperateSrtNodesListener.onOperationFail(new Exception("没有符合sid条件的字幕！"));
            }
        }else{
            SrtTime srtTime = new SrtTime();
            try{
                srtTime.setHour(hour);
                srtTime.setMinute(minute);
                srtTime.setSecond(second);
                srtTime.setMsecond(msecond);
                Console.log("输入的时间："+srtTime.toString());

                //此处进行遍历查找，可修改为二分查找等更高效的查找方式
                boolean isFound = false;
                Iterator it = srtNodes.iterator();
                while (it.hasNext()){
                    SrtNode node = (SrtNode)it.next();
                    if(SrtNodeTimeOperate.isIn(node.getBegin(),node.getEnd(),srtTime)){
                        isFound = true;
                        onOperateSrtNodesListener.onOperationSuccess(node);
                        break;
                    }
                }
                if(!isFound){
                    onOperateSrtNodesListener.onOperationFail(new Exception("没有符合时间条件的字幕！"));
                }

            }catch (SrtTimeOutOfBoundaryException e){
                onOperateSrtNodesListener.onOperationFail(e);
            }
        }

        return null;
    }


}
