package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;


public class CrawlerUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CrawlerUI frame = new CrawlerUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CrawlerUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 524, 443);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Seed URL");
		lblNewLabel.setForeground(new Color(0, 0, 139));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblNewLabel.setBounds(21, 181, 84, 14);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(79, 206, 419, 27);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("CONTCY");
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setFont(new Font("Jing Jing", Font.BOLD, 40));
		lblNewLabel_1.setBounds(152, 46, 175, 50);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("smart search engine");
		lblNewLabel_2.setForeground(new Color(0, 0, 139));
		lblNewLabel_2.setFont(new Font("SketchFlow Print", Font.ITALIC, 11));
		lblNewLabel_2.setBounds(180, 92, 126, 14);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("Crawl");
		btnNewButton.setBackground(Color.RED);
		btnNewButton.setForeground(new Color(0, 0, 128));
		btnNewButton.setFont(new Font("SketchFlow Print", Font.BOLD, 12));
		btnNewButton.setBounds(196, 260, 89, 23);
		contentPane.add(btnNewButton);
		
		lblNewLabel_3 = new JLabel("crawled pages :");
		lblNewLabel_3.setFont(new Font("SketchFlow Print", Font.ITALIC, 12));
		lblNewLabel_3.setForeground(Color.RED);
		lblNewLabel_3.setBounds(10, 294, 107, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_4.setBounds(115, 294, 46, 14);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Current page :");
		lblNewLabel_5.setFont(new Font("SketchFlow Print", Font.ITALIC, 12));
		lblNewLabel_5.setForeground(Color.RED);
		lblNewLabel_5.setBounds(10, 319, 100, 14);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setBounds(125, 319, 373, 14);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("CopyRights \u00A9 2014 - InfinityDream");
		lblNewLabel_7.setForeground(Color.BLUE);
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_7.setBounds(333, 379, 175, 14);
		contentPane.add(lblNewLabel_7);
	}
}
