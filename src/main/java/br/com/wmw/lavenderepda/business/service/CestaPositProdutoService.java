package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CestaPositProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CestaPositProdutoPdbxDao;
import totalcross.util.Vector;

public class CestaPositProdutoService extends CrudService {

    private static CestaPositProdutoService instance;

    private CestaPositProdutoService() {
    }

    public static CestaPositProdutoService getInstance() {
        if (instance == null) {
            instance = new CestaPositProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CestaPositProdutoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findAllCestaPositProd(String filtro, String cdCamapanha, String cdCesta, boolean inClientDetail, String cdCliente) throws SQLException {
    	Vector result = new Vector();
    	CestaPositProduto cestapositproduto = new CestaPositProduto();
    	cestapositproduto.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	cestapositproduto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		cestapositproduto.cdCliente = cdCliente;
    	cestapositproduto.cdCampanha = cdCamapanha;
    	cestapositproduto.cdCesta = cdCesta;

    	// TODO Melhorar consulta - Aqui, Quando !inClienteDetail sistema agrupa os valores por Produto na mão.
    	// Talvez seja mais eficaz agrupar ja na busca no banco (GroupBy)
        Vector list = findAllByExample(cestapositproduto);
        if (!inClientDetail) {
        	list = agrupaValoresPorProduto(list);
        }
        if (ValueUtil.isEmpty(filtro)) {
			return list;
		}
    	int size = list.size();
    	for (int i = 0; i < size; i++) {
			CestaPositProduto cestaPositProduto = (CestaPositProduto)list.items[i];
			String descricao;
    		descricao = ProdutoService.getInstance().getDsProduto(cestaPositProduto.cdProduto);
			if ( ((descricao != null) && ((descricao).toUpperCase().indexOf(filtro) >= 0))
						   || (StringUtil.getStringValue(cestaPositProduto.cdProduto).indexOf(filtro) >= 0)) {
				result.addElement(cestaPositProduto);
			}
		}
		return result;
    }

    public Vector agrupaValoresPorProduto(Vector list) {
    	Vector result = new Vector();
    	int size = list.size();
    	for (int i = 0; i < size; i++) {
    		CestaPositProduto cestaPositProduto = (CestaPositProduto)list.items[i];
			cestaPositProduto.cdCliente = null;
			int index = result.indexOf(cestaPositProduto);
			if (index != -1) {
				CestaPositProduto cestaPosit = (CestaPositProduto)result.items[index];
				if (cestaPosit.cdProduto.equals(cestaPositProduto.cdProduto)) {
					cestaPosit.vlMeta += cestaPositProduto.vlMeta;
					cestaPosit.vlRealizado += cestaPositProduto.vlRealizado;
					cestaPosit.vlMeta = ValueUtil.round(cestaPosit.vlMeta);
					cestaPosit.vlRealizado = ValueUtil.round(cestaPosit.vlRealizado);
				}
			} else {
				result.addElement(cestaPositProduto);
			}
		}
		return result;
    }
}