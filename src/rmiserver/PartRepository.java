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
	 * Método que sobrescreve a interface
	 * Esse método busca uma peça na lista do servidor.
	 * Ele realiza a busca pelo id da part que entra como parâmetro para ele
	 */
	public Part getp(Part part) throws RemoteException {
		Predicate<Part> predicate = x-> x.getId().equals(part.getId());
		return partRepository.stream().filter(predicate).findFirst().get();
		
	}
	
	@Override
	/**
	 * Método que sobrescreve a interface
	 * Este método retorna a lista de peças do servidor
	 */
	public List<Part> allParts() throws RemoteException {
		return this.partRepository;
	}
	
	@Override
	/**
	 * Método que sobrescreve a interface
	 * Esse método cria uma nova peça, atribuindo um nome, descricao, e inserindo as subpeças contidas
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
	 * Método que sobrescreve a interface
	 * Esse método cria uma nova peça, atribuindo um nome, descricao, e inserindo as subpeças contidas
	 */
	public Part addprimitive(String name, String description) throws RemoteException {
		Part newPart = new Part(name, description);
		this.partRepository.add(newPart);
		
		return newPart;
	}
	
	@Override
	/**
	 * Método que sobrescreve a interface
	 * Esse método limpa a lista de subpeças de uma peça em questão
	 */
	public void clearList(Part part) throws RemoteException{
		part.clearSubcomponents();
	}
	
	@Override
	/**
	 * Método que sobrescreve a interface
	 * Esse método limpa a lista de peças do SERVIDOR em questão
	 */
	public void clearList() throws RemoteException{
		this.partRepository.clear();
	}
	
	@Override
	/**
	 * Método que sobrescreve a interface
	 * Este método mostra os atributos de uma peça em específico
	 * Nome/ID/Descriçao
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
	 * Método que sobrescreve a interface
	 * Checa se uma peça possui subpeças, retornando true/false
	 */
	public boolean checkPrimitive(Part part)throws RemoteException{
		if (part.getSubparts().isEmpty())
			return true;
		else return false;
	}
	
	@Override
	/**
	 * Método que sobrescreve a interface
	 * Faz a listagem das subpeças de uma peça e suas quantidades, 
	 * Caso a peça seja primitiva, imprime "Peça eh primitiva"
	 */
	public String listSubparts(String partId) throws RemoteException{
		if (getp(new Part(partId)).getSubparts().isEmpty())
			return new String("A peca eh primitiva");
		StringBuilder message = new StringBuilder();
		
		getp(new Part(partId)).getSubparts().forEach((x,y) -> message.append("Part ID: " + x.getId() + "; Nome: "+x.getPartName() + "; Quantidade: "+ y + "\n"));
		
		return new String(message);
	}
	
	/**
	 * Método que inicializa 5 peças, sempre serão inicializadas quando um servidor é iniciado
	 * Porém não influenciam em nada o sistema, podem ser removidas normalmente, assim como as outras peças
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
	 * Método main
	 * Inicializa o Servidor. 
	 * Ao inicializar o programa, insere como parâmetros (na linha de comndo), o nome do servidor
	 * Caso nenhum nome de servidor seja inserido, o default é Server1
	 * Lembrar de alterar nome, ao gerar mais de um servidor, para não causar inconsistência
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
