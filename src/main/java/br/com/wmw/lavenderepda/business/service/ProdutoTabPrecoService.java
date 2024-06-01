package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoTabPrecoPdbxDao;
import totalcross.util.Vector;

public class ProdutoTabPrecoService extends CrudService {

    private static ProdutoTabPrecoService instance;
    private ProdutoTabPrecoService() {
        //--
    }

    public static ProdutoTabPrecoService getInstance() {
        if (instance == null) {
            instance = new ProdutoTabPrecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoTabPrecoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findProdutosByPrincipioAtivo(String dsFiltro, String cdProdutoAtual, String cdTabelaPreco) throws SQLException {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("%");
		strBuffer.append(dsFiltro);
		strBuffer.append("%");
		//--
    	ProdutoTabPreco p = new ProdutoTabPreco();
		p.cdEmpresa = SessionLavenderePda.cdEmpresa;
		p.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	p.dsPrincipioAtivo = strBuffer.toString();
    	if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
    		StringBuffer strBufferTabPreco = new StringBuffer();
    		strBufferTabPreco.append("%");
    		strBufferTabPreco.append(ProdutoTabPreco.SEPARADOR_CAMPOS);
    		strBufferTabPreco.append(cdTabelaPreco);
    		strBufferTabPreco.append(ProdutoTabPreco.SEPARADOR_CAMPOS);
    		strBufferTabPreco.append("%");
    		p.dsTabPrecoList = strBufferTabPreco.toString();
    		p.itemTabelaPreco = new ItemTabelaPreco();
    		p.itemTabelaPreco.cdTabelaPreco = cdTabelaPreco;
    	}
    	Vector list = findAllByExampleSummary(p);
    	p.cdProduto = cdProdutoAtual;
    	list.removeElement(p);
    	return list;
    }

    public String getProdutoTabPrecoColumn(String cdProduto, String column) throws SQLException {
    	ProdutoTabPreco produtoTabPrecoFilter = new ProdutoTabPreco();
    	produtoTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produtoTabPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produtoTabPrecoFilter.cdProduto = cdProduto;
    	return findColumnByRowKey(produtoTabPrecoFilter.getRowKey(), column);
    }


    public ProdutoTabPreco getProdutoTabPreco(String cdProduto) throws SQLException {
    	ProdutoTabPreco produtoTabPrecoFilter = new ProdutoTabPreco();
    	produtoTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produtoTabPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produtoTabPrecoFilter.cdProduto = cdProduto;
    	ProdutoTabPreco produtoTabPreco = (ProdutoTabPreco) findByRowKey(produtoTabPrecoFilter.getRowKey());
    	if (produtoTabPreco == null) {
    		produtoTabPreco = new ProdutoTabPreco();
    	}
		return produtoTabPreco;
    }

    public Produto findByCdProdutoAndCdTabelaPreco(String cdProduto, String cdTabelaPreco) throws SQLException {
    	ProdutoTabPreco produtoTabPrecoFilter = new ProdutoTabPreco();
    	produtoTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produtoTabPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produtoTabPrecoFilter.cdProduto = cdProduto;
    	//--
    	if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
    		StringBuffer strBufferTabPreco = new StringBuffer();
    		strBufferTabPreco.append("%");
    		strBufferTabPreco.append(ProdutoTabPreco.SEPARADOR_CAMPOS);
    		strBufferTabPreco.append(cdTabelaPreco);
    		strBufferTabPreco.append(ProdutoTabPreco.SEPARADOR_CAMPOS);
    		strBufferTabPreco.append("%");
    		produtoTabPrecoFilter.dsTabPrecoList = strBufferTabPreco.toString();
    	}
    	//--
    	Vector produtoTabPrecoList = findAllByExample(produtoTabPrecoFilter);
    	if (produtoTabPrecoList.size() > 0) {
    		if (produtoTabPrecoList.items[0] != null) {
    			Produto produto = (Produto) ProdutoService.getInstance().findByRowKey(((ProdutoTabPreco)produtoTabPrecoList.items[0]).rowKey);
    			if (produto != null) {
    				return produto;
    			}
    		}
    	}
    	return new Produto();
    }
    
    public int countByExampleFullSQL(BaseDomain produtoFilter) throws SQLException {
    	return ProdutoTabPrecoPdbxDao.getInstance().countByExampleFullSQL(produtoFilter);
    }
}