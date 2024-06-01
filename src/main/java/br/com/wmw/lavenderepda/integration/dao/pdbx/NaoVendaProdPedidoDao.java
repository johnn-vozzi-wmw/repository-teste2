package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.NaoVendaProdPedido;
import totalcross.sql.ResultSet;


public class NaoVendaProdPedidoDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NaoVendaProdPedido();
	}

	private static NaoVendaProdPedidoDao instance;

    public NaoVendaProdPedidoDao() {
        super(NaoVendaProdPedido.TABLE_NAME); 
    }
    
    public static NaoVendaProdPedidoDao getInstance() {
        if (instance == null) {
            instance = new NaoVendaProdPedidoDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        NaoVendaProdPedido naoVendaProdPedido = new NaoVendaProdPedido();
        naoVendaProdPedido.rowKey = rs.getString("rowkey");
        naoVendaProdPedido.cdEmpresa = rs.getString("cdEmpresa");
        naoVendaProdPedido.cdRepresentante = rs.getString("cdRepresentante");
        naoVendaProdPedido.nuPedido = rs.getString("nuPedido");
        naoVendaProdPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        naoVendaProdPedido.cdProduto = rs.getString("cdProduto");
        naoVendaProdPedido.cdMotivo = rs.getString("cdMotivo");
        naoVendaProdPedido.dsJustificativa = rs.getString("dsJustificativa");
        naoVendaProdPedido.cdUsuario = rs.getString("cdUsuario");
        naoVendaProdPedido.nuCarimbo = rs.getInt("nuCarimbo");
        naoVendaProdPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        naoVendaProdPedido.hrAlteracao = rs.getString("hrAlteracao");
        naoVendaProdPedido.dtAlteracao = rs.getDate("dtAlteracao");
        return naoVendaProdPedido;
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
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDMOTIVO,");
        sql.append(" DSJUSTIFICATIVA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" DTALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDMOTIVO,");
        sql.append(" DSJUSTIFICATIVA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" DTALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        NaoVendaProdPedido naoVendaProdPedido = (NaoVendaProdPedido) domain;
        sql.append(Sql.getValue(naoVendaProdPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.cdProduto)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.cdMotivo)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.dsJustificativa)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.cdUsuario)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.hrAlteracao)).append(",");
        sql.append(Sql.getValue(naoVendaProdPedido.dtAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        NaoVendaProdPedido naoVendaProdPedido = (NaoVendaProdPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", naoVendaProdPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", naoVendaProdPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", naoVendaProdPedido.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", naoVendaProdPedido.flOrigemPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }
  
}