package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.domain.TransportadoraCep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TransportadoraCepDbxDao;
import totalcross.util.Vector;

public class TransportadoraCepService extends CrudService{
	
	private static TransportadoraCepService instance = null;
	
	private TransportadoraCepService() {
	}
	
	public static TransportadoraCepService getInstance() {
		if(instance == null) {
			instance = new TransportadoraCepService();
		}
		return instance;
	}
	
	protected CrudDao getCrudDao() {
		return TransportadoraCepDbxDao.getInstance();
	}
		
	public void validate(BaseDomain domain) throws SQLException {}

	public Vector findTranspCepByCliente(Cliente cliente) throws SQLException {
		return TransportadoraCepDbxDao.getInstance().findTranspCepByCliente(cliente);
	}

	public String getCdTransportadoraPreferencial(Cliente cliente, TipoFrete tipoFrete) throws SQLException {
		if (tipoFrete.isTipoFreteCif()) {
			return cliente.cdTransportadoraCif;
		} else if (tipoFrete.isTipoFreteFob()) {
			return cliente.cdTransportadoraFob;
		}
		return null;
	}
	
	public TransportadoraCep findTransportadoraCepByCdTransportadoraAndDsCepComercial(String cdTransportadora, String dsCepComercial) throws SQLException {
		return TransportadoraCepDbxDao.getInstance().findTransportadoraCepByCdTransportadoraAndDsCepComercial(cdTransportadora, dsCepComercial);
	}
	
	public double getVlFreteLista(Pedido pedido, TransportadoraCep transportadoraCep, TipoFrete tipoFrete) throws SQLException {
		double vlFreteLista = 0;
		if (pedido.getTipoFrete() == null) {
			if (tipoFrete.isCalculaFrete()) {
				vlFreteLista = transportadoraCep.vlMinFrete + transportadoraCep.vlTaxaColeta;
			}
		} else if (tipoFrete.isCalculaFrete()) {
			vlFreteLista = pedido.vlTotalItens * transportadoraCep.vlPctFrete / 100 + transportadoraCep.vlTaxaColeta;
			vlFreteLista = vlFreteLista > transportadoraCep.vlMinFrete ? vlFreteLista : transportadoraCep.vlMinFrete + transportadoraCep.vlTaxaColeta;	
		}
	
		return ValueUtil.round(vlFreteLista, 2); 
	}
	public void setTransportadoraCep(Pedido pedido, TipoFrete tipoFrete, String rowKey) throws SQLException {
		if (tipoFrete.isTipoFreteSemFrete()) {
			pedido.cdTipoFrete = tipoFrete.cdTipoFrete;
			pedido.cdTransportadora = null;
			pedido.transportadora = null;
			pedido.transportadoraCep = null;
			pedido.vlFrete = 0;
			pedido.vlPctFrete = EmpresaService.getInstance().findVlPctFreteByCdEmpresa(pedido.cdEmpresa);
		} else {
			TransportadoraCep transportadoraCep = (TransportadoraCep) TransportadoraCepService.getInstance().findByRowKey(rowKey);
			if (transportadoraCep != null) {
				Transportadora transportadora = TransportadoraService.getInstance().getTransportadora(transportadoraCep.cdTransportadora);
				if (transportadora == null) {
					throw new ValidationException(Messages.TRANSPORTADORACEP_ERRO_NENHUM_TIPOFRETE_LOCALIZADO);
				} else {
					pedido.transportadoraCep = transportadoraCep;
					pedido.transportadora = transportadora;
					pedido.cdTransportadora = transportadora.cdTransportadora;
					pedido.cdTipoFrete = tipoFrete.cdTipoFrete;
					pedido.vlFrete = transportadoraCep.vlMinFrete + transportadoraCep.vlTaxaColeta;
				}
			} else {
				throw new NullPointerException(Messages.TRANSPORTADORACEP_ERRO_NENHUMA_DISPONIVEL);
			}
		
		}
	}

}
