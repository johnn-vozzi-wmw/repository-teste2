package br.com.wmw.lavenderepda;

import java.sql.SQLException;

import br.com.wmw.framework.config.AppConfig;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import totalcross.sys.Settings;
import totalcross.sys.Vm;

public class LavendereConfig extends AppConfig {
	
	public static final int CDSISTEMALAVENDEREPDA = 20;
	public static final String TIPO_ATUALIZACAO_COMPLETA = "C";
	public static final String TIPO_ATUALIZACAO_NORMAL = "A";
	public static final String WMWVENDAS_DATABASE = "wmwvendas.sqlite3";
	public static final String NMAPP_WTOOLS = "wtools-{0}.apk";
	public boolean aguardandoAtualizacaoApp;
	public boolean aguardandoFinalizacaoPedidosPendentes;

	protected String loadVersion() {
		try {
			byte[] bytes = Vm.getFile("version.conf");
			return new String(bytes);
		} catch (Throwable ex) {
			return Settings.appVersion;
		}
	}
	
	public static LavendereConfig getInstance() {
		return (LavendereConfig) instance;
	}
	
	public boolean isVersaoSistemaDesatualizada() throws SQLException {
		if (!VmUtil.isJava()) {
	    	String nuVersao = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.nuVersaoAtual);
			if (nuVersao != null) {
				if (getInstance().isValidVersion() && (!getInstance().version.equals(nuVersao))) {
					return true;
				}
			}
		}
		return false;
    }

	@Override
	protected int getCdSistema() {
		return LavendereConfig.CDSISTEMALAVENDEREPDA;
	}

	@Override
	protected String getDataBaseName() {
		return LavendereConfig.WMWVENDAS_DATABASE;
		
	}

}
