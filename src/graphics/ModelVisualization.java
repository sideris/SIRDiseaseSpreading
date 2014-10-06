package graphics;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import main.Epidemics;
import main.Person;

@SuppressWarnings("serial")
public class ModelVisualization extends JFrame {

    private static final String title = "Disease Model";
    private final XYSeries healthy = new XYSeries("Healthy");
    private final XYSeries infected = new XYSeries("Infected");
    private final XYSeries recovered = new XYSeries("Recovered");
	//imported variables
    private int populationSize, lattice;
	ArrayList<Person> population = new ArrayList<Person>();
	
	public JPanel control;
	public MyButton moveButton;  

    public ModelVisualization(String s, int latt, int popSize, int lat, ArrayList<Person> pop ) {
        super(s);
        //init same variables
        populationSize = popSize;
        population = pop;
        lattice = lat;
       
        update();
        
        final ChartPanel chartPanel = createPanel();
        this.add(chartPanel, BorderLayout.CENTER);
        //set actionListener
        ActionListener printListener = new ActionListener() {  
            public void actionPerformed(ActionEvent ae) { 
                Epidemics.dotheFlop(Epidemics.gamma, Epidemics.beta);
            	clearAll();
                update();
            }  
          }; 
          
        moveButton = new MyButton("Next step");
        moveButton.setVisible(false);
        control = new JPanel();
        control.add(moveButton);
        moveButton.addActionListener(printListener);  
        this.add(control, BorderLayout.SOUTH);
    }

    public void newPoints(ArrayList<Person> pop){
    	population = pop;
    }
    
    public void clearAll(){
        healthy.clear();
        infected.clear();
        recovered.clear();
    }
    
    private void update() {
        for (int i = 0; i < populationSize; i++) {
        	if(population.get(i).status==1) 
        		healthy.add(
        			population.get(i).position[0],
        			population.get(i).position[1]);
        	if(population.get(i).status==2) 
        		infected.add(
        				population.get(i).position[0],
        				population.get(i).position[1]);
        	if(population.get(i).status==3) 
        		recovered.add(
        				population.get(i).position[0],
        				population.get(i).position[1]);
        }
    }

    private ChartPanel createPanel() {
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            title, "", "", 
            createData(),
            PlotOrientation.VERTICAL,
            true, true, false);//legend, tooltips, urls
        
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        //create custom shapes
        Shape up = ShapeUtilities.createUpTriangle(2);
        Shape down = ShapeUtilities.createDownTriangle(3);
        Shape diamond = ShapeUtilities.createDiamond(2);

        //set colours and shapes with renderer
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        renderer.setSeriesPaint(1, Color.red);
        renderer.setSeriesPaint(2, Color.black);
        renderer.setSeriesShape(0, up);
        renderer.setSeriesShape(1, down);
        renderer.setSeriesShape(2, diamond);
        
        /**set the axis properly(range, etc)
         * domain : X axis
         * range  : Y axis
         */
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setRange(-0, lattice+1);
        domain.setTickUnit(new NumberTickUnit(10));
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        range.setRange(-0, lattice+1);
        range.setTickUnit(new NumberTickUnit(10));
        //range.setVisible(false);

        //return the panel with dimensions
        return new ChartPanel(jfreechart){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 600);
            }
        };
    }

    private XYDataset createData() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(healthy);
        xySeriesCollection.addSeries(infected);
        xySeriesCollection.addSeries(recovered);
        return xySeriesCollection;
    }
    
    public class MyButton extends Button {  
        public MyButton(String label) {  
            super(label);  
        }  
       
        public void processEvent(AWTEvent e) {  
        	super.processEvent(e);
        }
        
    }
}