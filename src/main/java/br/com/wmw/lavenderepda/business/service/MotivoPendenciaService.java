package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MotivoPendencia;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotivoPendenciaDao;
import totalcross.util.Vector;

public class MotivoPendenciaService extends CrudService {

    private static MotivoPendenciaService instance;
    
    private MotivoPendenciaService() { }
    
    public static MotivoPendenciaService getInstance() {
        if (instance == null) {
            instance = new MotivoPendenciaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() { return MotivoPendenciaDao.getInstance(); }
	
    @Override
	public void validate(BaseDomain domain) throws SQLException { }

	public MotivoPendencia findByMotivoPendencia(String cdMotivoPendencia) throws SQLException {
		MotivoPendencia motivoPendenciaFilter = new MotivoPendencia(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(MotivoPendencia.class), cdMotivoPendencia);
		return (MotivoPendencia) findByRowKey(motivoPendenciaFilter.getRowKey());
	}

	public MotivoPendencia findMotivoPendenciaPrincipalPedido(Pedido pedido) throws SQLException {
		MotivoPendencia motivoPendenciaFilter = new MotivoPendencia();
		motivoPendenciaFilter.cdEmpresa = pedido.cdEmpresa;
		motivoPendenciaFilter.cdRepresentante = pedido.cdRepresentante;
		motivoPendenciaFilter.nuPedido = pedido.nuPedido;
		motivoPendenciaFilter.limit = 1;
		Vector motivoPendenciaResult = MotivoPendenciaDao.getInstance().findMotivoPendenciaPrincipalPedido(motivoPendenciaFilter, false);
		return ValueUtil.isEmpty(motivoPendenciaResult) ? null : (MotivoPendencia)motivoPendenciaResult.items[0];
	}
	
	public MotivoPendencia findMotivoPendenciaPrincipalPedidoErp(Pedido pedido) throws SQLException {
		MotivoPendencia motivoPendenciaFilter = new MotivoPendencia();
		motivoPendenciaFilter.cdEmpresa = pedido.cdEmpresa;
		motivoPendenciaFilter.cdRepresentante = pedido.cdRepresentante;
		motivoPendenciaFilter.nuPedido = pedido.nuPedido;
		motivoPendenciaFilter.limit = 1;
		Vector motivoPendenciaResult = MotivoPendenciaDao.getInstance().findMotivoPendenciaPrincipalPedidoErp(motivoPendenciaFilter);
		return ValueUtil.isEmpty(motivoPendenciaResult) ? null : (MotivoPendencia)motivoPendenciaResult.items[0];
	}
    
}