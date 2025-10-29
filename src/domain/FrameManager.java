package domain;

import javax.swing.JFrame;

import gui.JFrameFarmaciaSel;
import gui.JFrameLobby;
import gui.JFrameLogin;
import gui.JFramePrincipal;

public class FrameManager {
	public static JFrameLogin loginFrame;
    public static JFrameLobby lobbyFrame;
    public static JFrameFarmaciaSel FarmaciaFrame;
    
    public static void init() {
        loginFrame = new JFrameLogin();
        JFramePrincipal.panelActual = loginFrame;
        loginFrame.setVisible(true);
        
        System.out.println("init");
    }
    
    
}
