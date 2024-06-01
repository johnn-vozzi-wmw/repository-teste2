package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ProdutoUnidadePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoUnidade();
	}

    private static ProdutoUnidadePdbxDao instance;

    public ProdutoUnidadePdbxDao() {
        super(ProdutoUnidade.TABLE_NAME);
    }

    public static ProdutoUnidadePdbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoUnidadePdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoUnidade produtoUnidade = new ProdutoUnidade();
        produtoUnidade.rowKey = rs.getString("rowkey");
        produtoUnidade.cdEmpresa = rs.getString("cdEmpresa");
        produtoUnidade.cdRepresentante = rs.getString("cdRepresentante");
        produtoUnidade.cdUnidade = rs.getString("cdUnidade");
        produtoUnidade.cdProduto = rs.getString("cdProduto");
        produtoUnidade.cdItemGrade1 = rs.getString("cdItemGrade1");
        produtoUnidade.nuConversaoUnidade = ValueUtil.round(rs.getDouble("nuConversaounidade"));
        produtoUnidade.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
        produtoUnidade.flDivideMultiplica = rs.getString("flDividemultiplica");
    	produtoUnidade.nuCodigoBarras = rs.getString("nuCodigoBarras");
    	produtoUnidade.nuMultiploEspecial = rs.getInt("nuMultiploEspecial");
        produtoUnidade.nuCarimbo = rs.getInt("nuCarimbo");
        produtoUnidade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoUnidade.cdUsuario = rs.getString("cdUsuario");
        produtoUnidade.unidade = new Unidade();
        produtoUnidade.unidade.cdEmpresa = produtoUnidade.cdEmpresa;
        produtoUnidade.unidade.cdRepresentante = produtoUnidade.cdRepresentante;
        produtoUnidade.unidade.cdUnidade = produtoUnidade.cdUnidade;
        produtoUnidade.unidade.dsUnidade = rs.getString("dsUnidade");
        produtoUnidade.unidade.dsUnidadePlural = rs.getString("dsUnidadePlural");
        if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
        	produtoUnidade.nuFracao = rs.getInt("nuFracao");
        	produtoUnidade.unidadeFracao = new Unidade();
        	produtoUnidade.unidadeFracao.dsUnidade = rs.getString("dsUnidadeFracao");
        	produtoUnidade.unidadeFracao.dsUnidadePlural = rs.getString("dsUnidadePlural");
        }        	
        return produtoUnidade;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDUNIDADE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.CDITEMGRADE1,");
        sql.append(" tb.NUCONVERSAOUNIDADE,");
        sql.append(" tb.VLINDICEFINANCEIRO,");
        sql.append(" tb.FLDIVIDEMULTIPLICA,");
    	sql.append(" tb.NUCODIGOBARRAS,");
    	sql.append(" tb.NUMULTIPLOESPECIAL,");
        sql.append(" tb.nuCarimbo,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" un.DSUNIDADE,");
        sql.append(" un.DSUNIDADEPLURAL");
        if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
        	sql.append(", CASE prod.FLUSAUNIDADEBASEDSFRACAO ")
        	.append(" WHEN 'S' THEN 1 ELSE prod.NUFRACAO END AS NUFRACAO,")
        	.append(" un.DSUNIDADE AS DSUNIDADEFRACAO");
        }
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoUnidade produtoUnidade = (ProdutoUnidade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", produtoUnidade.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", produtoUnidade.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDUNIDADE = ", produtoUnidade.cdUnidade);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", produtoUnidade.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE1 = ", produtoUnidade.cdItemGrade1);
		sqlWhereClause.addAndCondition("tb.NUCODIGOBARRAS = ", produtoUnidade.nuCodigoBarras);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
	public Vector getQtEmbalagensByExample(BaseDomain domain) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		addSelectColumns(domain, sql);
		sql.append(" from ").append(tableName);
		sql.append(" tb ");
		addJoin(domain, sql);
		addWhereByExample(domain, sql);
		sql.append(" order by NUCONVERSAOUNIDADE desc");
		return findAll(domain, sql.toString());
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			sql.append(" LEFT JOIN TBLVPPRODUTO prod ON")
			.append(" prod.CDEMPRESA = tb.CDEMPRESA AND")
			.append(" prod.CDREPRESENTANTE = tb.CDREPRESENTANTE AND")
			.append(" prod.CDPRODUTO = tb.CDPRODUTO");
		}	
		sql.append(" LEFT JOIN TBLVPUNIDADE un ON")
			.append(" un.CDEMPRESA = tb.CDEMPRESA AND")
			.append(" un.CDREPRESENTANTE = tb.CDREPRESENTANTE AND");
		if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			sql.append(" un.CDUNIDADE = (")
			.append("CASE WHEN prod.FLUSAUNIDADEBASEDSFRACAO = 'S' THEN prod.CDUNIDADE")
			.append(" ELSE prod.CDUNIDADEFRACAO END)");
		} else {
			sql.append(" un.CDUNIDADE = tb.CDUNIDADE ");
		}
	}
	
	public ProdutoUnidade getFirstProdutoUnidadeByExample(ProdutoUnidade filter) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		addSelectColumns(filter, sql);
		sql.append(" from ").append(tableName);
		sql.append(" tb ");
		addJoin(filter, sql);
		addWhereByExample(filter, sql);
		sql.append(" LIMIT 1");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return (ProdutoUnidade) populate(filter, rs);
			}
		}
		return null;
	}
    
}