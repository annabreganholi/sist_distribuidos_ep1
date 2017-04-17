package rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote{
	public Part findPart(Part b) throws RemoteException;
	public List<Part> allBooks() throws RemoteException;
	//public void getp(); //Get part by number part passa a ser a corrente
	//public void showp(); //Show atributes from Part
	//public void clearList();//Clear part list
	//public void addsubpart();//adiciona n pe�as da subpart corrente na subpart corrente
	//public void addp(); //Adiciona pe�a ao repositorio corrente
	
}