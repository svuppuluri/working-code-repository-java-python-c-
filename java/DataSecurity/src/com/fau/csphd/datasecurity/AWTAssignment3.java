package com.fau.csphd.datasecurity;

// importing Java AWT class  
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.fau.csphd.datasecurity.Assignment3;

import javax.swing.JFrame;

// class AWTExample2 directly creates instance of Frame class  
class AWTAssignment3 extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4005140857085006967L;
	private static int X_LABEL_INIT = 50;
	private static int Y_LABEL_INIT = 50;
	private static int LABEL_WIDTH = 275;
	private static int LABEL_HEIGHT = 20;
	private static int BUTTON_WIDTH = 100;
	private static int BUTTON_HEIGHT = 20;
	private static int X_GAP = 50;
	private static int Y_GAP = 20;
	private static int X_TEXT_INIT = X_LABEL_INIT + LABEL_WIDTH + X_GAP;
	private static int Y_TEXT_INIT = Y_LABEL_INIT;
	private static int TEXT_WIDTH = 200;
	private static int TEXT_HEIGHT = 20;
	JFrame f;
	TextArea taMessage;
	TextArea taCipherText;
	TextArea taPlainText;
	Label lPublic;
	Label lPrivate;
	Label lPublicKey;
	Label lAlphaGen;
	Label lAlphaPublicKey;
	Label lPrivateKey;
	Label lStatusMessage;
	Assignment3 eg;
	Choice cKeysize;

	// initializing using constructor
	AWTAssignment3() {

		// creating a Frame
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// creating a key size choice
		Label lKeysize = new Label("Select key size (bits)");
		cKeysize = new Choice();
		cKeysize.add("16");
		cKeysize.add("24");
		cKeysize.add("32");
		cKeysize.add("48");
		cKeysize.add("64");
		cKeysize.add("96");
		cKeysize.add("128");
		cKeysize.add("192");
		cKeysize.add("256");
		f.add(lKeysize);
		f.add(cKeysize);

		// creating a algorithm choice
		Label lAlgorithm = new Label("Select encryption algorithm");
		Choice cAlgorithm = new Choice();
		cAlgorithm.add("ElGamal");
		f.add(lAlgorithm);
		f.add(cAlgorithm);

		this.lPublic = new Label("Public key");
		f.add(this.lPublic);
		this.lPrivate = new Label("Private key");
		f.add(this.lPrivate);
		this.lPublicKey = new Label("");
		f.add(this.lPublicKey);
		this.lAlphaGen = new Label("");
		f.add(this.lAlphaGen);
		this.lAlphaPublicKey = new Label("");
		f.add(this.lAlphaPublicKey);
		this.lPrivateKey = new Label("");
		f.add(this.lPrivateKey);
		this.lStatusMessage = new Label("");
		f.add(this.lStatusMessage);

		// creating a message text field
		Label lMessage = new Label("Type message:");
		this.taMessage = new TextArea("",3,10,TextArea.SCROLLBARS_BOTH);
		
		f.add(lMessage);
		f.add(this.taMessage);

		// creating a cipher text field
		Label lCipher = new Label("Cipher text");
		f.add(lCipher);
		this.taCipherText = new TextArea("",3,10,TextArea.SCROLLBARS_BOTH);
		f.add(this.taCipherText);

		// creating a plain text field
		Label lPlain = new Label("Plain text");
		f.add(lPlain);
		this.taPlainText = new TextArea("",3,10,TextArea.SCROLLBARS_BOTH);
		f.add(this.taPlainText);

		// creating a encrypt Button
		Button bEncrypt = new Button("Encrypt");
		bEncrypt.setFocusable(true);
		f.add(bEncrypt);

		// creating a decrypt Button
		Button bDecrypt = new Button("Decrypt");
		bDecrypt.setEnabled(false);
		f.add(bDecrypt);

		// setting position of above components in the frame
		lKeysize.setBounds(X_LABEL_INIT, Y_LABEL_INIT, LABEL_WIDTH, LABEL_HEIGHT);
		lAlgorithm.setBounds(X_LABEL_INIT, Y_LABEL_INIT + LABEL_HEIGHT + Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lMessage.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 2 * LABEL_HEIGHT + 2 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		bEncrypt.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 5 * LABEL_HEIGHT + 5 * Y_GAP, BUTTON_WIDTH, BUTTON_HEIGHT);
		lPublic.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 6 * LABEL_HEIGHT + 6 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lPublicKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 7 * LABEL_HEIGHT + 6 * Y_GAP, LABEL_WIDTH * 3, LABEL_HEIGHT);
		lAlphaGen.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 8 * LABEL_HEIGHT + 6 * Y_GAP, LABEL_WIDTH * 3, LABEL_HEIGHT);
		lAlphaPublicKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 9 * LABEL_HEIGHT + 6 * Y_GAP, LABEL_WIDTH * 3, LABEL_HEIGHT);
		lPrivate.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 9 * LABEL_HEIGHT + 8 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lPrivateKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 9 * LABEL_HEIGHT + 9 * Y_GAP, LABEL_WIDTH * 3, LABEL_HEIGHT);
		lStatusMessage.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 18 * LABEL_HEIGHT + 18 * Y_GAP, LABEL_WIDTH * 2,
				LABEL_HEIGHT);
		lCipher.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 10 * LABEL_HEIGHT + 10 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lPlain.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 14 * LABEL_HEIGHT + 14 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);

		cKeysize.setBounds(X_TEXT_INIT, Y_TEXT_INIT, TEXT_WIDTH, TEXT_HEIGHT);
		cAlgorithm.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 1 * TEXT_HEIGHT + 1 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);
		taMessage.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 2 * TEXT_HEIGHT + 2 * Y_GAP, TEXT_WIDTH * 5, TEXT_HEIGHT * 5);
		bDecrypt.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 5 * TEXT_HEIGHT + 5 * Y_GAP, BUTTON_WIDTH, BUTTON_HEIGHT);
		taCipherText.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 10 * TEXT_HEIGHT + 10 * Y_GAP, TEXT_WIDTH * 5, TEXT_HEIGHT * 5);
		taPlainText.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 14 * TEXT_HEIGHT + 14 * Y_GAP, TEXT_WIDTH * 5, TEXT_HEIGHT * 5);

		bEncrypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int len = 0;
				if (eg == null) {
					len = Integer.parseInt(cKeysize.getItem(cKeysize.getSelectedIndex()));

					try {
						eg = new Assignment3(len);
					} catch (Exception exp) {
						lStatusMessage.setText(exp.getStackTrace().toString());
					}
				}

				lPublicKey.setText("["+eg.getPublicKey().toString()+"]");
				lPrivateKey.setText("["+eg.getPrivateKey().toString()+"]");
				lAlphaGen.setText("["+eg.getAlphaGenKey().toString()+"]");
				lAlphaPublicKey.setText("["+eg.getAlphaPublicKey().toString()+"]");
				taCipherText.setText(eg.bigEncrypt(taMessage.getText()));

				taPlainText.setVisible(false);
				
				lStatusMessage.setText("Encrypted successfully");
				bDecrypt.setEnabled(true);
				bDecrypt.setFocusable(true);
				cKeysize.setEnabled(false);
				cAlgorithm.setEnabled(false);
			}
		});

		bDecrypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					taPlainText.setText(eg.bigDecrypt(taCipherText.getText()));
					taPlainText.setVisible(true);
				} catch (Exception exp) {
					lStatusMessage.setText(exp.getStackTrace().toString());
				}
				lStatusMessage.setText("Decrypted successfully");
				bDecrypt.setEnabled(false);
			}
		});

		// frame size 300 width and 300 height
		f.setSize(1400, 1000);

		// setting the title of frame
		f.setTitle("ElGamal Encrypt & Decrypt Dialog");

		// no layout
		f.setLayout(null);

		// setting visibility of frame
		f.setVisible(true);
	}

	// main method
	public static void main(String args[]) {

		// creating instance of JFrame class
		AWTAssignment3 awt_obj = new AWTAssignment3();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}