/*
Bonjour je me call Orel
*/

package jeu24h;


public class Case {

	
	public static final int VIDE = 0;

	public static final int CROIX = 1;

	public static final int SORTIE = 2;

	/**
	 * Orientations absolues
	 */
	public static final int SUD = 0;

	public static final int OUEST = 1;

	public static final int NORD = 2;

	public static final int EST = 3;

	/**
	 * Orientations relatives
	 */
	public static final int FACE = 0;

	public static final int GAUCHE = 1;

	public static final int ARRIERE = 2;

	public static final int DROITE = 3;

	protected boolean[] mur = { false, false, false, false };

	/**
	 * Case voisine ou null si pas de voisine.
	 * <p>
	 * Exsemple: voisine[SUD]
	 * <p>
	 * Ne pas confondre avec: voisine(FACE)
	 */
	private Case[] voisine = new Case[4];

	/**
	 * Présence d'une marque dans la case.
	 */
	public int marque = VIDE;

	// Coordonnées (ligne colonne).
	private int l, c;

	private Grille g;

	/**
	 * Crée une case de coordonnées (l, c) dans la grille g.
	 * 
	 * @param l
	 * @param c
	 * @param g
	 */
	public Case(int l, int c, Grille g) {
		super();
		this.l = l;
		this.c = c;
		this.g = g;
	}

	public int getC() {
		return c;
	}

	public int getL() {
		return l;
	}

	public int getOrientation() {
		if (g == null)
			return SUD;
		return g.orientation;
	}

	public boolean getMur(int position) {
		if (voisine[position] == null)
			return true;
		return mur[position];
	}

	/**
	 * Présence du Mur en position relative.
	 * <p>
	 * Exemple: mur(DROIT)
	 * <p>
	 * Ne pas confondre avec la position absolue: getMur(OUEST)
	 * 
	 * @param direction
	 * @return présence du mur
	 */
	public boolean mur(int position) {
		return getMur((position + getOrientation()) % 4);
	}

	public void setMur(int position, boolean b) {
		mur[position] = b;
		if (voisine[position] != null)
			voisine[position].mur[(position + 2) % 4] = b;
	}

	public void toutFermer() {
		setMur(SUD, true);
		setMur(OUEST, true);
		setMur(NORD, true);
		setMur(EST, true);
	}

	public Case getVoisine(int direction) {
		return voisine[direction];
	}

	/**
	 * Case voisine selon la direction relative.
	 * <p>
	 * Exsemple: voisine(GAUCHE)
	 * <p>
	 * Ne pas confondre avec la direction absolue: getVoisine(EST)
	 * 
	 * @param direction
	 * @return Case ou null
	 */
	public Case voisine(int direction) {
		return voisine[(direction + getOrientation()) % 4];
	}

	public void setVoisine(int direction, Case voisine) {
		this.voisine[direction] = voisine;
	}

	public boolean libre(int direction) {
		Case c = voisine(direction);
		if (c == null)
			return false;
		else
			return c.marque == VIDE;
	}

	public boolean sortiePossible() {
		return libre(SUD) || libre(OUEST) || libre(NORD) || libre(EST);
	}

	public int nombreDeMurs() {
		return (getMur(SUD) ? 1 : 0) + (getMur(OUEST) ? 1 : 0)
				+ (getMur(NORD) ? 1 : 0) + (getMur(EST) ? 1 : 0);
	}

	public String toString() {
		return "Case(" + getL() + "," + getC() + ")";
	}
}
