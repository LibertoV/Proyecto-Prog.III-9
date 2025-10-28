package main;

import javax.swing.SwingUtilities;

import domain.FrameManager;
import gui.JFrameLobby;
import gui.JFrameLogin;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(() -> FrameManager.init());
		
	}

}
