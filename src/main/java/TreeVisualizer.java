import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.*;

public class TreeVisualizer {

    long nodeCounter = 0;
    List<Node> links;

    // build the tree links starting from the root node (root state), generate the graph, save it to png file, open the generated png file
    void drawMinimaxTree(HashMap<State, State[]> statesMap, State rootState) {
        try {
            nodeCounter = 0;
            links = new ArrayList<>();
            Node rootNode = node(getNodeId()).with(Label.html(String.valueOf(rootState.getValue())));
            buildLinks(statesMap, rootState, rootNode);
            Graph graph = graph("Tree").directed().with(links);
            File treeImage = new File("TreeImage.svg");
            Graphviz.fromGraph(graph).render(Format.SVG).toFile(treeImage);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(treeImage);
        } catch (Exception ignored) {
        }
    }

    /*
        recursive function takes a root state, extract its children from the minimax tree
        convert each child to a new node with unique id and set its label as the state minimax value
        recursively build the children tree for each generated new node
        link the root node to its extracted children nodes and add the result tree to the links (trees) list
     */
    private void buildLinks(HashMap<State, State[]> statesMap, State rootState, Node rootNode) {
        // make sure that the rootNode isn't a terminal node
        if (statesMap.containsKey(rootState)) {
            List<Node> childrenNodes = new ArrayList<>();
            for (State childState : statesMap.get(rootState)) {
                // the minimax tree contains an extra layer of terminal children with 0 values (empty list), we skip those nodes.
                if (!statesMap.containsKey(childState))
                    break;
                Node childNode = node(getNodeId()).with(Label.html(String.valueOf(childState.getValue())));
                childrenNodes.add(childNode);
                buildLinks(statesMap, childState, childNode);
            }
            links.add(rootNode.link(childrenNodes));
        }
    }

    // function to help generating and giving each node a unique id helping to represent the tree graph
    String getNodeId() {
        nodeCounter++;
        return String.valueOf(nodeCounter);
    }

}