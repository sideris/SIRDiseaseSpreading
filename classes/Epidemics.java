package classes;

import graphics.MakePhase;
import graphics.ModelVisualization;
import graphics.PopulationDynamicsPlot;
import graphics.RInfPlot;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
@SuppressWarnings("unused")
public class Epidemics{
	private static final int lattice = 100, populationSize = 1000; 
	public static double delta = 0.3, beta = 0.5, gamma = 0.005, infectRatio = 0.01; 
	//1.population List 
	private static ArrayList<Person> population = new ArrayList<Person>();
	//2.infection and recovered lists.
	private static ArrayList<Integer> infected = new ArrayList<Integer>();
	private static ArrayList<Integer> infectedPosition = new ArrayList<Integer>();
	private static ArrayList<Integer> recovered = new ArrayList<Integer>();
	//3. lists of the corresponding population status for plotting purposes
	private static ArrayList<Integer> simInfected = new ArrayList<Integer>();
	private static ArrayList<Integer> simRecovered = new ArrayList<Integer>();
	private static ArrayList<Integer> simHealthy = new ArrayList<Integer>();
	//4. list for the Rinf/R curve
	private static ArrayList<Integer> Rinf = new ArrayList<Integer>();
	private static ArrayList<Double> R = new ArrayList<Double>();
	//5. for the phase diagram
	private static ArrayList<Integer> Phase_infected = new ArrayList<Integer>();
	private static ArrayList<Double> phase_rr = new ArrayList<Double>();
	private static ArrayList<Double> phase_beta = new ArrayList<Double>();

	private static int[] positionValues = new int[populationSize];
	
	public static void main(String [] args){
		createPopulation(populationSize,infectRatio, lattice);
//		runModel();
//		runDynamics();
//		runInf();
		runPhase();
	}
	
	private static void createPopulation(int size, double ratio, int lat){
		//create the agents and add them to the list
		for(int i = 0;i<size;i++){
			Person a = new Person(lat);
			population.add( a );
			positionValues[i] = a.positionValue;
		}
		//creating lists to make a random permutation
		int infectionSize = (int) (populationSize*ratio);
		int[] lista = new int[populationSize];
		for(int i=0;i<populationSize;i++){
			lista[i] = i;
		}
		//(Everyday I'm) shuffling
		lista = FuncLib.shuffleArray(lista);
		//infect
		for(int i = 0;i<infectionSize;i++){
			population.get(lista[i]).status=2;
			//adding to the infected list and its position
			infected.add(lista[i]);
			infectedPosition.add(population.get(lista[i]).positionValue);
		}
	}

	private static void clearVars(){
		population.clear();
		infected.clear();
		recovered.clear();
		simInfected.clear();
		simRecovered.clear();
		simHealthy.clear();
	}
	
	/*Here is the magic done*/
	public static void dotheFlop(double gamma, double beta){
        for(int i = 0; i<populationSize;i++){
        	int prevPos = population.get(i).positionValue;

        	/**Check for near INFECTION and Act **/
        	boolean zombieAttack = infectedPosition.contains(population.get(i).positionValue);
        	
        	if(zombieAttack && population.get(i).status!=2 &&  population.get(i).status!=3  ){
        		int isInfected = population.get(i).getInfected(beta);
        		if(isInfected == 1){
            		infected.add(i);
            		infectedPosition.add( population.get(i).positionValue );
        		}
        	}
        	
        	/**Check if RECOVERY**/
        	if(population.get(i).status==2 ){
        		int didRecover = population.get(i).recover(gamma);
        		if(didRecover == 1){
        			infectedPosition.remove( infectedPosition.indexOf(prevPos) );
        			infected.remove( infected.indexOf(i) );
        			recovered.add(i);
        		}
        	}

        	/**Do the MOVE**/
        	int moved = population.get(i).move(delta);
    		
        	//update infection lists
        	if(population.get(i).status == 2 && moved == 1){
        		infectedPosition.remove( infectedPosition.indexOf(prevPos) );
        		infected.remove( infected.indexOf(i) );
        		infected.add(i);
        		infectedPosition.add( population.get(i).positionValue );
        	}
        }
        simInfected.add( infected.size() );
        simRecovered.add(recovered.size() );
        simHealthy.add( populationSize - infected.size() - recovered.size() );
	}
	
	private static void runPhase(){
		for(double b = 0.01;b<=1;b = b + 0.005){
			for(double g = 0.01;g<=1;g = g + 0.005 ){
				clearVars();
				createPopulation(populationSize,infectRatio, lattice);
				while(!infected.isEmpty()){
					dotheFlop(g, b);
				}
				//add the final number of the total infected
				Phase_infected.add( simRecovered.get( simRecovered.size() - 1 ) );
				phase_beta.add(b);
				phase_rr.add(b/g);
			}
		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				//create the Scatter-plot
				final MakePhase modelRun = new MakePhase(
						"Beta Gamma Phase", 
						Phase_infected,
						phase_rr,
						phase_beta);
				modelRun.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				modelRun.pack();
				modelRun.setLocationRelativeTo(null);
				modelRun.setVisible(true);
			}
		});
	}
	
	private static void runInf(){
		for(double g = 0.0001;g<0.6;g = g + 0.005 ){
			clearVars();
			createPopulation(populationSize,infectRatio, lattice);
			while(!infected.isEmpty()){
				dotheFlop(g, beta);
			}
			//add the final number of the total infected
			Rinf.add( simRecovered.get( simRecovered.size() - 1 ) );
			R.add(beta/g);
		}
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				//create the Scatter-plot
				final RInfPlot modelRun = new RInfPlot(
						"Disease Dynamics", 
						Rinf,
						R);
				modelRun.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				modelRun.pack();
				modelRun.setLocationRelativeTo(null);
				modelRun.setVisible(true);
			}
		});
	}
	
	/**Runs the population Dynamics for plot**/
	private static void runDynamics(){
		while(!infected.isEmpty()){
			dotheFlop(gamma, beta);
		}
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				//create the Scatter-plot
				final PopulationDynamicsPlot modelRun = new PopulationDynamicsPlot(
						"Disease Dynamics", 
						simHealthy, 
						simInfected, 
						simRecovered);
				modelRun.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				modelRun.pack();
				modelRun.setLocationRelativeTo(null);
				modelRun.setVisible(true);
			}
		});
		
	}
	
	/**Runs the lattice-agent Model**/
	private static void runModel(){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				//create the Scatter-plot
				final ModelVisualization modelRun = new ModelVisualization(
						"Disease", 
						lattice, 
						populationSize, 
						lattice, 
						population);
				modelRun.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				modelRun.pack();
				modelRun.setLocationRelativeTo(null);
				modelRun.setVisible(true);
				//create action for firing button
				final ActionEvent e = new ActionEvent(modelRun.moveButton, 1001, "Next step");  
				
				//set timer to repeat every few seconds
				final Timer timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						modelRun.moveButton.processEvent(e);// Here the event is fired 	 
						if (infected.isEmpty()) {
							timer.cancel();
							timer.purge();
							return;
						}
					}
				}, 1, 100);
			}
		});
	}
	
}
