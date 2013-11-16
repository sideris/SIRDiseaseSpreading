package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

@SuppressWarnings("serial")
public class PopulationDynamicsPlot extends JFrame  {
    private static final String title = "Disease Model";
    private final XYSeries healthy = new XYSeries("Healthy");
    private final XYSeries infected = new XYSeries("Infected");
    private final XYSeries recovered = new XYSeries("Recovered");
    
    private ArrayList<Integer> health = new ArrayList<Integer>();
    private ArrayList<Integer> infect = new ArrayList<Integer>();
    private ArrayList<Integer> recover = new ArrayList<Integer>();
	
    public JPanel control;

	public PopulationDynamicsPlot(String s, ArrayList<Integer> h, ArrayList<Integer> inf, ArrayList<Integer> rec )
	{
        super(s);
        health = h;
        infect = inf;
        recover = rec;

        addData();
        final ChartPanel chartPanel = createPanel();
        this.add(chartPanel, BorderLayout.CENTER);
        control = new JPanel();
        this.add(control, BorderLayout.SOUTH);
	}
	
	
	   private ChartPanel createPanel() {
	        JFreeChart jfreechart = ChartFactory.createScatterPlot(
	            title, 
	            "t", 
	            "Population", 
	            getData(),
	            PlotOrientation.VERTICAL, 
	            true, true, false);//legend, tooltips, urls
	        
	        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
	        //create custom shapes
	        Shape up = ShapeUtilities.createUpTriangle(0);
	        Shape down = ShapeUtilities.createDownTriangle(0);
	        Shape diamond = ShapeUtilities.createDiamond(0);

	        //set colours and shapes with renderer
	        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        renderer.setSeriesPaint(0, Color.blue);
	        renderer.setSeriesPaint(1, Color.red);
	        renderer.setSeriesPaint(2, Color.black);
	        renderer.setSeriesShape(0, up);
	        renderer.setSeriesShape(1, down);
	        renderer.setSeriesShape(2, diamond);
	        xyPlot.setRenderer(renderer);

	        /**set the axis properly(range, etc)
	         * domain : X axis
	         * range  : Y axis
	         */
	        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
	        //domain.setRange(-0, lattice+1);
	        domain.setTickUnit(new NumberTickUnit(500));
	        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
	        //range.setRange(-0, lattice+1);
	        range.setTickUnit(new NumberTickUnit(100));
	        //range.setVisible(false);

	        //return the panel with dimensions
	        return new ChartPanel(jfreechart){
	            @Override
	            public Dimension getPreferredSize() {
	                return new Dimension(600, 600);
	            }
	        };
	    }
	   
	    private void addData() {
	        for (int i = 0; i < health.size(); i++) {
	        	healthy.add( (int)i, (int)health.get(i) );
	        	infected.add( (int)i, (int)infect.get(i) );
	        	recovered.add( i, (int)recover.get(i) );
	        }
	    }
	   
	    private XYDataset getData() {
	        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
	        xySeriesCollection.addSeries(healthy);
	        xySeriesCollection.addSeries(infected);
	        xySeriesCollection.addSeries(recovered);
	        return xySeriesCollection;
	    }
}
