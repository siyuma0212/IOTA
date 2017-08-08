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

import com.csvreader.CsvReader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import org.gephi.datalab.api.datatables.DataTablesController;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.preview.api.*;
import org.gephi.preview.types.DependantColor;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.toolkit.demos.plugins.preview.PreviewSketch;
import org.openide.util.Lookup;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
/**
 *
 * @author Mathieu Bastian
 */
public class PreviewJFrame {

    public void script() {
 /*       //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Import file
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        Container container;
        try {
            File file = new File(getClass().getResource("/org/gephi/toolkit/demos/Java.gexf").toURI());
            container = importController.importFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);
*/
        //Preview configuration
       
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
            while(records.readRecord() && i<40)
            {

                System.out.println(i);
                String hash = records.get("hash");
                //System.out.println(hash);
//                System.out.println(hash);
                Node n = graphModel.factory().newNode(hash);
                n.setLabel(hash);
                n.setSize(10);
                n.setX((float) ((0.01 + Math.random()) * 1000) - 500);
                n.setY((float) ((0.01 + Math.random()) * 1000) - 500);
                String edge1 = records.get("branch");
                //System.out.println(edge1);
                String edge2 = records.get("trunk");
                
                Node n1 = graphModel.factory().newNode(edge1);
                n1.setLabel(edge1);
                n1.setSize(10);
                n1.setX((float) ((0.01 + Math.random()) * 1000) - 500);
                n1.setY((float) ((0.01 + Math.random()) * 1000) - 500);
                
                Node n2 = graphModel.factory().newNode(edge2);
                n2.setLabel(edge2);
                n2.setX((float) ((0.01 + Math.random()) * 1000) - 500);
                n2.setY((float) ((0.01 + Math.random()) * 1000) - 500);
                n2.setSize(10);
                i++;
                
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
                System.out.println(directedGraph.getNode(hash).getLabel());
                System.out.println(directedGraph.getNode(hash).getAttribute(trunk));
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
        
        
        System.out.println("Nodes: " + graphModel.getGraph().getNodeCount() + " Edges: " + graphModel.getGraph().getEdgeCount());
      
        //for (Column col : graphModel.getNodeTable()) {
           // System.out.println(col);
        //}
        
        //for(Node n : graphModel.getGraph().getNodes())
        //{
          //  System.out.println(n.getLabel());
        //}
        AutoLayout autoLayout = new AutoLayout(30, TimeUnit.SECONDS);
        autoLayout.setGraphModel(graphModel);
        YifanHuLayout firstLayout = new YifanHuLayout(null, new StepDisplacement(1f));
        ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
        AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout.createDynamicProperty("forceAtlas.adjustSizes.name", Boolean.TRUE, 0.1f);//True after 10% of layout time
        AutoLayout.DynamicProperty repulsionProperty = AutoLayout.createDynamicProperty("forceAtlas.repulsionStrength.name", 500., 0f);//500 for the complete period
        autoLayout.addLayout(firstLayout, 0.5f);
        autoLayout.addLayout(secondLayout, 0.5f, new AutoLayout.DynamicProperty[]{adjustBySizeProperty, repulsionProperty});
        autoLayout.execute();
        System.out.println("Finished auto layout");
 
 
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        //previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.DIRECTED, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_BORDER_COLOR,new DependantColor(Color.BLACK));
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
        previewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY,10.0);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
        //previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 100);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 0);
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);

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

    public static void main(String[] args) throws IOException {
        //ImportFile importGraph = new ImportFile();
        //importGraph.script();
        
        //WithAutoLayout autoLayout = new WithAutoLayout();
        //autoLayout.script();
        
//        public final String HOSTNAME = ;
//        public int final PORT = 8080;
        String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
        Socket socket = new Socket("104.238.185.253", 80);
        
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
        wr.write("POST " + "" + " HTTP/1.0\r\n");
        wr.write("Content-Length: " + data.length() + "\r\n");
        wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
        wr.write("\r\n");
        
        wr.write("hello world.");
        
        PreviewJFrame previewJFrame = new PreviewJFrame();
        previewJFrame.script();
        
        //wr.write(previewJFrame.script().frame);
        
        System.out.println("finished");
    }


}
