package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Feriado;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TituloFinanceiroPdbxDao;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class TituloFinanceiroService extends CrudPersonLavendereService {

    private static TituloFinanceiroService instance;

    private TituloFinanceiroService() {
        //--
    }

    public static TituloFinanceiroService getInstance() {
        if (instance == null) {
            instance = new TituloFinanceiroService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TituloFinanceiroPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    /**
     * Define o número de dias de atraso do cliente,
     * baseado no título mais atrasado.
     * @param cliente
     * @return a quantida de dias de atraso do cliente
    * @throws SQLException 
     */
    public int getDiasAtrasoCliente(Cliente cliente) throws SQLException {
    	if (cliente != null) {
    		TituloFinanceiro tituloFinanceiroFilter = new TituloFinanceiro();
    		tituloFinanceiroFilter.cdEmpresa = cliente.cdEmpresa;
    		tituloFinanceiroFilter.cdRepresentante = cliente.cdRepresentante;
    		tituloFinanceiroFilter.filtraSomenteNaoPagos = true;
    		if (LavenderePdaConfig.isLiberaComSenhaClienteRedeAtrasadoNovoPedido() && ValueUtil.isNotEmpty(cliente.cdRede)) {
    			tituloFinanceiroFilter.cdRedeFilter = cliente.cdRede;
    		} else {
    			tituloFinanceiroFilter.cdCliente = cliente.cdCliente;
    		}
    		//--
    		Date dtLastDtVencimento = TituloFinanceiroPdbxDao.getInstance().findLastDtVencimentoByExample(tituloFinanceiroFilter);
    		if (!ValueUtil.isEmpty(dtLastDtVencimento)) {
    			int diasAtrasoTitulo = DateUtil.getDaysBetween(new Date(), dtLastDtVencimento);
    			//--
    			diasAtrasoTitulo = diasAtrasoTitulo - getNuDiasNaoUteisNoPeriodo(dtLastDtVencimento);
    			//--
				if (diasAtrasoTitulo > 0) {
					return diasAtrasoTitulo;
				}
    		}
    	}
		return 0;
    }

    public int getNuDiasNaoUteisNoPeriodo(Date dataLastDtVencimento) throws SQLException {
    	String param = LavenderePdaConfig.usaDiasUteisContabilizarDiasAtrazoCliente;
    	if (ValueUtil.VALOR_NAO.equals(param)) {
    		return 0;
    	}
    	//--
    	Vector listFeriados = FeriadoService.getInstance().findFeriadosPeriodo(DateUtil.getCurrentDate(), dataLastDtVencimento);
    	Hashtable feriadoMap = new Hashtable(listFeriados.size() * 2);
    	Date dateAtual = new Date();
		Date dateVencimento = new Date();
		dateVencimento = dataLastDtVencimento;
		dateAtual = DateUtil.getCurrentDate();
		int count = 0;
		int size = listFeriados.size();
    	for (int i = 0; i < size; i++) {
    		Feriado feriado = (Feriado) listFeriados.items[i];
    		feriadoMap.put(StringUtil.getStringValue(feriado.nuDia) + StringUtil.getStringValue(feriado.nuMes) + StringUtil.getStringValue(feriado.nuAno), "");
		}
    	do {
			String dtFeriadoFixo = StringUtil.getStringValue(dateVencimento.getDay()) + StringUtil.getStringValue(dateVencimento.getMonth()) + "0";
			String dtFeriadoVariavel = StringUtil.getStringValue(dateVencimento.getDay()) + StringUtil.getStringValue(dateVencimento.getMonth()) + StringUtil.getStringValue(dateVencimento.getYear());
    		if (feriadoMap.get(dtFeriadoVariavel) != null || feriadoMap.get(dtFeriadoFixo) != null) {
				count++;
			} else if (DateUtil.getDayOfWeek(dateVencimento) == 0 || DateUtil.getDayOfWeek(dateVencimento) == 6) {
				if (ValueUtil.VALOR_SIM.equals(param) || Feriado.FERIADO_CONTABILIZA_SABADO_DOMINGO.equals(param)) {
					count++;
				} else if (Feriado.FERIADO_CONTABILIZA_DOMINGO.equals(param) && DateUtil.getDayOfWeek(dateVencimento) == 0) {
					count++;
				} else if (Feriado.FERIADO_CONTABILIZA_SABADO.equals(param) && DateUtil.getDayOfWeek(dateVencimento) == 6) {
					count++;
				}
			}
    		DateUtil.addDay(dateVencimento, 1);
		} while (dateVencimento.isBefore(dateAtual));
		return count;
    }

    public double getVlTitulosVencidos(TituloFinanceiro tituloFinanceiro) throws SQLException {
    	return TituloFinanceiroPdbxDao.getInstance().sumVlTitulosByExample(tituloFinanceiro);
    }

    public Vector getVlTitulosVlPagoByExample(Cliente cliente, Date date) throws SQLException {
    	TituloFinanceiro tituloFinanceiroFilter = new TituloFinanceiro();
		tituloFinanceiroFilter.cdEmpresa = cliente.cdEmpresa;
		tituloFinanceiroFilter.cdRepresentante = cliente.cdRepresentante;
		tituloFinanceiroFilter.cdCliente = cliente.cdCliente;
		tituloFinanceiroFilter.dtVencimento = date;
		tituloFinanceiroFilter.filtraSomenteNaoPagos = true;
    	return TituloFinanceiroPdbxDao.getInstance().findVlTitulosVlPagoByExample(tituloFinanceiroFilter);
    }

    
    public double getVlTotalTitulosAtraso() throws SQLException {
		Date dtLimite = DateUtil.getCurrentDate();
		if (SessionLavenderePda.getCliente().nuDiasToleranciaAtraso > 0) {
			DateUtil.decDay(dtLimite, SessionLavenderePda.getCliente().nuDiasToleranciaAtraso);
		}
		TituloFinanceiro tituloFinanceiroFilter = new TituloFinanceiro();
		tituloFinanceiroFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tituloFinanceiroFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TituloFinanceiro.class);
		tituloFinanceiroFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		tituloFinanceiroFilter.dtVencimentoFilter = dtLimite;
		tituloFinanceiroFilter.filtraSomenteNaoPagos = true;
		//--
		Vector tituloFinanceiroList = findAllByExample(tituloFinanceiroFilter);
		int size = tituloFinanceiroList.size();
		double vlTotalAtraso = 0d;
		for (int i = 0; i < size; i++) {
			TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) tituloFinanceiroList.items[i];
			int nuDiasNaoUteisPeriodo = getNuDiasNaoUteisNoPeriodo(DateUtil.getDateValue(tituloFinanceiro.dtVencimento));
			DateUtil.addDay(tituloFinanceiro.dtVencimento, nuDiasNaoUteisPeriodo);
			if (SessionLavenderePda.getCliente().nuDiasToleranciaAtraso > 0) {
				DateUtil.addDay(tituloFinanceiro.dtVencimento, SessionLavenderePda.getCliente().nuDiasToleranciaAtraso);
			}
			if (tituloFinanceiro.dtVencimento.isBefore(DateUtil.getCurrentDate())) {
				vlTotalAtraso += tituloFinanceiro.vlTitulo - tituloFinanceiro.vlPago; 
			}
		}
		return vlTotalAtraso;
	}
    
    public boolean isTituloFinanceiroRelacionadoByPedido(int nuNf) throws SQLException {
    	TituloFinanceiro filter = new TituloFinanceiro();
		filter.nuNf = Integer.toString(nuNf);
		Vector tituloFinanceiroList = findAllByExample(filter);
		return !tituloFinanceiroList.isEmpty();
	}
}