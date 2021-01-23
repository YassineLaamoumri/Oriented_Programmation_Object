package view;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Settings {
	/**
	 * hauteur du popup
	 */
	static final int POPUP_HEIGHT = 150;
	/**
	 * largeur du popup
	 */
	static final int POPUP_WIDTH = 690;
	/**
	 * map du jeu
	 */
	static AnchorPane gamePane;
	/**
	 * Scene du jeu
	 */
	static Scene gameScene;
	/**
	 * popup
	 */
	static Stage popup;
	/**
	 * Scene  du popup
	 */
	static Scene popupScene;
	/**
	 * jeu
	 */
	static Stage gameStage;
	/**
	 * menu du jeu
	 */
	static Stage menuStage;

	/**
	 * gridpane du popup
	 */

	static GridPane popup_gridpane;
	/**
	 * map du popup
	 */
	static AnchorPane popup_pane;
	/**
	 * taille d'une cellule en hauteur et largeur  du gridPane , correspond exactement à la taille de l'image du background
	 */
	static final int TILE_SIZE = 30;
	/**
	 * Largeur gridPane du jeu
	 */
	static final int GRIDPANE_WIDTH = 630;
	/**
	 * Hauteur gridPane du jeu
	 */
	static final int GRIDPANE_HEIGHT = 630;
	/**
	 * Largeur fenêtre du jeu
	 */
	static final int GAME_WIDTH = 840;
	/**
	 * Hauteur fenêtre du jeu
	 */
	static final int GAME_HEIGHT = 630;

	/**
	 * Nb de colonnes  du gridPane de la map du jeu
	 */
	static final int COL_GRIDPANE_GAME = GRIDPANE_WIDTH / TILE_SIZE;
	/**
	 * Nb de lignes  du gridPane de la map jeu
	 */
	static final int ROW_GRIDPANE_GAME = GRIDPANE_HEIGHT / TILE_SIZE;
	/**
	 * Nb de colonnes  du gridPane de la barre d'info
	 */
	static final int COL_GRIDPANE_INFO_BAR = GAME_WIDTH / TILE_SIZE - COL_GRIDPANE_GAME;
	/**
	 * Nb de lignes  du gridPane de la barre d'info
	 */
	static final int ROW_GRIDPANE_INFO_BAR = GAME_HEIGHT / TILE_SIZE - ROW_GRIDPANE_GAME;
	/**
	 * Nb de lignes  du gridPane du jeu
	 */
	static final int ROW_GRIDPANE = ROW_GRIDPANE_INFO_BAR + ROW_GRIDPANE_GAME;
	/**
	 * Nb de colonnes  du gridPane du jeu
	 */
	static final int COL_GRIDPANE = COL_GRIDPANE_INFO_BAR + COL_GRIDPANE_GAME;
	/**
	 * gridPane du jeu
	 */
	static GridPane gridPane1;
	/**
	 * image du background
	 */
	final static String BACKGROUND_IMAGE = "view/resources/tile.png";
	/**
	 * image du chateau 0
	 */
	final static String CASTLE1_IMAGE = "view/resources/castle0.png";
	/**
	 * image du chateau 1
	 */
	final static String CASTLE2_IMAGE = "view/resources/castle1.png";
	/**
	 * image du chateau 2
	 */
	final static String CASTLE3_IMAGE = "view/resources/castle2.png";
	/**
	 * image du chateau 3
	 */
	final static String CASTLE4_IMAGE = "view/resources/castle3.png";
	/**
	 * image du chateau neutre
	 */
	final static String CASTLE_NEUTRAL_IMAGE = "view/resources/castle4.png";
	/**
	 * image du background de la barre d'info
	 */
	final static String INFOBAR_IMAGE = "view/resources/InfoBar.png";
	/**
	 * image des épées
	 */
	final static String SWORD_IMAGE = "view/resources/sword.png";
	/**
	 * tableaux couvrant chaque cellules du gridpane du jeu , prend en valeur l'indice d'un duke qui occupe le chateau aux coordonnées pris en paramétres
	 */
	
	final static int cells[][] = new int[COL_GRIDPANE][ROW_GRIDPANE];
	/**
	 * nombre de tours max
	 */
	final static int nb_rounds_max = 15;
	/**
	 * nombre de tours 
	 */
	static int nb_rounds = 1;
	/**
	 * durée d'un tour en ms
	 */
	final static int TimeRound = 50000;
	/**
	 * boolean pour savoir si c'est le tour du joueur 
	 */
	static boolean round_player = true;
	/**
	 * nb de chateaux neutres
	 */
	final static int nb_neutral_castle = 2;
	/**
	 * nombre de ducs
	 */
	final static int nb_dukes = 4;
	/**
	 * nombre de soldiers min
	 */
	final static int soldier_min = 10;
	/**
	 * nombre de soldiers max
	 */
	final static int soldier_max = 40;
	/**
	 * nombre de trésor min
	 */
	final static int treasure_Min = 20;
	/**
	 * nombre de trésor max
	 */
	final static int treasure_Max = 50;
	/**
	 * tableau à deux dimensions prend tous les chateaux. On accède au X avec l'indice du duc et Y avec l'indice de son chateau
	 * 	 */
	final static  Castle castles[][] = new Castle[nb_neutral_castle+nb_dukes][nb_neutral_castle+nb_dukes];
	/**
	 * tableau prenant en tous les images des chateaux
	 */
	final static ImageView[] castle = new ImageView[nb_dukes+nb_neutral_castle];
	/**
	 * couts en trésor pour la production d'un soldat
	 */
	static final int COST_PROD = 2;
	/**
	 * trésor total du joueur
	 */
	static int Treasure = 0;
//	variable pour utiliser les multi-thread
	static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1000); 
	/**
	 * listes des chateaux
	 */
	static ArrayList<Integer> list_castles = new ArrayList<>();
	/**
	 * listes des ducs
	 */
	static ArrayList<Integer> list_Dukes = new ArrayList<>();
	/**
	 * Timer du jeu
	 */
	static AnimationTimer gameLoop;
}
