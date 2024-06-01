package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.DescFaixaValorPedido;
import totalcross.sql.ResultSet;

public class DescFaixaValorPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescFaixaValorPedido();
	}

    private static DescFaixaValorPedidoDbxDao instance;

    public DescFaixaValorPedidoDbxDao() {
        super(DescFaixaValorPedido.TABLE_NAME); 
    }
    
    public static DescFaixaValorPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new DescFaixaValorPedidoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        DescFaixaValorPedido descFaixaValorPedido = new DescFaixaValorPedido();
        descFaixaValorPedido.rowKey = rs.getString("rowkey");
        descFaixaValorPedido.cdEmpresa = rs.getString("cdEmpresa");
        descFaixaValorPedido.cdRepresentante = rs.getString("cdRepresentante");
        descFaixaValorPedido.cdFaixaValorPedido = rs.getString("cdFaixaValorPedido");
        descFaixaValorPedido.vlTotalPedido = ValueUtil.round(rs.getDouble("vlTotalPedido"));
        descFaixaValorPedido.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        descFaixaValorPedido.nuCarimbo = rs.getInt("nuCarimbo");
        descFaixaValorPedido.cdUsuario = rs.getString("cdUsuario");
        descFaixaValorPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return descFaixaValorPedido;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDFAIXAVALORPEDIDO,");
        sql.append(" VLTOTALPEDIDO,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDFAIXAVALORPEDIDO,");
        sql.append(" VLTOTALPEDIDO,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        DescFaixaValorPedido descFaixaValorPedido = (DescFaixaValorPedido) domain;
        sql.append(Sql.getValue(descFaixaValorPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(descFaixaValorPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(descFaixaValorPedido.cdFaixaValorPedido)).append(",");
        sql.append(Sql.getValue(descFaixaValorPedido.vlTotalPedido)).append(",");
        sql.append(Sql.getValue(descFaixaValorPedido.vlPctDesconto)).append(",");
        sql.append(Sql.getValue(descFaixaValorPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(descFaixaValorPedido.cdUsuario)).append(",");
        sql.append(Sql.getValue(descFaixaValorPedido.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        DescFaixaValorPedido descFaixaValorPedido = (DescFaixaValorPedido) domain;
        sql.append(" VLTOTALPEDIDO = ").append(Sql.getValue(descFaixaValorPedido.vlTotalPedido)).append(",");
        sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(descFaixaValorPedido.vlPctDesconto)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(descFaixaValorPedido.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(descFaixaValorPedido.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(descFaixaValorPedido.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        DescFaixaValorPedido descFaixaValorPedido = (DescFaixaValorPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descFaixaValorPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descFaixaValorPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("CDFAIXAVALORPEDIDO = ", descFaixaValorPedido.cdFaixaValorPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public double findFaixaDescontoPedido(double vlTotalPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" SELECT VLPCTDESCONTO FROM TBLVPDESCFAIXAVALORPEDIDO ");
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(DescFaixaValorPedido.class)));
		sql.append(" AND VLTOTALPEDIDO <= ").append(Sql.getValue(vlTotalPedido));
		sql.append(" ORDER BY VLTOTALPEDIDO DESC LIMIT 1");
		return getDouble(sql.toString());
	}
    
}