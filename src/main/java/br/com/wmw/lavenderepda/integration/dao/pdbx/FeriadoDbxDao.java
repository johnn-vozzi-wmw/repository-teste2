package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Feriado;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class FeriadoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Feriado();
	}

    private static FeriadoDbxDao instance;

    public FeriadoDbxDao() {
        super(Feriado.TABLE_NAME);
    }

    public static FeriadoDbxDao getInstance() {
        if (instance == null) {
            instance = new FeriadoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Feriado feriado = new Feriado();
        feriado.rowKey = rs.getString("rowkey");
        feriado.nuDia = rs.getInt("nuDia");
        feriado.nuMes = rs.getInt("nuMes");
        feriado.nuAno = rs.getInt("nuAno");
        feriado.dsFeriado = rs.getString("dsFeriado");
        feriado.nuCarimbo = rs.getInt("nuCarimbo");
        feriado.flTipoAlteracao = rs.getString("flTipoAlteracao");
        feriado.cdUsuario = rs.getString("cdUsuario");
        return feriado;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" NUDIA,");
        sql.append(" NUMES,");
        sql.append(" NUANO,");
        sql.append(" DSFERIADO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" NUDIA,");
        sql.append(" NUMES,");
        sql.append(" NUANO,");
        sql.append(" DSFERIADO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Feriado feriado = (Feriado) domain;
        sql.append(Sql.getValue(feriado.nuDia)).append(",");
        sql.append(Sql.getValue(feriado.nuMes)).append(",");
        sql.append(Sql.getValue(feriado.nuAno)).append(",");
        sql.append(Sql.getValue(feriado.dsFeriado)).append(",");
        sql.append(Sql.getValue(feriado.nuCarimbo)).append(",");
        sql.append(Sql.getValue(feriado.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(feriado.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Feriado feriado = (Feriado) domain;
        sql.append(" DSFERIADO = ").append(Sql.getValue(feriado.dsFeriado)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(feriado.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(feriado.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(feriado.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Feriado feriado = (Feriado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("NUDIA = ", feriado.nuDia);
		sqlWhereClause.addAndCondition("NUMES = ", feriado.nuMes);
		if (feriado.nuAno != 0) {
			sqlWhereClause.addAndCondition("(NUANO = ", feriado.nuAno);
			sqlWhereClause.addOrConditionForced("NUANO = ", 0);
			sqlWhereClause.addEndMultipleCondition();
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public Vector findFeriadosPeriodo(Date dataAtual, Date dataVencimento) throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" select nudia, numes, nuano from ");
        sql.append(tableName);
        sql.append(" where ((nuDia >=").append(Sql.getValue(dataVencimento.getDay())).append(" and nuMes = ").append(Sql.getValue(dataVencimento.getMonth()));
        sql.append(" and (nuAno =").append(Sql.getValue(dataVencimento.getYear())).append(" or nuAno = 0)) or ( nuMes > ").append(Sql.getValue(dataVencimento.getMonth()));
        sql.append(" and (nuAno = ").append(Sql.getValue(dataVencimento.getYear())).append(" or nuAno = 0)) or (nuAno > ").append(Sql.getValue(dataVencimento.getYear()));
        sql.append(" or nuAno = 0)) and ((nuDia <=").append(Sql.getValue(dataAtual.getDay())).append(" and nuMes = ").append(Sql.getValue(dataAtual.getMonth()));
        sql.append(" and (nuAno = ").append(Sql.getValue(dataAtual.getYear())).append(" or nuAno = 0)) or (nuMes < ").append(Sql.getValue(dataAtual.getMonth()));
        sql.append(" and (nuAno = ").append(Sql.getValue(dataAtual.getYear())).append(" or nuAno = 0)) or (nuAno < ").append(Sql.getValue(dataAtual.getYear())).append(" or nuAno = 0))");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector listFeriado = new Vector();
			Feriado feriado;
			while (rs.next()) {
				feriado = new Feriado();
				feriado.nuDia = rs.getInt(1);
				feriado.nuMes = rs.getInt(2);
				feriado.nuAno = rs.getInt(3);
				listFeriado.addElement(feriado);
			}
			return listFeriado;
		}
	}
    
}