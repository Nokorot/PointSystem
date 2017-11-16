/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package dnd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/*
 * DropDemo.java requires the following file:
 *     ListTransferHandler.java
 */
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel;

public class DropDemo extends JPanel {
	private static final long serialVersionUID = 1L;

	public DropDemo() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		add(createLabel());
	}

	private JPanel createLabel() {
		JLabel l = new JLabel();
		l.setBackground(Color.DARK_GRAY);
		l.setOpaque(true);
		l.setPreferredSize(new Dimension(500, 300));
		l.setDropTarget(new DropTarget(l, new DropTargetAdapter() {
			public void drop(DropTargetDropEvent e) {
				try {
					Transferable t = e.getTransferable();

					if (e.isDataFlavorSupported(DataFlavor.getTextPlainUnicodeFlavor())) {
						e.acceptDrop(e.getDropAction());

						StringBuffer sb = new StringBuffer();
						InputStream is = (InputStream) t.getTransferData(DataFlavor.getTextPlainUnicodeFlavor());
						int i;
						while ((i = is.read()) != -1)
							if (i != 0)
								sb.append((char) i);
						
						System.out.println(new String(sb));

						e.dropComplete(true);
					} else if (e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
						e.acceptDrop(e.getDropAction());

						String s = (String) t.getTransferData(DataFlavor.stringFlavor);

						System.out.println(new String(s));

						e.dropComplete(true);
					} else{
						e.rejectDrop();
					}
				} catch (java.io.IOException e2) {
				} catch (UnsupportedFlavorException e2) {
				}

			}
		}));

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(l, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createTitledBorder("Drop area"));
		return panel;
	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("DropDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new DropDemo();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);

				try {
					// Set cross-platform Java L&F (also called "Metal")
					UIManager.setLookAndFeel(
							// UIManager.getCrossPlatformLookAndFeelClassName()
							// UIManager.getSystemLookAndFeelClassName()
							// "javax.swing.plaf.metal.MetalLookAndFeel"
							// "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
							// "javax.swing.plaf.nimbus.NimbusLookAndFeel"
							new SyntheticaBlackMoonLookAndFeel()
//							new SyntheticaBlackEyeLookAndFeel()
							);
				} catch (UnsupportedLookAndFeelException e) {
					// handle exception
				} catch (ParseException e) {
					e.printStackTrace();
				}

				createAndShowGUI();
			}
		});
	}
}
