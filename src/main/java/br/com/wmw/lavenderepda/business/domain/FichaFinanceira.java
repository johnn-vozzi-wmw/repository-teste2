package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.TituloFinanceiroService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TituloFinanceiroPdbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class FichaFinanceira extends BasePersonDomain {

    public static String TABLE_NAME = "TBLVPFICHAFINANCEIRA";

	public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public double vlMaiorFatura;
    public double vlMedioCompra;
    public double vlSaldoCliente;
    public String dsPagamentoAntecipado;
    public String dsPagamentoVencimento;
    public String dsPagamentoAtrasado;
    public int qtMediaDiasAtraso;
    public double vlPedidosCarteira;
    public String flStatusCliente;

    //Não persistentes
    public double vlTotalAtraso;
    public int qtAtrasado;
    public double vlTotalAberto;
    public int qtAberto;
    public Date dtTituloMaisAtrasado;

    private Vector tituloFinanceiroList;

    public FichaFinanceira(String tableName) {
		super(tableName);
	}

    public FichaFinanceira() {
    	super(null);
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FichaFinanceira) {
            FichaFinanceira fichaFinanceira = (FichaFinanceira) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, fichaFinanceira.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, fichaFinanceira.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, fichaFinanceira.cdCliente);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
        return strBuffer.toString();
    }

	public Vector getTitulos() throws SQLException {
		if ((tituloFinanceiroList == null) && ValueUtil.isNotEmpty(cdCliente)) {
			TituloFinanceiro tituloFinanceiroFilter = montaFiltroTituloFinanceiro(null);
			Vector list = TituloFinanceiroService.getInstance().findAllByExample(tituloFinanceiroFilter);
			calculaDadosTituloFinanceiro(list);
			tituloFinanceiroList = list;
		}
		return tituloFinanceiroList;
	}
	
	private void calculaDadosTituloFinanceiro(Vector tituloFinanceiroList) {
		int listSize = tituloFinanceiroList.size();
		if (listSize == 0) return;
		
		vlTotalAberto = 0;
		qtAberto = 0;
		vlTotalAtraso = 0;
		qtAtrasado = 0;
		double vlAbertoTitulo;
		for (int i = 0; i < listSize; i++) {
			TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) tituloFinanceiroList.items[i];
			vlAbertoTitulo = ValueUtil.isEmpty(tituloFinanceiro.dtPagamento) ? tituloFinanceiro.vlTitulo - tituloFinanceiro.vlPago : 0;
			vlTotalAberto += vlAbertoTitulo;
			if (ValueUtil.isEmpty(tituloFinanceiro.dtPagamento)) {
				qtAberto++;
				if (tituloFinanceiro.dtVencimento.isBefore(new Date())) {
					vlTotalAtraso += vlAbertoTitulo;
					qtAtrasado++;
				}
			}
			if (isTituloAtrasado(tituloFinanceiro, dtTituloMaisAtrasado)) {
				dtTituloMaisAtrasado = tituloFinanceiro.dtVencimento;
			}
		}
	}
	
	private boolean isTituloAtrasado(TituloFinanceiro tituloFinanceiro, Date dtTituloMaisAtrasado) {
		if (ValueUtil.isEmpty(dtTituloMaisAtrasado)) {
			dtTituloMaisAtrasado = new Date();
		}
		return tituloFinanceiro.dtVencimento.isBefore(dtTituloMaisAtrasado);
	}

	private TituloFinanceiro montaFiltroTituloFinanceiro(String cdTipoPagamento) {
		TituloFinanceiro tituloFinanceiroFilter = new TituloFinanceiro();
		tituloFinanceiroFilter.cdEmpresa = cdEmpresa;
		tituloFinanceiroFilter.cdRepresentante = cdRepresentante;
		tituloFinanceiroFilter.cdCliente = cdCliente;
		tituloFinanceiroFilter.cdTipoPagamento = cdTipoPagamento;
		return tituloFinanceiroFilter;
	}

	public Vector getTitulos(String sortAtributte, String sortAsc, String cdTipoPagamento) throws SQLException {
		TituloFinanceiro tituloFinanceiroFilter = montaFiltroTituloFinanceiro(cdTipoPagamento);
		tituloFinanceiroFilter.sortAtributte = sortAtributte;
		tituloFinanceiroFilter.sortAsc = sortAsc;
		tituloFinanceiroFilter.ordenacaoDinamica = true;
		Vector tituloFinanceiroList = TituloFinanceiroService.getInstance().findAllByExample(tituloFinanceiroFilter);
		calculaDadosTituloFinanceiro(tituloFinanceiroList);
		return tituloFinanceiroList;
	}
	
	public Vector getTitulos(String sortAtributte, String sortAsc, String cdTipoPagamento, 
			Date dtIncialPagamento, Date dtFinalPagamento, Date dtIncialVencimento, Date dtFinalVencimento, String status) throws SQLException {
		TituloFinanceiro tituloFinanceiroFilter = montaFiltroTituloFinanceiro(cdTipoPagamento);
		tituloFinanceiroFilter.sortAtributte = sortAtributte;
		tituloFinanceiroFilter.sortAsc = sortAsc;
		tituloFinanceiroFilter.ordenacaoDinamica = true;
		tituloFinanceiroFilter.dtIncialPagamento = dtIncialPagamento;
		tituloFinanceiroFilter.dtFinalPagamento = dtFinalPagamento;
		tituloFinanceiroFilter.dtIncialVencimento = dtIncialVencimento;
		tituloFinanceiroFilter.dtFinalVencimento = dtFinalVencimento;
		tituloFinanceiroFilter.statusTituloFinanceiro = status;
		Vector tituloFinanceiroList = TituloFinanceiroPdbxDao.getInstance().findAllTituloFinanceiroPeriodoFilters(tituloFinanceiroFilter);
		calculaDadosTituloFinanceiro(tituloFinanceiroList);
		return tituloFinanceiroList;
	}	
}