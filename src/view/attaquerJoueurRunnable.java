package view;


import java.util.TimerTask;
/**
 * <Cette classe cr�e  permet de planifier les attaques  de les executer en arri�re plan gr�ce aux threads secondaire sans bloquer la mise � jour automatique de l'application.
 * En effet lorsque ce thread serra lancer la fonction "attaquer_Joueur()" @see {@link #attaquer_Joueur()} que nous implementerons serra executer dans ce thread.
 * 
 * 
 *
 */
public abstract class attaquerJoueurRunnable extends TimerTask{
	@Override
	public final void run(){
		attaquer_Joueur();
	}
	public abstract void attaquer_Joueur();

}