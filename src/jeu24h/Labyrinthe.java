package jeu24h;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

/**
 * @author Jacques Duma
 * @version 9 février 2006
 */
public class Labyrinthe extends JApplet implements ActionListener {

	private static final long serialVersionUID = -5413967253672451073L;

	private static final String AUSUJETDE = "À propos du Labyrinthe...";

	private static final String APROPOS = "<html>Un programme de: <center><b>Jacques Duma</b></center>"
			+ "Sur une idée de: <center><b>Benoît Rittaud</b></center>"
			+ "<i>L'Assassin des échecs et autres fictions mathématiques<br>"
			+ "Éditions Le Pommier</i></html>";

	private static final String MODEDEMPLOI = "Mode d'emploi...";

	private static final String AIDE = "<html><center><h1>Le Labyrinthe</h1></center> "
			+ "Vous êtes enfermé dans un Labyrinthe dont les murs"
			+ " périphériques sont indestructibles.<p>"
			+ "Les cloisons, c'est à dire les murs intérieurs, sont plus fragiles.<p>"
			+ "Une pièce secrète, fermée par des cloisons, marquée en vert ci-dessous,"
			+ " contient la porte de sortie.<p>"
			+ "Vous disposez d'une <b>unique</> charge de dynamite qui permet"
			+ " de faire exploser une <b>unique</> cloison.<p>"
			+ "Le but est de faire un plan du Labyrinthe pour découvrir"
			+ " la pièce secrète, et y pénétrer pour atteindre la sortie. <p>"
			+ "<center><i>Sans regarder le plan, évidement, ce qui serait trop facile !</i></center>"
			+ "</html>";

	private static final String AIDEBIS = "Attention:  Dans le Labyrinthe on ne voit que trois pièces successives, plus loin, il fait trop sombre.";

	private static final String JOUER = "Derrière quel mur est cachée la pièce secrète dans laquelle se trouve la sortie ?";

	private static final String BRAVO = "Bravo vous pouvez sortir !";

	private static final String PERDU = "Hélas, vous êtes définitivement enfermé !";

	private static final String MURPERIF = "Ce mur périphérique est indestructible.";

	private static final String PASBON = "Ce mur n'était pas celui de la pièce secrète.";

	private static final String FINI = "La visite est terminée";

	private static final String[] nomsOpts = { "Débutant", "Expert", "Champion" };

	private String[] optionsDeJeu = new String[nomsOpts.length];

	private static int numeroOption = 0;

	private static final String I_avancer = "avancer.gif";

	private static final String I_gauche = "gauche.gif";

	private static final String I_droite = "droite.gif";

	private static final String I_reculer = "reculer.gif";

	private static final String I_nord = "nord.gif";

	private static final String I_ouest = "ouest.gif";

	private static final String I_sud = "sud.gif";

	private static final String I_est = "est.gif";

	private static final String I_exemple = "exemple.gif";

	private static final String I_dynamite = "dynamite.gif";

	private static final int l = 3;

	private static final int c = 3;

	private boolean partieEnCours = true;

	private Grille labyrinthe = new Grille(l, c);

	private JPlanLabyrinthe planLabyrinthe;

	private JLabyrinthe3D labyrinthe3D;

	private JLabel message = new JLabel(JOUER, JLabel.CENTER);

	private JLabel roseDesVents = new JLabel((Icon) null, JLabel.CENTER);

	private JButton avancer, gauche, droite, reculer;

	private JRadioButton[] niveau = new JRadioButton[optionsDeJeu.length];

	private JCheckBox marquer = new JCheckBox("Marquer le sol en avançant");

	private JButton aPropos = new JButton(AUSUJETDE);

	private JButton aide = new JButton(MODEDEMPLOI);

	private JButton effacer = new JButton("Effacer la marque");

	private JButton exploser;

	private JButton rejouer = new JButton("Nouveau Labyrinthe");

	private ImageIcon[] vent = new ImageIcon[4];

	private JFrame frameAide, frameAPropos;

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String nomDuFichier) {
		java.net.URL imgURL = Labyrinthe.class.getResource("images/"
				+ nomDuFichier);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Fichier introuvable: " + nomDuFichier);
			return null;
		}
	}

	private JPanel panelJeux() {
		exploser = new JButton("Exploser le mur et Avancer",
				createImageIcon(I_dynamite));
		exploser.setVerticalTextPosition(SwingConstants.BOTTOM);
		exploser.setHorizontalTextPosition(SwingConstants.CENTER);
		exploser.setBorderPainted(false);
		JPanel c = new JPanel();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		avancer = new JButton(createImageIcon(I_avancer));
		gauche = new JButton(createImageIcon(I_gauche));
		droite = new JButton(createImageIcon(I_droite));
		reculer = new JButton(createImageIcon(I_reculer));
		avancer.setBorderPainted(false);
		gauche.setBorderPainted(false);
		droite.setBorderPainted(false);
		reculer.setBorderPainted(false);
		gauche.addActionListener(this);
		avancer.addActionListener(this);
		droite.addActionListener(this);
		reculer.addActionListener(this);
		exploser.addActionListener(this);
		rejouer.addActionListener(this);
		JPanel p = new JPanel();
		JPanel m = new JPanel(new GridLayout(2, 1));
		m.add(avancer);
		m.add(reculer);
		p.add(gauche);
		p.add(m);
		p.add(droite);
		c.add(p);
		p = new JPanel();
		p.add(exploser);
		c.add(p);
		c.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
		return c;
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

	private JPanel panelRose() {
		JPanel c = new JPanel();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		roseDesVents.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		roseDesVents.setIcon(vent[0]);
		JPanel p = new JPanel();
		p.add(new JSeparator());
		c.add(p);
		p = new JPanel();
		p.add(roseDesVents);
		c.add(p);
		c.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
		return c;
	}

	private JPanel panelNiveau() {
		JPanel p = new JPanel(new GridLayout(optionsDeJeu.length, 1));
		ButtonGroup bg = new ButtonGroup();
		for (int k = 0; k < nomsOpts.length; k++) {
			optionsDeJeu[k] = nomsOpts[k] + ",  grille " + (k * l + l) + "x"
					+ (k * c + c);
			niveau[k] = new JRadioButton(optionsDeJeu[k]);
			niveau[k].setEnabled(false);
			bg.add(niveau[k]);
			p.add(niveau[k]);
		}
		niveau[numeroOption].setSelected(true);
		p.setBorder(BorderFactory.createTitledBorder("Niveau de jeu"));
		return p;
	}

	private JPanel panelMarque() {
		JPanel c = new JPanel();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		aPropos.addActionListener(this);
		aide.addActionListener(this);
		marquer.addActionListener(this);
		effacer.addActionListener(this);
		JPanel p = new JPanel();
		p.add(aPropos);
		c.add(p);
		p = new JPanel();
		p.add(aide);
		c.add(p);
		p = new JPanel();
		p.add(panelNiveau());
		c.add(p);
		p = new JPanel();
		p.add(rejouer);
		c.add(p);
		p = new JPanel();
		p.add(new JSeparator());
		c.add(p);
		p = new JPanel();
		p.add(new JSeparator());
		c.add(p);
		p = new JPanel();
		p.add(marquer);
		c.add(p);
		p = new JPanel();
		p.add(effacer);
		c.add(p);
		c.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
		return c;
	}

	public void init() {
		vent[0] = createImageIcon(I_nord);
		vent[1] = createImageIcon(I_ouest);
		vent[2] = createImageIcon(I_sud);
		vent[3] = createImageIcon(I_est);
		labyrinthe.creerLabyrinthe();
		labyrinthe.placerSortieEtVoyageur();
		planLabyrinthe = new JPlanLabyrinthe(labyrinthe);
		labyrinthe3D = new JLabyrinthe3D(labyrinthe);
		// JPanel ll = new JPanel(new GridLayout(1,2));
		// ll.add(planLabyrinthe);
		// ll.add(labyrinthe3D);
		JSplitPane ll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				planLabyrinthe, labyrinthe3D);
		ll.setResizeWeight(0.0);
		ll.setOneTouchExpandable(true);
		ll.setContinuousLayout(true);
		JPanel p = new JPanel(new BorderLayout());
		message.setText(JOUER);
		message.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		p.add(message, BorderLayout.NORTH);
		p.add(ll, BorderLayout.CENTER);
		p.add(panelJeux(), BorderLayout.SOUTH);
		p.add(panelRose(), BorderLayout.WEST);
		p.add(panelMarque(), BorderLayout.EAST);
		p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		numeroOption = 0;
		niveau[numeroOption].setSelected(true);
		actualiserBoutons();
		getContentPane().add(p);
	}

	public static void main(String s[]) {
		JFrame frame = new JFrame("Le Labyrinthe");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		JApplet applet = new Labyrinthe();
		frame.getContentPane().add(applet, BorderLayout.CENTER);
		applet.init();
		frame.setSize(new Dimension(950, 750));
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == aPropos)
			aPropos();
		else if (s == aide)
			aide();
		else if (s == gauche)
			labyrinthe.tournerAgauche();
		else if (s == avancer)
			labyrinthe.avancerVoyageur();
		else if (s == reculer)
			labyrinthe.reculerVoyageur();
		else if (s == droite)
			labyrinthe.tournerAdroite();
		else if (s == effacer) {
			marquer.setSelected(false);
			labyrinthe.marquerCaseVoyageur(false);
		} else if (s == exploser)
			exploser();
		else if (s == rejouer)
			rejouer();
		actualiserBoutons();
	}

	private void aide() {
		if (frameAide == null) {
			frameAide = new JFrame(MODEDEMPLOI);
			frameAide.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frameAide.setResizable(false);

			// Create and set up the content pane.
			JPanel pp = new JPanel();
			pp.setLayout(new BoxLayout(pp, BoxLayout.Y_AXIS));
			JPanel p = new JPanel();
			p.add(new JLabel(AIDE));
			pp.add(p);
			p = new JPanel();
			p.add(new JLabel(createImageIcon(I_exemple)));
			pp.add(p);
			p = new JPanel();
			p.add(new JLabel(AIDEBIS));
			pp.add(p);
			pp.setOpaque(true);
			pp.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			frameAide.setContentPane(pp);
			// Display the window.
			frameAide.pack();
			frameAide.setLocationRelativeTo(null);
		}
		frameAide.show();
	}

	private void aPropos() {
		if (frameAPropos == null) {
			frameAPropos = new JFrame(AUSUJETDE);
			frameAPropos.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frameAPropos.setResizable(false);

			// Create and set up the content pane.
			JLabel etq = new JLabel(APROPOS);
			etq.setOpaque(true);
			etq.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			frameAPropos.setContentPane(etq);
			// Display the window.
			frameAPropos.pack();
			frameAPropos.setLocationRelativeTo(null);
		}
		frameAPropos.show();
	}

	private void exploser() {
		partieEnCours = false;
		String info = PASBON;
		marquer.setSelected(false);
		if (labyrinthe.voyageurEstFaceAuBord())
			info = MURPERIF;
		else
			labyrinthe.exploserEtFranchirMurEnFace();
		exploser.setEnabled(false);
		rejouer.setEnabled(true);
		message.setForeground(Color.RED);
		if (labyrinthe.voyageurEstSorti()) {
			message.setForeground(Color.BLUE);
			info = BRAVO;
		} else {
			info += " " + PERDU;
			message.setForeground(Color.RED);
		}
		message.setText(info);
		JOptionPane.showMessageDialog(null, info, FINI,
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void rejouer() {
		for (int k = 0; k < optionsDeJeu.length; k++)
			if (niveau[k].isSelected()) {
				numeroOption = k;
				labyrinthe = new Grille(l * k + l, c * k + c);
			}
		partieEnCours = true;
		labyrinthe.creerLabyrinthe();
		labyrinthe.placerSortieEtVoyageur();
		planLabyrinthe.setLabyrinthe(labyrinthe);
		labyrinthe3D.setLabyrinthe(labyrinthe);
		message.setForeground(Color.BLACK);
		message.setText(JOUER);
	}

	private void actualiserBoutons() {
		planLabyrinthe.repaint();
		labyrinthe3D.repaint();
		roseDesVents.setIcon(vent[labyrinthe.orientation]);
		avancer.setEnabled(!labyrinthe.voyageurEstFaceAuMur());
		reculer.setEnabled(!labyrinthe.voyageurEstDosAuMur());
		exploser.setEnabled(partieEnCours && labyrinthe.voyageurEstFaceAuMur());
		for (int k = 0; k < optionsDeJeu.length; k++)
			niveau[k].setEnabled(!partieEnCours);
		rejouer.setEnabled(!partieEnCours);
		if (marquer.isSelected())
			labyrinthe.marquerCaseVoyageur(true);
		effacer.setEnabled(labyrinthe.estMarqueeCaseVoyageur());
	}
}