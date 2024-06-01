package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VisitaAcomp;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class VisitaAcompDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VisitaAcomp();
	}

    private static VisitaAcompDbxDao instance;

    public VisitaAcompDbxDao() {
        super(VisitaAcomp.TABLE_NAME);
    }

    public static VisitaAcompDbxDao getInstance() {
        if (instance == null) {
            instance = new VisitaAcompDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VisitaAcomp visitaAcomp = new VisitaAcomp();
        visitaAcomp.rowKey = rs.getString("rowkey");
        visitaAcomp.cdEmpresa = rs.getString("cdEmpresa");
        visitaAcomp.cdRepresentante = rs.getString("cdRepresentante");
        visitaAcomp.dtRegistro = rs.getDate("dtRegistro");
        visitaAcomp.qtVisitasPositivas = rs.getInt("qtVisitasPositivas");
        visitaAcomp.qtVisitasNegativas = rs.getInt("qtVisitasNegativas");
        visitaAcomp.nuCarimbo = rs.getInt("nuCarimbo");
        visitaAcomp.flTipoAlteracao = rs.getString("flTipoAlteracao");
        visitaAcomp.cdUsuario = rs.getString("cdUsuario");
        return visitaAcomp;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" cdEmpresa,");
        sql.append(" cdRepresentante,");
        sql.append(" dtRegistro,");
        sql.append(" qtVisitasPositivas,");
        sql.append(" qtVisitasNegativas,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" cdEmpresa,");
        sql.append(" cdRepresentante,");
        sql.append(" dtRegistro,");
        sql.append(" qtVisitasPositivas,");
        sql.append(" qtVisitasNegativas,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaAcomp visitaAcomp = (VisitaAcomp) domain;
        sql.append(Sql.getValue(visitaAcomp.cdEmpresa)).append(",");
        sql.append(Sql.getValue(visitaAcomp.cdRepresentante)).append(",");
        sql.append(Sql.getValue(visitaAcomp.dtRegistro)).append(",");
        sql.append(Sql.getValue(visitaAcomp.qtVisitasPositivas)).append(",");
        sql.append(Sql.getValue(visitaAcomp.qtVisitasNegativas)).append(",");
        sql.append(Sql.getValue(visitaAcomp.nuCarimbo)).append(",");
        sql.append(Sql.getValue(visitaAcomp.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(visitaAcomp.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaAcomp visitaAcomp = (VisitaAcomp) domain;
        sql.append(" qtVisitasPositivas = ").append(Sql.getValue(visitaAcomp.qtVisitasPositivas)).append(",");
        sql.append(" qtVisitasNegativas = ").append(Sql.getValue(visitaAcomp.qtVisitasNegativas)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(visitaAcomp.nuCarimbo)).append(",");
        sql.append(" flTipoAlteracao = ").append(Sql.getValue(visitaAcomp.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(visitaAcomp.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaAcomp visitaAcomp = (VisitaAcomp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdEmpresa = ", visitaAcomp.cdEmpresa);
		sqlWhereClause.addAndCondition("cdRepresentante = ", visitaAcomp.cdRepresentante);
		sqlWhereClause.addAndCondition("dtRegistro = ", visitaAcomp.dtRegistro);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public double[] sumVisitasPositivasAndNegativasDoMes(String cdRepresentante, String cdEmpresa) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT sum(qtVisitasPositivas) as sumVisitasPositivas, sum(qtVisitasNegativas) as sumVisitasNegativas FROM " + tableName);
    	sql.append(" WHERE CDEMPRESA=").append(Sql.getValue(cdEmpresa));
    	if (ValueUtil.isNotEmpty(cdRepresentante)) {
    		sql.append(" AND CDREPRESENTANTE=").append(Sql.getValue(cdRepresentante));
    	}
    	sql.append(" AND strftime('%m', dtRegistro)=").append(Sql.getSqliteDayMothValue(DateUtil.getCurrentDate().getMonth()));
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		double[] somatorios = new double[2];
    		if (rs.next()) {
    			somatorios[0] = rs.getDouble("sumVisitasPositivas");
    			somatorios[1] = rs.getDouble("sumVisitasNegativas");
        	}
        	return somatorios;
    	}
    }

}