package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiarioEst;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FechamentoDiarioEstDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

import java.sql.SQLException;

public class FechamentoDiarioEstService extends CrudService {

	private static FechamentoDiarioEstService instance;

	private FechamentoDiarioEstService() {

	}

	public static FechamentoDiarioEstService getInstance() {
		if (instance == null) {
			instance = new FechamentoDiarioEstService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain baseDomain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return FechamentoDiarioEstDbxDao.getInstance();
	}

	public FechamentoDiarioEst getFilterByDate(Date date) {
		FechamentoDiarioEst fechamentoDiarioEst = new FechamentoDiarioEst();
		fechamentoDiarioEst.cdEmpresa = SessionLavenderePda.cdEmpresa;
		fechamentoDiarioEst.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		fechamentoDiarioEst.dtMovimentacao = date;
		return fechamentoDiarioEst;
	}

	public String[][] fechamentoDiarioEstListToGridList(Vector fechamentoDiarioEstList) throws SQLException {
		int size = fechamentoDiarioEstList.size();
		String[][] items = new String[size + 1][5];
		double totalRemessa = 0;
		double totalRetorno = 0;
		double totalVendido = 0;
		double totalSaldo = 0;
		for (int i = 0; i < size; i++) {
			FechamentoDiarioEst fechamentoDiarioEst = (FechamentoDiarioEst) fechamentoDiarioEstList.items[i];
			items[i][0] = ProdutoService.getInstance().getDescriptionWithId(fechamentoDiarioEst.cdProduto);
			items[i][1] = getEstoqueDescription(fechamentoDiarioEst.qtRemessa);
			items[i][2] = getEstoqueDescription(fechamentoDiarioEst.qtRetorno);
			items[i][3] = getEstoqueDescription(fechamentoDiarioEst.qtVendido);
			items[i][4] = getEstoqueDescription(fechamentoDiarioEst.vlSaldo);
			totalRemessa += fechamentoDiarioEst.qtRemessa;
			totalRetorno += fechamentoDiarioEst.qtRetorno;
			totalVendido += fechamentoDiarioEst.qtVendido;
			totalSaldo += fechamentoDiarioEst.vlSaldo;
		}
		items[size][0] = Messages.FECHAMENTO_DIARIO_EST_TOTAL;
		items[size][1] = getEstoqueDescription(totalRemessa);
		items[size][2] = getEstoqueDescription(totalRetorno);
		items[size][3] = getEstoqueDescription(totalVendido);
		items[size][4] = getEstoqueDescription(totalSaldo);
		return items;
	}

	private String getEstoqueDescription(double qtEstoque) {
		return LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? StringUtil.getStringValueToInterface((int) qtEstoque) : StringUtil.getStringValueToInterface(qtEstoque);
	}


}
