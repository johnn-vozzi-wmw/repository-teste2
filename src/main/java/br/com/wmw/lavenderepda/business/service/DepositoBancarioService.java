package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.DepositoBancario;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DepositoBancarioDao;
import totalcross.util.Date;

public class DepositoBancarioService extends CrudService {

    private static DepositoBancarioService instance;
    
    private DepositoBancarioService() {
        //--
    }
    
    public static DepositoBancarioService getInstance() {
        if (instance == null) {
            instance = new DepositoBancarioService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return DepositoBancarioDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		DepositoBancario depositoBancario = (DepositoBancario) domain;
		if (ValueUtil.isEmpty(depositoBancario.dtDepositoBancario)) {
			throw new ValidationException(Messages.DEPOSITO_BANCARIO_MSG_VALOR_DEPOSITO_SEM_DATA_DEPOSITO);
		}
		if (ValueUtil.isEmpty(depositoBancario.nuDepositoBancario)) {
			throw new ValidationException(Messages.DEPOSITO_BANCARIO_MSG_VALOR_DEPOSITO_SEM_NUMERO);
		}
		if (depositoBancario.vlTotalDepositoBancario == 0) {
			throw new ValidationException(Messages.DEPOSITO_BANCARIO_MSG_VALOR_DEPOSITO_MENOR_OU_IGUAL_ZERO);
		}
		if (depositoBancario.dtDepositoBancario.isAfter(new Date())) {
			throw new ValidationException(Messages.DEPOSITO_BANCARIO_MSG_DATA_DEPOSITO_MAIOR_DATA_ATUAL);
		}
	}

	public DepositoBancario getNewDepositoBancario(Date dtFechamentoDiario) {
		DepositoBancario depositoBancario = new DepositoBancario();
		depositoBancario.cdEmpresa = SessionLavenderePda.cdEmpresa;
		depositoBancario.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(DepositoBancario.class);
		depositoBancario.dtFechamentoDiario = dtFechamentoDiario;
		return depositoBancario;
	}
	
	public double getVlTotalDeposito(Date dateFilter) throws SQLException {
		DepositoBancario depositoBancarioFilter = getNewDepositoBancario(dateFilter);
		return sumByExample(depositoBancarioFilter, "VLTOTALDEPOSITOBANCARIO");
	}
	
	protected void deleteDepositoBancarioMantendoUltimoFechamentoDiario(Date dateFilter) throws SQLException {
		if (ValueUtil.isNotEmpty(dateFilter)) {
			DepositoBancario depositoBancarioFilter = getNewDepositoBancario(null);
			depositoBancarioFilter.dtFechamentoDiarioFilter = dateFilter;
			deleteAllByExample(depositoBancarioFilter);
		}
	}
 
}