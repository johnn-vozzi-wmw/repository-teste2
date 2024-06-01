package br.com.wmw.lavenderepda.integration.dao.pdbx;


import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MargemRentab;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class MargemRentabDao extends CrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MargemRentab();
	}

    private static MargemRentabDao instance = null;

    public MargemRentabDao() {
        super(MargemRentab.TABLE_NAME);
    }
    
    public static MargemRentabDao getInstance() {
        if (instance == null) {
            instance = new MargemRentabDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MargemRentab margemRentab = new MargemRentab();
        margemRentab.rowKey = rs.getString("rowkey");
        margemRentab.cdEmpresa = rs.getString("cdEmpresa");
        margemRentab.cdMargemRentab = rs.getString("cdMargemRentab");
        margemRentab.dtIniVigencia = rs.getDate("dtIniVigencia");
        margemRentab.dtFimVigencia = rs.getDate("dtFimVigencia");
        margemRentab.cdPlataformaVenda = rs.getString("cdPlataformaVenda");
        margemRentab.cdCentroCusto = rs.getString("cdCentroCusto");
        margemRentab.cdGrupoDescCli = rs.getString("cdGrupoDescCli");
        margemRentab.cdCliente = rs.getString("cdCliente");
        margemRentab.cdLinha = rs.getString("cdLinha");
        margemRentab.cdStatusColecao = rs.getString("cdStatusColecao");
        margemRentab.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        margemRentab.cdProduto = rs.getString("cdProduto");
        margemRentab.flMargemRentabPedido = rs.getString("flMargemRentabPedido");
        margemRentab.cdTabelaPreco = rs.getString("cdTabelaPreco");
        margemRentab.vlPctMargemRentabMin = rs.getDouble("vlPctMargemRentabMin");
        margemRentab.vlPctMargemRentabMax = rs.getDouble("vlPctMargemRentabMax");
        margemRentab.flTipoAlteracao = rs.getString("flTipoAlteracao");
        margemRentab.nuCarimbo = rs.getInt("nuCarimbo");
        margemRentab.dtAlteracao = rs.getDate("dtAlteracao");
        margemRentab.hrAlteracao = rs.getString("hrAlteracao");
        margemRentab.cdUsuario = rs.getString("cdUsuario");
        return margemRentab;
    }
    
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDMARGEMRENTAB,");
        sql.append(" DTINIVIGENCIA,");
        sql.append(" DTFIMVIGENCIA,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" CDCENTROCUSTO,");
        sql.append(" CDGRUPODESCCLI,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDLINHA,");
        sql.append(" CDSTATUSCOLECAO,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLMARGEMRENTABPEDIDO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" VLPCTMARGEMRENTABMIN,");
        sql.append(" VLPCTMARGEMRENTABMAX,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
    //@Override
    protected void addInsertColumns(StringBuffer sql)  {  }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MargemRentab margemrentab = (MargemRentab) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", margemrentab.cdEmpresa);
		sqlWhereClause.addAndCondition("CDMARGEMRENTAB = ", margemrentab.cdMargemRentab);
		sqlWhereClause.addAndCondition("DTINIVIGENCIA <= ", margemrentab.dtIniVigencia);
		sqlWhereClause.addAndCondition("DTFIMVIGENCIA >= ", margemrentab.dtFimVigencia);
		sqlWhereClause.addAndConditionOr("IFNULL(CDPLATAFORMAVENDA,'0')= ", new String[] {margemrentab.cdPlataformaVenda, ValueUtil.VALOR_ZERO, null});
		sqlWhereClause.addAndConditionOr("IFNULL(CDCENTROCUSTO,'0')= ", new String[] {margemrentab.cdCentroCusto, ValueUtil.VALOR_ZERO, null});
		sqlWhereClause.addAndConditionOr("IFNULL(CDGRUPODESCCLI,'0') = ", new String[] {margemrentab.cdGrupoDescCli, ValueUtil.VALOR_ZERO, null});
		sqlWhereClause.addAndConditionOr("IFNULL(CDCLIENTE,'0') = ", new String[] {margemrentab.cdCliente, ValueUtil.VALOR_ZERO, null});
		sqlWhereClause.addAndConditionOr("IFNULL(CDLINHA,'0') = ", new String[] {margemrentab.cdLinha, ValueUtil.VALOR_ZERO, null});
		sqlWhereClause.addAndConditionOr("IFNULL(CDSTATUSCOLECAO,'0') = ", new String[] {margemrentab.cdStatusColecao, ValueUtil.VALOR_ZERO, null});
		sqlWhereClause.addAndConditionOr("IFNULL(CDGRUPOPRODUTO1,'0') = ", new String[] {margemrentab.cdGrupoProduto1, ValueUtil.VALOR_ZERO, null});
		sqlWhereClause.addAndConditionOr("IFNULL(CDPRODUTO,'0') = ", new String[] {margemrentab.cdProduto, ValueUtil.VALOR_ZERO, null});
        sqlWhereClause.addAndConditionOr("IFNULL(CDTABELAPRECO,'0') = ", new String[] {margemrentab.cdTabelaPreco, ValueUtil.VALOR_ZERO, null});
		if (ValueUtil.valueEquals(margemrentab.flMargemRentabPedido, ValueUtil.VALOR_SIM)) {
			sqlWhereClause.addAndCondition("IFNULL(FLMARGEMRENTABPEDIDO,'0') = ", ValueUtil.VALOR_SIM);			
		} else {
			sqlWhereClause.addAndCondition("IFNULL(FLMARGEMRENTABPEDIDO,'0') <> ", ValueUtil.VALOR_SIM);
		}
		sql.append(sqlWhereClause.getSql());
    }

	public String findCdMargemRent(MargemRentab margemRentabFilter) throws SQLException {
		margemRentabFilter.cdPlataformaVenda = ValueUtil.isNotEmpty(margemRentabFilter.cdPlataformaVenda) ? margemRentabFilter.cdPlataformaVenda : ValueUtil.VALOR_ZERO;
		margemRentabFilter.cdCentroCusto = ValueUtil.isNotEmpty(margemRentabFilter.cdCentroCusto) ? margemRentabFilter.cdCentroCusto : ValueUtil.VALOR_ZERO;
		margemRentabFilter.cdGrupoDescCli = ValueUtil.isNotEmpty(margemRentabFilter.cdGrupoDescCli) ? margemRentabFilter.cdGrupoDescCli : ValueUtil.VALOR_ZERO;
		margemRentabFilter.cdCliente = ValueUtil.isNotEmpty(margemRentabFilter.cdCliente) ? margemRentabFilter.cdCliente : ValueUtil.VALOR_ZERO;
		margemRentabFilter.cdLinha = ValueUtil.isNotEmpty(margemRentabFilter.cdLinha) ? margemRentabFilter.cdLinha : ValueUtil.VALOR_ZERO;
		margemRentabFilter.cdStatusColecao = ValueUtil.isNotEmpty(margemRentabFilter.cdStatusColecao) ? margemRentabFilter.cdStatusColecao : ValueUtil.VALOR_ZERO;
		margemRentabFilter.cdGrupoProduto1 = ValueUtil.isNotEmpty(margemRentabFilter.cdGrupoProduto1) ? margemRentabFilter.cdGrupoProduto1 : ValueUtil.VALOR_ZERO;
		margemRentabFilter.cdProduto = ValueUtil.isNotEmpty(margemRentabFilter.cdProduto) ? margemRentabFilter.cdProduto : ValueUtil.VALOR_ZERO;
        margemRentabFilter.cdTabelaPreco = ValueUtil.isNotEmpty(margemRentabFilter.cdTabelaPreco) ? margemRentabFilter.cdTabelaPreco : ValueUtil.VALOR_ZERO;
		margemRentabFilter.dtIniVigencia = DateUtil.getCurrentDate();
		margemRentabFilter.dtFimVigencia = DateUtil.getCurrentDate();
		margemRentabFilter.limit = 1;
		Vector margemRentabList = findColumnValuesByExample(margemRentabFilter, "CDMARGEMRENTAB");
		return ValueUtil.isNotEmpty(margemRentabList) ? (String) margemRentabList.items[0] : null;
	}
	
	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		List<String> colunas = LavenderePdaConfig.getListaOrdenacaoMargemRentabilidade();
    	if (ValueUtil.isEmpty(colunas)) {
			return;
	    }
    	
    	boolean possuiTB = sql.indexOf(" tb ") > -1 || sql.indexOf("tb.") > -1; 
    	sql.append(" order by ");
		List<String> ascDescColumn;
    	for (int i = 0; i < colunas.size(); i++) {
		    String coluna = colunas.get(i);
		    ascDescColumn = Arrays.asList(coluna.split(":"));
		    if (MargemRentab.NMCOLUNA_DTINIVIGENCIA.equalsIgnoreCase(ascDescColumn.get(0)) || MargemRentab.NMCOLUNA_DTFIMVIGENCIA.equalsIgnoreCase(ascDescColumn.get(0))) {
			    appendOrderColunaData(sql, possuiTB, ascDescColumn, i, colunas);
			    continue;
			} else {
				sql.append(" case when ").append(possuiTB ? "tb." : "").append(ascDescColumn.get(0)).append(" in ('0', 'N', null)  then '0' else '1' end");
			}
		    addAscDescParameter(sql, ascDescColumn);
		    appendComma(sql, i, colunas);
	    }
	}

	private void appendOrderColunaData(StringBuffer sql, boolean possuiTB, List<String> ascDescColumn, int i, List<String> colunas) {
		sql.append(" case when ").append(possuiTB ? "tb." : "").append(ascDescColumn.get(0))
				.append(" in ('0', 'N', null)  then '0' else ")
				.append(" CAST(strftime('%Y%m%d', ").append(ascDescColumn.get(0)).append(") AS int)")
				.append(" end ");
		addAscDescParameter(sql, ascDescColumn);
		appendComma(sql, i, colunas);
	}

	private void appendComma(StringBuffer sql, int i, List<String> colunas) {
		if (i < colunas.size() - 1) {
			sql.append(", ");
		}
	}

	private void addAscDescParameter(StringBuffer sql, List<String> ascDescColumn) {
		if (ascDescColumn.size() > 1) {
			sql.append(" ").append(ascDescColumn.get(1)).append(" ");
		} else {
			sql.append(" DESC ");
		}
	}


}
