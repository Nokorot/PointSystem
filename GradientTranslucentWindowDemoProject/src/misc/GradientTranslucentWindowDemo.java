/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
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

package misc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;

public class GradientTranslucentWindowDemo extends JFrame {
    public GradientTranslucentWindowDemo() {
        super("GradientTranslucentWindow");

        setBackground(new Color(0,0,0,0));
        setSize(new Dimension(300,200));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BufferedImage plane = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) plane.getGraphics();
        
        Paint p =
                new GradientPaint(0.0f, 0.0f, new Color(240, 240, 240, 0),
                    0.0f, getHeight(), new Color(240, 240, 240, 255), true);
	    g.setPaint(p);
	    g.fillRect(0, 0, getWidth(), getHeight());
        
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
//            	super.paintComponent(g);
//            	 if (true) {
//                     Graphics2D g2=(Graphics2D) g ;
//
//                     Color[] c = new Color[]{Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, 
//                              Color.MAGENTA, Color.WHITE, Color.ORANGE, Color.PINK};
//                     for(int i=0; i<8; ++i){
//                         g2.setColor(c[i]);
//	                     int start_angle=i*45;
//	                     g2.fillArc(150-100, 100-100, 200, 200, start_angle,45);
//	            	 }
//                     
//                     return;
//                 }
//            	
//            	g.clearRect(0, 0, getWidth(), getHeight());
            	
            	g.drawImage(plane, 0, 0, null);
            	
            	
            	
//                if (g instanceof Graphics2D) {
//                    final int R = 240;
//                    final int G = 240;
//                    final int B = 240;
//
//                    Paint p =
//                        new GradientPaint(0.0f, 0.0f, new Color(R, G, B, 0),
//                            0.0f, getHeight(), new Color(R, G, B, 255), true);
//                    Graphics2D g2d = (Graphics2D)g;
//                    g2d.setPaint(p);
//                    g2d.fillRect(0, 0, getWidth(), getHeight());
//                }
            }
        };
        setContentPane(panel);
        setLayout(new GridBagLayout());
        JButton b;
        add(b = new JButton("I am a Button"));
        b.addActionListener((ActionEvent e) -> {
			setVisible(false);
			setVisible(true);
		});
    }

    public static void main(String[] args) {
        // Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        boolean isPerPixelTranslucencySupported = 
            gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);

        //If translucent windows aren't supported, exit.
        if (!isPerPixelTranslucencySupported) {
            System.out.println(
                "Per-pixel translucency is not supported");
            System.exit(0);
        }

        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(() -> {
            GradientTranslucentWindowDemo gtw = new GradientTranslucentWindowDemo();

            // Display the window.
            gtw.setVisible(true);
        });
    }
}
