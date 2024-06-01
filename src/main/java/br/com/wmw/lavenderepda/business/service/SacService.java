package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ContatoCrm;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.domain.SubTipoSac;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SacDao;

public class SacService extends CrudService {

    private static SacService instance;
    
    private SacService() {
        //--
    }
    
    public static SacService getInstance() {
        if (instance == null) {
            instance = new SacService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return SacDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	Sac sac = (Sac) domain;

    	if (ValueUtil.isEmpty(sac.cdTipoSac) && ValueUtil.isEmpty(sac.tipoSac)) {
    		throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SAC_LABEL_TIPO_SAC);
    	}
    	
		if (sac.tipoSac != null) {
			if (ValueUtil.valueEquals(sac.tipoSac.flRelacionarNotaFiscal, "O") && ValueUtil.isEmpty(sac.nuNotaFiscal)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SAC_LABEL_NOTA_FISCAL);
			}

			if (ValueUtil.valueEquals(sac.tipoSac.flRelacionarPedido, "O") && ValueUtil.isEmpty(sac.nuPedido)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SAC_LABEL_PEDIDO);
			}

			if (LavenderePdaConfig.usaRelacionamentoProdutosSAC && ValueUtil.valueEquals(sac.tipoSac.flRelacionarProduto, "O") && ValueUtil.isEmpty(sac.listProdutoSac)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTO);
			}
		}
    	SubTipoSac subTipoSac = new SubTipoSac();
		subTipoSac.cdEmpresa = SessionLavenderePda.cdEmpresa;
		subTipoSac.cdTipoSac = sac.cdTipoSac;
    	
		if (SubTipoSacService.getInstance().findAllByExample(subTipoSac).size() > 0 && ValueUtil.isEmpty(sac.cdSubTipoSac)) {
    		throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SAC_LABEL_SUBTIPO_SAC);
    	}
    	
    	if (ValueUtil.isEmpty(sac.dsSac)) {
    		throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SAC_LABEL_DESCRICAO);
    	}
    	if (sac.contatoCrm != null && !LavenderePdaConfig.ocultaContatoSAC && ValueUtil.isEmpty(sac.contatoCrm.cdContato)) {
	    	throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SAC_LABEL_CONTATO);
    	}
    }
    
    
    public boolean haveSacs(String cdCliente) throws SQLException {
    	Sac sac = new Sac();
    	sac.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	sac.cdCliente = cdCliente;
    	return countByExample(sac) > 0;
    }

	public boolean hasSacExibirRelatorio() throws SQLException {
		Sac sacNovoFilter = new Sac();
		sacNovoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		sacNovoFilter.filterBySacNaoExibido = true;
		Sac sacStatusFilter = new Sac();
		sacStatusFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		sacStatusFilter.flStatusAlterado = ValueUtil.VALOR_SIM;
		return countByExample(sacNovoFilter) > 0 || countByExample(sacStatusFilter) > 0;
	}
	
	public void sacAtulizaStatus(BaseDomain domain) throws SQLException {
		((SacDao) getCrudDao()).sacAtulizaStatus(domain);
	}

}