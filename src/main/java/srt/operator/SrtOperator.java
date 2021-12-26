package srt.operator;

import srt.core.SrtNode;
import srt.exception.NullOnOperateSrtNodesListenerException;
import srt.listener.OnOperateSrtNodesListener;

import javax.management.JMException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SrtOperator {

    protected Map<String, Object> parameters_;

    public SrtOperator(HashMap<String, Object> parameters) {
        this.parameters_ = parameters;
    }

    public abstract Object execute(List<SrtNode> srtNodes, OnOperateSrtNodesListener onOperateSrtNodesListener) throws JMException, NullOnOperateSrtNodesListenerException;

    public void setParameter(String name, Object value) {
        this.parameters_.put(name, value);
    }

    public Object getParameter(String name) {
        return this.parameters_.get(name);
    }

}
