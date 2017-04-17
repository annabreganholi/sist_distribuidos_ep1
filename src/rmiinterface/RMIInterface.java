package rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote{
	public Part getp(Part b) throws RemoteException;
	public List<Part> allParts() throws RemoteException;
	public String showp(Part part) throws RemoteException; //Show atributes from Part
	public void clearList(Part part) throws RemoteException;//Clear part list
	public void clearList() throws RemoteException;//Clear part list
	public void addsubpart(Part part, String[] subparts) throws RemoteException;//adiciona n peças da subpart corrente na subpart corrente
	public Part addp(String name, String description) throws RemoteException; //Adiciona peça ao repositorio corrente
	
}