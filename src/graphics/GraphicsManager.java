package graphics;

import javax.swing.JFrame;

import org.jfree.data.xy.XYSeries;

public abstract class GraphicsManager extends JFrame {

    private final XYSeries healthy = new XYSeries("Healthy");
    private final XYSeries infected = new XYSeries("Infected");
    private final XYSeries recovered = new XYSeries("Recovered");
	
}
