package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Regiao;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class RegiaoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Regiao();
	}

	private static RegiaoDbxDao instance = null;

    public RegiaoDbxDao() {
        super(Regiao.TABLE_NAME); 
    }
    
    public static RegiaoDbxDao getInstance() {
        if (instance == null) {
            instance = new RegiaoDbxDao();
        }
        return instance;
    }

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		Regiao regiao = new Regiao();
		regiao.rowKey = rs.getString("rowkey");
		regiao.cdRegiao = rs.getString("cdRegiao");
		regiao.nmRegiao = rs.getString("nmRegiao");
		regiao.flTipoAlteracao = rs.getString("flTipoALteracao");
		regiao.nuCarimbo = rs.getInt("nuCarimbo");
        return regiao;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
        sql.append(" CDREGIAO,");
        sql.append(" NMREGIAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDREGIAO,");
    	sql.append(" NMREGIAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");

	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		Regiao regiao = (Regiao) domain;
        sql.append(Sql.getValue(regiao.cdRegiao)).append(",");
        sql.append(Sql.getValue(regiao.nmRegiao)).append(",");
        sql.append(Sql.getValue(regiao.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(regiao.nuCarimbo));

	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		Regiao regiao = (Regiao) domain;
        sql.append(" CDREGIAO = ").append(Sql.getValue(regiao.cdRegiao)).append(",");
        sql.append(" NMREGIAO = ").append(Sql.getValue(regiao.nmRegiao));

	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		Regiao regiao = (Regiao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		boolean adicionouInicioBloco = false;
		sqlWhereClause.addStartOrMultipleCondition();
		adicionouInicioBloco |= sqlWhereClause.addOrCondition("tb.CDREGIAO = ", regiao.cdRegiao);
		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("tb.NMREGIAO ",regiao.nmRegiao);
		if (adicionouInicioBloco) {
			sqlWhereClause.addEndMultipleCondition();
		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
		//--
		sql.append(sqlWhereClause.getSql());

	}
	
	public void addWhereByExampleInJoin(BaseDomain domain, StringBuffer sql) {
		try {
			StringBuffer sqlWhere = new StringBuffer();
			addWhereByExample(domain, sqlWhere);
			String result = sqlWhere.toString().replaceAll("(?i)tb\\.", DaoUtil.ALIAS_REGIAO.concat("."));
			result = result.replaceFirst("(?i)where", ValueUtil.VALOR_NI);
			sql.append(result);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

}
