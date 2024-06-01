package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoVlBase;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TributacaoVlBasePdbxDao;

public class TributacaoVlBaseService extends CrudService {

    private static TributacaoVlBaseService instance;

    private TributacaoVlBaseService() {
        //--
    }

    public static TributacaoVlBaseService getInstance() {
        if (instance == null) {
            instance = new TributacaoVlBaseService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TributacaoVlBasePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public TributacaoVlBase getTributacaoVlBase(String cdTributacaoCliente, String cdTributacaoProduto, String cdProduto, String cdUf) throws SQLException {
    	TributacaoVlBase tributacaoVlBase = new TributacaoVlBase();
    	tributacaoVlBase.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tributacaoVlBase.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	tributacaoVlBase.cdTributacaoCliente = cdTributacaoCliente;
    	tributacaoVlBase.cdTributacaoProduto = cdTributacaoProduto;
    	tributacaoVlBase.cdProduto = cdProduto;
    	return (TributacaoVlBase) findTributacaoVlBaseDeAcordoComUF(tributacaoVlBase, cdUf);
    }
    
    private TributacaoVlBase findTributacaoVlBaseDeAcordoComUF(TributacaoVlBase tributacaoVlBaseFilter, String cdUf) throws SQLException {
    	tributacaoVlBaseFilter.cdUf = ValueUtil.isNotEmpty(cdUf) ? cdUf : Tributacao.CDUFVALORPADRAO;
    	TributacaoVlBase tributacaoVlBase = (TributacaoVlBase)findByRowKey(tributacaoVlBaseFilter.getRowKey());
    	if (tributacaoVlBase == null && !Tributacao.CDUFVALORPADRAO.equals(cdUf)) {
    		tributacaoVlBase =  findTributacaoVlBaseDeAcordoComUF(tributacaoVlBaseFilter, Tributacao.CDUFVALORPADRAO);
    	}
    	return tributacaoVlBase;
    }

}