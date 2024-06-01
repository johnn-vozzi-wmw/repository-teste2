package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ProdutoTipoRelacaoBase;

public abstract class AbstractProdutoTipoRelacaoBaseDbxDao extends LavendereCrudDbxDao {

	public AbstractProdutoTipoRelacaoBaseDbxDao(String newTableName) {
		super(newTableName);
	}
	
	protected abstract String getAliasCteFlTipoRelacao();
	protected abstract String getAliasCTEDomain();
	
	protected void addFlTipoRelacaoDomainCondition(ProdutoTipoRelacaoBase domainFilter, SqlWhereClause sqlWhereClause) {
		if (ValueUtil.isEmpty(domainFilter.flTipoRelacaoList)) {
			sqlWhereClause.addAndCondition("FLTIPORELACAO = ", domainFilter.flTipoRelacao);
			addCustomConditionByFlTipoRelacao(domainFilter, sqlWhereClause, StringUtil.getStringValue(domainFilter.flTipoRelacao));
		} else {
			sqlWhereClause.addStartAndMultipleCondition();
			for (int i = 0; i < domainFilter.flTipoRelacaoList.length; i++) {
				if (i > 0) {
					sqlWhereClause.addStartOrMultipleCondition();
				}
				sqlWhereClause.addOrCondition("FLTIPORELACAO = " + Sql.getValue(domainFilter.flTipoRelacaoList[i]));
				addCustomConditionByFlTipoRelacao(domainFilter, sqlWhereClause, StringUtil.getStringValue(domainFilter.flTipoRelacaoList[i]));
				if (i > 0) {
					sqlWhereClause.addEndMultipleCondition();
				}
			}
			sqlWhereClause.addEndMultipleCondition();
		}
	}
	
	protected void addCustomConditionByFlTipoRelacao(ProdutoTipoRelacaoBase produtoTipoRelacao, SqlWhereClause sqlWhereClause, String flTipoRelacao) {
		switch (flTipoRelacao) {
		case ProdutoTipoRelacaoBase.RELACAOEXCLUSIVA:
			sqlWhereClause.addAndConditionNotEquals(produtoTipoRelacao.getCdDomainEntidadeNomeColuna(), produtoTipoRelacao.getCdDomainEntidade());
			
			if (!produtoTipoRelacao.validandoCount) {
				StringBuffer sb = getSqlBuffer();
				sb.append("NOT EXISTS (SELECT 1 FROM ").append(tableName).append(" WHERE")
				.append(" CDEMPRESA = TB.CDEMPRESA AND ")
				.append(" CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
				.append(produtoTipoRelacao.getCdDomainEntidadeNomeColuna()).append(" = ").append(Sql.getValue(produtoTipoRelacao.getCdDomainEntidade())).append(" AND ")
				.append(" CDPRODUTO = TB.CDPRODUTO AND ")
				.append(" FLTIPORELACAO = TB.FLTIPORELACAO)");
				sqlWhereClause.addAndCondition(sb.toString());
			}
			break;
		default:
			sqlWhereClause.addAndConditionEquals(produtoTipoRelacao.getCdDomainEntidadeNomeColuna(), produtoTipoRelacao.getCdDomainEntidade());
			break;
		}
	}
	
	public void addCTEsProdutoTipoRelacao(StringBuffer sql, ProdutoTipoRelacaoBase domainFilter) {
		sql.append(getAliasCteFlTipoRelacao()).append(" AS (");
		addSelectCteProdutoTipoRelacao(sql, domainFilter, true);
		sql.append("), ");
		
		sql.append(getAliasCTEDomain()).append(" AS (");
		addSelectCteProdutoTipoRelacao(sql, domainFilter, false);
		sql.append(")");
	}
	
    protected void addSelectCteProdutoTipoRelacao(StringBuffer sql, ProdutoTipoRelacaoBase domainFilter, boolean filterOnlyFlTipoRelacao) {
    	if (filterOnlyFlTipoRelacao) {
			sql.append("SELECT FLTIPORELACAO AS FLTIPORELACAOFILTRO FROM ");
		} else {
			sql.append("SELECT CDEMPRESA, CDREPRESENTANTE, CDPRODUTO, FLTIPORELACAO FROM ");
		}
		sql.append(this.tableName).append(" tb ");
		if (!filterOnlyFlTipoRelacao) {
			sql.append(" JOIN ").append(getAliasCteFlTipoRelacao()).append(" ON FLTIPORELACAOFILTRO = TB.FLTIPORELACAO ");
		}
		try {
			addWhereByExample(domainFilter, sql);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		if (filterOnlyFlTipoRelacao) {
			sql.append(" GROUP BY FLTIPORELACAO");
			sql.append(" ORDER BY CASE FLTIPORELACAO ")
				.append("WHEN ").append(Sql.getValue(ProdutoTipoRelacaoBase.RELACAOEXCLUSIVA)).append(" THEN 1 ")
				.append("WHEN ").append(Sql.getValue(ProdutoTipoRelacaoBase.RELACAOEXCECAO)).append(" THEN 2 ")
				.append("ELSE 3 END LIMIT 1");
		}
    }
    
    public void addJoinCTEProdutoClienteBaseDomain(StringBuffer sql, ProdutoTipoRelacaoBase domainFilter) {
    	String aliasCTEDomain = getAliasCTEDomain();
    	sql.append(" LEFT JOIN ").append(aliasCTEDomain).append(" ON ")
			.append("TB.CDEMPRESA = ").append(aliasCTEDomain).append(".CDEMPRESA ")
			.append(" AND ").append(aliasCTEDomain).append(".CDREPRESENTANTE = ").append(Sql.getValue(domainFilter.cdRepresentante))
			.append(" AND TB.CDPRODUTO = ").append(aliasCTEDomain).append(".CDPRODUTO ");
	}
    
    public String getFlTipoRelacaoCteCondition(boolean onlyValue) {
		StringBuffer sb = new StringBuffer();
		if (onlyValue) {
			sb.append(getProdClienteCaseWhenCondition());
		} else {
			sb.append("(1 = ");
			sb.append(getProdClienteCaseWhenCondition());
			sb.append(")");
		}
		return sb.toString();
	}

	protected String getProdClienteCaseWhenCondition() {
		String aliasCTETipoRelacao = getAliasCteFlTipoRelacao();
		String aliasCTEDomain = getAliasCTEDomain();
		StringBuffer sb = new StringBuffer();
		sb.append("case when (select fltiporelacaofiltro from ").append(aliasCTETipoRelacao)
			.append(") = ").append(Sql.getValue(ProdutoTipoRelacaoBase.RELACAORESTRICAO)).append(" then CASE WHEN ").append(aliasCTEDomain).append(".FLTIPORELACAO is not null THEN 1 ELSE 0 END")
			.append(" ELSE CASE WHEN ").append(aliasCTEDomain).append(".FLTIPORELACAO is null THEN 1 ELSE 0 END end");
		return sb.toString();
	}

}
