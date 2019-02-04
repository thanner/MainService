package br.edu.ufrgs.inf.bpm.validation.grapheditdistance;

import br.edu.ufrgs.inf.bpm.wrapper.BpmnWrapper;
import org.omg.spec.bpmn._20100524.model.*;
import org.processmining.analysis.epc.similarity.ActivityContextFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityContextBuilder {

    private Map<TFlowNode, Integer> idMap;

    public ActivityContextBuilder() {
        idMap = new HashMap<>();
    }

    public List<ActivityContextFragment> convertWithContext(TDefinitions definitions) {
        List<ActivityContextFragment> activityContextFragmentList = new ArrayList<>();
        BpmnWrapper bpmnWrapper = new BpmnWrapper(definitions);

        cleanNames(bpmnWrapper);

        for (TFlowElement tFlowElement : bpmnWrapper.getFlowElementListDeep()) {
            if (tFlowElement instanceof TFlowNode) {
                TFlowNode tFlowNode = (TFlowNode) tFlowElement;
                ActivityContextFragment activityContextFragment = new ActivityContextFragment(tFlowElement.getName());

                activityContextFragment.setInType(getInType(tFlowNode, bpmnWrapper));
                activityContextFragment.setOutType(getOutType(tFlowNode, bpmnWrapper));

                for (TFlowNode tFlowNodeInput : bpmnWrapper.getFlowNodeSourceList(tFlowNode)) {
                    activityContextFragment.addToInputContext(tFlowNodeInput.getName());
                }

                for (TFlowNode tFlowNodeInput : bpmnWrapper.getFlowNodeTargetList(tFlowNode)) {
                    activityContextFragment.addToOutputContext(tFlowNodeInput.getName());
                }

                idMap.put(tFlowNode, activityContextFragmentList.size());
                activityContextFragmentList.add(activityContextFragment);
            }
        }

        return activityContextFragmentList;
    }

    public List<ActivityContextFragment> convert(TDefinitions definitions) {
        List<ActivityContextFragment> activityContextFragmentList = new ArrayList<>();

        BpmnWrapper bpmnWrapper = new BpmnWrapper(definitions);

        cleanNames(bpmnWrapper);

        for (TFlowElement tFlowElement : bpmnWrapper.getFlowElementListDeep()) {
            if (tFlowElement instanceof TActivity) {
                TActivity tActivity = (TActivity) tFlowElement;
                ActivityContextFragment activityContextFragment = new ActivityContextFragment(tActivity.getName());

                for (TFlowNode tFlowNodeInput : bpmnWrapper.getFlowNodeSourceList(tActivity)) {
                    if (tFlowNodeInput instanceof TEvent) {
                        activityContextFragment.addToInputContext(tFlowNodeInput.getName());
                    }
                }

                for (TFlowNode tFlowNodeInput : bpmnWrapper.getFlowNodeTargetList(tActivity)) {
                    if (tFlowNodeInput instanceof TEvent) {
                        activityContextFragment.addToOutputContext(tFlowNodeInput.getName());
                    }
                }

                activityContextFragment.setInType(3);
                activityContextFragment.setOutType(3);

                idMap.put(tActivity, activityContextFragmentList.size());
                activityContextFragmentList.add(activityContextFragment);
            }
        }

        return activityContextFragmentList;
    }

    private void cleanNames(BpmnWrapper bpmnWrapper) {
        for (TFlowElement tFlowElement : bpmnWrapper.getFlowElementListDeep()) {
            if (tFlowElement.getName() == null) {
                tFlowElement.setName("");
            }
        }
    }

    private int getInType(TFlowNode tFlowNode, BpmnWrapper bpmnWrapper) {
        List<TFlowNode> flowNodeInputList = bpmnWrapper.getFlowNodeSourceList(tFlowNode);
        return getType(flowNodeInputList);
    }

    private int getOutType(TFlowNode tFlowNode, BpmnWrapper bpmnWrapper) {
        List<TFlowNode> flowNodeOutputList = bpmnWrapper.getFlowNodeTargetList(tFlowNode);
        return getType(flowNodeOutputList);
    }

    private int getType(List<TFlowNode> flowNodeList) {
        if (flowNodeList.size() > 1) {
            return ActivityContextFragment.NONE;
        } else if (flowNodeList.size() == 1) {
            TFlowNode flowNodeInput = flowNodeList.get(0);
            if (flowNodeInput instanceof TExclusiveGateway) {
                return ActivityContextFragment.XOR;
            } else if (flowNodeInput instanceof TInclusiveGateway) {
                return ActivityContextFragment.OR;
            } else if (flowNodeInput instanceof TParallelGateway) {
                return ActivityContextFragment.AND;
            }
        }

        return ActivityContextFragment.NONE;
    }

    public Map<TFlowNode, Integer> getIdMap() {
        return idMap;
    }
}
