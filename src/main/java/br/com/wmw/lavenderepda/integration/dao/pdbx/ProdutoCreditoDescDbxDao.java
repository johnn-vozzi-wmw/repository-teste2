package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ProdutoCreditoDescDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoCreditoDesc();
	}

    private static ProdutoCreditoDescDbxDao instance = null;
	

    public ProdutoCreditoDescDbxDao() {
        super(ProdutoCreditoDesc.TABLE_NAME);
    }
    
    public static ProdutoCreditoDescDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoCreditoDescDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoCreditoDesc produtoCreditoDesconto = new ProdutoCreditoDesc();
        produtoCreditoDesconto.rowKey = rs.getString("rowkey");
        produtoCreditoDesconto.cdEmpresa = rs.getString("cdEmpresa");
        produtoCreditoDesconto.cdProduto = rs.getString("cdProduto");
        produtoCreditoDesconto.cdProdutoCreditoDesc = rs.getString("cdProdutoCreditoDesc");
        produtoCreditoDesconto.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
        produtoCreditoDesconto.qtItem = ValueUtil.round(rs.getDouble("qtItem"));
        produtoCreditoDesconto.vlUnitario = ValueUtil.round(rs.getDouble("vlUnitario"));
        produtoCreditoDesconto.flTipoCadastroProduto = rs.getString("flTipoCadastroProduto");
        produtoCreditoDesconto.dtVigenciaFinal = rs.getDate("dtVigenciaFinal");
        produtoCreditoDesconto.flInativacaoAuto = rs.getString("flInativacaoAuto");
        produtoCreditoDesconto.dtCriacao = rs.getDate("dtCriacao");
        produtoCreditoDesconto.hrCriacao = rs.getString("hrCriacao");
        produtoCreditoDesconto.cdUsuario = rs.getString("cdUsuario");
        produtoCreditoDesconto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoCreditoDesconto.nuCarimbo = rs.getInt("nuCarimbo");
        produtoCreditoDesconto.dtAlteracao = rs.getDate("dtAlteracao");
        produtoCreditoDesconto.hrAlteracao = rs.getString("hrAlteracao");
        return produtoCreditoDesconto;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDPRODUTOCREDITODESC,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" QTITEM,");
        sql.append(" VLUNITARIO,");
        sql.append(" FLTIPOCADASTROPRODUTO,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" FLINATIVACAOAUTO,");
        sql.append(" DTCRIACAO,");
        sql.append(" HRCRIACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDPRODUTOCREDITODESC,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" QTITEM,");
        sql.append(" VLUNITARIO,");
        sql.append(" FLTIPOCADASTROPRODUTO,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" FLINATIVACAOAUTO,");
        sql.append(" DTCRIACAO,");
        sql.append(" HRCRIACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domain;
        sql.append(Sql.getValue(produtoCreditoDesconto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.cdProdutoCreditoDesc)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.dtVigenciaInicial)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.qtItem)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.vlUnitario)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.flTipoCadastroProduto)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.dtVigenciaFinal)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.flInativacaoAuto)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.dtCriacao)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.hrCriacao)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.cdUsuario)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.dtAlteracao)).append(",");
        sql.append(Sql.getValue(produtoCreditoDesconto.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domain;
        sql.append(" QTITEM = ").append(Sql.getValue(produtoCreditoDesconto.qtItem)).append(",");
        sql.append(" VLUNITARIO = ").append(Sql.getValue(produtoCreditoDesconto.vlUnitario)).append(",");
        sql.append(" FLTIPOCADASTROPRODUTO = ").append(Sql.getValue(produtoCreditoDesconto.flTipoCadastroProduto)).append(",");
        sql.append(" DTVIGENCIAFINAL = ").append(Sql.getValue(produtoCreditoDesconto.dtVigenciaFinal)).append(",");
        sql.append(" FLINATIVACAOAUTO = ").append(Sql.getValue(produtoCreditoDesconto.flInativacaoAuto)).append(",");
        sql.append(" DTCRIACAO = ").append(Sql.getValue(produtoCreditoDesconto.dtCriacao)).append(",");
        sql.append(" HRCRIACAO = ").append(Sql.getValue(produtoCreditoDesconto.hrCriacao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(produtoCreditoDesconto.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoCreditoDesconto.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(produtoCreditoDesconto.nuCarimbo)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(produtoCreditoDesconto.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(produtoCreditoDesconto.hrAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoCreditoDesconto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoCreditoDesconto.cdProduto);
		sqlWhereClause.addAndCondition("DTVIGENCIAINICIAL = ", produtoCreditoDesconto.dtVigenciaInicial);
		sqlWhereClause.addAndCondition("FLTIPOCADASTROPRODUTO = ", produtoCreditoDesconto.flTipoCadastroProduto);
		
		sqlWhereClause.addAndCondition(" DTVIGENCIAINICIAL IS NOT NULL AND DTVIGENCIAFINAL IS NOT NULL ");
		
		sql.append(sqlWhereClause.getSql());
    }

    
    public Vector findAllByItemPedidoList(Pedido pedido, BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domain, sql);
        sql.append(" from ");
        sql.append(tableName);
        addWhereByExample(domain, sql);
        sql.append(" and DTVIGENCIAINICIAL <= ").append(Sql.getValue(DateUtil.getCurrentDate()));
        sql.append(" and DTVIGENCIAFINAL >= ").append(Sql.getValue(DateUtil.getCurrentDate()));
        boolean addCdproduto = true;
        for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
        	ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
        	if (itemPedido.qtdCreditoDesc == 0) {
        		if (addCdproduto) {
        			sql.append(" and CDPRODUTO in (");
        			addCdproduto = false;
        		} else {
        			sql.append(",");
        		}
        		sql.append(Sql.getValue(itemPedido.cdProduto));
        	}
		}
        if (addCdproduto) {
        	return new Vector();
        }
        sql.append(")");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			while (rs.next()) {
		        result.addElement(populate(domain, rs));
			}
			return result;
		}
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	if (!"DSPRODUTO".equals(domain.sortAtributte)) {
    		super.addOrderBy(sql, domain);
    	}
    }
}