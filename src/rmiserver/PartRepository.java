package rmiserver;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import rmiinterface.Part;
import rmiinterface.RMIInterface;

public class PartRepository extends UnicastRemoteObject implements RMIInterface{
	private static final long serialVersionUID = 1L;
	private List<Part> partRepository;

	protected PartRepository(List<Part> list) throws RemoteException {
		super();
		this.partRepository = list;
	}
	
	//The client sends a Book object with the isbn information on it (note: it could be a string with the isbn too)
	//With this method the server searches in the List bookList for any book that has that isbn and returns the whole object
	@Override
	public Part findPart(Part part) throws RemoteException {
		Predicate<Part> predicate = x-> x.getIsbn().equals(part.getIsbn());
		return partRepository.stream().filter(predicate).findFirst().get();
		
	}
	
	@Override
	public List<Part> allParts() throws RemoteException {
		return partRepository;
	}
	
	private static List<Part> initializeList(){
		List<Part> list = new ArrayList<>();
		list.add(new Part("Head First Java, 2nd Edition", "978-0596009205", 31.41));
		list.add(new Part("Java In A Nutshell", "978-0596007737", 10.90));
		list.add(new Part("Java: The Complete Reference", "978-0071808552", 40.18));
		list.add(new Part("Head First Servlets and JSP", "978-0596516680", 35.41));
		list.add(new Part("Java Puzzlers: Traps, Pitfalls, and Corner Cases", "978-0321336781", 39.99));
		return list;
	}
	
	@Override
	public void addp(String name, String description, int idPart) throws RemoteException {
		this.partRepository.add(new Part(name, description, 0.0));
	}
	
	public static void main(String[] args){
        try {
            Naming.rebind("//localhost/PartRepository", new PartRepository(initializeList()));
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }
}
