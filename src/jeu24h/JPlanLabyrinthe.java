package jeu24h;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Polygon;

import javax.swing.JComponent;

/**
 * @author Jacques Duma
 * @version 8 février 2006
 */
public class JPlanLabyrinthe extends JComponent {

	private static final long serialVersionUID = 8334827827222779928L;

	private static final Color BORD = Color.BLACK;

	private static final Color SORTIE = Color.GREEN;

	private static final Color CROIX = Color.YELLOW;

	private static final Color VOYAGEUR = Color.BLUE;

	private Grille labyrinthe;

	private int l, c, lc, w, w0, h, h0;

	public JPlanLabyrinthe(Grille labyrinthe) {
		super();
		this.labyrinthe = labyrinthe;
	}

	private void actualiserConstantes() {
		l = labyrinthe.getL();
		c = labyrinthe.getC();
		Dimension size = getSize();
		Insets in = getInsets();
		w = size.width;
		h = size.height;
		int lw = (w - in.left - in.right - 6) / c;
		int lh = (h - in.top - in.bottom - 6) / l;
		lc = (lw < lh ? lw : lh);
		w0 = (w - c * lc) / 2;
		h0 = (h - l * lc) / 2;
	}

	private void paintVoyageur(Graphics g) {
		Case voyageur = labyrinthe.voyageur;
		int vl = voyageur.getL();
		int vc = voyageur.getC();
		Polygon p = new Polygon();
		if (labyrinthe.orientation == Case.SUD) {
			p.addPoint(w0 + vc * lc + lc / 2, h0 + vl * lc + lc / 4);
			p.addPoint(w0 + vc * lc + lc / 4, h0 + vl * lc + 3 * lc / 4);
			p.addPoint(w0 + vc * lc + lc / 2, h0 + vl * lc + lc / 2);
			p.addPoint(w0 + vc * lc + 3 * lc / 4, h0 + vl * lc + 3 * lc / 4);
		}
		if (labyrinthe.orientation == Case.OUEST) {
			p.addPoint(w0 + vc * lc + lc / 4, h0 + vl * lc + lc / 2);
			p.addPoint(w0 + vc * lc + 3 * lc / 4, h0 + vl * lc + lc / 4);
			p.addPoint(w0 + vc * lc + lc / 2, h0 + vl * lc + lc / 2);
			p.addPoint(w0 + vc * lc + 3 * lc / 4, h0 + vl * lc + 3 * lc / 4);
		}
		if (labyrinthe.orientation == Case.NORD) {
			p.addPoint(w0 + vc * lc + lc / 2, h0 + vl * lc + 3 * lc / 4);
			p.addPoint(w0 + vc * lc + lc / 4, h0 + vl * lc + lc / 4);
			p.addPoint(w0 + vc * lc + lc / 2, h0 + vl * lc + lc / 2);
			p.addPoint(w0 + vc * lc + 3 * lc / 4, h0 + vl * lc + lc / 4);
		}
		if (labyrinthe.orientation == Case.EST) {
			p.addPoint(w0 + vc * lc + 3 * lc / 4, h0 + vl * lc + lc / 2);
			p.addPoint(w0 + vc * lc + lc / 4, h0 + vl * lc + lc / 4);
			p.addPoint(w0 + vc * lc + lc / 2, h0 + vl * lc + lc / 2);
			p.addPoint(w0 + vc * lc + lc / 4, h0 + vl * lc + 3 * lc / 4);
		}
		g.setColor(VOYAGEUR);
		g.fillPolygon(p);
	}

	private void paintCase(Graphics g, int ll, int cc) {
		Case maCase = labyrinthe.getCase(ll, cc);
		if (maCase.marque == Case.CROIX) {
			g.setColor(CROIX);
			g.fillOval(w0 + cc * lc + lc / 4 + 1, h0 + ll * lc + lc / 4 + 1,
					lc / 2, lc / 2);
		}
		if (maCase.marque == Case.SORTIE) {
			g.setColor(SORTIE);
			g.fillRect(w0 + cc * lc + 2, h0 + ll * lc + 2, lc - 2, lc - 2);
		}
		g.setColor(BORD);
		if (maCase.getMur(Case.SUD))
			g.drawLine(w0 + cc * lc, h0 + ll * lc + 1, w0 + cc * lc + lc, h0
					+ ll * lc + 1);
		if (maCase.getMur(Case.OUEST))
			g.drawLine(w0 + cc * lc + 1, h0 + ll * lc, w0 + cc * lc + 1, h0
					+ ll * lc + lc);
		if (maCase.getMur(Case.NORD))
			g.drawLine(w0 + cc * lc, h0 + ll * lc + lc, w0 + cc * lc + lc, h0
					+ ll * lc + lc);
		if (maCase.getMur(Case.EST))
			g.drawLine(w0 + cc * lc + lc, h0 + ll * lc, w0 + cc * lc + lc, h0
					+ ll * lc + lc);
	}

	public void paint(Graphics g) {
		actualiserConstantes();
		for (int ll = 0; ll < l; ll++)
			for (int cc = 0; cc < c; cc++) {
				paintCase(g, ll, cc);
			}
		paintVoyageur(g);
		g.setColor(BORD);
		g.drawRect(w0, h0, c * lc + 1, l * lc + 1);
	}

	public void setLabyrinthe(Grille labyrinthe) {
		this.labyrinthe = labyrinthe;
	}

}