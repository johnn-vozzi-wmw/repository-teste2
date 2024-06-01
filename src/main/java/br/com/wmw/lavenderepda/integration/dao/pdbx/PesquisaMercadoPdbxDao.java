package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import totalcross.sql.ResultSet;

public class PesquisaMercadoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PesquisaMercado();
	}

    private static PesquisaMercadoPdbxDao instance;

    public PesquisaMercadoPdbxDao() {
        super(PesquisaMercado.TABLE_NAME);
    }

    public static PesquisaMercadoPdbxDao getInstance() {
        if (instance == null) {
            instance = new PesquisaMercadoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        PesquisaMercado pesquisaMercado = new PesquisaMercado();
        pesquisaMercado.rowKey = rs.getString("rowkey");
        pesquisaMercado.cdEmpresa = rs.getString("cdEmpresa");
        pesquisaMercado.cdRepresentante = rs.getString("cdRepresentante");
        pesquisaMercado.flOrigemPesquisaMercado = rs.getString("flOrigempesquisamercado");
        pesquisaMercado.cdPesquisaMercado = rs.getString("cdPesquisamercado");
        pesquisaMercado.cdCliente = rs.getString("cdCliente");
        pesquisaMercado.cdProduto = rs.getString("cdProduto");
        pesquisaMercado.cdConcorrente = rs.getString("cdConcorrente");
        pesquisaMercado.dtEmissao = rs.getDate("dtEmissao");
        pesquisaMercado.vlUnitario = ValueUtil.round(rs.getDouble("vlUnitario"));
        pesquisaMercado.qtItem = ValueUtil.round(rs.getDouble("qtItem"));
        pesquisaMercado.dsObservacao = rs.getString("dsObservacao");
        pesquisaMercado.cdProdutoConcorrente = rs.getString("cdProdutoConcorrente");
        pesquisaMercado.qtItemFrente = rs.getInt("qtItemFrente");
        pesquisaMercado.qtItemProfundidade = rs.getInt("qtItemProfundidade");
        pesquisaMercado.qtItemAndar = rs.getInt("qtItemAndar");
        pesquisaMercado.qtItemTotal = rs.getInt("qtItemTotal");
        pesquisaMercado.cdTipoPesquisa = rs.getString("cdTipoPesquisa");
        pesquisaMercado.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pesquisaMercado.flPesquisaNovoCliente = rs.getString("flPesquisaNovoCliente");
        pesquisaMercado.cdUsuarioEmissao = rs.getString("cdUsuarioEmissao");
        if (LavenderePdaConfig.excluiPesquisaMercadoPedido) {
        	pesquisaMercado.nuPedido = rs.getString("nuPedido");
        	pesquisaMercado.flOrigemPedido = rs.getString("flOrigemPedido");
        }
        if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
        	pesquisaMercado.cdLatitude = rs.getDouble("cdLatitude");
        	pesquisaMercado.cdLongitude = rs.getDouble("cdLongitude");
        }
        pesquisaMercado.cdUsuario = rs.getString("cdUsuario");
        return pesquisaMercado;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPESQUISAMERCADO,");
        sql.append(" CDPESQUISAMERCADO,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCONCORRENTE,");
        sql.append(" DTEMISSAO,");
        sql.append(" VLUNITARIO,");
        sql.append(" QTITEM,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" CDPRODUTOCONCORRENTE,");
        sql.append(" QTITEMFRENTE,");
        sql.append(" QTITEMPROFUNDIDADE,");
        sql.append(" QTITEMANDAR,");
        sql.append(" QTITEMTOTAL,");
        sql.append(" CDTIPOPESQUISA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLPESQUISANOVOCLIENTE,");
        sql.append(" CDUSUARIOEMISSAO,");
        if (LavenderePdaConfig.excluiPesquisaMercadoPedido) {
        	sql.append(" NUPEDIDO,");
        	sql.append(" FLORIGEMPEDIDO,");
        }
        if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
        	sql.append(" CDLATITUDE, ");
        	sql.append(" CDLONGITUDE, ");
        }
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPESQUISAMERCADO,");
        sql.append(" CDPESQUISAMERCADO,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCONCORRENTE,");
        sql.append(" DTEMISSAO,");
        sql.append(" VLUNITARIO,");
        sql.append(" QTITEM,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" CDPRODUTOCONCORRENTE,");
        sql.append(" QTITEMFRENTE,");
        sql.append(" QTITEMPROFUNDIDADE,");
        sql.append(" QTITEMANDAR,");
        sql.append(" QTITEMTOTAL,");
        sql.append(" CDTIPOPESQUISA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLPESQUISANOVOCLIENTE,");
        sql.append(" CDUSUARIOEMISSAO,");
        if (LavenderePdaConfig.excluiPesquisaMercadoPedido) {
        	sql.append(" NUPEDIDO,");
        	sql.append(" FLORIGEMPEDIDO,");
        }
    	sql.append(" CDLATITUDE, ");
    	sql.append(" CDLONGITUDE, ");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PesquisaMercado pesquisaMercado = (PesquisaMercado) domain;
        sql.append(Sql.getValue(pesquisaMercado.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.flOrigemPesquisaMercado)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.cdPesquisaMercado)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.cdCliente)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.cdProduto)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.cdConcorrente)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.dtEmissao)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.vlUnitario)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.qtItem)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.dsObservacao)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.cdProdutoConcorrente)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.qtItemFrente)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.qtItemProfundidade)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.qtItemAndar)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.qtItemTotal)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.cdTipoPesquisa)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.flPesquisaNovoCliente)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.cdUsuarioEmissao)).append(",");
        if (LavenderePdaConfig.excluiPesquisaMercadoPedido) {
        	sql.append(Sql.getValue(pesquisaMercado.nuPedido)).append(",");
        	sql.append(Sql.getValue(pesquisaMercado.flOrigemPedido)).append(",");
        }
    	sql.append(Sql.getValue(pesquisaMercado.cdLatitude)).append(",");
    	sql.append(Sql.getValue(pesquisaMercado.cdLongitude)).append(",");
        sql.append(Sql.getValue(pesquisaMercado.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PesquisaMercado pesquisaMercado = (PesquisaMercado) domain;
        sql.append(" CDPRODUTO = ").append(Sql.getValue(pesquisaMercado.cdProduto)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(pesquisaMercado.cdCliente)).append(",");
        sql.append(" CDCONCORRENTE = ").append(Sql.getValue(pesquisaMercado.cdConcorrente)).append(",");
        sql.append(" VLUNITARIO = ").append(Sql.getValue(pesquisaMercado.vlUnitario)).append(",");
        sql.append(" QTITEM = ").append(Sql.getValue(pesquisaMercado.qtItem)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(pesquisaMercado.dsObservacao)).append(",");
        sql.append(" QTITEMFRENTE = ").append(Sql.getValue(pesquisaMercado.qtItemFrente)).append(",");
        sql.append(" QTITEMPROFUNDIDADE = ").append(Sql.getValue(pesquisaMercado.qtItemProfundidade)).append(",");
        sql.append(" QTITEMANDAR = ").append(Sql.getValue(pesquisaMercado.qtItemAndar)).append(",");
        sql.append(" QTITEMTOTAL = ").append(Sql.getValue(pesquisaMercado.qtItemTotal)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pesquisaMercado.flTipoAlteracao)).append(",");
        sql.append(" FLPESQUISANOVOCLIENTE = ").append(Sql.getValue(pesquisaMercado.flPesquisaNovoCliente)).append(",");
        if (LavenderePdaConfig.excluiPesquisaMercadoPedido) {
        	sql.append(" NUPEDIDO = ").append(Sql.getValue(pesquisaMercado.nuPedido)).append(",");
        	sql.append(" FLORIGEMPEDIDO = ").append(Sql.getValue(pesquisaMercado.flOrigemPedido)).append(",");
        }
        if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
        	sql.append(" CDLATITUDE = ").append(Sql.getValue(pesquisaMercado.cdLatitude)).append(",");
        	sql.append(" CDLONGITUDE = ").append(Sql.getValue(pesquisaMercado.cdLongitude)).append(",");
        }
        sql.append(" cdUsuario = ").append(Sql.getValue(pesquisaMercado.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PesquisaMercado pesquisamercado = (PesquisaMercado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pesquisamercado.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pesquisamercado.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPESQUISAMERCADO = ", pesquisamercado.flOrigemPesquisaMercado);
		sqlWhereClause.addAndCondition("CDPESQUISAMERCADO = ", pesquisamercado.cdPesquisaMercado);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", pesquisamercado.cdCliente);
		sqlWhereClause.addAndCondition("CDTIPOPESQUISA = ", pesquisamercado.cdTipoPesquisa);
		if (LavenderePdaConfig.excluiPesquisaMercadoPedido) {
			sqlWhereClause.addAndCondition("NUPEDIDO = ", pesquisamercado.nuPedido);
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }

    //@Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by DTEMISSAO desc");
    }

    public int deleteAllByDtEmissao(BaseDomain domain) {
        StringBuffer sql = getSqlBuffer();
        sql.append(" delete from ").append(tableName);
        //--
        PesquisaMercado pesquisamercado = (PesquisaMercado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DTEMISSAO <= ", pesquisamercado.dtEmissao);
		sql.append(sqlWhereClause.getSql());
        //--
        try {
        	return executeUpdate(sql.toString());
        } catch (Throwable e) {
			// Não faz anda, apenas não exclui nenhum registro
		}
        return 0;
    }

    protected void addSelectGridColumns(StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" VLUNITARIO,");
        sql.append(" DTEMISSAO,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCONCORRENTE");
    }
    
    public void clearPedidoFromPesquisaMercado(PesquisaMercado filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("UPDATE ").append(tableName)
    	.append(" SET NUPEDIDO = NULL, FLORIGEMPEDIDO = NULL");
    	addWhereByExample(filter, sql);
    	executeUpdate(sql.toString());
    }
}