package com.fau.csphd.datasecurity;

// importing Java AWT class  
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.fau.csphd.datasecurity.Assignment2;

import javax.crypto.Cipher;
import javax.swing.JFrame;

// class AWTExample2 directly creates instance of Frame class  
class AWTAssignment2 extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4005140857085006967L;
	private static int X_LABEL_INIT = 150;
	private static int Y_LABEL_INIT = 150;
	private static int LABEL_WIDTH = 275;
	private static int LABEL_HEIGHT = 20;
	private static int X_GAP = 50;
	private static int Y_GAP = 20;
	private static int X_TEXT_INIT = X_LABEL_INIT + LABEL_WIDTH + X_GAP;
	private static int Y_TEXT_INIT = Y_LABEL_INIT;
	private static int TEXT_WIDTH = 200;
	private static int TEXT_HEIGHT = 20;
	JFrame f;
	TextField tfKeyword;
	Label lFileStatus;
	Label lStatusMessage;
	Assignment2 enc;
	Choice cMode;
	String strOperationMode = "";
	String strKeyword = "";
	String strFile = "";
	String strOutputFilename = "";

	// initializing using constructor
	AWTAssignment2() {

		// creating a Frame
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// creating a key size choice
		Label lKeysize = new Label("Select key size");
		Choice cKeysize = new Choice();
		cKeysize.add("128 bits");
		// cKeysize.add("192 bits");
		// cKeysize.add("256 bits");
		f.add(lKeysize);
		f.add(cKeysize);

		// creating a algorithm choice
		Label lAlgorithm = new Label("Select data encryption algorithm");
		Choice cAlgorithm = new Choice();
		cAlgorithm.add("AES");
		f.add(lAlgorithm);
		f.add(cAlgorithm);

		// creating a mode of operation choice
		Label lMode = new Label("Select mode of operation");
		this.cMode = new Choice();
		cMode.add("ECB (Electronic Code Book)");
		cMode.add("CBC (Cipher Block Chaining)");
		cMode.add("CFB (Cipher FeedBack)");
		cMode.add("OFB (Output FeedBack)");
		cMode.add("CTR (Counter)");
		cMode.add("GCM (Galois/Counter Mode)");
		f.add(lMode);
		f.add(cMode);

		// creating a keyword text field
		Label lKeyword = new Label("Password for AES key generation");
		this.tfKeyword = new TextField();
		f.add(lKeyword);
		f.add(tfKeyword);

		// creating a message text field
		Label lStatusLabel = new Label("Status");
		f.add(lStatusLabel);
		lStatusMessage = new Label();
		f.add(lStatusMessage);

		// creating a file dialog
		Label lFileDialog = new Label("Select a file to be processed (encrypt / decrypt)");
		lFileStatus = new Label();
		f.add(lFileDialog);
		Button bFileDialog = new Button("Open file");
		f.add(lFileStatus);
		f.add(bFileDialog);
		final FileDialog fileDialog = new FileDialog(f, "Select file");

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
		lMode.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 2 * LABEL_HEIGHT + 2 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lKeyword.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 3 * LABEL_HEIGHT + 3 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lFileDialog.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 4 * LABEL_HEIGHT + 5 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lFileStatus.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 5 * LABEL_HEIGHT + 6 * Y_GAP, LABEL_WIDTH * 3, LABEL_HEIGHT);
		bEncrypt.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 6 * LABEL_HEIGHT + 7 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lStatusMessage.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 8 * LABEL_HEIGHT + 8 * Y_GAP, LABEL_WIDTH * 4,
				LABEL_HEIGHT);

		cKeysize.setBounds(X_TEXT_INIT, Y_TEXT_INIT, LABEL_WIDTH, LABEL_HEIGHT);
		cAlgorithm.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 1 * TEXT_HEIGHT + 1 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);
		cMode.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 2 * TEXT_HEIGHT + 2 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);
		tfKeyword.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 3 * TEXT_HEIGHT + 3 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);
		bFileDialog.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 4 * TEXT_HEIGHT + 5 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);
		bDecrypt.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 6 * TEXT_HEIGHT + 7 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);

		bEncrypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (enc == null) {

					if (tfKeyword.getText() == null || tfKeyword.getText().length() == 0) {
						strKeyword = "DataSecurity";
						tfKeyword.setText(strKeyword);
					} else {
						strKeyword = tfKeyword.getText();
					}
					
					tfKeyword.setEnabled(false);

					switch (cMode.getItem(cMode.getSelectedIndex())) {
					case "ECB (Electronic Code Book)":
						strOperationMode = "AES/ECB/PKCS5Padding";
						break;
					case "CBC (Cipher Block Chaining)":
						strOperationMode = "AES/CBC/PKCS5Padding";
						break;
					case "CFB (Cipher FeedBack)":
						strOperationMode = "AES/CFB/NoPadding";
						break;
					case "OFB (Output FeedBack)":
						strOperationMode = "AES/OFB/NoPadding";
						break;
					case "CTR (Counter)":
						strOperationMode = "AES/CTR/NoPadding";
						break;
					case "GCM (Galois/Counter Mode)":
						strOperationMode = "AES/GCM/NoPadding";
						break;
					default:
						strOperationMode = "AES/CBC/PKCS5Padding";
						break;
					}
					
					try {
						enc = new Assignment2(strOperationMode, strKeyword);
					} catch (Exception exp) {
						lStatusMessage.setText(exp.getStackTrace().toString());
					}
					
				}
				
				try {
					strOutputFilename = enc.processFile(strFile, 1);
				} catch (Exception exp) {
					lStatusMessage.setText(exp.getStackTrace().toString());
				}
				
				lStatusMessage.setText("Encrypted successfully. Output is available in: " + strOutputFilename);
				strFile = strOutputFilename;
				lFileStatus.setText("File Selected: " + strFile);
				bDecrypt.setEnabled(true);
				bDecrypt.setFocusable(true);
				cMode.setEnabled(false);
				cAlgorithm.setEnabled(false);
				cKeysize.setEnabled(false);
			}
		});

		bDecrypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					strOutputFilename = enc.processFile(strFile, 2);
				} catch (Exception exp) {
					lStatusMessage.setText(exp.getStackTrace().toString());
				}
				lStatusMessage.setText("Decrypted successfully. Output is available in: " + strOutputFilename);
				strFile = strOutputFilename;
				lFileStatus.setText("File Selected: " + strFile);
				bDecrypt.setEnabled(false);
			}
		});

		bFileDialog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileDialog.setVisible(true);
				strFile = fileDialog.getDirectory() + fileDialog.getFile();
				lFileStatus.setText("File Selected: " + strFile);
				lStatusMessage.setText("");
			}
		});

		// frame size 300 width and 300 height
		f.setSize(1200, 800);

		// setting the title of frame
		f.setTitle("AES Encrypt & Decrypt Dialog");

		// no layout
		f.setLayout(null);

		// setting visibility of frame
		f.setVisible(true);
	}

	// main method
	public static void main(String args[]) {

		// creating instance of JFrame class
		AWTAssignment2 awt_obj = new AWTAssignment2();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}