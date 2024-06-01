package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DepositoBancario;
import totalcross.sql.ResultSet;

public class DepositoBancarioDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DepositoBancario();
	}

    private static DepositoBancarioDao instance;

    public DepositoBancarioDao() {
        super(DepositoBancario.TABLE_NAME); 
    }
    
    public static DepositoBancarioDao getInstance() {
        if (instance == null) {
            instance = new DepositoBancarioDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        DepositoBancario depositoBancario = new DepositoBancario();
        depositoBancario.rowKey = rs.getString("rowkey");
        depositoBancario.cdEmpresa = rs.getString("cdEmpresa");
        depositoBancario.cdRepresentante = rs.getString("cdRepresentante");
        depositoBancario.dtFechamentoDiario = rs.getDate("dtFechamentoDiario");
        depositoBancario.nuDepositoBancario = rs.getString("nuDepositoBancario");
        depositoBancario.vlTotalDepositoBancario = ValueUtil.round(rs.getDouble("vlTotalDepositoBancario"));
        depositoBancario.dtDepositoBancario = rs.getDate("dtDepositoBancario");
        depositoBancario.cdUsuario = rs.getString("cdUsuario");
        depositoBancario.nuCarimbo = rs.getInt("nuCarimbo");
        depositoBancario.flTipoAlteracao = rs.getString("flTipoAlteracao");
        depositoBancario.hrAlteracao = rs.getString("hrAlteracao");
        depositoBancario.dtAlteracao = rs.getDate("dtAlteracao");
        return depositoBancario;
    }
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DTFECHAMENTODIARIO,");
        sql.append(" NUDEPOSITOBANCARIO,");
        sql.append(" VLTOTALDEPOSITOBANCARIO,");
        sql.append(" DTDEPOSITOBANCARIO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" DTALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DTFECHAMENTODIARIO,");
        sql.append(" NUDEPOSITOBANCARIO,");
        sql.append(" VLTOTALDEPOSITOBANCARIO,");
        sql.append(" DTDEPOSITOBANCARIO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" DTALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        DepositoBancario depositoBancario = (DepositoBancario) domain;
        sql.append(Sql.getValue(depositoBancario.cdEmpresa)).append(",");
        sql.append(Sql.getValue(depositoBancario.cdRepresentante)).append(",");
        sql.append(Sql.getValue(depositoBancario.dtFechamentoDiario)).append(",");
        sql.append(Sql.getValue(depositoBancario.nuDepositoBancario)).append(",");
        sql.append(Sql.getValue(depositoBancario.vlTotalDepositoBancario)).append(",");
        sql.append(Sql.getValue(depositoBancario.dtDepositoBancario)).append(",");
        sql.append(Sql.getValue(depositoBancario.cdUsuario)).append(",");
        sql.append(Sql.getValue(depositoBancario.nuCarimbo)).append(",");
        sql.append(Sql.getValue(depositoBancario.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(depositoBancario.hrAlteracao)).append(",");
        sql.append(Sql.getValue(depositoBancario.dtAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        DepositoBancario depositoBancario = (DepositoBancario) domain;
        sql.append(" VLTOTALDEPOSITOBANCARIO = ").append(Sql.getValue(depositoBancario.vlTotalDepositoBancario)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(depositoBancario.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(depositoBancario.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(depositoBancario.flTipoAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(depositoBancario.hrAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(depositoBancario.dtAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        DepositoBancario depositoBancario = (DepositoBancario) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", depositoBancario.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", depositoBancario.cdRepresentante);
		sqlWhereClause.addAndCondition("DTFECHAMENTODIARIO = ", depositoBancario.dtFechamentoDiario);
		sqlWhereClause.addAndCondition("NUDEPOSITOBANCARIO = ", depositoBancario.nuDepositoBancario);
		sqlWhereClause.addAndCondition("DTFECHAMENTODIARIO < ", depositoBancario.dtFechamentoDiarioFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	DepositoBancario depositoBancario = (DepositoBancario) domain;
    	if (ValueUtil.isNotEmpty(depositoBancario.dtFechamentoDiarioFilter)) {
    		sql.append(" order by DTFECHAMENTODIARIO desc");
    	} else {
    		super.addOrderBy(sql, domain);
    	}
    }
    
}