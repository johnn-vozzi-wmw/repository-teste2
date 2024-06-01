package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorGrupoProduto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class MetasPorGrupoProdutoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetasPorGrupoProduto();
	}

    private static MetasPorGrupoProdutoPdbxDao instance;

    public MetasPorGrupoProdutoPdbxDao() {
        super(MetasPorGrupoProduto.TABLE_NAME);
    }

    public static MetasPorGrupoProdutoPdbxDao getInstance() {
        if (instance == null) {
            instance = new MetasPorGrupoProdutoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }


    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	MetasPorGrupoProduto metasPorGrupoProduto = (MetasPorGrupoProduto) domain;
		//--
    	sql.append(" VLREALIZADO = ").append(Sql.getValue(metasPorGrupoProduto.vlRealizado));
    }

    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MetasPorGrupoProduto metasPorGrupoProduto = new MetasPorGrupoProduto();
        metasPorGrupoProduto.rowKey = rs.getString("tb.rowkey");
    	metasPorGrupoProduto.cdGrupoProduto1 = rs.getString("CDGRUPOPRODUTO1");
    	metasPorGrupoProduto.dsGrupoProduto1 = rs.getString("DSGRUPOPRODUTO1");
        metasPorGrupoProduto.cdGrupoProduto2 = rs.getString("CDGRUPOPRODUTO2");
        metasPorGrupoProduto.cdGrupoProduto3 = rs.getString("CDGRUPOPRODUTO3");
        metasPorGrupoProduto.cdProduto = rs.getString("CDPRODUTO");
        metasPorGrupoProduto.dtInicial = rs.getDate("DTINICIAL");
        metasPorGrupoProduto.dtFinal = rs.getDate("DTFINAL");
        metasPorGrupoProduto.vlMeta = rs.getDouble("vlMeta");
        metasPorGrupoProduto.vlRealizado = rs.getDouble("VlRealizado");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado() || LavenderePdaConfig.usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd) {
        	metasPorGrupoProduto.qtUnidadeMeta = rs.getDouble("qtUnidadeMeta");
        	metasPorGrupoProduto.qtUnidadeRealizado = rs.getDouble("qtUnidadeRealizado");
        	metasPorGrupoProduto.qtCaixaMeta = rs.getDouble("qtCaixaMeta");
        	metasPorGrupoProduto.qtCaixaRealizado = rs.getDouble("qtCaixaRealizado");
        	metasPorGrupoProduto.qtMixClienteMeta = rs.getDouble("qtMixClienteMeta");
        	metasPorGrupoProduto.qtMixClienteRealizado = rs.getDouble("qtMixClienteRealizado");
        }
        return metasPorGrupoProduto;
	}

	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" tb.rowkey,");
        if (LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd) {
        	sql.append(" tb.CDGRUPOPRODUTO1,");
    		sql.append(" tb.DSGRUPOPRODUTO1,");
    	} else {
    		sql.append(" p.CDGRUPOPRODUTO1,");
    		sql.append(" p.DSGRUPOPRODUTO1,");
    	}
        sql.append(" tb.CDGRUPOPRODUTO2,");
        sql.append(" tb.CDGRUPOPRODUTO3,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.DTINICIAL,");
        sql.append(" tb.DTFINAL,");
        sql.append(" tb.VlMeta,");
        sql.append(" tb.VlRealizado");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado() || LavenderePdaConfig.usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd) {
            sql.append(" , tb.qtUnidadeMeta,");
            sql.append(" tb.qtUnidadeRealizado,");
        	sql.append(" tb.qtCaixaMeta,");
        	sql.append(" tb.qtCaixaRealizado,");
        	sql.append(" tb.qtMixClienteMeta,");
        	sql.append(" tb.qtMixClienteRealizado");
        }
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" CDPRODUTO,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
        sql.append(" VlMeta,");
        sql.append(" VlRealizado,");
        sql.append(" qtUnidadeMeta,");
        sql.append(" qtUnidadeRealizado,");
        sql.append(" DSGRUPOPRODUTO1,");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado() || LavenderePdaConfig.usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd) {
        	sql.append(" qtCaixaMeta,");
        	sql.append(" qtCaixaRealizado,");
        	sql.append(" qtMixClienteMeta,");
        	sql.append(" qtMixClienteRealizado,");
        }
        sql.append(" DSPERIODO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MetasPorGrupoProduto metasPorGrupoProduto = new MetasPorGrupoProduto();
        metasPorGrupoProduto.rowKey = rs.getString("rowkey");
        metasPorGrupoProduto.cdEmpresa = rs.getString("cdEmpresa");
        metasPorGrupoProduto.cdRepresentante = rs.getString("cdRepresentante");
        metasPorGrupoProduto.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        metasPorGrupoProduto.cdGrupoProduto2 = rs.getString("CDGRUPOPRODUTO2");
        metasPorGrupoProduto.cdGrupoProduto3 = rs.getString("CDGRUPOPRODUTO3");
        metasPorGrupoProduto.cdProduto = rs.getString("CDPRODUTO");
        metasPorGrupoProduto.dtInicial = rs.getDate("DTINICIAL");
        metasPorGrupoProduto.dtFinal = rs.getDate("DTFINAL");
        metasPorGrupoProduto.vlMeta = ValueUtil.round(rs.getDouble("vlMeta"));
        metasPorGrupoProduto.vlRealizado = ValueUtil.round(rs.getDouble("vlRealizado"));
        metasPorGrupoProduto.qtUnidadeMeta = ValueUtil.round(rs.getDouble("qtUnidademeta"));
        metasPorGrupoProduto.qtUnidadeRealizado = ValueUtil.round(rs.getDouble("qtUnidadeRealizado"));
        metasPorGrupoProduto.dsGrupoProduto1 = rs.getString("dsGrupoProduto1");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado() || LavenderePdaConfig.usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd) {
        	metasPorGrupoProduto.qtCaixaMeta = ValueUtil.round(rs.getDouble("qtCaixameta"));
        	metasPorGrupoProduto.qtCaixaRealizado = ValueUtil.round(rs.getDouble("qtCaixaRealizado"));
        	metasPorGrupoProduto.qtMixClienteMeta = ValueUtil.round(rs.getDouble("qtMixclientemeta"));
        	metasPorGrupoProduto.qtMixClienteRealizado = ValueUtil.round(rs.getDouble("qtMixClienteRealizado"));
        }
        metasPorGrupoProduto.dsPeriodo = rs.getString("dsPeriodo");
        return metasPorGrupoProduto;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
       	MetasPorGrupoProduto metasPorGrupoProduto = (MetasPorGrupoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (metasPorGrupoProduto.findDescGrupoProduto1 && !LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd) {
			sqlWhereClause.addJoin(", TBLVPGRUPOPRODUTO1 p WHERE tb.cdGrupoProduto1 = p.cdGrupoProduto1");
		}
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", metasPorGrupoProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", metasPorGrupoProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDGRUPOPRODUTO1 = ", metasPorGrupoProduto.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("tb.CDGRUPOPRODUTO2 = ", metasPorGrupoProduto.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("tb.CDGRUPOPRODUTO3 = ", metasPorGrupoProduto.cdGrupoProduto3);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO != ", metasPorGrupoProduto.cdProdutoDif);
		sqlWhereClause.addAndCondition("tb.DTINICIAL = ", metasPorGrupoProduto.dtInicial);
		sqlWhereClause.addAndCondition("tb.DTFINAL = ", metasPorGrupoProduto.dtFinal);
		sqlWhereClause.addAndCondition("tb.DSPERIODO = ", metasPorGrupoProduto.dsPeriodo);
		sql.append(sqlWhereClause.getSql());
		//--
		if (!ValueUtil.isEmpty(metasPorGrupoProduto.cdGrupoProduto1Like)) {
			String prefixo = LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd ? "tb." : "p.";
			if (ValueUtil.isValidNumber(metasPorGrupoProduto.cdGrupoProduto1Like)) {
				sql.append(" AND ( ").append(prefixo).append("CDGRUPOPRODUTO1 = ");
				sql.append(Sql.getValue(metasPorGrupoProduto.cdGrupoProduto1Like));
				sql.append(" OR ").append(prefixo).append("DSGRUPOPRODUTO1 LIKE ");
				sql.append(Sql.getValue("%" + metasPorGrupoProduto.cdGrupoProduto1Like + "%"));
				sql.append(" ) ");
			} else {
				sql.append(" AND ").append(prefixo).append("DSGRUPOPRODUTO1 LIKE ");
				sql.append(Sql.getValue("%" + metasPorGrupoProduto.cdGrupoProduto1Like + "%"));
			}
       	}
    }

    protected void addSelectGridColumns(StringBuffer sql, BaseDomain domain) {
    	sql.append(" tb.rowkey,");
        if (LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd) {
        	sql.append(" tb.CDGRUPOPRODUTO1,");
    		sql.append(" tb.DSGRUPOPRODUTO1,");
    	} else {
    		sql.append(" p.CDGRUPOPRODUTO1,");
    		sql.append(" p.DSGRUPOPRODUTO1,");
    	}
        sql.append(" tb.CDGRUPOPRODUTO2,");
        sql.append(" tb.CDGRUPOPRODUTO3,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.DTINICIAL,");
        sql.append(" tb.DTFINAL,");
        sql.append(" tb.VlMeta,");
        sql.append(" tb.VlRealizado");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado() || LavenderePdaConfig.usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd) {
            sql.append(" , tb.qtUnidadeMeta,");
            sql.append(" tb.qtUnidadeRealizado,");
        	sql.append(" tb.qtCaixaMeta,");
        	sql.append(" tb.qtCaixaRealizado,");
        	sql.append(" tb.qtMixClienteMeta,");
        	sql.append(" tb.qtMixClienteRealizado");
        }
    }

    protected void addOrderByGrid(StringBuffer sql) {
    	String orderBy = LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos ? "CDGRUPOPRODUTO1" : "DSGRUPOPRODUTO1";
    	if (LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd) {
    		sql.append(" order by tb.").append(orderBy);
    	} else {
    		sql.append(" order by p.").append(orderBy);
    	}
    }

    public Vector findAllMetasByGrupoProduto(BaseDomain domain, int nuNivel) throws SQLException {
    	MetasPorGrupoProduto metasPorGrupoProduto = (MetasPorGrupoProduto) domain;
    	boolean groupByGrupoProduto2 = nuNivel == 2;
    	boolean groupByGrupoProduto3 = nuNivel == 3;
    	boolean groupByProduto = nuNivel == 4;
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	sql.append(" tb.CDGRUPOPRODUTO1, ");
    	if (groupByProduto) {
			sql.append(" tb.CDGRUPOPRODUTO2, ");
			sql.append(" tb.CDGRUPOPRODUTO3, ");
			sql.append(" tb.CDPRODUTO, ");
		} else if (groupByGrupoProduto3) {
			sql.append(" tb.CDGRUPOPRODUTO2, ");
			sql.append(" tb.CDGRUPOPRODUTO3, ");
		} else if (groupByGrupoProduto2) {
			sql.append(" tb.CDGRUPOPRODUTO2, ");
		} else {
	    	sql.append(" tb.DSGRUPOPRODUTO1, ");
		}
    	sql.append(" tb.DTINICIAL, ");
    	sql.append(" tb.DTFINAL, ");
    	sql.append(" SUM(tb.VLMETA)as vlMeta, ");
    	sql.append(" SUM(tb.VLREALIZADO)as vlRealizado, ");
    	sql.append(" SUM(tb.qtUnidadeMeta) as qtUnidadeMeta, ");
    	sql.append(" SUM(tb.qtCaixaMeta) as qtCaixaMeta, ");
    	sql.append(" SUM(tb.qtMixClienteMeta) as qtMixClienteMeta, ");
    	sql.append(" SUM(tb.qtUnidadeRealizado) as qtUnidadeRealizado, ");
    	sql.append(" SUM(tb.qtCaixaRealizado) as qtCaixaRealizado, ");
    	sql.append(" SUM(tb.qtMixClienteRealizado) as qtMixClienteRealizado ");
    	sql.append(" from ").append(tableName).append(" tb ").append(" where ");
    	//--
    	sql.append(" tb.DSPERIODO = " + Sql.getValue(metasPorGrupoProduto.dsPeriodo));
    	sql.append(" and tb.cdEmpresa = " + Sql.getValue(metasPorGrupoProduto.cdEmpresa));
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdRepresentante)) {
    		sql.append(" and tb.cdRepresentante = " + Sql.getValue(metasPorGrupoProduto.cdRepresentante));
    	}
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdGrupoProduto1)) {
    		sql.append(" and tb.CDGRUPOPRODUTO1 = " + Sql.getValue(metasPorGrupoProduto.cdGrupoProduto1));
    	}
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdGrupoProduto2)) {
    		sql.append(" and tb.CDGRUPOPRODUTO2 = " + Sql.getValue(metasPorGrupoProduto.cdGrupoProduto2));
    	}
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdGrupoProduto3)) {
    		sql.append(" and tb.CDGRUPOPRODUTO3 = " + Sql.getValue(metasPorGrupoProduto.cdGrupoProduto3));
    	}
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdProduto)) {
    		sql.append(" and tb.CDPRODUTO = " + Sql.getValue(metasPorGrupoProduto.cdProduto));
    	}
    	//--
		sql.append(" group by tb.CDGRUPOPRODUTO1");

		if (groupByProduto) {
			sql.append(", tb.CDGRUPOPRODUTO2 ");
			sql.append(", tb.CDGRUPOPRODUTO3 ");
			sql.append(", tb.CDPRODUTO ");
    	} else if (groupByGrupoProduto3) {
			sql.append(", tb.CDGRUPOPRODUTO2 ");
			sql.append(", tb.CDGRUPOPRODUTO3 ");
		} else if (groupByGrupoProduto2) {
			sql.append(", tb.CDGRUPOPRODUTO2 ");
		} else {
			sql.append(", tb.DSGRUPOPRODUTO1 ");
		}
		//--
		sql.append(" order by tb.NUSEQUENCIA, TB.DSGRUPOPRODUTO1, TB.CDGRUPOPRODUTO1");
		if (groupByGrupoProduto3) {
			sql.append(", TB.CDGRUPOPRODUTO2");
			sql.append(", TB.CDGRUPOPRODUTO3");
	    } else if (groupByGrupoProduto2) {
			sql.append(", TB.CDGRUPOPRODUTO2");
	    }
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				MetasPorGrupoProduto metasGrupoProd = new MetasPorGrupoProduto();
				metasGrupoProd.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
				if (groupByProduto) {
					metasGrupoProd.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
					metasGrupoProd.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
					metasGrupoProd.cdProduto = rs.getString("cdProduto");
		    	} else if (groupByGrupoProduto3) {
					metasGrupoProd.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
					metasGrupoProd.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
				} else if (groupByGrupoProduto2) {
					metasGrupoProd.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
				} else {
					metasGrupoProd.dsGrupoProduto1 = rs.getString("dsGrupoProduto1");
				}
				metasGrupoProd.vlMeta = rs.getDouble("vlMeta");
				metasGrupoProd.vlRealizado = rs.getDouble("vlRealizado");
				metasGrupoProd.qtUnidadeMeta = rs.getDouble("qtUnidadeMeta");
				metasGrupoProd.qtCaixaMeta = rs.getDouble("qtCaixaMeta");
				metasGrupoProd.qtMixClienteMeta = rs.getDouble("qtMixClienteMeta");
				metasGrupoProd.qtUnidadeRealizado = rs.getDouble("qtUnidadeRealizado");
				metasGrupoProd.qtCaixaRealizado = rs.getDouble("qtCaixaRealizado");
				metasGrupoProd.qtMixClienteRealizado = rs.getDouble("qtMixClienteRealizado");
				metasGrupoProd.dtInicial = rs.getDate("dtInicial");
				metasGrupoProd.dtFinal = rs.getDate("dtFinal");
				result.addElement(metasGrupoProd);
			}
			return result;
		}
    }

    public Vector findMetasByGrupoProduto(BaseDomain domain, int nuNivel) throws SQLException {
    	MetasPorGrupoProduto metasPorGrupoProduto = (MetasPorGrupoProduto) domain;
    	boolean groupByGrupoProduto2 = nuNivel == 2;
    	boolean groupByGrupoProduto3 = nuNivel == 3;
    	boolean groupByProduto = nuNivel == 4;
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	sql.append(" tb.CDGRUPOPRODUTO1, ");
    	if (groupByProduto) {
			sql.append(" tb.CDGRUPOPRODUTO2, ");
			sql.append(" tb.CDGRUPOPRODUTO3, ");
			sql.append(" tb.CDPRODUTO, ");
		} else if (groupByGrupoProduto3) {
			sql.append(" tb.CDGRUPOPRODUTO2, ");
			sql.append(" tb.CDGRUPOPRODUTO3, ");
			sql.append(" grupo.DSGRUPOPRODUTO3,");
		} else if (groupByGrupoProduto2) {
			sql.append(" tb.CDGRUPOPRODUTO2, ");
			sql.append(" grupo.DSGRUPOPRODUTO2,");
		} else {
			if (LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd) {
	    		sql.append(" tb.DSGRUPOPRODUTO1,");
	    	} else {
	    		sql.append(" grupo.DSGRUPOPRODUTO1,");
	    	}
		}
    	sql.append(" tb.DTINICIAL, ");
    	sql.append(" tb.DTFINAL, ");
    	sql.append(" SUM(tb.VLMETA)as vlMeta, ");
    	sql.append(" SUM(tb.VLREALIZADO)as vlRealizado, ");
    	sql.append(" SUM(tb.qtUnidadeMeta) as qtUnidadeMeta, ");
    	sql.append(" SUM(tb.qtCaixaMeta) as qtCaixaMeta, ");
    	sql.append(" SUM(tb.qtMixClienteMeta) as qtMixClienteMeta, ");
    	sql.append(" SUM(tb.qtUnidadeRealizado) as qtUnidadeRealizado, ");
    	sql.append(" SUM(tb.qtCaixaRealizado) as qtCaixaRealizado, ");
    	sql.append(" SUM(tb.qtMixClienteRealizado) as qtMixClienteRealizado ");
    	sql.append(" from ").append(tableName).append(" tb ");
    	//--
    	if (groupByProduto) {
    		sql.append(" where ");
    	} else if (groupByGrupoProduto3) {
			sql.append(", TBLVPGRUPOPRODUTO3 grupo WHERE tb.cdGrupoProduto1 = grupo.cdGrupoProduto1 and tb.cdGrupoProduto2 = grupo.cdGrupoProduto2 and tb.cdGrupoProduto3 = grupo.cdGrupoProduto3 and ");
		} else if (groupByGrupoProduto2) {
			sql.append(", TBLVPGRUPOPRODUTO2 grupo WHERE tb.cdGrupoProduto1 = grupo.cdGrupoProduto1 and tb.cdGrupoProduto2 = grupo.cdGrupoProduto2 and ");
		} else {
			if (LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd) {
				sql.append(" where ");
			} else {
				sql.append(", TBLVPGRUPOPRODUTO1 grupo WHERE tb.cdGrupoProduto1 = grupo.cdGrupoProduto1 and ");
			}
		}
    	sql.append(" tb.DSPERIODO = " + Sql.getValue(metasPorGrupoProduto.dsPeriodo));
    	sql.append(" and tb.cdEmpresa = " + Sql.getValue(metasPorGrupoProduto.cdEmpresa));
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdRepresentante)) {
    		sql.append(" and tb.cdRepresentante = " + Sql.getValue(metasPorGrupoProduto.cdRepresentante));
    	}
    	if (!ValueUtil.isEmpty(metasPorGrupoProduto.cdGrupoProduto1Like)) {
    		String prefixo = LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd ? "tb." : "grupo.";
    		if (ValueUtil.isValidNumber(metasPorGrupoProduto.cdGrupoProduto1Like)) {
				sql.append(" AND ( ").append(prefixo).append("CDGRUPOPRODUTO1 = ");
				sql.append(Sql.getValue(metasPorGrupoProduto.cdGrupoProduto1Like));
				sql.append(" OR ").append(prefixo).append("DSGRUPOPRODUTO1 LIKE ");
				sql.append(Sql.getValue("%" + metasPorGrupoProduto.cdGrupoProduto1Like + "%"));
				sql.append(" ) ");
			} else {
				sql.append(" AND ").append(prefixo).append("DSGRUPOPRODUTO1 LIKE ");
				sql.append(Sql.getValue("%" + metasPorGrupoProduto.cdGrupoProduto1Like + "%"));
			}
    	}
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdGrupoProduto1)) {
    		sql.append(" and tb.CDGRUPOPRODUTO1 = " + Sql.getValue(metasPorGrupoProduto.cdGrupoProduto1));
    	}
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdGrupoProduto2)) {
    		sql.append(" and tb.CDGRUPOPRODUTO2 = " + Sql.getValue(metasPorGrupoProduto.cdGrupoProduto2));
    	}
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdGrupoProduto3)) {
    		sql.append(" and tb.CDGRUPOPRODUTO3 = " + Sql.getValue(metasPorGrupoProduto.cdGrupoProduto3));
    	}
    	if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdProduto)) {
    		sql.append(" and tb.CDPRODUTO = " + Sql.getValue(metasPorGrupoProduto.cdProduto));
    	}
    	//--
		sql.append(" group by tb.CDGRUPOPRODUTO1");

		if (groupByProduto) {
			sql.append(", tb.CDGRUPOPRODUTO2 ");
			sql.append(", tb.CDGRUPOPRODUTO3 ");
			sql.append(", tb.CDPRODUTO ");
    	} else if (groupByGrupoProduto3) {
			sql.append(", tb.CDGRUPOPRODUTO2 ");
			sql.append(", tb.CDGRUPOPRODUTO3 ");
			sql.append(", grupo.DSGRUPOPRODUTO3");
		} else if (groupByGrupoProduto2) {
			sql.append(", tb.CDGRUPOPRODUTO2 ");
			sql.append(", grupo.DSGRUPOPRODUTO2");
		} else {
			if (LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd) {
				sql.append(", tb.DSGRUPOPRODUTO1 ");
			} else {
				sql.append(", grupo.DSGRUPOPRODUTO1");
			}
		}
		//--
	    sql.append(" order by tb.NUSEQUENCIA, TB.DSGRUPOPRODUTO1, TB.CDGRUPOPRODUTO1");
	    if (groupByGrupoProduto3) {
		    sql.append(", TB.DSGRUPOPRODUTO2, TB.CDGRUPOPRODUTO2");
		    sql.append(", TB.DSGRUPOPRODUTO3, TB.CDGRUPOPRODUTO3");
	    } else if (groupByGrupoProduto2) {
		    sql.append(", TB.DSGRUPOPRODUTO2, TB.CDGRUPOPRODUTO2");
	    }
	    try (Statement st = getCurrentDriver().getStatement();
	    		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				MetasPorGrupoProduto metasGrupoProd = new MetasPorGrupoProduto();
				metasGrupoProd.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
				if (groupByProduto) {
					metasGrupoProd.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
					metasGrupoProd.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
					metasGrupoProd.cdProduto = rs.getString("cdProduto");
		    	} else if (groupByGrupoProduto3) {
					metasGrupoProd.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
					metasGrupoProd.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
					metasGrupoProd.dsGrupoProduto3 = rs.getString("dsGrupoProduto3");
				} else if (groupByGrupoProduto2) {
					metasGrupoProd.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
					metasGrupoProd.dsGrupoProduto2 = rs.getString("dsGrupoProduto2");
				} else {
					metasGrupoProd.dsGrupoProduto1 = rs.getString("dsGrupoProduto1");
				}
				metasGrupoProd.vlMeta = rs.getDouble("vlMeta");
				metasGrupoProd.vlRealizado = rs.getDouble("vlRealizado");
				metasGrupoProd.qtUnidadeMeta = rs.getDouble("qtUnidadeMeta");
				metasGrupoProd.qtCaixaMeta = rs.getDouble("qtCaixaMeta");
				metasGrupoProd.qtMixClienteMeta = rs.getDouble("qtMixClienteMeta");
				metasGrupoProd.qtUnidadeRealizado = rs.getDouble("qtUnidadeRealizado");
				metasGrupoProd.qtCaixaRealizado = rs.getDouble("qtCaixaRealizado");
				metasGrupoProd.qtMixClienteRealizado = rs.getDouble("qtMixClienteRealizado");
				metasGrupoProd.dtInicial = rs.getDate("dtInicial");
				metasGrupoProd.dtFinal = rs.getDate("dtFinal");
				result.addElement(metasGrupoProd);
			}
			return result;
		}
    }

    //@Override
    public Vector findAllDistinctPeriodo(MetasPorGrupoProduto metasPorGrupoProdutoFilter) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select DISTINCT DSPERIODO from ");
        sql.append(tableName);
        sql.append(" where cdEmpresa = " + Sql.getValue(metasPorGrupoProdutoFilter.cdEmpresa));
        //--
        if (LavenderePdaConfig.usaSistemaModoOffLine) {
        	Date dataAtual = DateUtil.getCurrentDate();
        	sql.append(" AND ((");
        	sql.append(" dtInicial <= " + Sql.getValue(dataAtual)).append(" AND ");
        	if (LavenderePdaConfig.usaNuDiasPermanenciaMetas()) {
        		DateUtil.decDay(dataAtual, LavenderePdaConfig.nuDiasPermanenciaMetas);
        	}
        	sql.append(" dtFinal >= " + Sql.getValue(dataAtual));
        	sql.append(")");
        	sql.append(" OR (");
        	sql.append(" dtFinal  = " + Sql.getValue(DateUtil.DATE_FOR_NULL_VALUE_SQL)).append(" OR ");
        	sql.append(" dtInicial  = " + Sql.getValue(DateUtil.DATE_FOR_NULL_VALUE_SQL));
        	sql.append(")) ");
        }
		//--
        sql.append(" order by NUSEQUENCIA");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
		        result.addElement(rs.getString("DSPERIODO"));
			}
			return result;
		}
    }

	public Vector findAllMetasVigentes(MetasPorGrupoProduto metasPorGrupoProdutoFilter) throws SQLException {
		Date dataAtual = DateUtil.getCurrentDate();
		StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        sql.append(" where cdEmpresa = ").append(Sql.getValue(metasPorGrupoProdutoFilter.cdEmpresa)).append(" and cdRepresentante = ").append(Sql.getValue(metasPorGrupoProdutoFilter.cdRepresentante));
        sql.append(" and dtInicial <= ").append(Sql.getValue(dataAtual)).append(" and dtFinal >= ").append(Sql.getValue(dataAtual));
        if (ValueUtil.isNotEmpty(metasPorGrupoProdutoFilter.cdGrupoProduto1)) {
        	sql.append(" and cdGrupoProduto1 = ").append(Sql.getValue(metasPorGrupoProdutoFilter.cdGrupoProduto1));
        }
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector listMeta = new Vector();
			while (rs.next()) {
				MetasPorGrupoProduto metasPorGrupoProduto = new MetasPorGrupoProduto();
				metasPorGrupoProduto.dsPeriodo = rs.getString("DSPERIODO");
				metasPorGrupoProduto.dtInicial = rs.getDate("DTINICIAL");
				metasPorGrupoProduto.dtFinal = rs.getDate("DTFINAL");
				metasPorGrupoProduto.cdGrupoProduto1 = rs.getString("CDGRUPOPRODUTO1");
				metasPorGrupoProduto.cdGrupoProduto2 = rs.getString("CDGRUPOPRODUTO2");
				metasPorGrupoProduto.cdGrupoProduto3 = rs.getString("CDGRUPOPRODUTO3");
				metasPorGrupoProduto.cdProduto = rs.getString("CDPRODUTO");
				metasPorGrupoProduto.qtUnidadeMeta = rs.getDouble("QTUNIDADEMETA");
				metasPorGrupoProduto.vlRealizado = rs.getDouble("VLREALIZADO");
				metasPorGrupoProduto.cdEmpresa = rs.getString("CDEMPRESA");
				metasPorGrupoProduto.cdRepresentante = rs.getString("CDREPRESENTANTE");
				metasPorGrupoProduto.rowKey = rs.getString("ROWKEY");
				metasPorGrupoProduto.cdUsuario = rs.getString("CDUSUARIO");
				listMeta.addElement(metasPorGrupoProduto);
			}
			return listMeta;
		}
	}

}