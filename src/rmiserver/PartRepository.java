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
	
	@Override
	public Part findPart(Part part) throws RemoteException {
		Predicate<Part> predicate = x-> x.getId().equals(part.getId());
		return partRepository.stream().filter(predicate).findFirst().get();
		
	}
	
	@Override
	public List<Part> allParts() throws RemoteException {
		return partRepository;
	}
	
	@Override
	public void addp(String name, String description, int idPart) throws RemoteException {
		this.partRepository.add(new Part(name, description, 0.0));
	}	
	
	@Override
	public void clearList(Part part) throws RemoteException{
		part.clearSubcomponents();
	}
	
	@Override
	public void clearList() throws RemoteException{
		this.partRepository.clear();
	}
	
	@Override
	public String showp(Part part) throws RemoteException{
		StringBuilder message = new StringBuilder();
		message.append("Part name: ");
		message.append(part.getPartName() + "\n");
		message.append("Part ID: ");
		message.append(part.getId() + "\n");
		message.append("Part description: ");
		message.append(part.getDescription() + "\n");
		return new String(message);
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
	

	
	public static void main(String[] args){
        try {
            Naming.rebind("//localhost/PartRepository", new PartRepository(initializeList()));
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }
}
