package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ConsignacaoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Consignacao();
	}

    private static ConsignacaoPdbxDao instance;

    public ConsignacaoPdbxDao() {
        super(Consignacao.TABLE_NAME);
    }

    public static ConsignacaoPdbxDao getInstance() {
        if (instance == null) {
            instance = new ConsignacaoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Consignacao consignacao = new Consignacao();
        consignacao.rowKey = rs.getString("rowkey");
        consignacao.cdEmpresa = rs.getString("cdEmpresa");
        consignacao.cdRepresentante = rs.getString("cdRepresentante");
        consignacao.cdCliente = rs.getString("cdCliente");
        consignacao.cdConsignacao = rs.getString("cdConsignacao");
        consignacao.dtConsignacao = rs.getDate("dtConsignacao");
        consignacao.dtProximaVisita = rs.getDate("dtProximaVisita");
        consignacao.vlTotalConsignado = ValueUtil.round(rs.getDouble("vlTotalconsignado"));
        consignacao.flOrigemPedido = rs.getString("flOrigemPedido");
        consignacao.flPagamentoEfetuado = rs.getString("flPagamentoEfetuado");
        consignacao.nuPedido = rs.getString("nuPedido");
        consignacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
        consignacao.nuCarimbo = rs.getInt("nuCarimbo");
        consignacao.cdUsuario = rs.getString("cdUsuario");
        return consignacao;
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
        sql.append(" CDCONSIGNACAO,");
        sql.append(" DTCONSIGNACAO,");
        sql.append(" DTPROXIMAVISITA,");
        sql.append(" VLTOTALCONSIGNADO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" FLPAGAMENTOEFETUADO,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
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
        sql.append(" CDCONSIGNACAO,");
        sql.append(" DTCONSIGNACAO,");
        sql.append(" DTPROXIMAVISITA,");
        sql.append(" VLTOTALCONSIGNADO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" FLPAGAMENTOEFETUADO,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Consignacao consignacao = (Consignacao) domain;
        sql.append(Sql.getValue(consignacao.cdEmpresa)).append(",");
        sql.append(Sql.getValue(consignacao.cdRepresentante)).append(",");
        sql.append(Sql.getValue(consignacao.cdCliente)).append(",");
        sql.append(Sql.getValue(consignacao.cdConsignacao)).append(",");
        sql.append(Sql.getValue(consignacao.dtConsignacao)).append(",");
        sql.append(Sql.getValue(consignacao.dtProximaVisita)).append(",");
        sql.append(Sql.getValue(consignacao.vlTotalConsignado)).append(",");
        sql.append(Sql.getValue(consignacao.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(consignacao.flPagamentoEfetuado)).append(",");
        sql.append(Sql.getValue(consignacao.nuPedido)).append(",");
        sql.append(Sql.getValue(consignacao.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(1)).append(",");
        sql.append(Sql.getValue(consignacao.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Consignacao consignacao = (Consignacao) domain;
        sql.append(" DTCONSIGNACAO = ").append(Sql.getValue(consignacao.dtConsignacao)).append(",");
        sql.append(" DTPROXIMAVISITA = ").append(Sql.getValue(consignacao.dtProximaVisita)).append(",");
        sql.append(" VLTOTALCONSIGNADO = ").append(Sql.getValue(consignacao.vlTotalConsignado)).append(",");
        sql.append(" FLORIGEMPEDIDO = ").append(Sql.getValue(consignacao.flOrigemPedido)).append(",");
        sql.append(" FLPAGAMENTOEFETUADO = ").append(Sql.getValue(consignacao.flPagamentoEfetuado)).append(",");
        sql.append(" NUPEDIDO = ").append(Sql.getValue(consignacao.nuPedido)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(consignacao.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(consignacao.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(consignacao.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Consignacao consignacao = (Consignacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", consignacao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", consignacao.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", consignacao.cdCliente);
		sqlWhereClause.addAndCondition("CDCONSIGNACAO = ", consignacao.cdConsignacao);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public BaseDomain findConsignacaoInOpen(BaseDomain domain) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domain, sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addWhereByExample(domain, sql);
        sql.append(" and nuPedido = ''");
        addOrderBy(sql, domain);
        try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
	        if (rs.next()) {
	            return populate(domain, rs);
	        }
        }
        return null;
    }

    public BaseDomain findConsignacaoFechada(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	addSelectColumns(domain, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(domain, sql);
    	sql.append(" and nuPedido != ''");
    	sql.append(" order by dtConsignacao desc, rowid desc");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		if (rs.next()) {
    			return populate(domain, rs);
    		}
    	}
    	return null;
    }

	public void retiraNuPedido(Consignacao consignacao) throws SQLException {
		String rowKey = consignacao.getRowKey();
		consignacao.nuPedido = "";
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ");
		sql.append(tableName);
		sql.append(" set");
		addUpdateValues(consignacao, sql);
		sql.append(" where rowkey = ");
		sql.append(Sql.getValue(rowKey));
		executeUpdate(sql.toString());
	}

	public void updateFlPagamentoEfetuado(Consignacao consignacao) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ");
		sql.append(tableName);
		sql.append(" set FLPAGAMENTOEFETUADO = ").append(Sql.getValue("S"));
        sql.append(", FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		addWhereByExample(consignacao, sql);
		sql.append(" and NUPEDIDO != ''");
		//--
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) { /*Nenhum registro afetado não tem problema*/ }
	}


	public Vector findAllConsigSentAndPaid(BaseDomain domain) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domain, sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addWhereByExample(domain, sql);
        sql.append(" AND ").append("NUPEDIDO != ").append("''");
        sql.append(" AND ").append("FLPAGAMENTOEFETUADO = ").append(Sql.getValue("S"));
        sql.append(" AND ").append("FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
        addOrderBy(sql, domain);
        //--
        return findAll(domain, sql.toString());
	}
}