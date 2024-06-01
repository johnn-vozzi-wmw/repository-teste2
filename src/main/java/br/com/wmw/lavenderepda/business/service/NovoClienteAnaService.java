package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.NovoClienteAna;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NovoClienteAnaDbxDao;
import totalcross.util.Vector;

public class NovoClienteAnaService extends CrudService {

    private static NovoClienteAnaService instance = null;
    
    private NovoClienteAnaService() {
        //--
    }
    
    public static NovoClienteAnaService getInstance() {
        if (instance == null) {
            instance = new NovoClienteAnaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return NovoClienteAnaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public void loadNovoCliAna(NovoCliente novoCliente) throws SQLException {
		NovoClienteAna novoClienteAnaFilter = new NovoClienteAna();
		novoClienteAnaFilter.cdEmpresa = novoCliente.cdEmpresa;
		novoClienteAnaFilter.cdRepresentante = novoCliente.cdRepresentante;
		novoClienteAnaFilter.cdNovoCliente = novoCliente.cdNovoCliente;
		novoClienteAnaFilter.flOrigemNovoCliente = novoCliente.flOrigemNovoCliente;
		novoCliente.novoClienteAna = (NovoClienteAna) findByPrimaryKey(novoClienteAnaFilter);
		novoCliente.novoClienteAna = novoCliente.novoClienteAna != null ? novoCliente.novoClienteAna : new NovoClienteAna();
		if (ValueUtil.isEmpty(novoCliente.nuCnpj)) {
			novoCliente.nuCnpj = novoCliente.novoClienteAna.nuCnpj;
		} 
		if (ValueUtil.isEmpty(novoCliente.flTipoPessoa)) {
			novoCliente.flTipoPessoa = novoCliente.novoClienteAna.flTipoPessoa;
		} 
		if (novoCliente.getHashValuesDinamicos().get("NMRAZAOSOCIAL") == null || novoCliente.getHashValuesDinamicos().get("NMRAZAOSOCIAL") == ValueUtil.VALOR_NI) {
			novoCliente.getHashValuesDinamicos().put("NMRAZAOSOCIAL", StringUtil.getStringValue(novoCliente.novoClienteAna.nmRazaoSocial));
		}
	}

	public void loadNovoClienteAna(Prospect prospect) throws SQLException {
		NovoClienteAna novoClienteAnaFilter = new NovoClienteAna();
		novoClienteAnaFilter.cdEmpresa = prospect.cdEmpresa;
		novoClienteAnaFilter.cdRepresentante = prospect.cdRepresentante;
		novoClienteAnaFilter.cdNovoCliente = prospect.cdProspect;
		novoClienteAnaFilter.flOrigemNovoCliente = prospect.flOrigemProspect;
		prospect.novoClienteAna = (NovoClienteAna) findByPrimaryKey(novoClienteAnaFilter);
		prospect.novoClienteAna = prospect.novoClienteAna != null ? prospect.novoClienteAna : new NovoClienteAna();
		if (ValueUtil.isEmpty(prospect.nuCnpj)) {
			prospect.nuCnpj = prospect.novoClienteAna.nuCnpj;
		}
		if (ValueUtil.isEmpty(prospect.flTipoPessoa)) {
			prospect.flTipoPessoa = prospect.novoClienteAna.flTipoPessoa;
		}
		if (prospect.getHashValuesDinamicos().get("NMRAZAOSOCIAL") == null || prospect.getHashValuesDinamicos().get("NMRAZAOSOCIAL") == ValueUtil.VALOR_NI) {
			prospect.getHashValuesDinamicos().put("NMRAZAOSOCIAL", StringUtil.getStringValue(prospect.novoClienteAna.nmRazaoSocial));
		}
	}

	public Vector findAllNovoClienteAna(NovoCliente novoCliente) throws SQLException {
		return NovoClienteAnaDbxDao.getInstance().findAllNovoClienteAna(novoCliente);
	}

	public boolean hasNovoClientePendenteEdicao() throws SQLException {
		return NovoClienteAnaDbxDao.getInstance().hasNovoClienteAnaPendenteEdicao();
	}

	public Vector findAllNovoClienteAna(Prospect prospect) throws SQLException {
		return NovoClienteAnaDbxDao.getInstance().findAllNovoClienteAna(prospect);
	}
}