package br.com.wmw.lavenderepda.util;

import br.com.wmw.framework.business.domain.LogSyncBackground;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;

public final class DominioParametroUtil {
	
	public static boolean isParametroDominio(int cdParametro) {
		switch (cdParametro) {
		case ValorParametro.NIVELLOGSYNCBACKGROUND:
			return true;
		}
		return false;
	}
	
	public static String[] getDominioParametro(int cdParametro) {
		switch (cdParametro) {
		case ValorParametro.NIVELLOGSYNCBACKGROUND:
			return getDominiosNivelLogSyncBackground();
		}
		return new String[] {""};
	}
	
	public static String[] getDominiosNivelLogSyncBackground() {
		return new String[] {LogSyncBackground.LOG_NIVEL_DEBUG, LogSyncBackground.LOG_NIVEL_INFO, LogSyncBackground.LOG_NIVEL_ERROR};
	}
	
	public static int getIndexDominioParametro(int cdParametro, String valorParametro) {
		switch (cdParametro) {
		case ValorParametro.NIVELLOGSYNCBACKGROUND:
			String[] dominios = getDominiosNivelLogSyncBackground();
			for (int i = 0; i < dominios.length; i++) {
				if (dominios[i].equalsIgnoreCase(valorParametro)) return i;
			}
		}
		return 0;
	}
	
	public static boolean isParametroDominioSubDominio(int cdParametro) {
		switch (cdParametro) {
		case ValorParametro.NIVELLOGSYNCBACKGROUND:
			return false;
		}
		return false;
	}

}
