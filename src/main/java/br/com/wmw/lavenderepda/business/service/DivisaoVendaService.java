package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.sql.Types;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DivisaoVenda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DivisaoVendaPdbxDao;
import totalcross.util.Vector;

public class DivisaoVendaService extends CrudService {

	private static DivisaoVendaService instance;

	private DivisaoVendaService() {}
	
	public static DivisaoVendaService getInstance() {
        if (instance == null) {
            instance = new DivisaoVendaService();
        }
        return instance;
    }
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}
	
	@Override 
	protected CrudDao getCrudDao() { 
		return DivisaoVendaPdbxDao.getInstance(); 
	}
	
	public boolean isIgnoraVerbaGrupoSaldoPorDivisaoVenda(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao && ValueUtil.isNotEmpty(pedido.cdDivisaoVenda)) {
			DivisaoVenda divisaoVendaFilter = new DivisaoVenda();
			divisaoVendaFilter.cdEmpresa = pedido.cdEmpresa;
			divisaoVendaFilter.cdRepresentante = pedido.cdRepresentante;
			divisaoVendaFilter.cdDivisaoVenda = pedido.cdDivisaoVenda;
			String flIgnoraVerbaGrpProd = findColumnByRowKey(divisaoVendaFilter.getRowKey(), "FLIGNORAVERBAGRPPROD");
			return  ValueUtil.valueEquals(flIgnoraVerbaGrpProd, ValueUtil.VALOR_SIM);
		}
		return false;
	}

	public Vector findAllByExampleOrderByDsDivisaoVenda(BaseDomain domain) throws SQLException  {
	    domain.sortAtributte = DivisaoVenda.NMCOLUNA_DSDIVISAOVENDA;
	    domain.sortAsc = ValueUtil.VALOR_SIM;
	    return findAllByExample(domain);
	}
}