package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PagamentoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Pagamento();
	}

    private static PagamentoPdbxDao instance;

    public PagamentoPdbxDao() {
        super(Pagamento.TABLE_NAME);
    }

    public static PagamentoPdbxDao getInstance() {
        if (instance == null) {
            instance = new PagamentoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Pagamento pagamento = new Pagamento();
        pagamento.rowKey = rs.getString("rowkey");
        pagamento.cdEmpresa = rs.getString("cdEmpresa");
        pagamento.cdRepresentante = rs.getString("cdRepresentante");
        pagamento.cdCliente = rs.getString("cdCliente");
        pagamento.cdPagamento = rs.getString("cdPagamento");
        pagamento.cdTipoPagamento = rs.getString("cdTipoPagamento");
        pagamento.dtPagamento = rs.getDate("dtPagamento");
        pagamento.vlPago = ValueUtil.round(rs.getDouble("vlPago"));
        if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
        	pagamento.vlAdicionalPago = ValueUtil.round(rs.getDouble("vlAdicionalPago"));
        }
        pagamento.dsObservacao = rs.getString("dsObservacao");
        pagamento.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pagamento.nuCarimbo = rs.getInt("nuCarimbo");
        pagamento.cdUsuario = rs.getString("cdUsuario");
        return pagamento;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPAGAMENTO,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" DTPAGAMENTO,");
        sql.append(" VLPAGO,");
        sql.append(" dsObservacao,");
        if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
        	sql.append(" VLADICIONALPAGO,");
        }
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" nuCarimbo,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPAGAMENTO,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" DTPAGAMENTO,");
        sql.append(" VLPAGO,");
        sql.append(" dsObservacao,");
       	sql.append(" VLADICIONALPAGO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" nuCarimbo,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Pagamento pagamento = (Pagamento) domain;
        sql.append(Sql.getValue(pagamento.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pagamento.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pagamento.cdCliente)).append(",");
        sql.append(Sql.getValue(pagamento.cdPagamento)).append(",");
        sql.append(Sql.getValue(pagamento.cdTipoPagamento)).append(",");
        sql.append(Sql.getValue(pagamento.dtPagamento)).append(",");
        sql.append(Sql.getValue(pagamento.vlPago)).append(",");
        sql.append(Sql.getValue(pagamento.dsObservacao)).append(",");
        sql.append(Sql.getValue(pagamento.vlAdicionalPago)).append(",");
        sql.append(Sql.getValue(pagamento.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(pagamento.nuCarimbo)).append(",");
        sql.append(Sql.getValue(pagamento.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Pagamento pagamento = (Pagamento) domain;
        sql.append(" CDTIPOPAGAMENTO = ").append(Sql.getValue(pagamento.cdTipoPagamento)).append(",");
        sql.append(" DTPAGAMENTO = ").append(Sql.getValue(pagamento.dtPagamento)).append(",");
        sql.append(" VLPAGO = ").append(Sql.getValue(pagamento.vlPago)).append(",");
        sql.append(" dsObservacao = ").append(Sql.getValue(pagamento.dsObservacao)).append(",");
        sql.append(" VLADICIONALPAGO = ").append(Sql.getValue(pagamento.vlAdicionalPago)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pagamento.flTipoAlteracao)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(pagamento.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(pagamento.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Pagamento pagamento = (Pagamento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pagamento.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pagamento.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", pagamento.cdCliente);
		sqlWhereClause.addAndCondition("CDPAGAMENTO = ", pagamento.cdPagamento);
		sqlWhereClause.addAndCondition("FLTIPOALTERACAO != ", pagamento.flTipoAlteracao);
		if (pagamento.flFilterTipoAlteracaoOriginal) {
			sqlWhereClause.addAndConditionForced("FLTIPOALTERACAO != ", Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
		}
		if (ValueUtil.isNotEmpty(pagamento.dtPagamentoInicialFilter)) {
			sqlWhereClause.addAndCondition("DTPAGAMENTO >= ", pagamento.dtPagamentoInicialFilter);
		}
		if (ValueUtil.isNotEmpty(pagamento.dtPagamentoFinalFilter)) {
			sqlWhereClause.addAndCondition("DTPAGAMENTO <= ", pagamento.dtPagamentoFinalFilter);
		}
		if (pagamento.flFiltraFechamentoDiario) {
			sqlWhereClause.addAndCondition("DTPAGAMENTO < ", pagamento.dtPagamento);
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public Vector findAllNotSentByExample(BaseDomain domain) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domain, sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addWhereByExample(domain, sql);
        sql.append(" AND ").append("FLTIPOALTERACAO != ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
        addOrderBy(sql, domain);
        return findAll(domain, sql.toString());
    }

    public Vector findAllGroupByTipoPagamento(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select CDTIPOPAGAMENTO, sum(vlPago + vlAdicionalPago) as vlPago");
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(domain, sql);
    	sql.append(" group by CDTIPOPAGAMENTO");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				Pagamento p = new Pagamento();
				p.cdTipoPagamento = rs.getString("CDTIPOPAGAMENTO");
				p.vlPago = rs.getDouble("VLPAGO");
				result.addElement(p);
			}
			return result;
		}
    }
    
	public double somaTotalDinheiroPorData(String cdEmpresa, String cdRepresentante, Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(PG.VLPAGO + PG.VLADICIONALPAGO) AS TOTAL  ");
		sql.append(" FROM ").append(tableName).append(" AS PG ");
		sql.append(" INNER JOIN TBLVPTIPOPAGAMENTO TP ON (TP.CDEMPRESA = PG.CDEMPRESA AND TP.CDREPRESENTANTE = PG.CDREPRESENTANTE AND TP.CDTIPOPAGAMENTO = PG.CDTIPOPAGAMENTO) ");
		sql.append(" WHERE PG.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND PG.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" AND PG.DTPAGAMENTO >= ").append(Sql.getValue(dtPagamentoInicial));
		sql.append(" AND PG.DTPAGAMENTO <= ").append(Sql.getValue(dtPagamentoFinal));
		sql.append(" AND (tp.flCheque is null OR tp.flCheque = 'N') ");
		sql.append(" AND (tp.flBoleto is null OR tp.flBoleto = 'N') ");
		return getDouble(sql.toString());
	}

	public double somaTotalChequePorData(String cdEmpresa, String cdRepresentante, Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(PG.VLPAGO + PG.VLADICIONALPAGO) AS TOTAL  ");
		sql.append(" FROM ").append(tableName).append(" AS PG ");
		sql.append(" INNER JOIN TBLVPTIPOPAGAMENTO TP ON (TP.CDEMPRESA = PG.CDEMPRESA AND TP.CDREPRESENTANTE = PG.CDREPRESENTANTE AND TP.CDTIPOPAGAMENTO = PG.CDTIPOPAGAMENTO) ");
		sql.append(" WHERE PG.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND PG.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" AND PG.DTPAGAMENTO >= ").append(Sql.getValue(dtPagamentoInicial));
		sql.append(" AND PG.DTPAGAMENTO <= ").append(Sql.getValue(dtPagamentoFinal));
		sql.append(" AND tp.flCheque = 'S' ");
		sql.append(" AND (tp.flBoleto is null OR tp.flBoleto = 'N') ");
		return getDouble(sql.toString());
	}
	
	public double somaTotalBoletoPorData(String cdEmpresa, String cdRepresentante, Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(PG.VLPAGO + PG.VLADICIONALPAGO) AS TOTAL  ");
		sql.append(" FROM ").append(tableName).append(" AS PG ");
		sql.append(" INNER JOIN TBLVPTIPOPAGAMENTO TP ON (TP.CDEMPRESA = PG.CDEMPRESA AND TP.CDREPRESENTANTE = PG.CDREPRESENTANTE AND TP.CDTIPOPAGAMENTO = PG.CDTIPOPAGAMENTO) ");
		sql.append(" WHERE PG.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND PG.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" AND PG.DTPAGAMENTO >= ").append(Sql.getValue(dtPagamentoInicial));
		sql.append(" AND PG.DTPAGAMENTO <= ").append(Sql.getValue(dtPagamentoFinal));
		sql.append(" AND (tp.flCheque is null OR tp.flCheque = 'N') ");
		sql.append(" AND tp.flBoleto = 'S' ");
		return getDouble(sql.toString());
	}
	
	public double somaTotalOutrosPorData(String cdEmpresa, String cdRepresentante, Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(PG.VLPAGO + PG.VLADICIONALPAGO) AS TOTAL  ");
		sql.append(" FROM ").append(tableName).append(" AS PG ");
		sql.append(" WHERE PG.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND PG.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" AND PG.DTPAGAMENTO >= ").append(Sql.getValue(dtPagamentoInicial));
		sql.append(" AND PG.DTPAGAMENTO <= ").append(Sql.getValue(dtPagamentoFinal));
		sql.append(" AND NOT EXISTS (SELECT CDTIPOPAGAMENTO FROM TBLVPTIPOPAGAMENTO TP "); 
		sql.append("                  WHERE TP.CDEMPRESA = PG.CDEMPRESA ");
		sql.append("                    AND TP.CDREPRESENTANTE = PG.CDREPRESENTANTE ");
		sql.append(" 				    AND TP.CDTIPOPAGAMENTO = PG.CDTIPOPAGAMENTO) ");
		return getDouble(sql.toString());
	}	
	
	public double somaTotalPagamentosPorData(String cdEmpresa, String cdRepresentante, Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(PG.VLPAGO + PG.VLADICIONALPAGO) AS TOTAL  ");
		sql.append(" FROM ").append(tableName).append(" AS PG ");
		sql.append(" WHERE PG.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND PG.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" AND PG.DTPAGAMENTO >= ").append(Sql.getValue(dtPagamentoInicial));
		sql.append(" AND PG.DTPAGAMENTO <= ").append(Sql.getValue(dtPagamentoFinal));
		return getDouble(sql.toString());
	}
}