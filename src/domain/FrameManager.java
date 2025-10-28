package domain;

import gui.JFrameLobby;
import gui.JFrameLogin;

public class FrameManager {
	public static JFrameLogin loginFrame;
    public static JFrameLobby lobbyFrame;
    
    public static void init() {
        loginFrame = new JFrameLogin();
        lobbyFrame = new JFrameLobby();
        loginFrame.setVisible(true);
        System.out.println("init");
    }
    
    public static void Login() {
    	loginFrame.setVisible(false);
        lobbyFrame.setVisible(true);
    }
    
    public static void Relogin(){
        lobbyFrame.setVisible(false);
    	loginFrame.setVisible(true);
    }
    
}
