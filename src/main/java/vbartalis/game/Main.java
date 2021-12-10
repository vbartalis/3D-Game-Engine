package vbartalis.game;

import vbartalis.engine.GameEngine;
import vbartalis.engine.IGameLogic;
import vbartalis.engine.Window;

public class Main {

	public static void main(String[] args) {

		try {
			boolean vSync = true;
			IGameLogic gameLogic = new GameLogicImpl();
			Window.WindowOptions opts = new Window.WindowOptions();
			opts.cullFace = false;
			opts.showFps = true;
			opts.compatibleProfile = true;
			opts.antialiasing = true;
			opts.frustumCulling = false;
			GameEngine gameEng = new GameEngine("GAME", vSync, opts, gameLogic);
			gameEng.run();
		} catch (Exception excp) {
			excp.printStackTrace();
			System.exit(-1);
		}
	}

}
