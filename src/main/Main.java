package main;

import javax.swing.SwingUtilities;

import domain.FrameManager;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(() -> FrameManager.init());
		
	}

}
