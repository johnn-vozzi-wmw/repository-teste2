package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CargaProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CargaProdutoDbxDao;
import totalcross.util.Date;

public class CargaProdutoService extends CrudService {

    private static CargaProdutoService instance = null;
    
    private CargaProdutoService() {
        //--
    }
    
    public static CargaProdutoService getInstance() {
        if (instance == null) {
            instance = new CargaProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CargaProdutoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
        CargaProduto cargaProduto = (CargaProduto) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(cargaProduto.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPRODUTO_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(cargaProduto.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPRODUTO_LABEL_CDREPRESENTANTE);
        }
        //cdProduto
        if (ValueUtil.isEmpty(cargaProduto.cdProduto)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPRODUTO_LABEL_CDPRODUTO);
        }
        //cdCargaProduto
        if (ValueUtil.isEmpty(cargaProduto.cdCargaProduto)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPRODUTO_LABEL_CDCARGAPRODUTO);
        }
        //qtSolicitado
        if (cargaProduto.qtSolicitado == 0) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPRODUTO_LABEL_QTSOLICITADO);
        }
        //dtCadastro
        if (ValueUtil.isEmpty(cargaProduto.dtCadastro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPRODUTO_LABEL_DTCADASTRO);
        }
        //hrCadastro
        if (ValueUtil.isEmpty(cargaProduto.hrCadastro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPRODUTO_LABEL_HRCADASTRO);
        }
    }

	public void salvarCargaProduto(ArrayList<Produto> produtoCargaList) throws SQLException {
    	String cdCargaProduto = generateIdGlobal();
		for (Produto produto : produtoCargaList) {
			if (produto.qtSolicitCargaProd > 0) {
				CargaProduto cargaProduto = new CargaProduto();
				cargaProduto.cdEmpresa = SessionLavenderePda.cdEmpresa;
				cargaProduto.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
				cargaProduto.cdProduto = produto.cdProduto;
				cargaProduto.cdCargaProduto = cdCargaProduto;
				cargaProduto.qtSolicitado = produto.qtSolicitCargaProd;
				cargaProduto.dtCadastro = DateUtil.getCurrentDate();
				cargaProduto.hrCadastro = TimeUtil.getCurrentTimeHHMMSS();
				insert(cargaProduto);
			}
		}
	}


	public void validaPeriodo(Date dtInicial, Date dtFinal) {
		Date dtAtual = DateUtil.getCurrentDate();
		if (ValueUtil.isNotEmpty(dtInicial) && dtInicial.isAfter(dtAtual)) {
			throw new ValidationException(Messages.CARGAPRODUTO_DTINICIAL_MAIOR_DTATUAL);
		}
		if (ValueUtil.isNotEmpty(dtFinal) && ValueUtil.isNotEmpty(dtInicial) && dtInicial.isAfter(dtFinal)) {
			throw new ValidationException(Messages.CARGAPRODUTO_DTINICIAL_MAIOR_DTATUAL);
		}
	}

}