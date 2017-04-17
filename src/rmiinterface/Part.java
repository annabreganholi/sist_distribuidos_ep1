package rmiinterface;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Part implements Serializable{
	private static final long serialVersionUID = 1190476516911661470L;
	private String name;
	private String idPart;
	private String description;
	private HashMap<Part, Integer> subcomponents;
	//private String isbn;
	private double cost;
	
	public Part(String idPart){
		this.idPart = idPart;
	}
	
	public Part(String name, String isbn, double cost){
		this.name = name;
		this.cost = cost;
		this.idPart = setId();
		this.subcomponents = new HashMap<Part, Integer>();
	}

	public String setId(){
		Random rand = new Random();
		int randomNum = rand.nextInt((999999 - 100000) + 1) + 100000;
		return Integer.toString(randomNum);
	}
	
	public String getPartName() {
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getId(){
		return this.idPart;
	}

	public double getCost() {
		return cost;
	}
	
	public String toString(){
		return "> " + this.name + " (ID:" + this.idPart + ")";
	}
	
	public void clearSubcomponents(){
		this.subcomponents.clear();
	}
	
	public void addPart(Part part, int n){
		this.subcomponents.put(part, n);
	}
}
