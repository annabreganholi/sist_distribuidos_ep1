package rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote{
	public Part findPart(Part b) throws RemoteException;
	public List<Part> allParts() throws RemoteException;
	//public void getp(); //Get part by number part passa a ser a corrente
	public String showp(Part part) throws RemoteException; //Show atributes from Part
	public void clearList(Part part) throws RemoteException;//Clear part list
	public void clearList() throws RemoteException;//Clear part list
	//public void addsubpart();//adiciona n peças da subpart corrente na subpart corrente
	public void addp(String name, String description, int idPart) throws RemoteException; //Adiciona peça ao repositorio corrente
	
}