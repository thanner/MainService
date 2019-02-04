package br.edu.ufrgs.inf.bpm.validation.grapheditdistance;

import br.edu.ufrgs.inf.bpm.wrapper.BpmnWrapper;
import br.edu.ufrgs.inf.bpm.wrapper.JaxbWrapper;
import org.omg.spec.bpmn._20100524.model.*;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

public class PeocessModelReduction {

    private static int id = 0;

    public static TDefinitions makeReduction(TDefinitions definitions) {
        BpmnWrapper model = new BpmnWrapper(definitions);
        BpmnWrapper newModel = new BpmnWrapper(new TDefinitions());

        TProcess tProcess = new TProcess();
        newModel.addTProcess(tProcess);

        for (TFlowNode flowObject : model.getFlowNodeList()) {
            if (flowObject instanceof TActivity) {
                JAXBElement<TTask> jaxbFlowNode = new ObjectFactory().createTask((TTask) flowObject);
                tProcess.getFlowElement().add(jaxbFlowNode);
            }
        }

        for (TFlowNode tFlowNode : model.getFlowNodeList()) {
            if (tFlowNode instanceof TActivity) {
                TActivity activitySource = (TActivity) tFlowNode;
                for (TSequenceFlow sequenceFlow : model.getSequenceFlowTarget(activitySource)) {
                    for (TActivity activityTarget : getTargetActivities(model, sequenceFlow)) {
                        TSequenceFlow tSequenceFlow = new TSequenceFlow();
                        tSequenceFlow.setId("Sequence-" + id++);

                        tSequenceFlow.setSourceRef(activitySource);
                        activitySource.getOutgoing().add(JaxbWrapper.getQName(tSequenceFlow.getClass(), tSequenceFlow));

                        tSequenceFlow.setTargetRef(activityTarget);
                        activityTarget.getIncoming().add(JaxbWrapper.getQName(tSequenceFlow.getClass(), tSequenceFlow));

                        tProcess.getFlowElement().add(new ObjectFactory().createSequenceFlow(tSequenceFlow));
                    }
                }
            }
        }

        return newModel.getDefinitions();
    }

    private static List<TActivity> getTargetActivities(BpmnWrapper model, TSequenceFlow sequenceFlow) {
        List<TActivity> activityList = new ArrayList<>();
        TFlowNode target = model.getTargetBySequenceFlow(sequenceFlow);
        if (target instanceof TActivity) {
            activityList.add((TActivity) target);
        } else if (target instanceof TGateway) {
            for (TSequenceFlow sequenceFlowTarget : model.getSequenceFlowTarget(target)) {
                activityList.addAll(getTargetActivities(model, sequenceFlowTarget));
            }
        }
        return activityList;
    }

}
