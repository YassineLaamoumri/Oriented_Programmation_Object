package view;

public class Production {
String name_castle;	
private int nb_round_before_production;
private int nb_soldiers_prod;
public int getNb_soldiers_prod() {
	return nb_soldiers_prod;
}


public void setNb_soldiers_prod(int nb_soldiers_prod) {
	this.nb_soldiers_prod = nb_soldiers_prod;
}


/**
 * Classe production répresente une production en cours
 * Constructeur production
 * @param name_castle
 * Le nom du chateau qui produit.
 * @param nb_round_before_production
 * Le nombre de tour avant la fin de la production.
 * @param nb_soldiers_prod
 * Le nombre de soldats à produire.
 * 
 */

public Production(String name_castle, int  nb_round_before_production, int nb_soldiers_prod){
	this.name_castle = name_castle;
	this.nb_soldiers_prod=  nb_soldiers_prod;
	this.nb_round_before_production = nb_round_before_production;
}

/**
 * Constructeur Production
 * @param p
 */
public Production(Production p) {
	this(p.name_castle,p.nb_soldiers_prod,p.nb_round_before_production);
}

/**Retourne nom du chateau en production
 * 
 * @return Le nom du chateau en production
 */

public String getName_castle() {
	return name_castle;
}

/**
* Met à jour le nom du chateau en production.
* 
* @param le nom du du chateau
*            Le nouveau nombre du chateau
* 
*/


public void setName_castle(String name_castle) {
	this.name_castle = name_castle;
}


/**Retourne le nombre de tour avant la fin de la production
 * 
 * @return Le  nombre de tour avant la fin de la production
 */



private void forward_production() {
	nb_round_before_production--;
}



}
