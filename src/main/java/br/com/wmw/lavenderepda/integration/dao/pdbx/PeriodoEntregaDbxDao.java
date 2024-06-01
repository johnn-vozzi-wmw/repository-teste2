package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.PeriodoEntrega;
import br.com.wmw.lavenderepda.business.domain.PreferenciaFuncao;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PeriodoEntregaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PeriodoEntrega();
	}

    private static PeriodoEntregaDbxDao instance = null;
	

    public PeriodoEntregaDbxDao() {
        super(PeriodoEntrega.TABLE_NAME);
    }
    
    public static PeriodoEntregaDbxDao getInstance() {
        if (instance == null) {
            instance = new PeriodoEntregaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        PeriodoEntrega periodoEntrega = new PeriodoEntrega();
        periodoEntrega.rowKey = rs.getString("rowkey");
        periodoEntrega.cdPeriodoEntrega = rs.getString("cdPeriodoEntrega");
        periodoEntrega.dsPeriodoEntrega = rs.getString("dsPeriodoEntrega");
        periodoEntrega.hrPeriodoEntregaInicial = rs.getString("hrPeriodoEntregaInicial");
        periodoEntrega.hrPeriodoEntregaFinal = rs.getString("hrPeriodoEntregaFinal");
        periodoEntrega.cdEmpresa = rs.getString("cdEmpresa");
        periodoEntrega.nuCarimbo = rs.getInt("nuCarimbo");
        periodoEntrega.flTipoAlteracao = rs.getString("flTipoAlteracao");
        periodoEntrega.cdUsuario = rs.getString("cdUsuario");
        periodoEntrega.dtAlteracao = rs.getDate("dtAlteracao");
        periodoEntrega.hrAlteracao = rs.getString("hrAlteracao");
        return periodoEntrega;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDPERIODOENTREGA,");
        sql.append(" DSPERIODOENTREGA,");
        sql.append(" HRPERIODOENTREGAINICIAL,");
        sql.append(" HRPERIODOENTREGAFINAL,");
        sql.append(" CDEMPRESA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDPERIODOENTREGA,");
        sql.append(" DSPERIODOENTREGA,");
        sql.append(" HRPERIODOENTREGAINICIAL,");
        sql.append(" HRPERIODOENTREGAFINAL,");
        sql.append(" CDEMPRESA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        PeriodoEntrega periodoEntrega = (PeriodoEntrega) domain;
        sql.append(Sql.getValue(periodoEntrega.cdPeriodoEntrega)).append(",");
        sql.append(Sql.getValue(periodoEntrega.dsPeriodoEntrega)).append(",");
        sql.append(Sql.getValue(periodoEntrega.hrPeriodoEntregaInicial)).append(",");
        sql.append(Sql.getValue(periodoEntrega.hrPeriodoEntregaFinal)).append(",");
        sql.append(Sql.getValue(periodoEntrega.cdEmpresa)).append(",");
        sql.append(Sql.getValue(periodoEntrega.nuCarimbo)).append(",");
        sql.append(Sql.getValue(periodoEntrega.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(periodoEntrega.cdUsuario)).append(",");
        sql.append(Sql.getValue(periodoEntrega.dtAlteracao)).append(",");
        sql.append(Sql.getValue(periodoEntrega.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        PeriodoEntrega periodoEntrega = (PeriodoEntrega) domain;
        sql.append(" DSPERIODOENTREGA = ").append(Sql.getValue(periodoEntrega.dsPeriodoEntrega)).append(",");
        sql.append(" HRPERIODOENTREGAINICIAL = ").append(Sql.getValue(periodoEntrega.hrPeriodoEntregaInicial)).append(",");
        sql.append(" HRPERIODOENTREGAFINAL = ").append(Sql.getValue(periodoEntrega.hrPeriodoEntregaFinal)).append(",");
        sql.append(" CDEMPRESA = ").append(Sql.getValue(periodoEntrega.cdEmpresa)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(periodoEntrega.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(periodoEntrega.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(periodoEntrega.cdUsuario)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(periodoEntrega.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(periodoEntrega.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PeriodoEntrega periodoEntrega = (PeriodoEntrega) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDPERIODOENTREGA = ", periodoEntrega.cdPeriodoEntrega);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public Vector findAllPeriodoEntrega(String cdPeriodoEntrega) throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        sql.append(" where (cdEmpresa = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
        sql.append(" or cdEmpresa = '' ");
        sql.append(" or cdEmpresa is null )");
        if (!SessionLavenderePda.hasPreferencia(PreferenciaFuncao.PERMITE_USO_PERIODO_ENTREGA_RESTRITO)) {
        	sql.append(" and ((flRestrito = 'N' or flRestrito is null or flRestrito = '') ");
        	if (ValueUtil.isNotEmpty(cdPeriodoEntrega)) {
        		sql.append(" or cdPeriodoEntrega = ").append(Sql.getValue(cdPeriodoEntrega));
        	}
        	sql.append(" )");
        }
        sql.append(" order by dsPeriodoEntrega");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector periodoEntregaList = new Vector();
			while (rs.next()) {
				periodoEntregaList.addElement(populate(getBaseDomainDefault(), rs));
			}
			return periodoEntregaList;
		}
	}
	
	public Vector findPeriodoEntregaCliEndereco(Vector list) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select ");  
		addSelectColumns(null, sql);
		sql.append(" from " + tableName);
		String nmColuna = "cdPeriodoEntrega";
		int size = list.size();
		ClienteEndereco clienteEndereco;
		if (size > 0) {
			sql.append(" where ");
			for (int i = 0; i < size; i++) {
				clienteEndereco = (ClienteEndereco)list.items[i];
				if (i > 0) {
					sql.append(" or ");
				}
				sql.append(nmColuna + " = " + Sql.getValue(clienteEndereco.cdPeriodoEntrega));
			}
		}
		return findAll(null, sql.toString());
	}

	public Vector findAllCampoDinamicoComboBox(String[] columns) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	for (int i = 0; i < columns.length; i++) {
    		sql.append(columns[i]);
    		if ((i + 1) != columns.length) {
        		sql.append(", ");
    		}
    	}
    	sql.append(" from ");
    	sql.append(tableName);
		sql.append(" where (cdEmpresa = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
		sql.append(" or cdEmpresa = '' or cdEmpresa is null)");
		if (!SessionLavenderePda.hasPreferencia(PreferenciaFuncao.PERMITE_USO_PERIODO_ENTREGA_RESTRITO)) {
        	sql.append(" and (flRestrito = 'N' or flRestrito is null or flRestrito = '')");
        }
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				String[] obj = new String[columns.length];
		    	for (int i = 0; i < columns.length; i++) {
		    		obj[i] = rs.getString(columns[i]);
		    	}
				result.addElement(obj);
			}
			return result;
		}
	}
    
}