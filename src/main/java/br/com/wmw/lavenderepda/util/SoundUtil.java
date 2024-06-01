package br.com.wmw.lavenderepda.util;

import br.com.wmw.framework.util.VmUtil;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.media.Sound;

public class SoundUtil {

	private static String SOUND_ERROR_MP3 = "error.mp3";

	public static void soundError() {
		if (VmUtil.isAndroid()) {
			Sound.play(getPath() + SOUND_ERROR_MP3);
			Vm.sleep(150);
			Sound.play(getPath() + SOUND_ERROR_MP3);
		}
	}

	private static String getPath() {
		if (VmUtil.isAndroid() || VmUtil.isIOS() || (VmUtil.isSimulador() && !VmUtil.isJava())) {
			return Settings.appPath + "/";
		} else {
			return Settings.dataPath + "/";
		}
	}
	
}