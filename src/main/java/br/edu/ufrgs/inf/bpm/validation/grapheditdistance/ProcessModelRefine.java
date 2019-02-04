package br.edu.ufrgs.inf.bpm.validation.grapheditdistance;

import br.edu.ufrgs.inf.bpm.wrapper.BpmnWrapper;
import org.omg.spec.bpmn._20100524.model.*;

public class ProcessModelRefine {

    public static TDefinitions refineProcess(TDefinitions tDefinitions) {
        BpmnWrapper bpmnWrapper = new BpmnWrapper(tDefinitions);
        for (TFlowElement tFlowElement : bpmnWrapper.getFlowElementListDeep()) {
            if (tFlowElement instanceof TFlowNode) {
                TFlowNode tFlowNode = (TFlowNode) tFlowElement;
                if (tFlowNode.getName() == null || tFlowNode.getName().isEmpty()) {
                    if (tFlowNode instanceof TActivity) {
                        tFlowNode.setName("activity");
                    } else if (tFlowNode instanceof TExclusiveGateway) {
                        if (tFlowNode.getOutgoing().size() > 1) {
                            tFlowNode.setName("xorsplit");
                        } else {
                            tFlowNode.setName("xorjoin");
                        }
                    } else if (tFlowNode instanceof TInclusiveGateway) {
                        if (tFlowNode.getOutgoing().size() > 1) {
                            tFlowNode.setName("orsplit");
                        } else {
                            tFlowNode.setName("orjoin");
                        }
                    } else if (tFlowNode instanceof TParallelGateway) {
                        if (tFlowNode.getOutgoing().size() > 1) {
                            tFlowNode.setName("andsplit");
                        } else {
                            tFlowNode.setName("andjoin");
                        }
                    } else if (tFlowNode instanceof TEventBasedGateway) {
                        tFlowNode.setName("eventbasedgateway");
                    } else if (tFlowNode instanceof TStartEvent) {
                        tFlowNode.setName("startevent");
                    } else if (tFlowNode instanceof TEndEvent) {
                        tFlowNode.setName("endevent");
                    } else if (tFlowNode instanceof TIntermediateCatchEvent) {
                        tFlowNode.setName("intermediatecatchevent");
                    } else if (tFlowNode instanceof TIntermediateThrowEvent) {
                        tFlowNode.setName("intermediatethrowevent");
                    } else if (tFlowNode instanceof TGateway) {
                        tFlowNode.setName("gateway");
                    } else if (tFlowNode instanceof TEvent) {
                        tFlowNode.setName("event");
                    } else {
                        tFlowNode.setName("unknown");
                    }
                }
            }
        }

        return tDefinitions;
    }

}
