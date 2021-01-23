package view;
/**
 * <Cette classe cr�e  permet de planifier les attaques  de les executer en arri�re plan gr�ce aux threads secondaire sans bloquer la mise � jour automatique de l'application.
 * En effet lorsque ce thread serra lancer la fonction @see {@link #attaquer()} que nous implementerons serra executer dans ce thread.
 * 
 * 
 *
 */
public abstract class attaquerRunnable implements Runnable {
	@Override
	public final void run(){
		attaquer();
	}
	public abstract void attaquer();
	}
