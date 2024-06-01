package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class TituloFinanceiroPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TituloFinanceiro();
	}

    private static TituloFinanceiroPdbxDao instance;

    public TituloFinanceiroPdbxDao() {
        super(TituloFinanceiro.TABLE_NAME);
    }

    public static TituloFinanceiroPdbxDao getInstance() {
        if (instance == null) {
            instance = new TituloFinanceiroPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
    	TituloFinanceiro tituloFinanceiro = new TituloFinanceiro();
        tituloFinanceiro.rowKey = rs.getString("rowkey");
        tituloFinanceiro.cdEmpresa = rs.getString("cdEmpresa");
        tituloFinanceiro.cdRepresentante = rs.getString("cdRepresentante");
        tituloFinanceiro.cdCliente = rs.getString("cdCliente");
        tituloFinanceiro.nuNf = rs.getString("nuNf");
        tituloFinanceiro.nuSerie = rs.getString("nuSerie");
        tituloFinanceiro.nuTitulo = rs.getString("nuTitulo");
        tituloFinanceiro.nuSubDoc = rs.getString("nuSubDoc");
        tituloFinanceiro.vlNf = ValueUtil.round(rs.getDouble("vlNf"));
        tituloFinanceiro.vlTitulo = ValueUtil.round(rs.getDouble("vlTitulo"));
        tituloFinanceiro.vlPago = ValueUtil.round(rs.getDouble("vlPago"));
        tituloFinanceiro.cdTipoPagamento = rs.getString("cdTipoPagamento");
        tituloFinanceiro.dtVencimento = rs.getDate("dtVencimento");
        tituloFinanceiro.dtPagamento = rs.getDate("dtPagamento");
        return tituloFinanceiro;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUNF,");
        sql.append(" NUSERIE,");
        sql.append(" NUTITULO,");
        sql.append(" NUSUBDOC,");
        sql.append(" VLNF,");
        sql.append(" VLTITULO,");
        sql.append(" VLPAGO,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" DTVENCIMENTO,");
        sql.append(" DTPAGAMENTO");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tituloFinanceiro.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tituloFinanceiro.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", tituloFinanceiro.cdCliente);
		sqlWhereClause.addAndCondition("CDTIPOPAGAMENTO = ", tituloFinanceiro.cdTipoPagamento);
		sqlWhereClause.addAndCondition("NUNF = ", tituloFinanceiro.nuNf);
		sqlWhereClause.addAndCondition("NUSERIE = ", tituloFinanceiro.nuSerie);
		sqlWhereClause.addAndCondition("NUTITULO = ", tituloFinanceiro.nuTitulo);
		sqlWhereClause.addAndCondition("NUSUBDOC = ", tituloFinanceiro.nuSubDoc);
		sqlWhereClause.addAndCondition("DTVENCIMENTO < ", tituloFinanceiro.dtVencimentoFilter);
		if (tituloFinanceiro.filtraSomenteNaoPagos) {
			sqlWhereClause.addAndCondition("VLTITULO > VLPAGO");
			sqlWhereClause.addAndCondition("COALESCE(DTPAGAMENTO,'') = ''");
		} else {
			sqlWhereClause.addAndCondition("DTPAGAMENTO < ", tituloFinanceiro.dtPagamento);
		}
		if (LavenderePdaConfig.isLiberaComSenhaClienteRedeAtrasadoNovoPedido() && ValueUtil.isNotEmpty(tituloFinanceiro.cdRedeFilter)) {
			StringBuffer whereCdRedeCliente = new StringBuffer();
			whereCdRedeCliente.append("CDCLIENTE IN (SELECT CDCLIENTE FROM TBLVPCLIENTE CLI WHERE CLI.CDEMPRESA = CDEMPRESA AND CLI.CDREPRESENTANTE = CDREPRESENTANTE AND CLI.CDREDE = ")
				.append(Sql.getValue(tituloFinanceiro.cdRedeFilter)).append(")");
			sqlWhereClause.addAndCondition(whereCdRedeCliente.toString());
		}
		sql.append(sqlWhereClause.getSql());
    }

    //@Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) domain;
    	if (tituloFinanceiro.ordenacaoDinamica) {
    		super.addOrderBy(sql, tituloFinanceiro);
    	} else {
    		sql.append(" order by DTVENCIMENTO ");
    	}
    }

    public Date findLastDtVencimentoByExample(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT min(DTVENCIMENTO) FROM ");
    	sql.append(tableName);
    	addWhereByExample(domain, sql);
    	return getDate(sql.toString());
    }
    
    public double sumVlTitulosByExample(BaseDomain domain) throws SQLException {
    	TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) domain;
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT SUM(VLTITULO) AS VLTITULO FROM ");
    	sql.append(tableName);
    	addWhereByExample(domain, sql);
    	sql.append(" AND DTVENCIMENTO <= ");
    	sql.append(Sql.getValue(tituloFinanceiro.dtVencimento));
    	return getDouble(sql.toString());
    }

    public Vector findVlTitulosVlPagoByExample(BaseDomain domain) throws SQLException {
    	TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) domain;
    	StringBuffer sql = getSqlBuffer();
    	Vector listVlTituloVlPago = new Vector();
    	sql.append("SELECT VLTITULO, VLPAGO FROM ");
    	sql.append(tableName);
    	addWhereByExample(domain, sql);
    	sql.append(" AND DTVENCIMENTO <= ");
    	sql.append(Sql.getValue(tituloFinanceiro.dtVencimento));
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		while (rs.next()) {
    			TituloFinanceiro tituloFinanceiroList = new TituloFinanceiro();
    			tituloFinanceiroList.vlTitulo = rs.getDouble(1);
    			tituloFinanceiroList.vlPago = rs.getDouble(2);
    			listVlTituloVlPago.addElement(tituloFinanceiroList);
    		}
    		return listVlTituloVlPago;
    	}
    }
    
    public Vector findTitulosAtrasados(BaseDomain domain) throws SQLException {
    	TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) domain;
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT T.* FROM TBLVPTITULOFINANCEIRO T")
    		.append(" JOIN TBLVPCLIENTE C ON T.CDCLIENTE = C.CDCLIENTE");
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition(" T.CDEMPRESA = " + Sql.getValue(tituloFinanceiro.cdEmpresa));
    	sqlWhereClause.addAndCondition(" T.CDREPRESENTANTE = " + Sql.getValue(tituloFinanceiro.cdRepresentante));
    	sqlWhereClause.addAndCondition(" T.CDCLIENTE = " + Sql.getValue(tituloFinanceiro.cdCliente));
    	sqlWhereClause.addAndCondition(" date(T.DTVENCIMENTO, '+' || C.NUDIASTOLERANCIAATRASO || ' day') < CURRENT_DATE");
    	sqlWhereClause.addAndCondition(" DTPAGAMENTO IS NULL");
    	sql.append(sqlWhereClause.getSql());
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector listTituloFinanceiro = new Vector();
    		while (rs.next()) {
    			listTituloFinanceiro.addElement(populate(domain, rs));
        	}
        	return listTituloFinanceiro;
    	}
    }

	protected BasePersonDomain populateColumnsDynFixas(ResultSet rs) throws SQLException {
		return new TituloFinanceiro();
	}
	
    public Vector findAllTituloFinanceiroPeriodoFilters(BaseDomain domain) throws SQLException {
    	TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) domain;
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT ");
    	addSelectColumns(domain, sql);
    	sql.append(" FROM ");
    	sql.append(tableName);
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition(" CDEMPRESA = "+Sql.getValue(tituloFinanceiro.cdEmpresa));
    	sqlWhereClause.addAndCondition(" CDREPRESENTANTE = "+Sql.getValue(tituloFinanceiro.cdRepresentante));
    	sqlWhereClause.addAndCondition(" CDCLIENTE = "+Sql.getValue(tituloFinanceiro.cdCliente));
    	
    	if (LavenderePdaConfig.filtroPeriodoVencimentoConfigTituloFinanceiro()) {
    		if (tituloFinanceiro.dtIncialVencimento != null) sqlWhereClause.addAndCondition(" DTVENCIMENTO >= " + Sql.getValue(tituloFinanceiro.dtIncialVencimento));
    		if (tituloFinanceiro.dtFinalVencimento != null) sqlWhereClause.addAndCondition(" DTVENCIMENTO <=" + Sql.getValue(tituloFinanceiro.dtFinalVencimento));
    	}
    	if (LavenderePdaConfig.filtroPeriodoPagamentoConfigTituloFinanceiro()) {
    		if (tituloFinanceiro.dtIncialPagamento != null) sqlWhereClause.addAndCondition(" DTPAGAMENTO >=" + Sql.getValue(tituloFinanceiro.dtIncialPagamento));
    		if (tituloFinanceiro.dtFinalPagamento != null) sqlWhereClause.addAndCondition(" DTPAGAMENTO <=" + Sql.getValue(tituloFinanceiro.dtFinalPagamento));
    	}
    	if (LavenderePdaConfig.filtroTituloPagoConfigTituloFinanceiro() && tituloFinanceiro.statusTituloFinanceiro != null) {
	    	if (Messages.COMBOBOX_TITULOFINANCEIRO_PAGO.equals(tituloFinanceiro.statusTituloFinanceiro)) {
	    		sqlWhereClause.addAndCondition(" DTPAGAMENTO IS NOT NULL");
	    	}
	    	if (Messages.COMBOBOX_TITULOFINANCEIRO_NAO_PAGO.equals(tituloFinanceiro.statusTituloFinanceiro)) {
	    		sqlWhereClause.addAndCondition(" DTPAGAMENTO IS NULL");
	    	}    	
    	}
    	if (LavenderePdaConfig.usaFiltroTituloFinanceiroPorTipoPagamento && (tituloFinanceiro.cdTipoPagamento != null && !tituloFinanceiro.cdTipoPagamento.isEmpty())) {
    		sqlWhereClause.addAndCondition(" CDTIPOPAGAMENTO = "+Sql.getValue(tituloFinanceiro.cdTipoPagamento));
    	}
    	sql.append(sqlWhereClause.getSql());
    	addOrderBy(sql, domain);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector listTituloFinanceiro = new Vector();
    		while (rs.next()) {
    			listTituloFinanceiro.addElement(populate(domain, rs));
        	}
        	return listTituloFinanceiro;
    	}
    }
}