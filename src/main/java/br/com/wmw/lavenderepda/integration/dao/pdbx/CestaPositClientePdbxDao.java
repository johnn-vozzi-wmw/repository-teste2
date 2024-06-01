package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CestaPositCliente;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class CestaPositClientePdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CestaPositCliente();
	}

    private static CestaPositClientePdbxDao instance;

    public CestaPositClientePdbxDao() {
        super(CestaPositCliente.TABLE_NAME);
    }

    public static CestaPositClientePdbxDao getInstance() {
        if (instance == null) {
            instance = new CestaPositClientePdbxDao();
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
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCAMPANHA,");
        sql.append(" tb.CDCESTA,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.vlPctPositivacao,");
        sql.append(" tb.FLTIPOALTERACAO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CestaPositCliente cestapositcliente = new CestaPositCliente();
        cestapositcliente.rowKey = rs.getString("rowkey");
        cestapositcliente.cdEmpresa = rs.getString("cdEmpresa");
        cestapositcliente.cdRepresentante = rs.getString("cdRepresentante");
        cestapositcliente.cdCampanha = rs.getString("cdCampanha");
        cestapositcliente.cdCesta = rs.getString("cdCesta");
        cestapositcliente.cdCliente = rs.getString("cdCliente");
        cestapositcliente.vlPctpositivacao = ValueUtil.round(rs.getDouble("vlPctpositivacao"));
        return cestapositcliente;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	CestaPositCliente cestapositcliente = (CestaPositCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", cestapositcliente.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", cestapositcliente.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCAMPANHA = ", cestapositcliente.cdCampanha);
		sqlWhereClause.addAndCondition("tb.CDCESTA = ", cestapositcliente.cdCesta);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", cestapositcliente.cdCliente);
		//--
		sqlWhereClause.addStartAndMultipleCondition();
       	boolean adicionouInicioBloco = false;
       	adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition("cli.NMRAZAOSOCIAL", cestapositcliente.nmRazaoSocial, false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("cli.CDCLIENTE", cestapositcliente.nmRazaoSocial, false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("cli.NMFANTASIA", cestapositcliente.nmRazaoSocial, false);
       	if (LavenderePdaConfig.usaFiltroCnpjClienteListaClientes()) {
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("cli.NUCNPJ", cestapositcliente.nuCnpjFilter, false);
       	}
       	if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
       	if (cestapositcliente.efetivado) {
       		sqlWhereClause.addAndCondition("VLPCTPOSITIVACAO >= ", 100);
       	}
		//--
		sql.append(sqlWhereClause.getSql());
    }

    protected void addSelectGridColumns(StringBuffer sql) {
    	sql.append(" ROWKEY,");
        sql.append(" CDCESTA,");
        sql.append(" vlPctPositivacao,");
        sql.append(" CDCAMPANHA");
    }

    protected void addOrderByGrid(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by cdCesta ");
    }

    public Vector findAllListCestaPositivacao(CestaPositCliente cestaPositClienteFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
        sql.append(" SELECT ");
        addSelectColumns(cestaPositClienteFilter, sql);
        sql.append(" ,NMRAZAOSOCIAL ");
        sql.append(" ,DSCESTA ");
        sql.append(" ,100 - COALESCE(tb.vlPctPositivacao, 0) vlPctFaltante");
        sql.append(" FROM ");
        sql.append(tableName).append(" tb ");
        sql.append(" LEFT OUTER JOIN TBLVPCESTA CESTA ON ");
        sql.append(" TB.CDEMPRESA = CESTA.CDEMPRESA ");
        sql.append(" AND TB.CDREPRESENTANTE = CESTA.CDREPRESENTANTE ");
        sql.append(" AND TB.CDCESTA = CESTA.CDCESTA ");
        sql.append(" LEFT OUTER JOIN TBLVPCLIENTE CLI ON ");
        sql.append(" TB.CDEMPRESA = CLI.CDEMPRESA ");
        sql.append(" AND TB.CDREPRESENTANTE = CLI.CDREPRESENTANTE ");
        sql.append(" AND TB.CDCLIENTE = CLI.CDCLIENTE ");
        addWhereByExample(cestaPositClienteFilter, sql);
        try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			while (rs.next()) {
				CestaPositCliente cestaPositCliente = (CestaPositCliente) populate(cestaPositClienteFilter, rs);
				cestaPositCliente.getCesta().dsCesta = rs.getString("DSCESTA");
				cestaPositCliente.getCesta().cdCesta = rs.getString("CDCESTA");
				cestaPositCliente.nmRazaoSocial = rs.getString("NMRAZAOSOCIAL");
				cestaPositCliente.vlPctFaltante = rs.getDouble("VLPCTFALTANTE");
				result.addElement(cestaPositCliente);
			}
			return result;
		}
    }
    
}