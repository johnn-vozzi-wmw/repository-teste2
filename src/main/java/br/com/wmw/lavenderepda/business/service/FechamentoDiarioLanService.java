package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiarioLan;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FechamentoDiarioLanDbxDao;
import totalcross.util.Date;

import java.sql.SQLException;

public class FechamentoDiarioLanService extends CrudService {

	private static FechamentoDiarioLanService instance;

	private FechamentoDiarioLanService() {

	}

	public static FechamentoDiarioLanService getInstance() {
		if (instance == null) {
			instance = new FechamentoDiarioLanService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain baseDomain) throws SQLException {
		FechamentoDiarioLan fechamentoDiarioLan = (FechamentoDiarioLan) baseDomain;
		if (ValueUtil.isEmpty(fechamentoDiarioLan.dtFechamentoDiario)) {
			throw new ValidationException(Messages.FECHAMENTO_DIARIO_LAN_VALIDATION_DTFECHAMENTODIARIO);
		}
		if (ValueUtil.isEmpty(fechamentoDiarioLan.cdTipoLancamento)) {
			throw new ValidationException(Messages.FECHAMENTO_DIARIO_LAN_VALIDATION_CDTIPOLANCAMENTO);
		}
		if (fechamentoDiarioLan.vlTotalLancamento <= 0) {
			throw new ValidationException(Messages.FECHAMENTO_DIARIO_LAN_VALIDATION_VLTOTALLANCAMENTO);
		}
	}

	@Override
	protected CrudDao getCrudDao() {
		return FechamentoDiarioLanDbxDao.getInstance();
	}

	public FechamentoDiarioLan getFilterByDate(Date dateFilter) {
		FechamentoDiarioLan fechamentoDiarioLan = new FechamentoDiarioLan();
		fechamentoDiarioLan.cdEmpresa = SessionLavenderePda.cdEmpresa;
		fechamentoDiarioLan.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		fechamentoDiarioLan.dtFechamentoDiario = dateFilter;
		return fechamentoDiarioLan;
	}

	public String[] getItemForInterface(FechamentoDiarioLan fechamentoDiarioLan) {
		return new String[]{
				fechamentoDiarioLan.toString(),
				fechamentoDiarioLan.dsTipoLancamento,
				MessageUtil.getMessage(Messages.FECHAMENTO_DIARIO_LAN_VLTOTALLANCAMENTO, StringUtil.getStringValueToInterface(fechamentoDiarioLan.vlTotalLancamento))
		};
	}

	public double getSumVlTotalLancamentosByDate(Date dtFechamentoDiario) throws SQLException {
		return sumByExample(getFilterByDate(dtFechamentoDiario), "VLTOTALLANCAMENTO");
	}
}
