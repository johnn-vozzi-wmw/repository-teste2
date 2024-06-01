package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteSeg;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Segmento;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SegmentoDbxDao;
import totalcross.util.Vector;

public class SegmentoService extends CrudService {

    private static SegmentoService instance;

    private SegmentoService() {
        //--
    }

    public static SegmentoService getInstance() {
        if (instance == null) {
            instance = new SegmentoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return SegmentoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        Segmento segmento = (Segmento) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(segmento.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SEGMENTO_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(segmento.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SEGMENTO_LABEL_CDREPRESENTANTE);
        }
        //cdSegmento
        if (ValueUtil.isEmpty(segmento.cdSegmento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SEGMENTO_LABEL_CDSEGMENTO);
        }
        //dsSegmento
        if (ValueUtil.isEmpty(segmento.dsSegmento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SEGMENTO_LABEL_DSSEGMENTO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(segmento.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SEGMENTO_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(segmento.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SEGMENTO_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(segmento.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SEGMENTO_LABEL_CDUSUARIO);
        }
*/
    }
	
	public Vector loadSegmentoListForCombo(Cliente cliente) throws SQLException {
		Segmento segmento = new Segmento();
		segmento.cdEmpresa = SessionLavenderePda.cdEmpresa;
		segmento.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Segmento.class);
		try {
			prepareClienteSegFilter(cliente, segmento);
		} catch (FilterNotInformedException e) {
			return new Vector(0);
		}
		return findAllByExample(segmento);
	}

	public boolean isSegmentoPedidoValido(Pedido pedido, Cliente cliente) throws SQLException {
		if (cliente != null && pedido != null) {
			Vector segmentoList = loadSegmentoListForCombo(cliente);
			if (ValueUtil.isNotEmpty(segmentoList)) {
				for (int i = 0; i < segmentoList.size(); i++) {
					Segmento segmento = (Segmento)  segmentoList.items[i];
					if (ValueUtil.valueEquals(segmento.cdSegmento, pedido.cdSegmento)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void prepareClienteSegFilter(Cliente cliente, Segmento segmento) {
		if (LavenderePdaConfig.usaSegmentoPorCliente && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
			if (ValueUtil.isEmpty(cliente.cdCliente)) {
				throw new FilterNotInformedException();
			}
			segmento.clienteSegFilter = new ClienteSeg();
			segmento.clienteSegFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			segmento.clienteSegFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ClienteSeg.class);
			segmento.clienteSegFilter.cdCliente = cliente.cdCliente;
		}
	}

}
