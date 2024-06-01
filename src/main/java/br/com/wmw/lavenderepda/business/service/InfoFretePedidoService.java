package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.InfoFretePedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.InfoFretePedidoDao;

public class InfoFretePedidoService extends CrudService {

    private static InfoFretePedidoService instance;

    private InfoFretePedidoService() {
        //--
    }

    public static InfoFretePedidoService getInstance() {
        if (instance == null) {
            instance = new InfoFretePedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return InfoFretePedidoDao.getInstance();
    }

    public InfoFretePedido getInfoFrete(String cdEmpresa, String cdRepresentante, String nuPedido, String flOrigemPedido) throws SQLException {
    	InfoFretePedido infoFretePedidoFilter = new InfoFretePedido();
    	infoFretePedidoFilter.cdEmpresa = cdEmpresa;
    	infoFretePedidoFilter.cdRepresentante = cdRepresentante;
    	infoFretePedidoFilter.nuPedido = nuPedido;
    	infoFretePedidoFilter.flOrigemPedido = flOrigemPedido;
    	return (InfoFretePedido) findByRowKey(infoFretePedidoFilter.getRowKey());
    }

	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public void insertInfoFretePedido(InfoFretePedido infoFretePedido, String cdEmpresa, String cdRepresentante, String nuPedido, String flOrigemPedido) throws SQLException {
		infoFretePedido.cdEmpresa = cdEmpresa;
		infoFretePedido.cdRepresentante = cdRepresentante;
		infoFretePedido.nuPedido = nuPedido;
		infoFretePedido.flOrigemPedido = flOrigemPedido;
		insert(infoFretePedido);
	}
	
	public void insertOrUpdate(InfoFretePedido infoFretePedido) throws SQLException {
		try {
			validateDuplicated(infoFretePedido);
			insert(infoFretePedido);
		} catch (ValidationException ve) {
			update(infoFretePedido);
		}
	}

}