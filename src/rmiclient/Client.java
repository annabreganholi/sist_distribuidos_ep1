package rmiclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rmiinterface.Part;
import rmiinterface.RMIInterface;

public class Client {
	private static RMIInterface look_up;
	private Part actualPart;
	

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
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("Descricao:"));
				    myPanel.add(descriptionField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer   
				    myPanel.add(new JLabel("Subparts (Formato: 'id,quantidade;id,quantidade' Entrar com 'null' caso não possua subpartes:"));
				    myPanel.add(subpartsField);

				    int result = JOptionPane.showConfirmDialog(null, myPanel, 
				             "Adicionar Parte", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	look_up.addp(nameField.getText(), descriptionField.getText(), 0);
				       System.out.println("x value: " + nameField.getText());
				       System.out.println("y value: " + descriptionField.getText());
				    }
					break;
				case 1:
					list.forEach(x -> {message.append(x.toString() + "\n");});
					JOptionPane.showMessageDialog(null, new String(message));
					break;
				case 2:
					String idPart = JOptionPane.showInputDialog("Type the isbn of the book you want to find.");
					try{
						Part response = look_up.findPart(new Part(idPart));
						//JOptionPane.showMessageDialog(null, "Title: " + response.getPartName() + "\n" + "Cost: $" + response.getCost(), response.getIsbn(), JOptionPane.INFORMATION_MESSAGE);
						boolean continueLoop;
						do{
							String[] optionst = {"Peça Primitiva?", "Mostrar Atributos", "Listar SubPartes", "Limpar lista de subpartes", "Exit"};
							int choicet = JOptionPane.showOptionDialog(null, "Title: " + response.getPartName() + "\n" + "ID: " + response.getId(), "Option dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, optionst, optionst[0]);
							switch(choicet){
								case 0:	
									break;								
								case 1:
									JOptionPane.showMessageDialog(null, look_up.showp(response));
									break;								
								case 2:
									break;
								case 3:
									look_up.clearList();
									JOptionPane.showMessageDialog(null, "Lista de subparts limpa");
									break;	
								default:
									System.exit(0);
									break;
							}
							continueLoop = (JOptionPane.showConfirmDialog(null, "Do you want to return?", "Return", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION);
						}while(continueLoop);
						
					}catch(NoSuchElementException ex){
						JOptionPane.showMessageDialog(null, "Not found");
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
			findmore = (JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION);
		}while(findmore);
	}
}
