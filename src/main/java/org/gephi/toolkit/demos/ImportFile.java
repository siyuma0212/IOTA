/*
Copyright 2008-2010 Gephi
Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.toolkit.demos;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.gephi.appearance.api.AppearanceController;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.appearance.api.Function;
import org.gephi.appearance.plugin.RankingElementColorTransformer;
import org.gephi.appearance.plugin.RankingNodeSizeTransformer;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

import com.csvreader.CsvReader;
import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import org.gephi.graph.api.*;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.toolkit.demos.plugins.preview.PreviewSketch;
import org.openide.util.Lookup;

/**
 * This demo shows basic features from GraphAPI, how to create and query a graph
 * programmatically.
 *
 * @author Mathieu Bastian
 */
public class ImportFile {

    public void script() {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Get a graph model - it exists because we have a workspace
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        Column tag = graphModel.getNodeTable().addColumn("tag",String.class);
        Column address = graphModel.getNodeTable().addColumn("address",String.class);
        Column branch  = graphModel.getNodeTable().addColumn("branch",String.class);
        Column trunk = graphModel.getNodeTable().addColumn("trunk",String.class);
        Column bundle = graphModel.getNodeTable().addColumn("bundle",String.class);
        Column currentIndex = graphModel.getNodeTable().addColumn("currentIndex",Integer.class);
        Column lastIndex = graphModel.getNodeTable().addColumn("lastIndex",Integer.class);
        Column value = graphModel.getNodeTable().addColumn("value",Long.class);
        Column timestamp = graphModel.getNodeTable().addColumn("timestamp",Long.class);
        
        
        String filename = "/Users/cecilia/Documents/iota/data/from0407.csv";
        int i = 0;
        DirectedGraph directedGraph = graphModel.getDirectedGraph();
        try{
            CsvReader records = new CsvReader(filename);
            
            records.readHeaders();

            while(records.readRecord())
            {
                i++;
                System.out.println(i);
                String hash = records.get("hash");
                Node n = graphModel.factory().newNode(hash);
                n.setLabel(hash);
                //System.out.println("1");
                
                String edge1 = records.get("branch");
                String edge2 = records.get("trunk");
                
                Node n1 = graphModel.factory().newNode(edge1);
                n1.setLabel(edge1);
                Node n2 = graphModel.factory().newNode(edge2);
                n2.setLabel(edge2);
                
                
//                Edge e1 = graphModel.factory().newEdge(n, n1, true);
//                Edge e2 = graphModel.factory().newEdge(n, n2, true);
                //System.out.println("2");
                

                if(directedGraph.getNode(n.getId()) == null)
                {
                    directedGraph.addNode(n);
                    //graphModel.getGraph().getNode(n.getId());
                    n.setAttribute(address,records.get("address"));
                    n.setAttribute(tag,records.get("tag"));
                    n.setAttribute(bundle, records.get("bundle"));
                    n.setAttribute(branch, records.get("branch"));
                    n.setAttribute(trunk, records.get("trunk"));
                    n.setAttribute(currentIndex, Integer.parseInt(records.get("currentIndex")));
                    n.setAttribute(lastIndex, Integer.parseInt(records.get("lastIndex")));
                    n.setAttribute(timestamp, Long.parseLong(records.get("timestamp")));
                    n.setAttribute(value, Long.parseLong(records.get("value")));
                    
                    //System.out.println("a");
                }
                else
                {
                    n = directedGraph.getNode(n.getId());
                }
                
                if(directedGraph.getNode(n1.getId()) == null)
                {
                    directedGraph.addNode(n1);
                    //System.out.println("b");
                }
                else
                {
                    n1 = directedGraph.getNode(n1.getId());
                }
                if(directedGraph.getNode(n2.getId()) == null)
                {
                    directedGraph.addNode(n2);
                    //System.out.println("c");
                }
                else
                {
                    n2 = directedGraph.getNode(n2.getId());
                }
                if(directedGraph.getEdge(n,n1)== null)
                {   
                    Edge e1 = graphModel.factory().newEdge(n, n1, true);
                    directedGraph.addEdge(e1);
                    //System.out.println("33");
                }
                
                if(directedGraph.getEdge(n,n2) == null)
                {
                    Edge e2 = graphModel.factory().newEdge(n, n2, true);
                    directedGraph.addEdge(e2);
                }
            }
            records.close();
        }catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
        //System.out.println("Nodes: " + graphModel.getGraph().getNodeCount() + " Edges: " + graphModel.getGraph().getEdgeCount());
        
        //for (Column col : graphModel.getNodeTable()) {
           // System.out.println(col);
        //}
        
        //Column bundle = graphModel.getNodeTable().getColumn("bundle");
        //for(Node n : graphModel.getGraph().getNodes())
        //{
            //System.out.println(n.getAttribute(bundle));
        //}
       PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.DIRECTED, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.WHITE));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.BLACK);

        //New Processing target, get the PApplet
        G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
        final PreviewSketch previewSketch = new PreviewSketch(target);
        previewController.refreshPreview();

        //Add the applet to a JFrame and display
        JFrame frame = new JFrame("Test Preview");
        frame.setLayout(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(previewSketch, BorderLayout.CENTER);

        frame.setSize(1024, 768);
        
        //Wait for the frame to be visible before painting, or the result drawing will be strange
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                previewSketch.resetZoom();
            }
        });
        frame.setVisible(true);
    }
}     


        /*//Create three nodes
        Node n0 = graphModel.factory().newNode("n0");
        n0.setLabel("Node 0");
        Node n1 = graphModel.factory().newNode("n1");
        n1.setLabel("Node 1");
        Node n2 = graphModel.factory().newNode("n2");
        n2.setLabel("Node 2");

        //Create three edges
        Edge e1 = graphModel.factory().newEdge(n1, n2, 0, 1.0, true);
        Edge e2 = graphModel.factory().newEdge(n0, n2, 0, 2.0, true);
        Edge e3 = graphModel.factory().newEdge(n2, n0, 0, 2.0, true);   //This is e2's mutual edge

        //Append as a Directed Graph
        DirectedGraph directedGraph = graphModel.getDirectedGraph();
        directedGraph.addNode(n0);
        directedGraph.addNode(n1);
        directedGraph.addNode(n2);
        directedGraph.addEdge(e1);
        directedGraph.addEdge(e2);
        directedGraph.addEdge(e3);

        //Count nodes and edges
        System.out.println("Nodes: " + directedGraph.getNodeCount() + " Edges: " + directedGraph.getEdgeCount());

        //Get a UndirectedGraph now and count edges
        UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();
        System.out.println("Edges: " + undirectedGraph.getEdgeCount());   //The mutual edge is automatically merged

        //Iterate over nodes
        for (Node n : directedGraph.getNodes()) {
            Node[] neighbors = directedGraph.getNeighbors(n).toArray();
            System.out.println(n.getLabel() + " has " + neighbors.length + " neighbors");
        }

        //Iterate over edges
        for (Edge e : directedGraph.getEdges()) {
            System.out.println(e.getSource().getId() + " -> " + e.getTarget().getId());
        }

        //Find node by id
        Node node2 = directedGraph.getNode("n2");

        //Get degree
        System.out.println("Node2 degree: " + directedGraph.getDegree(node2));

        //Modify the graph while reading
        //Due to locking, you need to use toArray() on Iterable to be able to modify
        //the graph in a read loop
        for (Node n : directedGraph.getNodes().toArray()) {
            directedGraph.removeNode(n);
        }
    }*/

