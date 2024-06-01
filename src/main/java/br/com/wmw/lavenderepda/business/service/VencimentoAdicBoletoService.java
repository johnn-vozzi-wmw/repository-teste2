package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.VencimentoAdicBoleto;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VencimentoAdicBoletoDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class VencimentoAdicBoletoService extends CrudService {

    private static VencimentoAdicBoletoService instance;
    
    private VencimentoAdicBoletoService() {
        //--
    }
    
    public static VencimentoAdicBoletoService getInstance() {
        if (instance == null) {
            instance = new VencimentoAdicBoletoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return VencimentoAdicBoletoDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		
	}
    
	public Vector findVencimentos(String cdEmpresa, String cdRepresentante, String cdVencimentoAdicBoleto) throws SQLException {
		return VencimentoAdicBoletoDao.getInstance().findVencimentos(cdEmpresa, cdRepresentante, cdVencimentoAdicBoleto);
	}
	
	public int getDataMaximaVencAdic(CondicaoPagamento condicaoPagamento, VencimentoAdicBoleto vencimentoAdicBoleto, Cliente cliente) {
		int inicio = LavenderePdaConfig.permiteAlterarVencimentoConformeCliente ? (cliente != null ? cliente.qtDiasMaximoPagamento : 0) : condicaoPagamento.nuIntervaloEntrada > 0 ? condicaoPagamento.nuIntervaloEntrada : condicaoPagamento.nuIntervaloParcelas;
		return inicio + (vencimentoAdicBoleto.nuIntervaloVencimentos * (vencimentoAdicBoleto.nuMaxVencimentos - 1));
	}
	
	public Date getDateVctoOuReferencia(Date dtReferencia, List<VenctoPagamentoPedido> listVencto, int i) {
		if (listVencto != null && listVencto.size() > i) {
			return listVencto.get(i).dtVencimento;
		}
		return dtReferencia;
	}
}