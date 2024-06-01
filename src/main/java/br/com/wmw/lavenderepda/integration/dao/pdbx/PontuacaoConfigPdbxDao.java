package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PontuacaoConfig;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaDias;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaPreco;
import br.com.wmw.lavenderepda.business.domain.PontuacaoProduto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;

public class PontuacaoConfigPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PontuacaoConfig();
	}

    private static PontuacaoConfigPdbxDao instance;

    public PontuacaoConfigPdbxDao() {
        super(PontuacaoConfig.TABLE_NAME);
    }

    public static PontuacaoConfigPdbxDao getInstance() {
        return instance == null ? instance = new PontuacaoConfigPdbxDao() : instance;
    }
    
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}

    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPONTUACAOCONFIG,");
        sql.append(" DSPONTUACAOCONFIG,");
		sql.append(" CDCANAL,");
		sql.append(" CDSEGMENTO,");
        sql.append(" DTINICIOVIGENCIA,");
        sql.append(" DTFIMVIGENCIA");
    }

    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
        pontuacaoConfig.rowKey = rs.getString("rowkey");
        pontuacaoConfig.cdEmpresa = rs.getString("cdEmpresa");
        pontuacaoConfig.cdRepresentante = rs.getString("cdRepresentante");
        pontuacaoConfig.cdPontuacaoConfig = rs.getString("cdPontuacaoConfig");
        pontuacaoConfig.dsPontuacaoConfig = rs.getString("dsPontuacaoConfig");
        pontuacaoConfig.cdCanal = rs.getString("cdCanal");
        pontuacaoConfig.cdSegmento = rs.getString("cdSegmento");
        pontuacaoConfig.dtInicioVigencia = rs.getDate("dtInicioVigencia");
        pontuacaoConfig.dtFimVigencia = rs.getDate("dtFimVigencia");
        return pontuacaoConfig;
    }
    
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        PontuacaoConfig pontuacaoConfigFilter = (PontuacaoConfig) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDEMPRESA = ", pontuacaoConfigFilter.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pontuacaoConfigFilter.cdRepresentante);
        sqlWhereClause.addAndCondition("CDPONTUACAOCONFIG = ", pontuacaoConfigFilter.cdPontuacaoConfig);
        sqlWhereClause.addAndCondition("CDCANAL = ", pontuacaoConfigFilter.cdCanal);
        sqlWhereClause.addAndCondition("CDSEGMENTO = ", pontuacaoConfigFilter.cdSegmento);
        sqlWhereClause.addAndCondition("DSCIDADECOMERCIAL = ", pontuacaoConfigFilter.dsCidadeComercial);
        sqlWhereClause.addAndCondition("DTINICIOVIGENCIA <= ", pontuacaoConfigFilter.dtInicioVigencia);
        sqlWhereClause.addAndCondition("DTFIMVIGENCIA >= ", pontuacaoConfigFilter.dtFimVigencia);
        sql.append(sqlWhereClause.getSql());
    }

	public PontuacaoConfig findPontuacaoConfigItemPedido(PontuacaoConfig pontuacaoConfigFilter) throws SQLException {
		if (pontuacaoConfigFilter == null) return null;
		StringBuffer sql = getSqlPontuacao(pontuacaoConfigFilter);
        addWherePontuacao(pontuacaoConfigFilter, sql);
        if (pontuacaoConfigFilter.dsCidadeComercial != null) {
        	sql.append(" ORDER BY DSCIDADECOMERCIAL DESC, CDCANAL DESC, CDSEGMENTO DESC LIMIT 1 ");
        }
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
	        if (rs.next()) {
	            PontuacaoConfig pontuacaoConfig = populatePontuacaoConfig(pontuacaoConfigFilter, rs);
	            populatePontuacaoProduto(pontuacaoConfigFilter, rs, pontuacaoConfig);
	            populatePontuacaoFaixaDiaCondicaoPagto(pontuacaoConfigFilter, rs, pontuacaoConfig);
	            populatePontuacaoFaixaPreco(pontuacaoConfigFilter, rs, pontuacaoConfig);
	            return pontuacaoConfig;
	        }
        } catch (Throwable e) {
        	ExceptionUtil.handle(e);
        }
        return null;
	}

	private void addWherePontuacao(PontuacaoConfig pontuacaoConfigFilter, StringBuffer sql) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", pontuacaoConfigFilter.cdEmpresa);
        sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", pontuacaoConfigFilter.cdRepresentante);
        sqlWhereClause.addAndCondition("TB.CDPONTUACAOCONFIG = ", pontuacaoConfigFilter.cdPontuacaoConfig);
        sql.append(sqlWhereClause.getSql());
        addPriorityCondition(sql, pontuacaoConfigFilter.dsCidadeComercial, "DSCIDADECOMERCIAL");
        addPriorityCondition(sql, pontuacaoConfigFilter.cdCanal, "CDCANAL");
        addPriorityCondition(sql, pontuacaoConfigFilter.cdSegmento, "CDSEGMENTO");
        addVigenciaWhere(sql, "TB", pontuacaoConfigFilter.dtInicioVigencia, pontuacaoConfigFilter.dtFimVigencia);
	}
	
	private void addPriorityCondition(StringBuffer sql, String valorColuna, String nmColuna) {
		if (ValueUtil.isEmpty(valorColuna)) return;
		sql.append("AND (TB.").append(nmColuna).append(" = ").append(Sql.getValue(valorColuna)).append(" or TB.").append(nmColuna).append(" IS NULL OR TB.").append(nmColuna).append(" = '')");
	}

	private void addVigenciaWhere(StringBuffer sql, String alias, Date dtInicial, Date dtFinal) {
		sql.append(" AND ((").append(alias).append(".DTINICIOVIGENCIA <= ").append(Sql.getValue(dtInicial)).append(" OR ").append(alias).append(".DTINICIOVIGENCIA IS NULL OR ").append(alias).append(".DTINICIOVIGENCIA = '') AND (").append(alias).append(".DTFIMVIGENCIA >= ")
        .append(Sql.getValue(dtFinal)).append(" OR ").append(alias).append(".DTFIMVIGENCIA IS NULL OR ").append(alias).append(".DTFIMVIGENCIA = ''))");
	}

	private StringBuffer getSqlPontuacao(PontuacaoConfig pontuacaoConfigFilter) {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT TB.ROWKEY, TB.CDPONTUACAOCONFIG, TB.CDCANAL, TB.CDSEGMENTO, TB.DSPONTUACAOCONFIG, TB.DTINICIOVIGENCIA, TB.DTFIMVIGENCIA,")
		   .append(" PONTUACAOFAIXADIAS.ROWKEY AS ROWKEYFAIXADIASCOND, PONTUACAOFAIXADIAS.QTDIASMEDIOS AS QTDIASMEDIOSFAIXADIASCOND, PONTUACAOFAIXADIAS.VLTOTALPEDIDO, COALESCE(PONTUACAOFAIXADIAS.VLFATORCORRECAO, 1) VLFATORCORRECAOFAIXADIASCOND, ")
		   .append(" PONTUACAOFAIXAPRECO.ROWKEY AS ROWKEYFAIXAPRECO, PONTUACAOFAIXAPRECO.VLPCTFAIXA AS VLPCTFAIXAFAIXAPRECO, COALESCE(PONTUACAOFAIXAPRECO.VLFATORCORRECAO, 1) VLFATORCORRECAOFAIXAPRECO,")
		   .append(" PONTUACAOPRODUTO.VLPESOPONTUACAO, PONTUACAOPRODUTO.ROWKEY AS ROWKEYPONTUACAOPRODUTO")
		   .append(" FROM ").append(tableName).append(" TB ")
		   .append(" JOIN TBLVPPONTUACAOPRODUTO PONTUACAOPRODUTO ON ")
		   .append(" PONTUACAOPRODUTO.CDEMPRESA = TB.CDEMPRESA ")
		   .append(" AND PONTUACAOPRODUTO.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
		   .append(" AND PONTUACAOPRODUTO.CDPONTUACAOCONFIG = TB.CDPONTUACAOCONFIG ")
		   .append(" AND PONTUACAOPRODUTO.CDPRODUTO = ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoProduto.cdProduto))
		   .append(" LEFT JOIN ( ")
		   .append(" SELECT RN.* FROM ( ")
		   .append(" SELECT TA.rowkey, TA.CDEMPRESA, TA.CDREPRESENTANTE, TA.CDPONTUACAOCONFIG, TA.FLTIPOFATORCORRECAO, TA.QTDIASMEDIOS, TA.VLFATORCORRECAO, TA.VLTOTALPEDIDO ")
		   .append(" FROM TBLVPPONTUACAOFAIXADIAS TA ");
		addJoinPontuacaoConfig(sql, pontuacaoConfigFilter);
		sql.append(" WHERE TA.CDEMPRESA = ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoFaixaDiaCondicaoPagto.cdEmpresa))
		   .append(" AND TA.CDREPRESENTANTE = ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoFaixaDiaCondicaoPagto.cdRepresentante))
		   .append(" AND TA.FLTIPOFATORCORRECAO = ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoFaixaDiaCondicaoPagto.flTipoFatorCorrecao))
		   .append(" AND TA.QTDIASMEDIOS <= ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoFaixaDiaCondicaoPagto.qtDiasMedios));
		if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
			sql.append(" AND TA.VLTOTALPEDIDO <= ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoFaixaDiaCondicaoPagto.vlTotalPedido));
			sql.append(" ORDER BY TA.QTDIASMEDIOS DESC, TA.VLTOTALPEDIDO DESC ");
		} else {
			sql.append(" ORDER BY TA.QTDIASMEDIOS DESC ");
		}
		sql.append(" ) RN LIMIT 1 ")
		   .append(" ) PONTUACAOFAIXADIAS ON  ")
		   .append(" PONTUACAOFAIXADIAS.CDEMPRESA = TB.CDEMPRESA ")
		   .append(" AND PONTUACAOFAIXADIAS.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
		   .append(" AND PONTUACAOFAIXADIAS.CDPONTUACAOCONFIG = TB.CDPONTUACAOCONFIG ")
		   .append(" LEFT JOIN ( ")
		   .append(" SELECT RN.* FROM ( ")
		   .append(" SELECT TA.rowkey, TA.CDEMPRESA, TA.CDREPRESENTANTE, TA.CDPONTUACAOCONFIG, TA.FLTIPOFATORCORRECAO, TA.VLPCTFAIXA, TA.VLFATORCORRECAO ")
		   .append(" FROM TBLVPPONTUACAOFAIXAPRECO TA ");
		addJoinPontuacaoConfig(sql, pontuacaoConfigFilter);
		sql.append(" WHERE TA.CDEMPRESA = ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoFaixaPreco.cdEmpresa))
		   .append(" AND TA.CDREPRESENTANTE = ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoFaixaPreco.cdRepresentante))
		   .append(" AND TA.FLTIPOFATORCORRECAO = ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoFaixaPreco.flTipoFatorCorrecao))
		   .append(" AND TA.VLPCTFAIXA <= ").append(Sql.getValue(pontuacaoConfigFilter.pontuacaoFaixaPreco.vlPctFaixa))
		   .append(" ORDER BY TA.VLPCTFAIXA DESC ")
		   .append(" ) RN LIMIT 1 ")
		   .append(" ) PONTUACAOFAIXAPRECO ON ")
		   .append(" PONTUACAOFAIXAPRECO.CDEMPRESA = TB.CDEMPRESA ")
		   .append(" AND PONTUACAOFAIXAPRECO.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
		   .append(" AND PONTUACAOFAIXAPRECO.CDPONTUACAOCONFIG = TB.CDPONTUACAOCONFIG ");
		return sql;
	}

	private PontuacaoConfig populatePontuacaoConfig(PontuacaoConfig pontuacaoConfigFilter, ResultSet rs) throws SQLException {
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.rowKey = rs.getString("rowkey");
		pontuacaoConfig.cdEmpresa = pontuacaoConfigFilter.cdEmpresa;
		pontuacaoConfig.cdRepresentante = pontuacaoConfigFilter.cdRepresentante;
		pontuacaoConfig.cdPontuacaoConfig = rs.getString("cdPontuacaoConfig");
		pontuacaoConfig.cdCanal = rs.getString("cdCanal");
		pontuacaoConfig.cdSegmento = rs.getString("cdSegmento");
		pontuacaoConfig.dsPontuacaoConfig = rs.getString("dsPontuacaoConfig");
		pontuacaoConfig.dtInicioVigencia = rs.getDate("dtInicioVigencia");
		pontuacaoConfig.dtFimVigencia = rs.getDate("dtFimVigencia");
		return pontuacaoConfig;
	}

	private void populatePontuacaoFaixaPreco(PontuacaoConfig pontuacaoConfigFilter, ResultSet rs, PontuacaoConfig pontuacaoConfig) throws SQLException {
		pontuacaoConfig.pontuacaoFaixaPreco = new PontuacaoFaixaPreco();
		pontuacaoConfig.pontuacaoFaixaPreco.rowKey = rs.getString("ROWKEYFAIXAPRECO");
		pontuacaoConfig.pontuacaoFaixaPreco.cdEmpresa = pontuacaoConfig.cdEmpresa;
		pontuacaoConfig.pontuacaoFaixaPreco.cdRepresentante = pontuacaoConfig.cdRepresentante;
		pontuacaoConfig.pontuacaoFaixaPreco.cdPontuacaoConfig = pontuacaoConfig.cdPontuacaoConfig;
		pontuacaoConfig.pontuacaoFaixaPreco.flTipoFatorCorrecao = pontuacaoConfigFilter.pontuacaoFaixaPreco.flTipoFatorCorrecao;
		pontuacaoConfig.pontuacaoFaixaPreco.vlPctFaixa = rs.getDouble("VLPCTFAIXAFAIXAPRECO");
		pontuacaoConfig.pontuacaoFaixaPreco.vlFatorCorrecao = rs.getDouble("VLFATORCORRECAOFAIXAPRECO");
	}

	private void populatePontuacaoFaixaDiaCondicaoPagto(PontuacaoConfig pontuacaoConfigFilter, ResultSet rs, PontuacaoConfig pontuacaoConfig) throws SQLException {
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto = new PontuacaoFaixaDias();
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.rowKey = rs.getString("ROWKEYFAIXADIASCOND");
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.cdEmpresa = pontuacaoConfig.cdEmpresa;
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.cdRepresentante = pontuacaoConfig.cdRepresentante;
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.cdPontuacaoConfig = pontuacaoConfig.cdPontuacaoConfig;
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.flTipoFatorCorrecao = pontuacaoConfigFilter.pontuacaoFaixaDiaCondicaoPagto.flTipoFatorCorrecao;
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.qtDiasMedios = rs.getInt("QTDIASMEDIOSFAIXADIASCOND");
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.vlTotalPedido = rs.getDouble("VLTOTALPEDIDO");
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.vlFatorCorrecao = rs.getDouble("VLFATORCORRECAOFAIXADIASCOND");
	}

	private void populatePontuacaoProduto(PontuacaoConfig pontuacaoConfigFilter, ResultSet rs, PontuacaoConfig pontuacaoConfig) throws SQLException {
		pontuacaoConfig.pontuacaoProduto = new PontuacaoProduto();
		pontuacaoConfig.pontuacaoProduto.rowKey = rs.getString("ROWKEYPONTUACAOPRODUTO");
		pontuacaoConfig.pontuacaoProduto.cdEmpresa = pontuacaoConfig.cdEmpresa;
		pontuacaoConfig.pontuacaoProduto.cdRepresentante = pontuacaoConfig.cdRepresentante;
		pontuacaoConfig.pontuacaoProduto.cdPontuacaoConfig = pontuacaoConfig.cdPontuacaoConfig;
		pontuacaoConfig.pontuacaoProduto.cdProduto = pontuacaoConfigFilter.pontuacaoProduto.cdProduto;
		pontuacaoConfig.pontuacaoProduto.vlPesoPontuacao = rs.getDouble("VLPESOPONTUACAO");
	}

	private void addJoinPontuacaoConfig(StringBuffer sql, PontuacaoConfig pontuacaoConfigFilter) {
		sql.append(" JOIN TBLVPPONTUACAOCONFIG CONFIG ON ")
		   .append(" CONFIG.CDEMPRESA = TA.CDEMPRESA ")
		   .append(" AND CONFIG.CDREPRESENTANTE = TA.CDREPRESENTANTE ")
		   .append(" AND CONFIG.CDPONTUACAOCONFIG = TA.CDPONTUACAOCONFIG ");
		addVigenciaWhere(sql, "CONFIG", pontuacaoConfigFilter.dtInicioVigencia, pontuacaoConfigFilter.dtFimVigencia);
	}

}