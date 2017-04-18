package rmiinterface;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class Part implements Serializable{
	private static final long serialVersionUID = 1190476516911661470L;
	private String name;
	private String idPart;
	private String description;
	private HashMap<Part, Integer> subcomponents;
	
	/**
	 * Construtor Auxiliar
	 * Cria uma "peça", inserindo um ID, para facilitar na busca de peças por ID
	 * Utilizada apenhas na busca (getp)
	 * @param idPart
	 */
	public Part(String idPart){
		this.idPart = idPart;
	}
	
	/**
	 * Construtor
	 * Cria uma nova peça, com o nome e descrição desejados
	 * Faz chamada ao método que gera um ID 6(dígitos) aleatórios
	 * Inicializa a lista de subcomponentes da peça
	 * @param name
	 * @param description
	 */
	public Part(String name, String description){
		this.name = name;
		this.description = description;
		this.idPart = setId();
		this.subcomponents = new HashMap<Part, Integer>();
		
	}

	/**
	 * Gera um ID de 6 dígitos aleatóriamente 
	 * (não faz verificação de duplicidade)
	 * @return
	 */
	public String setId(){
		Random rand = new Random();
		int randomNum = rand.nextInt((999999 - 100000) + 1) + 100000;
		return Integer.toString(randomNum);
	}
	
	/**
	 * Retorna no nome da peça
	 * @return
	 */
	public String getPartName() {
		return this.name;
	}
	
	/**
	 * Retorna a descriçao da peca
	 * @return
	 */
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * Retorna o ID da peça
	 * @return
	 */
	public String getId(){
		return this.idPart;
	}

	/**
	 * Retorna uma string com o nome e o ID da peça
	 */
	public String toString(){
		return "> " + this.name + " (ID:" + this.idPart + ")";
	}
	
	/**
	 * Limpa a lista de subcomponentes
	 */
	public void clearSubcomponents(){
		this.subcomponents.clear();
	}
	
	/**
	 * Adiciona subparts da peça
	 * Insere no HashMap que contém a peça e a quantidade
	 * @param part
	 * @param n
	 */
	public void addSubparts(Part part, int n){
		this.subcomponents.put(part, n);
	}
	
	/**
	 * Retorna o Mapa de subparts
	 * @return
	 */
	public HashMap<Part, Integer> getSubparts(){
		return this.subcomponents;
	}
}
