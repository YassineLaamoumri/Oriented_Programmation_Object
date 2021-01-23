package view;
import java.util.TimerTask;

/**
 * <Cette classe crée  permet de planifier les attaques  de les executer en arrière plan grâce aux threads secondaire sans bloquer la mise à jour automatique de l'application.
 * En effet lorsque ce thread serra lancer la fonction "attack()" @see {@link #attack()} que nous implementerons serra executer dans ce thread.
 * 
 * 
 *
 */
public abstract class AttackRunnable implements Runnable {
	@Override
	public final void run(){
		attack();
	}
	public abstract void attack();
	}
