package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame{
	
	private JTextArea textArea;
	private JScrollPane scroll;
	private JButton btn_analyze;
	private JTextField path;
	private JButton btn_selection;
	private JPanel upperOnePanel;
	private JComboBox<String> komboBox;
	private String[] filter = new String[] {"ALL","HTTP","HTTPS","TELNET","FTP-data","FTP-control","SSH","TFTP","ICMP","ARP"};
	
	public GUI() throws HeadlessException {
		super("Network data analyzer - Peter Tomascik");
		textArea = new JTextArea();
		scroll = new JScrollPane(textArea);
		btn_analyze = new JButton("Analyze!   ");
		btn_selection = new JButton("Select file");
		path = new JTextField();
		komboBox = new JComboBox<String>(filter);
		
		
		textArea.setFont(new Font("Monospaced",Font.PLAIN,12));
		textArea.setEditable(false);
		
		this.setLayout(new BorderLayout());
		this.add(scroll,BorderLayout.CENTER);
		Box pravyBox = Box.createVerticalBox();
		this.add(pravyBox,BorderLayout.EAST);
		pravyBox.add(Box.createVerticalStrut(10));
		pravyBox.add(btn_analyze);
		pravyBox.add(Box.createVerticalStrut(10));
		pravyBox.add(komboBox);
		komboBox.setAlignmentX(LEFT_ALIGNMENT);
		pravyBox.add(Box.createVerticalStrut(10));
		komboBox.setMaximumSize(btn_analyze.getMaximumSize());
		
		upperOnePanel = new JPanel();
		upperOnePanel.setLayout(new BorderLayout());
		upperOnePanel.add(path, BorderLayout.CENTER);
		upperOnePanel.add(btn_selection, BorderLayout.EAST);
		
		this.add(upperOnePanel, BorderLayout.NORTH);
		
		this.setSize(600, 400);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public JButton getBtn_analyze() {
		return btn_analyze;
	}

	public void setBtn_analyze(JButton btn_analyze) {
		this.btn_analyze = btn_analyze;
	}

	public JTextField getPath() {
		return path;
	}

	public void setPath(JTextField path) {
		this.path = path;
	}

	public JButton getBtn_selection() {
		return btn_selection;
	}

	public void setBtn_selection(JButton btn_selection) {
		this.btn_selection = btn_selection;
	}

	public static void main(String[] args) {
		//new GUI();

	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public JComboBox<String> getKomboBox() {
		return komboBox;
	}

	public void setKomboBox(JComboBox<String> komboBox) {
		this.komboBox = komboBox;
	}
	
	

}
