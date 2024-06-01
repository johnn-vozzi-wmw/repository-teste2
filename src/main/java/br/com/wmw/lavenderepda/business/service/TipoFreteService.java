package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.TipoFreteCli;
import br.com.wmw.lavenderepda.business.domain.TransportadoraReg;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoFretePdbxDao;
import totalcross.util.Vector;

public class TipoFreteService extends CrudService {

    private static TipoFreteService instance;

    private TipoFreteService() {
        //--
    }

    public static TipoFreteService getInstance() {
        if (instance == null) {
            instance = new TipoFreteService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return TipoFretePdbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws SQLException {
    	/**/
    }

    public TipoFrete getTipoFrete(String cdTipoFrete, String cdUf) throws SQLException {
    	TipoFrete tipoFreteFilter = new TipoFrete();
    	tipoFreteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tipoFreteFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoFrete.class);
    	tipoFreteFilter.cdTipoFrete = cdTipoFrete;
    	tipoFreteFilter.cdUf = LavenderePdaConfig.usaTipoFretePorEstado ? cdUf : TipoFrete.CD_ESTADO_PADRAO;
    	TipoFrete tipoFrete = (TipoFrete) findByRowKey(tipoFreteFilter.getRowKey());
    	if (tipoFrete == null) {
    		return new TipoFrete();
    	}
    	return tipoFrete;
    }

	public void calculateFreteItemPedido(TipoFrete tipoFrete, ItemPedido itemPedido) {
		if (tipoFrete != null) {
			itemPedido.vlItemPedidoFrete = (tipoFrete.vlPctFrete / 100) * itemPedido.vlItemPedido;
		}
	}
	
	public TipoFrete findTipoFreteDefaultParaNovoPedido(Pedido pedido) throws SQLException {
		Vector tipoFreteList;
		try {
			TipoFrete tipoFreteFilter = getTipoFreteFilterByPedido(pedido);
			tipoFreteFilter.limit = 1;
			tipoFreteList = findAllByExample(tipoFreteFilter);
		} catch (FilterNotInformedException e) {
			tipoFreteList = new Vector(0);
		}
		if (ValueUtil.isNotEmpty(tipoFreteList)) {
			return (TipoFrete) tipoFreteList.elementAt(0);
		}
    	return new TipoFrete();
	}

	public boolean isTipoFreteCif(TransportadoraReg transportadoraReg, String cdUf) throws SQLException {
		TipoFrete tipoFreteFilter = new TipoFrete();
    	tipoFreteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tipoFreteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	tipoFreteFilter.cdTipoFrete = transportadoraReg.cdTipoFrete;
    	tipoFreteFilter.cdUf = LavenderePdaConfig.usaTipoFretePorEstado ? cdUf : TipoFrete.CD_ESTADO_PADRAO;
    	String flTipoFreteCif = findColumnByRowKey(tipoFreteFilter.getRowKey(), TipoFrete.NMCOLUNA_FLTIPOFRETECIF);
    	return flTipoFreteCif != null && ValueUtil.VALOR_SIM.equals(flTipoFreteCif);
	}
	
	public void validateChangeTipoFrete(String newCdTipoFrete, String cdEstadoComercial, Vector itemPedidoList) throws SQLException {
		TipoFrete tipoFrete = getTipoFrete(newCdTipoFrete, cdEstadoComercial);
		if (tipoFrete.isTipoFreteFob() && ComboService.getInstance().isPedidoComItemCombo(itemPedidoList)) {
			throw new ValidationException(Messages.MSG_ERRO_COMBO_TIPO_FRETE_FOB);
		}
	}
	
	public TipoFrete getTipoFreteFilterByPedido(Pedido pedido) throws SQLException {
		TipoFrete tipoFrete = new TipoFrete();
		tipoFrete.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoFrete.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoFrete.class);
		tipoFrete.cdUf = LavenderePdaConfig.usaTipoFretePorEstado && pedido == null ? null : TipoFrete.CD_ESTADO_PADRAO;
		if (pedido != null && pedido.getCliente() != null) {
			if (LavenderePdaConfig.usaTipoFretePorCliente) {
				if (pedido.isPedidoAberto()) {
					tipoFrete.tipoFreteCliFilter = new TipoFreteCli();
					tipoFrete.tipoFreteCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
					tipoFrete.tipoFreteCliFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoFreteCli.class);
					tipoFrete.tipoFreteCliFilter.cdCliente = pedido.cdCliente;
				}
			} else if (LavenderePdaConfig.usaTipoFretePorEstado) {
				if (ValueUtil.isEmpty(pedido.getCliente().cdEstadoComercial)) {
					throw new FilterNotInformedException(); 
				}
				tipoFrete.cdUf = pedido.getCliente().cdEstadoComercial;
			}
		}
		return tipoFrete;
	}

}