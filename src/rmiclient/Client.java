package rmiclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
		look_up = (RMIInterface) Naming.lookup("//localhost/PartRepository");
		
		boolean findmore;
		do{
			String[] options = {"Adicionar Part", "Mostrar Parts", "Recuperar uma Part", "Excluir Lista de Parts", "Exit"};
			int choice = JOptionPane.showOptionDialog(null, "Choose an action", "Option dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			List<Part> list = look_up.allParts();
			StringBuilder message = new StringBuilder();
			
			switch(choice){
				case 0:
					
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
				    	String[] subparts = subpartsField.getText().split(";");
				    	look_up.addp(nameField.getText(), descriptionField.getText(), subparts);
				    	
				    }
					break;
				case 1:
					list.forEach(x -> {message.append(x.toString() + "\n");});
					JOptionPane.showMessageDialog(null, new String(message));
					break;
				case 2:
					String idPart = JOptionPane.showInputDialog("Digite o ID da Part que deseja buscar.");
					try{
						Part response = look_up.getp(new Part(idPart));
						boolean continueLoop;
						do{
							String[] optionst = {"Pe�a Primitiva?", "Mostrar Atributos", "Listar SubPartes", "Limpar lista de subpartes", "Exit"};
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
					break;
				case 3:
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
}
