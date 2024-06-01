package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetaAcompanhamento;
import totalcross.sql.ResultSet;
import totalcross.util.Date;

public class MetaAcompanhamentoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetaAcompanhamento();
	}

    private static MetaAcompanhamentoDbxDao instance;

    public MetaAcompanhamentoDbxDao() {
        super(MetaAcompanhamento.TABLE_NAME);
    }

    public static MetaAcompanhamentoDbxDao getInstance() {
        if (instance == null) {
            instance = new MetaAcompanhamentoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetaAcompanhamento metaAcompanhamento = new MetaAcompanhamento();
        metaAcompanhamento.rowKey = rs.getString("rowkey");
        metaAcompanhamento.cdEmpresa = rs.getString("cdEmpresa");
        metaAcompanhamento.cdRepresentante = rs.getString("cdRepresentante");
        metaAcompanhamento.dsPeriodo = rs.getString("dsPeriodo");
        metaAcompanhamento.dtRegistro = rs.getDate("dtRegistro");
        metaAcompanhamento.vlRealizadoMeta = ValueUtil.round(rs.getDouble("vlRealizadoMeta"));
        metaAcompanhamento.vlRealizadoFlex = ValueUtil.round(rs.getDouble("vlRealizadoFlex"));
        metaAcompanhamento.qtPedidosRealizado = rs.getInt("qtPedidosRealizado");
        metaAcompanhamento.nuCarimbo = rs.getInt("nuCarimbo");
        metaAcompanhamento.flTipoAlteracao = rs.getString("flTipoAlteracao");
        metaAcompanhamento.cdUsuario = rs.getString("cdUsuario");
        return metaAcompanhamento;
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
        sql.append(" dsPeriodo,");
        sql.append(" dtRegistro,");
        sql.append(" vlRealizadoMeta,");
        sql.append(" vlRealizadoFlex,");
        sql.append(" qtPedidosRealizado,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        sql.append(" cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" cdEmpresa,");
        sql.append(" cdRepresentante,");
        sql.append(" dsPeriodo,");
        sql.append(" dtRegistro,");
        sql.append(" vlRealizadoMeta,");
        sql.append(" vlRealizadoFlex,");
        sql.append(" qtPedidosRealizado,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaAcompanhamento metaAcompanhamento = (MetaAcompanhamento) domain;
        sql.append(Sql.getValue(metaAcompanhamento.cdEmpresa)).append(",");
        sql.append(Sql.getValue(metaAcompanhamento.cdRepresentante)).append(",");
        sql.append(Sql.getValue(metaAcompanhamento.dsPeriodo)).append(",");
        sql.append(Sql.getValue(metaAcompanhamento.dtRegistro)).append(",");
        sql.append(Sql.getValue(metaAcompanhamento.vlRealizadoMeta)).append(",");
        sql.append(Sql.getValue(metaAcompanhamento.vlRealizadoFlex)).append(",");
        sql.append(Sql.getValue(metaAcompanhamento.qtPedidosRealizado)).append(",");
        sql.append(Sql.getValue(metaAcompanhamento.nuCarimbo)).append(",");
        sql.append(Sql.getValue(metaAcompanhamento.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(metaAcompanhamento.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaAcompanhamento metaAcompanhamento = (MetaAcompanhamento) domain;
        sql.append(" vlRealizadoMeta = ").append(Sql.getValue(metaAcompanhamento.vlRealizadoMeta)).append(",");
        sql.append(" vlRealizadoFlex = ").append(Sql.getValue(metaAcompanhamento.vlRealizadoFlex)).append(",");
        sql.append(" qtPedidosRealizado = ").append(Sql.getValue(metaAcompanhamento.qtPedidosRealizado)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(metaAcompanhamento.nuCarimbo)).append(",");
        sql.append(" flTipoAlteracao = ").append(Sql.getValue(metaAcompanhamento.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(metaAcompanhamento.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaAcompanhamento metaAcompanhamento = (MetaAcompanhamento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdEmpresa = ", metaAcompanhamento.cdEmpresa);
		sqlWhereClause.addAndCondition("cdRepresentante = ", metaAcompanhamento.cdRepresentante);
		sqlWhereClause.addAndCondition("dsPeriodo = ", metaAcompanhamento.dsPeriodo);
		if (LavenderePdaConfig.usaSistemaModoOffLine) {
			Date currentDate = DateUtil.getCurrentDate();
			if (ValueUtil.isNotEmpty(metaAcompanhamento.dtRegistro) && !metaAcompanhamento.dtRegistro.isAfter(currentDate)) {
				sqlWhereClause.addAndCondition("dtRegistro = ", metaAcompanhamento.dtRegistro);
			} else {
				sqlWhereClause.addAndCondition("dtRegistro <= ", currentDate);
			}
		} else {
			sqlWhereClause.addAndCondition("dtRegistro = ", metaAcompanhamento.dtRegistro);
		}
		sql.append(sqlWhereClause.getSql());
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by DTREGISTRO ");
    }

}