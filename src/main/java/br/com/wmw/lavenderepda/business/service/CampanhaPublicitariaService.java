package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CampanhaPublicitaria;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CampanhaPublicitariaPdbxDao;
import totalcross.util.Vector;

import java.sql.SQLException;

public class CampanhaPublicitariaService extends CrudService {

	private static CampanhaPublicitariaService instance;

	private CampanhaPublicitariaService() {
		//--
	}

	public static CampanhaPublicitariaService getInstance() {
		if (instance == null) {
			instance = new CampanhaPublicitariaService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return CampanhaPublicitariaPdbxDao.getInstance();
	}

	private CampanhaPublicitaria getCampanhaPublicitaria(String cdEmpresa, String cdRepresentante, String cdCliente, String cdCampanhaPublicitaria) {
		CampanhaPublicitaria campanhaPublicitaria = new CampanhaPublicitaria();
		campanhaPublicitaria.cdEmpresa = cdEmpresa;
		campanhaPublicitaria.cdRepresentante = cdRepresentante;
		campanhaPublicitaria.cdCampanhaPublicitaria = cdCampanhaPublicitaria;
		campanhaPublicitaria.cdCliente = cdCliente;
		return campanhaPublicitaria;
	}

	public CampanhaPublicitaria getCampanhaPublicitariaPadrao(String cdCliente) {
		CampanhaPublicitaria campanhaPublicitaria = getCampanhaPublicitaria(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante, cdCliente, Messages.CD_CAMPANHA_PUBLICITARIA_PADRAO);
		campanhaPublicitaria.dsCampanhaPublicitaria = Messages.CD_CAMPANHA_PUBLICITARIA_PADRAO;
		return campanhaPublicitaria;
	}

	public String findColumnByRowKey(String rowKey, String column) throws SQLException {
		return getCrudDao().findColumnByRowKey(rowKey, column);
	}

	public String findDsCampanhaPublicitariaByPedido(Pedido pedido, String cdCampanha) throws SQLException {
		if (ValueUtil.valueEquals(Messages.CD_CAMPANHA_PUBLICITARIA_PADRAO, cdCampanha)) return Messages.CD_CAMPANHA_PUBLICITARIA_PADRAO;
		return CampanhaPublicitariaPdbxDao.getInstance().findColumnByRowKey(getCampanhaPublicitaria(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, cdCampanha).getRowKey(), "DSCAMPANHAPUBLICITARIA");
	}

}
