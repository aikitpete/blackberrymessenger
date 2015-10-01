package lib.adapter;

import net.rim.device.api.system.KeyListener;

public abstract class KeyAdapter implements KeyListener {

	public boolean keyChar(char key, int status, int time) {
		return false;
	}

	public boolean keyDown(int keycode, int time) {
		return false;
	}

	public boolean keyRepeat(int keycode, int time) {
		return false;
	}

	public boolean keyStatus(int keycode, int time) {
		return false;
	}

	public boolean keyUp(int keycode, int time) {
		return false;
	}

}
