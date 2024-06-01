package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.GrupoDescProd;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoDescProdDbxDao;
import totalcross.util.Vector;

public class GrupoDescProdService extends CrudService {

    private static GrupoDescProdService instance;
    
    private GrupoDescProdService() {}
    
    public static GrupoDescProdService getInstance() {
    	return (instance == null) ? instance = new GrupoDescProdService() : instance; 
    }
    
    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {}

    @Override
    protected CrudDao getCrudDao() {
        return GrupoDescProdDbxDao.getInstance();
    }
    
    public double getQtItensByGrupoProdutoNoPedido(Pedido pedido, String cdGrupoProduto) throws SQLException {
		int size = pedido.itemPedidoList.size();
		if (ValueUtil.isEmpty(cdGrupoProduto)) return 0d;
		double qtItensGrupo = 0d;
		for (int j = 0 ; j < size; j++) {
			ItemPedido itemPedidoTemp = (ItemPedido) pedido.itemPedidoList.items[j];
			if (!ValueUtil.valueEquals(cdGrupoProduto, itemPedidoTemp.getProduto().cdGrupoDescProd)) continue;
			qtItensGrupo += itemPedidoTemp.getQtItemFisico();
		}
    	return qtItensGrupo;
    }

    public String getDsGrupoProduto(String cdEmpresa, String cdRepresentante, String cdProduto, String cdGrupoProduto) throws SQLException {
		GrupoDescProd grupoDescProd = new GrupoDescProd();
		grupoDescProd.cdEmpresa = cdEmpresa;
		grupoDescProd.cdRepresentante = cdRepresentante;
		grupoDescProd.cdProduto = cdProduto;
		grupoDescProd.cdGrupoDescProd = cdGrupoProduto;
		grupoDescProd.dsGrupoDescProd = findColumnByRowKey(grupoDescProd.getRowKey(), "DSGRUPODESCPROD");
   		return grupoDescProd.toString();
    }

	public Vector findAllByExampleCombo(GrupoDescProd grupoDescProdFilter) throws SQLException {
		return GrupoDescProdDbxDao.getInstance().findAllByExampleCombo(grupoDescProdFilter);
	}

}
