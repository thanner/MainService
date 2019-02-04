package br.edu.ufrgs.inf.bpm.validation.grapheditdistance;

import br.edu.ufrgs.inf.bpm.wrapper.BpmnWrapper;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TFlowNode;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.processmining.analysis.graphmatching.algos.GraphEditDistanceGreedy;
import org.processmining.analysis.graphmatching.graph.SimpleGraph;
import org.processmining.analysis.graphmatching.graph.TwoVertices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimilarityHandler {

    private static Object weights[] = {"vweight", 0.0, "eweight", 0.0, "sweight", 0.0, "ledcutoff", 0.0};
    private GraphEditDistanceGreedy graphEditDistanceGreedy;
    private double graphEditDistance;
    private int id = 0;

    public SimilarityHandler() {
        graphEditDistanceGreedy = new GraphEditDistanceGreedy();
    }

    public static void setWeightskippedVertices(double value) {
        weights[1] = value;
    }

    public static void setWeightskippedEdges(double value) {
        weights[3] = value;
    }

    public static void setWeightsubstitutedVertices(double value) {
        weights[5] = value;
    }

    public static void setNotConsideredCutOffSimilarity(double value) {
        weights[7] = value;
    }

    public void compute(SimpleGraph simpleGraphOriginal, SimpleGraph simpleGraphGenerated) {
        graphEditDistanceGreedy.setWeight(weights);
        graphEditDistance = graphEditDistanceGreedy.compute(simpleGraphOriginal, simpleGraphGenerated);
    }

    public SimpleGraph generateSimpleGraph(TDefinitions definitions, Map<TFlowNode, Integer> elementMap) {
        Set<Integer> vertices = new HashSet<>();
        Map<Integer, String> labels = new HashMap<>();

        Map<Integer, Set<Integer>> incomingEdges = new HashMap<>();
        Map<Integer, Set<Integer>> outgoingEdges = new HashMap<>();

        Set<Integer> functionVertices = new HashSet<>();
        Set<Integer> eventVertices = new HashSet<>();
        Set<Integer> connectorVertices = new HashSet<>();

        BpmnWrapper bpmnWrapper = new BpmnWrapper(definitions);

        for (TFlowElement flowObject : bpmnWrapper.getFlowElementListDeep()) {
            if (flowObject instanceof TFlowNode) {
                vertices.add(elementMap.get(flowObject));
                String label = flowObject.getName() != null ? flowObject.getName() : "";
                labels.put(elementMap.get(flowObject), label);

                incomingEdges.put(elementMap.get(flowObject), new HashSet<>());
                outgoingEdges.put(elementMap.get(flowObject), new HashSet<>());
            }
        }

        for (TSequenceFlow sequenceFlow : bpmnWrapper.getSequenceFlowList()) {
            incomingEdges.get(elementMap.get(bpmnWrapper.getTargetBySequenceFlow(sequenceFlow))).add(elementMap.get(bpmnWrapper.getSourceBySequenceFlow(sequenceFlow)));
            outgoingEdges.get(elementMap.get(bpmnWrapper.getSourceBySequenceFlow(sequenceFlow))).add(elementMap.get(bpmnWrapper.getTargetBySequenceFlow(sequenceFlow)));
        }

        return new SimpleGraph(vertices, outgoingEdges, incomingEdges, labels, functionVertices, eventVertices, connectorVertices);
    }


    private Map<TFlowNode, Integer> mapElements(BpmnWrapper bpmnWrapper) {
        Map<TFlowNode, Integer> elementMap = new HashMap<>();

        for (TFlowNode tFlowNode : bpmnWrapper.getFlowNodeList()) {
            elementMap.put(tFlowNode, id++);
        }

        return elementMap;
    }

    public double getSimilarity() {
        return 1 - graphEditDistance;
    }

    public Set<TwoVertices> getMapping() {
        return graphEditDistanceGreedy.bestMapping();
    }

}
