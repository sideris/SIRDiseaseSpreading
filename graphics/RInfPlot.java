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
public class RInfPlot extends JFrame  {
    private static final String title = "R plane";
    private final XYSeries infected = new XYSeries("Infected");
    
    private ArrayList<Integer> infect = new ArrayList<Integer>();
    private ArrayList<Double> R = new ArrayList<Double>();

    public JPanel control;

	public RInfPlot(String s, ArrayList<Integer> inf, ArrayList<Double> Rr ){
        super(s);
        infect = inf;
        R = Rr;
        
        addData();
        final ChartPanel chartPanel = createPanel();
        this.add(chartPanel, BorderLayout.CENTER);
        control = new JPanel();
        this.add(control, BorderLayout.SOUTH);
	}
	
	
	   private ChartPanel createPanel() {
	        JFreeChart jfreechart = ChartFactory.createScatterPlot(
	            title, 
	            "R", 
	            "Population", 
	            getData(),
	            PlotOrientation.VERTICAL, 
	            true, true, false);//legend, tooltips, urls
	        
	        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
	        //create custom shapes
	        Shape diamond = ShapeUtilities.createDiamond(0);
	        //set colours and shapes with renderer
	        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        renderer.setSeriesPaint(0, Color.blue);
	        renderer.setSeriesShape(0, diamond);
	        xyPlot.setRenderer(renderer);

	        /**set the axis properly(range, etc)
	         * domain : X axis
	         * range  : Y axis
	         */
	        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
	        domain.setTickUnit(new NumberTickUnit(1));
	        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
	        range.setTickUnit(new NumberTickUnit(100));

	        //return the panel with dimensions
	        return new ChartPanel(jfreechart){
	            @Override
	            public Dimension getPreferredSize() {
	                return new Dimension(600, 600);
	            }
	        };
	    }
	   
	    private void addData() {
	        for (int i = 0; i < infect.size(); i++) {
	        	infected.add(R.get(i) , infect.get(i) );
	        }
	    }
	   
	    private XYDataset getData() {
	        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
	        xySeriesCollection.addSeries(infected);
	        return xySeriesCollection;
	    }
}
