package rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote{
	public Part getp(Part b) throws RemoteException;
	public List<Part> allParts() throws RemoteException;
	public String showp(Part part) throws RemoteException; 
	public void clearList(Part part) throws RemoteException;
	public void clearList() throws RemoteException;
	public Part addp(String name, String description, String[] subparts) throws RemoteException;
	public boolean checkPrimitive(Part part)throws RemoteException; 
	public String listSubparts(String partId) throws RemoteException;
	public Part addprimitive(String name, String description) throws RemoteException; 
}