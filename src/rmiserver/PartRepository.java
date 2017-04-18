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
	/**
	 * M�todo que sobrescreve a interface
	 * Esse m�todo busca uma pe�a na lista do servidor.
	 * Ele realiza a busca pelo id da part que entra como par�metro para ele
	 */
	public Part getp(Part part) throws RemoteException {
		Predicate<Part> predicate = x-> x.getId().equals(part.getId());
		return partRepository.stream().filter(predicate).findFirst().get();
		
	}
	
	@Override
	/**
	 * M�todo que sobrescreve a interface
	 * Este m�todo retorna a lista de pe�as do servidor
	 */
	public List<Part> allParts() throws RemoteException {
		return this.partRepository;
	}
	
	@Override
	/**
	 * M�todo que sobrescreve a interface
	 * Esse m�todo cria uma nova pe�a, atribuindo um nome, descricao, e inserindo as subpe�as contidas
	 */
	public Part addp(String name, String description, String[] subparts) throws RemoteException {
		Part newPart = new Part(name, description);
		this.partRepository.add(newPart);
		String[] splitted;
		for (int i=0; i<subparts.length; i++){
			splitted = subparts[i].split(",");
			newPart.addSubparts(getp(new Part(splitted[0])), Integer.parseInt(splitted[1]));
		}
		return newPart;
	}	
	
	@Override
	/**
	 * M�todo que sobrescreve a interface
	 * Esse m�todo cria uma nova pe�a, atribuindo um nome, descricao, e inserindo as subpe�as contidas
	 */
	public Part addprimitive(String name, String description) throws RemoteException {
		Part newPart = new Part(name, description);
		this.partRepository.add(newPart);
		
		return newPart;
	}
	
	@Override
	/**
	 * M�todo que sobrescreve a interface
	 * Esse m�todo limpa a lista de subpe�as de uma pe�a em quest�o
	 */
	public void clearList(Part part) throws RemoteException{
		part.clearSubcomponents();
	}
	
	@Override
	/**
	 * M�todo que sobrescreve a interface
	 * Esse m�todo limpa a lista de pe�as do SERVIDOR em quest�o
	 */
	public void clearList() throws RemoteException{
		this.partRepository.clear();
	}
	
	@Override
	/**
	 * M�todo que sobrescreve a interface
	 * Este m�todo mostra os atributos de uma pe�a em espec�fico
	 * Nome/ID/Descri�ao
	 */
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
	
	@Override
	/**
	 * M�todo que sobrescreve a interface
	 * Checa se uma pe�a possui subpe�as, retornando true/false
	 */
	public boolean checkPrimitive(Part part)throws RemoteException{
		if (part.getSubparts().isEmpty())
			return true;
		else return false;
	}
	
	@Override
	/**
	 * M�todo que sobrescreve a interface
	 * Faz a listagem das subpe�as de uma pe�a e suas quantidades, 
	 * Caso a pe�a seja primitiva, imprime "Pe�a eh primitiva"
	 */
	public String listSubparts(String partId) throws RemoteException{
		if (getp(new Part(partId)).getSubparts().isEmpty())
			return new String("A peca eh primitiva");
		StringBuilder message = new StringBuilder();
		
		getp(new Part(partId)).getSubparts().forEach((x,y) -> message.append("Part ID: " + x.getId() + "; Nome: "+x.getPartName() + "; Quantidade: "+ y + "\n"));
		
		return new String(message);
	}
	
	/**
	 * M�todo que inicializa 5 pe�as, sempre ser�o inicializadas quando um servidor � iniciado
	 * Por�m n�o influenciam em nada o sistema, podem ser removidas normalmente, assim como as outras pe�as
	 * @return
	 */
	private static List<Part> initializeList(){
		List<Part> list = new ArrayList<>();
		list.add(new Part("Part 1", "Part Primitiva 1"));
		list.add(new Part("Part 2", "Part Primitiva 2"));
		list.add(new Part("Part 3", "Part Primitiva 3"));
		list.add(new Part("Part 4", "Part Primitiva 4"));
		list.add(new Part("Part 5", "Part Primitiva 5"));
		return list;
	}
	

	/**
	 * M�todo main
	 * Inicializa o Servidor. 
	 * Ao inicializar o programa, insere como par�metros (na linha de comndo), o nome do servidor
	 * Caso nenhum nome de servidor seja inserido, o default � Server1
	 * Lembrar de alterar nome, ao gerar mais de um servidor, para n�o causar inconsist�ncia
	 * @param args
	 */
	public static void main(String[] args){
		if (args.length == 0){
			try {
	            Naming.rebind("//localhost/Server1/PartRepository", new PartRepository(initializeList()));
	            System.err.println("Server ready");
	        } catch (Exception e) {
	            System.err.println("Server exception: " + e.getMessage());
	        }
		}
		else{
		    try {
		        Naming.rebind(new String("//localhost/"+args[0]+"/"), new PartRepository(initializeList()));
		        System.err.println("Server "+args[0]+" ready");
		    } catch (Exception e) {
		        System.err.println("Server "+args[0]+" exception: " + e.getMessage());
	        }
		}
    }
}
