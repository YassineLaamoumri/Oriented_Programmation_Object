
package view;

import java.util.TimerTask;
/**
 * <Cette classe cr�e  permet de planifier les attaques  de les executer en arri�re plan gr�ce aux threads secondaire sans bloquer la mise � jour automatique de l'application.
 * En effet lorsque ce thread serra lancer la fonction @see #random_attack() que nous implementerons serra executer dans ce thread.
 * 
 * 
 *
 */
public abstract class Random_Attack_Runnable extends TimerTask{
	@Override
	public final void run(){
		random_attack();
	}
	public abstract void random_attack();

}