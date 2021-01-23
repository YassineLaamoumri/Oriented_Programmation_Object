package view;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DKButton;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;





public class GameViewManager {




	public GameViewManager() {
		initializeStage();
		



	}
/**
 * <p>Implémentation d'une interface Runnable permettant d'exécuter dans un thread au bout d'un certain temps l'opération @see {@link #random_attack_dukes(int)}.
 * Qui consiste à faire jouer aléatoirement tous les ducs saufs le joueur principal.
 * Cette opération dure 5000ms ce qui correspond au temps d'attente du joueur lorsque ce n'est pas son tour.</p>
 * 
 * Grâce au TimerTask nous pouvons exécuter des tâches planifiées:
 * <ul>
 * 
 *<li>pour une seule exécution</li>
 *<li>pour plusieurs exécutions</li>
 *<li>pour des exécutions récurrentes</li>
 *<li>avec éventuellement un délai d'attente avant la première exécution</li>
 *</ul>
 *<p>Ainsi, on programmme ici l'attaque aléatoire pour qu'elle s'effectue pendant une durée de 5000 millisecondes et avec une période de 50000 milliseconde .
 *Ce qui laisse au joueur 45000 millisecondes pour exécuter ses actions.
 *
 * @return
 * Un Runnable appelé plus tard dans un thread;
 */
	private static TimerTask newRandom_Attack_Runnable() {
		return new Random_Attack_Runnable() {
			@Override
			public void random_attack() {
				 System.out.println("Round");
				 Settings.round_player = false;
				 for(int i=0; i<Settings.list_Dukes.size(); i++) {
					 final int id_attacker_duke = Settings.list_Dukes.get(i);
					 if(id_attacker_duke !=0) {
						 random_attack_dukes(id_attacker_duke);
					 }
				 }
				 Settings.nb_rounds++;
			    try {
			        Thread.sleep(5000);
			      } catch (final InterruptedException e) {
			        e.printStackTrace();
			      }
			}
		};
	}
	
	/**Cette fonction effectue les opérations faites par le joueur pendant 45000ms tous les 50000ms comme détaillé ci-dessus.
	 * 
	 * 
	 * @param column_player
	 * L'abscisse du chateau avec lequel le joueur veut attaquer sur le gridPane1
	 * @param row_player
	 * L'ordonnée du chateau avec lequel le joueur veut attaquer sur le gridPane1
	 * @param column_castle
	 * L'abscisse du chateau attaqué sur le gridPane1
	 * @param row_castle
	 * L'ordonnée du chateau attaqué sur le gridPane1
	 * @param cells
	 * L'indice du joueur occupant le chateau attaqué 
	 * Initialisé ici @see {@link Settings#cells}
	 * Elle a une valeur entre 0 et 4 inclus si c'est un duc qui l'occupe et -1 sinon.
	 * @return
		Un Runnable  est lancé dans un thread.
	 */
	@SuppressWarnings("unused")
	private static TimerTask newAttackPlayerRunnable(final int column_player,final int row_player,final int column_castle,final int row_castle,final int cells ) {
		return new AttackPlayerRunnable() {
			@Override
			public void attack_player() {
				Settings.round_player = true;

				//listener
				
				
				to_attack_player(column_player, row_player, column_castle, row_castle,cells )	;
				try {
					Thread.sleep(45000);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}

		};
	}
	
	
	/**
	 * Création de la scène du jeu
	 * 
	 * La fonction @see {@link #gameOver()} est appelée dans le gameLoop pour vérifier si le jeu est terminé.
	 * On identifie trois cas possibles:
	 * <ul>
	 * 	<li> Le dernier chateau du joueur a été conquis</li>
	 * 	<li>Il est le seul à posseder tous les chateaux appartenant aux ducs</li>
	 * 	<li>Le nombre de tours est terminé et il reste encore un duc possedant au moins un chateau</li>
	 * </ul>
	 * 
	 * Dans les deux premiers cas, notre joueur gagne le jeu et dans le dernier il perd.
	 * GameOver() @see {@link #gameOver()} met fin au jeu.
	 * 
	 *
	 * @param menuStage
	 */

	public void createNewGame(final Stage menuStage) {
		Settings.menuStage = menuStage;
		Settings.menuStage.hide();
		createBackground();
		createCastle(Settings.gridPane1);
		Mouse_event_game(Settings.gridPane1);
		



		Settings.gameStage.show();

		
			jouer();
		
			Settings.gameLoop = new AnimationTimer() {
			@Override
			public void handle(final long now) {
				Settings.Treasure = Treasure();
				gameOver() ;


			}
		};
		Settings.gameLoop.start();


	}
	/**
	 * Cette fonction parcours tous les chateaux appartenant au joueur et renvoie son trésor total, qui est affiché ici @see {@link #update_general_info(int, int)}
	 * @return
	 * Trésor total.
	 */
	private int Treasure() {
		int Treasure=0;
		for(int i = 0 ; i< count(Settings.castles[0]) ;i++) {
			Settings.Treasure = Treasure + Settings.castles[0][i].getTreasure();
		}
		return Treasure;
	}

	/**
	 * Cette fonction demarre le Thread qui permet de metttre à jour le trésor et le nombre de tour.
	 * Elle demarre également le Thread qui permet au joueur d'interargir.
	 * Et le Thread qui lance les attaques aléatoires programmées @see {@link #to_attack_player(int, int, int, int, int)}
	 * Ces thread sont arrêtés au bout d'une durée  (nombres _de_tours * durée d'un tour). correspondant à la durée maximum du jeu.
	 */
	private void jouer() {

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
					final Runnable updater = new Runnable() {
						@Override
						public void run() {

							 clear_general_info();
							update_general_info(Settings.nb_rounds,Settings.Treasure);
						}
					};
					while (true) {
						try {
							Thread.sleep(2000);
						} catch (final InterruptedException ex) {
							 Thread.currentThread().interrupt();
						}
						Platform.runLater(updater);
					}
			}

		});
        thread.setDaemon(true);
        thread.start();


		final Runnable attaque = newRandom_Attack_Runnable();
		final int duree_jeu = 50*Settings.nb_rounds_max;


	    final ScheduledFuture<?> attaqueAleatoireHandle =
	    		Settings.scheduler.scheduleAtFixedRate(attaque, 0, 50 ,TimeUnit.SECONDS);
	     final Runnable canceller = () -> attaqueAleatoireHandle.cancel(false);
	     Settings.scheduler.schedule(canceller, duree_jeu, TimeUnit.SECONDS);




	}
	/**
	 * Cette fonction permet de vérifier si le jeu est terminé
	 * <ul>
	 * <li>On verifie d'abord si le nombre de tours est terminé</li>
	 * <li>Ensuite si le joueur est le seul duc qui possède encore un chateau</li>
	 * <li>Et en fin si le joueur a perdu tous ses chateaux</li>
	 * </ul>
	 */

	private static void gameOver() {
		if( (Settings.nb_rounds == Settings.nb_rounds_max ) || (Settings.list_Dukes.size() == 1) || !Settings.list_Dukes.contains(0)){
			System.out.println("JEU TERMINEEEE");
			Settings.gameLoop.stop();
		}

	}
	
	/**
	 * Cette fonction permet d'initialiser le Stage du jeu
	 * 
	 */

	private void initializeStage() {
		Settings.gamePane = new AnchorPane();
		Settings.gameScene = new Scene(Settings.gamePane,Settings.GAME_WIDTH,Settings.GAME_HEIGHT);
		Settings.gameStage = new Stage();


		Settings.gameStage.setScene(Settings.gameScene);

	}
	/**
	 * Cette fonction permet de créer le background du jeu
	 * Pour cela, on sépare notre scène en deux parties avec des images différents comme background:
	 * <ul>
	 * <li>une répresentant la map où les chateaux seront placés</li>
	 * <li>une autre représentant la barre d'info avec  @see {@link #createInfoBar(final GridPane gridPane1)}</li>
	 * </ul>
	 */

	private void createBackground() {
		Settings.gridPane1 = new GridPane();


		for (int i=0 ; i < Settings.COL_GRIDPANE_GAME ; i++) {
			for( int j=0 ; j < Settings.ROW_GRIDPANE_GAME; j++) {
				//				cells [i][j] = "empty";

				final ImageView backgroundImage1 = new ImageView(Settings.BACKGROUND_IMAGE);
				//				Label empty = new Label("empty");

				GridPane.setConstraints(backgroundImage1, i,  j);

				Settings.gridPane1.getChildren().add(backgroundImage1);

			}

		}

		initialize_cells();
		Settings.gridPane1.setGridLinesVisible(false);
		createInfoBar(Settings.gridPane1);


		Settings.gamePane.getChildren().addAll(Settings.gridPane1);
	}
/**
 * Cette fonctions affiche les informations sur les chateaux quand le joueur clique dessus :
 * Qaund il clique sur son propre chateau, les informations suivantes sont affichées:
 * <ul>
 * <li>Le nom du chateau</li>
 * <li>Le nombre des soldats</li>
 * <li>Le trésor</li>
 * <li>Le nombre de soldats en production</li>
 * <li>Le nombre de tours avant que la production se termine</li>
 * </ul>
 *  Qaund il clique sur un chateau adverse:
 *  <ul>
 * <li>Le nom du chateau</li>
 * <li>Le nombre de soldats</li>
 * <li>Le trésor</li>
 * </ul>
 * Le nombre de tours avant la fin du jeu et le tresor total sont mis à jour en temps réel. 
 * @see #createCastle(GridPane)
 * @see #update_general_info(int, int)
 * @param gridPane1
 * GridPane utilisé
 */

	private static void createInfoBar(final GridPane gridPane1) {


		for (int i=Settings.COL_GRIDPANE_GAME ; i < Settings.GAME_WIDTH / Settings.TILE_SIZE; i++) {
			for( int j=0 ; j < Settings.GAME_HEIGHT / Settings.TILE_SIZE; j++) {



				final ImageView InforBarImage = new ImageView(Settings.INFOBAR_IMAGE);
				GridPane.setConstraints(InforBarImage,i,j);
				Settings.gridPane1.getChildren().addAll(InforBarImage);


			}
		}


	



		final Label General_Info = new Label("General Info");

		GridPane.setConstraints(General_Info, Settings.COL_GRIDPANE_GAME, 0,6,1);

		Settings.gridPane1.getChildren().add(General_Info);
		General_Info.setTextAlignment(TextAlignment.CENTER);



		final Label rounds = new Label("Nb of rounds :");
		GridPane.setConstraints(rounds, Settings.COL_GRIDPANE_GAME, 2,3,1);
		Settings.gridPane1.getChildren().add(rounds);



		final Label text = new Label("My treasure :");
		GridPane.setConstraints(text, Settings.COL_GRIDPANE_GAME, 4,4,1);
		Settings.gridPane1.getChildren().add(text);

		for(int i=Settings.COL_GRIDPANE_GAME ; i < Settings.GAME_WIDTH / Settings.TILE_SIZE ; i++) {
			final ImageView sword = new ImageView(Settings.SWORD_IMAGE);
			GridPane.setConstraints(sword, i,6);
			Settings.gridPane1.getChildren().add(sword);

			final ImageView sword2 = new ImageView(Settings.SWORD_IMAGE);
			GridPane.setConstraints(sword2, i,13);
			Settings.gridPane1.getChildren().add(sword2);
		}


		final Label my_castle = new Label("My castle ");
		GridPane.setConstraints(my_castle, Settings.COL_GRIDPANE_GAME, 7,4,1);
		Settings.gridPane1.getChildren().add(my_castle);

		final Label my_name = new Label("Name : ");
		GridPane.setConstraints(my_name, Settings.COL_GRIDPANE_GAME, 8,4,1);
		Settings.gridPane1.getChildren().add(my_name);

		final Label Numbers_soldiers = new Label("Numbers of soldiers :");
		GridPane.setConstraints(Numbers_soldiers, Settings.COL_GRIDPANE_GAME, 9,4,1);
		Settings.gridPane1.getChildren().add(Numbers_soldiers);


		final Label treasure = new Label("Treasure :");
		GridPane.setConstraints(treasure, Settings.COL_GRIDPANE_GAME, 10,4,1);
		Settings.gridPane1.getChildren().add(treasure);

		final Label production = new Label("Nb soldiers in production :");
		GridPane.setConstraints(production, Settings.COL_GRIDPANE_GAME, 11,5,1);
		Settings.gridPane1.getChildren().add(production);

		final Label production_end = new Label("Nb_rounds to finish prod :");
		GridPane.setConstraints(production_end, Settings.COL_GRIDPANE_GAME, 12,5,1);
		Settings.gridPane1.getChildren().add(production_end);


		final Label enemy_castle = new Label("Enemy castle ");
		GridPane.setConstraints(enemy_castle, Settings.COL_GRIDPANE_GAME, 14,4,1);
		Settings.gridPane1.getChildren().add(enemy_castle);

		final Label name = new Label("Name : ");
		GridPane.setConstraints(name, Settings.COL_GRIDPANE_GAME, 16,4,1);
		Settings.gridPane1.getChildren().add(name);

		final Label Numbers_soldiers_enemy = new Label("Numbers of soldiers :");
		GridPane.setConstraints(Numbers_soldiers_enemy, Settings.COL_GRIDPANE_GAME, 17,4,1);
		Settings.gridPane1.getChildren().add(Numbers_soldiers_enemy);


		final Label treasure_enemy = new Label("Treasure :");
		GridPane.setConstraints(treasure_enemy, Settings.COL_GRIDPANE_GAME, 18,4,1);
		Settings.gridPane1.getChildren().add(treasure_enemy);



		final DKButton button_actions = new DKButton("ACTIONS",5);
		GridPane.setConstraints(button_actions, Settings.COL_GRIDPANE_GAME, 19,7,2);
		Settings.gridPane1.getChildren().add(button_actions);



	}
/**
 * Cette fonction prend en paramètre les coordonnées d'une cellule du gridPane1 et renvoie un booléen true s'il est occupé par un chateau et false sinon.
 * Elle est dans @see {@link #createCastle(GridPane)} et permet de ne pas superposer les chateaux les uns sur les autres.
 * @param row
 * L'abscisse
 * @param column
 * L'ordonnée
 * @return
 */
	private static boolean isCellOccupied( final int row, final int column)
	{
		if(Settings.cells[row][column] == -1) {
			return false;
		}
		return true;
		//		    return gridPane
		//		            .getChildren()
		//		            .stream()
		//		            .filter(Node::isManaged)
		//		            
		//		            .anyMatch(
		//		                    n -> Objects.equals(GridPane.getRowIndex(n), row)
		//		                            && Objects.equals(GridPane.getColumnIndex(n),
		//		                                    column));

	}
/**
 * Cette fonction lit les interactions de l'utlisateur avec la souris et met l'application à jour;
 * @param gridPane
 */
	public void Mouse_event_game(final GridPane gridPane) {
		for(final Node node : gridPane.getChildren()){

			System.out.println( node.getLayoutX()  +" "+ node.getLayoutY() );

			node.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					final int x = (int) node.getLayoutX() / Settings.TILE_SIZE;
					final int y = (int) node.getLayoutY() / Settings.TILE_SIZE;
					if(isCellOccupied(x ,y)) {
						if(Settings.cells[x][y] == 0){
							clear_my_castle_info();
							update_my_castle(found_castle(x,y,0));
							System.out.println("Le Boss");
						}
						else {
							if(Settings.cells[x][y] == 4) {
								clear_enemy_castle_info();
								update_enemy_castle(found_neutral_castle(x, y),0);
								System.out.println("Neutre");
							}
							else {
								clear_enemy_castle_info();
								update_enemy_castle(Settings.cells[x][y],found_castle(x, y,Settings.cells[x][y]));
								System.out.println("Duke");
							}
						}

					}
					if(node instanceof DKButton) {
						System.out.println("ATTACK");
						create_popup();


					}



				}
			});

			System.out.println((int) node.getLayoutX() / Settings.TILE_SIZE +" "+ (int) node.getLayoutY() / Settings.TILE_SIZE);
			node.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					System.out.println((int) node.getLayoutX() / Settings.TILE_SIZE +" "+ (int) node.getLayoutY() / Settings.TILE_SIZE);
					node.setEffect(new DropShadow());


				}
			});
			node.setOnMouseExited(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					node.setEffect(null);


				}
			});



		}
	}

/**
 * Cette fonction met à jour le nombre de tours et le trésor total.
 * Elle est  appelé dans un thread ici @see {@link #jouer()}
 * @param nb_rounds
 * Le nombre de tours effectués
 * @param Treasure
 * Le tresor total
 */

	private static void update_general_info(final int nb_rounds,final int Treasure) {
		final String nb_Rounds  = String.valueOf(nb_rounds);

		final Label rounds = new Label(nb_Rounds);
		GridPane.setConstraints(rounds, Settings.COL_GRIDPANE_GAME +5, 2,1,1);
		Settings.gridPane1.getChildren().add(rounds);


		final String Total_Treasure  = String.valueOf(Treasure);

		final Label treasure= new Label(Total_Treasure);
		GridPane.setConstraints(treasure, Settings.COL_GRIDPANE_GAME +4, 4,1,1);
		Settings.gridPane1.getChildren().add(treasure);
	}	
	/**
	 * Cette fonction prend en paramètre l'indice du chateau  sur lequel le joueur à cliqué et met les infos à jour ici @see {@link #createInfoBar(GridPane)}
	 * @param castle_y
	 * correspond à l'indice y du chateau du duc principal 
	 */

	private void update_my_castle(final int castle_y) {
		final String nb_soldiers  = String.valueOf(Settings.castles[0][castle_y].soldiers);

		final Label soldiers = new Label(nb_soldiers);
		GridPane.setConstraints(soldiers, Settings.COL_GRIDPANE_GAME +4, 9,2,1);
		Settings.gridPane1.getChildren().add(soldiers);

		final Label my_name = new Label(Settings.castles[0][castle_y].Name);
		GridPane.setConstraints(my_name, Settings.COL_GRIDPANE_GAME+3, 8,4,1);
		Settings.gridPane1.getChildren().add(my_name);

		final String nb_treasure  = String.valueOf(Settings.castles[0][castle_y].treasure);
		final Label treasure = new Label(nb_treasure);
		GridPane.setConstraints(treasure, Settings.COL_GRIDPANE_GAME +2, 10,4,1);
		Settings.gridPane1.getChildren().add(treasure);



	}

/**
 * Cette fonction prend en paramètre les coordonnées du chateau adverse  sur le quel, le jouer a cliqué et met à jour les informations concernant le chateau choisi.
 * 
 * @param castle_x
 * L'abscisse chateau cliqué.
 * @param castle_y
 * Ordonnée chateau cliqué
 */
	private void update_enemy_castle(final int castle_x,final int castle_y) {
		final String nb_soldiers  = String.valueOf(Settings.castles[castle_x][castle_y].soldiers);

		final Label soldiers = new Label(nb_soldiers);
		GridPane.setConstraints(soldiers, Settings.COL_GRIDPANE_GAME +4, 17,2,1);
		Settings.gridPane1.getChildren().add(soldiers);

		final Label enemy_name = new Label(Settings.castles[castle_x][castle_y].Name);
		GridPane.setConstraints(enemy_name, Settings.COL_GRIDPANE_GAME+3, 16,4,1);
		Settings.gridPane1.getChildren().add(enemy_name);
		final String nb_treasure  = String.valueOf(Settings.castles[castle_x][castle_y].treasure);
		final Label treasure = new Label(nb_treasure);
		GridPane.setConstraints(treasure, Settings.COL_GRIDPANE_GAME +2, 18,4,1);
		Settings.gridPane1.getChildren().add(treasure);



	}
	
	/**
	 * Cette fonction efface les informations générales affichées ici @see {@link #update_general_info(int, int)} pour faciliter la mise à jour et éviter la superposition des labels.
	 */
	private void clear_general_info() {
		removeNodeByRowColumnIndex_Label(4,Settings.COL_GRIDPANE_GAME +4,Settings.gridPane1);
		removeNodeByRowColumnIndex_Label(2,Settings.COL_GRIDPANE_GAME +5,Settings.gridPane1);
	}
	
	/**
	 *Cette fonction efface les informations affichées sur le chateau adversaire pour que la mise se fasse à chaque clique.
	 */
	private void clear_enemy_castle_info() {
		removeNodeByRowColumnIndex_Label(16,Settings.COL_GRIDPANE_GAME +3,Settings.gridPane1);
		removeNodeByRowColumnIndex_Label(17,Settings.COL_GRIDPANE_GAME +4,Settings.gridPane1);
		removeNodeByRowColumnIndex_Label(18,Settings.COL_GRIDPANE_GAME +2,Settings.gridPane1);

	}

	/**
	 *Cette fonction efface les informations affichées sur le chateau appartenant au joueur pour qu'elles puissent se mettre à jour à chaque clique.
	 */
	
	
	private void clear_my_castle_info() {
		removeNodeByRowColumnIndex_Label(9,Settings.COL_GRIDPANE_GAME +4,Settings.gridPane1);
		removeNodeByRowColumnIndex_Label(8,Settings.COL_GRIDPANE_GAME +3,Settings.gridPane1);
		removeNodeByRowColumnIndex_Label(10,Settings.COL_GRIDPANE_GAME +2,Settings.gridPane1);





	}

/**
 * Fonction initialisant les  chateaux neutres au debut avec les valeurs aléatoires
 *Et les chateaux appartenant aux ducs avec des valeurs précises.
 *Les valeurs de cells sont initialisées ici @see #initialize_cells() et mis à jour pour connaitre si les cases sont occupées ou pas grâce à la fonction 
 *@see {@link #isCellOccupied( final int row, final int column)}.
 *
 * @param gridPane
 */



	private void createCastle(final GridPane gridPane) {



		final ImageView castle1 = new ImageView(Settings.CASTLE1_IMAGE);
		final ImageView castle2 = new ImageView(Settings.CASTLE2_IMAGE);
		final ImageView castle3 = new ImageView(Settings.CASTLE3_IMAGE);
		final ImageView castle4 = new ImageView(Settings.CASTLE4_IMAGE);

		Settings.castle[0]=  castle1;
		Settings.castle[1] = castle2;
		Settings.castle[2] = castle3;
		Settings.castle[3] = castle4;
		final String  name_castle[] = {"Winterfell","Highgarden","Dragonstone","Harrenhal","CastleBlack","RedKeep"};

		for(int i = 0; i < Settings.nb_neutral_castle ; i++) {
			final ImageView castle5 = new ImageView(Settings.CASTLE_NEUTRAL_IMAGE);
			Settings.castle[4+i] = castle5;
		}


		for(int i=0 ; i < Settings.castle.length ; i++) {
			Settings.castle[i].setRotate(random_angle());
		}



		for(int i = 0; i <Settings.nb_dukes ; i++)
		{
			final int treasure  =0;
			final int soldiers = 50;

			int x;
			int y;
			do {
				x  = new Random().nextInt(5) * 4+1;
				y  = new Random().nextInt(5) * 4+1;
			}while(isCellOccupied( x, y) == true) ;


			final Duke d = new Duke(name_castle[i],treasure, soldiers,x,y);
			Settings.castles[i][0] = d;
//			castles[i][0].Name = name_castle[i];
			Settings.list_castles.add(i);
			Settings.list_Dukes.add(i);
			//			System.out.println(x+" "+y);
			GridPane.setConstraints(Settings.castle[i],x,y);

			Settings.cells[x][y] = i;

			gridPane.getChildren().add(Settings.castle[i]);
		}




		for(int i = 0; i < Settings.nb_neutral_castle ; i++)
		{
			final int treasure  =Settings.treasure_Min + (int)(Math.random() * ((Settings.treasure_Max - Settings.treasure_Min) + 1));
			final int soldiers = Settings.soldier_min + (int)(Math.random() * ((Settings.soldier_max - Settings.soldier_min) + 1));

			int x;
			int y;
			do {
				x  = new Random().nextInt(5) * 4+1;
				y  = new Random().nextInt(5) * 4+1;
			}while(isCellOccupied( x, y) == true) ;

			final Castle c = new Castle(name_castle[i+Settings.nb_dukes],treasure, soldiers,x,y);
			Settings.castles[i+Settings.nb_dukes][0] = c;	
			Settings.castles[i+Settings.nb_dukes][0].setName(name_castle[i+Settings.nb_dukes]); 
			Settings.list_castles.add(i+Settings.nb_dukes);
			Settings.cells[x][y] = 4;
			System.out.println(x+" "+y);
			GridPane.setConstraints(Settings.castle[4+i],x,y);

			gridPane.getChildren().add(Settings.castle[4+i]);


		}




	}
	/** 
	 * Choix aléatoire de l'orientation des portes des chateaux.
	 */
	private  int random_angle() {

		final int[] intArray = {0, 90, 180,270};

		final int max = intArray.length;
		final int random = (int)(Math.random() * max);
		final int angle = intArray[random];
		return angle;
	}   

	/**
	 * Cette fonction retire un node de type ImageView du gridPane
	 * @param row
	 * L'ordonnnnée du node à retirer
	 * @param column
	 * L'abscisse du node à retirer.
	 * @param gridPane
	 * Le gridpane sur lequel retirer la node.
	 */
	public void removeNodeByRowColumnIndex_Image(final int row,final int column,final GridPane gridPane) {

		final ObservableList<Node> childrens = gridPane.getChildren();
		for(final Node node : childrens) {
			if(node instanceof ImageView && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				final ImageView imageView= (ImageView) (node); // use what you want to remove
				gridPane.getChildren().remove(imageView);
				break;
			}
		} 
	}
	
	/**
	 * Cette fonction retire un bouton du gridPane
	 * @param row
	 * L'ordonnnnée du bouton à retirer
	 * @param column
	 * L'abscisse du bouton à retirer.
	 * @param gridPane
	 * Le gridpane sur lequel retirer la node.
*/
	public static void removeNodeByRowColumnIndex_DKButton(final int row,final int column,final GridPane gridPane) {

		final ObservableList<Node> childrens = gridPane.getChildren();
		for(final Node node : childrens) {
			if(node instanceof DKButton && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				final DKButton button= (DKButton) (node); // use what you want to remove
				gridPane.getChildren().remove(button);
				break;
			}
		} 
	}
	/**
	 * Cette fonction retire un node de type label de la gridPane
	 * @param row
	 * L'ordonnnnée du label à retirer
	 * @param column
	 * L'abscisse du label à retirer.
	 * @param gridPane
	 * Le gridpane sur duquel retirer le label
*/

	public void removeNodeByRowColumnIndex_Label(final int row,final int column,final GridPane gridPane) {

		final ObservableList<Node> childrens = gridPane.getChildren();
		for(final Node node : childrens) {
			if(node instanceof Label && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				//			if(node instanceof Label) {
				final Label label= (Label) (node); // use what you want to remove
				gridPane.getChildren().remove(label);
				break;
			}
		} 
	}
/**
 * Création du popup permettant au joueur de faire une action:
 * <ul>
 * <li>Attaquer un chateau adverse</li>
 * <li>Produire des soldats</li>
 * <li>Transférer des troupes d'un chateau à un autre</li>
 * </ul>
 */
	private static void create_popup() {


		Settings.popup_pane = new AnchorPane();
		Settings.popupScene = new Scene(Settings.popup_pane, Settings.POPUP_WIDTH, Settings.POPUP_HEIGHT);
		Settings.popup = new Stage();
		Settings.popup.setScene(Settings.popupScene);
		Settings.popup_gridpane = new GridPane();
		Settings.popup.setTitle("Choose an action ");
		Settings.popup.initModality(Modality.APPLICATION_MODAL);
		Settings.popup.initOwner(Settings.gameStage);




		for (int i=0 ; i < Settings.POPUP_WIDTH / Settings.TILE_SIZE; i++) {
			for( int j=0 ; j < Settings.POPUP_HEIGHT / Settings.TILE_SIZE; j++) {



				final ImageView InfoBarImage = new ImageView(Settings.INFOBAR_IMAGE);
				GridPane.setConstraints(InfoBarImage,i,j);
				Settings.popup_gridpane.getChildren().add(InfoBarImage);


			}
		}
		
		final DKButton Attack = new DKButton("ATTACK",20);

		GridPane.setConstraints(Attack, 1, 1,7,2);
		Settings.popup_gridpane.getChildren().add(Attack);

		final DKButton Production = new DKButton("Prod",20);

		GridPane.setConstraints(Production, 8, 1,7,2);
		Settings.popup_gridpane.getChildren().add(Production);

		final DKButton Recruit = new DKButton("Recruit",20);

		GridPane.setConstraints(Recruit, 15, 1,7,2);
		Settings.popup_gridpane.getChildren().add(Recruit);

		Settings.popup_pane.getChildren().addAll(Settings.popup_gridpane);
		Settings.popup.show();
		
		for(final Node node : Settings.popup_gridpane.getChildren()){



			node.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					final int x = (int) node.getLayoutX() / Settings.TILE_SIZE;
					final int y = (int) node.getLayoutY() / Settings.TILE_SIZE;
					System.out.println( x  +" "+ y );
					
						if(node instanceof DKButton && x == 1 && y==1) {
							clear_button_actions_popup();
							actions_attack_popup();
							
							System.out.println("ATTACK");



						}
						if(node instanceof DKButton && x == 8 && y==1) {
							clear_button_actions_popup();
							actions_prod_popup();
							System.out.println("PROD");




						}
						if(node instanceof DKButton && x == 15 && y==1) {
							clear_button_actions_popup();
							actions_recruit_popup();
							System.out.println("RECRUIT");




						}
					



				}
			});


			node.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					System.out.println((int) node.getLayoutX() / Settings.TILE_SIZE +" "+ (int) node.getLayoutY() / Settings.TILE_SIZE);
					node.setEffect(new DropShadow());


				}
			});
			node.setOnMouseExited(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					node.setEffect(null);


				}
			});



		}

	}
	
	/**
	 * Cette fonction est appelé après avoir cliqué sur le bouton Recruit du popup.
	 * Elle nous affiche les paramétres  à choisir après comme le nombre de soldats à transferer et le chateau cible;
	 * 
	 */
	private static void actions_recruit_popup() {

		final Label Nb_soldiers  = new Label("Nb soldiers :");

		GridPane.setConstraints(Nb_soldiers, 1, 1,3,1);
		Settings.popup_gridpane.getChildren().add(Nb_soldiers);
		final Spinner<Integer> soldiers = new Spinner<>(1, 10000, 1);

		GridPane.setConstraints(soldiers, 4, 1,5,1);
		Settings.popup_gridpane.getChildren().add(soldiers);





		final Label My_castle_1  = new Label("My castle :");

		GridPane.setConstraints(My_castle_1, 10, 1,2,1);
		Settings.popup_gridpane.getChildren().add(My_castle_1);

		final ChoiceBox<String> My_castle_choice_1 = new ChoiceBox<String>();
		for(int i=0 ; i < count(Settings.castles[0]) ; i++) {
			My_castle_choice_1.getItems().add(Settings.castles[0][i].Name);

		GridPane.setConstraints(My_castle_choice_1, 12, 1,3,1);
		Settings.popup_gridpane.getChildren().add(My_castle_choice_1);

		final Label My_castle_2  = new Label("My castle :");

		GridPane.setConstraints(My_castle_2, 16, 1,3,1);
		Settings.popup_gridpane.getChildren().add(My_castle_2);

		final ChoiceBox<String> My_castle_choice_2 = new ChoiceBox<String>();

		for(int j=0 ; j < count(Settings.castles[0]); j++) {
			My_castle_choice_2.getItems().add(Settings.castles[0][j].Name);

		}

		GridPane.setConstraints(My_castle_choice_2, 19, 1,3,1);
		Settings.popup_gridpane.getChildren().add(My_castle_choice_2);


		final DKButton Submit = new DKButton("SUBMIT",20);
		GridPane.setConstraints(Submit, 8, 3,7,2);
		Settings.popup_gridpane.getChildren().add(Submit);



		for(final Node node : Settings.popup_gridpane.getChildren()){



			node.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					final int x = (int) node.getLayoutX() / Settings.TILE_SIZE;
					final int y = (int) node.getLayoutY() / Settings.TILE_SIZE;
					System.out.println( x  +" "+ y );
					if(node instanceof DKButton && x == 8 && y==3) {
					
						System.out.println("SUBMIT");
						Settings.popup.close();


					}



				}
			});


			node.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					System.out.println((int) node.getLayoutX() / Settings.TILE_SIZE +" "+ (int) node.getLayoutY() / Settings.TILE_SIZE);
					node.setEffect(new DropShadow());



				}
			});
			node.setOnMouseExited(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					node.setEffect(null);


				}
			});



		}
		}

	}
	/**
	 * Cette fonction est appelé après avoir cliqué sur le bouton Prod du popup.
	 * Elle nous affiche les paramétres  à choisir  comme le nombre de soldats à produire et le chateau concerné;
	 * 
	 */

	private static void actions_prod_popup() {

		final Label Nb_treasure  = new Label("Nb treasure :");

		GridPane.setConstraints(Nb_treasure, 1, 1,3,1);
		Settings.popup_gridpane.getChildren().add(Nb_treasure);


		final Spinner<Integer> soldiers = new Spinner<>(1, 10000, 1);

		GridPane.setConstraints(soldiers, 4, 1,5,1);
		Settings.popup_gridpane.getChildren().add(soldiers);

		final Label My_castle  = new Label("My castle :");

		GridPane.setConstraints(My_castle, 10, 1,3,1);
		Settings.popup_gridpane.getChildren().add(My_castle);

		final ChoiceBox<String> My_castle_choice = new ChoiceBox<String>();
		for(int i=0 ; i < count(Settings.castles[0]) ; i++) {
			My_castle_choice.getItems().add(Settings.castles[0][i].Name);
		}

		GridPane.setConstraints(My_castle_choice, 13, 1,3,1);
		Settings.popup_gridpane.getChildren().add(My_castle_choice);







		final DKButton Submit = new DKButton("SUBMIT",20);
		GridPane.setConstraints(Submit, 8, 3,7,2);
		Settings.popup_gridpane.getChildren().add(Submit);



		for(final Node node : Settings.popup_gridpane.getChildren()){



			node.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					final int x = (int) node.getLayoutX() / Settings.TILE_SIZE;
					final int y = (int) node.getLayoutY() / Settings.TILE_SIZE;
					System.out.println( x  +" "+ y );
					if(node instanceof DKButton && x == 8 && y==3) {
						int soldiers_choice = soldiers.getValue();
						final Castle my_castle = get_castle(My_castle_choice.getValue());
						if(soldiers_choice* Settings.COST_PROD  > my_castle.getTreasure() ) {
							while(soldiers_choice* Settings.COST_PROD  > my_castle.getTreasure()) {
								soldiers_choice--;
							}
						}
						@SuppressWarnings("unused")
						final int my_castle_Y = my_castle.getY();


						
						Settings.popup.close();


					}



				}
			});


			node.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					System.out.println((int) node.getLayoutX() / Settings.TILE_SIZE +" "+ (int) node.getLayoutY() / Settings.TILE_SIZE);
					node.setEffect(new DropShadow());



				}
			});
			node.setOnMouseExited(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					node.setEffect(null);


				}
			});



		}


	}
	
	/**
	 * Cette fonction est appelé après avoir cliqué sur le bouton Attack du popup.
	 * Elle nous affiche les paramétres  à choisir :  
	 * <ul>
	 * <li> le nombre de soldats à envoyer </li>
	 * <li> le chateau  avec lequel on souhaite attaquer </li>
	 * <li> le chateau que l'on souhaite attaquer</li>
	 * </ul>
	 */
	
	private static void actions_attack_popup() {


		final Label Nb_soldiers  = new Label("Nb soldiers :");

		GridPane.setConstraints(Nb_soldiers, 1, 1,3,1);
		Settings.popup_gridpane.getChildren().add(Nb_soldiers);
		final Spinner<Integer> soldiers = new Spinner<>(1, 1000, 1);

		GridPane.setConstraints(soldiers, 4, 1,5,1);
		Settings.popup_gridpane.getChildren().add(soldiers);





		final Label My_castle  = new Label("My castle :");

		GridPane.setConstraints(My_castle, 10, 1,2,1);
		Settings.popup_gridpane.getChildren().add(My_castle);

		final ChoiceBox<String> My_castle_choice = new ChoiceBox<String>();
		for(int i=0 ; i < count(Settings.castles[0]) ; i++) {
			My_castle_choice.getItems().add(Settings.castles[0][i].Name);
		}

		GridPane.setConstraints(My_castle_choice, 12, 1,3,1);
		Settings.popup_gridpane.getChildren().add(My_castle_choice);

		final Label Enemy_castle  = new Label("Enemy castle :");

		GridPane.setConstraints(Enemy_castle, 16, 1,3,1);
		Settings.popup_gridpane.getChildren().add(Enemy_castle);

		final ChoiceBox<String> Enemy_castle_choice = new ChoiceBox<String>();
		for(int i=1 ; i < Settings.nb_dukes -1; i++) {
			for(int j=0 ; j < count(Settings.castles[i]) ; j++) {

				Enemy_castle_choice.getItems().add(Settings.castles[i][j].getName());

			}

		}
		GridPane.setConstraints(Enemy_castle_choice, 19, 1,4,1);
		Settings.popup_gridpane.getChildren().add(Enemy_castle_choice);


		final DKButton Submit = new DKButton("SUBMIT",20);
		GridPane.setConstraints(Submit, 8, 3,7,2);
		Settings.popup_gridpane.getChildren().add(Submit);



		for(final Node node : Settings.popup_gridpane.getChildren()){



			node.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					final int x = (int) node.getLayoutX() / Settings.TILE_SIZE;
					final int y = (int) node.getLayoutY() / Settings.TILE_SIZE;
					System.out.println( x  +" "+ y );
					if(node instanceof DKButton && x == 8 && y==3) {
						int soldiers_choice = soldiers.getValue();
						final Castle my_castle = get_castle(My_castle_choice.getValue());
						final Castle enemy_castle = get_castle(Enemy_castle_choice.getValue());
						
						if(soldiers_choice > my_castle.getSoldiers()) {
							soldiers_choice = my_castle.getSoldiers()-1;
						}
						final int my_castle_X = my_castle.getX();
						final int my_castle_Y = my_castle.getY();
						final int enemy_castle_X = enemy_castle.getX();
						final int enemy_castle_Y = enemy_castle.getY();
						
						to_attack_player(soldiers_choice,my_castle_X,my_castle_Y, enemy_castle_X, enemy_castle_Y);
						System.out.println("SUBMIT");
						Settings.popup.close();
//						move_soldiers(my_castle_X,my_castle_Y,enemy_castle_X,enemy_castle_Y);


					}



				}
			});


			node.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					System.out.println((int) node.getLayoutX() / Settings.TILE_SIZE +" "+ (int) node.getLayoutY() / Settings.TILE_SIZE);
					node.setEffect(new DropShadow());



				}
			});
			node.setOnMouseExited(new EventHandler<MouseEvent>() {

				@Override
				public void handle(final MouseEvent event) {
					node.setEffect(null);


				}
			});



		}


	}










	/**
	 * Supprime les buttons trois buttons d'actions du popup.
	 * Cette fonction est appelé après un cliquer sur une des actions possibles.
	 */


	private static void clear_button_actions_popup() {
		removeNodeByRowColumnIndex_DKButton(1,1,Settings.popup_gridpane);
		removeNodeByRowColumnIndex_DKButton(1,8,Settings.popup_gridpane);
		removeNodeByRowColumnIndex_DKButton(1,15,Settings.popup_gridpane);

	}

/**
 * Initialisatio du tableau cells pour savoir si une case est occupée en appelant @see {@link #isCellOccupied(int, int)}.
 */

	private void initialize_cells() {
		for( int i=0 ; i < Settings.COL_GRIDPANE; i++) {
			for(int j=0 ; j < Settings.ROW_GRIDPANE ; j++) {
				Settings.cells[i][j] = -1;
			}
		}
	}
	/**
	 * Retrouver un chateau appartenant à un duc  dans le tableau des chateaux
	 * @param x_castle
	 * Son abscisse
	 * @param y_castle
	 * Son ordonnée
	 * @param indice_Duc
	 * L'indice du duc proprietaire du chateau
	 * 
	 * @return l'indice du chateau si le chateau est trouvé et -1 sinon.
	 */

	static int found_castle(final int x_castle, final int y_castle, final int indice_Duc){
		try {
			for(int i=0; i< count(Settings.castles[indice_Duc]) ;i++) {
				if(!isEmpty(Settings.castles[indice_Duc])) {
					if(Settings.castles[indice_Duc][i].getX() == x_castle && Settings.castles[indice_Duc][i].getY() ==y_castle ) {
						return i;
					}
				}
			}
		}catch(final ArrayIndexOutOfBoundsException e){
			System.out.println("Element introuvable");
		}
		return -1;
	}
	/**
	 * Retrouver un chateau neutre   dans le tableau des chateaux
	 * @param x_castle
	 * Son abscisse
	 * @param y_castle
	 * Son ordonnée
	 * 
	 * @return l'indice du chateau si le chateau est trouvé et -1 sinon.
	 */

	static int found_neutral_castle(final int x_castle, final int y_castle){
		try {
			for(int i=Settings.nb_dukes; i< Settings.nb_dukes+Settings.nb_neutral_castle  ;i++) {
				if(!isEmpty(Settings.castles[i])) {
					if(Settings.castles[i][0].getX() == x_castle && Settings.castles[i][0].getY() ==y_castle ) {
						return i;
					}
				}
			}
		}catch(final ArrayIndexOutOfBoundsException e){
			System.out.println("Element introuvable");
		}
		return -1;

	}

	/**
	 * Vérifie si un chateau de chateaux est vide 
	 * @param tab
	 * tableau de chateaux
	 * @return
	 * true si le tableau est vide et faux sinon.
	 */
	static boolean isEmpty(final Castle[] tab){
		boolean empty = true;
		for (int i=0; i<tab.length; i++) {
			if (tab[i] != null) {
				empty = false;
				break;
			}
		}
		return empty;		
	}
/**
 * Retourne le nombre d'éléments d'un tableau de Castle.
 * @param tab
 * Tableau de Castle
 * @return
 Le nombre d'élements d'un tableau de type Castle.
 */
	static int count(final Castle[] tab){
		int c = 0;
		for (int i=0; i<tab.length; i++) {
			if (tab[i] != null) {
				c++;
			}
		}
		return c;		
	}
	/**
	 * Renvoie  aleatoirement un chateau parmis les chateaux du duc avec lequel il peut attaquer;
	 * Cette fonction est utile pour l'implémentation de l'attaque aléatoire des ducs possedant plusieurs chateaux.
	 * @param indice_Duc
	 * indice du duc
	 * @return
	 *l'indice du chateau s'il existe  et -1 sinon
	 */
	private static int choose_attacker_castle(final int indice_Duc){
		final ArrayList<Integer> liste_des_castles_avec_le_nombre_de_soldiers_non_nul = new ArrayList<>();
		for(int i=0; i<count(Settings.castles[indice_Duc]); i++) {
			if(Settings.castles[indice_Duc][i].getSoldiers() !=0) {
				liste_des_castles_avec_le_nombre_de_soldiers_non_nul.add(i);
			}	
		}
		if(liste_des_castles_avec_le_nombre_de_soldiers_non_nul.size() !=0) {
			final int i = new Random().nextInt(liste_des_castles_avec_le_nombre_de_soldiers_non_nul.size());
			return liste_des_castles_avec_le_nombre_de_soldiers_non_nul.get(i);
		}else {
			return -1;
		}
	}
/**
 * Prend l'indice d'un duc et renvoie aléatoirement parmis les autres chateaux lequel attaquer
 * Cette fonction nous permet d'impléménter l'attaque aléatoire des ducs adverses.
 * @param id_attacker_duke
 * @return l'indice d'unchateau si trouvé sinon -1
 */

	private static int choisir_x_castle_a_attack(final int id_attacker_duke) {
		int i;
		int indice_castle_a_attack;
		do {
			i = new Random().nextInt(Settings.list_castles.size());
			indice_castle_a_attack=Settings.list_castles.get(i);
		}while(indice_castle_a_attack==id_attacker_duke);
		return indice_castle_a_attack;

	}
	/**
	 * Si l'attaque aléatoire tombe sur un duc possedant plusieurs chateaux,cette fonction renvoie aléatoirement un chateaux à attaquer.
	 * @param x_castle_a_attack
	 * indice d'un chateau
	 * @return
	 * indice d'un chateau
	 */

	private static int choisir_y_castle_a_attack(final int x_castle_a_attack){
		final int y_castle_a_attack = new Random().nextInt(count(Settings.castles[x_castle_a_attack]));
		return y_castle_a_attack;
	}
/**
 * Cette fonction met à jour l'image d'un chateau lorsque un duc conquiert un chateau
 * Elle est appelé dans un thread Runnable dans une Platform.runLater pour ne pas accombrer le thread principale et faciliter la mise à jour du GUI.
 * @param id_attacker_duke
 * @param x_win_castle
 * @param column
 * @param row
 */
	private static void change_image_castle( final int id_attacker_duke,final int x_win_castle, final int column, final int row) {
		GridPane.setConstraints(Settings.castle[x_win_castle],column,row);
		Settings.gridPane1.getChildren().remove(Settings.castle[x_win_castle]);
		final String NEW_CASTLE_IMAGE = "view/resources/castle"+id_attacker_duke+".png";
		
		final ImageView castle5 = new ImageView(NEW_CASTLE_IMAGE);
		Settings.castle[x_win_castle] = castle5;

		GridPane.setConstraints(Settings.castle[x_win_castle],column,row);
		Settings.gridPane1.getChildren().add(Settings.castle[x_win_castle]);

	}
/**
 * Cette fonction est appelée quand un chateau a été conquis, elle met à jour les données et appelle la fonction @see {@link #change_image_castle(int, int, int, int)} 
 * qui change l'image du chateau conquis
 * 
 * @param id_attacker_duke
 * L'indice du duc qui vient de gagner le chateau
 * @param x_win_castle
 * L'abscisse du chateau
 * @param y_win_castle
 * L'ordonnée duchateau
 */
	static void successful_attack(final int id_attacker_duke,final int x_win_castle,final int y_win_castle ) {

		final Castle win_castle = Settings.castles[ x_win_castle][y_win_castle];
		final int indice_nouveau_castle = count(Settings.castles[id_attacker_duke]);

		if(Settings.castles[x_win_castle][y_win_castle].getClass().getSimpleName() == "Duke") {						
			Settings.castles[id_attacker_duke][indice_nouveau_castle] =Settings.castles[x_win_castle][y_win_castle];						
		}else {
			final Duke nouveau_castle = new Duke(win_castle.getName(),win_castle.getTreasure(), win_castle.getSoldiers(),win_castle.getX(),win_castle.getY());
			Settings.castles[id_attacker_duke][indice_nouveau_castle] =nouveau_castle;
		}
		Settings.castles[x_win_castle][y_win_castle] = null;

		if (isEmpty(Settings.castles[x_win_castle])) {
			int indice_vide;
			if(x_win_castle == 0) {
				gameOver();
				Settings.gameLoop.stop();

			}
			if(Settings.list_Dukes.contains(x_win_castle)) {
				indice_vide = Settings.list_Dukes.indexOf(x_win_castle);
				Settings.list_Dukes.remove(indice_vide);		
			}
			

			indice_vide = Settings.list_castles.indexOf(x_win_castle);
		}
		final int column = win_castle.getX();
		final int row = win_castle.getY();
		Settings.cells[column][row] = id_attacker_duke;
		System.out.println("indice du gagnant: "+id_attacker_duke);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				final Runnable updater = new Runnable() {
					@Override
					public void run() {
						change_image_castle( id_attacker_duke,x_win_castle,  column, row);
					}
				};
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					Platform.runLater(updater);
				}
			}

		});
		thread.setDaemon(true);
		thread.start();
		thread = null;
	}

/**
 * Elle implemente l'attaque aléatoire des ducs adverses 
 * Elle est appelé dans un thread ic @see #jouer().
 * @param id_attacker_duke
 */

	private static void  random_attack_dukes(final int id_attacker_duke){
		final int id_attacker_castle= choose_attacker_castle(id_attacker_duke);

		
		if(id_attacker_castle != -1){
			final int x_castle_a_attack = choisir_x_castle_a_attack(id_attacker_duke);
			final int y_castle_a_attack = choisir_y_castle_a_attack(x_castle_a_attack);


			final Duke attacker_castle = (Duke)Settings.castles[id_attacker_duke][id_attacker_castle];

			final int x_gridpane = Settings.castles[x_castle_a_attack][y_castle_a_attack].getX();
			final int y_gridpane = Settings.castles[x_castle_a_attack][y_castle_a_attack].getY();

			attacker_castle.attack(id_attacker_duke,id_attacker_castle,x_gridpane, y_gridpane);

		}
	}
	/**
	 * 
	 * @param nb_soldiers
	 * @param column_player
	 * @param row_player
	 * @param column_castle
	 * @param row_castle
	 */
	private static void to_attack_player(final int nb_soldiers,final int column_player,final int row_player,final int column_castle,final int row_castle){
		final int id_attacker_duke = 0;
		final int id_attacker_castle = found_castle(column_player,row_player,0);
		final Duke attacker_castle = (Duke)Settings.castles[id_attacker_duke][id_attacker_castle];
		attacker_castle.attack(nb_soldiers,id_attacker_duke,id_attacker_castle,column_castle, row_castle);
	}
	/**
	 * Cette fonction est le début de notre tentative de créer une ligne entre deux chateaux lors d'une attaque.
	 */
	
	@SuppressWarnings("unused")
	private static void move_soldiers(final int castle_A_X,final int castle_A_Y,final int castle_B_X, final int castle_B_Y) {
		final Line line = new Line(castle_A_X * Settings.TILE_SIZE,castle_A_Y*Settings.TILE_SIZE,castle_B_X*Settings.TILE_SIZE,castle_B_Y*Settings.TILE_SIZE);
		line.setStrokeWidth(2);
		
		GridPane.setConstraints(line, castle_A_X, castle_A_Y,15,15);
		Settings.gridPane1.getChildren().add(line);
		
	}
	/**
	 * 
	 * Elle renvoi un chateau ayant comme nom le @param Name
	 * @param Name
	 * nom d'un chateau 
	 */
	
	private static Castle get_castle(final String Name) {

		for(int i =0 ; i < Settings.nb_dukes+Settings.nb_neutral_castle; i++) {
			for(int j=0 ; j < count(Settings.castles[i]) ; j++) {
				
				if(Objects.equals(Settings.castles[i][j].Name, Name)) {
					return Settings.castles[i][j];
				}
			}


		}
		return null;
	}






}
