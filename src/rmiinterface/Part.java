package rmiinterface;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Part implements Serializable{
	private static final long serialVersionUID = 1190476516911661470L;
	private String name;
	private int idPart;
	private String description;
	private HashMap<Part, Integer> subcomponents;
	private String isbn;
	private double cost;
	
	public Part(String isbn){
		this.isbn = isbn;
	}
	
	public Part(String name, String isbn, double cost){
		this.name = name;
		this.isbn = isbn;
		this.cost = cost;
		this.idPart = setId();
		this.subcomponents = new HashMap<Part, Integer>();
	}

	public int setId(){
		Random rand = new Random();
		int randomNum = rand.nextInt((999999 - 100000) + 1) + 100000;
		return randomNum;
	}
	
	public String getPartName() {
		return name;
	}
	
	public int getId(){
		return this.idPart;
	}

	public String getIsbn() {
		return isbn;
	}

	public double getCost() {
		return cost;
	}
	
	public String toString(){
		return "> " + this.name + " ($" + this.cost + ")";
	}
	
	public void addPart(Part part, int n){
		this.subcomponents.put(part, n);
	}
}
