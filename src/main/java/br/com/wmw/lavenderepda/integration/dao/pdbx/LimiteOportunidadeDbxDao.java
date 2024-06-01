package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.LimiteOportunidade;
import totalcross.sql.ResultSet;

public class LimiteOportunidadeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new LimiteOportunidade();
	}

    private static LimiteOportunidadeDbxDao instance;

    public LimiteOportunidadeDbxDao() {
        super(LimiteOportunidade.TABLE_NAME);
    }

    public static LimiteOportunidadeDbxDao getInstance() {
        if (instance == null) {
            instance = new LimiteOportunidadeDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        LimiteOportunidade limiteOportunidade = new LimiteOportunidade();
        limiteOportunidade.rowKey = rs.getString("rowkey");
        limiteOportunidade.cdEmpresa = rs.getString("cdEmpresa");
        limiteOportunidade.cdRepresentante = rs.getString("cdRepresentante");
        limiteOportunidade.vlLimite = ValueUtil.round(rs.getDouble("vlLimite"));
        limiteOportunidade.flTipoLimite = rs.getString("flTipoLimite");
        limiteOportunidade.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
        limiteOportunidade.flOrigemSaldo = rs.getString("flOrigemSaldo");
        limiteOportunidade.nuCarimbo = rs.getInt("nuCarimbo");
        limiteOportunidade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        limiteOportunidade.cdUsuario = rs.getString("cdUsuario");
        return limiteOportunidade;
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
        sql.append(" VLLIMITE,");
        sql.append(" FLTIPOLIMITE,");
        sql.append(" VLSALDO,");
        sql.append(" FLORIGEMSALDO,");
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
        sql.append(" VLLIMITE,");
        sql.append(" FLTIPOLIMITE,");
        sql.append(" VLSALDO,");
        sql.append(" FLORIGEMSALDO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        LimiteOportunidade limiteOportunidade = (LimiteOportunidade) domain;
        sql.append(Sql.getValue(limiteOportunidade.cdEmpresa)).append(",");
        sql.append(Sql.getValue(limiteOportunidade.cdRepresentante)).append(",");
        sql.append(Sql.getValue(limiteOportunidade.vlLimite)).append(",");
        sql.append(Sql.getValue(limiteOportunidade.flTipoLimite)).append(",");
        sql.append(Sql.getValue(limiteOportunidade.vlSaldo)).append(",");
        sql.append(Sql.getValue(limiteOportunidade.flOrigemSaldo)).append(",");
        sql.append(Sql.getValue(limiteOportunidade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(limiteOportunidade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(limiteOportunidade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        LimiteOportunidade limiteOportunidade = (LimiteOportunidade) domain;
        sql.append(" VLLIMITE = ").append(Sql.getValue(limiteOportunidade.vlLimite)).append(",");
        sql.append(" FLTIPOLIMITE = ").append(Sql.getValue(limiteOportunidade.flTipoLimite)).append(",");
        sql.append(" VLSALDO = ").append(Sql.getValue(limiteOportunidade.vlSaldo)).append(",");
        sql.append(" FLORIGEMSALDO = ").append(Sql.getValue(limiteOportunidade.flOrigemSaldo)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(limiteOportunidade.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(limiteOportunidade.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(limiteOportunidade.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        LimiteOportunidade limiteOportunidade = (LimiteOportunidade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", limiteOportunidade.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", limiteOportunidade.cdRepresentante);
		sqlWhereClause.addAndCondition("flOrigemSaldo = ", limiteOportunidade.flOrigemSaldo);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}