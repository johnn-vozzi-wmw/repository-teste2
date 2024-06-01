package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Regiao;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.domain.TransportadoraReg;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TransportadoraRegDbxDao;
import totalcross.util.Vector;

public class TransportadoraRegService extends CrudService {

    private static TransportadoraRegService instance = null;
    
    private TransportadoraRegService() {
    }
    
    public static TransportadoraRegService getInstance() {
        if (instance == null) {
            instance = new TransportadoraRegService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TransportadoraRegDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public void removeVlFretePedido(Pedido pedido) throws SQLException {
		Transportadora transportadora = TransportadoraService.getInstance().getTransportadora(pedido.cdTransportadora);
		if (transportadora != null && transportadora.isFlSomaFrete()) {
			pedido.vlTotalPedido -= pedido.vlFrete;
			pedido.vlFrete = 0;
		} else {
			pedido.vlFrete = 0;
		}
	}
	
	public Vector getTransportadorasByCdRegiao(String value, BaseDomain domain) throws SQLException {
		Vector vectorRegiaoFiltro;
		vectorRegiaoFiltro = RegiaoService.getInstance().findAllRegiaoByCdRegiao(value);
		Vector transportadoraList = new Vector();
		int sizeRegiaoFiltro = vectorRegiaoFiltro.size();
		for (int i = 0; i < sizeRegiaoFiltro; i++) {
			TransportadoraReg filtroTransportadoraReg = (TransportadoraReg) domain;
			Regiao regiao = (Regiao) vectorRegiaoFiltro.elementAt(i);
			filtroTransportadoraReg.cdRegiao = regiao.cdRegiao;
			Vector transportadorasRegiao = findAllByExample(filtroTransportadoraReg);
			if (!transportadorasRegiao.isEmpty()) {
				int sizeTransportadoraFiltro = transportadorasRegiao.size();
				for (int j = 0; j < sizeTransportadoraFiltro; j++) {
					transportadoraList.addElement(transportadorasRegiao.elementAt(j));
				}
			}
		}
		return transportadoraList;
	}
	
	public Vector getTransportadorasByFiltroRegiao(Regiao regiaoFiltro, BaseDomain domain) throws SQLException {
		Vector vectorRegiaoFiltro;
		vectorRegiaoFiltro = RegiaoService.getInstance().findAllByExample(regiaoFiltro);
		Vector transportadoraList = new Vector();
		int sizeRegiaoFiltro = vectorRegiaoFiltro.size();
		for (int i = 0; i < sizeRegiaoFiltro; i++) {
			TransportadoraReg filtroTransportadoraReg = (TransportadoraReg) domain;
			Regiao regiao = (Regiao) vectorRegiaoFiltro.elementAt(i);
			filtroTransportadoraReg.cdRegiao = regiao.cdRegiao;
			Vector transportadorasRegiao = findAllByExample(filtroTransportadoraReg);
			if (!transportadorasRegiao.isEmpty()) {
				int sizeTransportadoraFiltro = transportadorasRegiao.size();
				for (int j = 0; j < sizeTransportadoraFiltro; j++) {
					transportadoraList.addElement(transportadorasRegiao.elementAt(j));
				}
			}
		}
		return transportadoraList;
	}
	
	public void validaTransportadoras(Transportadora transportadoraRelacionada, Transportadora transportadoraSelecionada) {
		if (transportadoraRelacionada != null && transportadoraSelecionada != null && !transportadoraSelecionada.equals(transportadoraRelacionada)) {
			String[] param = {transportadoraSelecionada.toString(), transportadoraRelacionada.toString()};
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_ERRO_TRANSPORTADORA_DIFERENTE_PEDIDO_RELACIONADO, param));
		}
	}
	
}