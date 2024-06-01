package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.DadosTc2Web;
import br.com.wmw.lavenderepda.business.domain.PontoGps;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DadosTc2WebPdbxDao;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.sys.Vm;
import totalcross.util.Vector;

public class DadosTc2WebService {

	private static DadosTc2WebService instance;

	public static DadosTc2WebService getInstance() {
        if (instance == null) {
            instance = new DadosTc2WebService();
        }
        return instance;
    }

	public void updateDadosTc2WebEnviadosServidor() throws SQLException {
		Vector dadosTc2WebList = EnviaDadosThread.getInstance().dadosEnviadosSucessoCache;
		if (ValueUtil.isNotEmpty(dadosTc2WebList)) {
			int count = 0;
			while (SessionLavenderePda.isRunningSync()) {
				Vm.sleep(100);
				count++;
				if (count > Session.valorTimeoutReadHttpConnection) {
					throw new ValidationException(Messages.TIMEOUT_AGUARDANDO_CONEXAO_BACKGROUND);
				}
			}
			Vector dadosEnviadosList = new Vector();
			int size = dadosTc2WebList.size();
			for (int i = 0; i < size; i++) {
				DadosTc2Web dadosTc2Web = (DadosTc2Web) dadosTc2WebList.items[i];
				if (PontoGps.TABLE_NAME.equals(dadosTc2Web.tableName)) {
					dadosEnviadosList.addElement(dadosTc2Web);
					continue;
				}
				DadosTc2WebPdbxDao.getInstance().updateDadosTc2WebEnviadosServidor(dadosTc2Web.tableName, dadosTc2Web.rowKeys);
				dadosEnviadosList.addElement(dadosTc2Web);
			}
			VectorUtil.removeObjectsByList(dadosTc2WebList, dadosEnviadosList);
		}
	}




}
