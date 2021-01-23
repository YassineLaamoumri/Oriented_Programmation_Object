package view;
/**
 * <Cette classe crée  permet de planifier les attaques  de les executer en arrière plan grâce aux threads secondaire sans bloquer la mise à jour automatique de l'application.
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
