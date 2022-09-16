package com.fau.csphd.datasecurity;

// importing Java AWT class  
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

// class AWTExample2 directly creates instance of Frame class  
class AWTAssignment4Server extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static int X_LABEL_INIT = 50;
	private static int Y_LABEL_INIT = 50;
	private static int LABEL_WIDTH = 275;
	private static int LABEL_HEIGHT = 20;
	private static int BUTTON_WIDTH = 150;
	private static int BUTTON_HEIGHT = 20;
	private static int X_GAP = 50;
	private static int Y_GAP = 20;
	private static int X_TEXT_INIT = X_LABEL_INIT + LABEL_WIDTH + X_GAP;
	private static int Y_TEXT_INIT = Y_LABEL_INIT;
	private static int TEXT_WIDTH = 200;
	private static int TEXT_HEIGHT = 20;
	JFrame f;
	TextArea lPublicKey;
	TextArea lAlphaGen;
	TextArea lAlphaPublicKey;
	TextArea lPrivateKey;
	Label lKey;
	Label lStatusMessage;
	Assignment4Server eg;
	Choice cKeysize;

	// initializing using constructor
	AWTAssignment4Server() {

		// creating a Frame
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// creating a key size choice
		Label lKeysize = new Label("Select key size (bits)");
		cKeysize = new Choice();
		cKeysize.add("128");
		cKeysize.add("256");
		cKeysize.add("512");
		cKeysize.add("1024");
		cKeysize.add("2048");
		f.add(lKeysize);
		f.add(cKeysize);

		Label lPublic = new Label("Public key");
		f.add(lPublic);
		Label lPrivate = new Label("Private key");
		f.add(lPrivate);
		this.lPublicKey = new TextArea("", 1, 1, TextArea.SCROLLBARS_BOTH);
		f.add(this.lPublicKey);
		this.lAlphaGen = new TextArea("", 1, 1, TextArea.SCROLLBARS_BOTH);
		f.add(this.lAlphaGen);
		this.lAlphaPublicKey = new TextArea("", 1, 1, TextArea.SCROLLBARS_BOTH);
		f.add(this.lAlphaPublicKey);
		this.lPrivateKey = new TextArea("", 1, 1, TextArea.SCROLLBARS_BOTH);
		f.add(this.lPrivateKey);
		Label lLabelKey = new Label("Key received");
		f.add(lLabelKey);
		this.lKey = new Label("");
		f.add(this.lKey);
		lLabelKey.setVisible(false);

		this.lStatusMessage = new Label("");
		f.add(this.lStatusMessage);

		// creating a publish key Button
		Button bPublishKey = new Button("Publish Key");
		bPublishKey.setFocusable(true);
		f.add(bPublishKey);

		// creating a key file dialog
		Button bImportKey = new Button("Import AES Key");
		f.add(bImportKey);
		Button bDecrypt = new Button("Decrypt Message File");
		f.add(bDecrypt);
		bImportKey.setEnabled(false);
		bDecrypt.setEnabled(false);

		// setting position of above components in the frame
		lKeysize.setBounds(X_LABEL_INIT, Y_LABEL_INIT, LABEL_WIDTH, LABEL_HEIGHT);
		cKeysize.setBounds(X_TEXT_INIT, Y_TEXT_INIT, TEXT_WIDTH, TEXT_HEIGHT);

		bPublishKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 1 * LABEL_HEIGHT + 1 * Y_GAP, BUTTON_WIDTH, BUTTON_HEIGHT);
		lPublic.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 2 * LABEL_HEIGHT + 2 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lPublicKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 3 * LABEL_HEIGHT + 2 * Y_GAP, LABEL_WIDTH * 4, LABEL_HEIGHT);
		lAlphaGen.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 4 * LABEL_HEIGHT + 2 * Y_GAP, LABEL_WIDTH * 4, LABEL_HEIGHT);
		lAlphaPublicKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 5 * LABEL_HEIGHT + 2 * Y_GAP, LABEL_WIDTH * 4,
				LABEL_HEIGHT);

		lPrivate.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 6 * LABEL_HEIGHT + 3 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lPrivateKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 7 * LABEL_HEIGHT + 3 * Y_GAP, LABEL_WIDTH * 4, LABEL_HEIGHT);

		bImportKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 8 * LABEL_HEIGHT + 4 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);

		bDecrypt.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 8 * TEXT_HEIGHT + 4 * Y_GAP, BUTTON_WIDTH, BUTTON_HEIGHT);

		lStatusMessage.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 12 * LABEL_HEIGHT + 8 * Y_GAP, LABEL_WIDTH * 3,
				LABEL_HEIGHT);

		bPublishKey.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int len = 0;
				if (eg == null) {
					len = Integer.parseInt(cKeysize.getItem(cKeysize.getSelectedIndex()));

					try {
						eg = new Assignment4Server(len);
					} catch (Exception exp) {
						lStatusMessage.setText(exp.getStackTrace().toString());
					}
				}

				lPublicKey.setText("[" + eg.getPublicKey().toString() + "]");
				lPrivateKey.setText("[" + eg.getPrivateKey().toString() + "]");
				lAlphaGen.setText("[" + eg.getAlphaGenKey().toString() + "]");
				lAlphaPublicKey.setText("[" + eg.getAlphaPublicKey().toString() + "]");

				lStatusMessage.setText("Public & private keys generated successfully");
				cKeysize.setEnabled(false);
				String strKeyFile = "";
				try {
					// creating a file dialog
					FileDialog fileDialog = new FileDialog(f, "Select a file for public key export");
					fileDialog.setVisible(true);
					strKeyFile = fileDialog.getFile();
					if (strKeyFile != null && strKeyFile.length() > 0) {
						strKeyFile = fileDialog.getDirectory() + fileDialog.getFile();
						eg.exportPublicKey(strKeyFile);
						lStatusMessage.setText("Public key exported successfully: " + strKeyFile);
						bPublishKey.setEnabled(false);
						bImportKey.setEnabled(true);
						bDecrypt.setEnabled(true);
					} else {
						lStatusMessage.setText("Please select file to export public key");
					}
				} catch (Exception exp) {
					lStatusMessage.setText(exp.getStackTrace().toString());
				}
			}
		});

		bImportKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String strKeyFile = "";
				try {
					// creating a file dialog
					FileDialog fileDialog = new FileDialog(f, "Select a file for AES key import");
					fileDialog.setVisible(true);
					strKeyFile = fileDialog.getFile();
					if (strKeyFile != null && strKeyFile.length() > 0) {
						strKeyFile = fileDialog.getDirectory() + fileDialog.getFile();
						eg.importSecretKey(strKeyFile);
						lStatusMessage.setText("AES keys imported successfully");
					} else {
						lStatusMessage.setText("Please select AES key file");
					}
				} catch (Exception exp) {
					lStatusMessage.setText(exp.getStackTrace().toString());
				}
			}
		});

		bDecrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String strMessageFile = "";
				try {
					// creating a file dialog
					FileDialog fileDialog = new FileDialog(f, "Select a file to be decrypted");
					fileDialog.setVisible(true);
					strMessageFile = fileDialog.getFile();
					if (strMessageFile != null && strMessageFile.length() > 0) {
						strMessageFile = fileDialog.getDirectory() + fileDialog.getFile();
						strMessageFile = eg.processFile(strMessageFile, 2);
						lStatusMessage.setText("Decrypted file is available: " + strMessageFile);
					} else {
						lStatusMessage.setText("Please select a file for decryption");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					lStatusMessage.setText("Decryption failed - please check AES keys");
					e1.printStackTrace();
				}
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
		AWTAssignment4Server awt_obj = new AWTAssignment4Server();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}