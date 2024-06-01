package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ValorizacaoProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ValorizacaoProdDbxDao;
import totalcross.util.Vector;

public class ValorizacaoProdService extends CrudService {

    private static ValorizacaoProdService instance = null;
    
    private ValorizacaoProdService() {
        //--
    }
    
    public static ValorizacaoProdService getInstance() {
        if (instance == null) {
            instance = new ValorizacaoProdService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ValorizacaoProdDbxDao.getInstance();
    }
    
    public Vector findValorizacoes(String dsFiltro, BaseDomain domain) throws SQLException {
    	boolean onlyStartString = false;
    	if (LavenderePdaConfig.usaPesquisaInicioString) {
    		if (dsFiltro.startsWith("*")) {
        		dsFiltro = dsFiltro.substring(1);
    		} else {
        		onlyStartString = true;
    		}
    	}
		StringBuffer strBuffer = ProdutoService.getInstance().strBufferLikeDesc(dsFiltro, onlyStartString);
		ValorizacaoProd valorizacaoProd = (ValorizacaoProd) domain;
		if (!ValueUtil.isEmpty(dsFiltro)) {
			valorizacaoProd.dsProduto = LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro) ? "" : strBuffer.toString();
			if (!LavenderePdaConfig.usaFiltroSomenteDescricaoProduto) {
				valorizacaoProd.nuCodigoBarras = dsFiltro;
				if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro)) {
					valorizacaoProd.cdProduto = dsFiltro;
				} else {
					valorizacaoProd.cdProdutoLikeFilter = strBuffer.toString();
				}
				if (LavenderePdaConfig.usaFiltroMarcaProduto) {
					valorizacaoProd.dsMarca = strBuffer.toString();
				}
			}
			if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
				if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro)) {
					valorizacaoProd.dsReferencia = dsFiltro;
				} else {
					valorizacaoProd.dsReferenciaLikeFilter = strBuffer.toString();
				}
			}
		}
		return findAllByExample(valorizacaoProd);
    }
    
    //@Override
    public void validate(BaseDomain domain) {
    	ValorizacaoProd valorizacaoProd = (ValorizacaoProd) domain;
        //cdProduto
        if (ValueUtil.isEmpty(valorizacaoProd.dsProduto)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VALORIZACAOPROD_LABEL_DSPRODUTO);
        }
        //qtItem
        if (valorizacaoProd.qtItem == 0) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VALORIZACAOPROD_LABEL_QTITEM);
        }
        //vlUnitario
        if (valorizacaoProd.vlUnitario == 0) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VALORIZACAOPROD_LABEL_VLUNITARIO);
        }
    }
}