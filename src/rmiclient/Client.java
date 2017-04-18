package rmiclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rmiinterface.Part;
import rmiinterface.RMIInterface;

public class Client {
	private static RMIInterface look_up;
	

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		
		String initiateServer = new String();
		boolean check = false;
		while(!check){
			//Inicializa uma tela requisitando o nome do primeiro servidor que quer acessar
			
			initiateServer = JOptionPane.showInputDialog("Digite nome do server com o qual deseja se conectar:");
			
			try {
				//Inicializa a conexão com o servidor
				look_up = (RMIInterface) Naming.lookup(new String("//localhost/"+initiateServer+"/"));
				check = true;
			}catch (NotBoundException e){
				JOptionPane.showMessageDialog(null, "Server requisitado não existe");
			}
		}
		//Cria um array com todos os servers ativos
		String[] names = Naming.list("//localhost/"+initiateServer+"/PartRepository");
		//Inicializa a segunda tela que é referente às funções diretas do servidor
		boolean findmore;
		do{
			//String[] options, contém todos os botões (opções) de funções disponíveis no servidor
			String[] options = {"Trocar Server", "Adicionar Part", "Mostrar Parts", "Recuperar uma Part", "Excluir Lista de Parts", "Exit"};
			int choice = JOptionPane.showOptionDialog(null, "Choose an action", "Option dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			
			List<Part> list = look_up.allParts();//Recupera a lista de Parts do servidor
			
			
			switch(choice){
				case 0:
					//Atualiza a lista de servidores ativos
					names = Naming.list("//localhost/"+initiateServer+"/PartRepository");
					changeServer(names);
					break;
				case 1:
					addPart(list);
					break;
				case 2:
					showParts(list);
					break;
				case 3:
					String idPart = JOptionPane.showInputDialog("Digite o ID da Part que deseja buscar.");
					enterPart(idPart);
					break;
				case 4:
					look_up.clearList();
					JOptionPane.showMessageDialog(null, "Lista de Parts do servidor deletada");
					break;
				default:
					System.exit(0);
					break;
			}
			findmore = (JOptionPane.showConfirmDialog(null, "Deseja fechar a aplicacao?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION);
		}while(findmore);
	}
	
	/**
	 * Método mostra a janela de ações de Parts, 
	 * tudo o que está ocorrendo nessa área envolve uma Part específica
	 * @param idPart
	 * @throws RemoteException
	 */
	private static void enterPart(String idPart) throws RemoteException {
		try{
			Part response = look_up.getp(new Part(idPart));
			boolean continueLoop;
			do{
				String[] optionst = {"Peça Primitiva?", "Mostrar Atributos", "Listar SubPartes", "Limpar lista de subpartes", "Exit"};
				int choicet = JOptionPane.showOptionDialog(null, "Nome: " + response.getPartName() + "\n" + "ID: " + response.getId(), "Option dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, optionst, optionst[0]);
				switch(choicet){
					case 0:	
						if (look_up.checkPrimitive(response))
							JOptionPane.showMessageDialog(null, "Part eh primitiva");
						else
							JOptionPane.showMessageDialog(null, "Part eh composta");
						break;								
					case 1:
						JOptionPane.showMessageDialog(null, look_up.showp(look_up.getp(new Part(idPart))));
						break;								
					case 2:
						JOptionPane.showMessageDialog(null, look_up.listSubparts(idPart));
						break;
					case 3:
						look_up.clearList();
						JOptionPane.showMessageDialog(null, "Lista de subparts limpa");
						break;	
					default:
						System.exit(0);
						break;
				}
				continueLoop = (JOptionPane.showConfirmDialog(null, "Deseja sair das funcoes de parts?", "Return", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION);
			}while(continueLoop);
			
		}catch(NoSuchElementException ex){
			JOptionPane.showMessageDialog(null, "Id nao existente");
		}
		
	}

	/**
	 * Método que mostra a janela com a quantidade de peças do servidor
	 * e lista todas as peças + ID
	 * @param list
	 */
	private static void showParts(List<Part> list) {
		StringBuilder message2 = new StringBuilder();
		message2.append("Quantidade de parts do server: "+ list.size()+"\n");
		list.forEach(x -> {message2.append(x.toString() + "\n");});
		JOptionPane.showMessageDialog(null, new String(message2));
		
	}
	/**
	 * Método que mostra a janela responsável pela adiçao de novas parts
	 * @param list
	 * @throws RemoteException
	 */
	private static void addPart(List<Part> list) throws RemoteException {
		StringBuilder message = new StringBuilder();
		JTextField nameField = new JTextField(5);
	    JTextField descriptionField = new JTextField(15);
	    JTextField subpartsField = new JTextField(20);
	    
	    message.append("<html>");
		list.forEach(x -> {message.append(x.toString() + "<br>");});
		message.append("</html>");
		JLabel partList = new JLabel(new String(message));
		
	    JPanel myPanel = new JPanel();
	    myPanel.setLayout(new BoxLayout(myPanel,BoxLayout.Y_AXIS));
	    myPanel.add(partList);
	    myPanel.add(new JLabel("Nome:"));
	    myPanel.add(nameField);
	    myPanel.add(new JLabel("Descricao:"));
	    myPanel.add(descriptionField); 
	    myPanel.add(new JLabel("Subparts (Formato: 'id,quantidade;id,quantidade' Deixar em branco caso a part seja primitiva:"));
	    myPanel.add(subpartsField);

	    int result = JOptionPane.showConfirmDialog(null, myPanel, 
	             "Adicionar Parte", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	    	if (subpartsField.getText().equals("")) {
	    		look_up.addprimitive(nameField.getText(), descriptionField.getText());
	    	}
	    	else{
		    	String[] subparts = subpartsField.getText().split(";");
		    	look_up.addp(nameField.getText(), descriptionField.getText(), subparts);
	    	}
	    }
		
	}

	/**
	 * Método que mostra a janela responsável pela troca de server
	 * @param names
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private static void changeServer(String[] names) throws MalformedURLException, RemoteException, NotBoundException {
		StringBuilder messageS = new StringBuilder();
		//Passa a lista de servidores ativos para ArrayList
		List<String> listServer = Arrays.asList(names);
		messageS.append("<html>");
		listServer.forEach(x -> {messageS.append(x.toString() + "<br>");});
		messageS.append("<br>Digite nome do server com o qual deseja se conectar:</html>");
		JLabel serverList = new JLabel(new String(messageS));
		JTextField serverField = new JTextField(5);
		
	    JPanel serverPanel = new JPanel();
	    
		
		boolean check = false;
		while(!check){
			serverPanel.add(serverList);
		    serverPanel.add(serverField);
		    serverPanel.setLayout(new BoxLayout(serverPanel,BoxLayout.Y_AXIS));

			String changeServer = serverField.getText();
			
			int resultServer = JOptionPane.showConfirmDialog(null, serverPanel, 
		             "Digite nome do server com o qual deseja se conectar:", JOptionPane.OK_CANCEL_OPTION);
			if (resultServer == JOptionPane.OK_OPTION) {
				try {
					look_up = (RMIInterface) Naming.lookup(new String("//localhost/"+changeServer+"/"));
					check = true;
				}catch (NotBoundException e){
					JOptionPane.showMessageDialog(null, "Server requisitado não existe");
				}
			}else check = true;
		}
	    
	    	
		
	}
}
