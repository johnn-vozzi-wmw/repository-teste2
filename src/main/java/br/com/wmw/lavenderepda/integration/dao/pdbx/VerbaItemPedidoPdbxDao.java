package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.VerbaItemPedido;
import totalcross.sql.ResultSet;

public class VerbaItemPedidoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaItemPedido();
	}

    private static VerbaItemPedidoPdbxDao instance;

    public VerbaItemPedidoPdbxDao() {
        super(VerbaItemPedido.TABLE_NAME);
    }

    public static VerbaItemPedidoPdbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaItemPedidoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VerbaItemPedido verbaItemPedido = new VerbaItemPedido();
        verbaItemPedido.rowKey = rs.getString("rowkey");
        verbaItemPedido.cdEmpresa = rs.getString("cdEmpresa");
        verbaItemPedido.cdRepresentante = rs.getString("cdRepresentante");
        verbaItemPedido.cdVerba = rs.getString("cdVerba");
        verbaItemPedido.cdUsuario = rs.getString("cdUsuario");
        verbaItemPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        verbaItemPedido.nuPedido = rs.getString("nuPedido");
        verbaItemPedido.cdProduto = rs.getString("cdProduto");
        verbaItemPedido.flTipoItemPedido = rs.getString("flTipoItemPedido");
        verbaItemPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        verbaItemPedido.vlConsumido = rs.getDouble("vlConsumido");
        verbaItemPedido.vlGerado = rs.getDouble("vlGerado");
        return verbaItemPedido;
    }

    //@Override
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDVERBA,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" cdProduto,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" vlGerado,");
        sql.append(" vlConsumido,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDVERBA,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" cdProduto,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" vlGerado,");
        sql.append(" vlConsumido,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaItemPedido verbaItemPedido = (VerbaItemPedido) domain;
        sql.append(Sql.getValue(verbaItemPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.cdVerba)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.cdUsuario)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.cdProduto)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.flTipoItemPedido)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.vlGerado)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.vlConsumido)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(verbaItemPedido.flTipoAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaItemPedido verbaItemPedido = (VerbaItemPedido) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(verbaItemPedido.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(verbaItemPedido.flTipoAlteracao));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaItemPedido verbaItemPedido = (VerbaItemPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", verbaItemPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", verbaItemPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("CDVERBA = ", verbaItemPedido.cdVerba);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", verbaItemPedido.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", verbaItemPedido.nuPedido);
		sqlWhereClause.addAndCondition("cdProduto = ", verbaItemPedido.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", verbaItemPedido.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", verbaItemPedido.nuSeqProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}