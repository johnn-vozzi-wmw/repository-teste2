package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ComissaoGrupoFaixaNeg;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ComissaoGrupoFaixaNegDao;

public class ComissaoGrupoFaixaNegService extends CrudService {
	
	private static ComissaoGrupoFaixaNegService instance;
	
	public static ComissaoGrupoFaixaNegService getInstance() {
		if (instance == null) {
			instance = new ComissaoGrupoFaixaNegService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return ComissaoGrupoFaixaNegDao.getInstance();
	}

	public double findVlPctComissaoByGrupoProduto(ItemPedido itemPedido) throws SQLException {
		ComissaoGrupoFaixaNeg comissaoGrupoFaixaNeg = new ComissaoGrupoFaixaNeg();
		int nuCasasDecimaisCalculoComissao = LavenderePdaConfig.getNuCasasDecimaisCalculoComissao();
		comissaoGrupoFaixaNeg.cdEmpresa = itemPedido.cdEmpresa;
		comissaoGrupoFaixaNeg.cdRepresentante = itemPedido.cdRepresentante;
		comissaoGrupoFaixaNeg.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
		comissaoGrupoFaixaNeg.cdGrupoProduto2 = itemPedido.getProduto().cdGrupoProduto2;
		comissaoGrupoFaixaNeg.cdGrupoProduto3 = itemPedido.getProduto().cdGrupoProduto3;
		if (itemPedido.vlBaseAntecipacao != 0 ) {
			comissaoGrupoFaixaNeg.vlIndice = ValueUtil.round((itemPedido.vlItemPedido / itemPedido.vlBaseAntecipacao), (nuCasasDecimaisCalculoComissao > 0 ? nuCasasDecimaisCalculoComissao : LavenderePdaConfig.nuCasasDecimais));	
		}
		return ComissaoGrupoFaixaNegDao.getInstance().findVlPctComissaoByGrupoProduto(comissaoGrupoFaixaNeg);
	}

}
