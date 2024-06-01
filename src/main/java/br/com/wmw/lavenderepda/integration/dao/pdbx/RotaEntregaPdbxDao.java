package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.RotaEntrega;
import br.com.wmw.lavenderepda.business.domain.RotaEntregaCli;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RotaEntregaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RotaEntrega();
	}

    private static RotaEntregaPdbxDao instance;

    public RotaEntregaPdbxDao() {
        super(RotaEntrega.TABLE_NAME);
    }

    public static RotaEntregaPdbxDao getInstance() {
        if (instance == null) {
            instance = new RotaEntregaPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

	//@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" cdRotaEntrega,");
		sql.append(" dsRotaEntrega");
	}

	//@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        RotaEntrega rotaEntrega = new RotaEntrega();
        rotaEntrega.rowKey = rs.getString("rowkey");
        rotaEntrega.cdEmpresa = rs.getString("cdEmpresa");
        rotaEntrega.cdRepresentante = rs.getString("cdRepresentante");
        rotaEntrega.cdRotaEntrega = rs.getString("cdRotaEntrega");
        rotaEntrega.dsRotaEntrega = rs.getString("dsRotaEntrega");
        return rotaEntrega;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RotaEntrega rotaEntrega = (RotaEntrega) domain;
		SqlWhereClause sqlWhereClause = addWhere(rotaEntrega.cdEmpresa, rotaEntrega.cdRepresentante);
		sqlWhereClause.addAndCondition("cdRotaEntrega = ", rotaEntrega.cdRotaEntrega);
		sql.append(sqlWhereClause.getSql());
    }

	private SqlWhereClause addWhere(String cdEmpresa, String cdRepresentante) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", cdRepresentante);
		return sqlWhereClause;
	}
    
    public Vector filterRotaEntregaPorCliente(String cdCliente) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT TB.* FROM TBLVPROTAENTREGA TB ");
    	addFiltroPorCliente(cdCliente, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector rotaEntregaList = new Vector();
    		while (rs.next()) {
    			rotaEntregaList.addElement(getNewRotaEntrega(rs));
    		}
    		return rotaEntregaList;
		}
    }
    
    public int countRotaEntregaPorCliente(Cliente cliente) throws SQLException {
		StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT COUNT(TB.CDROTAENTREGA) AS QTDE FROM TBLVPROTAENTREGA TB ");
    	RotaEntrega rotaEntregaFilter = new RotaEntrega(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdRotaEntrega, null, null);
    	addWhereByExample(rotaEntregaFilter, sql);
    	return getInt(sql.toString());
	}


	private void addFiltroPorCliente(String cdCliente, StringBuffer sql) {
    	sql.append(" JOIN TBLVPROTAENTREGACLI ROTAENTREGACLI ON ");
    	sql.append(" TB.CDEMPRESA = ROTAENTREGACLI.CDEMPRESA AND ");
    	sql.append(" TB.CDREPRESENTANTE = ROTAENTREGACLI.CDREPRESENTANTE AND ");
    	sql.append(" TB.CDROTAENTREGA = ROTAENTREGACLI.CDROTAENTREGA AND ");
    	sql.append(" ROTAENTREGACLI.CDCLIENTE = ").append(Sql.getValue(cdCliente));
    	SqlWhereClause sqlWhereClause = addWhere(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(RotaEntregaCli.class));
    	sql.append(sqlWhereClause.getSql());
	}

	private RotaEntrega getNewRotaEntrega(ResultSet resultSet) throws SQLException {
		String cdEmpresa = resultSet.getString("CDEMPRESA");
		String cdRepresentante = resultSet.getString("CDREPRESENTANTE");
		String cdRotaEntrega = resultSet.getString("CDROTAENTREGA");
		String dsRotaEntrega = resultSet.getString("DSROTAENTREGA");
		String dsDiasEntrega = resultSet.getString("DSDIASENTREGA");
		return new RotaEntrega(cdEmpresa, cdRepresentante, cdRotaEntrega, dsRotaEntrega, dsDiasEntrega);
	}

	}