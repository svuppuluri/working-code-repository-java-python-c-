package com.fau.csphd.datasecurity;

// importing Java AWT class  
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

// class AWTExample2 directly creates instance of Frame class  
class AWTAssignment4Client extends JFrame implements ActionListener {
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
	Label lFileDialog;
	Label lStatusMessage;
	Assignment4Client enc;
	Choice cMode;
	String strOperationMode = "";
	String strKeyword = "";
	String strFile = "";
	String strOutputFilename = "";
	String strMessageFile = "";
	String strKeyFile = "";
	Label lPublicKey;
	TextField tfPublicKey;
	TextField tfAlphaGen;
	TextField tfAlphaPublicKey;

	// initializing using constructor
	AWTAssignment4Client() {

		// creating a Frame
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// creating a key size choice
		Label lKeysize = new Label("Select key size (bits)");
		Choice cKeysize = new Choice();
		cKeysize.add("128");
		cKeysize.add("192");
		cKeysize.add("256");
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

		// creating a encrypt Button
		Button bTransmit = new Button("Transmit File Securely");
		bTransmit.setFocusable(true);
		bTransmit.setEnabled(false);
		f.add(bTransmit);

		// creating a encrypt Button
		Button bPublicKey = new Button("Import Public Key");
		bPublicKey.setFocusable(true);
		f.add(bPublicKey);
		lPublicKey = new Label("");
		tfPublicKey = new TextField("");
		tfAlphaGen = new TextField("");
		tfAlphaPublicKey = new TextField("");
		tfPublicKey.setEnabled(false);
		tfAlphaGen.setEnabled(false);
		tfAlphaPublicKey.setEnabled(false);
		tfPublicKey.setVisible(false);
		tfAlphaGen.setVisible(false);
		tfAlphaPublicKey.setVisible(false);
		f.add(lPublicKey);
		f.add(tfPublicKey);
		f.add(tfAlphaGen);
		f.add(tfAlphaPublicKey);

		// creating a decrypt Button
		Button bAESKey = new Button("Export AES Key");
		bAESKey.setEnabled(false);
		f.add(bAESKey);

		// setting position of above components in the frame
		lKeysize.setBounds(X_LABEL_INIT, Y_LABEL_INIT, LABEL_WIDTH, LABEL_HEIGHT);
		lAlgorithm.setBounds(X_LABEL_INIT, Y_LABEL_INIT + LABEL_HEIGHT + Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lMode.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 2 * LABEL_HEIGHT + 2 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lKeyword.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 3 * LABEL_HEIGHT + 3 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		bPublicKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 4 * LABEL_HEIGHT + 4 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		lPublicKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 5 * LABEL_HEIGHT + 5 * Y_GAP, LABEL_WIDTH, LABEL_HEIGHT);
		tfPublicKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 6 * LABEL_HEIGHT + 5 * Y_GAP, LABEL_WIDTH * 3, LABEL_HEIGHT);
		tfAlphaGen.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 7 * LABEL_HEIGHT + 5 * Y_GAP, LABEL_WIDTH * 3, LABEL_HEIGHT);
		tfAlphaPublicKey.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 8 * LABEL_HEIGHT + 5 * Y_GAP, LABEL_WIDTH * 3,
				LABEL_HEIGHT);
		lStatusMessage.setBounds(X_LABEL_INIT, Y_LABEL_INIT + 8 * LABEL_HEIGHT + 8 * Y_GAP, LABEL_WIDTH * 4,
				LABEL_HEIGHT);

		cKeysize.setBounds(X_TEXT_INIT, Y_TEXT_INIT, LABEL_WIDTH, LABEL_HEIGHT);
		cAlgorithm.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 1 * TEXT_HEIGHT + 1 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);
		cMode.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 2 * TEXT_HEIGHT + 2 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);
		tfKeyword.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 3 * TEXT_HEIGHT + 3 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);
		bTransmit.setBounds(X_TEXT_INIT, Y_TEXT_INIT + 4 * TEXT_HEIGHT + 4 * Y_GAP, TEXT_WIDTH, TEXT_HEIGHT);
		bAESKey.setBounds(X_TEXT_INIT + TEXT_WIDTH + X_GAP, Y_TEXT_INIT + 4 * TEXT_HEIGHT + 4 * Y_GAP, TEXT_WIDTH,
				TEXT_HEIGHT);

		bPublicKey.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int len = 128;
				String buffer = "";
				// TODO Auto-generated method stub
				if (enc == null) {
					len = Integer.parseInt(cKeysize.getItem(cKeysize.getSelectedIndex()));
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
						enc = new Assignment4Client(strOperationMode, strKeyword, len);
					} catch (Exception exp) {
						lStatusMessage.setText(exp.getStackTrace().toString());
					}

				}

				try {
					// creating a file dialog
					FileDialog fileDialog = new FileDialog(f, "Select a public key file");
					fileDialog.setVisible(true);
					buffer = fileDialog.getFile();
					if (buffer != null && buffer.length() > 0) {
						buffer = fileDialog.getDirectory() + fileDialog.getFile();
						lStatusMessage.setText("File Selected: " + buffer);
						enc.importPublicKey(buffer);
						lPublicKey.setText("Public Key");
						tfPublicKey.setText(enc.getPublicKey().toString());
						tfAlphaGen.setText(enc.getAlphaGenKey().toString());
						tfAlphaPublicKey.setText(enc.getAlphaPublicKey().toString());
						tfPublicKey.setVisible(true);
						tfAlphaGen.setVisible(true);
						tfAlphaPublicKey.setVisible(true);
						tfPublicKey.setEnabled(true);
						tfAlphaGen.setEnabled(true);
						tfAlphaPublicKey.setEnabled(true);
						lStatusMessage.setText("AES generated and public key imported successfully.");
						bTransmit.setEnabled(true);
					} else {
						lStatusMessage.setText("Please select file to import public key");
					}
				} catch (Exception exp) {
					lStatusMessage.setText(exp.getStackTrace().toString());
				}

				cKeysize.setEnabled(false);
				cMode.setEnabled(false);
				cAlgorithm.setEnabled(false);
			}
		});

		bAESKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					// creating a file dialog
					FileDialog fileDialog = new FileDialog(f, "Select a file for AES key export");
					fileDialog.setVisible(true);
					strKeyFile = fileDialog.getFile();
					if (strKeyFile != null && strKeyFile.length() > 0) {
						strKeyFile = fileDialog.getDirectory() + fileDialog.getFile();
						enc.exportSecretKey(strKeyFile);
						lStatusMessage.setText("AES keys saved to (.key|.param|mode| files " + strKeyFile);
						bAESKey.setEnabled(false);
						bAESKey.setFocusable(false);
					} else {
						lStatusMessage.setText("Please select file for AES key export");
					}
				} catch (Exception exp) {
					lStatusMessage.setText(exp.getStackTrace().toString());
				}
			}
		});

		bTransmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// creating a file dialog
				try {
					FileDialog fileDialog = new FileDialog(f, "Select a file for secure transmission");
					fileDialog.setVisible(true);
					strMessageFile = fileDialog.getFile();
					if (strMessageFile != null && strMessageFile.length() > 0) {
						strMessageFile = fileDialog.getDirectory() + fileDialog.getFile();
						strMessageFile = enc.processFile(strMessageFile, 1);
						lStatusMessage.setText("Export AES key now for encrypted file: " + strMessageFile);
						bAESKey.setEnabled(true);
						bAESKey.setFocusable(true);
					} else {
						lStatusMessage.setText("Please select a file for encryption");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					lStatusMessage.setText("Encryption failed - please check public key");
					e1.printStackTrace();
				}
			}
		});

		// frame size 300 width and 300 height
		f.setSize(1400, 1000);

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
		AWTAssignment4Client awt_obj = new AWTAssignment4Client();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}