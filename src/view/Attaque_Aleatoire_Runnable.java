package view;
/**
 * <Cette classe cr�e  permet de planifier les attaques aleatoires  de les executer en arri�re plan gr�ce aux threads secondaire sans bloquer la mise � jour automatique de l'application.
 * En effet lorsque ce thread serra lancer la fonction  attaquer_Aleatoire() que nous implementerons serra executer dans ce thread.
 * 
 * 
 *
 */
import java.util.TimerTask;

public abstract class Attaque_Aleatoire_Runnable extends TimerTask{
	@Override
	public final void run(){
		attaquer_Aleatoire();
	}
	public abstract void attaquer_Aleatoire();

}