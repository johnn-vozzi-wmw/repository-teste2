package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.VerbaCliente;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class VerbaClientePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaCliente();
	}

    private static VerbaClientePdbxDao instance;

    public VerbaClientePdbxDao() {
        super(VerbaCliente.TABLE_NAME);
    }

    public static VerbaClientePdbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaClientePdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VerbaCliente verbaCliente = new VerbaCliente();
        verbaCliente.rowKey = rs.getString("rowkey");
        verbaCliente.cdEmpresa = rs.getString("cdEmpresa");
        verbaCliente.cdRepresentante = rs.getString("cdRepresentante");
        verbaCliente.flOrigemSaldo = rs.getString("flOrigemSaldo");
        verbaCliente.cdCliente = rs.getString("cdCliente");
        verbaCliente.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
        verbaCliente.dsControleatualizacao = rs.getString("dsControleatualizacao");
        verbaCliente.cdVerbaSaldoCliente = rs.getString("cdVerbaSaldoCliente");
        verbaCliente.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        verbaCliente.nuCarimbo = rs.getInt("nuCarimbo");
        verbaCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        verbaCliente.cdUsuario = rs.getString("cdUsuario");
        return verbaCliente;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.FLORIGEMSALDO,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.VLSALDO,");
        sql.append(" tb.dsControleAtualizacao,");
        sql.append(" tb.nuCarimbo,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.CDVERBASALDOCLIENTE,");
        sql.append(" tb.CDGRUPOPRODUTO1,");
        sql.append(" tb.CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMSALDO,");
        sql.append(" CDCLIENTE,");
        sql.append(" VLSALDO,");
        sql.append(" dsControleAtualizacao,");
        sql.append(" CDVERBASALDOCLIENTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaCliente verbaCliente = (VerbaCliente) domain;
        sql.append(Sql.getValue(verbaCliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(verbaCliente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(verbaCliente.flOrigemSaldo)).append(",");
        sql.append(Sql.getValue(verbaCliente.cdCliente)).append(",");
        sql.append(Sql.getValue(verbaCliente.vlSaldo)).append(",");
        sql.append(Sql.getValue(verbaCliente.dsControleatualizacao)).append(",");
        sql.append(Sql.getValue(verbaCliente.cdVerbaSaldoCliente)).append(",");
        sql.append(Sql.getValue(verbaCliente.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(verbaCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(verbaCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(verbaCliente.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaCliente verbaCliente = (VerbaCliente) domain;
        sql.append(" VLSALDO = ").append(Sql.getValue(verbaCliente.vlSaldo)).append(",");
        sql.append(" dsControleAtualizacao = ").append(Sql.getValue(verbaCliente.dsControleatualizacao)).append(",");
        sql.append(" CDVERBASALDOCLIENTE = ").append(Sql.getValue(verbaCliente.cdVerbaSaldoCliente)).append(",");
        sql.append(" CDGRUPOPRODUTO1 = ").append(Sql.getValue(verbaCliente.cdGrupoProduto1)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(verbaCliente.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(verbaCliente.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(verbaCliente.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaCliente verbaCliente = (VerbaCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", verbaCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", verbaCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.FLORIGEMSALDO = ", verbaCliente.flOrigemSaldo);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", verbaCliente.cdCliente);
		sqlWhereClause.addAndCondition("tb.CDVERBASALDOCLIENTE = ", verbaCliente.cdVerbaSaldoCliente);
		sqlWhereClause.addAndCondition("tb.CDGRUPOPRODUTO1 = ", verbaCliente.cdGrupoProduto1);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findCdVerbaSaldoCliente(VerbaCliente filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT DISTINCT CDVERBASALDOCLIENTE FROM TBLVPVERBACLIENTE tb");
    	addWhereByExample(filter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector list = new Vector();
    		while (rs.next()) {
    			list.addElement(rs.getString("cdVerbaSaldoCliente"));
    		}
    		return list;
		}
    }
    
    public Vector findVerbaClienteGrupoProduto1ByExample(VerbaCliente filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT ");
    	addSelectColumns(filter, sql);
    	sql.append(", gp.DSGRUPOPRODUTO1, (COALESCE(tb.VLSALDO, 0) + COALESCE(pda.VLSALDO, 0)) AS VLSUMSALDO FROM TBLVPVERBACLIENTE tb ");
    	addJoinSqlGrupoProduto(filter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector list = new Vector();
    		while (rs.next()) {
    			VerbaCliente verbaCliente = (VerbaCliente)populate(filter, rs);
    			verbaCliente.grupoProduto1 = new GrupoProduto1();
    			verbaCliente.grupoProduto1.cdGrupoproduto1 = verbaCliente.cdGrupoProduto1;
    			verbaCliente.grupoProduto1.dsGrupoproduto1 = rs.getString("dsGrupoProduto1");
    			verbaCliente.vlSaldo = rs.getDouble("vlSumSaldo");
				list.addElement(verbaCliente);
    		}
    		return list;
		}
    }
    
    public int countVerbaClienteGrupoProduto1ByExample(VerbaCliente filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT COUNT(1) FROM TBLVPVERBACLIENTE tb");
    	addJoinSqlGrupoProduto(filter, sql);
    	return getInt(sql.toString());
    }

	private void addJoinSqlGrupoProduto(VerbaCliente filter, StringBuffer sql) throws SQLException {
		sql.append(" JOIN TBLVPGRUPOPRODUTO1 gp ON ")
    	.append(" gp.CDGRUPOPRODUTO1 = tb.CDGRUPOPRODUTO1 ")
    	.append(" LEFT JOIN TBLVPVERBACLIENTE pda ON ")
    	.append(" pda.CDEMPRESA = tb.CDEMPRESA AND ")
    	.append(" pda.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
    	.append(" pda.CDCLIENTE = tb.CDCLIENTE AND ")
    	.append(" pda.CDVERBASALDOCLIENTE = tb.CDVERBASALDOCLIENTE AND ")
    	.append(" pda.CDGRUPOPRODUTO1 = tb.CDGRUPOPRODUTO1 AND ")
    	.append(" pda.FLORIGEMSALDO = 'P'");
    	addWhereByExample(filter, sql);
	}

}