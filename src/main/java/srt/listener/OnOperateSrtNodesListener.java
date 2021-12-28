package srt.listener;

import srt.core.SrtNode;

import java.util.List;

public interface OnOperateSrtNodesListener {
    public void onOperationStart();

    public void onOperationSuccess(SrtNode node);

    public void onOperationSuccess(List<SrtNode> srtNodes);

    public void onOperationFail(Exception e);
}
