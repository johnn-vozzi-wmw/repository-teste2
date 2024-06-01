package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ValorizacaoProd;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ValorizacaoProdDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ValorizacaoProd();
	}

    private static ValorizacaoProdDbxDao instance = null;

	public ValorizacaoProdDbxDao() {
		super(ValorizacaoProd.TABLE_NAME);
	}

    public ValorizacaoProdDbxDao(String newTableName) {
		super(newTableName);
    }
    
    public static ValorizacaoProdDbxDao getInstance() {
        if (instance == null) {
            instance = new ValorizacaoProdDbxDao(ValorizacaoProd.TABLE_NAME);
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ValorizacaoProd valorizacaoProd = new ValorizacaoProd();
        valorizacaoProd.rowKey = rs.getString("rowkey");
        valorizacaoProd.cdEmpresa = rs.getString("cdEmpresa");
        valorizacaoProd.cdRepresentante = rs.getString("cdRepresentante");
        valorizacaoProd.cdProduto = rs.getString("cdProduto");
        valorizacaoProd.cdProdutoValorizacao = rs.getString("cdProdutoValorizacao");
        valorizacaoProd.qtItem = ValueUtil.round(rs.getDouble("qtItem"));
        valorizacaoProd.vlUnitario = ValueUtil.round(rs.getDouble("vlUnitario"));
        valorizacaoProd.vlTotalItem = ValueUtil.round(rs.getDouble("vlTotalItem"));
        valorizacaoProd.dsObservacao = rs.getString("dsObservacao");
        valorizacaoProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        valorizacaoProd.dtValorizacao = rs.getDate("dtValorizacao");
        valorizacaoProd.hrValorizacao = rs.getString("hrValorizacao");
        valorizacaoProd.dtAlteracao = rs.getDate("dtAlteracao");
        valorizacaoProd.hrAlteracao = rs.getString("hrAlteracao");
        valorizacaoProd.nuCarimbo = rs.getInt("nuCarimbo");
        valorizacaoProd.cdUsuario = rs.getString("cdUsuario");
        valorizacaoProd.dsProduto = rs.getString("dsProduto");
        return valorizacaoProd;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ValorizacaoProd valorizacaoProd = new ValorizacaoProd();
        valorizacaoProd.rowKey = rs.getString("rowkey");
        valorizacaoProd.cdEmpresa = rs.getString("cdEmpresa");
        valorizacaoProd.cdRepresentante = rs.getString("cdRepresentante");
        valorizacaoProd.cdProduto = rs.getString("cdProduto");
        valorizacaoProd.cdProdutoValorizacao = rs.getString("cdProdutoValorizacao");
        valorizacaoProd.qtItem = ValueUtil.round(rs.getDouble("qtItem"));
        valorizacaoProd.vlUnitario = ValueUtil.round(rs.getDouble("vlUnitario"));
        valorizacaoProd.vlTotalItem = ValueUtil.round(rs.getDouble("vlTotalItem"));
        valorizacaoProd.dsObservacao = rs.getString("dsObservacao");
        valorizacaoProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        valorizacaoProd.dtValorizacao = rs.getDate("dtValorizacao");
        valorizacaoProd.hrValorizacao = rs.getString("hrValorizacao");
        return valorizacaoProd;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.CDPRODUTOVALORIZACAO,");
        sql.append(" tb.QTITEM,");
        sql.append(" tb.VLUNITARIO,");
        sql.append(" tb.VLTOTALITEM,");
        sql.append(" tb.DSOBSERVACAO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.DTVALORIZACAO,");
        sql.append(" tb.HRVALORIZACAO,");
        sql.append(" tb.DTALTERACAO,");
        sql.append(" tb.HRALTERACAO,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tb.CDUSUARIO ");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.CDPRODUTOVALORIZACAO,");
        sql.append(" tb.QTITEM,");
        sql.append(" tb.VLUNITARIO,");
        sql.append(" tb.VLTOTALITEM,");
        sql.append(" tb.DSOBSERVACAO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.DTVALORIZACAO,");
        sql.append(" tb.HRVALORIZACAO");
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDPRODUTOVALORIZACAO,");
        sql.append(" QTITEM,");
        sql.append(" VLUNITARIO,");
        sql.append(" VLTOTALITEM,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTVALORIZACAO,");
        sql.append(" HRVALORIZACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ValorizacaoProd valorizacaoProd = (ValorizacaoProd) domain;
        sql.append(Sql.getValue(valorizacaoProd.cdEmpresa)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.cdRepresentante)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.cdProduto)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.cdProdutoValorizacao)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.qtItem)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.vlUnitario)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.vlTotalItem)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.dsObservacao)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.dtValorizacao)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.hrValorizacao)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.dtAlteracao)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.hrAlteracao)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.nuCarimbo)).append(",");
        sql.append(Sql.getValue(valorizacaoProd.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ValorizacaoProd valorizacaoProd = (ValorizacaoProd) domain;
        sql.append(" QTITEM = ").append(Sql.getValue(valorizacaoProd.qtItem)).append(",");
        sql.append(" VLUNITARIO = ").append(Sql.getValue(valorizacaoProd.vlUnitario)).append(",");
        sql.append(" VLTOTALITEM = ").append(Sql.getValue(valorizacaoProd.vlTotalItem)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(valorizacaoProd.dsObservacao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(valorizacaoProd.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(valorizacaoProd.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(valorizacaoProd.hrAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(valorizacaoProd.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ValorizacaoProd valorizacaoProd = (ValorizacaoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndLikeCondition("P.CDPRODUTO", valorizacaoProd.cdProduto, false);
		sqlWhereClause.addAndCondition("DTVALORIZACAO >= ", valorizacaoProd.dtValorizacaoFiltroInic);
		sqlWhereClause.addAndCondition("DTVALORIZACAO <= ", valorizacaoProd.dtValorizacaoFiltroFim);
		if (ValorizacaoProd.STATUS_ENVIADO.equals(valorizacaoProd.flTipoAlteracao)) {
			sqlWhereClause.addAndCondition("TB.FLTIPOALTERACAO = ''");
		} else if (ValorizacaoProd.STATUS_NAO_ENVIADO.equals(valorizacaoProd.flTipoAlteracao)) {
			sqlWhereClause.addAndCondition("TB.FLTIPOALTERACAO != ''");
		}
		sqlWhereClause.addStartAndMultipleCondition();
		boolean adicionouInicioBloco = false;
		adicionouInicioBloco |= sqlWhereClause.addOrCondition("P.DSPRODUTO like ", valorizacaoProd.dsProduto);
		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("P.CDPRODUTO", valorizacaoProd.cdProdutoLikeFilter, false);
		adicionouInicioBloco |= sqlWhereClause.addOrCondition("P.DSPRINCIPIOATIVO = ", valorizacaoProd.dsPrincipioAtivo);
		adicionouInicioBloco |= sqlWhereClause.addOrCondition("P.NUCODIGOBARRAS = ", valorizacaoProd.nuCodigoBarras);
		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("P.DSMARCA", valorizacaoProd.dsMarca);
		adicionouInicioBloco |= sqlWhereClause.addOrCondition("P.DSREFERENCIA = ", valorizacaoProd.dsReferencia);
		adicionouInicioBloco |= sqlWhereClause.addOrCondition("P.DSREFERENCIA = ", valorizacaoProd.dsReferenciaLikeFilter);
       	if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
		sql.append(sqlWhereClause.getSql());
    }
    
	@Override
	public Vector findAllByExample(BaseDomain domain) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		addSelectColumns(domain, sql);
		sql.append(", P.DSPRODUTO ");
		sql.append(" FROM ").append(tableName).append(" TB ");
        addJoin(null, sql);
		addWhereByExample(domain, sql);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector registros = new Vector(50);
			while (rs.next()) {
				registros.addElement(populate(domain, rs));
			}
			return registros;
		}
	}
	
	@Override
	public BaseDomain findByRowKey(String rowKey) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domainFilter, sql);
        sql.append(", DSPRODUTO ");
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addJoin(domainFilter, sql);
        sql.append(" where tb.rowKey = ");
        sql.append(Sql.getValue(rowKey));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
	        if (rs.next()) {
	            return populate(domainFilter, rs);
	        }
        }
        return null;
	}
	
	public void deleteAllByDtEmissao(BaseDomain domain) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" DELETE FROM ").append(tableName);
		ValorizacaoProd filtroValorizacao = (ValorizacaoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DTVALORIZACAO <= ", filtroValorizacao.dtValorizacao);
		sql.append(sqlWhereClause.getSql());
        try {
        	executeUpdate(sql.toString());
        } catch (Throwable e) {
			// Não faz anda, apenas não exclui nenhum registro
		}
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append("JOIN TBLVPPRODUTO P ON P.CDPRODUTO = TB.CDPRODUTO ");
	}
	
}