package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescontoComissao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescontoComissaoDao;
import totalcross.util.Vector;

public class DescontoComissaoService extends CrudService {
	
	private static DescontoComissaoService instance;
	
	public static DescontoComissaoService getInstance() {
		if (instance == null) {
			instance = new DescontoComissaoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return DescontoComissaoDao.getInstance();
	}
	
	public DescontoComissao findDescComissaoByFaixa(String cdEmpresa, String cdRepresentante, double vlFaixaDesconto) throws SQLException {
		DescontoComissao descontoComissao = new DescontoComissao();
		descontoComissao.cdEmpresa = cdEmpresa;
		descontoComissao.cdRepresentante = cdRepresentante;
		descontoComissao.vlFaixaDesconto = vlFaixaDesconto;
		descontoComissao.limit = 1;
		descontoComissao.sortAtributte = DescontoComissao.NMCOLUNA_VLFAIXADESCONTO;
		descontoComissao.sortAsc = ValueUtil.VALOR_NAO;
		Vector descontoComissaoList = findAllByExample(descontoComissao);
		return ValueUtil.isNotEmpty(descontoComissaoList) ? (DescontoComissao)descontoComissaoList.items[0] : null;
	}

}
