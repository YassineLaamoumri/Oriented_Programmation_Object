package view;

import java.util.TimerTask;
/**
 * <Cette classe crée  permet de planifier les attaques  de les executer en arrière plan grâce aux threads secondaire sans bloquer la mise à jour automatique de l'application.
 * En effet lorsque ce thread serra lancer la fonction "attaquer_Joueur()" @see {@link #attack_player()} que nous implementerons serra executer dans ce thread.
 * 
 * 
 *
 */
public abstract class AttackPlayerRunnable extends TimerTask{
	@Override
	public final void run(){
		attack_player();
	}
	public abstract void attack_player();

}