package vbartalis.engine.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;

@AllArgsConstructor
public enum MouseButton {

	LEFT(GLFW.GLFW_MOUSE_BUTTON_LEFT),
	MIDDLE(GLFW.GLFW_MOUSE_BUTTON_MIDDLE),
	RIGHT(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

	@Getter
	private final int glfwId;

}
