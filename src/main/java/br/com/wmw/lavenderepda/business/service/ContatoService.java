package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudPersonService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ContatoErp;
import br.com.wmw.lavenderepda.business.domain.ContatoPda;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContatoPdaPdbxDao;
import totalcross.util.Vector;

public class ContatoService extends CrudPersonService {

    private static ContatoService instance;

    private ContatoService() {
        //--
    }

    public static ContatoService getInstance() {
        if (instance == null) {
            instance = new ContatoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ContatoPdaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findAllContatoByDtAniversario(BaseDomain domain) throws SQLException {
    	ContatoPda contato = (ContatoPda) domain;
    	contato.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	if (!SessionLavenderePda.isUsuarioSupervisor()) {
    		contato.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
    	contato.flAcaoAlteracao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO;
    	contato.dtAniversario = DateUtil.getCurrentDate();
    	Vector listContatoFinal = new Vector();
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		listContatoFinal = ContatoPdaService.getInstance().findAllContatosGroupByRepresentante(contato);
    	} else {
    		listContatoFinal = findAllByExample(contato);
    	}
    	//---
    	ContatoErp contatoErp = new ContatoErp();
    	contatoErp.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	if (!SessionLavenderePda.isUsuarioSupervisor()) {
    		contatoErp.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
    	contatoErp.dtAniversario = DateUtil.getCurrentDate();
    	contatoErp.sortAsc = domain.sortAsc;
    	contatoErp.sortAtributte = domain.sortAtributte;
    	Vector listContatoErp = new Vector();
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		listContatoErp = ContatoErpService.getInstance().findAllContatosGroupByRepresentante(contatoErp);
    	} else {
    		listContatoErp = ContatoErpService.getInstance().findAllByExample(contatoErp);
    	}
    	if (listContatoErp.size() > 0) {
    		listContatoFinal.addElements(listContatoErp.toObjectArray());
    	}
    	listContatoErp = null;
    	return listContatoFinal;
    }

    public Vector findAllContatoByCliente(String cdCliente) throws SQLException {
    	ContatoPda contato = new ContatoPda();
    	contato.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	contato.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	contato.cdCliente = cdCliente;
    	contato.flAcaoAlteracao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO;
    	Vector listContatoFinal = findAllByExample(contato);
    	//--
    	ContatoErp contatoErp = new ContatoErp();
    	contatoErp.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	contatoErp.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	contatoErp.cdCliente = cdCliente;
    	Vector contatoList = ContatoErpService.getInstance().findAllByExample(contatoErp);
    	if (contatoList.size() > 0) {
    		listContatoFinal.addElements(contatoList.toObjectArray());
    	}
    	//--
    	return listContatoFinal;
    }

	public Vector findAllContatoByNovoCliente(String nuCnpj) throws SQLException {
    	return ContatoPdaPdbxDao.getInstance().findAllContatoByNovoCliente(nuCnpj);
	}

    public void deleteContatosPdaByContatosErp() throws SQLException {
		if (LavenderePdaConfig.isUsaModuloContatoCliente()) {
	    	Vector contatoList = findAll();
	    	//--
    		int size = contatoList.size();
    		for (int i = 0; i < size; i++) {
	    		ContatoPda contato = (ContatoPda) contatoList.items[i];
	    		//--
	    		ContatoErp contatoErpFilter = new ContatoErp();
	    		contatoErpFilter.cdEmpresa = contato.cdEmpresa;
	    		contatoErpFilter.cdRepresentante = contato.cdRepresentante;
	    		contatoErpFilter.cdContatoRelacionado = contato.cdContato;
	    		contatoErpFilter.cdCliente = contato.cdCliente;
	    		//--
	    		ContatoErp contatoErp;
	    		if (!contato.flOrigemContato.equals(OrigemPedido.FLORIGEMPEDIDO_ERP) || contato.flOrigemContato.equals(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
	    			contatoErp = ContatoErpService.getInstance().findContatoDefault(contatoErpFilter);
						
	    			if (contatoErp == null) {
						contatoErpFilter.cdContato = contato.cdContato;
	    				contatoErp = (ContatoErp) ContatoErpService.getInstance().findByRowKey(contatoErpFilter.getRowKey());
	
					}
	    			try {
		    			if (contatoErp != null) {
		    	    		delete(contato);
		    			}
			    	} catch (ApplicationException e) {
						// Não faz nada. Apneas não exclui, pois nao achou nenhum registro de contato Pda com a mesma chave do Contato ERP ou PDA
					}
	    		} else {
	    			Vector contatosErpList = ContatoErpService.getInstance().findAllByExample(contatoErpFilter);
	    			if (contatosErpList.size() == 1) {
	    				contatoErp = (ContatoErp)contatosErpList.items[0];
	    				try {
			    			if (contatoErp != null) {
			    	    		delete(contato);
			    			}
				    	} catch (ApplicationException e) {
							// Não faz nada. Apneas não exclui, pois nao achou nenhum registro de contato Pda com a mesma chave do Contato ERP
						}
	    			}
	    		}
	    	}
		}
    }
}