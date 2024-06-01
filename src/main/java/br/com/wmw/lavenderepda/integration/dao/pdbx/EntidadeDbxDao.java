package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Entidade;
import totalcross.sql.ResultSet;

public class EntidadeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Entidade();
	}

    private static EntidadeDbxDao instance;

    public EntidadeDbxDao() {
        super(Entidade.TABLE_NAME);
    }

    public static EntidadeDbxDao getInstance() {
        if (instance == null) {
			instance = new EntidadeDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Entidade entidade = new Entidade();
        entidade.rowKey = rs.getString("rowkey");
        entidade.cdSistema = rs.getInt("cdSistema");
        entidade.nmEntidade = rs.getString("nmEntidade");
        entidade.nmDomain = rs.getString("nmDomain");
        entidade.dsEntidade = rs.getString("dsEntidade");
        entidade.flAuditaIns = rs.getString("flAuditaIns");
        entidade.flAuditaUpd = rs.getString("flAuditaUpd");
        entidade.flAuditaDel = rs.getString("flAuditaDel");
        entidade.flDinamico = rs.getString("flDinamico");
        entidade.nuCarimbo = rs.getInt("nuCarimbo");
        entidade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        entidade.cdUsuario = rs.getString("cdUsuario");
        return entidade;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDSISTEMA,");
        sql.append(" NMENTIDADE,");
        sql.append(" NMDOMAIN,");
        sql.append(" DSENTIDADE,");
        sql.append(" FLAUDITAINS,");
        sql.append(" FLAUDITAUPD,");
        sql.append(" FLAUDITADEL,");
        sql.append(" FLDINAMICO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDSISTEMA,");
        sql.append(" NMENTIDADE,");
        sql.append(" NMDOMAIN,");
        sql.append(" DSENTIDADE,");
        sql.append(" FLAUDITAINS,");
        sql.append(" FLAUDITAUPD,");
        sql.append(" FLAUDITADEL,");
        sql.append(" FLDINAMICO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Entidade entidade = (Entidade) domain;
        sql.append(Sql.getValue(entidade.cdSistema)).append(",");
        sql.append(Sql.getValue(entidade.nmEntidade)).append(",");
        sql.append(Sql.getValue(entidade.nmDomain)).append(",");
        sql.append(Sql.getValue(entidade.dsEntidade)).append(",");
        sql.append(Sql.getValue(entidade.flAuditaIns)).append(",");
        sql.append(Sql.getValue(entidade.flAuditaUpd)).append(",");
        sql.append(Sql.getValue(entidade.flAuditaDel)).append(",");
        sql.append(Sql.getValue(entidade.flDinamico)).append(",");
        sql.append(Sql.getValue(entidade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(entidade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(entidade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Entidade entidade = (Entidade) domain;
        sql.append(" NMDOMAIN = ").append(Sql.getValue(entidade.nmDomain)).append(",");
        sql.append(" DSENTIDADE = ").append(Sql.getValue(entidade.dsEntidade)).append(",");
        sql.append(" FLAUDITAINS = ").append(Sql.getValue(entidade.flAuditaIns)).append(",");
        sql.append(" FLAUDITAUPD = ").append(Sql.getValue(entidade.flAuditaUpd)).append(",");
        sql.append(" FLAUDITADEL = ").append(Sql.getValue(entidade.flAuditaDel)).append(",");
        sql.append(" FLDINAMICO = ").append(Sql.getValue(entidade.flDinamico)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(entidade.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(entidade.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(entidade.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Entidade entidade = (Entidade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDSISTEMA = ", entidade.cdSistema);
		sqlWhereClause.addAndCondition("NMENTIDADE = ", entidade.nmEntidade);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}