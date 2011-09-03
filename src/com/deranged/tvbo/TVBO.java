package com.deranged.tvbo;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

public class TVBO
{
	private Model model;
	private JFrame frame;
	private View viewPanel;
	private JPanel sidePanel;
	private JPanel unitsPanel;
	private JLabel unitsLabel;
	private JComboBox unitsDropDown;
	private JButton unitsAddButton;
	private JPanel buildingsPanel;
	private JLabel buildingsLabel;
	private JComboBox buildingsDropDown;
	private JButton buildingsAddButton;
	private JPanel researchPanel;
	private JLabel researchLabel;
	private JComboBox researchDropDown;
	private JButton researchAddButton;
	private JPanel totalsPanel;
	private JLabel totalsLabel;
	private JTextArea textArea;
	private JPanel buttonsPanel;
	private JButton loadButton;
	private JButton saveButton;
	private JButton clearButton;
	private JButton printButton;
	private int frameWidth = 1600;
	private int frameHeight = 1000;
	JFileChooser fc;
	File cwd;

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TVBO window = new TVBO();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} } );
	}

	public TVBO() {
		this.model = new Model();
		initialize();
		this.model.setup();
		this.model.play();
	}

	private void initialize() {
		this.fc = new JFileChooser();
		this.frame = new JFrame();
		this.frame.getContentPane().setBackground(SystemColor.control);
		this.viewPanel = new View(this.model);
		this.frame.setTitle("Terran Visual Build Order Designer - Cyanophage (ErebusWolf Fixes)");

		this.sidePanel = new JPanel();
		this.sidePanel.setBorder(new BevelBorder(1, null, null, null, null));
		GroupLayout groupLayout = new GroupLayout(this.frame.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(3)
						.addComponent(this.viewPanel, -1, 1035, 32767)
						.addGap(3)
						.addComponent(this.sidePanel, -2, 213, -2)
						.addGap(3)));

		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(3)
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(this.viewPanel, GroupLayout.Alignment.LEADING, -1, 841, 32767)
								.addComponent(this.sidePanel, GroupLayout.Alignment.LEADING, -1, 841, 32767))
								.addGap(3)));

		this.unitsPanel = new JPanel();
		this.unitsPanel.setBorder(null);
		this.unitsPanel.setBackground(SystemColor.control);
		this.buildingsPanel = new JPanel();
		this.buildingsPanel.setBorder(null);
		this.buildingsPanel.setBackground(SystemColor.control);
		this.researchPanel = new JPanel();
		this.researchPanel.setBorder(null);
		this.researchPanel.setBackground(SystemColor.control);
		this.buttonsPanel = new JPanel();
		this.buttonsPanel.setBackground(SystemColor.control);
		this.totalsPanel = new JPanel();
		this.totalsPanel.setBorder(null);
		this.totalsPanel.setBackground(SystemColor.control);
		GroupLayout gl_sidePanel = new GroupLayout(this.sidePanel);
		gl_sidePanel.setHorizontalGroup(
				gl_sidePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_sidePanel.createSequentialGroup()
						.addGap(3)
						.addGroup(gl_sidePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.unitsPanel, -1, 203, 32767)
								.addGroup(gl_sidePanel.createSequentialGroup()
										.addComponent(this.buildingsPanel, -1, 203, 32767)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
										.addGroup(gl_sidePanel.createSequentialGroup()
												.addComponent(this.researchPanel, -1, 203, 32767)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
												.addGroup(gl_sidePanel.createSequentialGroup()
														.addComponent(this.buttonsPanel, -1, 203, 32767)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
														.addGroup(gl_sidePanel.createSequentialGroup()
																.addComponent(this.totalsPanel, -2, -1, 32767)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
																.addGap(3)));

		gl_sidePanel.setVerticalGroup(
				gl_sidePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_sidePanel.createSequentialGroup()
						.addGap(3)
						.addComponent(this.unitsPanel, -2, 90, -2)
						.addGap(3)
						.addComponent(this.buildingsPanel, -2, 90, -2)
						.addGap(3)
						.addComponent(this.researchPanel, -2, 90, -2)
						.addGap(3)
						.addComponent(this.totalsPanel, -1, 564, 32767)
						.addGap(3)
						.addComponent(this.buttonsPanel, -2, 65, -2)
						.addGap(3)));

		this.totalsLabel = new JLabel("Totals");
		this.textArea = new JTextArea();
		this.textArea.setFont(new Font("Monospaced", 0, 11));
		this.textArea.setBackground(SystemColor.control);
		this.textArea.setLineWrap(true);
		this.textArea.setEditable(false);
		GroupLayout gl_totalsPanel = new GroupLayout(this.totalsPanel);
		gl_totalsPanel.setHorizontalGroup(
				gl_totalsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_totalsPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_totalsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.totalsLabel)
								.addComponent(this.textArea))
								.addContainerGap()));

		gl_totalsPanel.setVerticalGroup(
				gl_totalsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_totalsPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(this.totalsLabel)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(this.textArea, -2, 0, 32767)
						.addGap(3)));

		this.totalsPanel.setLayout(gl_totalsPanel);
		this.loadButton = new JButton("Load");
		this.saveButton = new JButton("Save");
		this.clearButton = new JButton("Clear");
		this.printButton = new JButton("Print");

		GroupLayout gl_buttonsPanel = new GroupLayout(this.buttonsPanel);
		gl_buttonsPanel.setHorizontalGroup(
				gl_buttonsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_buttonsPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_buttonsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(gl_buttonsPanel.createSequentialGroup()
										.addComponent(this.loadButton, -2, 70, -2)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(this.saveButton, -2, 70, -2)
										.addGap(27))
										.addGroup(gl_buttonsPanel.createSequentialGroup()
												.addComponent(this.clearButton, -2, 70, -2)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(this.printButton, -2, 70, -2)))
												.addContainerGap(47, 32767)));

		gl_buttonsPanel.setVerticalGroup(
				gl_buttonsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_buttonsPanel.createSequentialGroup()
						.addGap(6)
						.addGroup(gl_buttonsPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(this.loadButton)
								.addComponent(this.saveButton))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(gl_buttonsPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(this.clearButton)
										.addComponent(this.printButton))
										.addContainerGap(-1, 32767)));

		this.buttonsPanel.setLayout(gl_buttonsPanel);

		this.unitsLabel = new JLabel("Units");
		this.unitsDropDown = new JComboBox(this.model.getUnitOptions());
		this.unitsAddButton = new JButton("Add");
		GroupLayout gl_unitsPanel = new GroupLayout(this.unitsPanel);
		gl_unitsPanel.setHorizontalGroup(
				gl_unitsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_unitsPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_unitsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.unitsLabel)
								.addComponent(this.unitsDropDown, 0, -1, 32767)
								.addComponent(this.unitsAddButton, GroupLayout.Alignment.TRAILING))
								.addContainerGap()));

		gl_unitsPanel.setVerticalGroup(
				gl_unitsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_unitsPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(this.unitsLabel)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(this.unitsDropDown, -2, -1, -2)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(this.unitsAddButton)
						.addContainerGap(20, 32767)));

		this.unitsPanel.setLayout(gl_unitsPanel);

		this.buildingsLabel = new JLabel("Buildings");
		this.buildingsDropDown = new JComboBox(this.model.getBuildingOptions());
		this.buildingsAddButton = new JButton("Add");
		GroupLayout gl_buildingsPanel = new GroupLayout(this.buildingsPanel);
		gl_buildingsPanel.setHorizontalGroup(
				gl_buildingsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_buildingsPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_buildingsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.buildingsLabel)
								.addComponent(this.buildingsDropDown, 0, 208, 32767)
								.addComponent(this.buildingsAddButton, GroupLayout.Alignment.TRAILING))
								.addContainerGap()));

		gl_buildingsPanel.setVerticalGroup(
				gl_buildingsPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_buildingsPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(this.buildingsLabel)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(this.buildingsDropDown, -2, -1, -2)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(this.buildingsAddButton)
						.addContainerGap(-1, 32767)));

		this.buildingsPanel.setLayout(gl_buildingsPanel);

		this.researchLabel = new JLabel("Research");
		this.researchDropDown = new JComboBox(this.model.getResearchOptions());
		this.researchAddButton = new JButton("Add");
		GroupLayout gl_researchPanel = new GroupLayout(this.researchPanel);
		gl_researchPanel.setHorizontalGroup(
				gl_researchPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_researchPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_researchPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.researchLabel)
								.addComponent(this.researchDropDown, 0, -1, 32767)
								.addComponent(this.researchAddButton, GroupLayout.Alignment.TRAILING))
								.addContainerGap()));

		gl_researchPanel.setVerticalGroup(
				gl_researchPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_researchPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(this.researchLabel)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(this.researchDropDown, -2, -1, -2)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(this.researchAddButton)
						.addContainerGap(20, 32767)));

		this.researchPanel.setLayout(gl_researchPanel);

		this.sidePanel.setLayout(gl_sidePanel);
		this.frame.getContentPane().setLayout(groupLayout);
		this.frame.setResizable(true);
		this.frame.setMinimumSize(new Dimension(800, 500));
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		if (dim.width < this.frameWidth) {
			this.frameWidth = dim.width;
		}
		if (dim.height < this.frameHeight) {
			this.frameHeight = dim.height;
		}
		this.frame.setBounds(0, 0, this.frameWidth, this.frameHeight);
		this.frame.setDefaultCloseOperation(3);

		this.frame.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e) {
				TVBO.this.model.setWidth(TVBO.this.viewPanel.getWidth());
				TVBO.this.model.setHeight(TVBO.this.viewPanel.getHeight());
			}
		});
		this.printButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Popup p = new Popup(TVBO.this.model.printBuild());
				p.setVisible(true);
			}
		});
		this.viewPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1){
					if ((e.getX() >= TVBO.this.model.getWidth() - TVBO.this.model.getBorder() - 30) && 
							(e.getY() >= TVBO.this.model.getHeight() - TVBO.this.model.getBorder() + 5) && 
							(e.getX() <= TVBO.this.model.getWidth() - TVBO.this.model.getBorder()) && 
							(e.getY() <= TVBO.this.model.getHeight() - 5)) {
						TVBO.this.model.scroll(30);
						TVBO.this.viewPanel.repaint();
					} else if ((e.getX() >= TVBO.this.model.getWidth() - TVBO.this.model.getBorder() - 65) && 
							(e.getY() >= TVBO.this.model.getHeight() - TVBO.this.model.getBorder() + 5) && 
							(e.getX() <= TVBO.this.model.getWidth() - TVBO.this.model.getBorder() - 35) && 
							(e.getY() <= TVBO.this.model.getHeight() - 5)) {
						TVBO.this.model.scroll(-30);
						TVBO.this.viewPanel.repaint();
					} else if ((e.getX() >= TVBO.this.model.getWidth() - TVBO.this.model.getBorder() - 110) && 
							(e.getY() >= TVBO.this.model.getHeight() - TVBO.this.model.getBorder() + 5) && 
							(e.getX() <= TVBO.this.model.getWidth() - TVBO.this.model.getBorder() - 80) && 
							(e.getY() <= TVBO.this.model.getHeight() - 5)) {
						TVBO.this.model.changeScale(0.1D);
						TVBO.this.viewPanel.repaint();
					} else if ((e.getX() >= TVBO.this.model.getWidth() - TVBO.this.model.getBorder() - 145) && 
							(e.getY() >= TVBO.this.model.getHeight() - TVBO.this.model.getBorder() + 5) && 
							(e.getX() <= TVBO.this.model.getWidth() - TVBO.this.model.getBorder() - 115) && 
							(e.getY() <= TVBO.this.model.getHeight() - 5)) {
						TVBO.this.model.changeScale(-0.1D);
						TVBO.this.viewPanel.repaint();
					} else if (e.isShiftDown()) {
						TVBO.this.model.selectMultipleAction(e.getX(), e.getY());
					} else if (e.isControlDown()) {
						TVBO.this.model.selectAllActions(e.getX(), e.getY());
					}else{
						TVBO.this.model.selectAction(e.getX(), e.getY());
					}
				}
				else if (e.getButton() == 3) {
					TVBO.this.model.rightClick(e.getX(), e.getY());
				} 

				TVBO.this.viewPanel.requestFocus();
				TVBO.this.viewPanel.repaint();
				TVBO.this.textArea.setText(TVBO.this.model.setTotalsText());
			}

			public void mousePressed(MouseEvent e) {

				if(e.getButton()==1){
					if(!e.isShiftDown()){
						TVBO.this.model.selectNoActions();
					}
					TVBO.this.viewPanel.requestFocus();
					TVBO.this.model.startMarquee(e.getX(), e.getY());
				}
			}

			public void mouseReleased(MouseEvent e) {
				TVBO.this.viewPanel.requestFocus();
				TVBO.this.model.endMarquee(e.getX(), e.getY());
				TVBO.this.viewPanel.repaint();
			}
		});
		this.viewPanel.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				TVBO.this.viewPanel.requestFocus();
				TVBO.this.model.updateMarquee(e.getX(), e.getY());
				TVBO.this.viewPanel.repaint();
			}
		});
		this.unitsAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TVBO.this.model.addUnitAction((String)TVBO.this.unitsDropDown.getSelectedItem());
				TVBO.this.model.reset();
				TVBO.this.model.play();
				TVBO.this.textArea.setText(TVBO.this.model.setTotalsText());
				TVBO.this.viewPanel.repaint();
			}
		});
		this.buildingsAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TVBO.this.model.addBuildingAction((String)TVBO.this.buildingsDropDown.getSelectedItem());
				TVBO.this.model.reset();
				TVBO.this.model.play();
				TVBO.this.textArea.setText(TVBO.this.model.setTotalsText());
				TVBO.this.viewPanel.repaint();
			}
		});
		this.researchAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TVBO.this.model.addResearchAction((String)TVBO.this.researchDropDown.getSelectedItem());
				TVBO.this.model.reset();
				TVBO.this.model.play();
				TVBO.this.textArea.setText(TVBO.this.model.setTotalsText());
				TVBO.this.viewPanel.repaint();
			}
		});
		this.saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int r = TVBO.this.fc.showSaveDialog(TVBO.this.frame);
				if (TVBO.this.cwd != null) TVBO.this.fc.setCurrentDirectory(TVBO.this.cwd);
				if (r == 0) {
					File file = TVBO.this.fc.getSelectedFile();
					TVBO.this.cwd = TVBO.this.fc.getCurrentDirectory();
					TVBO.this.model.save(file);
				}
			}
		});
		this.loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int r = TVBO.this.fc.showOpenDialog(TVBO.this.frame);
				if (TVBO.this.cwd != null) TVBO.this.fc.setCurrentDirectory(TVBO.this.cwd);
				if (r == 0) {
					File file = TVBO.this.fc.getSelectedFile();
					TVBO.this.cwd = TVBO.this.fc.getCurrentDirectory();
					TVBO.this.model.load(file);
					TVBO.this.model.reset();
					TVBO.this.model.play();
					TVBO.this.viewPanel.repaint();
				}
			}
		});
		this.clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TVBO.this.model.clear();
				TVBO.this.viewPanel.repaint();
			}
		});
		this.viewPanel.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == 65) || (e.getKeyCode() == 37) || (e.getKeyCode() == 100)) {
					TVBO.this.model.moveSelected(-1, 0);
					TVBO.this.model.reset();
					TVBO.this.model.play();
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 68) || (e.getKeyCode() == 39) || (e.getKeyCode() == 102)) {
					TVBO.this.model.moveSelected(1, 0);
					TVBO.this.model.reset();
					TVBO.this.model.play();
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 87) || (e.getKeyCode() == 38) || (e.getKeyCode() == 104)) {
					TVBO.this.model.moveSelected(0, -1);
					TVBO.this.model.reset();
					TVBO.this.model.play();
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 83) || (e.getKeyCode() == 40) || (e.getKeyCode() == 98)) {
					TVBO.this.model.moveSelected(0, 1);
					TVBO.this.model.reset();
					TVBO.this.model.play();
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 81) || (e.getKeyCode() == 103)) {
					TVBO.this.model.moveSelected(-30, 0);
					TVBO.this.model.reset();
					TVBO.this.model.play();
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 69) || (e.getKeyCode() == 105)) {
					TVBO.this.model.moveSelected(30, 0);
					TVBO.this.model.reset();
					TVBO.this.model.play();
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 82) || (e.getKeyCode() == 101)) {
					TVBO.this.model.moveSelectedToEarliest();
					TVBO.this.model.reset();
					TVBO.this.model.play();
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 127) || (e.getKeyCode() == 8)) {
					TVBO.this.model.deleteAction();
					TVBO.this.model.reset();
					TVBO.this.model.play();
					TVBO.this.viewPanel.repaint();
				} else if (e.getKeyCode() == 78) {
					TVBO.this.model.selectNext();
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 107) || (e.getKeyCode() == 86))
				{
					TVBO.this.model.changeScale(0.1D);
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 109) || (e.getKeyCode() == 67))
				{
					TVBO.this.model.changeScale(-0.1D);
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 90) || (e.getKeyCode() == 97)) {
					TVBO.this.model.scroll(-30);
					TVBO.this.viewPanel.repaint();
				} else if ((e.getKeyCode() == 88) || (e.getKeyCode() == 99)) {
					TVBO.this.model.scroll(30);
					TVBO.this.viewPanel.repaint();
				}
				TVBO.this.textArea.setText(TVBO.this.model.setTotalsText());
			}
		});
	}
}