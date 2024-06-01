package br.com.wmw.lavenderepda.util;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cep;
import br.com.wmw.lavenderepda.business.domain.dto.CepDTO;
import br.com.wmw.lavenderepda.business.service.CepService;
import br.com.wmw.lavenderepda.presentation.ui.RelCepWindow;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONObject;
import totalcross.util.Vector;

public class CepUtil {
	
	public static Cep cepOffLine(String dsCep) throws SQLException {
		if (LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
			Vector cepList = CepService.getInstance().findAllByExample(new Cep(dsCep));
			if (cepList.size() == 0) return null;

			if (cepList.size() > 1) {
				return showRelCepWindow(cepList);
			} else if (cepList.size() == 1) {
				return (Cep) cepList.items[0];
			}
		}
		return null;
	}
	
	public static boolean consultaCepOnline(Cep cep) throws Exception {
		if (!LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
			return false;
		}
		String cepSync = SyncManager.consultaCepOnline(cep.dsCep);
		if (!cepSync.contains("logradouro")) {
			UiUtil.showInfoMessage(Messages.CEP_CONSULTA_ENDERECO, Messages.CEP_NAO_ENCONTRADO);
			return false;					
		}			
		CepDTO cepDto = new CepDTO(new JSONObject(cepSync));
		if (cepDto.getDsUf() == null) {
			UiUtil.showInfoMessage(Messages.CEP_CONSULTA_ENDERECO, Messages.CEP_NAO_ENCONTRADO);
			return false;
		} 
		cep.setEndereco(cepDto);
		return true;
	}
	
	private static Cep showRelCepWindow(Vector cepList) throws SQLException {
		RelCepWindow relCepWindow = new RelCepWindow(cepList);
		relCepWindow.popup();
		return relCepWindow.cepSelecionado;
	}	

}
