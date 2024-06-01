package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.InfoFretePedido;
import totalcross.sql.ResultSet;

public class InfoFretePedidoDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new InfoFretePedido();
	}
    private static InfoFretePedidoDao instance;

    public InfoFretePedidoDao() {
        super(InfoFretePedido.TABLE_NAME);
    }

    public static InfoFretePedidoDao getInstance() {
        if (instance == null) {
            instance = new InfoFretePedidoDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        InfoFretePedido infoFretePedido = new InfoFretePedido();
        infoFretePedido.rowKey = rs.getString("rowkey");
        infoFretePedido.cdEmpresa = rs.getString("cdEmpresa");
        infoFretePedido.cdRepresentante = rs.getString("cdRepresentante");
        infoFretePedido.nuPedido = rs.getString("nuPedido");
        infoFretePedido.flOrigemPedido = rs.getString("flOrigemPedido");
        infoFretePedido.flTaxaEntrega = rs.getString("flTaxaEntrega");
        infoFretePedido.vlTaxaEntrega = rs.getDouble("vlTaxaEntrega");
        infoFretePedido.flAjudante =  rs.getString("flAjudante");
        infoFretePedido.qtAjudante = rs.getInt("qtAjudante");
        infoFretePedido.flAntecipaEntrega = rs.getString("flAntecipaEntrega");
        infoFretePedido.flAgendamento = rs.getString("flAgendamento");
        infoFretePedido.cdTipoVeiculo = rs.getString("cdTipoVeiculo");
        infoFretePedido.nuCarimbo = rs.getInt("nuCarimbo");
        infoFretePedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        infoFretePedido.cdUsuario = rs.getString("cdUsuario");
        return infoFretePedido;
    }


    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" FLTAXAENTREGA,");
        sql.append(" VLTAXAENTREGA,");
        sql.append(" FLAJUDANTE,");
        sql.append(" QTAJUDANTE,");
        sql.append(" FLANTECIPAENTREGA,");
        sql.append(" FLAGENDAMENTO,");
        sql.append(" CDTIPOVEICULO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
		sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" FLTAXAENTREGA,");
        sql.append(" VLTAXAENTREGA,");
        sql.append(" FLAJUDANTE,");
        sql.append(" QTAJUDANTE,");
        sql.append(" FLANTECIPAENTREGA,");
        sql.append(" FLAGENDAMENTO,");
        sql.append(" CDTIPOVEICULO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");

	}

	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		InfoFretePedido infoFretePedido = (InfoFretePedido) domain;
		sql.append(Sql.getValue(infoFretePedido.cdEmpresa)).append(",");
		sql.append(Sql.getValue(infoFretePedido.cdRepresentante)).append(",");
		sql.append(Sql.getValue(infoFretePedido.nuPedido)).append(",");
		sql.append(Sql.getValue(infoFretePedido.flOrigemPedido)).append(",");
		sql.append(Sql.getValue(infoFretePedido.flTaxaEntrega)).append(",");
		sql.append(Sql.getValue(infoFretePedido.vlTaxaEntrega)).append(",");
		sql.append(Sql.getValue(infoFretePedido.flAjudante)).append(",");
		sql.append(Sql.getValue(infoFretePedido.qtAjudante)).append(",");
		sql.append(Sql.getValue(infoFretePedido.flAntecipaEntrega)).append(",");
		sql.append(Sql.getValue(infoFretePedido.flAgendamento)).append(",");
		sql.append(Sql.getValue(infoFretePedido.cdTipoVeiculo)).append(",");
		sql.append(Sql.getValue(infoFretePedido.nuCarimbo)).append(",");
		sql.append(Sql.getValue(infoFretePedido.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(infoFretePedido.cdUsuario));
	}

	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		InfoFretePedido infoFretePedido = (InfoFretePedido) domain;
		sql.append(" FLTAXAENTREGA = ").append(Sql.getValue(infoFretePedido.flTaxaEntrega)).append(",");
		sql.append(" VLTAXAENTREGA = ").append(Sql.getValue(infoFretePedido.vlTaxaEntrega)).append(",");
		sql.append(" FLAJUDANTE = ").append(Sql.getValue(infoFretePedido.flAjudante)).append(",");
		sql.append(" QTAJUDANTE = ").append(Sql.getValue(infoFretePedido.qtAjudante)).append(",");
		sql.append(" FLANTECIPAENTREGA = ").append(Sql.getValue(infoFretePedido.flAntecipaEntrega)).append(",");
		sql.append(" FLAGENDAMENTO = ").append(Sql.getValue(infoFretePedido.flAgendamento)).append(",");
		sql.append(" CDTIPOVEICULO = ").append(Sql.getValue(infoFretePedido.cdTipoVeiculo)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(infoFretePedido.nuCarimbo)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(infoFretePedido.flTipoAlteracao)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(infoFretePedido.cdUsuario));
	}

	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		InfoFretePedido infoFretePedido = (InfoFretePedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", infoFretePedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", infoFretePedido.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", infoFretePedido.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", infoFretePedido.flOrigemPedido);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected boolean isNotSaveTabelasEnvio() {
		return true;
	}
}