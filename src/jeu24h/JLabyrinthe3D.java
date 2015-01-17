package jeu24h;


import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

/**
 * @author Jacques Duma
 * @version 6 février 2006
 */
public class JLabyrinthe3D extends JComponent {

	private static final long serialVersionUID = -6512796267693107493L;

	private static final boolean DEBUG = false;

	private static final Color BORD = Color.BLACK;

	private static final Color SOL = Color.MAGENTA;//new Color(255, 200, 255);

	private static final Color FUITESOL = Color.GREEN;

	private static final Color CIEL = Color.BLUE;

	private static final Color FUITECIEL = Color.CYAN;

	private static final Color PLAN1 = Color.LIGHT_GRAY;

	private static final Color PLAN2 = Color.GRAY;

	private static final Color PLAN3 = Color.DARK_GRAY;

	private static final Color INFINI = Color.BLACK;

	private static final Color MARQUE = Color.YELLOW;

	private static final Color SORTIE = Color.GREEN;

	private int w, wc, wf, w0, w2, w4, w8, wp1, wp2, wp3, wl1, wl2, wl3, wm1,
			wm2, h, hc, hf, h0, h2, h4, h8, hi1, hi2, hi3, hh, hhhh, largeur;

	private boolean[] mur = { false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false };

	private int[] marque = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private Grille labyrinthe;

	private Case[] voyageur = new Case[9];

	public JLabyrinthe3D(Grille labyrinthe) {
		super();
		this.labyrinthe = labyrinthe;
	}

	private void actualiserConstantes() {
		Dimension size = getSize();
		w = size.width;
		h = size.height;
		largeur = (w < h ? w : h);
		// NEW
		w0 = (w - largeur) / 2;
		h0 = (h - largeur) / 2;
		wf = w0 + largeur;
		hf = h0 + largeur;
		wc = w / 2;
		hc = h / 2;
		w2 = largeur / 2;
		h2 = largeur / 2;
		w4 = largeur / 4;
		h4 = largeur / 4;
		w8 = largeur / 8;
		wm1 = (int) (largeur * 0.3);
		wm2 = (int) (largeur * 0.15);
		h8 = largeur / 8;
		hi1 = h4 + ((int) (h4 * 0.4));
		hi2 = h8 + ((int) (h8 * 0.4));
		hi3 = h8 / 2 + ((int) (h8 * 0.3));
		wp1 = w2 - w2 / 2;
		wp2 = w2 - w4 / 2;
		wp3 = w2 - ((int) (w8 / 1.5));
		wl1 = w2;
		wl2 = w4;
		wl3 = (int) (w8 * 1.3333);
		hhhh = (int) (w8 * 1.3333);
		hh = hhhh / 2;
	}
	
//	public void keyPressed(KeyEvent e)
//	{	
//		Point p = button.getLocation();
//		switch (e.getKeyCode())
//		{
//			case KeyEvent.VK_RIGHT:
//				button.setLocation(p.x+1, p.y);
//				break;
//			case KeyEvent.VK_LEFT:
//				button.setLocation(p.x-1, p.y);
//				break;
//			case KeyEvent.VK_UP:
//				button.setLocation(p.x, p.y-1);
//				break;
//			case KeyEvent.VK_DOWN:
//				button.setLocation(p.x, p.y+1);
//				break;
//		}
//	}

	/**
	 * Place le carrelage et le fond.
	 * 
	 * @param g
	 */
	private void paintPerspective(Graphics g) {
		// Carrelage
		g.setColor(SOL);
		g.fillRect(w0, hc, largeur - 1, h2);
		g.setColor(FUITESOL);
		for (int k = -10; k < 10; k++) {
			g.drawLine(wc, hc, wc + k * w4, hf);
		}
		g.drawLine(w0, hc + h4, wf, hc + h4);
		g.drawLine(w0, hc + hi1, wf, hc + hi1);
		g.drawLine(w0, hc + h8, wf, hc + h8);
		g.drawLine(w0, hc + hi2, wf, hc + hi2);
		g.drawLine(w0, hc + hi3, wf, hc + hi3);

		g.setColor(CIEL);
		g.fillRect(w0, h0, largeur - 1, h2);
		g.setColor(FUITECIEL);
		for (int k = -10; k < 10; k++) {
			g.drawLine(wc, hc, wc + k * w4, h0);
		}
		g.drawLine(w0, hc - h4, wf, hc - h4);
		g.drawLine(w0, hc - hi1, wf, hc - hi1);
		g.drawLine(w0, hc - h8, wf, hc - h8);
		g.drawLine(w0, hc - hi2, wf, hc - hi2);
		g.drawLine(w0, hc - hi3, wf, hc - hi3);
		g.setColor(PLAN3);
		g.setColor(INFINI);
		g.fillRect(w0, hc - hh, largeur - 1, hhhh);
		g.setColor(BORD);
		g.drawRect(w0, hc - hh, largeur - 1, hhhh);
		g.setColor(INFINI);
		g.fillRect(w0 + wp3, hc - hh, wl3, hhhh);
		g.setColor(BORD);
		g.drawRect(w0 + wp3, hc - hh, wl3, hhhh);
	}

	/**
	 * Place les murs selon les valeurs de mur[k]
	 * <p>
	 * ....... Fond <br>
	 * |.|.|.| 0 . 1 . 2 . 3 <br>
	 * +-+-+-+ . 4 . 5 . 6 . <br>
	 * |.|.|.| 7 . 8 . 9 . 10 <br>
	 * +-+-+-+ . 11. 12. 13. <br>
	 * |.|.|.| . . 14. 15. . <br>
	 * ...^... Position du voyageur <br>
	 * 
	 * @param g
	 */
	private void paintMur(Graphics g) {
		Polygon face;
		if (mur[0]) {
			face = new Polygon();
			face.addPoint(w0 + wp3 - wl3, hc + hh);
			face.addPoint(w0 + wp3 - wl3, hc - hh);
			face.addPoint(w0 + wp2 - wl2, h0 + h2 - h4 / 2);
			face.addPoint(w0 + wp2 - wl2, h0 + h2 + h4 / 2);
			g.setColor(PLAN3);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
		if (mur[1]) {
			face = new Polygon();
			face.addPoint(w0 + wp3, hc - hh);
			face.addPoint(w0 + wp3, hc + hh);
			face.addPoint(w0 + wp2, h0 + h2 + h4 / 2);
			face.addPoint(w0 + wp2, h0 + h2 - h4 / 2);
			g.setColor(PLAN3);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
		if (mur[2]) {
			face = new Polygon();
			face.addPoint(w0 + wp3 + wl3, hc - hh);
			face.addPoint(w0 + wp3 + wl3, hc + hh);
			face.addPoint(w0 + wp2 + wl2, h0 + h2 + h4 / 2);
			face.addPoint(w0 + wp2 + wl2, h0 + h2 - h4 / 2);
			g.setColor(PLAN3);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
		if (mur[3]) {
			face = new Polygon();
			face.addPoint(w0 + wp3 + 2 * wl3, hc + hh);
			face.addPoint(w0 + wp3 + 2 * wl3, hc - hh);
			face.addPoint(w0 + wp2 + 2 * wl2, h0 + h2 - h4 / 2);
			face.addPoint(w0 + wp2 + 2 * wl2, h0 + h2 + h4 / 2);
			g.setColor(PLAN3);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
		if (mur[4]) {
			g.setColor(PLAN2);
			g.fillRect(w0, h0 + h2 - h4 / 2, wp2, h4);
			g.setColor(BORD);
			g.drawRect(w0, h0 + h2 - h4 / 2, wp2, h4);
		}
		if (mur[5]) {
			g.setColor(PLAN2);
			g.fillRect(w0 + wp2, h0 + h2 - h4 / 2, wl2, h4);
			g.setColor(BORD);
			g.drawRect(w0 + wp2, h0 + h2 - h4 / 2, wl2, h4);
		}
		if (mur[6]) {
			g.setColor(PLAN2);
			g.fillRect(w0 + wp2 + wl2, h0 + h2 - h4 / 2, wp2, h4);
			g.setColor(BORD);
			g.drawRect(w0 + wp2 + wl2, h0 + h2 - h4 / 2, wp2, h4);
		}
		if (mur[7]) {
			face = new Polygon();
			face.addPoint(w0 + wp2 - wl2, h0 + h2 - h4 / 2);
			face.addPoint(w0 + wp2 - wl2, h0 + h2 + h4 / 2);
			face.addPoint(w0, h0 + h2 + h2 / 3);
			face.addPoint(w0, h0 + h2 - h2 / 3);
			g.setColor(PLAN2);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
		if (mur[8]) {
			face = new Polygon();
			face.addPoint(w0 + wp2, h0 + h2 - h4 / 2);
			face.addPoint(w0 + wp2, h0 + h2 + h4 / 2);
			face.addPoint(w0 + wp1, h0 + h2 + h2 / 2);
			face.addPoint(w0 + wp1, h0 + h2 - h2 / 2);
			g.setColor(PLAN2);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
		if (mur[9]) {
			face = new Polygon();
			face.addPoint(w0 + wp2 + wl2, h0 + h2 - h4 / 2);
			face.addPoint(w0 + wp2 + wl2, h0 + h2 + h4 / 2);
			face.addPoint(w0 + wp1 + wl1, h0 + h2 + h2 / 2);
			face.addPoint(w0 + wp1 + wl1, h0 + h2 - h2 / 2);
			g.setColor(PLAN2);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
		if (mur[10]) {
			face = new Polygon();
			face.addPoint(w0 + wp2 + 2 * wl2, h0 + h2 - h4 / 2);
			face.addPoint(w0 + wp2 + 2 * wl2, h0 + h2 + h4 / 2);
			face.addPoint(wf, h0 + h2 + h2 / 3);
			face.addPoint(wf, h0 + h2 - h2 / 3);
			g.setColor(PLAN2);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
		if (mur[11]) {
			g.setColor(PLAN1);
			g.fillRect(w0, h0 + h2 - h2 / 2, wp1, h2);
			g.setColor(BORD);
			g.drawRect(w0, h0 + h2 - h2 / 2, wp1, h2);
		}
		if (mur[12]) {
			g.setColor(PLAN1);
			g.fillRect(w0 + wp1, h0 + h2 - h2 / 2, wl1, h2);
			g.setColor(BORD);
			g.drawRect(w0 + wp1, h0 + h2 - h2 / 2, wl1, h2);
		}
		if (mur[13]) {
			g.setColor(PLAN1);
			g.fillRect(w0 + wp1 + wl1, h0 + h2 - h2 / 2, wp1, h2);
			g.setColor(BORD);
			g.drawRect(w0 + wp1 + wl1, h0 + h2 - h2 / 2, wp1, h2);
		}
		if (mur[14]) {
			face = new Polygon();
			face.addPoint(w0, h0);
			face.addPoint(w0, hf);
			face.addPoint(w0 + wp1, h0 + h2 + h2 / 2);
			face.addPoint(w0 + wp1, h0 + h2 - h2 / 2);
			g.setColor(PLAN1);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
		if (mur[15]) {
			face = new Polygon();
			face.addPoint(wf, h0);
			face.addPoint(wf, hf);
			face.addPoint(w0 + wp1 + wl1, h0 + h2 + h2 / 2);
			face.addPoint(w0 + wp1 + wl1, h0 + h2 - h2 / 2);
			g.setColor(PLAN1);
			g.fillPolygon(face);
			g.setColor(BORD);
			g.drawPolygon(face);
		}
	}

	/**
	 * Place les marques au sol selon les valeurs de marque[k]
	 * <p>
	 * ....... Fond <br>
	 * |.|.|.| 0 1 2 <br>
	 * +-+-+-+ <br>
	 * |.|.|.| 3 4 5 <br>
	 * +-+-+-+ <br>
	 * |.|.|.| . 6 . <br>
	 * ...^... Position du voyageur <br>
	 * 
	 * @param n
	 * @param b
	 */
	private void paintMarques(Graphics g) {
		int x = largeur / 50;
		int xx = 2 * x;
		int y = x / 2;
		int yy = 2 * y;
		if (marque[0] != 0) {
			g.setColor((marque[0] == Case.CROIX ? MARQUE : SORTIE));
			g.fillOval(w0 + wm1 - x, h0 + h2 + hi3 - y, xx, yy);
		}
		if (marque[1] != 0) {
			g.setColor((marque[1] == Case.CROIX ? MARQUE : SORTIE));
			g.fillOval(wc - x, h0 + h2 + hi3 - y, xx, yy);
		}
		if (marque[2] != 0) {
			g.setColor((marque[2] == Case.CROIX ? MARQUE : SORTIE));
			g.fillOval(wf - wm1 - x, h0 + h2 + hi3 - y, xx, yy);
		}
		if (marque[3] != 0) {
			g.setColor((marque[3] == Case.CROIX ? MARQUE : SORTIE));
			g.fillOval(w0 + wm2 - 2 * x, h0 + h2 + hi2 - 2 * y, 2 * xx, 2 * yy);
		}
		if (marque[4] != 0) {
			g.setColor((marque[4] == Case.CROIX ? MARQUE : SORTIE));
			g.fillOval(wc - 2 * x, h0 + h2 + hi2 - 2 * y, 2 * xx, 2 * yy);
		}
		if (marque[5] != 0) {
			g.setColor((marque[5] == Case.CROIX ? MARQUE : SORTIE));
			g.fillOval(wf - wm2 - 2 * x, h0 + h2 + hi2 - 2 * y, 2 * xx, 2 * yy);
		}
		if (marque[6] != 0) {
			g.setColor((marque[6] == Case.CROIX ? MARQUE : SORTIE));
			g.fillOval(w0 - 5 * x, h0 + h2 + hi1 - 4 * y, 4 * xx, 4 * yy);
		}
		if (marque[7] != 0) {
			g.setColor((marque[7] == Case.CROIX ? MARQUE : SORTIE));
			g.fillOval(wc - 4 * x, h0 + h2 + hi1 - 4 * y, 4 * xx, 4 * yy);
		}
		if (marque[8] != 0) {
			g.setColor((marque[8] == Case.CROIX ? MARQUE : SORTIE));
			g.fillOval(wf - 3 * x, h0 + h2 + hi1 - 4 * y, 4 * xx, 4 * yy);
		}
	}

	public void paint(Graphics g) {
		actualiserConstantes();
		actualiserCouloirs();
		g.setClip(w0, h0, largeur, largeur);
		paintPerspective(g);
		paintMarques(g);
		paintMur(g);
		// Cadre
		g.setColor(Color.BLACK);
		g.drawRect(w0, h0, largeur - 1, largeur - 1);
	}

	private void actualiserCouloirs() {
		if (DEBUG)
			return;
		int face = labyrinthe.orientation;
		int gauche = (face + Case.GAUCHE) % 4;
		int droite = (face + Case.DROITE) % 4;
		for (int k = 0; k < 9; k++)
			voyageur[k] = null;
		for (int k = 0; k < 9; k++)
			marque[k] = Case.VIDE;
		for (int k = 0; k < 16; k++)
			mur[k] = true;
		voyageur[7] = labyrinthe.voyageur;
		voyageur[6] = voyageur[7].getVoisine(gauche);
		voyageur[8] = voyageur[7].getVoisine(droite);
		if (voyageur[6] != null) {
			marque[6] = voyageur[6].marque;
			mur[11] = voyageur[6].getMur(face);
		}
		if (voyageur[8] != null) {
			marque[8] = voyageur[8].marque;
			mur[13] = voyageur[8].getMur(face);
		}
		marque[7] = voyageur[7].marque;
		mur[12] = voyageur[7].getMur(face);
		mur[14] = voyageur[7].getMur(gauche);
		mur[15] = voyageur[7].getMur(droite);
		voyageur[4] = voyageur[7].getVoisine(face);
		if (voyageur[4] != null) {
			marque[4] = voyageur[4].marque;
			voyageur[1] = voyageur[4].getVoisine(face);
			voyageur[3] = voyageur[4].getVoisine(gauche);
			voyageur[5] = voyageur[4].getVoisine(droite);
			mur[5] = voyageur[4].getMur(face);
			mur[8] = voyageur[4].getMur(gauche);
			mur[9] = voyageur[4].getMur(droite);
		}
		if (voyageur[1] != null) {
			marque[1] = voyageur[1].marque;
			voyageur[0] = voyageur[1].getVoisine(gauche);
			voyageur[2] = voyageur[1].getVoisine(droite);
			mur[1] = voyageur[1].getMur(gauche);
			mur[2] = voyageur[1].getMur(droite);
		}
		if (voyageur[3] != null) {
			marque[3] = voyageur[3].marque;
			mur[4] = voyageur[3].getMur(face);
			mur[7] = voyageur[3].getMur(gauche);
		}
		if (voyageur[5] != null) {
			marque[5] = voyageur[5].marque;
			mur[6] = voyageur[5].getMur(face);
			mur[10] = voyageur[5].getMur(droite);
		}
		if (voyageur[0] != null) {
			marque[0] = voyageur[0].marque;
			mur[0] = voyageur[0].getMur(gauche);
		}
		if (voyageur[2] != null) {
			marque[2] = voyageur[2].marque;
			mur[3] = voyageur[2].getMur(droite);
		}
	}

	/**
	 * Fixe la présence du mur de numéro n
	 * <p>
	 * ....... Fond <br>
	 * |.|.|.| 0 . 1 . 2 . 3 <br>
	 * +-+-+-+ . 4 . 5 . 6 . <br>
	 * |.|.|.| 7 . 8 . 9 . 10 <br>
	 * +-+-+-+ . 11. 12. 13. <br>
	 * |.|.|.| . . 14. 15. . <br>
	 * ...^... Position du voyageur <br>
	 * 
	 * @param n
	 * @param b
	 */
	public void setMur(int n, boolean b) {
		mur[n] = b;
	}

	/**
	 * Fixe la présence du marque au sol de numéro n
	 * <p>
	 * ....... Fond <br>
	 * |.|.|.| 0 1 2 <br>
	 * +-+-+-+ <br>
	 * |.|.|.| 3 4 5 <br>
	 * +-+-+-+ <br>
	 * |.|.|.| 6 7 8 <br>
	 * ...^... Position du voyageur <br>
	 * 
	 * @param n
	 * @param b
	 */
	public void setMarque(int n, boolean b) {
		marque[n] = (b ? Case.CROIX : Case.VIDE);
	}

	public void setLabyrinthe(Grille labyrinthe) {
		this.labyrinthe = labyrinthe;
	}
}