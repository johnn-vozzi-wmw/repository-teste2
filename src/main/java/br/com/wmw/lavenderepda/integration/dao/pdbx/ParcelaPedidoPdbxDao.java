package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import totalcross.sql.ResultSet;

public class ParcelaPedidoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ParcelaPedido();
	}

    private static ParcelaPedidoPdbxDao instance;

    public ParcelaPedidoPdbxDao() {
        super(ParcelaPedido.TABLE_NAME);
    }

    public static ParcelaPedidoPdbxDao getInstance() {
        if (instance == null) {
            instance = new ParcelaPedidoPdbxDao();
        }
        return instance;
    }

    //@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPARCELA,");
        sql.append(" VLPARCELA,");
        sql.append(" DTVENCIMENTO,");
        sql.append(" QTDIASPRAZO,");
        sql.append(" VLPCTPARCELA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ParcelaPedido parcelaPedido = new ParcelaPedido();
        parcelaPedido.rowKey = rs.getString("rowkey");
        parcelaPedido.cdEmpresa = rs.getString("cdEmpresa");
        parcelaPedido.cdRepresentante = rs.getString("cdRepresentante");
        parcelaPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        parcelaPedido.nuPedido = rs.getString("nuPedido");
        parcelaPedido.cdParcela = rs.getString("cdParcela");
        parcelaPedido.vlParcela = ValueUtil.round(rs.getDouble("vlParcela"));
        parcelaPedido.dtVencimento = rs.getDate("dtVencimento");
        parcelaPedido.qtDiasPrazo = rs.getInt("qtDiasPrazo");
        parcelaPedido.vlPctParcela = ValueUtil.round(rs.getDouble("vlPctParcela"));
        parcelaPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        parcelaPedido.cdUsuario = rs.getString("cdUsuario");
        return parcelaPedido;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPARCELA,");
        sql.append(" VLPARCELA,");
        sql.append(" DTVENCIMENTO,");
        sql.append(" QTDIASPRAZO,");
        sql.append(" VLPCTPARCELA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ParcelaPedido parcelaPedido = (ParcelaPedido) domain;
        sql.append(Sql.getValue(parcelaPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(parcelaPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(parcelaPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(parcelaPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(parcelaPedido.cdParcela)).append(",");
        sql.append(Sql.getValue(parcelaPedido.vlParcela)).append(",");
        sql.append(Sql.getValue(parcelaPedido.dtVencimento)).append(",");
        sql.append(Sql.getValue(parcelaPedido.qtDiasPrazo)).append(",");
        sql.append(Sql.getValue(parcelaPedido.vlPctParcela)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(parcelaPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(parcelaPedido.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ParcelaPedido parcelaPedido = (ParcelaPedido) domain;
        sql.append(" VLPARCELA = ").append(Sql.getValue(parcelaPedido.vlParcela)).append(",");
        sql.append(" DTVENCIMENTO = ").append(Sql.getValue(parcelaPedido.dtVencimento)).append(",");
        sql.append(" QTDIASPRAZO = ").append(Sql.getValue(parcelaPedido.qtDiasPrazo)).append(",");
        sql.append(" VLPCTPARCELA = ").append(Sql.getValue(parcelaPedido.vlPctParcela)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(parcelaPedido.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(parcelaPedido.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ParcelaPedido parcelaPedido = (ParcelaPedido) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("CDEMPRESA = ", parcelaPedido.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", parcelaPedido.cdRepresentante);
       	sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", parcelaPedido.flOrigemPedido);
       	sqlWhereClause.addAndCondition("NUPEDIDO = ", parcelaPedido.nuPedido);
       	sqlWhereClause.addAndCondition("CDPARCELA = ", parcelaPedido.cdParcela);
        sql.append(sqlWhereClause.getSql());
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by DTVENCIMENTO");
    }
    
	@Override
	protected boolean isNotSaveTabelasEnvio() {
		return true;
	}
}