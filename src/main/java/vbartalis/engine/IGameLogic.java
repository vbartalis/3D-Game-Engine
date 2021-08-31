package vbartalis.engine;

import vbartalis.engine.input.KeyboardInput;
import vbartalis.engine.input.MouseInput;

public interface IGameLogic {

    void init(Window window) throws Exception;
    
    void input(Window window, MouseInput mouseInput, KeyboardInput keyboardInput);

    void update(float interval, MouseInput mouseInput, Window window);
    
    void render(Window window);
    
    void cleanup();
}