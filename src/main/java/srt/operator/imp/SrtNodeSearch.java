package srt.operator.imp;

import srt.core.SrtNode;
import srt.listener.OnOperateSrtNodesListener;
import srt.operator.SrtOperator;

import javax.management.JMException;
import java.util.HashMap;
import java.util.List;

public class SrtNodeSearch extends SrtOperator {
    public SrtNodeSearch(HashMap<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Object execute(List<SrtNode> srtNodes, OnOperateSrtNodesListener onOperateSrtNodesListener) throws JMException {
        return null;
    }


}
