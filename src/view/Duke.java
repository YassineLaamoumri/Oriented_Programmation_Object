package view;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit ;

public class Duke extends Castle {

	/**<b>Duke est une classe representant un chateau appartenant à un Duc.</b>
	 * <p>Duke est une classe fille de Castle carcun chateau appartenant à un duc est un chateaau neutre + les méthodes qui lui permettent d'attaquer les autres ducs.
	 * Raison pour laquelle Duke est une classe fille de Castle @see #Castle.</p>
	 * 
	 * 
	 * Constructeur Duke :
	 * @param Name
	 * 		Le nom du chateau.
	 * @param treasure
	 * 		Le trésor initialisé à zéro au début.
	 * @param soldiers
	 * 		Le nombre de soldats.
	 * @param x
	 * 		L'abscisse.
	 * @param y
	 * 		L'ordonnée.
	 * Construit avec le constructeur de la classe mère Caste @see #Castle
	 */
	public Duke(String Name,int treasure, int soldiers,int x,int y) {
		super(Name,treasure,soldiers,x,y);

	}
	/**
	 * Constructeur Duke prenant en paramètre un éllémebt de type Duke;
	 * @param d
	 */
	public Duke(Duke d) {
		super(d);
	}
	
	/**
	 * Une méthode produisant un revenu de "income" à chaque tour.
	 */
	private void income() {
		treasure = treasure + income;		
	}
/**
 * Cette méthode crée un thread appelant la fonction contain_attack() de la classe Castle @see #Castle{@link #contain_attack(int, int, int, int, int)}.
 * Elle envoie un groupe de deux soldats toute les X temps au bout d'une durée t determinés en fonction de la distance entre les chateaux et du nombre de soldats envoyés.
 * 
 *<p>Grâce au coordonnées des chateaux stockées obtenues par les méthodes: @see {@link #getX()} et @see {@link #getY()}.</nl>
 * Elle calcule la distance eucludience separant les deux chateux en considerant une case comme unité.
 * Grâce aux nombres de cases separant les chateaux on determine le "debut de l'attaque", c'est-à-dire le temps que mettra le premier soldat pour à arriver.
 * Toutes ces donnnées sont données en paramètres à la fonction @see {@link #attack(int, int, int, int)} pour effectuer l'attaque.
 * @param x_gridpane
 * @param y_gridpane
 * @param id_attacker_duke
 * @param id_attacker_castle
 * @return
 */
	private static Runnable newAttackRunnable(int x_gridpane, int y_gridpane ,int id_attacker_duke,int id_attacker_castle) {
		return new AttackRunnable() {
			@Override
			public void attack() {
				int id =Settings.cells[x_gridpane][y_gridpane];
				int x_castle_to_attack ;
				int y_castle_to_attack; 

				if(id == 4) {
					x_castle_to_attack = GameViewManager.found_neutral_castle( x_gridpane,  y_gridpane);
					y_castle_to_attack =0;
				}else {
					x_castle_to_attack = id;
					y_castle_to_attack = GameViewManager.found_castle( x_gridpane,  y_gridpane ,id);
				}

				Castle castle_to_attack =  Settings.castles[x_castle_to_attack][y_castle_to_attack];
				castle_to_attack.contain_attack( id_attacker_duke, id_attacker_castle, 2,x_castle_to_attack,y_castle_to_attack);
			}
		};
	}	
	/**
	 *<p> Comme expliquer precedemment l'attaque s'arrêtera au bout de la durée : start_attack+time_attack*nb_attack avec:
	 *<ul>
	 *<li>start_attack est à égale au temps que metttront les deux premiers soldats pour arriver devant le chateau(car l'ost est constituer de deux rang de soldats aligné côte à côte).</li>
	 *<li>time_attack correspond au temps que mettra chaque soldat entrant dans le chateau.</li>
	 *<li>Et nb_attack qui correspond à la taille de l'ost c'est à dire que la moitié du nombre de soldats envoyés.</li>
	 * </ul>
	 * </p>
	 */
	
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1000);


	void attack(int nb_soldiers,int id_attacker_duke,int id_attacker_castle, int x_gridpane, int y_gridpane){
				 System.out.println("Nombre de soldiers totale"+nb_soldiers);
				 System.out.println(" nb soldiers "+nb_soldiers + " id_attacker_duke " + id_attacker_duke+ " id_attacker_castle "+id_attacker_castle +" x_gridpane "+x_gridpane + " y_gridpane "+y_gridpane );
		soldiers =soldiers- nb_soldiers;
		int nb_grid_per_round = 10;
		int x =  Settings.castles[id_attacker_duke][id_attacker_castle].getX();
		int y =  Settings.castles[id_attacker_duke][id_attacker_castle].getY();
		x = x-x_gridpane;
		y = y-y_gridpane;
		int d= (int) Math.sqrt((Math.pow(x, 2))+(Math.pow(y, 2)));

		Runnable attack = newAttackRunnable(x_gridpane, y_gridpane,id_attacker_duke,id_attacker_castle);
		int time_attack = 1000;
		int start_attack = d*Settings.TimeRound/(nb_grid_per_round);


		int nb_attack = nb_soldiers/2;
	    ScheduledFuture<?> attackHandle =
	    	       scheduler.scheduleAtFixedRate(attack, start_attack, time_attack ,TimeUnit.MILLISECONDS);
	     Runnable canceller = () -> attackHandle.cancel(false);
	     if(Settings.list_Dukes.size()==1) {
	    	 attackHandle.cancel(true); 
	     }
	     scheduler.schedule(canceller,  start_attack+time_attack*nb_attack, TimeUnit.MILLISECONDS);
	}

	

/**
 * Cette permet aux dukes adverses d'attaquer, en choisissant aléatoirement le nombre soldats avant d'appeler la méthode pour attaquer.
 * @param id_attacker_duke
 * L'indentifiant du Duc qui attaque.
 * @param id_attacker_castle
 *  L'indentifiant du chateau attaqué.
 *  Et les coordonnées du chateau attaqué
 * @param x_gridpad
 * @param y_gridpad
 */

		void attack(int id_attacker_duke,int id_attacker_castle,int x_gridpad, int y_gridpad){
			final int nb_soldiers  =1 + (int)(Math.random() * ((this.getSoldiers() - 1) ));
			
			attack(nb_soldiers,id_attacker_duke,id_attacker_castle,x_gridpad,  y_gridpad);
		}

			
}