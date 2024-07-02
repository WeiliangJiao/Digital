/*
 * Copyright (c) 2018 Helmut Neemann.
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.hdl.model2.optimizations;

import de.neemann.digital.hdl.model2.HDLCircuit;
import de.neemann.digital.hdl.model2.HDLNet;
import de.neemann.digital.hdl.model2.HDLNode;
import de.neemann.digital.hdl.model2.HDLPort;
import de.neemann.digital.hdl.model2.expression.ExprUsingNet;
import de.neemann.digital.hdl.model2.expression.Expression;
import de.neemann.digital.hdl.model2.expression.Visitor;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Used to sort the nodes in a more "human typical" order.
 * Sorts the nodes from the input side to the output side.
 */
public class NodeSorterExpressionBased implements Optimization {

    @Override
    public void optimize(HDLCircuit circuit) {
        ArrayList<HDLNode> nodes = circuit.getNodes();
        ArrayList<HDLNode> nodesAvail = new ArrayList<>(nodes);
        nodes.clear();

        HashSet<HDLNet> nets = initializeNets(circuit);

        addNodesWithoutInputs(nodes, nodesAvail, nets);

        performLayerSorting(nodes, nodesAvail, nets);

        // if there are unsolvable circular dependencies, keep old order
        if (!nodesAvail.isEmpty()) {
            nodes.addAll(nodesAvail);
        }
    }

    private HashSet<HDLNet> initializeNets(HDLCircuit circuit) {
        HashSet<HDLNet> nets = new HashSet<>();
        for (HDLPort p : circuit.getInputs()) {
            nets.add(p.getNet());
        }
        return nets;
    }

    private void addNodesWithoutInputs(ArrayList<HDLNode> nodes, ArrayList<HDLNode> nodesAvail, HashSet<HDLNet> nets) {
        for (HDLNode n : nodesAvail) {
            if (n.getInputs().isEmpty()) {
                nodes.add(n);
                for (HDLPort p : n.getOutputs()) {
                    if (p.getNet() != null) {
                        nets.add(p.getNet());
                    }
                }
            }
        }
        nodesAvail.removeAll(nodes);
    }

    private void performLayerSorting(ArrayList<HDLNode> nodes, ArrayList<HDLNode> nodesAvail, HashSet<HDLNet> nets) {
        while (!nodesAvail.isEmpty()) {
            ArrayList<HDLNode> layer = getHdlNodes(nodesAvail, nets);

            extracted(nodesAvail, nets, layer);

            if (layer.isEmpty()) {
                break;
            }

            nodes.addAll(layer);
            nodesAvail.removeAll(layer);
            for (HDLNode n : layer) {
                for (HDLPort p : n.getOutputs()) {
                    if (p.getNet() != null) {
                        nets.add(p.getNet());
                    }
                }
            }
        }
    }

    private static void extracted(ArrayList<HDLNode> nodesAvail, HashSet<HDLNet> nets, ArrayList<HDLNode> layer) {
        if (layer.isEmpty()) {
            for (HDLNode n : nodesAvail) {
                if (n.traverseExpressions(new DependsAtLeastOnOneOf(nets)).ok()) {
                    layer.add(n);
                }
            }
        }
    }

    private static ArrayList<HDLNode> getHdlNodes(ArrayList<HDLNode> nodesAvail, HashSet<HDLNet> nets) {
        ArrayList<HDLNode> layer = new ArrayList<>();
        for (HDLNode n : nodesAvail) {
            if (n.traverseExpressions(new DependsOnlyOn(nets)).ok()) {
                layer.add(n);
            }
        }
        return layer;
    }

    private static final class DependsOnlyOn implements Visitor {
        private final HashSet<HDLNet> nets;
        private boolean dependsOnlyOn = true;

        private DependsOnlyOn(HashSet<HDLNet> nets) {
            this.nets = nets;
        }

        @Override
        public void visit(Expression expression) {
            if (expression instanceof ExprUsingNet) {
                final HDLNet net = ((ExprUsingNet) expression).getNet();
                if (!net.isClock() && !nets.contains(net))
                    dependsOnlyOn = false;
            }
        }

        public boolean ok() {
            return dependsOnlyOn;
        }
    }

    private static final class DependsAtLeastOnOneOf implements Visitor {
        private final HashSet<HDLNet> nets;
        private boolean dependsAtLeastOnOne = false;

        private DependsAtLeastOnOneOf(HashSet<HDLNet> nets) {
            this.nets = nets;
        }

        @Override
        public void visit(Expression expression) {
            if (expression instanceof ExprUsingNet) {
                final HDLNet net = ((ExprUsingNet) expression).getNet();
                if (!net.isClock() && nets.contains(net))
                    dependsAtLeastOnOne = true;
            }
        }

        public boolean ok() {
            return dependsAtLeastOnOne;
        }
    }

    private boolean dependsOnlyOn(HDLNode n, HashSet<HDLNet> nets) {
        for (HDLPort p : n.getInputs())
            if (!p.getNet().isClock() && !nets.contains(p.getNet()))
                return false;
        return true;
    }

    private boolean dependsAtLeastAtOne(HDLNode n, HashSet<HDLNet> nets) {
        for (HDLPort p : n.getInputs())
            if (!p.getNet().isClock() && nets.contains(p.getNet()))
                return true;
        return false;
    }

}
