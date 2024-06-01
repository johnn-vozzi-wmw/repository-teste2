package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.BonificacaoSaldo;
import totalcross.sql.ResultSet;

public class BonificacaoSaldoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new BonificacaoSaldo();
	}

    private static BonificacaoSaldoDbxDao instance;

    public BonificacaoSaldoDbxDao() {
        super(BonificacaoSaldo.TABLE_NAME); 
    }
    
    public static BonificacaoSaldoDbxDao getInstance() {
        if (instance == null) {
            instance = new BonificacaoSaldoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        BonificacaoSaldo bonificacaoSaldo = new BonificacaoSaldo();
        bonificacaoSaldo.rowKey = rs.getString("rowkey");
        bonificacaoSaldo.cdEmpresa = rs.getString("cdEmpresa");
        bonificacaoSaldo.cdRepresentante = rs.getString("cdRepresentante");
        bonificacaoSaldo.flOrigemSaldo = rs.getString("flOrigemSaldo");
        bonificacaoSaldo.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
        bonificacaoSaldo.dsControleAtualizacao = rs.getString("dsControleAtualizacao");
        bonificacaoSaldo.nuCarimbo = rs.getInt("nuCarimbo");
        bonificacaoSaldo.cdUsuario = rs.getString("cdUsuario");
        bonificacaoSaldo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return bonificacaoSaldo;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMSALDO,");
        sql.append(" VLSALDO,");
        sql.append(" DSCONTROLEATUALIZACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMSALDO,");
        sql.append(" VLSALDO,");
        sql.append(" DSCONTROLEATUALIZACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        BonificacaoSaldo bonificacaoSaldo = (BonificacaoSaldo) domain;
        sql.append(Sql.getValue(bonificacaoSaldo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(bonificacaoSaldo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(bonificacaoSaldo.flOrigemSaldo)).append(",");
        sql.append(Sql.getValue(bonificacaoSaldo.vlSaldo)).append(",");
        sql.append(Sql.getValue(bonificacaoSaldo.dsControleAtualizacao)).append(",");
        sql.append(Sql.getValue(bonificacaoSaldo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(bonificacaoSaldo.cdUsuario)).append(",");
        sql.append(Sql.getValue(bonificacaoSaldo.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        BonificacaoSaldo bonificacaoSaldo = (BonificacaoSaldo) domain;
        sql.append(" VLSALDO = ").append(Sql.getValue(bonificacaoSaldo.vlSaldo)).append(",");
        sql.append(" DSCONTROLEATUALIZACAO = ").append(Sql.getValue(bonificacaoSaldo.dsControleAtualizacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(bonificacaoSaldo.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(bonificacaoSaldo.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(bonificacaoSaldo.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        BonificacaoSaldo bonificacaoSaldo = (BonificacaoSaldo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", bonificacaoSaldo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", bonificacaoSaldo.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMSALDO = ", bonificacaoSaldo.flOrigemSaldo);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}