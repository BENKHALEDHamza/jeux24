package jeu24h;



import java.util.Random;


public class Grille {

	public final String[] orientations = { "Face au Nord", "Face Ã  l'Ouest",
			"Face au Sud", "Face  Ã  l'Est" };

	private Case[][] cases;

	public int orientation = Case.SUD;

	public Case voyageur;

	private int l, c;

	private static Random h = new Random();

	public Grille(int l, int c) {
		super();
		this.l = l;
		this.c = c;
		cases = new Case[l][c];
		for (int ll = 0; ll < l; ll++)
			for (int cc = 0; cc < c; cc++)
				cases[ll][cc] = new Case(ll, cc, this);
		for (int ll = 0; ll < l; ll++)
			for (int cc = 0; cc < c; cc++) {
				if (ll > 0)
					cases[ll][cc].setVoisine(Case.SUD, cases[ll - 1][cc]);
				if (cc > 0)
					cases[ll][cc].setVoisine(Case.OUEST, cases[ll][cc - 1]);
				if (ll < l - 1)
					cases[ll][cc].setVoisine(Case.NORD, cases[ll + 1][cc]);
				if (cc < c - 1)
					cases[ll][cc].setVoisine(Case.EST, cases[ll][cc + 1]);
			}
	}

	public void creerLabyrinthe() {
		for (int ll = 0; ll < l; ll++)
			for (int cc = 0; cc < c; cc++)
				cases[ll][cc].marque = Case.VIDE;
		int rl = zeroAnExclus(l);
		int rc = zeroAnExclus(c);
		Case racine = cases[rl][rc];
		racine.toutFermer();
		racine.marque = Case.CROIX;
		explorer(racine);
		for (int ll = 0; ll < l; ll++)
			for (int cc = 0; cc < c; cc++)
				cases[ll][cc].marque = Case.VIDE;
	}

	public void placerSortieEtVoyageur() {
		int rl = zeroAnExclus(l);
		int rc = zeroAnExclus(c);
		boolean cFait = false;
		int ll = rl;
		int cc = rc;
		while (!cFait && ll < l) {
			while (!cFait && cc < c) {
				int n = cases[ll][cc].nombreDeMurs();
				if (n == 3) {
					cases[ll][cc].toutFermer();
					cases[ll][cc].marque = Case.SORTIE;
					cFait = true;
				}
				cc++;
			}
			cc = 0;
			ll++;
		}
		ll = rl;
		cc = rc;
		while (!cFait && ll >= 0) {
			while (!cFait && cc >= 0) {
				int n = cases[ll][cc].nombreDeMurs();
				if (n == 3) {
					cases[ll][cc].toutFermer();
					cases[ll][cc].marque = Case.SORTIE;
					cFait = true;
				}
				cc--;
			}
			cc = c - 1;
			ll--;
		}
		// voyageur;
		ll = zeroAnExclus(l);
		cc = zeroAnExclus(c);
		while (cases[ll][cc].marque != Case.VIDE) {
			ll = zeroAnExclus(l);
			cc = zeroAnExclus(c);
		}
		voyageur = cases[ll][cc];
	}

	private void explorer(Case racine) {
		if (!racine.sortiePossible())
			return;
		while (racine.sortiePossible()) {
			int direction = zeroAnExclus(4);
			while (!racine.libre(direction))
				direction = zeroAnExclus(4);
			Case v = racine.getVoisine(direction);
			v.toutFermer();
			racine.setMur(direction, false);
			v.marque = Case.CROIX;
			explorer(v);
		}
	}

	public static int zeroAnExclus(int n) {
		return h.nextInt(n);
	}

	public int getC() {
		return c;
	}

	public int getL() {
		return l;
	}

	public Case getCase(int l, int c) {
		return cases[l][c];
	}

	// Gestion du voyageur

	public String orientationDuVoyageur() {
		return orientations[orientation];
	}

	public void marquerCaseVoyageur(boolean b) {
		voyageur.marque = (b ? Case.CROIX : Case.VIDE);
	}

	public boolean estMarqueeCaseVoyageur() {
		return voyageur.marque == Case.CROIX;
	}

	public boolean voyageurEstSorti() {
		return voyageur.marque == Case.SORTIE;
	}

	public boolean voyageurEstFaceAuBord() {
		return voyageur.getVoisine(orientation) == null;
	}

	public boolean voyageurEstFaceAuMur() {
		return voyageur.getMur(orientation);
	}

	public boolean voyageurEstDosAuMur() {
		return voyageur.getMur((orientation + Case.ARRIERE) % 4);
	}

	public void exploserEtFranchirMurEnFace() {
		voyageur.setMur(orientation, false);
		avancerVoyageur();
	}

	public void avancerVoyageur() {
		if (!voyageurEstFaceAuMur())
			voyageur = voyageur.getVoisine(orientation);
	}

	public void reculerVoyageur() {
		if (!voyageurEstDosAuMur())
			voyageur = voyageur.getVoisine((orientation + Case.ARRIERE) % 4);
	}

	public void tournerAgauche() {
		orientation = (orientation + 1) % 4;
	}

	public void tournerAdroite() {
		orientation = (orientation + 3) % 4;
	}

	public String toString() {
		return "Grille(" + getL() + "," + getC() + ")";
	}

	public String affichage() {
		String txt = toString() + "\n";
		for (int cc = 0; cc < c; cc++) {
			txt += "+---";
		}
		txt += "+\n";
		for (int ll = 0; ll < l; ll++) {
			txt += "|";
			for (int cc = 0; cc < c; cc++) {
				txt += (cases[ll][cc].marque == Case.CROIX ? " X " : "   ");
				txt += (cases[ll][cc].getMur(Case.EST) ? "|" : " ");
			}
			txt += "\n";
			txt += "+";
			for (int cc = 0; cc < c; cc++) {
				txt += (cases[ll][cc].getMur(Case.NORD) ? "---+" : "   +");
			}
			txt += "\n";
		}
		return txt;
	}
}