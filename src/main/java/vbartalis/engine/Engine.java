package vbartalis.engine;


import vbartalis.engine.input.Keyboard;
import vbartalis.engine.input.Mouse;
import vbartalis.engine.window.Window;

public class Engine {

	public final Window window;
	public final Keyboard keyboard;
	public final Mouse mouse;
	public final Resources resources;
	public final StateManager stateManager;
	
	private final FrameTimer timer;
	
	private boolean closeFlag = false;

	protected Engine(Window window, Mouse mouse, Keyboard keyboard, FrameTimer timer, StateManager stateManager, Resources resources) {
		this.window = window;
		this.mouse = mouse;
		this.keyboard = keyboard;
		this.timer = timer;
		this.resources = resources;
		this.stateManager = stateManager;
	}

	public float getDeltaSeconds(){
		return timer.getDelta();
	}

	public void update() {
//		Ui.update(getDeltaSeconds());
//		BackgroundLoader.doTopGlRequests();
		keyboard.update();
		mouse.update();
		window.update();
		timer.update();
		stateManager.updateState();
	}

	public void cleanUp() {
//		BackgroundLoader.cleanUp();
//		Ui.cleanUp();
		window.destroy();
	}
	
	public void requestClose() {
		this.closeFlag = true;
	}
	
	public boolean isCloseRequested() {
		return closeFlag || window.closeButtonPressed();
	}

}
