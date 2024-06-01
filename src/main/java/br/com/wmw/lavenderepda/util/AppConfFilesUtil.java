package br.com.wmw.lavenderepda.util;

import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.util.DatabaseUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.domain.ConexaoPda;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.util.Vector;

public class AppConfFilesUtil {
	
	public static final String USER_CONF = "user.conf";
	public static final String CONNECTION_CONF = "conn.conf";
	
	private AppConfFilesUtil() {
		super();
	}
	
	public static String loadConnectionConf() {
		return loadConf(CONNECTION_CONF);
	}

	public static String loadCdUsuarioConf() {
		return loadConf(USER_CONF);
	}

	private static String loadConf(String filename) {
		final String path = Convert.appendPath(Settings.dataPath, DatabaseUtil.PATH_APPCONFG);
		String data = readConfFile(Convert.appendPath(path, filename));
		if (ValueUtil.isEmpty(data) && VmUtil.isAndroid()) {
			data = readConfFile(Convert.appendPath(Settings.appPath, filename));
		}
		return data;
	}
	
	private static String readConfFile(String path) {
		try {
			if (FileUtil.exists(path)) {
				Vector bytes = FileUtil.readFile(path);
				if (bytes.size() > 0) {
					return new String((byte[]) bytes.items[0]);
				}
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return ValueUtil.VALOR_NI;
	}
	
	public static void manageConnectionConfFile() {
		try {
			StringBuilder newData = new StringBuilder();
			Object[] conexaoPdaList = ConexaoPdaService.getInstance().findConexaoPdaSorted().items;
			for (Object cp : conexaoPdaList) {
				if (cp != null) {
					newData.append(((ConexaoPda) cp).dsUrlWebService).append(";");
				}
			}
			manageConfFiles(CONNECTION_CONF, newData.toString());
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public static void manageUserConfFile() {
		manageConfFiles(USER_CONF, Session.getCdUsuario());
	}

	private static void manageConfFiles(String filename, String newData) {
		manageConfFile(Convert.appendPath(Settings.dataPath, DatabaseUtil.PATH_APPCONFG), filename, newData);
		if (VmUtil.isAndroid()) {
			manageConfFile(Settings.appPath, filename, newData);
		}
	}
	
	private static void manageConfFile(String path, String filename, String newData) {
		FileUtil.createDirIfNecessaryQuietly(path);
		String absolutePath = Convert.appendPath(path, filename);
		if (FileUtil.exists(absolutePath)) {
			updateConf(absolutePath, newData);
		} else {
			createConf(absolutePath, newData);
		}
	}

	private static void updateConf(String path, String newData) {
		try {
			String oldData = readConfFile(path);
			if (!newData.equals(oldData)) {
				FileUtil.deleteFile(path);
				createConf(path, newData);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private static void createConf(String path, String data) {
		try (File f = FileUtil.criaFile(path)) {
			f.writeBytes(data.getBytes());
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		}
	}

}
