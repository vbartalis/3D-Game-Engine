package vbartalis.game;


public class Main {

	public static void main(String[] args) {
		
		GameManager.init(new EngineSettings());
		while (!GameManager.readyToClose()) {	
			GameManager.update();
		}
		GameManager.cleanUp();
	}

}
