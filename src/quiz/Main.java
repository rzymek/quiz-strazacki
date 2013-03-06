package quiz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Main {
	private final static List<String> ACCEPTED = Arrays.asList(ImageIO.getReaderFileSuffixes());
	private final static Pattern ANSWER_REGEX = Pattern.compile("-([a-z])[.]");

	private JLabel results;
	private JFrame frame;
	private JLabel img;

	private int index = -1;
	private ArrayList<ImageIcon> images;

	private List<File> files;

	private int good = 0;

	private int bad = 0;
	private final Set<Character> buttons = new TreeSet<Character>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		try {
			File dir = loadFiles();
			initialize();
			if (files.isEmpty()) {
				img.setText("<html>Brak obrazków w katalogu " + dir.getAbsolutePath() + "<br>" + "Obsługiwane pliki: "
						+ ACCEPTED);
			}
			Collections.shuffle(files);
			images = new ArrayList<ImageIcon>(files.size());
			Dimension dim = new Dimension(0, 0);
			for (File file : files) {
				BufferedImage img = ImageIO.read(file);
				if (img == null) {
					throw new RuntimeException("Bład odczytu:" + file);
				}
				ImageIcon icon = new ImageIcon(img);
				images.add(icon);
				dim.width = Math.max(dim.width, icon.getIconWidth());
				dim.height = Math.max(dim.height, icon.getIconHeight());
			}
			img.setMinimumSize(dim);
			img.setPreferredSize(dim);

			results = new JLabel("");
			frame.getContentPane().add(results, BorderLayout.NORTH);
			nextImg();
			frame.pack();
		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = ex.getMessage();
			if (msg == null || msg.isEmpty()) {
				msg = ex.toString();
			}
			JOptionPane.showMessageDialog(frame, msg);
			System.exit(1);
		}
	}

	private File loadFiles() {
		File dir = new File("img");
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				int dot = name.lastIndexOf('.');
				if (dot == -1) {
					return false;
				}
				String ext = name.substring(dot + 1);
				return ACCEPTED.contains(ext.toLowerCase());
			}
		};
		files = Arrays.asList(dir.listFiles(filter));
		checkNames();		
		return dir;
	}

	private void checkNames() {
		for (File file : files) {
			String name = file.getName();
			Matcher matcher = ANSWER_REGEX.matcher(name);
			if (!matcher.find()) {
				throw new RuntimeException("Nazwa pliku nie zawiera odpowiedzi: " + name);
			}else{
				buttons.add(matcher.group(1).charAt(0));
			}
		}
	}

	private void nextImg() {
		index++;
		int no = Math.min(index + 1, images.size());
		int left = Math.max(0, files.size() - index - 1);
		results.setText(String.format("<html>Dobrze: %d, Źle: %d<br>Pytanie: %d, Pozostało: %d", good, bad, no, left));
		if (index >= images.size()) {
			img.setText("Koniec");
			img.setIcon(null);
			disable(frame);
		} else {
			ImageIcon icon = images.get(index);
			img.setIcon(icon);
		}
	}

	private void disable(Container c) {
		c.setEnabled(false);
		Component[] components = c.getComponents();
		for (Component component : components) {
			component.setEnabled(false);
			if (component instanceof Container) {
				disable((Container) component);
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Quiz");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		img = new JLabel("");
		img.setHorizontalTextPosition(SwingConstants.CENTER);
		img.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(img, BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		btnPanel.setPreferredSize(new Dimension(100*buttons.size(), 100));
		frame.getContentPane().add(btnPanel, BorderLayout.SOUTH);
		btnPanel.setLayout(new GridLayout(0, buttons.size(), 0, 0));

		for (char name : buttons) {
			addButton(btnPanel, name);			
		}
	}

	private void addButton(JPanel btnPanel, char name) {
		final String n = String.valueOf(name);
		JButton c = new JButton(n.toUpperCase());
		c.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				answer(n.toLowerCase());
			}
		});
		c.setFont(new Font("Dialog", Font.BOLD, 20));
		btnPanel.add(c);
	}

	protected void answer(String answer) {
		if (index >= files.size()) {
			return;
		}
		String name = files.get(index).getName();
		Matcher macher = ANSWER_REGEX.matcher(name);
		if (macher.find()) {
			String filenameAnswer = macher.group(1);
			if (answer.equalsIgnoreCase(filenameAnswer)) {
				good++;
			} else {
				bad++;
			}
		} else {
			JOptionPane.showMessageDialog(frame, "Nazwa pliku bez odpowiedzi: " + name);
		}
		nextImg();
	}

}
