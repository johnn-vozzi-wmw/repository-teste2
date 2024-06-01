package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Categoria;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CategoriaPdbxDao;
import totalcross.util.Vector;

public class CategoriaService extends CrudService {

    private static CategoriaService instance;

    private CategoriaService() {
    }

    public static CategoriaService getInstance() {
        if (instance == null) {
            instance = new CategoriaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CategoriaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public static String getDsCategoria(String cdCategoria) throws SQLException {
		Categoria categoriaFilter = new Categoria();
		categoriaFilter.cdCategoria = cdCategoria;
		categoriaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		categoriaFilter.dsCategoria = CategoriaService.getInstance().findColumnByRowKey(categoriaFilter.getRowKey(), "DSCATEGORIA");
    	return categoriaFilter.toString();
    }

	public Categoria getCategoria(String cdEmpresa,  String cdCategoria) throws SQLException {
    	Categoria categoriaFilter = new Categoria();
    	categoriaFilter.cdEmpresa = cdEmpresa;
    	categoriaFilter.cdCategoria = cdCategoria;
    	Categoria categoria = (Categoria)findByRowKey(categoriaFilter.getRowKey());
    	if (categoria == null) {
    		categoria = new Categoria();
    	}
    	return categoria;
	}
	
	public boolean isCategoriaDisponivelPorValorMinimo(Pedido pedido, Categoria categoria, String tipoCategoria) throws SQLException {
		if (categoria != null) {
			boolean value;
			if (Categoria.TIPO_CATEGORIA_ESPECIAL.equals(tipoCategoria)) {
				value = categoria.isCategoriaAtacado() || categoria.isCategoriaEspecial() || pedido.getCondicaoPagamento().isEspecial();
				if (value) {
					return false;
				}
				return pedido.vlTotalPedido >= categoria.vlMinPedidoEspecial;
			} else if (Categoria.TIPO_CATEGORIA_ATACADO.equals(tipoCategoria)) {
				value = categoria.isCategoriaAtacado() || (categoria.isCategoriaEspecial() && pedido.getCondicaoPagamento().isEspecial());
				if (value) {
					return false;
				}
				return pedido.vlTotalPedido >= categoria.vlMinPedidoAtacado && categoria.isCategoriaEspecial();
			}
		}
		return false;
	}
	
	public double getVlMaxDescCategoria(double vlPedido, Categoria categoria, String tipoCategoria, boolean isCategoriaDisponivel, double vlIndiceFinanceiro) throws SQLException {
		double vlMinPedido = 0;
		if (isCategoriaDisponivel) {
			if (Categoria.TIPO_CATEGORIA_ESPECIAL.equals(tipoCategoria)) {
				vlMinPedido = categoria.vlMinPedidoEspecial;
			} else if (Categoria.TIPO_CATEGORIA_ATACADO.equals(tipoCategoria)) {
				vlMinPedido = categoria.vlMinPedidoAtacado;
			}
		}
		if (!LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido()) {
			return ValueUtil.getDoubleValueTruncated((1 - (vlMinPedido / vlPedido)) * 100, 2); 
		} else {
			double value = ValueUtil.getDoubleValueTruncated((((vlMinPedido - (vlPedido * vlIndiceFinanceiro)) / vlIndiceFinanceiro) / vlPedido) * 100, 2);
			return value < 0 ? -value : value;
		}
	}

}