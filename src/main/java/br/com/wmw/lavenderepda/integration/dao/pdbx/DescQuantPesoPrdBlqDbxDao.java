package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DescQuantidadePeso;
import br.com.wmw.lavenderepda.business.domain.DescQuantPesoPrdBlq;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class DescQuantPesoPrdBlqDbxDao extends LavendereCrudDbxDao {

    private static DescQuantPesoPrdBlqDbxDao instance = null;
	

    public DescQuantPesoPrdBlqDbxDao() {
        super(DescQuantPesoPrdBlq.TABLE_NAME);
    }
    
    public static DescQuantPesoPrdBlqDbxDao getInstance() {
        if (instance == null) {
            instance = new DescQuantPesoPrdBlqDbxDao();
        }
        return instance;
    }
    
    //@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
    	DescQuantPesoPrdBlq descQuantidadePesoPrdBlq = new DescQuantPesoPrdBlq();
    	descQuantidadePesoPrdBlq.rowKey = rs.getString("rowkey");
        descQuantidadePesoPrdBlq.cdEmpresa = rs.getString("cdEmpresa");
        descQuantidadePesoPrdBlq.cdRepresentante = rs.getString("cdRepresentante");
        descQuantidadePesoPrdBlq.cdTabelaPreco = rs.getString("cdTabelaPreco");
        descQuantidadePesoPrdBlq.cdProduto = rs.getString("cdProduto");
        descQuantidadePesoPrdBlq.cdUsuario = rs.getString("cdUsuario");
        descQuantidadePesoPrdBlq.flTipoAlteracao = rs.getString("flTipoAlteracao");
        descQuantidadePesoPrdBlq.nuCarimbo = rs.getInt("nuCarimbo");
        return descQuantidadePesoPrdBlq;
    }
    
	//@Override
	protected BaseDomain populateSummary(ResultSet rs) {
		return null;
	}
    
    //@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDPRODUTO,");
        sql.append(" VLPESO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
    	DescQuantPesoPrdBlq descQuantidadePesoPrdBlq = (DescQuantPesoPrdBlq) domain;
        sql.append(Sql.getValue(descQuantidadePesoPrdBlq.cdEmpresa)).append(",");
        sql.append(Sql.getValue(descQuantidadePesoPrdBlq.cdRepresentante)).append(",");
        sql.append(Sql.getValue(descQuantidadePesoPrdBlq.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(descQuantidadePesoPrdBlq.cdProduto)).append(",");
        sql.append(Sql.getValue(descQuantidadePesoPrdBlq.cdUsuario)).append(",");
        sql.append(Sql.getValue(descQuantidadePesoPrdBlq.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(descQuantidadePesoPrdBlq.nuCarimbo));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
    	DescQuantPesoPrdBlq descQuantidadePesoPrdBlq = (DescQuantPesoPrdBlq) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descQuantidadePesoPrdBlq.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descQuantidadePesoPrdBlq.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", descQuantidadePesoPrdBlq.cdTabelaPreco);
		if(descQuantidadePesoPrdBlq.cdProduto != null) {
			sqlWhereClause.addAndCondition("CDPRODUTO = ", descQuantidadePesoPrdBlq.cdProduto);
		}
		sql.append(sqlWhereClause.getSql());
    }
    
    
    public Vector findProdutosBloqueados(BaseDomain domain) throws SQLException {
    	DescQuantidadePeso descQuantidadePeso =	new DescQuantidadePeso();
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT CDPRODUTO FROM ");
    	sql.append(tableName);
    	addWhereByExample(domain, sql);
    	descQuantidadePeso.produtoBloqueadoList = new Vector();
    	try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
    		while (rs.next()) {
    			descQuantidadePeso.produtoBloqueadoList.addElement(rs.getString("CDPRODUTO"));
    		}
    	}
    	return descQuantidadePeso.produtoBloqueadoList;
    	
    }
    
	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescQuantPesoPrdBlq();
	}

}