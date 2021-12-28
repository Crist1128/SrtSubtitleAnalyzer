package srt.operator.imp;

import cn.hutool.core.lang.Console;
import srt.core.SrtNode;
import srt.core.SrtTime;
import srt.exception.NullOnOperateSrtNodesListenerException;
import srt.listener.OnOperateSrtNodesListener;
import srt.operator.SrtOperator;

import javax.management.JMException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static java.lang.System.exit;
import static srt.operator.imp.SrtNodeTimeOperate.*;


public class SrtTimeShift extends SrtOperator {

    private String shiftType;
    private SrtTime shiftSrtTime;
    private int shiftMsecond;
    private SrtNode startNode;

    public SrtTimeShift(HashMap<String, Object> parameters) {
        super(parameters);
        if (parameters.get("shiftType") != null) {
            this.shiftType = (String) parameters.get("shiftType");
        }

        if (parameters.get("srtMsecond") != null) {
            this.shiftMsecond = (int) parameters.get("srtMsecond");
        }

        if (parameters.get("startNode") != null) {
            this.startNode = (SrtNode) parameters.get("startNode");
        }


    }

    @Override
    public Object execute(List<SrtNode> srtNodes, OnOperateSrtNodesListener onOperateSrtNodesListener) {
        if(onOperateSrtNodesListener == null){
            onOperateSrtNodesListener.onOperationFail(new NullOnOperateSrtNodesListenerException());
        }

        if(startNode != null){
            Console.log("startNode != null");
            doTimeShift(srtNodes,startNode,onOperateSrtNodesListener);
        }else{
            doTimeShift(srtNodes,onOperateSrtNodesListener);
        }
        return null;
    }

    public void doTimeShift(List<SrtNode> srtNodes, OnOperateSrtNodesListener onOperateSrtNodesListener){
        onOperateSrtNodesListener.onOperationStart();
        Iterator it = srtNodes.iterator();
        while(it.hasNext()){
            SrtNode node = (SrtNode)it.next();
            switch (shiftType){
                case SHIFT_TYPE_PLUS:
                    if(plusBoth(node,shiftMsecond)){
                        break;
                    }else{
                        //此处应当进行回滚，返回未被修改过的源srtNodes
                        if(!minusBoth(node,shiftMsecond)){
                            onOperateSrtNodesListener.onOperationFail(new Exception("时间轴后移失败，回滚失败，程序退出"));
                            exit(-1);
                        }
                        onOperateSrtNodesListener.onOperationFail(new Exception("时间轴后移失败"));
                    }
                    break;
                case SHIFT_TYPE_MINUS:
                    if(minusBoth(node,shiftMsecond)){
                        continue;
                    }else{
                        //此处应当进行回滚，返回未被修改过的源srtNodes
                        if(!plusBoth(node,shiftMsecond)){
                            onOperateSrtNodesListener.onOperationFail(new Exception("时间轴前移失败，回滚失败，程序退出"));
                            exit(-1);
                        }
                        onOperateSrtNodesListener.onOperationFail(new Exception("时间轴前移失败"));
                    }
                    break;
            }
        }
        onOperateSrtNodesListener.onOperationSuccess(srtNodes);
    }

    public void doTimeShift(List<SrtNode> srtNodes,SrtNode startNode, OnOperateSrtNodesListener onOperateSrtNodesListener){

        ListIterator it = srtNodes.listIterator();
        boolean isFoundFirst = false;
        SrtNode node = null;

        while(!isFoundFirst) {
            node = (SrtNode)it.next();
            //Console.log("还没找到，当前sid="+node.getSid());
            if (node.getSid() == startNode.getSid()) {
                //Console.log("找到第一个节点！sid="+node.getSid());
                isFoundFirst = true;
                break;
            } else {
                continue;
            }
        }

        //分情况查找和移动
        switch (shiftType){
            case SHIFT_TYPE_PLUS:
                it.previous();
                while(it.hasNext()){
                    node = (SrtNode)it.next();
                    if(plusBoth(node,shiftMsecond)){
                        continue;
                    }else{
                        //此处应当进行回滚，返回未被修改过的源srtNodes
                        if(!minusBoth(node,shiftMsecond)){
                            onOperateSrtNodesListener.onOperationFail(new Exception("时间轴后移失败，回滚失败，程序退出"));
                            exit(-1);
                        }
                        onOperateSrtNodesListener.onOperationFail(new Exception("时间轴后移失败"));
                        break;
                    }
                }
                break;
            case SHIFT_TYPE_MINUS:
                //it.next();
                while (it.hasPrevious()){
                    node = (SrtNode)it.previous();
                    if(minusBoth(node,shiftMsecond)){
                        continue;
                    }else {
                        //此处为失败情况，应当进行回滚，返回未被修改过的源srtNodes
                        if(!plusBoth(node,shiftMsecond)){
                            onOperateSrtNodesListener.onOperationFail(new Exception("时间轴前移失败，回滚失败，程序退出"));
                            exit(-1);
                        }
                        onOperateSrtNodesListener.onOperationFail(new Exception("时间轴前移失败"));
                    }
                }

                break;
        }


        if(isFoundFirst){
            onOperateSrtNodesListener.onOperationSuccess(srtNodes);
        }else {
            onOperateSrtNodesListener.onOperationFail(new Exception("移动失败，起始节点不在字幕序列中！"));
        }

    }
}
