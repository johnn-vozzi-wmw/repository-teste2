package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCli;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class MetaVendaCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetaVendaCli();
	}

    private static MetaVendaCliDbxDao instance;
	

    public MetaVendaCliDbxDao() {
        super(MetaVendaCli.TABLE_NAME);
    }

    public static MetaVendaCliDbxDao getInstance() {
        if (instance == null) {
            instance = new MetaVendaCliDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetaVendaCli metaVendaCli = new MetaVendaCli();
        metaVendaCli.rowKey = rs.getString("rowkey");
        metaVendaCli.cdEmpresa = rs.getString("cdEmpresa");
        metaVendaCli.cdRepresentante = rs.getString("cdRepresentante");
        metaVendaCli.cdMetaVendaCli = rs.getString("cdMetaVendaCli");
        metaVendaCli.cdCliente = rs.getString("cdCliente");
        metaVendaCli.dsMetaVendaCli = rs.getString("dsMetaVendaCli");
        metaVendaCli.dtInicialVigencia = rs.getDate("dtInicialVigencia");
        metaVendaCli.dtFinalVigencia = rs.getDate("dtFinalVigencia");
        metaVendaCli.nuCarimbo = rs.getInt("nuCarimbo");
        metaVendaCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        metaVendaCli.cdUsuario = rs.getString("cdUsuario");
        return metaVendaCli;
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
        sql.append(" CDMETAVENDACLI,");
        sql.append(" CDCLIENTE,");
        sql.append(" DSMETAVENDACLI,");
        sql.append(" DTINICIALVIGENCIA,");
        sql.append(" DTFINALVIGENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDMETAVENDACLI,");
        sql.append(" CDCLIENTE,");
        sql.append(" DSMETAVENDACLI,");
        sql.append(" DTINICIALVIGENCIA,");
        sql.append(" DTFINALVIGENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaCli metaVendaCli = (MetaVendaCli) domain;
        sql.append(Sql.getValue(metaVendaCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(metaVendaCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(metaVendaCli.cdMetaVendaCli)).append(",");
        sql.append(Sql.getValue(metaVendaCli.cdCliente)).append(",");
        sql.append(Sql.getValue(metaVendaCli.dsMetaVendaCli)).append(",");
        sql.append(Sql.getValue(metaVendaCli.dtInicialVigencia)).append(",");
        sql.append(Sql.getValue(metaVendaCli.dtFinalVigencia)).append(",");
        sql.append(Sql.getValue(metaVendaCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(metaVendaCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(metaVendaCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaCli metaVendaCli = (MetaVendaCli) domain;
        sql.append(" DSMETAVENDACLI = ").append(Sql.getValue(metaVendaCli.dsMetaVendaCli)).append(",");
        sql.append(" DTINICIALVIGENCIA = ").append(Sql.getValue(metaVendaCli.dtInicialVigencia)).append(",");
        sql.append(" DTFINALVIGENCIA = ").append(Sql.getValue(metaVendaCli.dtFinalVigencia)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(metaVendaCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(metaVendaCli.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(metaVendaCli.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaCli metaVendaCli = (MetaVendaCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", metaVendaCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", metaVendaCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMETAVENDACLI = ", metaVendaCli.cdMetaVendaCli);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", metaVendaCli.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
	public Vector findAllMetaVendaCli(MetaVendaCli metaVendaCliFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT METAVENDACLI.CDEMPRESA, METAVENDACLI.CDREPRESENTANTE, METAVENDACLI.CDMETAVENDACLI, METAVENDACLI.CDCLIENTE, METAVENDACLI.DSMETAVENDACLI" + 
				   ",METAVENDACLI.DTINICIALVIGENCIA, METAVENDACLI.DTFINALVIGENCIA, CLIENTE.NMRAZAOSOCIAL, CLIENTE.NUCNPJ, METAVENDACLI.VLMETA, METAVENDACLI.VLREALIZADO, "
				   + " CASE WHEN METAVENDACLI.VLREALIZADO < METAVENDACLI.VLMETA THEN METAVENDACLI.VLMETA - METAVENDACLI.VLREALIZADO ELSE 0 END AS VLRESTANTE, "
				   + " ((METAVENDACLI.VLREALIZADO * 100) / METAVENDACLI.VLMETA) as pctRealizado,  "
				   + " CASE WHEN (METAVENDACLI.VLMETA > 0 AND ((METAVENDACLI.VLREALIZADO * 100) / METAVENDACLI.VLMETA) < 100) "
				   + "		THEN 100 - ((METAVENDACLI.VLREALIZADO * 100) / METAVENDACLI.VLMETA) "
				   + "		ELSE 0 END as pctRestante "
				   + " FROM ");
		sql.append(tableName).append(" METAVENDACLI");
		sql.append(" JOIN TBLVPCLIENTE CLIENTE ON" + 
				   "	CLIENTE.CDCLIENTE = METAVENDACLI.CDCLIENTE" + 
				   " AND CLIENTE.CDEMPRESA = METAVENDACLI.CDEMPRESA" + 
				   " AND CLIENTE.CDREPRESENTANTE = METAVENDACLI.CDREPRESENTANTE");
		
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("METAVENDACLI.CDEMPRESA = ", metaVendaCliFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("METAVENDACLI.CDREPRESENTANTE = ", metaVendaCliFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("METAVENDACLI.CDCLIENTE = ", metaVendaCliFilter.cdCliente);
		if(ValueUtil.isNotEmpty(metaVendaCliFilter.dsMetaVendaCli) && ValueUtil.isNotEmpty(metaVendaCliFilter.cdMetaVendaCli)) {
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addAndLikeCondition("METAVENDACLI.DSMETAVENDACLI ", metaVendaCliFilter.dsMetaVendaCli);
			sqlWhereClause.addOrCondition("METAVENDACLI.CDMETAVENDACLI = ", metaVendaCliFilter.cdMetaVendaCli);
			sqlWhereClause.addEndMultipleCondition();
		}
		sql.append(sqlWhereClause.getSql().toUpperCase());
		addOrderBy(sql, metaVendaCliFilter);
		boolean isNeedCnpjCliente = LavenderePdaConfig.getConfigDescricaoEntidadesCliente() == 1;
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector metaVendaCliVector = new Vector();
        	while (rs.next()) {
        		MetaVendaCli metaVendaCli = new MetaVendaCli();
        		metaVendaCli.cdEmpresa =  rs.getString(1);
        		metaVendaCli.cdRepresentante = rs.getString(2);
        		metaVendaCli.cdMetaVendaCli = rs.getString(3);
        		metaVendaCli.cdCliente = rs.getString(4);
        		metaVendaCli.dsMetaVendaCli = rs.getString(5);
        		metaVendaCli.dtInicialVigencia = rs.getDate(6);
        		metaVendaCli.dtFinalVigencia = rs.getDate(7);
        		metaVendaCli.vlMeta = rs.getDouble(10);
        		metaVendaCli.vlRealizado = rs.getDouble(11);
        		metaVendaCli.vlRealizar = rs.getDouble(12);
        		metaVendaCli.pctRealizado = rs.getDouble(13);
        		metaVendaCli.pctRestante = rs.getDouble(14);
        		
        		Cliente cliente = new Cliente();
        		cliente.cdCliente = metaVendaCli.cdCliente;
        		cliente.nmRazaoSocial = rs.getString(8);
        		if(isNeedCnpjCliente) {
        			cliente.nuCnpj = rs.getString(9);
        		}
        		
        		metaVendaCli.cliente = cliente;
        		metaVendaCliVector.addElement(metaVendaCli);
        	}
        	return metaVendaCliVector;
    	}
	}

}