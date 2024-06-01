package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaPedidoDbxDao;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.util.Vector;

public class VisitaPedidoService extends CrudService {

    private static VisitaPedidoService instance;
    
    private VisitaPedidoService() {
        //--
    }
    
    public static VisitaPedidoService getInstance() {
        if (instance == null) {
            instance = new VisitaPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VisitaPedidoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public void adicionarVisitaPedido(String nuPedido, String cdVisita) throws SQLException {
    	VisitaPedido visitaPedido = new VisitaPedido();
    	visitaPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	visitaPedido.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	visitaPedido.cdVisita = cdVisita;
    	visitaPedido.nuPedido = nuPedido;
    	visitaPedido.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	if (findByRowKey(visitaPedido.getRowKey()) == null) {
	    	insert(visitaPedido);
    	}
    }
    
    public void insert(BaseDomain domain) throws SQLException {
    	validate(domain);
		validateDuplicated(domain);
		getCrudDao().insert(domain);
    }
    
    public void excluirVisitaPedido(Pedido pedido, Visita visita) throws SQLException {
    	VisitaPedido visitaPedido = new VisitaPedido();
    	visitaPedido.cdEmpresa = pedido.cdEmpresa;
    	visitaPedido.cdRepresentante = pedido.cdRepresentante;
    	visitaPedido.cdVisita = visita.cdVisita;
    	visitaPedido.nuPedido = pedido.nuPedido;
    	delete(visitaPedido);
    }
    
    public void updateVisitaPedidoParaEnvio(Visita visita, Pedido pedido) throws SQLException {
    	VisitaPedido visitaPedidoFilter = new VisitaPedido();
		visitaPedidoFilter.cdEmpresa = visita.cdEmpresa;
		visitaPedidoFilter.cdRepresentante = visita.cdRepresentante;
		visitaPedidoFilter.cdVisita = visita.cdVisita;
		visitaPedidoFilter.nuPedido = pedido.nuPedido;
		VisitaPedido visitaPedido = (VisitaPedido) findByRowKey(visitaPedidoFilter.getRowKey());
		if (visitaPedido != null) {
			visitaPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
			update(visitaPedido);
		}
    }
    
    public void updateVisitasPedidoParaEnvio(Visita visita) throws SQLException {
    	VisitaPedido visitaPedidoFilter = new VisitaPedido();
		visitaPedidoFilter.cdEmpresa = visita.cdEmpresa;
		visitaPedidoFilter.cdRepresentante = visita.cdRepresentante;
		visitaPedidoFilter.cdVisita = visita.cdVisita;
		Vector visitaPedidoList = findAllByExample(visitaPedidoFilter);
		if (ValueUtil.isNotEmpty(visitaPedidoList)) {
			for (int i = 0; i < visitaPedidoList.size(); i++) {
				VisitaPedido visitaPedido = (VisitaPedido) visitaPedidoList.items[i];
				Pedido pedido = PedidoService.getInstance().findPedidoByVisitaPedido(visitaPedido);
				if (pedido != null && !pedido.isPedidoAberto()) {
					visitaPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
					update(visitaPedido);
				}
			}
		}
    }
    
    public boolean isVisitaPossuiPedido(Visita visita) throws SQLException {
    	VisitaPedido visitaPedido = new VisitaPedido();
    	visitaPedido.cdEmpresa = visita.cdEmpresa;
    	visitaPedido.cdRepresentante = visita.cdRepresentante;
    	visitaPedido.cdVisita = visita.cdVisita;
    	return countByExample(visitaPedido) > 0;
    }
    
    public void deleteAllVisitaPedidoByVisita(Visita visita) throws SQLException {
    	VisitaPedido visitaPedidoFilter = new VisitaPedido();
    	visitaPedidoFilter.cdEmpresa = visita.cdEmpresa;
    	visitaPedidoFilter.cdRepresentante = visita.cdRepresentante;
    	visitaPedidoFilter.cdVisita = visita.cdVisita;
    	deleteAllByExample(visitaPedidoFilter);
    }
    
	public void enviaVisitaPedido(String cdSessao, Visita visita, String nuPedido) throws SQLException {
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			VisitaPedido visitaPedidoFilter = new VisitaPedido();
			visitaPedidoFilter.cdEmpresa = visita.cdEmpresa;
			visitaPedidoFilter.cdRepresentante = visita.cdRepresentante;
			visitaPedidoFilter.nuPedido = nuPedido;
			visitaPedidoFilter.cdVisita = visita.cdVisita;
			VisitaPedido visitaPedido = (VisitaPedido) findByRowKey(visitaPedidoFilter.getRowKey());
			if (visitaPedido != null) {
				EnviaDadosThread.getInstance().enviaVisitaPedido(cdSessao, visitaPedido);
			}
		}
	}
    
	public void reabreVisitaPedido(Pedido pedido) throws SQLException {
		VisitaPedido visitaPedidoFilter = new VisitaPedido();
		visitaPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		visitaPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		visitaPedidoFilter.nuPedido = pedido.nuPedido;
		Vector visitaPedidoList = findAllByExample(visitaPedidoFilter);
		if (ValueUtil.isNotEmpty(visitaPedidoList)) {
			for (int i = 0; i < visitaPedidoList.size(); i++) {
				VisitaPedido visitaPedido = (VisitaPedido) visitaPedidoList.items[i];
				visitaPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
				update(visitaPedido, false);
			}
		}
	}
    
}
