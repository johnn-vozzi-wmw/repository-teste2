package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescontoVenda;
import totalcross.sql.ResultSet;

public class DescontoVendaDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescontoVenda();
	}

    private static DescontoVendaDbxDao instance = null;

    public DescontoVendaDbxDao() {
        super(DescontoVenda.TABLE_NAME);
    }
    
    public static DescontoVendaDbxDao getInstance() {
        if (instance == null) {
            instance = new DescontoVendaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        DescontoVenda descontoVenda = new DescontoVenda();
        descontoVenda.rowKey = rs.getString("rowkey");
        descontoVenda.cdEmpresa = rs.getString("cdEmpresa");
        descontoVenda.cdUf = rs.getString("cdUf");
        descontoVenda.vlVenda = ValueUtil.round(rs.getDouble("vlVenda"));
        descontoVenda.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        descontoVenda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        descontoVenda.nuCarimbo = rs.getInt("nuCarimbo");
        descontoVenda.cdUsuario = rs.getString("cdUsuario");
        descontoVenda.dtAlteracao = rs.getDate("dtAlteracao");
        descontoVenda.hrAlteracao = rs.getString("hrAlteracao");
        return descontoVenda;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDUF,");
        sql.append(" VLVENDA,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDUF,");
        sql.append(" VLVENDA,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        DescontoVenda descontoVenda = (DescontoVenda) domain;
        sql.append(Sql.getValue(descontoVenda.cdEmpresa)).append(",");
        sql.append(Sql.getValue(descontoVenda.cdUf)).append(",");
        sql.append(Sql.getValue(descontoVenda.vlVenda)).append(",");
        sql.append(Sql.getValue(descontoVenda.vlPctDesconto)).append(",");
        sql.append(Sql.getValue(descontoVenda.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(descontoVenda.nuCarimbo)).append(",");
        sql.append(Sql.getValue(descontoVenda.cdUsuario)).append(",");
        sql.append(Sql.getValue(descontoVenda.dtAlteracao)).append(",");
        sql.append(Sql.getValue(descontoVenda.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        DescontoVenda descontoVenda = (DescontoVenda) domain;
        sql.append(" VLVENDA = ").append(Sql.getValue(descontoVenda.vlVenda)).append(",");
        sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(descontoVenda.vlPctDesconto)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(descontoVenda.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(descontoVenda.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(descontoVenda.cdUsuario)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(descontoVenda.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(descontoVenda.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        DescontoVenda descontoVenda = (DescontoVenda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descontoVenda.cdEmpresa);
		sqlWhereClause.addAndCondition("CDUF = ", descontoVenda.cdUf);
		sqlWhereClause.addAndCondition("VLVENDA <= ", descontoVenda.vlVendaFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}