import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.*;

public class TreeVisualizer {

    long nodeCounter = 0;
    List<Node> links;

    void drawMinimaxTree(HashMap<State, State[]> statesMap, State rootState) {
        try {
            links = new ArrayList<>();
            Node rootNode = node(getNodeId()).with(Label.html(String.valueOf(rootState.getValue())));
            buildLinks(statesMap, rootState, rootNode);
            Graph graph = graph("Tree").directed().with(links);
            Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File("treeTest.png"));
        } catch (Exception ignored) {
        }
    }

    private void buildLinks(HashMap<State, State[]> statesMap, State rootState, Node rootNode) {
        if (statesMap.containsKey(rootState)) {
            List<Node> childrenNodes = new ArrayList<>();
            for (State childState : statesMap.get(rootState)) {
                if (!statesMap.containsKey(childState))
                    break;
                Node childNode = node(getNodeId()).with(Label.html(String.valueOf(childState.getValue())));
                childrenNodes.add(childNode);
                buildLinks(statesMap, childState, childNode);
            }
            links.add(rootNode.link(childrenNodes));
        }
    }

    String getNodeId() {
        nodeCounter++;
        return String.valueOf(nodeCounter);
    }

}