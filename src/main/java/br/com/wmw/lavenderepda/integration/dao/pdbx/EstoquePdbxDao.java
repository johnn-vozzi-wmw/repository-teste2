package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueDisponivel;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoEstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoEstoqueDto;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class EstoquePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Estoque();
	}

    private static EstoquePdbxDao instance;

	private boolean initializingCache;
	private Hashtable cacheSomaEstoque = null;
	
    public EstoquePdbxDao() {
        super(Estoque.TABLE_NAME);
    }

    public static EstoquePdbxDao getInstance() {
        if (instance == null) {
            instance = new EstoquePdbxDao();
        }
        return instance;
    }

    @Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowKey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.CDITEMGRADE1,");
        sql.append(" tb.CDITEMGRADE2,");
        sql.append(" tb.CDITEMGRADE3,");
        sql.append(" tb.CDLOCALESTOQUE,");
		if (LavenderePdaConfig.mostraEstoquePrevisto || LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
			sql.append(" tb.QTESTOQUEPREVISTO,");
			sql.append(" tb.DTESTOQUEPREVISTO,");
		}
		if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
			sql.append(" tb.QTESTOQUEMIN,");
		}
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			sql.append(" tb.QTESTOQUEREMESSA,");
		}
        sql.append(" tb.QTESTOQUE,");
        sql.append(" tb.FLORIGEMESTOQUE");
    }
    
    @Override
    protected void addCacheColumns(StringBuffer sql) throws SQLException {
    	sql.append(" tb.CDEMPRESA,");
    	sql.append(" tb.CDREPRESENTANTE,");
    	sql.append(" tb.CDPRODUTO,");
    	sql.append(" tb.CDITEMGRADE1,");
    	sql.append(" tb.CDITEMGRADE2,");
    	sql.append(" tb.CDITEMGRADE3,");
    	sql.append(" tb.CDLOCALESTOQUE,");
    	if (LavenderePdaConfig.mostraEstoquePrevisto || LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
    		sql.append(" tb.QTESTOQUEPREVISTO,");
    		sql.append(" tb.DTESTOQUEPREVISTO,");
    	}
    	if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
    		sql.append(" tb.QTESTOQUEMIN,");
    	}
    	sql.append(" tb.QTESTOQUE,");
    	sql.append(" tb.FLORIGEMESTOQUE");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Estoque estoque = new Estoque();
        estoque.rowKey = rs.getString("rowkey");
        estoque.cdEmpresa = rs.getString("cdEmpresa");
        estoque.cdRepresentante = rs.getString("cdRepresentante");
        estoque.cdProduto = rs.getString("cdProduto");
        estoque.cdItemGrade1 = rs.getString("cdItemGrade1");
        estoque.cdItemGrade2 = rs.getString("cdItemGrade2");
        estoque.cdItemGrade3 = rs.getString("cdItemGrade3");
        estoque.cdLocalEstoque = rs.getString("cdLocalEstoque");
        estoque.qtEstoque = ValueUtil.round(rs.getDouble("qtEstoque"));
		if (LavenderePdaConfig.mostraEstoquePrevisto || LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
			estoque.qtEstoquePrevisto = rs.getDouble("qtEstoquePrevisto");
			estoque.dtEstoquePrevisto = rs.getDate("dtEstoquePrevisto");
		}
		if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
			estoque.qtEstoqueMin = rs.getDouble("qtEstoqueMin");
		}
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			estoque.qtEstoqueRemessa = rs.getDouble("qtEstoqueRemessa");
		}
		estoque.flOrigemEstoque = rs.getString("flOrigemEstoque");
        return estoque;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" QTESTOQUE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" QTESTOQUEREMESSA,");
        sql.append(" FLORIGEMESTOQUE");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Estoque estoque = (Estoque) domain;
        sql.append(Sql.getValue(estoque.cdEmpresa)).append(",");
        sql.append(Sql.getValue(estoque.cdRepresentante)).append(",");
        sql.append(Sql.getValue(estoque.cdProduto)).append(",");
        sql.append(Sql.getValue(estoque.cdItemGrade1)).append(",");
        sql.append(Sql.getValue(estoque.cdItemGrade2)).append(",");
        sql.append(Sql.getValue(estoque.cdItemGrade3)).append(",");
        sql.append(Sql.getValue(estoque.cdLocalEstoque)).append(",");
        sql.append(Sql.getValue(estoque.qtEstoque)).append(",");
        sql.append(Sql.getValue(estoque.nuCarimbo)).append(",");
        sql.append(Sql.getValue(estoque.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(estoque.cdUsuario)).append(",");
        sql.append(Sql.getValue(estoque.qtEstoqueRemessa)).append(",");
        sql.append(Sql.getValue(estoque.flOrigemEstoque));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Estoque estoque = (Estoque) domain;
        sql.append(" QTESTOQUE = ").append(Sql.getValue(estoque.qtEstoque)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(estoque.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(estoque.flTipoAlteracao));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Estoque estoque = (Estoque) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", estoque.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", estoque.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", estoque.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE1 = ", estoque.cdItemGrade1);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE2 = ", estoque.cdItemGrade2);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE3 = ", estoque.cdItemGrade3);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE <> ", estoque.cdLocalEstoqueDif);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE = ", estoque.cdLocalEstoque);
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", estoque.flOrigemEstoque);
		//--
		sql.append(sqlWhereClause.getSql());
		addInProdutoCondition(sql);
    }
    
    @Override
    protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        Estoque estoque = (Estoque) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", estoque.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", estoque.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", estoque.cdProduto);
		sqlWhereClause.addAndCondition("CDITEMGRADE1 = ", estoque.cdItemGrade1);
		sqlWhereClause.addAndCondition("CDITEMGRADE2 = ", estoque.cdItemGrade2);
		sqlWhereClause.addAndCondition("CDITEMGRADE3 = ", estoque.cdItemGrade3);
		sqlWhereClause.addAndCondition("CDLOCALESTOQUE = ", estoque.cdLocalEstoque);
		sqlWhereClause.addAndCondition("FLORIGEMESTOQUE = ", estoque.flOrigemEstoque);
		//--
		sql.append(sqlWhereClause.getSql());
		addInProdutoCondition(sql);
    }
    
    public int getMaxNuCarimbo() throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.setLength(0);
    	sql.append("select max(");
    	sql.append(BaseDomain.NMCAMPONUCARIMBO);
    	sql.append(") as maxCarimbo from ");
    	sql.append(Estoque.TABLE_NAME);
    	return getInt(sql.toString());
//    	ResultSet rsTemp = executeQuery(sql.toString());
//    	int maxCarimbo = 0;
//    	try {
//    		if (rsTemp.next()) {
//    			maxCarimbo = rsTemp.getInt("maxCarimbo");
//    		}
//    	} finally {
//    		rsTemp.close();
//    	}
//    	return maxCarimbo;
    }
    
    public Vector findEstoqueByCdLocal(Estoque estoqueFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("select ");
    	addSelectColumns(estoqueFilter, sql);
    	sql.append(", prod.dsProduto, ");
    	sql.append("(tb.QTESTOQUE - COALESCE (ESTOQUECONSUMIDO.QTESTOQUE,0)) QTESTOQUEATUAL from ");
    	sql.append(Estoque.TABLE_NAME).append(" tb ");
    	sql.append(" left outer join tblvpproduto prod on ");
    	sql.append(" prod.cdEmpresa = tb.cdEmpresa");
    	sql.append(" and prod.cdRepresentante = tb.cdRepresentante");
    	sql.append(" and prod.cdProduto = tb.cdProduto");
    	sql.append(" LEFT OUTER JOIN TBLVPESTOQUE ESTOQUECONSUMIDO ON ");
    	sql.append(" ESTOQUECONSUMIDO.CDEMPRESA = tb.CDEMPRESA ");
    	sql.append(" AND ESTOQUECONSUMIDO.CDREPRESENTANTE = tb.CDREPRESENTANTE ");
    	sql.append(" AND ESTOQUECONSUMIDO.CDLOCALESTOQUE = tb.CDLOCALESTOQUE ");
    	sql.append(" AND ESTOQUECONSUMIDO.CDPRODUTO = tb.CDPRODUTO ");
    	sql.append(" AND ESTOQUECONSUMIDO.FLORIGEMESTOQUE = 'P'");
    	addWhereByExample(estoqueFilter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector list = new Vector();
			while (rs.next()) {
				Estoque estoque = (Estoque) populate(estoqueFilter, rs);
				estoque.getProduto().dsProduto = rs.getString("dsProduto");
				estoque.getProduto().cdProduto = estoque.cdProduto;
				estoque.qtEstoque = rs.getDouble("QTESTOQUEATUAL");
				list.addElement(estoque);
			}
			return list;
		}
    }
    
    public double qtdRemessaSemEstoqueParaDevolver(Estoque estoqueFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("select SUM(QTESTOQUE) AS QTESTOQUE from ");
    	sql.append(Estoque.TABLE_NAME).append(" tb ");
    	sql.append(" left outer join tblvpproduto prod on ");
    	sql.append(" prod.cdEmpresa = tb.cdEmpresa");
    	sql.append(" and prod.cdRepresentante = tb.cdRepresentante");
    	sql.append(" and prod.cdProduto = tb.cdProduto");
    	addWhereByExample(estoqueFilter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			double qtdRemessaSemEstoqueParaDevolver = 0;
			while (rs.next()) {
				qtdRemessaSemEstoqueParaDevolver += rs.getDouble("QTESTOQUE");
			}
			return qtdRemessaSemEstoqueParaDevolver;
		}
    }
    
    public double getSumQtEstoqueGradeNivel1PorLocalPorTabPreco(String cdProduto, String cdLocalEstoque, Vector itemTabPrecoList) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("select SUM(QTESTOQUE) AS QTESTOQUE from ").append(Estoque.TABLE_NAME)
    		.append(" where CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa))
    		.append(" and CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getRepresentante().cdRepresentante))
    		.append(" and CDPRODUTO = ").append(Sql.getValue(cdProduto))
    		.append(" and CDLOCALESTOQUE = ").append(Sql.getValue(cdLocalEstoque))
    		.append(" and FLORIGEMESTOQUE = ").append(Sql.getValue(Estoque.FLORIGEMESTOQUE_ERP))
    		.append(" and (");
    	int size = itemTabPrecoList.size();
    	for (int i = 0; i < size; i++) {
			ItemTabelaPreco itemtabelaPreco = (ItemTabelaPreco) itemTabPrecoList.items[i];
    		sql.append(" CDITEMGRADE1 = ").append(Sql.getValue(itemtabelaPreco.cdItemGrade1)).append(" OR");
    	}
    	sql.delete(sql.length() - 3, sql.length());
    	sql.append(")");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			double sumQtEstoque = 0;
			while (rs.next()) {
				sumQtEstoque += rs.getDouble("QTESTOQUE");
			}
			return sumQtEstoque;
		}
    }
    
    public double getSumQtEstoqueSaidaPor(String cdEmpresa, String cdRepresentante, ItemPedido itemPedido, String cdLocalEstoqueEmpresa, String flOrigemEstoque) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("select SUM(QTESTOQUE) AS QTESTOQUE from ").append(Estoque.TABLE_NAME);
    	sql.append(" JOIN TBLVPREMESSAESTOQUE re ");
		sql.append(" ON re.CDEMPRESA = tblvpestoque.CDEMPRESA");
		sql.append(" AND re.CDREPRESENTANTE = tblvpestoque.CDREPRESENTANTE");
		sql.append(" AND re.CDLOCALESTOQUE = tblvpestoque.CDLOCALESTOQUE");
		sql.append(" AND re.FLESTOQUELIBERADO = 'S' ");
    	sql.append(" where re.CDEMPRESA = ").append(Sql.getValue(cdEmpresa))
    		.append(" and re.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante))
    		.append(" and tblvpestoque.CDPRODUTO = ").append(Sql.getValue(itemPedido.cdProduto))
    		.append(" and tblvpestoque.FLORIGEMESTOQUE = ").append(Sql.getValue(flOrigemEstoque));
    	if (LavenderePdaConfig.usaModoControleEstoquePorTipoPedido && itemPedido.pedido != null && !itemPedido.pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
    		sql.append(" and (tblvpestoque.CDLOCALESTOQUE is null OR tblvpestoque.CDLOCALESTOQUE <> ").append(Sql.getValue(cdLocalEstoqueEmpresa)).append(")");
    	} else {
			sql.append(" and (tblvpestoque.CDLOCALESTOQUE is null OR tblvpestoque.CDLOCALESTOQUE == ").append(Sql.getValue(cdLocalEstoqueEmpresa)).append(")");
		}
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			double sumQtEstoque = 0;
			while (rs.next()) {
				sumQtEstoque += rs.getDouble("QTESTOQUE");
			}
			return sumQtEstoque;
		}
    }

	private void addInProdutoCondition(StringBuffer sql) {
		if (initializingCache) {
			sql.append(" and CDPRODUTO in (select CDPRODUTO from ").append(LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() ? ProdutoTabPreco.TABLE_NAME : Produto.TABLE_NAME).append(") ");
		}
	}
    
	@Override
	public void initCacheByExample(BaseDomain domainFilter) throws SQLException {
		initializingCache = true;
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			iniciaCacheEstoqueRemessa(domainFilter);
		} else {
			super.initCacheByExample(domainFilter);
		}
		initializingCache = false;
	}

	private void iniciaCacheEstoqueRemessa(BaseDomain domainFilter) throws SQLException {
		StringBuffer sb = new StringBuffer(64);
		cacheSomaEstoque = new Hashtable(256);
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(getSqlEstoqueRemessa(domainFilter))) {
			String sufixo = LavenderePdaConfig.apresentaEstoqueDaRemessaEmpresaNaListaProdutos ? ";R" : "";
			populateCacheRemessa(rs, sb, sufixo);
		}
		if (LavenderePdaConfig.apresentaEstoqueDaRemessaEmpresaNaListaProdutos) {
			String sufixo = ";E";
			Estoque estoque = (Estoque) domainFilter;
			estoque.estoqueEmpresa = true;
			try (Statement st = getCurrentDriver().getStatement();
	    			ResultSet rs = st.executeQuery(getSqlEstoqueRemessa(estoque))) {
				populateCacheRemessa(rs, sb, sufixo);
			}
		}
	}
	
	public void populateCacheRemessa(ResultSet rs, StringBuffer sb, String sufixo) throws SQLException {
		while (rs.next()) {
			BaseDomain domain = populaCacheEstoqueRemessa(rs);
			sb.setLength(0);
			sb.append(rs.getString(1)).append(";").append(rs.getString(2)).append(";").append(rs.getString(3)).append(sufixo);
			cacheSomaEstoque.put(sb.toString(), domain);
		}
	}
	
	private Estoque populaCacheEstoqueRemessa(ResultSet rs) throws SQLException {
		Estoque estoque = new Estoque();
	    estoque.cdEmpresa = rs.getString(1);
	    estoque.cdRepresentante = rs.getString(2);
	    estoque.cdProduto = rs.getString(3);
	    estoque.qtEstoque = rs.getDouble(4);
	    return estoque;
	}
	
	@Override
	public BaseDomain findByRowKeyInCache(BaseDomain domain) throws SQLException {
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			Estoque estoque = (Estoque) domain;
			boolean mostraEstEmpresa = LavenderePdaConfig.apresentaEstoqueDaRemessaEmpresaNaListaProdutos;
			String tipoRemessa = !estoque.estoqueEmpresa ? PedidoEstoqueDto.TIPOREMESSA_R : PedidoEstoqueDto.TIPOREMESSA_EMPRESA;
			return (BaseDomain) getCacheSomaEstoque(estoque).get(estoque.cdEmpresa + ";" + estoque.cdRepresentante + ";" + estoque.cdProduto + (mostraEstEmpresa ? ";" + tipoRemessa : ""));
		}
		return super.findByRowKeyInCache(domain);
	}

	private Hashtable getCacheSomaEstoque(Estoque estoque) throws SQLException {
		if (cacheSomaEstoque == null || cacheSomaEstoque.size() <= 0) {
			Estoque estoqueFilter = new Estoque();
			estoqueFilter.cdEmpresa = estoque.cdEmpresa;
			estoqueFilter.cdRepresentante = estoque.cdRepresentante;
			estoqueFilter.flOrigemEstoque = estoque.flOrigemEstoque;
			estoqueFilter.flModoEstoque = estoque.flModoEstoque;
			initCacheByExample(estoqueFilter);
		}
		return cacheSomaEstoque;
	}
	
	private String getSqlEstoqueRemessa(BaseDomain domainFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		Estoque estoqueFilter = (Estoque) domainFilter;
		if (estoqueFilter.estoqueEmpresa) {
			getSqlRemessasEmpresa(domainFilter, sql);
		} else {
			if ("2".equals(estoqueFilter.flModoEstoque)) {
				getSqlEstoquePorLocal(domainFilter, sql);
			} else {
				getSqlRemessaEstoque(domainFilter, sql);
			}
		}
		return sql.toString();
	}

	private void getSqlEstoquePorLocal(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" SELECT tb.cdempresa, tb.cdrepresentante, tb.cdproduto, sum(tb.qtestoque)");
		sql.append(" FROM TBLVPESTOQUE tb ");
		sql.append(" INNER JOIN TBLVPEMPRESA em ON (tb.cdempresa = em.cdempresa AND em.cdlocalestoque IS NOT NULL AND tb.cdlocalestoque = em.cdlocalestoque) ");
		addWhereByExample(domainFilter, sql);
		sql.append(" GROUP BY tb.cdempresa, tb.cdrepresentante, tb.cdproduto ");
	}
	
	private void getSqlRemessaEstoque(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" SELECT tb.cdempresa, tb.cdrepresentante, tb.cdproduto, sum(tb.qtestoque)");
		sql.append(" FROM TBLVPESTOQUE tb ");
		addWhereByExample(domainFilter, sql);
		sql.append(" AND EXISTS(Select 1 from TBLVPREMESSAESTOQUE re WHERE re.cdempresa = tb.cdempresa and re.cdrepresentante = tb.cdrepresentante and re.cdlocalestoque = tb.cdlocalestoque and re.flestoqueliberado = 'S' limit 1)");
		sql.append(" GROUP BY tb.cdempresa, tb.cdrepresentante, tb.cdproduto ");
	}

	private void getSqlLocaisEstoque(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" SELECT cdlocalestoque ");
		sql.append(" FROM TBLVPESTOQUE tb ");
		addWhereByExample(domainFilter, sql);
		sql.append(" AND EXISTS(Select 1 from TBLVPREMESSAESTOQUE re WHERE re.cdempresa = tb.cdempresa and re.cdrepresentante = tb.cdrepresentante and re.cdlocalestoque = tb.cdlocalestoque and re.flestoqueliberado = 'S' limit 1)");
		sql.append(" GROUP BY tb.cdlocalestoque ");
	}
	
	private void getSqlRemessasEmpresa(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" SELECT tb.cdempresa, tb.cdrepresentante, tb.cdproduto, sum(tb.qtestoque)")
		.append(" FROM TBLVPESTOQUE tb ")
		.append("JOIN TBLVPREMESSAESTOQUE re ON re.cdempresa = tb.cdempresa and re.cdrepresentante = tb.cdrepresentante and re.cdlocalestoque = tb.cdlocalestoque and (re.flestoqueliberado <> 'S' or re.flestoqueliberado is null)");
		addWhereByExample(domainFilter, sql);
		sql.append(" GROUP BY tb.cdempresa, tb.cdrepresentante, tb.cdproduto ");
	}
	
	public List<String> getListaDeLocaisEstoque(BaseDomain domainFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		getSqlLocaisEstoque(domainFilter, sql);
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			List<String> list = new ArrayList<>();
			String local;
			while (rs.next()) {
				local = rs.getString("cdlocalestoque");
				list.add(local);
			}
			return list;
		}
	}
	
	public List<Object[]> buscaSomaEstoqueLocalPorProduto(BaseDomain domainFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT tb.cdlocalestoque as localestoque, sum(CASE WHEN tb.florigemestoque = 'P' THEN tb.qtestoque * -1 ELSE tb.qtestoque END) as totalEstoque");
		sql.append(" FROM TBLVPESTOQUE tb ");
		addWhereByExample(domainFilter, sql);
		sql.append(" GROUP BY tb.cdlocalestoque ");
		sql.append(" ORDER BY tb.cdlocalestoque ");
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			List<Object[]> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Object[] {rs.getString("localestoque"), rs.getString("totalEstoque")});
			}
			return list;
		}
	}
	
	@Override
	public void clearCache() {
		super.clearCache();
		cacheSomaEstoque = null;
	}
	
	public Vector findAllEstoqueRemessaLiberada(ItemPedido itemPedido) throws SQLException {
		Estoque estoqueFilter = new Estoque();
		estoqueFilter.cdEmpresa = itemPedido.cdEmpresa;
		estoqueFilter.cdRepresentante = itemPedido.cdRepresentante;
		estoqueFilter.cdProduto = itemPedido.cdProduto;
		estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
		StringBuffer sql = getSqlBuffer();
    	sql.append("select ");
    	addSelectColumns(estoqueFilter, sql);
    	sql.append(", nuNotaRemessa, nuSerieRemessa ");
    	sql.append(" from ");
    	sql.append(Estoque.TABLE_NAME).append(" tb ");
    	sql.append(" join tblvpremessaestoque rem on ");
    	sql.append(" rem.cdEmpresa = tb.cdEmpresa");
    	sql.append(" and rem.cdRepresentante = tb.cdRepresentante");
    	sql.append(" and rem.cdLocalEstoque = tb.cdLocalEstoque");
    	addWhereByExample(estoqueFilter, sql);
    	sql.append(" and flEstoqueLiberado = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
    	sql.append(" and (flFinalizada <> ").append(Sql.getValue(ValueUtil.VALOR_SIM)).append(" or flFinalizada is null)");
    	sql.append(" order by dtRemessa, hrRemessa");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector list = new Vector();
			while (rs.next()) {
				Estoque estoque = (Estoque) populate(estoqueFilter, rs);
				estoque.nuSerieRemessa = rs.getString("nuSerieRemessa");
				estoque.nuNotaRemessa = rs.getString("nuNotaRemessa");
				list.addElement(estoque);
			}
			return list;
		}
	}

	public double getQtdEstoqueRemessaProduto(ItemPedido itemPedido, String flOrigemEstoque) throws SQLException {
		Estoque estoqueFilter = new Estoque();
		estoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		estoqueFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Estoque.class);
		estoqueFilter.cdProduto = itemPedido.cdProduto;
		estoqueFilter.flOrigemEstoque = flOrigemEstoque;
		StringBuffer sql = getSqlBuffer();
    	sql.append("select sum(qtEstoque) as qtEstoque from ");;
    	sql.append(Estoque.TABLE_NAME).append(" tb ");
    	sql.append(" INNER JOIN TBLVPEMPRESA EM ON (em.cdempresa = tb.cdempresa)");
		if (itemPedido.pedido != null && itemPedido.pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
			Empresa empresa = new Empresa();
			empresa.cdEmpresa = itemPedido.pedido.cdEmpresa;
			String cdLocalFilter = EmpresaService.getInstance().getLocalEstoqueEmpresa();
			if (ValueUtil.isNotEmpty(cdLocalFilter)) {
				estoqueFilter.cdLocalEstoque = cdLocalFilter;
			}
		}
		if (Estoque.FLORIGEMESTOQUE_ERP.equals(flOrigemEstoque)) {
			addWhereByExample(estoqueFilter, sql);
			sql.append(" AND EXISTS(Select 1 from TBLVPREMESSAESTOQUE re WHERE re.cdempresa = tb.cdempresa and re.cdrepresentante = tb.cdrepresentante and re.cdlocalestoque = tb.cdlocalestoque and re.flestoqueliberado = 'S' and (re.flFinalizada <> 'S' or re.flFinalizada is null) limit 1)");
		} else {
    		addWhereByExample(estoqueFilter, sql);
    	}
		if (LavenderePdaConfig.usaModoControleEstoquePorTipoPedido ) {
			if (itemPedido.pedido == null || !itemPedido.pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
				sql.append(" AND COALESCE(em.cdlocalestoque,0) <> COALESCE(tb.cdlocalestoque,0) ");
			}
		}
    	return getDouble(sql.toString());
	}
	
	public List<Estoque> getEstoqueListDevolucao(boolean parcial) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDPRODUTO, SUM(")
		.append("tb.QTESTOQUE")
		.append(") QTESTOQUE FROM TBLVPESTOQUE tb ")
		.append("JOIN TBLVPREMESSAESTOQUE rem ON ")
		.append("tb.CDEMPRESA = rem.CDEMPRESA AND ")
		.append("tb.CDREPRESENTANTE = rem.CDREPRESENTANTE AND ")
		.append("tb.CDLOCALESTOQUE = rem.CDLOCALESTOQUE ")
		.append("WHERE tb.CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa)).append(" AND ")
		.append("tb.CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(Estoque.class))).append(" AND ");
		if (parcial) {
			sql.append("rem.FLTIPOREMESSA = 'R'");
		} else {
			sql.append("(rem.FLTIPOREMESSA <> 'R' OR rem.FLTIPOREMESSA IS NULL)");
		}
		sql.append(" GROUP BY tb.CDPRODUTO");
		List<Estoque> estoqueList = new ArrayList<>(50);
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				Estoque estoque = new Estoque();
				estoque.cdProduto = rs.getString(1);
				estoque.qtEstoque = rs.getDouble(2);
				estoqueList.add(estoque);
			}
		}
		return estoqueList;
	}
	
	public void updateEstoqueAposDevolucao() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		String cdEmpresa = SessionLavenderePda.cdEmpresa;
		String cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Estoque.class);
		sql.append("UPDATE TBLVPESTOQUE SET QTESTOQUE = QTESTOQUE + IFNULL(")
		.append("(SELECT SUM(QTESTOQUE) FROM TBLVPESTOQUE est ")
		.append("JOIN TBLVPREMESSAESTOQUE rem ON est.CDEMPRESA = rem.CDEMPRESA AND ")
		.append("est.CDREPRESENTANTE = rem.CDREPRESENTANTE AND ")
		.append("est.CDLOCALESTOQUE = rem.CDLOCALESTOQUE AND ")
		.append("est.CDPRODUTO = TBLVPESTOQUE.CDPRODUTO AND ")
		.append("est.CDREPRESENTANTE = TBLVPESTOQUE.CDREPRESENTANTE ")
		.append("WHERE rem.FLTIPOREMESSA = 'R' and est.FLORIGEMESTOQUE = 'E' GROUP BY CDPRODUTO), 0) ")
		.append("WHERE TBLVPESTOQUE.rowid = (")
		.append("SELECT MIN(est2.rowid) from TBLVPESTOQUE est2 WHERE ")
		.append("TBLVPESTOQUE.CDEMPRESA = est2.CDEMPRESA AND ")
		.append("TBLVPESTOQUE.CDREPRESENTANTE = est2.CDREPRESENTANTE AND ")
		.append("TBLVPESTOQUE.CDPRODUTO = est2.CDPRODUTO AND ")
		.append("est2.FLORIGEMESTOQUE = 'E') ")
		.append("AND TBLVPESTOQUE.FLORIGEMESTOQUE = 'E' ")
		.append("AND TBLVPESTOQUE.CDEMPRESA = ").append(Sql.getValue(cdEmpresa))
		.append("AND TBLVPESTOQUE.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante))
		.append(" AND not EXISTS (SELECT 1 FROM TBLVPREMESSAESTOQUE WHERE ")
		.append("CDEMPRESA = TBLVPESTOQUE.CDEMPRESA AND ")
		.append("CDREPRESENTANTE = TBLVPESTOQUE.CDREPRESENTANTE AND ")
		.append("CDLOCALESTOQUE = TBLVPESTOQUE.CDLOCALESTOQUE AND ")
		.append("FLTIPOREMESSA = 'R' )");
		executeUpdate(sql.toString());
	}
	
	public void deleteEstoqueDevolucaoRemessa() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		//--
		String cdEmpresa = Sql.getValue(SessionLavenderePda.cdEmpresa);
		String cdRepresentante = Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(Estoque.class));
		//--
		sql.append("DELETE FROM TBLVPESTOQUE WHERE CDEMPRESA = ").append(cdEmpresa).append(" AND ")
		.append("CDREPRESENTANTE = ").append(cdRepresentante).append(" AND ")
		.append("FLORIGEMESTOQUE = 'E' AND ")
		.append("EXISTS (SELECT 1 FROM TBLVPREMESSAESTOQUE WHERE CDEMPRESA = TBLVPESTOQUE.CDEMPRESA AND ")
		.append("CDREPRESENTANTE = TBLVPESTOQUE.CDREPRESENTANTE AND ")
		.append("CDLOCALESTOQUE = TBLVPESTOQUE.CDLOCALESTOQUE AND FLTIPOREMESSA = 'R')");
		executeUpdate(sql.toString());
	}
	
	public Vector findEstoquesParaAjusteRemessa(ItemPedidoEstoqueDto itemPedidoEstoque) throws SQLException {
		Estoque estoque;
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(getSqlAjusteEstoqueRemessa(itemPedidoEstoque))) {
			Vector list = new Vector(50);
			while (rs.next()) {
				estoque = new Estoque();
				estoque.cdLocalEstoque = rs.getString(1);
				estoque.qtEstoque = rs.getDouble(2);
				list.addElement(estoque);
			}
			return list;
		}
	}
	
	private String getSqlAjusteEstoqueRemessa(ItemPedidoEstoqueDto itemPedidoEstoque) {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDLOCALESTOQUE")
		.append(", tb.QTESTOQUE")
		.append(" FROM TBLVPESTOQUE tb ")
		.append("JOIN TBLVPREMESSAESTOQUE rem ON ")
		.append("tb.CDEMPRESA = rem.CDEMPRESA AND ")
		.append("tb.CDREPRESENTANTE = rem.CDREPRESENTANTE AND ")
		.append("tb.CDLOCALESTOQUE = rem.CDLOCALESTOQUE");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", itemPedidoEstoque.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", itemPedidoEstoque.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", itemPedidoEstoque.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE1 = '0'");
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE2 = '0'");
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE3 = '0'");
		sqlWhereClause.addAndCondition("(rem.FLTIPOREMESSA <> 'R' OR rem.FLTIPOREMESSA IS NULL)");
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", Estoque.FLORIGEMESTOQUE_ERP);
		sql.append(sqlWhereClause.getSql());
		return sql.toString();
	}
	
	public void deletaEstoqueAposDevolucaoTotal(Estoque filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("DELETE FROM TBLVPESTOQUE ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", filter.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENATANTE = ", filter.cdRepresentante);
		executeUpdate(sql.toString());
	}
	
	private String getSqlDadosPrevistoParaEstoque(Estoque estoque) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT QTESTOQUE, QTESTOQUEPREVISTO, DTESTOQUEPREVISTO ");
		sql.append(" FROM TBLVPESTOQUE tb ");
		addWhereByExample(estoque, sql);
		return sql.toString();
	}

	public Estoque findDadosPrevistoParaEstoque(Estoque estoqueFilter) throws SQLException {
		String sql = getSqlDadosPrevistoParaEstoque(estoqueFilter);
    	Estoque estoque = new Estoque();
    	try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql)) {
    		if (rs.next()) {
    			estoque.qtEstoque = rs.getDouble("qtEstoque");
    			estoque.qtEstoquePrevisto = rs.getDouble("qtEstoquePrevisto");
    			estoque.dtEstoquePrevisto = rs.getDate("dtEstoquePrevisto");
    		}
    	}
    	return estoque;
	}
	
	public String getSqlDadosEstoqueCentroCusto(Produto estoqueFilter) {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		sql.append(" tb.rowKey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.CDITEMGRADE1,");
        sql.append(" tb.CDITEMGRADE2,");
        sql.append(" tb.CDITEMGRADE3,");
        sql.append(" tb.CDLOCALESTOQUE,");
		if (LavenderePdaConfig.mostraEstoquePrevisto) {
			sql.append(" tb.QTESTOQUEPREVISTO,");
			sql.append(" tb.DTESTOQUEPREVISTO,");
		}
		if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
			sql.append(" tb.QTESTOQUEMIN,");
		}
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			sql.append(" tb.QTESTOQUEREMESSA,");
		}
        sql.append(" sum(case TB.FLORIGEMESTOQUE when 'P' then QTESTOQUE * -1 else QTESTOQUE end) as QTESTOQUE,");
        sql.append(" tb.FLORIGEMESTOQUE");		
        sql.append(" FROM TBLVPESTOQUE tb ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", estoqueFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", estoqueFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", estoqueFilter.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE = ", estoqueFilter.cdLocalEstoqueFilter);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE1 = '0'");
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE2 = '0'");
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE3 = '0'");
		sql.append(sqlWhereClause.getSql());
		return sql.toString();		
	}
	
	public Estoque findEstoqueCentroCusto(Produto produto) throws SQLException {
		Estoque estoque = new Estoque();
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(getSqlDadosEstoqueCentroCusto(produto))) {
			if (rs.next()) {
				estoque.rowKey = rs.getString("rowKey");
				estoque.cdEmpresa = rs.getString("cdEmpresa");
				estoque.cdRepresentante = rs.getString("cdRepresentante");
				estoque.cdProduto = rs.getString("cdProduto");
				estoque.cdItemGrade1 = rs.getString("cdItemGrade1");
				estoque.cdItemGrade2 = rs.getString("cdItemGrade2");
				estoque.cdItemGrade3 = rs.getString("cdItemGrade3");
				estoque.cdLocalEstoque = rs.getString("cdLocalEstoque");
				if (LavenderePdaConfig.mostraEstoquePrevisto) {
					estoque.qtEstoquePrevisto = rs.getInt("qtEstoquePrevisto");
					estoque.dtEstoquePrevisto = rs.getDate("dtEstoquePrevisto");
				}
				if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
					estoque.qtEstoqueMin = rs.getInt("qtEstoqueMin");
				}
				if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
					estoque.qtEstoqueRemessa = rs.getDouble("qtEstoqueRemessa");
				}
				estoque.qtEstoque = rs.getInt("qtEstoque");
				estoque.flOrigemEstoque = rs.getString("flOrigemEstoque");
			}
			return estoque;
		}
	}
	
	public Estoque findEstoqueAgrupadorGrade(Estoque filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ")
		.append(" tb.rowkey,")
        .append(" tb.CDEMPRESA,")
        .append(" tb.CDREPRESENTANTE,")
        .append(" tb.CDPRODUTO,")
        .append(" tb.CDITEMGRADE1,")
        .append(" tb.CDITEMGRADE2,")
        .append(" tb.CDITEMGRADE3,")
        .append(" tb.CDLOCALESTOQUE,");
		if (LavenderePdaConfig.mostraEstoquePrevisto) {
			sql.append(" SUM(tb.QTESTOQUEPREVISTO) QTESTOQUEPREVISTO,")
			.append(" tb.DTESTOQUEPREVISTO,");
		}
		if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
			sql.append(" tb.QTESTOQUEMIN,");
		}
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			sql.append(" tb.QTESTOQUEREMESSA,");
		}
        sql.append(" SUM(ifnull(tb.QTESTOQUE, 0) - ifnull(pda.QTESTOQUE, 0)) QTESTOQUE,")
        .append(" tb.FLORIGEMESTOQUE")
        .append(" FROM TBLVPESTOQUE tb ")
        .append(" LEFT JOIN TBLVPESTOQUE pda ON ")
		.append(" tb.CDEMPRESA = pda.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = pda.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = pda.CDPRODUTO AND ")
		.append(" tb.CDLOCALESTOQUE = pda.CDLOCALESTOQUE AND ")
		.append(" tb.CDITEMGRADE1 = pda.CDITEMGRADE1 AND ")
		.append(" tb.CDITEMGRADE2 = pda.CDITEMGRADE2 AND ")
		.append(" tb.CDITEMGRADE3 = pda.CDITEMGRADE3 AND ")
		.append(" tb.CDUSUARIO = pda.CDUSUARIO AND ")
		.append(" pda.FLORIGEMESTOQUE = 'P'")
        .append(" JOIN TBLVPPRODUTO prod ON ")
        .append(" tb.CDEMPRESA = prod.CDEMPRESA AND ")
        .append(" tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
        .append(" tb.CDPRODUTO = prod.CDPRODUTO AND ")
        .append(" prod.CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario))
        .append(" JOIN TBLVPPRODUTOGRADE pg ON ")
		.append(" tb.CDEMPRESA = pg.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = pg.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = pg.CDPRODUTO AND ")
		.append(" pg.CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
        Produto produtoFilter = filter.getProduto();
        Vector cdMarcadores = SessionLavenderePda.getUltimoProdutoFilter() != null ? SessionLavenderePda.getUltimoProdutoFilter().cdMarcadores : null; 
        if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3() && produtoFilter.itemTabelaPreco != null) {
			DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, DaoUtil.ALIAS_PRODUTO, produtoFilter.itemTabelaPreco, false, true);
			sql.append(" AND ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
		}
        if (LavenderePdaConfig.filtraProdutoPorTipoPedido && ValueUtil.isNotEmpty(produtoFilter.cdTipoPedidoFilter)) {
			DaoUtil.addJoinProdutoTipoPedido(sql, DaoUtil.ALIAS_PRODUTO, produtoFilter);
			sql.append(" AND ").append(DaoUtil.ALIAS_PRODUTOTIPOPED).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
		}
        if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && produtoFilter.cdPlataformaVendaFilter != null) {
			DaoUtil.addJoinPlataformaVendaProduto(sql, produtoFilter.cdPlataformaVendaFilter, DaoUtil.ALIAS_PRODUTO);
			sql.append(" AND ").append(DaoUtil.ALIAS_PLATAFORMAVENDAPRODUTO).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
		}
        if (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() && LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais() && ValueUtil.isNotEmpty(produtoFilter.cdCentroCustoProdFilter)) {
			DaoUtil.addJoinCentroCustoProd(sql, produtoFilter, DaoUtil.ALIAS_PRODUTO);
			sql.append(" AND ").append(DaoUtil.ALIAS_CENTROCUSTOPROD).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
		}
        if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
        	DaoUtil.addJoinProdutoClienteExclusivo(sql, SessionLavenderePda.getCliente().cdCliente, DaoUtil.ALIAS_PRODUTO);
        }
        
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", filter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", filter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", filter.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE1 = ", filter.cdItemGrade1);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE2 = ", filter.cdItemGrade2);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADE3 = ", filter.cdItemGrade3);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE <> ", filter.cdLocalEstoqueDif);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE = ", filter.cdLocalEstoque);
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", filter.flOrigemEstoque);
		//--
		ProdutoBase ultimoProdutoFilter = SessionLavenderePda.getUltimoProdutoFilter();
		if (ultimoProdutoFilter != null && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdStatusEstoque)) {
			addFilterStatusEstoque(sqlWhereClause, ultimoProdutoFilter.cdStatusEstoque);
		}
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido && produtoFilter != null) {
    		DaoUtil.addAndFlExcecaoProdutoTipoPedCondition(produtoFilter, sqlWhereClause);
    	}
        sql.append(sqlWhereClause.getSql());
        addInProdutoCondition(sql);
        sql.append(" AND tb.CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario))
        .append(" AND prod.DSAGRUPADORGRADE = ").append(Sql.getValue(filter.dsAgrupadorGrade));
        if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
        	sql.append(" AND (PRODCLIENTE.FLTIPORELACAO IS NOT NULL OR")
	    	.append(" NOT EXISTS ")
	    	.append("(SELECT 1 FROM TBLVPPRODUTOCLIENTE WHERE")
	    	.append(" CDEMPRESA = TB.CDEMPRESA AND ")
	    	.append(" CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
	    	.append(" CDPRODUTO = TB.CDPRODUTO AND ")
	    	.append(" CDCLIENTE <> ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente))
	    	.append(" AND FLTIPORELACAO = 'X'")
	    	.append("))");
        }
        if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao && ValueUtil.isNotEmpty(cdMarcadores)) {
        	sql.append(" AND EXISTS (")
        	.append(" SELECT 1 FROM TBLVPMARCADOR M ")
    		.append(" JOIN TBLVPMARCADORPRODUTO MP ON ")
    		.append(" M.CDMARCADOR = MP.CDMARCADOR ")
    		.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE()")
    		.append(" AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE() ) AND ")
    		.append(" MP.CDEMPRESA = prod.CDEMPRESA AND ")
    		.append(" MP.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
    		.append(" MP.CDPRODUTO = prod.CDPRODUTO AND ")
    		.append(" MP.CDMARCADOR IN (");
        	int size = cdMarcadores.size();
        	for (int i = 0; i < size; i++) {
        		sql.append(Sql.getValue(cdMarcadores.items[i])).append(",");
        	}
        	sql.setLength(sql.length() - 1);
    		sql.append("))");
        }
        sql.append(" GROUP BY tb.CDEMPRESA, tb.CDREPRESENTANTE, prod.DSAGRUPADORGRADE");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
        	if (rs.next()) {
        		return (Estoque)populate(filter, rs);
        	}
		}
        return null;
	}
	
	public double sumQtEstoqueGrade(Estoque filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT SUM(QTESTOQUE) FROM TBLVPESTOQUE tb")
		.append(" JOIN TBLVPPRODUTO prod ON ")
        .append(" tb.CDEMPRESA = prod.CDEMPRESA AND ")
        .append(" tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
        .append(" tb.CDPRODUTO = prod.CDPRODUTO");
        addWhereByExample(filter, sql);
        sql.append(" AND prod.DSAGRUPADORGRADE = ").append(Sql.getValue(filter.dsAgrupadorGrade));
        return getDouble(sql.toString());
	}
	
	public Vector findEstoqueGradeAgrupador(Estoque filter) throws SQLException {
		StringBuffer sql = getSqlEstoqueAgrupadorGrade(filter, false, false);
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populateEstoqueGradeAgrupador(rs));
			}
			return list;
		}
	}

	private StringBuffer getSqlEstoqueAgrupadorGrade(Estoque filter, boolean sum, boolean filtraDisponibilidade) {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		if (sum) sql.append("SUM(ifnull(tb.QTESTOQUE, 0) - ifnull(pda.QTESTOQUE, 0)) QTESTOQUE");
		else sql.append("pg.CDITEMGRADE1, pg.CDITEMGRADE2, pg.CDITEMGRADE3, tb.CDPRODUTO, ifnull(tb.QTESTOQUE, 0) - ifnull(pda.QTESTOQUE, 0) QTESTOQUE, tb.QTESTOQUEPREVISTO");
		sql.append(" FROM TBLVPESTOQUE tb LEFT JOIN TBLVPESTOQUE pda ON ")
		.append(" tb.CDEMPRESA = pda.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = pda.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = pda.CDPRODUTO AND ")
		.append(" tb.CDLOCALESTOQUE = pda.CDLOCALESTOQUE AND ")
		.append(" tb.CDITEMGRADE1 = pda.CDITEMGRADE1 AND ")
		.append(" tb.CDITEMGRADE2 = pda.CDITEMGRADE2 AND ")
		.append(" tb.CDITEMGRADE3 = pda.CDITEMGRADE3 AND ")
		.append(" pda.FLORIGEMESTOQUE = 'P'")
		.append(" JOIN TBLVPPRODUTO prod ON ")
		.append(" prod.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = prod.CDPRODUTO AND ")
		.append(" prod.CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario))
		.append(" JOIN TBLVPPRODUTOGRADE pg ON ")
		.append(" tb.CDEMPRESA = pg.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = pg.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = pg.CDPRODUTO AND ")
		.append(" pg.CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
		ProdutoBase ultimoProdutoFilter = SessionLavenderePda.getUltimoProdutoFilter();
		if (ultimoProdutoFilter != null) {
			if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3() && ultimoProdutoFilter.itemTabelaPreco != null) {
				DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, DaoUtil.ALIAS_PRODUTO, ultimoProdutoFilter.itemTabelaPreco, false, true);
				sql.append(" AND ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
			}
			if (LavenderePdaConfig.filtraProdutoPorTipoPedido && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdTipoPedidoFilter)) {
				DaoUtil.addJoinProdutoTipoPedido(sql, DaoUtil.ALIAS_PRODUTO, ultimoProdutoFilter);
				sql.append(" AND ").append(DaoUtil.ALIAS_PRODUTOTIPOPED).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
			}
			if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && ultimoProdutoFilter.cdPlataformaVendaFilter != null) {
				DaoUtil.addJoinPlataformaVendaProduto(sql, ultimoProdutoFilter.cdPlataformaVendaFilter, DaoUtil.ALIAS_PRODUTO);
				sql.append(" AND ").append(DaoUtil.ALIAS_PLATAFORMAVENDAPRODUTO).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
			}
			if (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() && LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais() && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdCentroCustoProdFilter)) {
				DaoUtil.addJoinCentroCustoProd(sql, ultimoProdutoFilter, DaoUtil.ALIAS_PRODUTO);
				sql.append(" AND ").append(DaoUtil.ALIAS_CENTROCUSTOPROD).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
			}
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
			DaoUtil.addJoinProdutoClienteExclusivo(sql, SessionLavenderePda.getCliente().cdCliente, DaoUtil.ALIAS_PRODUTO);
		}
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", filter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", filter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE = ", filter.cdLocalEstoque);
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", filter.flOrigemEstoque);
		sqlWhereClause.addAndCondition("pg.CDITEMGRADE1 = ", filter.cdItemGrade1);
		sqlWhereClause.addAndCondition("prod.DSAGRUPADORGRADE = ", filter.dsAgrupadorGrade);
		sqlWhereClause.addAndCondition("tb.CDUSUARIO = ", SessionLavenderePda.usuarioPdaRep.cdUsuario);
		if (ultimoProdutoFilter != null && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdStatusEstoque)) {
			addFilterStatusEstoque(sqlWhereClause, ultimoProdutoFilter.cdStatusEstoque);
		}
		if (ultimoProdutoFilter != null) {
			ProdutoGradeDbxDao.getInstance().addWhereProdutoGrade5(ultimoProdutoFilter, sqlWhereClause, DaoUtil.ALIAS_PRODUTO);
		}
		sql.append(sqlWhereClause.getSql());
		Vector cdMarcadores = ultimoProdutoFilter != null ? ultimoProdutoFilter.cdMarcadores : null;
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
        	sql.append(" AND (PRODCLIENTE.FLTIPORELACAO IS NOT NULL OR")
	    	.append(" NOT EXISTS ")
	    	.append("(SELECT 1 FROM TBLVPPRODUTOCLIENTE WHERE")
	    	.append(" CDEMPRESA = TB.CDEMPRESA AND ")
	    	.append(" CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
	    	.append(" CDPRODUTO = TB.CDPRODUTO AND ")
	    	.append(" CDCLIENTE <> ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente))
	    	.append(" AND FLTIPORELACAO = 'X'")
	    	.append("))");
        }
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao && ValueUtil.isNotEmpty(cdMarcadores)) {
        	sql.append(" AND EXISTS (")
        	.append(" SELECT 1 FROM TBLVPMARCADOR M ")
    		.append(" JOIN TBLVPMARCADORPRODUTO MP ON ")
    		.append(" M.CDMARCADOR = MP.CDMARCADOR ")
    		.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE()")
    		.append(" AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE() ) AND ")
    		.append(" MP.CDEMPRESA = prod.CDEMPRESA AND ")
    		.append(" MP.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
    		.append(" MP.CDPRODUTO = prod.CDPRODUTO AND ")
    		.append(" MP.CDMARCADOR IN (");
        	int size = cdMarcadores.size();
        	for (int i = 0; i < size; i++) {
        		sql.append(Sql.getValue(cdMarcadores.items[i])).append(",");
        	}
        	sql.setLength(sql.length() - 1);
    		sql.append("))");
        }
		return sql;
	}
	
	public double getSumEstoqueAgrupadorGrade(Estoque filter, boolean filtraDisponibilidade) throws SQLException {
		return getDouble(getSqlEstoqueAgrupadorGrade(filter, true, filtraDisponibilidade).toString());
	}
	
	public double getSumEstoquePrevistoAgrupadorGrade(Estoque filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT SUM(tb.QTESTOQUEPREVISTO) FROM TBLVPESTOQUE tb ")
		.append(" JOIN TBLVPPRODUTO prod ON ")
		.append(" prod.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = prod.CDPRODUTO AND ")
		.append(" prod.CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario))
		.append(" JOIN TBLVPPRODUTOGRADE pg ON ")
		.append(" tb.CDEMPRESA = pg.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = pg.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = pg.CDPRODUTO AND ")
		.append(" pg.CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
		ProdutoBase ultimoProdutoFilter = SessionLavenderePda.getUltimoProdutoFilter();
		if (ultimoProdutoFilter != null) {
			if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3() && ultimoProdutoFilter.itemTabelaPreco != null) {
				DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, DaoUtil.ALIAS_PRODUTO, ultimoProdutoFilter.itemTabelaPreco, false, true);
				sql.append(" AND ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
			}
			if (LavenderePdaConfig.filtraProdutoPorTipoPedido && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdTipoPedidoFilter)) {
				DaoUtil.addJoinProdutoTipoPedido(sql, DaoUtil.ALIAS_PRODUTO, ultimoProdutoFilter);
				sql.append(" AND ").append(DaoUtil.ALIAS_PRODUTOTIPOPED).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
			}
			if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && ultimoProdutoFilter.cdPlataformaVendaFilter != null) {
				DaoUtil.addJoinPlataformaVendaProduto(sql, ultimoProdutoFilter.cdPlataformaVendaFilter, DaoUtil.ALIAS_PRODUTO);
				sql.append(" AND ").append(DaoUtil.ALIAS_PLATAFORMAVENDAPRODUTO).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
			}
			if (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() && LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais() && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdCentroCustoProdFilter)) {
				DaoUtil.addJoinCentroCustoProd(sql, ultimoProdutoFilter, DaoUtil.ALIAS_PRODUTO);
				sql.append(" AND ").append(DaoUtil.ALIAS_CENTROCUSTOPROD).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
			}
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
			DaoUtil.addJoinProdutoClienteExclusivo(sql, SessionLavenderePda.getCliente().cdCliente, DaoUtil.ALIAS_PRODUTO);
		}
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", filter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", filter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE = ", filter.cdLocalEstoque);
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", filter.flOrigemEstoque);
		sqlWhereClause.addAndCondition("pg.CDITEMGRADE1 = ", filter.cdItemGrade1);
		sqlWhereClause.addAndCondition("prod.DSAGRUPADORGRADE = ", filter.dsAgrupadorGrade);
		sqlWhereClause.addAndCondition("tb.CDUSUARIO = ", SessionLavenderePda.usuarioPdaRep.cdUsuario);
		if (ultimoProdutoFilter != null) {
			ProdutoGradeDbxDao.getInstance().addWhereProdutoGrade5(ultimoProdutoFilter, sqlWhereClause, "TB");
		}
		sql.append(sqlWhereClause.getSql());
		Vector cdMarcadores = ultimoProdutoFilter != null ? ultimoProdutoFilter.cdMarcadores : null;
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
        	sql.append(" AND (PRODCLIENTE.FLTIPORELACAO IS NOT NULL OR")
	    	.append(" NOT EXISTS ")
	    	.append("(SELECT 1 FROM TBLVPPRODUTOCLIENTE WHERE")
	    	.append(" CDEMPRESA = TB.CDEMPRESA AND ")
	    	.append(" CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
	    	.append(" CDPRODUTO = TB.CDPRODUTO AND ")
	    	.append(" CDCLIENTE <> ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente))
	    	.append(" AND FLTIPORELACAO = 'X'")
	    	.append("))");
        }
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao && ValueUtil.isNotEmpty(cdMarcadores)) {
        	sql.append(" AND EXISTS (")
        	.append(" SELECT 1 FROM TBLVPMARCADOR M ")
    		.append(" JOIN TBLVPMARCADORPRODUTO MP ON ")
    		.append(" M.CDMARCADOR = MP.CDMARCADOR ")
    		.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE()")
    		.append(" AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE() ) AND ")
    		.append(" MP.CDEMPRESA = prod.CDEMPRESA AND ")
    		.append(" MP.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
    		.append(" MP.CDPRODUTO = prod.CDPRODUTO AND ")
    		.append(" MP.CDMARCADOR IN (");
        	int size = cdMarcadores.size();
        	for (int i = 0; i < size; i++) {
        		sql.append(Sql.getValue(cdMarcadores.items[i])).append(",");
        	}
        	sql.setLength(sql.length() - 1);
    		sql.append("))");
        }
		return getDouble(sql.toString());
	}
	
	private Estoque populateEstoqueGradeAgrupador(ResultSet rs) throws SQLException {
		Estoque estoque = new Estoque();
		estoque.cdItemGrade1 = rs.getString(1);
		estoque.cdItemGrade2 = rs.getString(2);
		estoque.cdItemGrade3 = rs.getString(3);
		estoque.cdProduto = rs.getString(4);
		estoque.qtEstoque = rs.getDouble(5);
		estoque.qtEstoquePrevisto = rs.getDouble(6);
		return estoque;
	}
	
	private void addFilterStatusEstoque(SqlWhereClause sqlWhereClause, String cdStatusEstoque) {
		boolean usaMultiplicaCondicaoEstoquePrevisto = false;
		sqlWhereClause.addStartAndMultipleCondition();
		if (cdStatusEstoque.contains(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_1) ) {
			sqlWhereClause.addOrCondition("ifnull("+"tb.QTESTOQUE, 0) - "+"ifnull("+"pda.QTESTOQUE,0) > 0");
			usaMultiplicaCondicaoEstoquePrevisto = true;
		}
		if (cdStatusEstoque.contains(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_2)) {
			sqlWhereClause.addOrCondition("ifnull("+"tb.QTESTOQUE, 0) - "+"ifnull("+"pda.QTESTOQUE,0) <= 0");
			usaMultiplicaCondicaoEstoquePrevisto = true;
		} 
		if (cdStatusEstoque.contains(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_3)) {
			if (usaMultiplicaCondicaoEstoquePrevisto) sqlWhereClause.addStartOrMultipleCondition();	
			sqlWhereClause.addAndCondition("tb.QTESTOQUEPREVISTO > 0");
			sqlWhereClause.addAndCondition("tb.DTESTOQUEPREVISTO IS NOT NULL");
			sqlWhereClause.addAndCondition("tb.DTESTOQUEPREVISTO >= " , DateUtil.getCurrentDate());
			if (usaMultiplicaCondicaoEstoquePrevisto) sqlWhereClause.addEndMultipleCondition();
		}
		sqlWhereClause.addEndMultipleCondition();
	}
	
	public Estoque getSumEstoqueEstoquePrevistoGradeProduto(Estoque filter) throws SQLException {
		StringBuffer sql = new StringBuffer();
    	sql.append("SELECT sum(QTESTOQUE) QTESTOQUE, sum(QTESTOQUEPREVISTO) QTESTOQUEPREVISTO FROM ")
    	.append(tableName).append(" tb ");
    	addWhereByExample(filter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		if (rs.next()) {
    			Estoque estoque = new Estoque();
    			estoque.qtEstoque = rs.getDouble("QTESTOQUE");
    			estoque.qtEstoquePrevisto = rs.getDouble("QTESTOQUEPREVISTO");
    			return estoque;
    		}
    		return null;
		}
	}
	
	public Estoque findEstoqueLimitOne(Estoque filter) throws SQLException {
		filter.limit = 1;
		Vector list = findAllByExample(filter);
		if (ValueUtil.isNotEmpty(list)) {
			return (Estoque) list.items[0];
		}
		return null;
	}

}
