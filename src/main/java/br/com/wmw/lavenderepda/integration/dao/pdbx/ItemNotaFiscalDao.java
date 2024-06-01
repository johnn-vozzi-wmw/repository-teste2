package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemNotaFiscal;
import totalcross.sql.ResultSet;

public class ItemNotaFiscalDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemNotaFiscal();
	}

	private static ItemNotaFiscalDao instance;

	public ItemNotaFiscalDao() {
		super(ItemNotaFiscal.TABLE_NAME);
	}
	
	public static ItemNotaFiscalDao getInstance() {
        if (instance == null) {
            instance = new ItemNotaFiscalDao();
        }
        return instance;
    }

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemNotaFiscal itemNotaFiscal = new ItemNotaFiscal();
		itemNotaFiscal.rowKey = rs.getString("rowkey");
		itemNotaFiscal.cdEmpresa = rs.getString("cdEmpresa");
		itemNotaFiscal.nuNotaFiscal = rs.getString("nuNotaFiscal");
		itemNotaFiscal.cdSerie = rs.getString("cdSerie");
		itemNotaFiscal.cdProduto = rs.getString("cdProduto");
		itemNotaFiscal.nuPedido = rs.getString("nuPedido");
		itemNotaFiscal.flOrigemPedido = rs.getString("flOrigemPedido");
		itemNotaFiscal.cdRepresentante = rs.getString("cdRepresentante");
		itemNotaFiscal.dsProduto = rs.getString("dsProduto");
		itemNotaFiscal.nuSequenciaItem = rs.getInt("nuSequenciaItem");
		itemNotaFiscal.dsUnidade = rs.getString("dsUnidade");
		itemNotaFiscal.qtItem = rs.getDouble("qtItem");
		itemNotaFiscal.vlUnitario = rs.getDouble("vlUnitario");
		itemNotaFiscal.vlTotalItem = rs.getDouble("vlTotalItem");
		itemNotaFiscal.nuCarimbo = rs.getInt("nuCarimbo");
		itemNotaFiscal.flTipoAlteracao = rs.getString("flTipoAlteracao");
		itemNotaFiscal.cdUsuario = rs.getString("cdUsuario");
		return itemNotaFiscal;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" NUNOTAFISCAL,");
		sql.append(" CDSERIE,");
		sql.append(" CDPRODUTO,");
		sql.append(" NUPEDIDO,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" DSPRODUTO,");
		sql.append(" NUSEQUENCIAITEM,");
		sql.append(" DSUNIDADE,");
		sql.append(" QTITEM,");
		sql.append(" VLUNITARIO,");
		sql.append(" VLTOTALITEM,");
		sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
		
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException { }

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemNotaFiscal itemNotaFiscal = (ItemNotaFiscal) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemNotaFiscal.cdEmpresa);
		sqlWhereClause.addAndCondition("NUNOTAFISCAL = ", itemNotaFiscal.nuNotaFiscal);
		sqlWhereClause.addAndCondition("CDSERIE = ", itemNotaFiscal.cdSerie);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemNotaFiscal.cdProduto);
		sql.append(sqlWhereClause.getSql());
	}

}
