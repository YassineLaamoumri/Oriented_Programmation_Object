package view;

public class Castle {

/**
 * <b>Castle est une classe representant un chateau .</b>
 * <p>
 * Un membre de la classe Castle  est caract�ris� par les informations suivantes :
 * <ul>
 * <li>Un "Name", nom unique attribu� d�finitivement.</li>
 * 
 * <li>Un "treasure",tr�sor suceptible d'�tre chang�.</li>
 * <li>Un "soldiers", le nombre des soldats.</li>
 * <li>Un "soldier_damage", les points de d�g�ts r��us cumul�s.</li>
 * <li>Les coordonn�es dans le gridePane:</li>
 * 		<ul>
 * 			<li>"x", l'ordonn�e</li>
 * 			<li>"y", l'ordonn�e</li>
 * 		</ul>
 * <li>Une constante "damage", representant les points de d�g�ts que peut administere un soldat.</li>
 * <li>Une constante "life_point", representant les points de vie d'un soldat" .</li>
 * <li>Une constante "Icome", le revenu � la fin de chaque tour.</li>
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
 *		 Son ordonn�e/
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
	
	
	/**Retourne l'ordonn�e du chateau
	 * 
	 * @return L'ordonn�e du chateau
	 */
	
	public int getY() {
		return y;
	}
	   /**
     * Met � jour l'abscisse du chateau.
     * 
     * @param level
     *            La nouvelle abscisse.
     * 
     */

	public void setX(int x) {
		this.x=x;
	}	
	
	   /**
     * Met � jour l'ordonn�e du chateau.
     * 
     * @param level
     *            La nouvelle ordonn�e.
     * 
     */
	public void setY(int y) {
		this.y=y;
	}


	/**Retourne les points de d�g�ts d'un soldat
	 * 
	 * @return Les points de d�g�ts d'un soldat
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
     * Met � jour du nombre de soldats
     * 
     * @param level
     *            Le nouveau nombre des soldats .
     * 
     */

	public void setSoldiers(int soldiers) {
		this.soldiers = soldiers;
	}
	/**Retourne les points d�g�ts cumul�s
	 * 
	 * @return Le  points d�g�ts cumul�s
	 */
	public int getSoldier_damage() {
		return soldier_damage;
	}
	   /**
     * Met � jour les  points de d�g�ts administr�s par soldat.
     * 
     * @param level
     *            Le nouveau points de d�g�ts
     * 
     */
	public void setSoldier_damage(int soldier_damage) {
		this.soldier_damage = soldier_damage;
	}

	   /**Cettte m�thode subit les attaques des chateaux adversaires
	    * 
	    * @param id_Duke_Attacker Identifiant du Duc adverse qui attaque, correspondant � son abscissse dans le tableau des chateaux.
	    * @see #GameViewManager#
	    * @param id_castle_attacker
	    * @param nb_soldiers
	    * @param x_castle_a_attaquer
	    * @param y_castle_a_attaquer
	    * 
	    * 
	    *Elle v�rifie si le duc qui attaque est le proprietaire du chateaux.
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

