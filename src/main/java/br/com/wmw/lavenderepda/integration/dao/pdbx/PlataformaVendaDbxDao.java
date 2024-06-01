package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PlataformaVenda;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaCliente;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PlataformaVendaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PlataformaVenda();
	}

    private static PlataformaVendaDbxDao instance;

    public PlataformaVendaDbxDao() {
        super(PlataformaVenda.TABLE_NAME); 
    }
    
    public static PlataformaVendaDbxDao getInstance() {
        if (instance == null) {
            instance = new PlataformaVendaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        PlataformaVenda plataformavenda = new PlataformaVenda();
        plataformavenda.rowKey = rs.getString("rowkey");
        plataformavenda.cdEmpresa = rs.getString("cdEmpresa");
        plataformavenda.cdPlataformaVenda = rs.getString("cdPlataformaVenda");
        plataformavenda.dsPlataformaVenda = rs.getString("dsPlataformaVenda");
        plataformavenda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        plataformavenda.cdUsuario = rs.getString("cdUsuario");
        return plataformavenda;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDPLATAFORMAVENDA,");
        sql.append(" tb.DSPLATAFORMAVENDA,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.CDUSUARIO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" DSPLATAFORMAVENDA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        PlataformaVenda plataformavenda = (PlataformaVenda) domain;
        sql.append(Sql.getValue(plataformavenda.cdEmpresa)).append(",");
        sql.append(Sql.getValue(plataformavenda.cdPlataformaVenda)).append(",");
        sql.append(Sql.getValue(plataformavenda.dsPlataformaVenda)).append(",");
        sql.append(Sql.getValue(plataformavenda.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(plataformavenda.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        PlataformaVenda plataformavenda = (PlataformaVenda) domain;
        sql.append(" DSPLATAFORMAVENDA = ").append(Sql.getValue(plataformavenda.dsPlataformaVenda)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(plataformavenda.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(plataformavenda.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PlataformaVenda plataformavenda = (PlataformaVenda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", plataformavenda.cdEmpresa);
		sqlWhereClause.addAndCondition("CDPLATAFORMAVENDA = ", plataformavenda.cdPlataformaVenda);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findAllPlataformaVendaByPlataformaVendaCliente(PlataformaVendaCliente filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT ");
		addSelectColumns(filter, sql);
		sql.append(" FROM ").append(tableName).append(" tb ");
		DaoUtil.addJoinPlataformaVendaCliente(sql, filter.cdEmpresa, filter.cdCliente, filter.cdCentroCusto, filter.ignoreCliente);
		if (ValueUtil.isNotEmpty(filter.cdRepresentante)) {
			sql.append(" AND CDREPRESENTANTE = '").append(filter.cdRepresentante).append("'");
		}
		if (filter.ignoreCliente) addIgnoreClienteGroupBy(sql);
		Vector returnList = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
	        while (rs.next()) {
	        	returnList.addElement(populate(filter, rs));
	        } 
		}
		return returnList;
	}
    
    public Vector findAllDistinctPlataformaVendaByPlataformaVendaCliente(PlataformaVendaCliente filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT DISTINCT ");
		addSelectColumns(filter, sql);
		sql.append(" FROM ").append(tableName).append(" tb ");
		DaoUtil.addJoinPlataformaVendaCliente(sql, filter.cdEmpresa, filter.cdCliente, filter.cdCentroCusto, filter.ignoreCliente);
		if (ValueUtil.isNotEmpty(filter.cdRepresentante)) {			
			sql.append(" AND CDREPRESENTANTE = '").append(filter.cdRepresentante).append("'");
		}
		Vector returnList = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
	        while (rs.next()) {
	        	returnList.addElement(populate(filter, rs));
	        } 
		}
		return returnList;
    }
    
    private void addIgnoreClienteGroupBy(StringBuffer sql) {
    	sql.append(" GROUP BY ");
    	sql.append("tb.CDEMPRESA, ");
    	sql.append("pvc.CDREPRESENTANTE, ");
    	sql.append("tb.CDPLATAFORMAVENDA, ");
    	sql.append("pvc.CDCENTROCUSTO");
    }
    
}