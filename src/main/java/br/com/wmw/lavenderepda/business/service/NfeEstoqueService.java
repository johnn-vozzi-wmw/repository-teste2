package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemNfeEstoque;
import br.com.wmw.lavenderepda.business.domain.NfeEstoque;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfeEstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.NfeEstoqueDto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfeEstoqueDao;

public class NfeEstoqueService extends CrudService {
	
	private static NfeEstoqueService instance;
	
	public static NfeEstoqueService getInstance() {
		if (instance == null) {
			instance = new NfeEstoqueService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return NfeEstoqueDao.getInstance();
	}
	
	public void insert(NfeEstoqueDto nfeEstoqueDto) throws SQLException {
		insert(new NfeEstoque(nfeEstoqueDto));
		for (ItemNfeEstoqueDto itemNfeEstoqueDto : nfeEstoqueDto.itemNfeEstoqueList) {
			ItemNfeEstoqueService.getInstance().insert(new ItemNfeEstoque(itemNfeEstoqueDto));
		}
	}
	
	public String getTipoNfe(String flTipoNfe) {
		if (NfeEstoque.TIPONFE_ENTRADA.equals(flTipoNfe)) {
			return flTipoNfe + " - " + Messages.REMESSAESTOQUE_NFE_ENTRADA;
		} else if (NfeEstoque.TIPONFE_SAIDA.equals(flTipoNfe)) {
			return flTipoNfe + " - " + Messages.REMESSAESTOQUE_NFE_SAIDA;
		}
		return ValueUtil.VALOR_NI;
	}

}
