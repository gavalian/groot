/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.studio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jlab.groot.base.GStyle;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.data.DatasetOperations;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.EmbeddedCanvasTabbed;
import org.jlab.groot.tree.DynamicTree;
import org.jlab.groot.tree.Tree;
import org.jlab.groot.tree.TreeAnalyzer;
import org.jlab.groot.tree.TreeFile;
import org.jlab.groot.ui.CutPanel;
import org.jlab.groot.ui.DescriptorPanel;
import org.jlab.groot.ui.TreeEditor;
import org.jlab.groot.tree.TreeTextFile;

/**
 *
 * @author gavalian
 */
public class StudioUI implements MouseListener, ActionListener {

	JSplitPane splitPane = null;
	JPanel navigationPane = null;
	EmbeddedCanvas drawCanvas = null;
        EmbeddedCanvasTabbed drawCanvasTabbed = null;
        
	JFrame frame = null;
	Tree studioTree = null;

	JTree jtree = null;
	JTree jtreeAnalyzer = null;

	JPanel studioPane = null;
	JPanel statusPane = null;
	JMenuBar menuBar = null;
	StudioToolBar toolBar = null;
	TreeAnalyzer analyzer = new TreeAnalyzer();
	Boolean previewMode = true;
	int previewEvents = 1000;
	JSplitPane secondSplitPane = null;
	JSplitPane thirdSplitPane = null;
	JScrollPane scrollPane = null;
	JCheckBoxMenuItem menuPreviewMode;

	public StudioUI(Tree tree) {
		frame = new JFrame("GROOT Studio");
                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        if (JOptionPane.showConfirmDialog(frame, 
                                "Are you sure to close this window?", "Really Closing?", 
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                            frame.setVisible(false);
                            //System.exit(0);
                        }
                    }
                });
		studioTree = tree;
		frame.setMinimumSize(new Dimension(300, 300));
		initUI();

		frame.pack();
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int) (screensize.getHeight() * .75 * 1.618), (int) (screensize.getHeight() * .75));
		splitPane.setDividerLocation(0.4);
		secondSplitPane.setDividerLocation(0.4);
		thirdSplitPane.setDividerLocation(0.2);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void initUI() {

		studioPane = new JPanel();
		studioPane.setLayout(new BorderLayout());

		initMenu();

		splitPane = new JSplitPane();
		navigationPane = new JPanel();
		navigationPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		JPanel canvasPane = new JPanel();
		canvasPane.setLayout(new BorderLayout());
		canvasPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		drawCanvas = new EmbeddedCanvas();// 500,500,2,2);
                drawCanvasTabbed = new EmbeddedCanvasTabbed();
                
		canvasPane.add(drawCanvasTabbed, BorderLayout.CENTER);
		splitPane.setRightComponent(canvasPane);


		//DefaultMutableTreeNode top = studioTree.getTree();
		DynamicTree top = studioTree.getTree();
		//jtree = new JTree(top);
		top.getTree().addMouseListener(this);
		//top.setMinimumSize(new Dimension(100, 50));
		
		//DefaultMutableTreeNode topa = analyzer.getTree();
		//jtreeAnalyzer = new JTree(topa);
		scrollPane = new JScrollPane(top);
		secondSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		thirdSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		secondSplitPane.setTopComponent(scrollPane);
		//DynamicTree tree = new DynamicTree();
		//JScrollPane scrollPane2 = new JScrollPane(jtreeAnalyzer);
		/*JScrollPane scrollPane2 = new JScrollPane(tree);
		DefaultMutableTreeNode node = tree.addObject("Tree");
		tree.addObject(node,"tree1");
		tree.addObject(node,"tree2");
		tree.addObject(node,"tree3");*/
		thirdSplitPane.setTopComponent(studioTree.getSelector().getTree());
		thirdSplitPane.setBottomComponent(analyzer.getTree());
		analyzer.getTree().getTree().addMouseListener(new MouseListener(){
		
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Mouse clicked!");
				if (e.getClickCount() == 2) {
					TreePath path = analyzer.getTree().getTree().getPathForLocation(e.getX(), e.getY());
					if (path != null) {
						System.out.println(path.getLastPathComponent().toString());
						for(int i=0; i<analyzer.getDescriptors().size(); i++){
							if(analyzer.getDescriptors().get(i).getDescName() == path.getLastPathComponent().toString()){
								System.out.println(e.getModifiers());
								if((e.getModifiers() & InputEvent.SHIFT_MASK) != 0){
									System.out.println("Drawing same");
									drawCanvasTabbed.getCanvas().draw(analyzer.getDescriptors().get(i).getDataSet(),"same");
									drawCanvasTabbed.getCanvas().update();
								}else{
									drawCanvasTabbed.getCanvas().drawNext(analyzer.getDescriptors().get(i).getDataSet());
									drawCanvasTabbed.getCanvas().update();
								}
							}
						}
						//scanTreeItem(path.getLastPathComponent().toString());
						//String cutString = path.getLastPathComponent().toString();
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});


		secondSplitPane.setBottomComponent(thirdSplitPane);
		

		splitPane.setLeftComponent(secondSplitPane);
		splitPane.setDividerLocation(0.5);
		studioPane.add(splitPane, BorderLayout.CENTER);
		frame.add(studioPane);
	}

	private void initMenu() {
		statusPane = new JPanel();
		statusPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;

		// statusPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		JPanel statusPane1 = new JPanel();
		JPanel statusPane2 = new JPanel();
		JPanel statusPane3 = new JPanel();
		statusPane1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		statusPane2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		statusPane3.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		statusPane1.add(new JLabel("Status:"));
		statusPane2.add(new JLabel("Processed:"));
		statusPane3.add(new JLabel("Memory:"));
		statusPane.add(statusPane1, c);
		statusPane.add(statusPane2, c);
		statusPane.add(statusPane3, c);

		studioPane.add(statusPane, BorderLayout.PAGE_END);

		// JToolBar toolBar = new JToolBar("");
		// toolBar.add(new Button("Divide"));
		// toolBar.add(new Button("Add"));
		toolBar = new StudioToolBar(this);

		menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenuItem newStudioWindow = new JMenuItem("New Window");
		JMenuItem menuFileOpen = new JMenuItem("Import ASCII File...");
		JMenuItem menuFileOpenHipo = new JMenuItem("Open HIPO File...");
		JMenuItem newHistogram = new JMenuItem("New Histogram...");
		JMenuItem newHistogram2D = new JMenuItem("New 2D Histogram...");
		JMenuItem newGraphErrors = new JMenuItem("New GraphErrors...");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem closeWindow = new JMenuItem("Close Window");

		JMenu menuEdit = new JMenu("Edit");
		menuPreviewMode = new JCheckBoxMenuItem("Preview Mode");
                JMenuItem menuOperations  = new JMenuItem("Operations");
                menuOperations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
                            datasetOperationDialog();
			}
		});
                
                
                JMenu menuRun = new JMenu("Run");
                JMenuItem menuItemPlay = new JMenuItem("Play");
                menuItemPlay.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
                            processPlay();
			}
		});
                menuRun.add(menuItemPlay);
                
		menuEdit.add(menuPreviewMode);
                menuEdit.add(menuOperations);
                
		menuPreviewMode.setSelected(previewMode);
		menuPreviewMode.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menuPreviewMode.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				previewMode = menuPreviewMode.isSelected();
				toolBar.fastButton.setSelected(previewMode);
			}
		});

		JMenu menuHelp = new JMenu("Help");
		JMenuItem about = new JMenuItem("About...");
		menuHelp.add(about);
		about.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null,
						"GROOT Documentation\n https://github.com/gavalian/groot/wiki\n  Bug Reporting:\n https://github.com/gavalian/groot/issues\n Gagik Gavalian and Will Phelps\n gavalian@jlab.org , wphelps@jlab.org",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		closeWindow.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		closeWindow.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				frame.dispose();
			}
		});
		newStudioWindow.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		newStudioWindow.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				new StudioUI(studioTree);
			}
		});

		menuFileOpen.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menuFileOpen.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				String file = chooseFile("Select ASCII File to Open", true);
				openASCIIFile(file);
			}
		});

		menuFileOpenHipo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menuFileOpenHipo.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				String file = chooseFile("Select HIPO File to Open", true);
				openHipoFile(file);
			}
		});

		exit.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		exit.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		newHistogram.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		newHistogram.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				addDescriptor(1);
			}
		});

		newHistogram2D.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				addDescriptor(2);
			}
		});

		newGraphErrors.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
				addDescriptor(3);
			}
		});

		menuFile.add(newStudioWindow);
		menuFile.add(new JSeparator());
		menuFile.add(menuFileOpen);
		menuFile.add(menuFileOpenHipo);
		menuFile.add(new JSeparator());
		menuFile.add(newHistogram);
		menuFile.add(newHistogram2D);
		menuFile.add(newGraphErrors);
		menuFile.add(new JSeparator());
		menuFile.add(closeWindow);
		menuFile.add(exit);

		menuBar.add(menuFile);
		menuBar.add(menuEdit);
                menuBar.add(menuRun);
		menuBar.add(menuHelp);
		studioPane.add(toolBar.getToolBar(), BorderLayout.PAGE_START);
		frame.setJMenuBar(menuBar);
	}
	public void openHipoFile(String file) {
		if (file != null) {
			System.out.println("Open new hipo File:" + file);
			TreeFile tree = new TreeFile("HipoTree");
			tree.openFile(file);
			new StudioUI(tree);
		}
	}
	public void openASCIIFile(String file) {
		if (file != null) {
			System.out.println("Open new ASCII File:" + file);
			TreeTextFile tree = new TreeTextFile("TextTree");
			tree.readFile(file);
			new StudioUI(tree);
		}
	}
	public void createNewGraphErrors() {
		System.out.println("Create new graph errors");
	}
	public void createNewHistogram() {
		System.out.println("Create new Histogram");

	}
	public void createNewHistogram2D() {
		System.out.println("Create new Histogram 2D");
	}
	public void datasetOperationDialog() {
		JDialog dialog = DatasetOperations.createOperations(frame, drawCanvasTabbed.getCanvas().getObjectMap());
        dialog.setVisible(true);
        System.out.println("Finished operation");
        List<IDataSet> results =  ((DatasetOperations) dialog.getContentPane()).getResults();
        for(int i = 0; i < results.size(); i++){
            drawCanvasTabbed.getCanvas().drawNext(results.get(i));
        }				
	}

	public String chooseFile(String name, boolean open) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(chooser.getCurrentDirectory());
		chooser.setDialogTitle(name);
		chooser.setAcceptAllFileFilterUsed(true);

		if (open) {
			if (chooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
				return chooser.getSelectedFile().toString();
			} else {
				return null;
			}
		} else {
			if (chooser.showSaveDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
				return chooser.getSelectedFile().toString();
			} else {
				return null;
			}
		}
	}

	public H1F scanTreeItem(String item) {
		if (this.studioTree.hasBranch(item) == true) {
			// List<Double> vector =
			// studioTree.getVector(item,studioTree.getSelector());
			System.out.println("getting vector for item = " + item);
			DataVector vec;
			if (this.previewMode) {
				vec = studioTree.getDataVector(item, "", previewEvents);
			} else {
				vec = studioTree.getDataVector(item, "");
			}

			System.out.println("result = " + vec.getSize());
			H1F h1d = H1F.create(item, 100, vec);
			h1d.setTitle(item);
			h1d.setTitleX(item);
			h1d.setTitleY("Entries");
			return h1d;
			//h1d.setOptStat(11111);
			//h1d.setLineColor(1);
			//h1d.setFillColor(43);
		}
		return null;
	}

        public void processPlay(){
            System.out.println("---> Replaying all the descriptors from the tree");
            if(this.previewMode==true){
                this.analyzer.process(studioTree, 1000);
            } else {
                this.analyzer.process(studioTree);
            }
            System.out.println("---> done replaying");
        }
        
	public boolean isTree(String item) {
		return this.studioTree.getName() == item;
	}

	public void addCut() {
		System.out.println("doing some stuff...");
		CutPanel cutPane = new CutPanel(studioTree);
		JFrame frame = new JFrame("Cut Editor");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(cutPane);
		frame.pack();
		frame.setLocationRelativeTo(this.frame);
		frame.setMinimumSize(frame.getSize());
		frame.setVisible(true);
	}

	public void addDescriptor(int dim) {
		// panel = new DescriptorPanel(studioTree,analyzer,2);
		JFrame frame = new JFrame("Edit Histogram");
		DescriptorPanel panel = new DescriptorPanel(studioTree, analyzer, dim);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(this.frame);
		frame.setMinimumSize(frame.getSize());
		frame.setVisible(true);
	}

	/*
	 * public void fillDescriptors(){ this.analyzer. }
	 */

	/*public void updateTree() {
		DefaultTreeModel model = new DefaultTreeModel(studioTree.getTree());
		this.jtree.setModel(model);
		DefaultTreeModel modelAnalyzer = new DefaultTreeModel(this.analyzer.getTree());
		this.jtreeAnalyzer.setModel(modelAnalyzer);
	}*/

	public static void main(String[] args) {
		GStyle.getGraphErrorsAttributes().setMarkerStyle(0);
		GStyle.getGraphErrorsAttributes().setMarkerColor(3);
		GStyle.getGraphErrorsAttributes().setMarkerSize(7);
		GStyle.getGraphErrorsAttributes().setLineColor(3);
		GStyle.getGraphErrorsAttributes().setLineWidth(2);
		GStyle.getFunctionAttributes().setLineWidth(6);
		GStyle.getAxisAttributesX().setTitleFontSize(32);
		GStyle.getAxisAttributesX().setLabelFontSize(28);
		GStyle.getAxisAttributesY().setTitleFontSize(32);
		GStyle.getAxisAttributesY().setLabelFontSize(28);
		GStyle.getH1FAttributes().setFillColor(44);
		GStyle.getH1FAttributes().setOptStat("1110");
		TreeTextFile tree = new TreeTextFile("TextTree");
		// tree.readFile("/Users/gavalian/Desktop/pp_10k.txt");
		tree.readFile("src/main/resources/sample_data/studio_data/pp_10k_wlab.txt");
		// StudioUI sui = new StudioUI(new RandomTree());
		StudioUI sui = new StudioUI(tree);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println("Mouse clicked!");
		if (e.getClickCount() == 2) {
			//System.out.println("Mouse Double clicked!");
			TreePath path = this.studioTree.getTree().getTree().getPathForLocation(e.getX(), e.getY());
			if (path != null) {
				System.out.println(path.getLastPathComponent().toString());
				H1F histogram = scanTreeItem(path.getLastPathComponent().toString());
					if(histogram!=null){
					if((e.getModifiers() & InputEvent.SHIFT_MASK) != 0){
						System.out.println("Drawing same");
						drawCanvasTabbed.getCanvas().draw(histogram,"same");
						drawCanvasTabbed.getCanvas().update();
					}else{
						drawCanvasTabbed.getCanvas().drawNext(histogram);
						drawCanvasTabbed.getCanvas().update();
					}
				}
				String cutString = path.getLastPathComponent().toString();
				if (isTree(path.getLastPathComponent().toString())) {
					JFrame editorFrame = new JFrame("Tree Editor:" + path.getLastPathComponent().toString());
					TreeEditor editor = new TreeEditor(this.studioTree);
					editorFrame.add(editor);
					editorFrame.pack();
					editorFrame.setLocationRelativeTo(this.frame);
					editorFrame.setVisible(true);
				}

				/*
				 * if(path.getLastPathComponent() instanceof Tree){
				 * //path.getLastPathComponent()); }
				 */

				// if(cutString.contains("Selector")==true){
				// addCut();
				// }
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// this.updateTree();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// this.updateTree();

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action appeared = " + e.getActionCommand());
		if (e.getActionCommand().compareTo("Add Descriptor") == 0) {
			this.addDescriptor(1);
		}
		if (e.getActionCommand().compareTo("Add Cut") == 0) {
			this.addCut();
		}
		if (e.getActionCommand().compareTo("Add GraphErrors") == 0) {
			this.addDescriptor(3);
		}
		if (e.getActionCommand().compareTo("Play") == 0) {
			this.processPlay();
		}
		if (e.getActionCommand().compareTo("Preview Mode") == 0) {
			this.previewMode = this.toolBar.fastButton.isSelected();
			menuPreviewMode.setSelected(previewMode);
		}
		
		if (e.getActionCommand().compareTo("Import ASCII") == 0) {
			this.openASCIIFile(this.chooseFile("Import ASCII File", true));
		}
		if (e.getActionCommand().compareTo("Action Jack") == 0) {
			datasetOperationDialog();
		}
	}
}
