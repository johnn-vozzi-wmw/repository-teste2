package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.MetaVenda;
import totalcross.sql.ResultSet;

public class MetaVendaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetaVenda();
	}

    private static MetaVendaDbxDao instance;

    public MetaVendaDbxDao() {
        super(MetaVenda.TABLE_NAME);
    }

    public static MetaVendaDbxDao getInstance() {
        if (instance == null) {
            instance = new MetaVendaDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetaVenda metaVenda = new MetaVenda();
        metaVenda.rowKey = rs.getString("rowkey");
        metaVenda.cdEmpresa = rs.getString("cdEmpresa");
        metaVenda.cdMetaVenda = rs.getString("cdMetaVenda");
        metaVenda.dsMetaVenda = rs.getString("dsMetaVenda");
        metaVenda.cdTipoMetaVenda = rs.getString("cdTipoMetaVenda");
        metaVenda.cdVariavelMetaVenda = rs.getString("cdVariavelMetaVenda");
        metaVenda.dtInicialVigencia = rs.getDate("dtInicialVigencia");
        metaVenda.dtFinalVigencia = rs.getDate("dtFinalVigencia");
        metaVenda.nuCarimbo = rs.getInt("nuCarimbo");
        metaVenda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        metaVenda.cdUsuario = rs.getString("cdUsuario");
        return metaVenda;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDMETAVENDA,");
        sql.append(" DSMETAVENDA,");
        sql.append(" CDTIPOMETAVENDA,");
        sql.append(" CDVARIAVELMETAVENDA,");
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
        sql.append(" CDMETAVENDA,");
        sql.append(" DSMETAVENDA,");
        sql.append(" CDTIPOMETAVENDA,");
        sql.append(" CDVARIAVELMETAVENDA,");
        sql.append(" DTINICIALVIGENCIA,");
        sql.append(" DTFINALVIGENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVenda metaVenda = (MetaVenda) domain;
        sql.append(Sql.getValue(metaVenda.cdEmpresa)).append(",");
        sql.append(Sql.getValue(metaVenda.cdMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVenda.dsMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVenda.cdTipoMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVenda.cdVariavelMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVenda.dtInicialVigencia)).append(",");
        sql.append(Sql.getValue(metaVenda.dtFinalVigencia)).append(",");
        sql.append(Sql.getValue(metaVenda.nuCarimbo)).append(",");
        sql.append(Sql.getValue(metaVenda.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(metaVenda.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVenda metaVenda = (MetaVenda) domain;
        sql.append(" DSMETAVENDA = ").append(Sql.getValue(metaVenda.dsMetaVenda)).append(",");
        sql.append(" CDTIPOMETAVENDA = ").append(Sql.getValue(metaVenda.cdTipoMetaVenda)).append(",");
        sql.append(" CDVARIAVELMETAVENDA = ").append(Sql.getValue(metaVenda.cdVariavelMetaVenda)).append(",");
        sql.append(" DTINICIALVIGENCIA = ").append(Sql.getValue(metaVenda.dtInicialVigencia)).append(",");
        sql.append(" DTFINALVIGENCIA = ").append(Sql.getValue(metaVenda.dtFinalVigencia)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(metaVenda.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(metaVenda.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(metaVenda.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVenda metaVenda = (MetaVenda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", metaVenda.cdEmpresa);
		sqlWhereClause.addAndCondition("CDMETAVENDA = ", metaVenda.cdMetaVenda);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}