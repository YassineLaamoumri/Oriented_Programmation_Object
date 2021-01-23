package view;

public class Castle {

/**
 * <b>Castle est une classe representant un chateau .</b>
 * <p>
 * Un membre de la classe Castle  est caractérisé par les informations suivantes :
 * <ul>
 * <li>Un "Name", nom unique attribué définitivement.</li>
 * 
 * <li>Un "treasure",trésor suceptible d'être changé.</li>
 * <li>Un "soldiers", le nombre des soldats.</li>
 * <li>Un "soldier_damage", les points de dégâts réçus cumulés.</li>
 * <li>Les coordonnées dans le gridePane:</li>
 * 		<ul>
 * 			<li>"x", l'ordonnée</li>
 * 			<li>"y", l'ordonnée</li>
 * 		</ul>
 * <li>Une constante "damage", representant les points de dégâts que peut administere un soldat.</li>
 * <li>Une constante "life_point", representant les points de vie d'un soldat" .</li>
 * <li>Une constante "Icome", le revenu à la fin de chaque tour.</li>
 * </ul>
 * </p>
 * <p>

 * 

 */
	protected String Name;
	protected int treasure;
	protected int soldiers;
	protected int soldier_damage;
	protected int x;
	protected int y;
	final int damage = 5;
	final int life_points = 3;
	final int income = 10;

	
	
	/**Retourne le nom du chateau
	 * 
	 * @return Le nom du chateau
	 */
	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}
/**
 * Constructeur de Castle.
 * @param Name 
 * 		Le nom du chateau
 * @param treasure
 * 		Son tresor
 * @param soldiers
 * 		Le nombre de ses soldats
 * @param x
 *		 Son abscisse.
 * @param y
 *		 Son ordonnée/
 */

	public Castle(String Name,int treasure, int soldiers,int x, int y){
		this.x = x;
		this.y = y;
		this.treasure = treasure;
		this.soldiers = soldiers;
		this.soldier_damage = 0;
		this.Name=Name;
	}

	/**
	 * Constructeur Castle
	 * @param c
	 */
	public Castle(Castle c) {
		this(c.Name,c.treasure, c.soldiers,c.x,c.y);
	}

	
	/**Retourne le tresor du chateau
	 * 
	 * @return Le tresor du chateau
	 */

	public int getTreasure() {
		return treasure;
	}
	public void setTreasure(int treasure) {
		this.treasure = treasure;
	}
	
	
	/**Retourne l'abscisse du chateau
	 * 
	 * @return L'abscisse du chateau
	 */

	public int getX() {
		return x;
	}
	
	
	/**Retourne l'ordonnée du chateau
	 * 
	 * @return L'ordonnée du chateau
	 */
	
	public int getY() {
		return y;
	}
	   /**
     * Met à jour l'abscisse du chateau.
     * 
     * @param level
     *            La nouvelle abscisse.
     * 
     */

	public void setX(int x) {
		this.x=x;
	}	
	
	   /**
     * Met à jour l'ordonnée du chateau.
     * 
     * @param level
     *            La nouvelle ordonnée.
     * 
     */
	public void setY(int y) {
		this.y=y;
	}


	/**Retourne les points de dégâts d'un soldat
	 * 
	 * @return Les points de dégâts d'un soldat
	 */

	public int getDamage() {
		return damage;
	}

	

	/**Retourne les points de vie d'un soldat
	 * 
	 * @return Les points de vie d'un soldat
	 */

	public int getLife_points() {
		return life_points;
	}


	/**Retourne le revenu
	 * 
	 * @return Le revenu
	 */
	public int getIncome() {
		return income;
	}
	
	/**Retourne le nombre des soldats
	 * 
	 * @return Le nombre des soldats
	 */
	public int getSoldiers() {
		return this.soldiers;
	}
	   /**
     * Met à jour du nombre de soldats
     * 
     * @param level
     *            Le nouveau nombre des soldats .
     * 
     */

	public void setSoldiers(int soldiers) {
		this.soldiers = soldiers;
	}
	/**Retourne les points dégâts cumulés
	 * 
	 * @return Le  points dégâts cumulés
	 */
	public int getSoldier_damage() {
		return soldier_damage;
	}
	   /**
     * Met à jour les  points de dégâts administrés par soldat.
     * 
     * @param level
     *            Le nouveau points de dégâts
     * 
     */
	public void setSoldier_damage(int soldier_damage) {
		this.soldier_damage = soldier_damage;
	}

	   /**Cettte méthode subit les attaques des chateaux adversaires
	    * 
	    * @param id_Duke_Attacker Identifiant du Duc adverse qui attaque, correspondant à son abscissse dans le tableau des chateaux.
	    * @see #GameViewManager#
	    * @param id_castle_attacker
	    * @param nb_soldiers
	    * @param x_castle_a_attaquer
	    * @param y_castle_a_attaquer
	    * 
	    * 
	    *Elle vérifie si le duc qui attaque est le proprietaire du chateaux.
	    *Si oui ajouter les soldats qui attaquent aux soldats du chateau.
	    *Sinon l'attaque s'effectue et en cas de victoire du Duc attaquant on appelle la fonction :successful_attack().
	    *@see #GameViewManager#successful_attack(int ,int,int,int,int).
	    
	    */

	protected void contain_attack(int id_Duke_Attacker,int id_castle_attacker,int nb_soldiers,int x_castle_a_attaquer,int y_castle_a_attaquer) {

		 


		if( Settings.cells[x][y] != id_Duke_Attacker) {
			soldier_damage = soldier_damage + nb_soldiers*damage;
			int soldiers_restants = soldiers - soldier_damage/life_points;
			if(soldiers_restants <=0) {
				GameViewManager.successful_attack(id_Duke_Attacker, x_castle_a_attaquer,y_castle_a_attaquer );

			}

		}else {
			soldiers = soldiers+nb_soldiers;
		}
	}



/**
 * Fonction revenu augmente le trsor des chateaux neutres de "income" @see {@link #income()} 
 * Diviser par 10 car les chateux neutres ne produisent que 10% du revenu.
 */

	private void income() {
		treasure = treasure + income/10;	
	}


}

