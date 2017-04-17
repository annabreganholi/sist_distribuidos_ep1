package rmiserver;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import rmiinterface.Part;
import rmiinterface.RMIInterface;

public class Bookstore extends UnicastRemoteObject implements RMIInterface{
	private static final long serialVersionUID = 1L;
	private List<Part> bookList;

	protected Bookstore(List<Part> list) throws RemoteException {
		super();
		this.bookList = list;
	}
	
	//The client sends a Book object with the isbn information on it (note: it could be a string with the isbn too)
	//With this method the server searches in the List bookList for any book that has that isbn and returns the whole object
	@Override
	public Part findPart(Part part) throws RemoteException {
		Predicate<Part> predicate = x-> x.getIsbn().equals(part.getIsbn());
		return bookList.stream().filter(predicate).findFirst().get();
		
	}
	
	@Override
	public List<Part> allBooks() throws RemoteException {
		return bookList;
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
            Naming.rebind("//localhost/MyBookstore", new Bookstore(initializeList()));
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }
}
