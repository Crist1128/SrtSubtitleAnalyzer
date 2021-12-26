package srt.operator.imp;

import srt.core.SrtNode;
import srt.core.SrtTime;
import srt.exception.NullOnOperateSrtNodesListenerException;
import srt.listener.OnOperateSrtNodesListener;
import srt.operator.SrtOperator;

import javax.management.JMException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.exit;
import static srt.operator.imp.SrtNodeTimeOperate.*;


public class SrtTimeShift extends SrtOperator {

    private String shiftType;
    private SrtTime shiftSrtTime;
    private int shiftMsecond;

    public SrtTimeShift(HashMap<String, Object> parameters) {
        super(parameters);
        if (parameters.get("shiftType") != null) {
            this.shiftType = (String) parameters.get("shiftType");
        }

        if (parameters.get("srtMsecond") != null) {
            this.shiftMsecond = (int) parameters.get("srtMsecond");
        }
    }

    @Override
    public Object execute(List<SrtNode> srtNodes, OnOperateSrtNodesListener onOperateSrtNodesListener) throws JMException, NullOnOperateSrtNodesListenerException {
        if(onOperateSrtNodesListener == null){
            throw new NullOnOperateSrtNodesListenerException();
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                onOperateSrtNodesListener.onOperationStart();
                doTimeShift(srtNodes,onOperateSrtNodesListener);
            }
        });
        thread.start();
        return null;
    }


    public void doTimeShift(List<SrtNode> srtNodes, OnOperateSrtNodesListener onOperateSrtNodesListener){
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
                            exit(-1);
                        }
                        onOperateSrtNodesListener.onOperationFail();
                    }
                    break;
                case SHIFT_TYPE_MINUS:
                    if(minusBoth(node,shiftMsecond)){
                        continue;
                    }else{
                        //此处应当进行回滚，返回未被修改过的源srtNodes
                        if(!plusBoth(node,shiftMsecond)){
                            exit(-1);
                        }
                        onOperateSrtNodesListener.onOperationFail();
                    }
                    break;
            }
        }
        onOperateSrtNodesListener.onOperationSuccess(srtNodes);
    }
}
