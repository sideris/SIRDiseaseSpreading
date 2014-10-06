package main;
import java.lang.Math;

public class Person {
	public int status;
	public int[] position = new int[2];
	public int positionValue = 0;
	private double sizeOfArea = 20;
	
	public Person(){
		status = 1;
		position[0] = 1 + (int)(Math.random()*sizeOfArea); 
		position[1] = 1 + (int)(Math.random()*sizeOfArea);
		positionValue();
	}
	
	public Person(int lattice){
		sizeOfArea = lattice;
		status = 1;
		position[0] = 1 + (int)(Math.random()*sizeOfArea); 
		position[1] = 1 + (int)(Math.random()*sizeOfArea);
		positionValue();
	}
	
	public void updateStatus(int newStatus){
		status = newStatus;
	}
	
	public int recover(double gamma){
		int recovered = 0;
		double random = Math.random();		
		if(random <= gamma){
			status = 3;
			recovered = 1;
		}
		return recovered;	
	}
	
	public int getInfected(double beta){
		double random = Math.random();
		int isInfected = 0;
		if(random <= beta){
			status = 2;
			isInfected = 1;
		}
		return isInfected;		
	}
	
	public int move(double d){
		double random = Math.random();
		int move = 0;
		int hor = 0,ver = 0;
		
		if(random <= d){
			move = 1;
			while(hor==0 && ver==0){
				hor = 1 - (int)(Math.random()*3);
				ver = 1 - (int)(Math.random()*3);
				int forb1 = position[0]+ver;
				int forb2 = position[1]+hor;
				 if( (forb1>sizeOfArea) || (forb2>sizeOfArea) || (forb1<1) || (forb2<1) ){
					 hor = 0; 
					 ver = 0;
				 }
			}
			position[0] = position[0] + ver;
			position[1] = position[1] + hor;
			//update position values
			positionValue();
		}
		return move;
	}
	//we make the position to 1 number so we don't double-check
	public void positionValue(){
		String val = Integer.toString(position[0]) + Integer.toString(position[1]);
		positionValue = Integer.parseInt(val);
	}
	
}	
